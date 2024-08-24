package AnalisadorLexico;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.IOException;

public class Lexer {

    public static void  main (String[] args){
        String filename = System.getProperty("user.dir") + "\\AnalisadorLexico\\src\\main\\java\\fatorial.txt";
        try{
            CharStream input = CharStreams.fromFileName(filename);
            CuckooLexer lexer = new CuckooLexer(input);

            Token token;
            while (!lexer._hitEOF){
                token = lexer.nextToken();
                System.out.println("Token: " + "<Classe: " + lexer.getVocabulary().getDisplayName(token.getType()) + ", Lexema: " + token.getText() + ">");
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}