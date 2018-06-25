package ssuri.cassystem.test;

import ssuri.cassystem.gui.SyntaxTreeRenderer;
import ssuri.cassystem.parser.Parser;
import ssuri.cassystem.parser.tokenizer.Tokenizer;
import ssuri.cassystem.parser.tree.SyntaxTree;

public class ParserRendererTest extends SyntaxTreeRenderer
{
    private static String[] strings = 
    {
            "3/2*x^2",
            "(3/2)*x^2",
            "3*x^2 + x^3 + 17 + 5*x",
            "x^3 + 3*x^2 + 5*x + 17",
            "17 + 3*x^2 + 5*x + x^3",
            "5*x + x^3 + 3*x^2 + 17"
    };
    private static SyntaxTree[] trees = new SyntaxTree[strings.length];
    static
    {        
        for(int i = 0; i < strings.length; i++)
        {
            trees[i] = new Parser().parse(new Tokenizer().tokenize(strings[i]), true);
        }
    }
    
    public ParserRendererTest()
    {    
        super(trees);
    }
    
   
    public static void main(String... args)
    {
        launch(args);
    }
}
