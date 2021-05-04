grammar ICSS;

//--- LEXER: ---

//Literals
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z0-9\-]+;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
stylesheet: (variableAssignment | styleRule)+ EOF;

//Literals
pixelLiteral: PIXELSIZE;
colorLiteral: COLOR;
percentageLiteral: PERCENTAGE;
scalarLiteral: SCALAR;

//Variable
variableReference: CAPITAL_IDENT;
variableAssignment: variableReference ASSIGNMENT_OPERATOR operation SEMICOLON;

//Selectors:
tagSelector: LOWER_IDENT;
idSelector: ID_IDENT;
classSelector: CLASS_IDENT;

//Operations
operation: operation MUL operation #multiplyOperation |
           operation PLUS operation #addOperation |
           operation MIN operation #subtractOperation |
           variableReference #varInOperation |
           literal #literalInOperation;

//Properties
propertyName: LOWER_IDENT;

//Merged
property: propertyName;
literal: pixelLiteral | colorLiteral | percentageLiteral | scalarLiteral;
selector: classSelector |tagSelector| idSelector ;

styleRule: selector OPEN_BRACE (variableAssignment| declaration| styleRule)+ CLOSE_BRACE;
declaration: property COLON (literal | variableReference | operation) SEMICOLON;




