grammar Calculator;

prog: expr EOF ;

expr: '(' expr ')'                 # Parens
    | expr op=('*' | '/') expr     # MulDiv
    | expr op=('+' | '-') expr     # AddSub
    | NUMBER                       # Num
    ;

NUMBER: [0-9]+ ;
WS: [ \t\r\n]+ -> skip ;
