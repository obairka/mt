package tests;

import obairka.mt.translator.lexer.Lexer;
import obairka.mt.translator.lexer.SimpleLexer;
import obairka.mt.translator.lexer.Token;
import obairka.mt.translator.lexer.TokenType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;
import java.util.LinkedList;

import static junit.framework.Assert.fail;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 12:55
 */
@RunWith(JUnit4.class)
public class LexerExceptionsTest {
    private LinkedList<Token> scanInput(String stringToScan) {
        Lexer lexer = new SimpleLexer(new StringReader(stringToScan));
        LinkedList<Token> arrayList = new LinkedList<Token>();

        Token currToken;
        do {
            currToken = lexer.peekToken();
            lexer.nextToken();
            arrayList.add(currToken);
        }
        while (!currToken.hasType(TokenType.EOF) && !currToken.hasType(TokenType.ERROR))  ;

        return arrayList;
    }
    @Test
    public void testBadDoubleNum() {
        LinkedList<Token> tokens = scanInput("234. ");

        if (!tokens.getLast().hasType(TokenType.ERROR)) {
            fail(tokens.getLast().toString());
        }
    }
    @Test
    public void testBadDoubleNum2() {
        LinkedList<Token> tokens = scanInput(".234 ");

        if (!tokens.getLast().hasType(TokenType.ERROR)) {
            fail(tokens.getLast().toString());
        }
    }

    @Test
    public void testBadIntegerNum() {
        LinkedList<Token> tokens = scanInput("1111111111111111111111111111111111111111111111111111111111111");

        if (!tokens.getLast().hasType(TokenType.ERROR)) {
            fail(tokens.getLast().toString());
        }
    }
    // TODO: ambigious === ?
}
