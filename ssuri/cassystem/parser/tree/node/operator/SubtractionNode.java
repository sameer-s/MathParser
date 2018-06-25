package ssuri.cassystem.parser.tree.node.operator;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.parser.tree.node.Node;
import ssuri.cassystem.parser.tree.node.value.ConstantNode;

public class SubtractionNode extends Node
{
    public Node positive, negative;

    @Override
    public Node makeCopy()
    {
        SubtractionNode copy = new SubtractionNode();
        copy.positive = positive.makeCopy();
        copy.negative = negative.makeCopy();
        return copy;
    }
    
    @Override
    public Node simplify()
    {
        // Replaces subtraction (a - b) with addition & multiplication (a + (-1 * b))
        MultiplicationNode newMultiplicationNode = new MultiplicationNode();
        newMultiplicationNode.children.add(new ConstantNode(new Fraction(-1, 1)));
        newMultiplicationNode.children.add(negative);
        
        AdditionNode newAdditionNode = new AdditionNode();
        newAdditionNode.children.add(positive);
        newAdditionNode.children.add(newMultiplicationNode);
        
        return newAdditionNode.simplify();
    }

    @Override
    public String toString()
    {
        return "subtract(" + positive + ", " + negative + ")";
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<String>("Subtraction");
        item.getChildren().add(getGuiItem(positive));
        item.getChildren().add(getGuiItem(negative));
        return item;
    }
    
    @Override
    protected int compareToSameType(Node other)
    {
        SubtractionNode otherSubtraction = (SubtractionNode) other;
        int positiveCompare = positive.compareTo(positive);
        if(positiveCompare != 0)
        {
            return positiveCompare;
        }
        return negative.compareTo(otherSubtraction.negative);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((negative == null) ? 0 : negative.hashCode());
        result = prime * result + ((positive == null) ? 0 : positive.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SubtractionNode other = (SubtractionNode) obj;
        if (negative == null)
        {
            if (other.negative != null) return false;
        }
        else if (!negative.equals(other.negative)) return false;
        if (positive == null)
        {
            if (other.positive != null) return false;
        }
        else if (!positive.equals(other.positive)) return false;
        return true;
    }

    @Override
    public Node derive()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
