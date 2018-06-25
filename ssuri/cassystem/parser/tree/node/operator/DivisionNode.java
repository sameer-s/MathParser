package ssuri.cassystem.parser.tree.node.operator;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.parser.tree.node.Node;
import ssuri.cassystem.parser.tree.node.value.ConstantNode;

public class DivisionNode extends Node
{
    public Node numerator, denominator;
    
    @Override
    public Node makeCopy()
    {
        DivisionNode copy = new DivisionNode();
        copy.numerator = numerator.makeCopy();
        copy.denominator = denominator.makeCopy();
        return copy;
    }
    
    @Override
    public Node simplify()
    {
        // Disallow a division operator being a child of a division operator
        if(numerator instanceof DivisionNode)
        {
            // ((a / b) / c) becomes (a / (b * c))
            Node newNumerator = ((DivisionNode) numerator).numerator;
            MultiplicationNode newDenominator = new MultiplicationNode();
            newDenominator.children.add(((DivisionNode) numerator).denominator);
            newDenominator.children.add(denominator);
            
            numerator = newNumerator;
            denominator = newDenominator;
        }
        if(denominator instanceof DivisionNode)
        {
            // (a / (b / c)) becomes ((a * c) / b)
            MultiplicationNode newNumerator = new MultiplicationNode();
            Node newDenominator = ((DivisionNode) denominator).numerator;
            newNumerator.children.add(numerator);
            newNumerator.children.add(((DivisionNode) denominator).denominator);
            
            numerator = newNumerator;
            denominator = newDenominator;
        }
        
        if(numerator != null)
        {
            numerator = numerator.simplify();
        }
        if(denominator != null)
        {
            denominator = denominator.simplify();
        }
        
        return this;
    }
        
    @Override
    public String toString()
    {
        return "divide(" + numerator + ", " + denominator + ")";
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<String>("Division");
        item.getChildren().add(getGuiItem(numerator));
        item.getChildren().add(getGuiItem(denominator));
        return item;
    }

    @Override
    protected int compareToSameType(Node other)
    {
        DivisionNode otherDivision = (DivisionNode) other;
        int numeratorCompare = numerator.compareTo(otherDivision.numerator);
        if(numeratorCompare != 0)
        {
            return numeratorCompare;
        }
        return denominator.compareTo(otherDivision.denominator);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((denominator == null) ? 0 : denominator.hashCode());
        result = prime * result + ((numerator == null) ? 0 : numerator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DivisionNode other = (DivisionNode) obj;
        if (denominator == null)
        {
            if (other.denominator != null) return false;
        }
        else if (!denominator.equals(other.denominator)) return false;
        if (numerator == null)
        {
            if (other.numerator != null) return false;
        }
        else if (!numerator.equals(other.numerator)) return false;
        return true;
    }

    @Override
    public Node derive()
    {
        // ((l * dh) - (h * dl) / (l * l))
        MultiplicationNode ldh = new MultiplicationNode();
        ldh.children.add(denominator.makeCopy());
        ldh.children.add(numerator.makeCopy().derive());
        MultiplicationNode hdl = new MultiplicationNode();
        hdl.children.add(numerator.makeCopy());
        hdl.children.add(denominator.makeCopy().derive());
        SubtractionNode top = new SubtractionNode();
        top.positive = ldh;
        top.negative = hdl;
        PowerNode bottom = new PowerNode();
        bottom.base = denominator.makeCopy();
        bottom.exponent = new ConstantNode(new Fraction(2, 1));
        DivisionNode derivative = new DivisionNode();
        derivative.numerator = top;
        derivative.denominator = bottom;
        return derivative;
    }
}
