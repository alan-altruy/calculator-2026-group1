# ANTLR v4 Expression Parser Implementation

This document provides a step-by-step explanation of how the ANTLR v4 expression parser was implemented in this project to convert mathematical string expressions containing basic operations (`+`, `-`, `*`, `/`) and parentheses into the application's Abstract Syntax Tree (AST) `Expression` objects.

## Step 1: Maven Configuration
To use ANTLR v4, we first updated the `pom.xml` file.
1. **Added Properties**: We added `<antlr.version>4.13.1</antlr.version>` to manage the version centrally.
2. **Added Dependencies**: We included `antlr4-runtime` dependency which is required to execute the generated lexers and parsers.
3. **Added Plugins**: We added the `antlr4-maven-plugin` to the `<build><plugins>` section. This plugin automatically generates the Java source files from our ANTLR grammar (`.g4` file) every time we compile the code. We configured it to generate a `Visitor` instead of a `Listener` for more control over tree traversal.

## Step 2: Defining the Grammar
We created a new directory `src/main/antlr4/calculator/` and placed our grammar file `Calculator.g4` inside it.
The grammar does the following:
- Defines Lexer rules like `NUMBER : [0-9]+ ;` to identify integers and skips whitespaces.
- Defines Parser rules that understand operator precedence (multiplication and division occur before addition and subtraction).
- Labels rules using the `#` symbol (e.g., `# Parens` or `# AddSub`). These labels tell ANTLR to generate specific `visit<Label>` methods in the resulting Visitor class, making it easy to handle specific types of expressions.

## Step 3: Generating Code
By running `mvn compile`, the ANTLR Maven plugin automatically takes the `Calculator.g4` grammar file and outputs multiple Java classes to `target/generated-sources/antlr4/`:
- `CalculatorLexer.java`: Converts raw text characters into recognized tokens.
- `CalculatorParser.java`: Groups the tokens according to the grammar rules into a Parse Tree.
- `CalculatorBaseVisitor.java`: A base class we can extend to explore the Parse Tree.

## Step 4: Connecting ANTLR to the Application Objects
To convert the ANTLR Parse Tree into the pre-existing objects of our calculator (`Plus`, `Minus`, `Times`, `Divides`, `MyNumber`), we created `src/main/java/calculator/CalculatorVisitorImpl.java`. 

This class extends `CalculatorBaseVisitor<Expression>` and implements specific translation logic:
- `visitNum()` converts string values like `"42"` into `new MyNumber(42)`.
- `visitAddSub()` extracts the left and right expressions, applies the `visit` method recursively on them, and checks the operator to return either a `new Plus(...)` or a `new Minus(...)`.
- The logic handles `IllegalConstruction` exceptions by wrapping them as `RuntimeException`.

## Step 5: The ExpressionParser Wrapper
To provide a clean API to interact with the system, we created `ExpressionParser.java`.
It abstracts away the ANTLR boilerplate and provides a simple static entry point:
```java
public static Expression parse(String expressionString)
```
Behind the scenes, it injects the string into `CharStreams`, pushes it through the `CalculatorLexer` and `CalculatorParser`, generates the Parse Tree, and passes it to the `CalculatorVisitorImpl` for translation.

## Step 6: Updating Main & Tests
To verify all changes, we successfully integrated the new parser:
- Modified `Main.java` to demonstrate parsing string inputs directly: `ExpressionParser.parse("3 + 5 * ( 2 - 8 )")`.
- Added a `TestParser.java` unit testing file to evaluate simple and complex operator precedence automatically during the standard `mvn test` stage.

## Step 7: Prefix and Postfix Extensibility
After the initial Infix implementation, we expanded `Calculator.g4` to parse prefix formatting (e.g., `+(1, 2)`) and postfix formatting (e.g., `(1, 2)+`). 
- **Grammar Updates:** We added an `exprList` rule allowing space-separated or comma-separated expressions, enabling support for multi-argument operations natively without requiring strict parentheses around everything.
- **Visitor Updates:** The `CalculatorVisitorImpl` now recognizes `PrefixContext` and `PostfixContext`, specifically assigning `Notation.PREFIX` or `Notation.POSTFIX` to the AST Node. This guarantees that when parsing an expression and calling `.print()` on it, the output perfectly matches the original string's structural notation.

## Step 8: Advanced AST Features (Exponentiation, Implicit Math, and Precedence Formatting)

1. **Exponentiation (`**`)**: Created a new `Power` operations type and extended the Visitor to create it. We used `<assoc=right>` inside ANTLR to naturally enforce right-to-left evaluation commonly used for powers.
2. **Implicit Multiplication**: Extended ANTLR grammar with `expr '(' expr ')'` and `'(' expr ')' expr` resolving to `ImplicitMul`. This natively converts expressions like `(4+5)(6)` into `Times(Plus(4,5), 6)`.
3. **Smart Strings (Intelligent Parentheses)**: 
   - Introduced `getPrecedence()` interface for AST `Expression`.
   - Re-wrote the `Operation.toString(Notation.INFIX)` generator to compare child and parent precedences and associativities to selectively hide unneeded parentheses natively mimicking proper mathematical convention. `(4+5+6)*(7+5/2/7)*9` will now securely omit unnecessary inner parens.
