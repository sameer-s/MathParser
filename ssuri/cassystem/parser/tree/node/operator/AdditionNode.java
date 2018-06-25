package ssuri.cassystem.parser.tree.node.operator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.parser.tree.node.Node;

public class AdditionNode extends Node
{    
    public List<Node> children = new LinkedList<>();

    @Override
    public Node simplify()
    {
        // Levels addition operators (a + (b + c)) becomes (a + b + c)
        // May take multiple iterations
        AdditionNode childAdditionNode = null;
        for(Node child : children)
        {
            if(child instanceof AdditionNode)
            {
                childAdditionNode = (AdditionNode) child;
                break;
            }
        } 
        
        if(childAdditionNode != null)
        {
            int i = children.indexOf(childAdditionNode);
            children.remove(i);
            for(int j = 0; j < childAdditionNode.children.size(); j++)
            {
               children.add(i + j, childAdditionNode.children.get(j));
            }            
        }
        
        for(int i = 0; i < children.size(); i++)
        {
            if(children.get(i) != null)
            {
                children.set(i, children.get(i).simplify());        
            }
        }
        
        sort();
        return this;
    }
    
    private void sort()
    {
        Collections.sort(children, (node1, node2) -> node1.compareTo(node2));
    }
    
    @Override
    public String toString()
    {
        if(children.size() == 0) return "add()";
        
        String str = "add(";
        for(int i = 0; i < children.size() - 1; i++)
        {
            str += children.get(i);
            str += ", ";
        }
        str += children.get(children.size() - 1);
        str += ")";
        
        return str;
    }

    @Override
    public TreeItem<String> getGuiItem()
    {
        TreeItem<String> item = new TreeItem<String>("Addition");
        for(Node child : children)
        {
            item.getChildren().add(getGuiItem(child));
        }
        return item;
    }

    @Override
    protected int compareToSameType(Node other)
    {
        AdditionNode otherAddition = ((AdditionNode) other);
        sort();
        otherAddition.sort();
        int sizeCompare = Integer.compare(children.size(), otherAddition.children.size());
        if(sizeCompare != 0)
        {
            return sizeCompare;
        }
        
        for(int i = 0; i < children.size(); i++)
        {
            int compare = children.get(i).compareTo(otherAddition.children.get(i));
            if(compare != 0)
            {
                return compare;
            }
        }
       
        return 0;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AdditionNode other = (AdditionNode) obj;
        if (children == null)
        {
            if (other.children != null) return false;
        }
        else if (!children.equals(other.children)) return false;
        return true;
    }
}
