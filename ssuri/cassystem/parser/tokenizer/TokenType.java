package ssuri.cassystem.parser.tokenizer;

import java.util.regex.Pattern;

public enum TokenType
{
    OPEN_PARENTHESIS("\\("),
    CLOSE_PARENTHESIS("\\)"),
    PLUS("\\+", 1, true),
    MINUS("\\-", 1, false),
    MULTIPLY("\\*", 2, true),
    DIVIDE("\\/", 2, false),
    POWER("\\^", 3, true),
    UNARY_MINUS("\\-"), // MINUS will match first
    INTEGER("[0-9]+"),
    VARIABLE("[a-zA-Z][a-zA-Z0-9_]*");
    
    Pattern pattern;
    int precedence;
    boolean rightAssociative;

    TokenType(String regex)
    {
        this(regex, -1, false);
    }

    TokenType(String regex, int precedence, boolean rightAssociative)
    {
        if (regex == null) { this.pattern = null; }
        else { this.pattern = Pattern.compile("^(" + regex + ")"); }
        this.precedence = precedence;
        this.rightAssociative = rightAssociative;
    }
}