package obairka.mt.translator.lexer;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 18:47
 */
public interface Lexer {
     public static final Token EOF = new Token(TokenType.EOF);
     public Token peekToken();
     public void nextToken();
     public int currLine();
     public int currPos();
}
