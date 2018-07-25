package ssuri.cassystem.tree.node.operator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.tree.node.Node;
import ssuri.cassystem.tree.node.value.ConstantNode;
import ssuri.cassystem.tree.node.value.VariableNode;

public class MultiplicationNode extends OperatorNode
{
    public MultiplicationNode(Node...operands)
    {
        super(operands);
    }

    public MultiplicationNode(Stream<Node> operandStream)
    {
        super(operandStream);
    }
    
    @Override public String getReadableName()
    {
        return "Multiplication";
    }

    @Override
    public String getShortName()
    {
        return "mult";
    }

    @Override
    public String getSymbol()
    {
        return "*";
    }

    @Override
    public Node derive()
    {                
        OperatorNode[] multiplicationNodes = new OperatorNode[getOperandCount()];
        for(int i = 0; i < getOperandCount(); i++)
        {
            Node[] childNodes = new Node[getOperandCount()];
            for(int j = 0; j < getOperandCount(); j++)
            {
                if(i != j)
                {
                    childNodes[j] = getOperand(j);
                }
                else
                {
                    childNodes[j] = getOperand(j).derive();
                }
            }

            multiplicationNodes[i] = new MultiplicationNode(childNodes);
        }
        return new AdditionNode(multiplicationNodes);
    }
    
