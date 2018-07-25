package ssuri.cassystem.tree.node.operator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.tree.node.Node;
import ssuri.cassystem.tree.node.value.ConstantNode;

public class AdditionNode extends OperatorNode
{
    public AdditionNode(Node...operands)
    {
        super(operands);
    }

    public AdditionNode(Stream<Node> operandStream)
    {
        super(operandStream);
    }
    
    @Override
    public String getReadableName()
    {
        return "Addition";
    }

    @Override
    public String getShortName()
    {
        return "add";
    }

    @Override
    public String getSymbol()
    {
        return "+";
    }

    @Override
    public Node derive()
    {
        return new AdditionNode(getOperandStream().map(Node::derive));
    }

    @Override
    public Node simplifyThis()
    {
        if(getOperandCount() == 1)
        {
            return getOperand(0);
        }

        int previouslyMatchedConstantIndex = -1;
        Map<Map<String, Fraction>, Integer> previouslyMatchedMultiplicationIndices = new HashMap<>();

        for(int i = 0; i < getOperandCount(); i++)
        {
            Node operand = getOperand(i);
            if(operand instanceof ConstantNode)
            {
                if(operand.equals(ConstantNode.ZERO))
                {
                    Node[] newOperands = new Node[getOperandCount() - 1];
                    System.arraycopy(getOperands(), 0, newOperands, 0, i);
                    System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);
                    return new AdditionNode(newOperands);
                   }

                if(previouslyMatchedConstantIndex < 0)
                {
                    previouslyMatchedConstantIndex = i;
                }
                else
                {
                    Node[] newOperands = new Node[getOperandCount() - 1];
                    System.arraycopy(getOperands(), 0, newOperands, 0, i);
                    System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);
                    ConstantNode previouslyMatchedConstant = (ConstantNode) getOperand(previouslyMatchedConstantIndex);
                    ConstantNode constant = (ConstantNode) operand;
                    newOperands[previouslyMatchedConstantIndex] = new ConstantNode(Fraction.add(previouslyMatchedConstant.getValue(), constant.getValue()));
                    return new MultiplicationNode(newOperands);
                }
            }
            if(operand instanceof MultiplicationNode)
            {
                MultiplicationNode multiplicationOperand = (MultiplicationNode) operand;
                Map<String, Fraction> polynomialTerms = multiplicationOperand.getPolynomialTerms();

                if(polynomialTerms != null)
                {
                    if(previouslyMatchedMultiplicationIndices.containsKey(polynomialTerms))
                    {
                        int previouslyMatchedMultiplicationIndex = previouslyMatchedMultiplicationIndices.get(polynomialTerms);
                        MultiplicationNode previousMatchedMult = (MultiplicationNode) getOperand(previouslyMatchedMultiplicationIndex);

                        Node[] newOperands = new Node[getOperandCount() - 1];
                        System.arraycopy(getOperands(), 0, newOperands, 0, i);
                        System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);

                        Fraction operandMultiplier = multiplicationOperand.getConstantPart();

                        Node[] newMultOperands = new Node[previousMatchedMult.getOperandCount() + 1];
                        System.arraycopy(previousMatchedMult.getOperands(), 0, newMultOperands, 1, previousMatchedMult.getOperandCount());
                        newMultOperands[0] = new ConstantNode(operandMultiplier);
                        MultiplicationNode newMult = new MultiplicationNode(newMultOperands);

                        newOperands[previouslyMatchedMultiplicationIndex] = newMult;

                        return new AdditionNode(newOperands);
                    }
                    else
                    {
                        previouslyMatchedMultiplicationIndices.put(polynomialTerms, i);
                    }
                }
            }
            if(operand instanceof AdditionNode)
            {
                AdditionNode additionOperand = (AdditionNode) operand;
                Node[] newOperands = new Node[getOperandCount() - 1 + additionOperand.getOperandCount()];
                System.arraycopy(getOperands(), 0, newOperands, 0, i);
                System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);
                System.arraycopy(additionOperand.getOperands(), 0, newOperands, getOperandCount() - 1, additionOperand.getOperandCount());
                return new AdditionNode(newOperands);
            }
        }

        return this;
    }
}
