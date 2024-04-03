grammar Cuckoo;

TIPO: 'INT' | 'REAL' | 'PAL' | 'VOUF';

AM: '<';
FM: '>';

AP: '(';
FP: ')';

// COMANDOS ESTheCRITA E LEITURA

OUTPUT: 'EXIBA'AP+(ID | PALAVRA)+FP;
INPUT: 'RECEBA'AP+ID+FP;


ID: '@'LETRA(DIGITO|LETRA)*;
NUM: DIGITO+('.'DIGITO+)?;

OP_ARIT: '+'|'-'|'*'|'/';
OP_LOG: '==' | '=/=' | '>' | '<' | '=>' | '=<';

// Estrutura Condicional

SE_DEC: 'SE' EXPRESSAO DEC;





// Estrutura de Repetição


EXPRESSAO:



// Função


fragment LETRA: [a-zA-Z];
fragment DIGITO: [0-9];
fragment ESPECIAL: [!@#$%^&(*)-_={+};:,.<>?/|];

fragment CARACTERE : LETRA | DIGITO | ESPECIAL | [ \t\r\n];

fragment PALAVRA: '`'(CARACTERE)*'`';

// Ignora espaços em branco
WS: [ \r\t\n]* ->skip;