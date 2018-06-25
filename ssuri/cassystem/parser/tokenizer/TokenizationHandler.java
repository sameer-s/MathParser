package ssuri.cassystem.parser.tokenizer;

@FunctionalInterface interface TokenizationHandler
{
    boolean shouldMatchToken(String string, Token previousToken);
}