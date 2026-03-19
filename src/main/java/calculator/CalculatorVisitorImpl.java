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
    public Expression visitMulDiv(CalculatorParser.MulDivContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        List<Expression> args = new ArrayList<>();
        Collections.addAll(args, left, right);
        try {
            if (ctx.op.getText().equals("*")) {
                return new Times(args);
            } else {
                return new Divides(args);
            }
        } catch (IllegalConstruction e) {
            throw new RuntimeException("Invalid operation construction", e);
        }
    }

    @Override
    public Expression visitAddSub(CalculatorParser.AddSubContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        List<Expression> args = new ArrayList<>();
        Collections.addAll(args, left, right);
        try {
            if (ctx.op.getText().equals("+")) {
                return new Plus(args);
            } else {
                return new Minus(args);
            }
        } catch (IllegalConstruction e) {
            throw new RuntimeException("Invalid operation construction", e);
        }
    }

    @Override
    public Expression visitNum(CalculatorParser.NumContext ctx) {
        int val = Integer.parseInt(ctx.NUMBER().getText());
        return new MyNumber(val);
    }
}
