package AnalisadorLexico;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MyListener extends CuckooBaseListener {
    private final Stack<Map<String, String>> escoposPilha = new Stack<>();
    private Map<String, String> escopoAtual;
    private Map<String, String> escopoGlobal;

    private boolean isVariableDeclared(String id) {
        if (!escoposPilha.isEmpty()) {
            Map<String, String> escopoAtual = escoposPilha.peek();
            if (escopoAtual.containsKey(id)) {
                return true;
            }
        }
        return escopoGlobal.containsKey(id);
    }

    private String getTipoOperando(CuckooParser.OperandoContext ctx) {
        if (ctx.ID() != null) {
            return escopoAtual.get(ctx.ID().getText());
        } else if (ctx.NUM_INT() != null) {
            return "INT";
        } else if (ctx.NUM_REAL() != null) {
            return "REAL";
        } else if (ctx.STRING() != null) {
            return "PAL";
        } else if (ctx.BOOL() != null) {
            return "VOUF";
        } else if (ctx.chamada_func() != null) {
            return escopoAtual.get(ctx.chamada_func().ID_FUNC().getText());
        }
        return null;
    }

    private boolean tiposCompativeis(String tipo1, String tipo2) {
        return tipo1 != null && tipo1.equals(tipo2);
    }

    @Override
    public void enterPrograma(CuckooParser.ProgramaContext ctx) {
        escopoGlobal = new HashMap<String, String>();
    }

    @Override
    public void enterFuncao(CuckooParser.FuncaoContext ctx) {

        if(escopoGlobal.containsKey(ctx.ID_FUNC().getText())){
            System.out.printf("Erro: Declaração duplicada de função '%s' na linha %d, coluna %d.%n", ctx.ID_FUNC(), ctx.ID_FUNC().getSymbol().getLine(), ctx.ID_FUNC().getSymbol().getCharPositionInLine());
        } else {
            escopoGlobal.put(ctx.ID_FUNC().getText(), ctx.TIPO().getText());
        }

        escopoAtual = new HashMap<String, String>(escopoGlobal);
        escoposPilha.push(escopoAtual);
    }

    @Override
    public void exitFuncao(CuckooParser.FuncaoContext ctx) {
        escoposPilha.pop();
        if (!escoposPilha.isEmpty()) {
            escopoAtual = escoposPilha.peek();
        }
    }

    @Override
    public void enterChamada_func(CuckooParser.Chamada_funcContext ctx){
        if (ctx.ID_FUNC() != null) {
            String id = ctx.ID_FUNC().getText();

            int linha = ctx.ID_FUNC().getSymbol().getLine();
            int coluna = ctx.ID_FUNC().getSymbol().getCharPositionInLine();

            if (!isVariableDeclared(id)) {
                System.out.printf("Erro: Função '%s' não declarada utilizada na linha %d, coluna %d.%n", id, linha, coluna);
            }
        }
    }
    @Override
    public void enterMain(CuckooParser.MainContext ctx) {
        escopoAtual = new HashMap<String, String>(escopoGlobal);
        escoposPilha.push(escopoAtual);
    }

    @Override
    public void exitMain(CuckooParser.MainContext ctx) {
        escoposPilha.pop();
        if (!escoposPilha.isEmpty()) {
            escopoAtual = escoposPilha.peek();
        }
    }

    @Override
    public void enterBloco_instr(CuckooParser.Bloco_instrContext ctx) {
        escopoAtual = new HashMap<String, String>(escoposPilha.peek());
        escoposPilha.push(escopoAtual);
    }

    @Override
    public void exitBloco_instr(CuckooParser.Bloco_instrContext ctx) {
        escoposPilha.pop();
        if (!escoposPilha.isEmpty()) {
            escopoAtual = escoposPilha.peek();
        }
    }

    @Override
    public void exitDeclr(CuckooParser.DeclrContext ctx){

        String tipo = ctx.TIPO().getText();
        String id = ctx.ID().getText();
        int linha = ctx.ID().getSymbol().getLine();
        int coluna = ctx.ID().getSymbol().getCharPositionInLine();

        if(escopoAtual.containsKey(id)){
            System.out.printf("Erro: Declaração duplicada da variável '%s' na linha %d, coluna %d.%n", id, linha, coluna);
        } else {
            escopoAtual.put(id, tipo);
        }
    }

    @Override
    public void exitOperando(CuckooParser.OperandoContext ctx) {

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();

            int linha = ctx.ID().getSymbol().getLine();
            int coluna = ctx.ID().getSymbol().getCharPositionInLine();

            if (!isVariableDeclared(id)) {
                System.out.printf("Erro: Variável '%s' não declarada utilizada na linha %d, coluna %d.%n", id, linha, coluna);
            }
        }
    }
    @Override
    public void exitAtrib(CuckooParser.AtribContext ctx) {

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();

            int linha = ctx.ID().getSymbol().getLine();
            int coluna = ctx.ID().getSymbol().getCharPositionInLine();

            if (!isVariableDeclared(id)) {
                System.out.printf("Erro: Variável '%s' não declarada utilizada na linha %d, coluna %d.%n", id, linha, coluna);
            }
        }

        String tipo1 = escopoAtual.get(ctx.ID().getText());
        String tipo2;
        if(ctx.operacoes().operando() != null){
            tipo2 = getTipoOperando(ctx.operacoes().operando());
            if(!tiposCompativeis(tipo1, tipo2)){
                System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                        tipo1, tipo2, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
        } else if (ctx.operacoes().operacao_arit() != null){
            tipo2 = getTipoOperando(ctx.operacoes().operacao_arit().operando());
            if(!tiposCompativeis(tipo1, tipo2)){
                System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                        tipo1, tipo2, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
        } else if (ctx.operacoes().operacao_concat() != null){
            tipo2 = getTipoOperando(ctx.operacoes().operacao_concat().operando());
            if(!tiposCompativeis(tipo1, tipo2)){
                System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                        tipo1, tipo2, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
        } else if (ctx.operacoes().operacao_log() != null){
            tipo2 = getTipoOperando(ctx.operacoes().operacao_log().operando());
            if(!tiposCompativeis(tipo1, tipo2)){
                System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                        tipo1, tipo2, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
        }

    }

    @Override
    public void exitAcao(CuckooParser.AcaoContext ctx) {

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();

            int linha = ctx.ID().getSymbol().getLine();
            int coluna = ctx.ID().getSymbol().getCharPositionInLine();

            if (!isVariableDeclared(id)) {
                System.out.printf("Erro: Variável '%s' não declarada utilizada na linha %d, coluna %d.%n", id, linha, coluna);
            }
        }
    }

    @Override
    public void exitOperacao_arit(CuckooParser.Operacao_aritContext ctx) {

        String tipoOperando = getTipoOperando(ctx.operando());

        if (ctx.operacao_cauda_arit() != null) {

            CuckooParser.Operacao_cauda_aritContext caudaAtual = ctx.operacao_cauda_arit().operacao_cauda_arit(0);
            while(caudaAtual != null){

                String tipoCauda = getTipoOperando(caudaAtual.operando());

                if (!tiposCompativeis(tipoOperando, tipoCauda)) {
                    System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                            tipoOperando, tipoCauda, caudaAtual.getStart().getLine(), caudaAtual.getStart().getCharPositionInLine());
                }

                if (caudaAtual.operacao_cauda_arit().isEmpty()) {
                    caudaAtual = null;
                } else {
                    caudaAtual = caudaAtual.operacao_cauda_arit(0);
                }

            }
        }

        CuckooParser.OperandoContext caudaAtual = ctx.operacao_cauda_arit().operando();
        String tipoCauda = getTipoOperando(ctx.operacao_cauda_arit().operando());
        if (!tiposCompativeis(tipoOperando, tipoCauda)) {
            System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                    tipoOperando, tipoCauda, caudaAtual.getStart().getLine(), caudaAtual.getStart().getCharPositionInLine());
        }
    }

    @Override
    public void exitOperacao_concat(CuckooParser.Operacao_concatContext ctx) {

        if (ctx.operacao_cauda_concat() != null) {

            CuckooParser.Operacao_cauda_concatContext caudaAtual = ctx.operacao_cauda_concat().operacao_cauda_concat(0);
            while(caudaAtual != null){

                String tipoCauda = getTipoOperando(caudaAtual.operando());

                if (!tiposCompativeis(tipoCauda, "PAL")) {
                    System.out.printf("Erro: Incompatibilidade com o tipo PAL na linha %d, coluna %d.%n",
                            caudaAtual.getStart().getLine(), caudaAtual.getStart().getCharPositionInLine());
                }

                if (caudaAtual.operacao_cauda_concat().isEmpty()) {
                    caudaAtual = null;
                } else {
                    caudaAtual = caudaAtual.operacao_cauda_concat(0);
                }

            }
        }

        CuckooParser.OperandoContext caudaAtual = ctx.operacao_cauda_concat().operando();
        String tipoOperando = getTipoOperando(ctx.operacao_cauda_concat().operando());
        if (!tiposCompativeis(tipoOperando, "PAL")) {
            System.out.printf("Erro: Incompatibilidade com o tipo PAL na linha %d, coluna %d.%n",
                    caudaAtual.getStart().getLine(), caudaAtual.getStart().getCharPositionInLine());
        }

    }

    @Override
    public void exitOperacao_log(CuckooParser.Operacao_logContext ctx) {

        String tipoOperando = getTipoOperando(ctx.operando());

        if (ctx.operacao_cauda_log() != null) {

            CuckooParser.Operacao_cauda_logContext caudaAtual = ctx.operacao_cauda_log().operacao_cauda_log(0);
            while(caudaAtual != null){

                String tipoCauda = getTipoOperando(caudaAtual.operando());

                if (!tiposCompativeis(tipoOperando, tipoCauda)) {
                    System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                            tipoOperando, tipoCauda, caudaAtual.getStart().getLine(), caudaAtual.getStart().getCharPositionInLine());
                }

                if (caudaAtual.operacao_cauda_log().isEmpty()) {
                    caudaAtual = null;
                } else {
                    caudaAtual = caudaAtual.operacao_cauda_log(0);
                }

            }
        }

        CuckooParser.OperandoContext caudaAtual = ctx.operacao_cauda_log().operando();
        String tipoCauda = getTipoOperando(ctx.operacao_cauda_log().operando());
        if (!tiposCompativeis(tipoOperando, tipoCauda)) {
            System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                    tipoOperando, tipoCauda, caudaAtual.getStart().getLine(), caudaAtual.getStart().getCharPositionInLine());
        }

    }

    @Override
    public void exitOperacao_rel(CuckooParser.Operacao_relContext ctx) {

        String tipo1 = getTipoOperando(ctx.operando());
        String tipo2 = getTipoOperando(ctx.operacao_cauda_rel().operando());

        if(!tiposCompativeis(tipo1, tipo2)){
            System.out.printf("Erro: Incompatibilidade de tipos entre '%s' e '%s' na linha %d, coluna %d.%n",
                    tipo1, tipo2, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }

    }




}
