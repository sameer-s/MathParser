package ssuri.cassystem.parser.tree.node;

import javafx.scene.control.TreeItem;

public abstract class Node
{
    public abstract Node simplify();
    public abstract TreeItem<String> getGuiItem();
    public int compareTo(Node other)
    {  
        int compare = this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
                
        if(compare == 0)
        {
            return compareToSameType(other);
        }
        else
        {
            return compare;
        }
    }
    protected abstract int compareToSameType(Node other);
    
    protected static TreeItem<String> getGuiItem(Node node)
    {
        if(node == null) return new TreeItem<String>("null");
        
        return node.getGuiItem();
    }
    
    @Override public abstract int hashCode();
    @Override public abstract boolean equals(Object obj);
}