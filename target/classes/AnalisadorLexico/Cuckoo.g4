grammar Cuckoo;

TIPO: 'INT' | 'REAL' | 'PAL' | 'VOUF';

AP: '(';
FP: ')';

PONTO: '.';

INICIO: 'COMECO';
FIM: 'FIM';

COMENT: '%'(CARACTERE)*'%';

ID: '@'LETRA(DIGITO|LETRA)*;
NUM: DIGITO+(','DIGITO+)?;
STRING: '`'(CARACTERE)*'`';

ATRIBUI: '<-';

OP_ARIT: '+'|'-'|'*'|':'|'::'|'#';
OP_REL: '==' | '=/=' | '>>' | '<<' | '=>' | '=<';
OP_LOG: 'OU' | 'E' | 'NAO' ;
CONCATENA: '&';

// ESTRUTURA CONDICIONAL
IF: 'SE';
ELIF: 'DOCONTRARIO SE';
ELSE: 'DOCONTRARIO';

// ESTRUTURA DE REPETIÇÃO
WHILE: 'DURANTE';

// COMANDO DE ESCRITA E LEITURA
OUTPUT: 'EXIBA';
INPUT: 'RECEBA';

// FUNÇÃO
FUNC: 'FUNCAO';
VOID: 'VAZIO';

AM: '<';
FM: '>';

SEPARACAO: ';';
RETURN: 'VOLTE';

ID_FUNC: '_'LETRA(DIGITO|LETRA)*;

fragment LETRA: [a-zA-Z];
fragment DIGITO: [0-9];
fragment ESPECIAL : '+' | '-' | '*' | '/' | '=' | '<' | '>' | '(' | ')' | '{' | '}' | '[' | ']' | ',' | ';' | '.' | ':' | '|' | '&' | '%' ;

fragment CARACTERE : LETRA | DIGITO | ESPECIAL | [ \t\r\n];

// Ignora espaços em branco
WS: [ \r\t\n]* ->skip;
ErrorChar: . ;