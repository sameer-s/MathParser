package ssuri.cassystem.test;

import java.util.List;

import ssuri.cassystem.parser.Parser;
import ssuri.cassystem.parser.tokenizer.Token;
import ssuri.cassystem.parser.tokenizer.Tokenizer;
import ssuri.cassystem.tree.SyntaxTree;
import ssuri.cassystem.util.Log;

public class DerivativeTest
{
    public static void main(String... args)
    {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.tokenize("x^2 * 3*x");
        Log.i("Tokens: %s", tokens);
        Parser parser = new Parser();
        SyntaxTree tree = parser.parse(tokens);
        Log.i("Tree: %s", tree);
        Log.i("Tree to infix: %s", tree.getInfixString());
        SyntaxTree derivative = tree.derive();
        Log.i("Derived tree: %s", derivative);
        Log.i("Derived tree to infix: %s", derivative.getInfixString());
        SyntaxTree simpleDerivative = derivative.simplify();
        Log.i("Simplified derived tree: %s", simpleDerivative);
        Log.i("Simplified derived tree to infix: %s", simpleDerivative.getInfixString());
    }
}
