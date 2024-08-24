grammar Cuckoo;

programa: (funcao)* main EOF;
main: INICIO (instr)* FIM;
instr: ((atrib|acao|declr|retorno|chamada_func) PONTO) | condicao | repeticao;
operacoes: operando|operacao_arit|operacao_concat|operacao_log;
// AÇÃO
acao: (OUTPUT AM operacoes FM) | (INPUT AM ID FM);
// DECLARAÇÃO
declr: TIPO ID;
// RETORNO
retorno: RETURN operacoes;
// ATRIBUIÇÃO
atrib: ID ATRIBUI operacoes;
operando: ID|NUM_INT|NUM_REAL|STRING|BOOL|chamada_func;
// OPERAÇÃO ARITMÉTICA
operacao_arit: operando operacao_cauda_arit;
operacao_cauda_arit: OP_ARIT operando (operacao_cauda_arit)*;
// OPERAÇÃO CONCATENAÇÃO
operacao_concat: operando operacao_cauda_concat;
operacao_cauda_concat: CONCATENA operando (operacao_cauda_concat)*;
// OPERAÇÃO LÓGICA
operacao_log: operando operacao_cauda_log;
operacao_cauda_log: OP_LOG operando (operacao_cauda_log)*;
// OPERAÇÃO RELACIONAL
operacao_rel: operando operacao_cauda_rel;
operacao_cauda_rel: OP_REL operando(OP_LOG operacao_rel)*;
// CONDICIONAL
condicao: IF (operacao_log|operacao_rel) bloco_instr (ELIF (operacao_log|operacao_rel) bloco_instr)* (ELSE bloco_instr)?;
bloco_instr: AP (instr)+ FP;
//REPETIÇÃO
repeticao: WHILE (operacao_log|operacao_rel) bloco_instr;

// FUNÇÃO
funcao: FUNC (TIPO|VOID) ID_FUNC parametro_declr bloco_instr;
parametro_declr: AM (declr (SEPARACAO declr)*)? FM;
parametro_chamada: AM (operacoes (SEPARACAO operacoes)*)? FM;

chamada_func: ID_FUNC parametro_chamada;


TIPO: 'INT' | 'REAL' | 'PAL' | 'VOUF';

AP: '(';
FP: ')';

PONTO: '.';

INICIO: 'COMECO';
FIM: 'FIM';

ID: '@'LETRA(DIGITO|LETRA)*;

NUM_INT: DIGITO;
NUM_REAL: DIGITO+','DIGITO+;

STRING: '`' (~[`])* '`';
BOOL: 'VERDADE'|'FALSO';

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

// Ignora comentário
COMENT: '%'~[\r]*'%' -> skip;
// Ignora espaços em branco
WS: [ \t\r\n]+ -> skip;
