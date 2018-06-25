package ssuri.cassystem.parser.tree.node.operator;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.parser.tree.node.Node;
import ssuri.cassystem.parser.tree.node.value.ConstantNode;

public class UnaryNegativeNode extends Node
{
    public Node child;

    @Override
    public Node makeCopy()
    {
        UnaryNegativeNode copy = new UnaryNegativeNode();
        copy.child = child.makeCopy();
        return copy;
    }
    
    @Override
    public Node simplify()
    {
        // Replaces unary negation (-a) with multiplication (-1 * a)
        MultiplicationNode newNode = new MultiplicationNode();

        newNode.children.add(new ConstantNode(new Fraction(-1, 1)));
        newNode.children.add(child);
        
        return newNode.simplify();
    }
    
    @Override
    public String toString()
    {
        return "negate(" + child + ")";
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<String>("Negation");
        item.getChildren().add(getGuiItem(child));
        return item;
    }

    @Override
    protected int compareToSameType(Node other)
    {
        UnaryNegativeNode otherUnaryNegative = (UnaryNegativeNode) other;
        return child.compareTo(otherUnaryNegative.child);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((child == null) ? 0 : child.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UnaryNegativeNode other = (UnaryNegativeNode) obj;
        if (child == null)
        {
            if (other.child != null) return false;
        }
        else if (!child.equals(other.child)) return false;
        return true;
    }

    @Override
    public Node derive()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
