package AnalisadorLexico;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Parser {

    public static void  main (String[] args){
        String filename = System.getProperty("user.dir") + "\\AnalisadorLexico\\src\\main\\java\\fatorial.txt";
        try{
            CharStream input = CharStreams.fromFileName(filename);
            CuckooLexer lexer = new CuckooLexer(input);

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CuckooParser parser = new CuckooParser(tokens);

            ParseTree ast = parser.programa();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}