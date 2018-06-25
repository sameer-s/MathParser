package ssuri.cassystem.parser.tree.node.operator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.TreeItem;
import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.parser.tree.node.Node;
import ssuri.cassystem.parser.tree.node.value.ConstantNode;

public class MultiplicationNode extends Node
{
    public List<Node> children = new LinkedList<>();

    @Override
    public Node makeCopy()
    {
        MultiplicationNode copy = new MultiplicationNode();
        for(Node child : children)
        {
            copy.children.add(child.makeCopy());
        }
        return copy;
    }
    
    @Override
    public Node simplify()
    {
        if(children.size() == 1)
        {
            return children.get(0).simplify();
        }
        
        // Levels multiplication operators (a * (b * c)) becomes (a * b * c)
        // Also reshuffles nodes so that division nodes cannot be children of multiplication nodes
        // May take multiple iterations
        MultiplicationNode childMultiplicationNode = null;
        DivisionNode childDivisionNode = null;
        ConstantNode childConstantNode1 = null, childConstantNode2 = null;
        
        for(Node child : children)
        {
            if(child instanceof MultiplicationNode)
            {
                childMultiplicationNode = (MultiplicationNode) child;
                break;
            }
            else if(child instanceof DivisionNode)
            {
                childDivisionNode = (DivisionNode) child;
                break;
            }
            else if(child instanceof ConstantNode)
            {
                if(childConstantNode1 == null)
                {
                    childConstantNode1 = (ConstantNode) child;
                }
                else
                {
                    childConstantNode2 = (ConstantNode) child;
                }
                
                ((ConstantNode) child).value.reduce();
            }
        }
        
        if(childMultiplicationNode != null)
        {
            // Level multiplication operator
            int i = children.indexOf(childMultiplicationNode);
            children.remove(i);
            for(int j = 0; j < childMultiplicationNode.children.size(); j++)
            {
               children.add(i + j, childMultiplicationNode.children.get(j));
            }     
            
            simplifyChildren();
            return this;
        }
        
        if(childDivisionNode != null)
        {
            // Reshuffle node to remove division node child
            // (a * (b/c)) -> ((a*b) / c)
            DivisionNode topDivisionNode = new DivisionNode();
            children.remove(childDivisionNode);
            children.add(childDivisionNode.numerator);
            topDivisionNode.numerator = this;
            topDivisionNode.denominator = childDivisionNode.denominator;
            
            return topDivisionNode.simplify();
        }

        if(childConstantNode1 != null && childConstantNode2 != null)
        {
            childConstantNode1.value = Fraction.multiply(childConstantNode1.value, childConstantNode2.value);
            children.remove(childConstantNode2);
            return this;
        }
        
        simplifyChildren();
        return this;
    }
    
    private void simplifyChildren()
    {
        for(int i = 0; i < children.size(); i++)
        {
            if(children.get(i) != null)
            {
                children.set(i, children.get(i).simplify());        
            }
        }
    }
    
    private void sort()
    {
        Collections.sort(children, (node1, node2) -> node1.compareTo(node2));
    }
    
    @Override
    public String toString()
    {
        if(children.size() == 0) return "mult()";
        
        String str = "mult(";
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
        TreeItem<String> item = new TreeItem<String>("Multiplication");
        for(Node child : children)
        {
            item.getChildren().add(getGuiItem(child));
        }
        return item;
    }

    @Override
    protected int compareToSameType(Node other)
    {
        MultiplicationNode otherMultiplication = ((MultiplicationNode) other);
        sort();
        otherMultiplication.sort();
        int sizeCompare = Integer.compare(children.size(), otherMultiplication.children.size());
        if(sizeCompare != 0)
        {
            return sizeCompare;
        }
        
        for(int i = 0; i < children.size(); i++)
        {
            int compare = children.get(i).compareTo(otherMultiplication.children.get(i));
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
        MultiplicationNode other = (MultiplicationNode) obj;
        if (children == null)
        {
            if (other.children != null) return false;
        }
        else if (!children.equals(other.children)) return false;
        return true;
    }

    @Override
    public Node derive()
    {
        AdditionNode derivative = new AdditionNode();
        
        return derivative;
    }
}
