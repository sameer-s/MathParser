package ssuri.cassystem.tree.node.operator;

import ssuri.cassystem.tree.node.Node;
import ssuri.cassystem.tree.node.value.ConstantNode;

public class SubtractionNode extends BinaryOperatorNode
{
    public SubtractionNode(Node positive, Node negative)
    {
        super(positive, negative);
    }
    
    public Node getPositive()
    {
        return getFirstOperand();
    }
    
    public Node getNegative()
    {
        return getSecondOperand();
    }
    
    @Override
    public String getReadableName()
    {
        return "Subtraction";
    }

    @Override
    public String getShortName()
    {
        return "sub";
    }

    @Override
    public String getSymbol()
    {
        return "-";
    }

    @Override
    public Node derive()
    {
        return new SubtractionNode(getPositive().derive(), getNegative().derive());
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(other == this) return true;
        return other instanceof SubtractionNode && this.hasEqualOperandArray((SubtractionNode) other);
    }
    
    @Override
    public Node simplifyThis()
    {
        return new AdditionNode(getPositive(), new MultiplicationNode(ConstantNode.NEGATIVE_ONE, getNegative()));
    }
}
