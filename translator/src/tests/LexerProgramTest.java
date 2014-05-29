package tests;

import obairka.mt.translator.lexer.Lexer;
import obairka.mt.translator.lexer.SimpleLexer;
import obairka.mt.translator.lexer.Token;
import obairka.mt.translator.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 13:01
 */
@RunWith(JUnit4.class)
public class LexerProgramTest {
    private static final String testProgram = "res/testProgram";
    private Lexer lexer;
    private final ArrayList<Token> lexerExpectedResult = new ArrayList<Token>();

    @Before
    public void setUp() {
        try {
            Reader reader = new FileReader(testProgram);
            lexer = new SimpleLexer(reader);
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        lexerExpectedResult.add(new Token(TokenType.VOID));
        lexerExpectedResult.add(new Token(TokenType.MAIN));
        lexerExpectedResult.add(new Token(TokenType.LBKT));
        lexerExpectedResult.add(new Token(TokenType.RBKT));
        lexerExpectedResult.add(new Token(TokenType.FLBKT));

        lexerExpectedResult.add(new Token(TokenType.INT));
        lexerExpectedResult.add(new Token(TokenType.ID, "a"));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));
        lexerExpectedResult.add(new Token(TokenType.DOUBLE));
        lexerExpectedResult.add(new Token(TokenType.ID, "b"));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.ID, "a"));
        lexerExpectedResult.add(new Token(TokenType.ASSIGN));
        lexerExpectedResult.add(new Token(TokenType.LBKT));
        lexerExpectedResult.add(new Token(TokenType.NUM, "3"));
        lexerExpectedResult.add(new Token(TokenType.PLUS));
        lexerExpectedResult.add(new Token(TokenType.NUM, "4"));
        lexerExpectedResult.add(new Token(TokenType.RBKT));
        lexerExpectedResult.add(new Token(TokenType.MUL));
        lexerExpectedResult.add(new Token(TokenType.NUM, "6"));
        lexerExpectedResult.add(new Token(TokenType.DIV));
        lexerExpectedResult.add(new Token(TokenType.NUM, "2"));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.ID, "b"));
        lexerExpectedResult.add(new Token(TokenType.ASSIGN));
        lexerExpectedResult.add(new Token(TokenType.NUM, "2.5"));
        lexerExpectedResult.add(new Token(TokenType.MUL));
        lexerExpectedResult.add(new Token(TokenType.NUM, "2.0"));
        lexerExpectedResult.add(new Token(TokenType.POWER));
        lexerExpectedResult.add(new Token(TokenType.NUM, "2.0"));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.ID, "b"));
        lexerExpectedResult.add(new Token(TokenType.ASSIGN));
        lexerExpectedResult.add(new Token(TokenType.ID, "a"));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.IF));
        lexerExpectedResult.add(new Token(TokenType.LBKT));
        lexerExpectedResult.add(new Token(TokenType.ID, "a"));
        lexerExpectedResult.add(new Token(TokenType.LESS));
        lexerExpectedResult.add(new Token(TokenType.ID, "b"));
        lexerExpectedResult.add(new Token(TokenType.RBKT));
        lexerExpectedResult.add(new Token(TokenType.FLBKT));

        lexerExpectedResult.add(new Token(TokenType.PRINT));
        lexerExpectedResult.add(new Token(TokenType.LBKT));
        lexerExpectedResult.add(new Token(TokenType.ID, "a"));
        lexerExpectedResult.add(new Token(TokenType.RBKT));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.FRBKT));

        lexerExpectedResult.add(new Token(TokenType.ELSE));
        lexerExpectedResult.add(new Token(TokenType.FLBKT));

        lexerExpectedResult.add(new Token(TokenType.DOUBLE));
        lexerExpectedResult.add(new Token(TokenType.ID, "c"));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.ID, "c"));
        lexerExpectedResult.add(new Token(TokenType.ASSIGN));
        lexerExpectedResult.add(new Token(TokenType.ID, "a"));
        lexerExpectedResult.add(new Token(TokenType.POWER));
        lexerExpectedResult.add(new Token(TokenType.NUM, "0.5"));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.PRINT));
        lexerExpectedResult.add(new Token(TokenType.LBKT));
        lexerExpectedResult.add(new Token(TokenType.ID, "c"));
        lexerExpectedResult.add(new Token(TokenType.RBKT));
        lexerExpectedResult.add(new Token(TokenType.SEMICOLON));

        lexerExpectedResult.add(new Token(TokenType.FRBKT));
        lexerExpectedResult.add(new Token(TokenType.FRBKT));
    }

    @Test

    public void test() {
        for (Token expected : lexerExpectedResult) {
            Token actual = lexer.peekToken();
            lexer.nextToken();
            assertEquals(expected, actual);
        }
        if (!lexer.peekToken().hasType(TokenType.EOF)) {
            fail();
        }
    }
}
