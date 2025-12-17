grammar Nottscript;
file: (subroutine | function)* program EOF;

// top level stuff
program: KW_PROGRAM progName=NAME block KW_END KW_PROGRAM endProgName=NAME;
subroutine: KW_SUBROUTINE subName=NAME L_BRACKET name_list R_BRACKET block KW_END KW_SUBROUTINE endSubName=NAME;
function: KW_FUNCTION funcName=NAME L_BRACKET name_list R_BRACKET (KW_RESULT L_BRACKET resName=NAME R_BRACKET)? block KW_END KW_FUNCTION endFuncName=NAME;
//function_call: NAME L_BRACKET expression_list? R_BRACKET;

statement: if_statement
         | if_then_statement
         | write
         | read
         | assignment
         | call;

block: statement*;
expression: literal #LiteralExpression
    | NAME #NameExpression
//    | function_call #FunctionCallExpression
    | L_BRACKET expression R_BRACKET #BinaryExpression
    | <assoc=right> lexp=expression DOUBLE_ASTERISK rexp=expression #BinaryExpression
    | lexp=expression (ASTERISK | SLASH) rexp=expression #BinaryExpression
    | lexp=expression (PLUS | MINUS) rexp=expression #BinaryExpression
    | lexp=expression DOUBLE_SLASH rexp=expression #BinaryExpression
    | lexp=expression (LT | GT | LE | GE | EQ | NEQ) rexp=expression #BinaryExpression
    | lexp=expression AND rexp=expression #BinaryExpression
    | lexp=expression OR rexp=expression #BinaryExpression
    | lexp=expression DOUBLE_SLASH rexp=expression #BinaryExpression;

definitions: type DOUBLE_COLON name_list;
type: (KW_INTEGER | KW_LOGICAL | KW_CHARACTER | KW_REAL) (L_BRACKET ((L_INT (COMMA L_INT)*) | (ASTERISK (COMMA ASTERISK)*)) R_BRACKET)?;

literal: L_INT #LiteralInt
    | L_REAL #LiteralReal
    | L_LOGICAL #LiteralLogical
    | L_CHAR #LiteralChar;

//operator: relational_op | logical_op | arithmentic_op | field_access_op | concatenation_op;
//
//// operators
//relational_op: LT | GT | LE | GE | EQ | NEQ;
//logical_op: AND | OR;
//arithmentic_op: PLUS | MINUS | ASTERISK | SLASH | DOUBLE_ASTERISK;
//field_access_op: PERCENT;
//concatenation_op: DOUBLE_SLASH;

// util
name_list: NAME (COMMA NAME)*;
expression_list: expression (COMMA expression)*;
var_ref: (dertype=NAME PERCENT)? varname=NAME(L_BRACKET L_INT (COMMA L_INT)* R_BRACKET)?;

// terminal statements
read: KW_READ name_list;
write: KW_WRITE expression_list;

// conditions and loops
if_statement: KW_IF L_BRACKET expression R_BRACKET statement;
if_then_statement: KW_IF L_BRACKET expression R_BRACKET KW_THEN thenBlock=block (KW_ELSE elseBlock=block)? KW_END KW_IF;
assignment: var_ref ASSIGN expression;
do_while_loop: KW_DO KW_WHILE L_BRACKET expression R_BRACKET block KW_END KW_DO;
do_loop: KW_DO assignment COMMA limit=expression (COMMA increment=expression)? block KW_END KW_DO;
call: KW_CALL L_BRACKET expression_list R_BRACKET;

/***********************************************************/

// Lexer

KW_ALLOCATE: 'allocate';
KW_BREAK: 'break';
KW_CALL: 'call';
KW_CHARACTER: 'character';
KW_DEALLOCATE: 'deallocate';
KW_DO: 'do';
KW_ELSE: 'else';
KW_END: 'end';
KW_FUNCTION: 'function';
KW_IF: 'if';
KW_INTEGER: 'integer';
KW_LOGICAL: 'logical';
KW_POINTER: 'pointer';
KW_PROGRAM: 'program';
KW_READ: 'read';
KW_REAL: 'real';
KW_RESULT: 'result';
KW_SUBROUTINE: 'subroutine';
KW_THEN: 'then';
KW_TYPE: 'type';
KW_WHILE: 'while';
KW_WRITE: 'write';

fragment ALPHA: [a-z];
fragment DECIMAL: [0-9];
fragment ASCII_NO_QUOTE: [\u0020-\u0021\u0023\u007E];

NAME: ALPHA (ALPHA | DECIMAL | '_')*;

fragment SIGN: [+-];
fragment BINARY: 'b"' [01]+ '"';
fragment OCTAL: 'o"' [0-7]+ '"';
fragment HEX: 'z"' [0-9a-f]+ '"';
fragment ESCAPED_QUOTE: '""';

L_INT: SIGN? DECIMAL+ | BINARY | OCTAL | HEX;
L_REAL: SIGN? DECIMAL+ '.' DECIMAL* | SIGN? DECIMAL* '.' DECIMAL+;
L_LOGICAL: '.true.' | '.false.';
L_CHAR: '"' (ESCAPED_QUOTE | ASCII_NO_QUOTE)* '"';

L_BRACKET: '(';
R_BRACKET: ')';
COMMA: ',';

LT: '.lt.' | '<';
GT: '.gt.' | '>';
LE: '.le.' | '<=';
GE: '.ge.' | '>=';
EQ: '.eq.' | '==';
NEQ: '.neq.' | '/=';

AND: '.and.';
OR: '.or.';

DOUBLE_ASTERISK: '**';
DOUBLE_SLASH: '//';
DOUBLE_COLON: '::';

PERCENT: '%';
SLASH: '/';
ASTERISK: '*';
MINUS: '-';
PLUS: '+';
ASSIGN: '=';

COMMENT: '!' ~[\n]* -> skip;
WHITESPACE: [ \t\r\n]+ -> skip;
