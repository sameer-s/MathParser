package ssuri.cassystem.tree.node.function;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.tree.node.Node;
import ssuri.cassystem.tree.node.operator.DivisionNode;

public class NaturalLogNode extends Node
{
    private Node operand;

    public NaturalLogNode(Node operand)
    {
        this.operand = operand;
    }
    
    public Node getOperand()
    {
        return operand;
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> operator = new TreeItem<>("Natural Log");
        operator.getChildren().add(getGuiItem(operand));
        operator.setExpanded(true);
        return operator;
    }
    
    @Override
    public String toString()
    {
        return String.format("ln(%s)", operand);
    }
    
    @Override
    public String getInfixString()
    {
        return String.format("ln(%s)", operand.getInfixString());
    }
    
    @Override
    public Node derive()
    {
        return new DivisionNode(operand.derive(), operand);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(other == this) return true;
        return other instanceof NaturalLogNode && operand.equals(((NaturalLogNode) other).operand);
    }

    @Override
    public Node simplify()
    {
        return new NaturalLogNode(operand.simplify());
    }
}
