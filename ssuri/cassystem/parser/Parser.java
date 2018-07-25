package ssuri.cassystem.parser;

import static ssuri.cassystem.parser.tokenizer.TokenType.OPEN_PARENTHESIS;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import ssuri.cassystem.math.Fraction;
import ssuri.cassystem.parser.tokenizer.Token;
import ssuri.cassystem.parser.tokenizer.TokenType;
import ssuri.cassystem.tree.SyntaxTree;
import ssuri.cassystem.tree.node.Node;
import ssuri.cassystem.tree.node.operator.AdditionNode;
import ssuri.cassystem.tree.node.operator.DivisionNode;
import ssuri.cassystem.tree.node.operator.ExponentiationNode;
import ssuri.cassystem.tree.node.operator.MultiplicationNode;
import ssuri.cassystem.tree.node.operator.OperatorNode;
import ssuri.cassystem.tree.node.operator.SubtractionNode;
import ssuri.cassystem.tree.node.value.ConstantNode;
import ssuri.cassystem.tree.node.value.VariableNode;
import ssuri.cassystem.util.Log;

public class Parser
{   
    public SyntaxTree parse(List<Token> tokensToParse)
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
        
        Log.d("Output Queue: %s", outputQueue);
        
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
                    throw new IllegalStateException("Parentheses should be removed during parse step 1");
                case INTEGER:
                    valueStack.push(new ConstantNode(new Fraction(Integer.parseInt(token.string), 1)));
                    break;
                case VARIABLE:
                    valueStack.push(new VariableNode(token.string));
                    break;
                case UNARY_MINUS:
                    Node child = root == null ? valueStack.pop() : root;
                    root = new MultiplicationNode(ConstantNode.NEGATIVE_ONE, child);
                    break;
                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE:
                case POWER:
                    Log.d("Current value stack state: %s", valueStack);
                    valueStack.push(getOperator(token.type, valueStack.pop(), valueStack.pop()));
                    break;
            }
        }
        
        root = valueStack.pop();
        
        if(valueStack.size() != 0)
        {
            if(valueStack.size() > 1 || root != null)
            {
                throw new IllegalStateException("Dangling values (numbers or variables)");
            }     

            root = valueStack.pop();
        }
 
        return new SyntaxTree(root);
    }
    
    private OperatorNode getOperator(TokenType type, Node child1, Node child2)
    {
        switch(type)
        {
            case PLUS:  
                return new AdditionNode(child2, child1);
            case MINUS:
                return new SubtractionNode(child2, child1);
            case MULTIPLY:
                return new MultiplicationNode(child2, child1);
            case DIVIDE:
                return new DivisionNode(child2, child1);
            case POWER:
                return new ExponentiationNode(child2, child1);
            default:
                throw new IllegalArgumentException(type + " is not a binary operator type.");       
        }
    }
}
