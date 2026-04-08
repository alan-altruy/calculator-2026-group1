package calculator.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import calculator.*;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.operations.Plus;
import calculator.operations.Power;
import calculator.operations.Times;
import calculator.operations.Mod;
import calculator.operations.IntDiv;
import calculator.operations.unaryoperations.Abs;
import calculator.operations.unaryoperations.ArcCos;
import calculator.operations.unaryoperations.ArcSin;
import calculator.operations.unaryoperations.ArcTan;
import calculator.operations.unaryoperations.Cos;
import calculator.operations.unaryoperations.Factorial;
import calculator.operations.unaryoperations.Ln;
import calculator.operations.unaryoperations.Log;
import calculator.operations.unaryoperations.Sin;
import calculator.operations.unaryoperations.Tan;

import org.junit.jupiter.api.Test;
import visitor.CalculatorVisitorImpl;
import calculator.enums.NumberDomain;
import calculator.value.ComplexValue;
import calculator.value.RealValue;
import calculator.value.RationalValue;
import calculator.value.IntegerValue;

class TestCalculatorVisitorImpl {

    private Calculator calc;

    private CalculatorVisitorImpl visitor;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        visitor = new CalculatorVisitorImpl();
    }

    private MethodHandle getCreateOperationHandle() throws Exception {
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorVisitorImpl.class, MethodHandles.lookup());
        return lookup.findVirtual(CalculatorVisitorImpl.class, "createOperation",
                MethodType.methodType(Expression.class, String.class, List.class, Notation.class));
    }

    @Test
    void testCreateOperationPowerCase() throws IllegalConstruction {
        List<Expression> args = Arrays.asList(new MyNumber(2), new MyNumber(3));

        Expression result = new Power(args, Notation.INFIX);

        assertNotNull(result);
        assertInstanceOf(Power.class, result);
        assertEquals("2 ** 3", result.toString());
    }

    @Test
    void testCreateOperationCatchIllegalConstructionThrowsIllegalConstruction() {
        assertThrows(IllegalConstruction.class, () -> new Plus(null));
    }

    @Test
    void testCreateOperationDefaultCaseThrowsIllegalArgumentException() throws Throwable {
        List<Expression> args = Arrays.asList(new MyNumber(1), new MyNumber(2));

        MethodHandle mh = getCreateOperationHandle();

        Throwable thrown = org.junit.jupiter.api.Assertions.assertThrows(Throwable.class,
            () -> mh.invoke(visitor, "%", args, Notation.INFIX));

        org.junit.jupiter.api.Assertions.assertInstanceOf(IllegalArgumentException.class, thrown);
        org.junit.jupiter.api.Assertions.assertEquals("Unknown operator: %", thrown.getMessage());
    }

    @Test
    void testCreateOperationCatchIllegalConstructionWrapsRuntimeException() throws Throwable {
        MethodHandle mh = getCreateOperationHandle();

        Throwable thrown = org.junit.jupiter.api.Assertions.assertThrows(Throwable.class,
            () -> mh.invoke(visitor, "+", null, Notation.INFIX));

        org.junit.jupiter.api.Assertions.assertInstanceOf(RuntimeException.class, thrown);
        org.junit.jupiter.api.Assertions.assertEquals("Invalid operation construction", thrown.getMessage());
        org.junit.jupiter.api.Assertions.assertNotNull(thrown.getCause());
        org.junit.jupiter.api.Assertions.assertInstanceOf(IllegalConstruction.class, thrown.getCause());
    }

    @Test
    void testParseImplicitMultiplication() {
        calc = new Calculator();
        Expression e = ExpressionParser.parse("2(3+4)");
        assertInstanceOf(Times.class, e);
        assertEquals(14, calc.eval(e));
    }

    @Test
    void testParsePower() {
        calc = new Calculator();
        Expression e = ExpressionParser.parse("2 ** 3 ** 2");
        assertInstanceOf(Power.class, e);
        assertEquals(512, calc.eval(e));
    }

    @Test
    void visitNumProducesComplexWhenDomainComplex() {
        Main.setCurrentDomain(NumberDomain.COMPLEX);
        Expression e = ExpressionParser.parse("42");
        assertInstanceOf(MyNumber.class, e);
        MyNumber n = (MyNumber) e;
        assertInstanceOf(ComplexValue.class, n.getValueObject());
    }

    @Test
    void visitNumProducesRealWhenDomainReal() {
        Main.setCurrentDomain(NumberDomain.REAL);
        Expression e = ExpressionParser.parse("42");
        assertInstanceOf(MyNumber.class, e);
        MyNumber n = (MyNumber) e;
        assertInstanceOf(RealValue.class, n.getValueObject());
    }

    @Test
    void visitNumProducesRationalWhenDomainRational() {
        Main.setCurrentDomain(NumberDomain.RATIONAL);
        Expression e = ExpressionParser.parse("42");
        assertInstanceOf(MyNumber.class, e);
        MyNumber n = (MyNumber) e;
        assertInstanceOf(RationalValue.class, n.getValueObject());
    }

    @Test
    void visitNumProducesIntegerWhenDomainInteger() {
        Main.setCurrentDomain(NumberDomain.INTEGER);
        Expression e = ExpressionParser.parse("42");
        assertInstanceOf(MyNumber.class, e);
        MyNumber n = (MyNumber) e;
        assertInstanceOf(IntegerValue.class, n.getValueObject());
    }

    @Test
    void visitNumProducesRealWhenTextContainsDot() {
        Main.setCurrentDomain(NumberDomain.INTEGER);
        Expression e = ExpressionParser.parse("3.14");
        assertInstanceOf(MyNumber.class, e);
        MyNumber n = (MyNumber) e;
        assertInstanceOf(RealValue.class, n.getValueObject());
    }

    @Test
    void visitNumProducesRealWhenTextContainsExponent() {
        Main.setCurrentDomain(NumberDomain.INTEGER);
        Expression e = ExpressionParser.parse("1e3");
        assertInstanceOf(MyNumber.class, e);
        MyNumber n = (MyNumber) e;
        assertInstanceOf(RealValue.class, n.getValueObject());
    }

    @Test
    void testCreateOperationModCase() throws Throwable {
        List<Expression> args = Arrays.asList(new MyNumber(8), new MyNumber(3));

        MethodHandle mh = getCreateOperationHandle();
        Object result = mh.invoke(visitor, "mod", args, Notation.INFIX);

        assertNotNull(result);
        assertInstanceOf(Mod.class, result);
        assertEquals("8 mod 3", result.toString());
    }

    @Test
    void testCreateOperationIntDivCase() throws Throwable {
        List<Expression> args = Arrays.asList(new MyNumber(8), new MyNumber(3));

        MethodHandle mh = getCreateOperationHandle();
        Object result = mh.invoke(visitor, "//", args, Notation.INFIX);

        assertNotNull(result);
        assertInstanceOf(IntDiv.class, result);
        assertEquals("8 // 3", result.toString());
    }
    
    @Test
    void testVisitAbsParsingAndEval() {
        NumberDomain previous = Main.getCurrentDomain();
        try {
            Main.setCurrentDomain(NumberDomain.REAL);
            calc = new Calculator();
            Expression e = ExpressionParser.parse("|(0-5)|");
            assertInstanceOf(Abs.class, e);
            assertEquals(5, calc.eval(e));
        } finally {
            Main.setCurrentDomain(previous);
        }
    }

    @Test
    void testVisitFactorialParsingAndEval() {
        NumberDomain previous = Main.getCurrentDomain();
        try {
            Main.setCurrentDomain(NumberDomain.REAL);
            calc = new Calculator();
            Expression e = ExpressionParser.parse("5!");
            assertInstanceOf(Factorial.class, e);
            assertEquals(120, calc.eval(e));
        } finally {
            Main.setCurrentDomain(previous);
        }
    }
    @Test
    void testVisitFuncTrigonometricAndLog() {
        NumberDomain previous = Main.getCurrentDomain();
        try {
            Main.setCurrentDomain(NumberDomain.REAL);
            calc = new Calculator();

            Expression s = ExpressionParser.parse("sin(0)");
            assertInstanceOf(Sin.class, s);
            assertEquals(0, calc.eval(s));

            Expression c = ExpressionParser.parse("cos(0)");
            assertInstanceOf(Cos.class, c);
            assertEquals(1, calc.eval(c));

            Expression t = ExpressionParser.parse("tan(0)");
            assertInstanceOf(Tan.class, t);
            assertEquals(0, calc.eval(t));

            Expression as = ExpressionParser.parse("arcsin(0)");
            assertInstanceOf(ArcSin.class, as);
            assertEquals(0, calc.eval(as));

            Expression ac = ExpressionParser.parse("arccos(1)");
            assertInstanceOf(ArcCos.class, ac);
            assertEquals(0, calc.eval(ac));

            Expression at = ExpressionParser.parse("arctan(0)");
            assertInstanceOf(ArcTan.class, at);
            assertEquals(0, calc.eval(at));

            Expression ln = ExpressionParser.parse("ln(1)");
            assertInstanceOf(Ln.class, ln);
            assertEquals(0, calc.eval(ln));

            Expression log = ExpressionParser.parse("log(1)");
            assertInstanceOf(Log.class, log);
            assertEquals(0, calc.eval(log));

        } finally {
            Main.setCurrentDomain(previous);
        }
    }

    @Test
    void testVisitFuncDirectUnknownThrowsIllegalArgumentException() {
        // build a real FuncContext then mutate its token text to an unknown func
        org.antlr.v4.runtime.CharStream cs = org.antlr.v4.runtime.CharStreams.fromString("sin(1)");
        calculator.CalculatorLexer lexer = new calculator.CalculatorLexer(cs);
        org.antlr.v4.runtime.CommonTokenStream tokens = new org.antlr.v4.runtime.CommonTokenStream(lexer);
        calculator.CalculatorParser parser = new calculator.CalculatorParser(tokens);
        calculator.CalculatorParser.ExprContext tree = parser.expr();
        org.junit.jupiter.api.Assertions.assertInstanceOf(calculator.CalculatorParser.FuncContext.class, tree);
        calculator.CalculatorParser.FuncContext ctx = (calculator.CalculatorParser.FuncContext) tree;
        ((org.antlr.v4.runtime.CommonToken) ctx.func).setText("foo");

        CalculatorVisitorImpl vis = new CalculatorVisitorImpl();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
            () -> vis.visitFunc(ctx));
    }
}