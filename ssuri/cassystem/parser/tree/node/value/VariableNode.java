package ssuri.cassystem.parser.tree.node.value;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.parser.tree.node.Node;

public class VariableNode extends Node
{
    public String name;
    public VariableNode(String name)
    {
        this.name = name;
    }

    @Override
    public Node simplify()
    {
        return this;
    }
    
    @Override
    public String toString()
    {
        return "{" + name + "}";
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<String>("Variable " + toString());
        return item;
    }

    @Override
    public int compareToSameType(Node other)
    {
        return name.compareTo(((VariableNode) other).name);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VariableNode other = (VariableNode) obj;
        if (name == null)
        {
            if (other.name != null) return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }
}