    @Override
    public Node simplifyThis()
    {
        if(getOperandCount() == 1)
        {
            return getOperand(0);
        }

        int previouslyMatchedConstantIndex = -1;
        Map<String, Integer> previouslyMatchedVariableIndices = new HashMap<>();

        for(int i = 0; i < getOperandCount(); i++)
        {
            Node operand = getOperand(i);
            if(operand instanceof ConstantNode)
            {
                if(operand.equals(ConstantNode.ZERO))
                {
                    return ConstantNode.ZERO;
                }
                if(operand.equals(ConstantNode.ONE))
                {
                    Node[] newOperands = new Node[getOperandCount() - 1];
                    System.arraycopy(getOperands(), 0, newOperands, 0, i);
                    System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);
                    return new MultiplicationNode(newOperands);
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
                    newOperands[previouslyMatchedConstantIndex] = new ConstantNode(Fraction.multiply(previouslyMatchedConstant.getValue(), constant.getValue()));
                    return new MultiplicationNode(newOperands);
                }
            }
            if(operand instanceof VariableNode)
            {
                VariableNode variableOperand = (VariableNode) operand;
                String varName = variableOperand.getName();
                if(previouslyMatchedVariableIndices.containsKey(varName))
                {
                    int previouslyMatchedVariableIndex = previouslyMatchedVariableIndices.get(varName);
                    Node previouslyMatched = getOperand(previouslyMatchedVariableIndex);

                    Node newNode = null;
                    if(previouslyMatched instanceof VariableNode)
                    {
                        newNode = new ExponentiationNode(variableOperand, ConstantNode.TWO);
                    }
                    if(previouslyMatched instanceof ExponentiationNode)
                    {
                        ExponentiationNode previouslyMatchedPolynomialTerm = (ExponentiationNode) previouslyMatched;
                        Fraction previousExponent = ((ConstantNode) previouslyMatchedPolynomialTerm.getExponent()).getValue();
                        Fraction newExponent = Fraction.add(previousExponent, new Fraction(1, 1));
                        newNode = new ExponentiationNode(variableOperand, new ConstantNode(newExponent));
                    }

                    Node[] newOperands = new Node[getOperandCount() - 1];
                    System.arraycopy(getOperands(), 0, newOperands, 0, i);
                    System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);
                    newOperands[previouslyMatchedVariableIndex] = newNode;
                    return new MultiplicationNode(newOperands);
                }
                else
                {
                    previouslyMatchedVariableIndices.put(varName, i);
                }
            }
            if(operand instanceof ExponentiationNode && ((ExponentiationNode) operand).isPolynomialTerm())
            {
                ExponentiationNode polynomialTermOperand = (ExponentiationNode) operand;
                String varName = ((VariableNode) polynomialTermOperand.getBase()).getName();

                if(previouslyMatchedVariableIndices.containsKey(varName))
                {
                    int previouslyMatchedVariableIndex = previouslyMatchedVariableIndices.get(varName);
                    Node previouslyMatched = getOperand(previouslyMatchedVariableIndex);

                    Node newNode = null;
                    if(previouslyMatched instanceof VariableNode)
                    {
                        VariableNode previouslyMatchedVariable = (VariableNode) previouslyMatched;
                        Fraction previousExponent = ((ConstantNode) polynomialTermOperand.getExponent()).getValue();
                        Fraction newExponent = Fraction.add(previousExponent, new Fraction(1, 1));
                        newNode = new ExponentiationNode(previouslyMatchedVariable, new ConstantNode(newExponent));
                    }
                    if(previouslyMatched instanceof ExponentiationNode)
                    {
                        ExponentiationNode previouslyMatchedPolynomialTerm = (ExponentiationNode) previouslyMatched;
                        Fraction previousExponent = ((ConstantNode) polynomialTermOperand.getExponent()).getValue();
                        Fraction otherPreviousExponent = ((ConstantNode) previouslyMatchedPolynomialTerm.getExponent()).getValue();
                        Fraction newExponent = Fraction.add(previousExponent, otherPreviousExponent);
                        newNode = new ExponentiationNode(polynomialTermOperand.getBase(), new ConstantNode(newExponent));
                    }

                    Node[] newOperands = new Node[getOperandCount() - 1];
                    System.arraycopy(getOperands(), 0, newOperands, 0, i);
                    System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);
                    newOperands[previouslyMatchedVariableIndex] = newNode;
                    return new MultiplicationNode(newOperands);
                }
                else
                {
                    previouslyMatchedVariableIndices.put(varName, i);
                }

            }
            if(operand instanceof MultiplicationNode)
            {
                MultiplicationNode multiplicationOperand = (MultiplicationNode) operand;
                Node[] newOperands = new Node[getOperandCount() - 1 + multiplicationOperand.getOperandCount()];
                System.arraycopy(getOperands(), 0, newOperands, 0, i);
                System.arraycopy(getOperands(), i + 1, newOperands, i, getOperandCount() - i - 1);
                System.arraycopy(multiplicationOperand.getOperands(), 0, newOperands, getOperandCount() - 1, multiplicationOperand.getOperandCount());
                return new MultiplicationNode(newOperands);
            }
            if(operand instanceof DivisionNode)
            {
                DivisionNode divisionOperand = (DivisionNode) operand;
                Node[] numeratorChildren = getOperands(); // ok because it gives you a copy
                numeratorChildren[i] = divisionOperand.getNumerator();
                return new DivisionNode(new MultiplicationNode(numeratorChildren), divisionOperand.getDenominator());
            }
        }

        return this;
    }

    public Map<String, Fraction> getPolynomialTerms()
    {
        Map<String, Fraction> termMap = new HashMap<>();

        for (Node operand : getOperands())
        {
            if(operand instanceof VariableNode)
            {
                VariableNode variable = (VariableNode) operand;
                String variableName = variable.getName();
                termMap.put(variableName, Fraction.add(termMap.getOrDefault(variableName, new Fraction(0, 1)), new Fraction(1, 1)));
            }
            else if(operand instanceof ExponentiationNode && ((ExponentiationNode) operand).isPolynomialTerm())
            {
                ExponentiationNode polynomialTerm = (ExponentiationNode) operand;
                String variableName = ((VariableNode) polynomialTerm.getBase()).getName();
                Fraction exponent = ((ConstantNode) polynomialTerm.getExponent()).getValue();
                termMap.put(variableName, Fraction.add(termMap.getOrDefault(variableName, new Fraction(0, 1)), exponent));
            }
            else if(!(operand instanceof ConstantNode))
            {
                return null;
            }
        }

        return termMap;
    }

    public Fraction getConstantPart()
    {
        Fraction current = new Fraction(1, 1);
        for(Node operand : getOperands())
        {
            if(operand instanceof ConstantNode)
            {
                current = Fraction.multiply(current, ((ConstantNode) operand).getValue());
            }
        }
        return current;
    }
}
