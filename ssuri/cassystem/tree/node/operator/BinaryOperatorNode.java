package ssuri.cassystem.tree.node.operator;

import ssuri.cassystem.tree.node.Node;

public abstract class BinaryOperatorNode extends OperatorNode
{
    protected BinaryOperatorNode(Node operand1, Node operand2)
    {
        super(operand1, operand2);
    }
    
    public Node getFirstOperand()
    {
        return getOperand(0);
    }
    
    public Node getSecondOperand()
    {
        return getOperand(1);
    }

    @Override
    protected boolean hasEqualOperandArray(OperatorNode other)
    {
        if(other instanceof BinaryOperatorNode)
        {
            BinaryOperatorNode otherBinary = (BinaryOperatorNode) other;
            return getFirstOperand().equals(otherBinary.getFirstOperand()) &&
                    getSecondOperand().equals(otherBinary.getSecondOperand());
        }
        return super.hasEqualOperandArray(other);
    }
}
