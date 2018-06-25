package ssuri.cassystem.test;

import java.util.List;

import ssuri.cassystem.parser.Parser;
import ssuri.cassystem.parser.tokenizer.Token;
import ssuri.cassystem.parser.tokenizer.Tokenizer;
import ssuri.cassystem.parser.tree.SyntaxTree;

public class SimpleParserTest
{
    public static void main(String... args)
    {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.tokenize("(3 * 5 + (9 + x^2))");
        System.out.println(tokens);
        Parser parser = new Parser();
        SyntaxTree tree = parser.parse(tokens);
        System.out.println(tree);
    }
}
