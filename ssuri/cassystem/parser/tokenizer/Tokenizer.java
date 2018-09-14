package ssuri.cassystem.parser.tokenizer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class Tokenizer
{
    public List<Token> tokenize(String stringToTokenize)
    {
        List<Token> tokens = new LinkedList<>();

        stringToTokenize = stringToTokenize.trim(); // Strings are immutable, so no need to make a copy

        while (!stringToTokenize.equals(""))
        {
            boolean matchFound = false;

            for (TokenType type : TokenType.values())
            {
                if (type.pattern == null) continue;

                Matcher m = type.pattern.matcher(stringToTokenize);
                if (m.find())
                {
                    String token = m.group().trim();
                    matchFound = true;
                    tokens.add(new Token(type, token));
                    stringToTokenize = m.replaceFirst("").trim();
                    break;
                }
            }

            if (!matchFound)
            {
                throw new IllegalStateException("Unable to tokenize input: " + stringToTokenize);
            }
        }

        return tokens;
    }
}