package ssuri.cassystem.tree.node.value;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.tree.node.Node;

public class VariableNode extends Node
{
    private String name;
    
    public VariableNode(String name)
    {
        this.name = name;
    }
  
    @Override
    public String toString()
    {
        return "{" + name + "}";
    }
    
    @Override
    public String getInfixString()
    {
        return name;
    }
    
    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<>("Variable " + toString());
        item.setExpanded(true);
        return item;
    }
    
    public String getName()
    {
        return name;
    }

    @Override
    public Node derive()
    {
        if(name.equals("x")) 
        {
            return ConstantNode.ONE;
        }
        else 
        {
            return ConstantNode.ZERO;
        }
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == this) return true;
        return other instanceof VariableNode && name.equals(((VariableNode) other).name);
    }

    @Override
    public Node simplify()
    {
        return this;
    }
}
