package ssuri.cassystem.tree.node.operator;

import ssuri.cassystem.tree.node.Node;
import ssuri.cassystem.tree.node.value.ConstantNode;
import ssuri.cassystem.tree.node.value.VariableNode;

public class DivisionNode extends BinaryOperatorNode
{
    public DivisionNode(Node numerator, Node denominator)
    {
        super(numerator, denominator);
    }
    
    public Node getNumerator()
    {
        return getFirstOperand();
    }
    
    public Node getDenominator()
    {
        return getSecondOperand();
    }
    
    @Override
    public String getReadableName()
    {
        return "Division";
    }

    @Override
    public String getShortName()
    {
        return "div";
    }

    @Override
    public String getSymbol()
    {
        return "/";
    }

    @Override
    public Node derive()
    {
        // derivative of h/l --> (ldh - hdl) / (l^2)
        Node ldh = new MultiplicationNode(getDenominator(), getNumerator().derive());
        Node hdl = new MultiplicationNode(getNumerator(), getDenominator().derive());
        Node numerator = new SubtractionNode(ldh, hdl);
        Node denominator = new ExponentiationNode(getDenominator(), ConstantNode.TWO);
        return new DivisionNode(numerator, denominator);
    }

    @Override
    public Node simplifyThis()
    {
        if(this.getNumerator() instanceof DivisionNode)
        {
            DivisionNode numerator = (DivisionNode) this.getNumerator();
            return new DivisionNode(numerator.getNumerator(), new MultiplicationNode(numerator.getDenominator(), this.getDenominator()));
        }
        if(this.getDenominator() instanceof DivisionNode)
        {
            DivisionNode denominator = (DivisionNode) this.getDenominator();
            return new DivisionNode(new MultiplicationNode(this.getNumerator(), denominator.getNumerator()), denominator.getDenominator());
        }

        if(this.getDenominator() instanceof VariableNode)
        {
            VariableNode denominator = (VariableNode) this.getDenominator();
            return new MultiplicationNode(this.getNumerator(), new ExponentiationNode(denominator, ConstantNode.NEGATIVE_ONE));
        }
        if(this.getDenominator() instanceof ExponentiationNode)
        {
            ExponentiationNode denominator = (ExponentiationNode) this.getDenominator();
            return new MultiplicationNode(this.getNumerator(), new ExponentiationNode(denominator.getBase(), new MultiplicationNode(ConstantNode.NEGATIVE_ONE, denominator.getExponent())));
        }
        return this;
    }
}
