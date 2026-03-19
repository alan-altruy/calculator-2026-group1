grammar Calculator;

prog: expr EOF ;

exprList: expr (','? expr)* ;

expr: '(' expr ')'                               # Parens
    | op=('*'|'/'|'+'|'-'|'**') '(' exprList ')' # Prefix
    | '(' exprList ')' op=('*'|'/'|'+'|'-'|'**') # Postfix
    | <assoc=right> expr '**' expr               # Power
    | expr '(' expr ')'                          # ImplicitMul
    | '(' expr ')' expr                          # ImplicitMul
    | expr op=('*' | '/') expr                   # MulDiv
    | expr op=('+' | '-') expr                   # AddSub
    | NUMBER                                     # Num
    ;

NUMBER: [0-9]+ ;
WS: [ \t\r\n]+ -> skip ;
