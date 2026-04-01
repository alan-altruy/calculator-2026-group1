package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        int val = Integer.parseInt(ctx.NUMBER().getText());
        return new MyNumber(val);
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
                case "+":
                    return new Plus(args, notation);
                case "-":
                    return new Minus(args, notation);
                case "*":
                    return new Times(args, notation);
                case "/":
                    return new Divides(args, notation);
                case "**":
                    return new Power(args, notation);
                default:
                    throw new IllegalArgumentException("Unknown operator: " + op);
            }
        } catch (IllegalConstruction e) {
            throw new RuntimeException("Invalid operation construction", e);
        }
    }
}
