grammar Nottscript;
file: program EOF;

// top level stuff
program: KW_PROGRAM NAME statement* KW_END KW_PROGRAM NAME | expression;

statement: if_statement | if_then_statement | read | write;
block: statement+;
expression: literal #LiteralExpression
    | NAME #NameExpression
    | L_BRACKET expression R_BRACKET #BinaryExpression
    | <assoc=right> lexp=expression DOUBLE_ASTERISK rexp=expression #BinaryExpression
    | lexp=expression (ASTERISK | SLASH) rexp=expression #BinaryExpression
    | lexp=expression (PLUS | MINUS) rexp=expression #BinaryExpression
    | lexp=expression DOUBLE_SLASH rexp=expression #BinaryExpression
    | lexp=expression (LT | GT | LE | GE | EQ | NEQ) rexp=expression #BinaryExpression
    | lexp=expression AND rexp=expression #BinaryExpression
    | lexp=expression OR rexp=expression #BinaryExpression;

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

// terminal statements
read: KW_READ name_list;
write: KW_WRITE expression_list;

// conditions and loops
if_statement: KW_IF L_BRACKET expression R_BRACKET statement;
if_then_statement: KW_IF L_BRACKET expression R_BRACKET KW_THEN thenBlock=block (KW_ELSE elseBlock=block)? KW_END KW_IF;

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
