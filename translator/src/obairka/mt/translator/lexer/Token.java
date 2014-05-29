package obairka.mt.translator.lexer;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 18:51
 */
public class Token {

    private final TokenType tokenType;
    private String attr = null;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Token(TokenType tokenType, String attr) {
        this.tokenType = tokenType;
        this.attr = attr;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getAttr() {
        return attr;
    }

    @Override
    public String toString() {
        if (null != attr)
            return tokenType + " " + attr;
        return tokenType.toString();
    }

    public boolean  hasType(TokenType type) {
        return  tokenType == type;
    }

    @Override
    public boolean equals(Object other) {
        if (null == other) return false;
        if (other instanceof Token) {
            Token otherToken = (Token) other;
            return  (otherToken.tokenType == tokenType) &&
                    ((otherToken.attr == null) && (attr == null) || otherToken.attr.equals(attr));
        }
        return false;
    }

}
