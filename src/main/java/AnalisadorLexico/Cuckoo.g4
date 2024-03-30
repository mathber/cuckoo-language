grammar Cuckoo;

DEC: 'DECLARACOES';
ALG: 'ALGORITMO';
TIPO: 'int'|'real';
ATR: 'atribuir';
A: 'a';
AP: '(';
FP: ')';
ESC: 'ESCREVA';
ID: LETRA(DIGITO|LETRA)*;
NUM: DIGITO+('.'DIGITO+)?;
OP_ARIT: '+'|'-'|'*'|'/';
fragment LETRA: [a-zA-Z];
fragment DIGITO: [0-9];

// Ignora espaços em branco
WS: [ \r\t\n]* ->skip;
ErrorChar: . ;
