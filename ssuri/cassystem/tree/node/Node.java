package ssuri.cassystem.tree.node;

import javafx.scene.control.TreeItem;

public abstract class Node
{
    public abstract TreeItem<String> getGuiItem();
       
    protected static TreeItem<String> getGuiItem(Node node)
    {
        if(node == null) return new TreeItem<>("null");
        
        return node.getGuiItem();
    }
        
    @Override public abstract boolean equals(Object other);

    @Override public abstract String toString();
    public abstract String getInfixString();
    
    public abstract Node derive();
    public abstract Node simplify();
}