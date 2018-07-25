package ssuri.cassystem.tree.node.value;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.tree.node.Node;

import java.math.BigInteger;

public class ConstantNode extends Node
{
    private Fraction value;

    public static final ConstantNode ONE = new ConstantNode(new Fraction(1, 1));
    public static final ConstantNode ZERO = new ConstantNode(new Fraction(0, 1));
    public static final ConstantNode NEGATIVE_ONE = new ConstantNode(new Fraction(-1, 1));
    public static final ConstantNode TWO = new ConstantNode(new Fraction(2, 1));

    public ConstantNode(Fraction value)
    {
        this.value = value;
    }

    public Fraction getValue()
    {
        return new Fraction(value.numerator, value.denominator);
    }
    
    @Override
    public String toString()
    {
        if(value.denominator.equals(BigInteger.ONE))
        {
            return value.numerator.toString();
        }

        return String.format("(%s / %s)", value.numerator, value.denominator);
    }

    @Override
    public String getInfixString()
    {
        return toString();
    }

    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<>("Constant " + toString());
        item.setExpanded(true);
        return item;
    }
    
    @Override
    public Node derive()
    {
        return ZERO;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == this) return true;
        return other instanceof ConstantNode && value.equals(((ConstantNode) other).value);
    }

    @Override
    public Node simplify()
    {
        BigInteger gcd = value.numerator.gcd(value.denominator);
        if(gcd.equals(BigInteger.ONE))
        {
            return this;
        }
        else
        {
            return new ConstantNode(value.reduce());
        }
    }
}
