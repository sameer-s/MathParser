package ssuri.cassystem.parser.tokenizer;

public class Token
{
    public TokenType type;
    public String string;

    public Token(TokenType type, String string)
    {
        this.type = type;
        this.string = string;
    }

    @Override
    public String toString()
    {
        return type.toString() + " <" + string + ">";
    }
    
    public boolean isOperator()
    {
        return type.precedence != -1;
    }
    
    public boolean isRightAssociative()
    {
        if(!isOperator()) throw new IllegalStateException("Cannot test associativity of non-operator token");
        
        return type.rightAssociative;
    }

    public int getPrecedence()
    {
        if(!isOperator()) throw new IllegalStateException("Cannot get precedence of non-operator token");
        
        return type.precedence;
    }

    public static boolean shouldMatchUnaryNegative(Token previousToken)
    {
        return previousToken.isOperator() || previousToken.type == TokenType.OPEN_PARENTHESIS;
    }
}