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
        } else if (Main.getCurrentDomain() == NumberDomain.REAL || text.contains(".") || text.toLowerCase(Locale.ROOT).contains("e")) {
            return new MyNumber(new calculator.value.RealValue(text));
        } else if (Main.getCurrentDomain() == NumberDomain.RATIONAL) {
            return new MyNumber(new calculator.value.RationalValue(Integer.parseInt(text)));
        } else {
            return new MyNumber(Integer.parseInt(text));
        }
    }

    @Override
    public Expression visitConst(CalculatorParser.ConstContext ctx) {
        return new MyConstant(ctx.CONSTANT().getText());
    }

    @Override
    public Expression visitAbs(CalculatorParser.AbsContext ctx) {
        Expression arg = visit(ctx.expr());
        try { return new Abs(java.util.List.of(arg)); }
        catch (IllegalConstruction e) {
            throw new IllegalConstruction("Invalid construction of Abs", e);
        }
    }

    @Override
    public Expression visitFactorial(CalculatorParser.FactorialContext ctx) {
        Expression arg = visit(ctx.expr());
        try { return new Factorial(java.util.List.of(arg), Notation.POSTFIX); }
        catch (IllegalConstruction e) {
            throw new IllegalConstruction("Invalid construction of Factorial", e);
        }
    }

    @Override
    public Expression visitFunc(CalculatorParser.FuncContext ctx) {
        Expression arg = visit(ctx.expr());
        String func = ctx.func.getText();
        try {
            switch(func) {
                case "sin": return new Sin(java.util.List.of(arg), Notation.PREFIX);
                case "cos": return new Cos(java.util.List.of(arg), Notation.PREFIX);
                case "tan": return new Tan(java.util.List.of(arg), Notation.PREFIX);
                case "arcsin": return new ArcSin(java.util.List.of(arg), Notation.PREFIX);
                case "arccos": return new ArcCos(java.util.List.of(arg), Notation.PREFIX);
                case "arctan": return new ArcTan(java.util.List.of(arg), Notation.PREFIX);
                case "ln": return new Ln(java.util.List.of(arg), Notation.PREFIX);
                case "log": return new Log(java.util.List.of(arg), Notation.PREFIX);
                default: throw new IllegalArgumentException("Unknown func: " + func);
            }
        } catch (IllegalConstruction e) {
            throw new IllegalConstruction("Invalid construction of Function", e);
        }
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
