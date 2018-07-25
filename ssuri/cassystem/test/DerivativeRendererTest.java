package ssuri.cassystem.test;

import ssuri.cassystem.gui.SyntaxTreeRenderer;
import ssuri.cassystem.parser.Parser;
import ssuri.cassystem.parser.tokenizer.Tokenizer;
import ssuri.cassystem.tree.SyntaxTree;

public class DerivativeRendererTest extends SyntaxTreeRenderer
{
    private static String[] strings = 
    {
            "x^2 * 3*x",
    };
    private static SyntaxTree[] trees = new SyntaxTree[strings.length];
    static
    {     
        for(int i = 0; i < strings.length; i++)
        {
            trees[i] = new Parser().parse(new Tokenizer().tokenize(strings[i])).derive();
        }
    }
    
    public DerivativeRendererTest()
    {    
        super(trees);
    }
    
   
    public static void main(String... args)
    {
        launch(args);
    }
}
