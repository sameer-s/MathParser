package ssuri.cassystem.parser;

import static ssuri.cassystem.parser.tokenizer.TokenType.OPEN_PARENTHESIS;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.parser.tokenizer.Token;
import ssuri.cassystem.parser.tokenizer.TokenType;
import ssuri.cassystem.parser.tree.SyntaxTree;
import ssuri.cassystem.parser.tree.node.Node;
import ssuri.cassystem.parser.tree.node.operator.AdditionNode;
import ssuri.cassystem.parser.tree.node.operator.DivisionNode;
import ssuri.cassystem.parser.tree.node.operator.MultiplicationNode;
import ssuri.cassystem.parser.tree.node.operator.PowerNode;
import ssuri.cassystem.parser.tree.node.operator.SubtractionNode;
import ssuri.cassystem.parser.tree.node.operator.UnaryNegativeNode;
import ssuri.cassystem.parser.tree.node.value.ConstantNode;
import ssuri.cassystem.parser.tree.node.value.VariableNode;

public class Parser
{
    public SyntaxTree parse(List<Token> tokensToParse)
    {
        return parse(tokensToParse, true);
    }
    
    public SyntaxTree parse(List<Token> tokensToParse, boolean doSimplify)
    {
        Node root = null;
        
        List<Token> tokens = new LinkedList<>(tokensToParse); // copy list to avoid side effects
        Deque<Token> outputQueue = new ArrayDeque<>();
        Deque<Token> operatorStack = new ArrayDeque<>();

        while(tokens.size() > 0)
        {
            Token token = tokens.remove(0);
            switch(token.type)
            {
                case INTEGER:
                case VARIABLE:
                    outputQueue.addLast(token);
                    break;
                case OPEN_PARENTHESIS:
                case UNARY_MINUS:
                    operatorStack.push(token);
                    break;
                case CLOSE_PARENTHESIS:
                    while(true)
                    {
                        Token newToken = operatorStack.pop();
                        if(newToken.type == OPEN_PARENTHESIS) break;
                        else outputQueue.addLast(newToken);
                    }
                    break;
                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE:
                case POWER:
                    while(true)
                    {
                        Token topOfStack = operatorStack.peek();

                        if(topOfStack == null || topOfStack.type == OPEN_PARENTHESIS)
                        {
                            operatorStack.push(token);
                            break;
                        }
                        
                        // topOfStack must be an operator; only parentheses and operators end up on the stack
                        int compare = Integer.compare(token.getPrecedence(), topOfStack.getPrecedence());
                        if(compare > 0 || (compare == 0 && !token.isRightAssociative()))
                        {
                            operatorStack.push(token);
                            break;
                        }
                        
                        outputQueue.addLast(operatorStack.pop());
                    }
            }
        }
        
        while(operatorStack.size() > 0)
        {
            outputQueue.addLast(operatorStack.pop());
        }
        
        System.out.println(outputQueue);
        
        // Step 2: Read values from the stack and apply operators in creating the tree
        // Eventually, integrate this with the previous part (to make it more efficient),
        // generating the tree as the tokens are initially read.
        Deque<Node> valueStack = new ArrayDeque<>();
        while(outputQueue.size() > 0)
        {
            Token token = outputQueue.removeFirst();
         
            switch(token.type)
            {
                case OPEN_PARENTHESIS:
                case CLOSE_PARENTHESIS:
                    throw new IllegalStateException("Parentheses should be removed during shunting yard");
                case INTEGER:
                    valueStack.push(new ConstantNode(new Fraction(Integer.parseInt(token.string), 1)));
                    break;
                case VARIABLE:
                    valueStack.push(new VariableNode(token.string));
                    break;
                case UNARY_MINUS:
                    UnaryNegativeNode unn = new UnaryNegativeNode();
                    if(root == null) unn.child = valueStack.pop();
                    else unn.child = root;
                    root = unn;
                    break;
                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE:
                case POWER:
                    if(root == null)
                    {
                        root = getBinaryOperatorNode(token.type, valueStack.pop(), valueStack.pop());
                    }
                    else if(valueStack.size() == 1)
                    {
                        root = getBinaryOperatorNode(token.type, valueStack.pop(), root);
                    }
                    else
                    {
                        valueStack.push(getBinaryOperatorNode(token.type, valueStack.pop(), valueStack.pop()));
                    }
                    break;
            }
        }
        
        if(valueStack.size() != 0)
        {
            if(valueStack.size() > 1 || root != null)
            {
                throw new IllegalStateException("Dangling values (numbers or variables)");
            }     
            
            if(root == null)
            {
                root = valueStack.pop();
            }
        }
        
        SyntaxTree tree = new SyntaxTree(root);
        if(doSimplify)
        {
            tree.simplify();
        }
        return tree;
    }
    
    private Node getBinaryOperatorNode(TokenType type, Node child1, Node child2)
    {
        switch(type)
        {
            case PLUS:
                AdditionNode an = new AdditionNode();
                an.children.add(child1);
                an.children.add(child2);
                return an;
            case MINUS:
                SubtractionNode sn = new SubtractionNode();
                sn.negative = child1;
                sn.positive = child2;
                return sn;
            case MULTIPLY:
                MultiplicationNode mn = new MultiplicationNode();
                mn.children.add(child1);
                mn.children.add(child2);
                return mn;
            case DIVIDE:
                DivisionNode dn = new DivisionNode();
                dn.denominator = child1;
                dn.numerator = child2;
                return dn;
            case POWER:
                PowerNode pn = new PowerNode();
                pn.exponent = child1;
                pn.base = child2;
                return pn;
            default:
                throw new IllegalArgumentException(type + " is not a binary operator type.");       
        }
    }
}
