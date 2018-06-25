package ssuri.cassystem.parser.tree.node.operator;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.parser.tree.node.Node;

public class PowerNode extends Node
{
    public Node base, exponent;
    @Override
    public Node simplify()
    {   
        return this;
    }
    
    @Override
    public String toString()
    {
        return "power(" + base + ", " + exponent + ")";
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<String>("Power");
        item.getChildren().add(getGuiItem(base));
        item.getChildren().add(getGuiItem(exponent));
        return item;
    }

    @Override
    protected int compareToSameType(Node other)
    {
        PowerNode otherPower = (PowerNode) other;
        int baseCompare = base.compareTo(otherPower.base);
        if(baseCompare != 0)
        {
            return baseCompare;
        }
        return exponent.compareTo(otherPower.exponent);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + ((exponent == null) ? 0 : exponent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PowerNode other = (PowerNode) obj;
        if (base == null)
        {
            if (other.base != null) return false;
        }
        else if (!base.equals(other.base)) return false;
        if (exponent == null)
        {
            if (other.exponent != null) return false;
        }
        else if (!exponent.equals(other.exponent)) return false;
        return true;
    }
}
