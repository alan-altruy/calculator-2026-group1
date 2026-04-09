grammar Calculator;

prog: expr EOF ;

exprList: expr (','? expr)* ;

expr: '(' expr ')'                               # Parens
    | '|' expr '|'                               # Abs
    | expr '!'                                   # Factorial
    | func=('sin'|'cos'|'tan'|'arcsin'|'arccos'|'arctan'|'ln'|'log') '(' expr ')' # Func
    | op=('*'|'/'|'+'|'-'|'**'|'mod'|'//') '(' exprList ')' # Prefix
    | '(' exprList ')' op=('*'|'/'|'+'|'-'|'**'|'mod'|'//') # Postfix
    | op='-' expr                                # UnaryMinus
    | <assoc=right> expr '**' expr               # Power
    | expr '(' expr ')'                          # ImplicitMul
    | '(' expr ')' expr                          # ImplicitMul
    | expr op=('*' | '/' | 'mod' | '//') expr    # MulDiv
    | expr op=('+' | '-') expr                   # AddSub
    | NUMBER                                     # Num
    | CONSTANT                                   # Const
    ;

NUMBER: [0-9]+ ('.' [0-9]+)? ([eE] [+-]? [0-9]+)? ;
CONSTANT: 'pi' | 'e' | 'phi' | 'i' ;
WS: [ \t\r\n]+ -> skip ;
