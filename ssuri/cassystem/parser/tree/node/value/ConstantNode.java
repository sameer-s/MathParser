package ssuri.cassystem.parser.tree.node.value;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.parser.tree.node.Node;

public class ConstantNode extends Node
{
    public Fraction value;
    
    public ConstantNode(Fraction value)
    {
        this.value = value;
    }

    @Override
    public Node makeCopy()
    {
        return new ConstantNode(new Fraction(value.numerator, value.denominator));
    }
    
    @Override
    public Node simplify()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return "[" + value.numerator.toString(10) + "/" + value.denominator.toString(10) + "]";
    }

    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<String>("Constant " + toString());
        return item;
    }

    @Override
    public int compareToSameType(Node other)
    {
        return value.compareTo(((ConstantNode) other).value);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConstantNode other = (ConstantNode) obj;
        if (value == null)
        {
            if (other.value != null) return false;
        }
        else if (!value.equals(other.value)) return false;
        return true;
    }

    @Override
    public Node derive()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
