package ssuri.cassystem.tree.node.operator;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.tree.node.Node;

import java.util.Arrays;
import java.util.stream.Stream;

public abstract class OperatorNode extends Node implements Cloneable
{
    private Node[] operands;
    
    protected OperatorNode(Node... operands)
    {
        if(operands.length == 0)
        {
            throw new IllegalArgumentException("Operator node must have operands");
        }

        this.operands = operands;
    }
    
    protected OperatorNode(Stream<Node> operandStream)
    {
        this(operandStream.toArray(Node[]::new));
    }
    
    public int getOperandCount()
    {
        return operands.length;
    }
    
    public Node getOperand(int index)
    {
        return operands[index];
    }
    
    public Stream<Node> getOperandStream()
    {
        return Arrays.stream(operands);
    }
    
    public Node[] getOperands()
    {
        // return a copy
        return getOperandStream().toArray(Node[]::new);
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        String operatorName = getReadableName();
        TreeItem<String> operator = new TreeItem<>(operatorName);
        for(Node operand : operands)
        {
            operator.getChildren().add(getGuiItem(operand));
        }
        operator.setExpanded(true);
        return operator;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getShortName());
        sb.append("(");
        for (int i = 0; i < operands.length; i++)
        {
            sb.append(operands[i]);
            if(i != operands.length - 1)
            {
                sb.append(", ");
            }
        }
        sb.append(")");
        
        return sb.toString();
    }
    
    @Override
    public String getInfixString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < operands.length; i++)
        {
            sb.append(operands[i].getInfixString());
            if(i != operands.length - 1)
            {
                sb.append(" ");
                sb.append(getSymbol());
                sb.append(" ");
            }
        }
        
        return sb.toString();
    }
    
    protected boolean hasEqualOperandArray(OperatorNode other)
    {
        if(this.getOperandCount() != other.getOperandCount())
        {
            return false;
        }
        
        // TODO: Re-implement this should be faster than O(n^2)
        Node[] thisOperands = this.getOperands();
        Node[] otherOperands = other.getOperands();

        for (int i = 0; i < thisOperands.length; i++)
        {
            boolean anyMatched = false;

            for (int j = 0; j < otherOperands.length; j++)
            {
                if(thisOperands[i].equals(otherOperands[j]))
                {
                    otherOperands[j] = null;
                    anyMatched = true;
                    break;
                }
            }

            if(!anyMatched)
            {
                return false;
            }
        }
        
        return true;
    }
    
    public abstract String getReadableName();
    public abstract String getShortName();
    public abstract String getSymbol();

    @Override
    public final Node simplify()
    {
        Node simplified = simplifyThis();
        if(!this.equals(simplified))
        {
            return simplified;
        }

        for(int i = 0; i < getOperandCount(); i++)
        {
            Node operand = getOperand(i);
            Node simplifiedOperand = operand.simplify();
            if(!operand.equals(simplifiedOperand))
            {
                Node[] newOperands = getOperands(); // returns a copy so it's okay
                newOperands[i] = simplifiedOperand;

                OperatorNode newNode;
                try
                {
                    newNode = (OperatorNode) this.clone();
                }
                catch (CloneNotSupportedException e)
                {
                    throw new RuntimeException(e);
                }

                newNode.operands = newOperands; // TODO: find a better way of doing this
                return newNode;
            }
        }

        return this;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == this)
        {
            return true;
        }
        return this.getClass().isInstance(other) && this.hasEqualOperandArray((OperatorNode) other);
    }

    public abstract Node simplifyThis();
}
