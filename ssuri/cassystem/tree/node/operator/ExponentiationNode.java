package ssuri.cassystem.tree.node.operator;

import ssuri.cassystem.tree.node.Node;
import ssuri.cassystem.tree.node.function.NaturalLogNode;
import ssuri.cassystem.tree.node.value.ConstantNode;
import ssuri.cassystem.tree.node.value.VariableNode;

public class ExponentiationNode extends BinaryOperatorNode
{
    public ExponentiationNode(Node base, Node exponent)
    {
        super(base, exponent);
    }
    
    public Node getBase()
    {
        return getFirstOperand();
    }
    
    public Node getExponent()
    {
        return getSecondOperand();
    }
    
    @Override
    public String getReadableName()
    {
        return "Exponentiation";
    }

    @Override
    public String getShortName()
    {
        return "pow";
    }

    @Override
    public String getSymbol()
    {
        return "^";
    }

    @Override
    public Node derive()
    {
        // b(x)^e(x) -> (b(x)^e(x))*(e(x)*ln(b(x)))'
        Node lnb = new NaturalLogNode(getBase());
        Node elnb = new MultiplicationNode(getExponent(), lnb);
        Node elnbPrime = elnb.derive();
        return new MultiplicationNode(this, elnbPrime);    
    }

    @Override
    public Node simplifyThis()
    {
        if(getBase().equals(ConstantNode.ZERO) && !getExponent().equals(ConstantNode.ZERO))
        {
            return ConstantNode.ZERO;
        }
        if(getBase().equals(ConstantNode.ONE))
        {
            return ConstantNode.ONE;
        }
        if(getExponent().equals(ConstantNode.ONE))
        {
            return getBase();
        }
        if(getExponent().equals(ConstantNode.ZERO) && !getBase().equals(ConstantNode.ZERO))
        {
            return ConstantNode.ONE;
        }

        return this;
    }

    public boolean isPolynomialTerm()
    {
        return getBase() instanceof VariableNode && getExponent() instanceof ConstantNode;
    }

    public String getPolynomialVariableName()
    {
        if(isPolynomialTerm())
        {
            return ((VariableNode) getBase()).getName();
        }
        else
        {
            return null;
        }
    }
}
