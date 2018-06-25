package ssuri.cassystem.math;

import ssuri.cassystem.parser.tree.SyntaxTree;
import ssuri.cassystem.parser.tree.node.Node;

public class ExpressionManipulator
{
    public SyntaxTree derive(SyntaxTree tree)
    {
        return new SyntaxTree(derive(tree.root));
    }
    
    private Node derive(Node node)
    {
        
    }
}
