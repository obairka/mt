package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import obairka.mt.translator.lexer.Lexer;
import obairka.mt.translator.lexer.SimpleLexer;
import obairka.mt.translator.lexer.Token;
import obairka.mt.translator.lexer.TokenType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 12:55
 */
@RunWith(JUnit4.class)
public class LexerTest {

    private Token scanInput(String stringToScan) {
        Lexer lexer = new SimpleLexer(new StringReader(stringToScan));
        Token t = lexer.peekToken();
        lexer.nextToken();
        return t;
    }

    @Test
    public void testTakeToken() {
        String string = "a = 3;";
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(TokenType.ID, "a"));
        tokens.add(new Token(TokenType.ASSIGN));
        tokens.add(new Token(TokenType.NUM, "3"));
        tokens.add(new Token(TokenType.SEMICOLON));
        tokens.add(new Token(TokenType.EOF)); // the end
        Lexer lexer = new SimpleLexer(new StringReader(string));

        for (Token expectedToken : tokens) {
            Token token = lexer.peekToken();
            lexer.nextToken();

            if (!token.equals(expectedToken))  {
                fail("expected and actual tokens don't match");
            }
        }

        if (!lexer.peekToken().hasType(TokenType.EOF)){
            // should be end of file but not
            fail("Expected the last token");
        }
    }

    @Test
    public void testKeyWords() {
        final Map<String, Token> KEY_WORDS = SimpleLexer.KEY_WORDS;
        final HashMap<String, Token> KEY_OPS = SimpleLexer.KEY_OPS;

        for (Map.Entry<String, Token> entry: KEY_WORDS.entrySet()) {
            Token actualToken = scanInput(entry.getKey());
            Token expectedToken = entry.getValue();

            assertEquals(expectedToken, actualToken);
        }

        for (Map.Entry<String, Token> entry: KEY_OPS.entrySet()) {
            Token actualToken = scanInput(entry.getKey());
            Token expectedToken = entry.getValue();
            assertEquals(expectedToken, actualToken);
        }
    }

    @Test
    public void testIntegerNumber() {
        int MAX_INT_VALUE = 10000000;// todo: WHAT TO DO WITH MAX VALUE?

        Random randomGenerator = new Random();
        String randomNumber =  Integer.toString(randomGenerator.nextInt(MAX_INT_VALUE));
        Token expected = new Token(TokenType.NUM, randomNumber);
        Token actual = scanInput(randomNumber);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoubleNumber() {
        Random randomGenerator = new Random();
        String randomNumber =  Double.toString(randomGenerator.nextDouble());
        Token expected = new Token(TokenType.NUM, randomNumber);
        Token actual = scanInput(randomNumber);
        assertEquals(expected, actual);
    }

    @Test
    public void testId() {
        ArrayList<Token> identifiers = new ArrayList<Token>();
        StringBuilder stringBuilder = new StringBuilder();

        identifiers.add(new Token(TokenType.ID,"a"));
        stringBuilder.append("a ");
        identifiers.add(new Token(TokenType.ID,"abc12345abc67890"));
        stringBuilder.append("abc12345abc67890 ");
        identifiers.add(new Token(TokenType.ID,"qwertyiopasdfghjklzxcvbnm"));
        stringBuilder.append("qwertyiopasdfghjklzxcvbnm ");
        Lexer lexer = new SimpleLexer(new StringReader(stringBuilder.toString()));
        for (Token t : identifiers) {
            Token token = lexer.peekToken();
            lexer.nextToken();
            assertEquals(t, token);
        }
    }

    @Test
    public void testLineCommentCut() {

        String string = "a = 3; // line comment \n a = 3; // comment";
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(TokenType.ID, "a"));
        tokens.add(new Token(TokenType.ASSIGN));
        tokens.add(new Token(TokenType.NUM, "3"));
        tokens.add(new Token(TokenType.SEMICOLON));

        tokens.add(new Token(TokenType.ID, "a"));
        tokens.add(new Token(TokenType.ASSIGN));
        tokens.add(new Token(TokenType.NUM, "3"));
        tokens.add(new Token(TokenType.SEMICOLON));
        tokens.add(new Token(TokenType.EOF)); // the end

        Lexer lexer = new SimpleLexer(new StringReader(string));

        for (Token expectedToken : tokens) {
            Token token = lexer.peekToken();
            lexer.nextToken();

            if (!token.equals(expectedToken))  {
                fail("expected and actual tokens don't match");
            }
        }

        if (!lexer.peekToken().hasType(TokenType.EOF)){
            // should be end of file but not
            fail("Expected the last token");
        }


    }

    @Test
    public void testMultiLineCommentCut() {

        String string = "a = 3; /* multiline \n comment */ \n a = 3; /* multiline * * * /* \n comment \n */";
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(TokenType.ID, "a"));
        tokens.add(new Token(TokenType.ASSIGN));
        tokens.add(new Token(TokenType.NUM, "3"));
        tokens.add(new Token(TokenType.SEMICOLON));

        tokens.add(new Token(TokenType.ID, "a"));
        tokens.add(new Token(TokenType.ASSIGN));
        tokens.add(new Token(TokenType.NUM, "3"));
        tokens.add(new Token(TokenType.SEMICOLON));
        tokens.add(new Token(TokenType.EOF)); // the end

        Lexer lexer = new SimpleLexer(new StringReader(string));

        for (Token expectedToken : tokens) {
            Token token = lexer.peekToken();
            lexer.nextToken();

            if (!token.equals(expectedToken))  {
                fail("expected and actual tokens don't match");
            }
        }

        if (!lexer.peekToken().hasType(TokenType.EOF)){
            // should be end of file but not
            fail("Expected the last token");
        }

    }
}
