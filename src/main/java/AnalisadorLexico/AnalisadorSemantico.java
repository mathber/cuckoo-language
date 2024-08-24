package AnalisadorLexico;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class AnalisadorSemantico {
    public static void main(String[] args){

        String filename = System.getProperty("user.dir") + "\\AnalisadorLexico\\src\\main\\java\\fatorial.txt";

        CuckooParser parser = getParser(filename);
        ParseTree ast = parser.programa();

        MyListener listener = new MyListener();
        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(listener, ast);

    }

    private static CuckooParser getParser(String fileName){
        CuckooParser parser = null;
        try {
            CharStream input = CharStreams.fromFileName(fileName);
            CuckooLexer lexer = new CuckooLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            parser = new CuckooParser(tokens);
        } catch (IOException e){
            e.printStackTrace();
        }
        return parser;
    }
}
