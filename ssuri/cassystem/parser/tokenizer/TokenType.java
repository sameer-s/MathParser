package ssuri.cassystem.parser.tokenizer;

import java.util.regex.Pattern;

public enum TokenType
{
    OPEN_PARENTHESIS("\\("),
    CLOSE_PARENTHESIS("\\)"),
    PLUS("\\+", 1, true),
    MINUS("\\-", 1, false, (string, previousToken) -> !shouldMatchUnaryNegative(previousToken)),
    MULTIPLY("\\*", 2, true),
    DIVIDE("\\/", 2, false),
    POWER("\\^", 3, true),
    // Not calling an operator constructor as it is unary and treated differently
    UNARY_MINUS("\\-", (string, previousToken) -> shouldMatchUnaryNegative(previousToken)),
    INTEGER("[0-9]+"),
    VARIABLE("[a-zA-Z][a-zA-Z0-9_]*");
    
    Pattern pattern;
    TokenizationHandler handler;
    int precedence;
    boolean rightAssociative;

    TokenType(String regex)
    {
        this(regex, -1, false);
    }
    
    TokenType(String regex, TokenizationHandler handler)
    {
        this(regex, -1, false, handler);
    }
    
    TokenType(String regex, int precedence, boolean rightAssociative)
    {
        this(regex, precedence, rightAssociative, (string, previousToken) -> true);
    }
  
    TokenType(String regex, int precedence, boolean rightAssociative, TokenizationHandler handler)
    {
        if (regex == null) this.pattern = null;
        else this.pattern = Pattern.compile("^(" + regex + ")");
        
        this.handler = handler;
        this.precedence = precedence;
        this.rightAssociative = rightAssociative;
    }
    
    static boolean shouldMatchUnaryNegative(Token previousToken)
    {
        return previousToken.isOperator() || previousToken.type == OPEN_PARENTHESIS;
    }
}