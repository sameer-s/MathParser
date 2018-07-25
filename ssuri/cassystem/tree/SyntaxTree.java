package ssuri.cassystem.tree;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.tree.node.Node;

public class SyntaxTree
{
    private Node root;
    
    public SyntaxTree(Node root)
    {
        this.root = root;
    }

    @Override
    public String toString()
    {
        return root.toString();
    }
    
    public String getInfixString()
    {
        return root.getInfixString();
    }

    public SyntaxTree derive()
    {
        return new SyntaxTree(root.derive());
    }

    public SyntaxTree simplify()
    {
        SyntaxTree tree = new SyntaxTree(root);

        Node simplified = tree.root.simplify();
        while(!tree.root.equals(simplified))
        {
            tree.root = simplified;
            simplified = tree.root.simplify();
        }

        return tree;
    }

    public TreeItem<String> getGuiItem()
    {
        if(root == null)
        {
            return new TreeItem<> ("Tree root is null");
        }
        return root.getGuiItem();
    }
}
 