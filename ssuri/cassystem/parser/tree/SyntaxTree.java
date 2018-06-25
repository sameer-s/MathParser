package ssuri.cassystem.parser.tree;

import ssuri.cassystem.parser.tree.node.Node;

public class SyntaxTree
{
    public Node root = null;
    
    public SyntaxTree(Node root)
    {
        this.root = root;
    }
    
    public SyntaxTree simplify()
    {
        int oldHash;
        do
        {
            oldHash = root.hashCode();
            root = root.simplify();
        }
        while(root.hashCode() != oldHash);

        return this;
    }
    
    @Override
    public String toString()
    {
        return root.toString();
    }
}
 