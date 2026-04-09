package visitor;

import calculator.*;
import calculator.enums.Notation;
import calculator.enums.NumberDomain;
import calculator.exceptions.IllegalConstruction;
import calculator.operations.*;
import calculator.operations.unaryoperations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CalculatorVisitorImpl extends CalculatorBaseVisitor<Expression> {

    @Override
    public Expression visitProg(CalculatorParser.ProgContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expression visitParens(CalculatorParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expression visitPrefix(CalculatorParser.PrefixContext ctx) {
        List<Expression> args = new ArrayList<>();
        for (CalculatorParser.ExprContext exprCtx : ctx.exprList().expr()) {
            args.add(visit(exprCtx));
        }
        return createOperation(ctx.op.getText(), args, Notation.PREFIX);
    }

    @Override
    public Expression visitPostfix(CalculatorParser.PostfixContext ctx) {
        List<Expression> args = new ArrayList<>();
        for (CalculatorParser.ExprContext exprCtx : ctx.exprList().expr()) {
            args.add(visit(exprCtx));
        }
        return createOperation(ctx.op.getText(), args, Notation.POSTFIX);
    }

    @Override
    public Expression visitMulDiv(CalculatorParser.MulDivContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        List<Expression> args = new ArrayList<>();
        Collections.addAll(args, left, right);
        return createOperation(ctx.op.getText(), args, Notation.INFIX);
    }

    @Override
    public Expression visitAddSub(CalculatorParser.AddSubContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        List<Expression> args = new ArrayList<>();
        Collections.addAll(args, left, right);
        return createOperation(ctx.op.getText(), args, Notation.INFIX);
    }

    @Override
    public Expression visitNum(CalculatorParser.NumContext ctx) {
        String text = ctx.NUMBER().getText();
        if (Main.getCurrentDomain() == NumberDomain.COMPLEX) {
            return new MyNumber(new calculator.value.ComplexValue(Double.parseDouble(text), 0));
        } else if (Main.getCurrentDomain() == NumberDomain.REAL) {
            return new MyNumber(new calculator.value.RealValue(text));
        } else if (Main.getCurrentDomain() == NumberDomain.RATIONAL) {
            if (text.contains(".") || text.toLowerCase(Locale.ROOT).contains("e")) {
                return new MyNumber(new calculator.value.RealValue(text));
            }
            return new MyNumber(new calculator.value.RationalValue(Integer.parseInt(text)));
        } else {
            // NumberDomain.INTEGER or default
            double d = Double.parseDouble(text);
            if (d > Integer.MAX_VALUE || d < Integer.MIN_VALUE) {
                throw new IllegalArgumentException("Number out of integer range: " + text);
            }
            int val = (int) d;
            return new MyNumber(new calculator.value.IntegerValue(val));
        }
    }

    @Override
    public Expression visitConst(CalculatorParser.ConstContext ctx) {
        return new MyConstant(ctx.CONSTANT().getText());
    }

    @Override
    public Expression visitAbs(CalculatorParser.AbsContext ctx) {
        Expression arg = visit(ctx.expr());
        return new Abs(java.util.List.of(arg));
        
    }

    @Override
    public Expression visitFactorial(CalculatorParser.FactorialContext ctx) {
        Expression arg = visit(ctx.expr());
        return new Factorial(java.util.List.of(arg), Notation.POSTFIX);
    }

    @Override
    public Expression visitFunc(CalculatorParser.FuncContext ctx) {
        Expression arg = visit(ctx.expr());
        String func = ctx.func.getText();
        switch(func) {
            case "sin": return new Sin(java.util.List.of(arg), Notation.PREFIX);
            case "cos": return new Cos(java.util.List.of(arg), Notation.PREFIX);
            case "tan": return new Tan(java.util.List.of(arg), Notation.PREFIX);
            case "sinh": return new Sinh(java.util.List.of(arg), Notation.PREFIX);
            case "cosh": return new Cosh(java.util.List.of(arg), Notation.PREFIX);
            case "tanh": return new Tanh(java.util.List.of(arg), Notation.PREFIX);
            case "arcsin": return new ArcSin(java.util.List.of(arg), Notation.PREFIX);
            case "arccos": return new ArcCos(java.util.List.of(arg), Notation.PREFIX);
            case "arctan": return new ArcTan(java.util.List.of(arg), Notation.PREFIX);
            case "ln": return new Ln(java.util.List.of(arg), Notation.PREFIX);
            case "log": return new Log(java.util.List.of(arg), Notation.PREFIX);
            case "random": return new RandomOp(java.util.List.of(arg), Notation.PREFIX);
            default: throw new IllegalArgumentException("Unknown func: " + func);
        }   
    }

    @Override
    public Expression visitRandomNoArg(CalculatorParser.RandomNoArgContext ctx) {
        // default argument = 1
        Expression defaultArg = new MyNumber(new calculator.value.IntegerValue(1));
        return new RandomOp(java.util.List.of(defaultArg), Notation.PREFIX);
    }

    @Override
    public Expression visitImplicitMul(CalculatorParser.ImplicitMulContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        List<Expression> args = new ArrayList<>();
        Collections.addAll(args, left, right);
        return createOperation("*", args, Notation.INFIX);
    }

    @Override
    public Expression visitPower(CalculatorParser.PowerContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        List<Expression> args = new ArrayList<>();
        Collections.addAll(args, left, right);
        return createOperation("**", args, Notation.INFIX);
    }

    @Override
    public Expression visitUnaryMinus(CalculatorParser.UnaryMinusContext ctx) {
        Expression right = visit(ctx.expr());
        Expression zero;
        if (Main.getCurrentDomain() == NumberDomain.COMPLEX) {
            zero = new MyNumber(new calculator.value.ComplexValue(0, 0));
        } else if (Main.getCurrentDomain() == NumberDomain.REAL) {
            zero = new MyNumber(new calculator.value.RealValue(0.0));
        } else if (Main.getCurrentDomain() == NumberDomain.RATIONAL) {
            zero = new MyNumber(new calculator.value.RationalValue(0, 1));
        } else {
            zero = new MyNumber(new calculator.value.IntegerValue(0));
        }
        List<Expression> args = new ArrayList<>();
        Collections.addAll(args, zero, right);
        return createOperation("-", args, Notation.INFIX);
    }

    private Expression createOperation(String op, List<Expression> args, Notation notation) {
        try {
            switch (op) {
                case "+": return new Plus(args, notation);
                case "-": return new Minus(args, notation);
                case "*": return new Times(args, notation);
                case "/": return new Divides(args, notation);
                case "**": return new Power(args, notation);
                case "mod": return new Mod(args, notation);
                case "//": return new IntDiv(args, notation);
                default: throw new IllegalArgumentException("Unknown operator: " + op);
            }
        } catch (IllegalConstruction e) {
            throw new IllegalConstruction("Invalid operation construction", e);
        }
    }
}
