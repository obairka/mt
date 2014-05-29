package obairka.mt.translator.lexer;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import obairka.mt.translator.buffer.*;
import obairka.mt.translator.lexer.fsm.*;

public class SimpleLexer implements Lexer  {
    public static final Map<String, Token> KEY_WORDS = new HashMap<String, Token>();
    public static final HashMap<String, Token> KEY_OPS = new HashMap<String, Token>();
    static  {
        /*KEY_WORDS.put("bool",   new Token(TokenType.BOOL));*/
        KEY_WORDS.put("int",    new Token(TokenType.INT));
        KEY_WORDS.put("double", new Token(TokenType.DOUBLE));
        KEY_WORDS.put("void",   new Token(TokenType.VOID));
        KEY_WORDS.put("main",   new Token(TokenType.MAIN));
        KEY_WORDS.put("print",      new Token(TokenType.PRINT));
        KEY_WORDS.put("return",     new Token(TokenType.RETURN));
        KEY_WORDS.put("true",       new Token(TokenType.TRUE));
        KEY_WORDS.put("false",      new Token(TokenType.FALSE));
        KEY_WORDS.put("if",      new Token(TokenType.IF));
        KEY_WORDS.put("else",      new Token(TokenType.ELSE));
        KEY_WORDS.put("while",      new Token(TokenType.WHILE));


        KEY_OPS.put("+",   new Token(TokenType.PLUS));
        KEY_OPS.put("-",   new Token(TokenType.MINUS));
        KEY_OPS.put("*",   new Token(TokenType.MUL));
        //KEY_OPS.put("/",   new Token(TokenType.DIV));
        KEY_OPS.put("^",   new Token(TokenType.POWER));
        KEY_OPS.put("=",   new Token(TokenType.ASSIGN));
        KEY_OPS.put(",",   new Token(TokenType.COMMA));

        KEY_OPS.put("(",   new Token(TokenType.LBKT));
        KEY_OPS.put(")",   new Token(TokenType.RBKT));
        KEY_OPS.put("{",   new Token(TokenType.FLBKT));
        KEY_OPS.put("}",   new Token(TokenType.FRBKT));

        KEY_OPS.put(";",   new Token(TokenType.SEMICOLON));

        KEY_OPS.put("==",  new Token(TokenType.EQUAL));
        KEY_OPS.put("<",   new Token(TokenType.LESS));
        KEY_OPS.put(">",   new Token(TokenType.GREATER));
    }

    private static final Token SKIP = new Token(TokenType.SKIP);
    private static final Token DIV = new Token(TokenType.DIV);
    private static final int BUFFER_SIZE = 10;
	private final FSM<Token> fsm;
	
	private StringBuilder currentString = null;
	
	private Token currentLexeme = null;
	private final Buffer buffer;
	
	public SimpleLexer(Reader reader) {
		fsm = new FSM<Token>();
		buffer = new SimpleBuffer(reader, BUFFER_SIZE);
		automataInit();
	}

	private Token formLexeme(char c){
        return   KEY_OPS.get(Character.toString(c));

    }
	/* form from buffer lexeme */
	private Token getToken() {
		currentString = new StringBuilder();
		fsm.reset();
		Token currentLexeme = null;
		while (null == currentLexeme) {
			int ch = buffer.peekChar();
			int event = getEvent(ch);
			currentLexeme = fsm.addEvent(event);

			 if (SKIP == currentLexeme) {
				 fsm.reset();
				 currentLexeme = null;
				 currentString = new StringBuilder();
			 }				
		}		
		return currentLexeme;
	}
	
	@Override
	public void nextToken() {
		currentLexeme = getToken();
    }
	
	@Override
	public Token peekToken() {
		if (null == currentLexeme) {
			currentLexeme = getToken();
		}
		return currentLexeme;
	}
	/*form event code*/
	private int getEvent(int ch) {
		switch (ch) {
		case '.': return POINT;
		case '/': return SLASH;
		case '\n': return CRLF;
		case Buffer.EOF: return SEOF;
		case '*': return ASTERISKS;
		default:
			if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z') return LITER;
			if ('0' <= ch && ch <= '9') return DIGIT;
			return OTHER;
		}
	}	
	/* events: binary presentation */
	private static final int POINT  		= ~128;
	private static final int LITER  		= ~64;
	private static final int DIGIT  		= ~32;
	private static final int ASTERISKS 		= ~16;
	private static final int SLASH  		= ~8;
	private static final int CRLF	  		= ~4;
	private static final int SEOF	  		= ~2;
	private static final int OTHER	  		= ~1;
	/* states' names. for debug */
	private static final String START 				= "START";
	private static final String IDENT_LITER_DIGIT 	= "LITER(LITER)*";
	private static final String DIGIT_MID 			= "DIGIT(DIGIT)*";
	private static final String DIGIT_END 			= "DIGIT_END";
	private static final String DIGIT_AFTER_POINT 	= "DIGIT.DIGIT";	
	private static final String SLASH_START 		= "/  STATE start";	
	private static final String SLASH_STATE 		= "/  STATE";
	private static final String SLASH_AST			= "/* STATE";
	private static final String SLASH_AST_AST		= "/*SMTH* STATE";	
	private static final String DOUBLE_SLASH		= "// STATE";	
	private static final String EOF_STATE 			= "EOF STATE";
	
	private static final String ERR_STATE 			= "ERROR STATE";
	
	/* Init our Flying Spaghetti Monster */
    void automataInit() {
		
		State<Token> start = new State<Token>(START, new Executable<Token>(){
			@Override
			public Token execute() {
				int c = buffer.peekChar();
				if (Character.isWhitespace((char)c)) {
					buffer.nextChar();
					return SKIP;
				}
				Token lex = formLexeme((char)c);

				if (null != lex){
					buffer.nextChar();
				    if (lex.hasType(TokenType.ASSIGN)) {
                        if ('=' == buffer.peekChar()) {
                            buffer.nextChar();
                            return KEY_OPS.get("==");
                        }
                    }
				    return lex;
				}
				return null; // transition will be
			}
			
		});		
		State<Token> identLiterDigit = new State<Token>(IDENT_LITER_DIGIT, new Executable<Token>(){

			@Override
			public Token execute() {
				int ch = buffer.peekChar();
				if (Character.isAlphabetic((char)ch) || Character.isDigit((char)ch) ) {
					currentString .append((char)ch);					
					buffer.nextChar();
				} else {
                    if (KEY_WORDS.containsKey(currentString.toString())) {
                        return KEY_WORDS.get(currentString.toString());
                    }
					return new Token(TokenType.ID, currentString.toString());
				}
				return null;
			}
			
		});
	
		State<Token> digitMid = new State<Token>(DIGIT_MID, new Executable<Token>() {
			@Override
			public Token execute() {
				int ch = buffer.peekChar();				
				if ('.' == (char)ch || Character.isDigit((char)ch)) {
					currentString.append((char) ch);					
					buffer.nextChar();
					return null;
				}
                Number number = null;
                try {
                    number = new Integer(currentString.toString());
                }
                catch (NumberFormatException ex){
                    return new Token(TokenType.ERROR, "Number format exception " + number);
                }
				return new Token(TokenType.NUM, currentString.toString());
			}			
		});	
		
		State<Token> digitAfterPoint = new State<Token>(DIGIT_AFTER_POINT, new Executable<Token>() {
			@Override
			public Token execute() {
				if (Character.isDigit((char)buffer.peekChar()) )	{
					currentString.append((char) buffer.peekChar());			
					buffer.nextChar();
					return null;
				}
				return null;
			}
		});		
		
		State<Token> digitsEnd = new State<Token>(DIGIT_END, new Executable<Token>() {
			@Override
			public Token execute() {
				 if (Buffer.EOF == buffer.peekChar() || !Character.isDigit(buffer.peekChar()) ) {
                     // try conversion
                     Number number = null;
                     try {
                         number = new Double(currentString.toString());
                     }
                     catch (NumberFormatException ex){
                         return new Token(TokenType.ERROR, "Number format exception " + number);
                     }
                     return new Token(TokenType.NUM,currentString.toString());
				 } 
				 currentString.append((char) buffer.peekChar());
				 buffer.nextChar();
				 return null;
			}
			
		});
			
		State<Token> endState = new State<Token>(EOF_STATE, new Executable<Token>() {

			@Override
			public Token execute() {
				return EOF;
			}
			
		});

		State<Token> errorState = new State<Token>(ERR_STATE, new Executable<Token>() {
			@Override
			public Token execute() {
				return new Token(TokenType.ERROR, "["+currentString+"["+(char)buffer.peekChar()+"]]");
			}
			
		});		
		State<Token> slashStart = new State<Token>(SLASH_START,  new Executable<Token>() {
			@Override
			public Token execute() {
				buffer.nextChar();
				return null;
			}
			
		});
			
		State<Token> slashState = new State<Token>(SLASH_STATE, new Executable<Token>() {
			@Override
			public Token execute() {
				 int ch = buffer.peekChar();
				 if ('*' != ch && '/' != ch) {
					 return DIV;
				 } 
				 buffer.nextChar();
				 return null;
			}			
		});
		
		State<Token> slashAst = new State<Token>(SLASH_AST, new Executable<Token>() {

			@Override
			public Token execute() {
				buffer.nextChar();
				return null;
			}
			
		});
		State<Token> slashAstAst = new State<Token>(SLASH_AST_AST, new Executable<Token>() {
			@Override
			public Token execute() {
				 int ch = buffer.peekChar();				
				 if ( '/' == ch) {
					 buffer.nextChar();
					 return SKIP;
				 }
				 return null;
			}			
		});
		
		State<Token> doubleSlash = new State<Token>(DOUBLE_SLASH, new Executable<Token>() {
			@Override
			public Token execute() {
				int ch = buffer.nextChar();
				if ('\n' == ch || Buffer.EOF==ch) {					 
					return SKIP;
				}
				return null;
			}			
		});
		
		/* transitions between states*/
		start.addTransition(new Transition(DIGIT, START, DIGIT_MID));
		start.addTransition(new Transition(LITER, START, IDENT_LITER_DIGIT));
		start.addTransition(new Transition(~(LITER & DIGIT & SLASH & SEOF), START, ERR_STATE));		
		start.addTransition(new Transition(SEOF, START, EOF_STATE));	 
		start.addTransition(new Transition(SLASH, START, SLASH_START));	 		
		identLiterDigit.addTransition(new Transition(SEOF,  IDENT_LITER_DIGIT, EOF_STATE));
		digitMid.addTransition(new Transition(POINT, DIGIT_MID, DIGIT_AFTER_POINT));
		digitMid.addTransition(new Transition(DIGIT, DIGIT_MID, DIGIT_MID));
		digitMid.addTransition(new Transition(SEOF, DIGIT_MID, EOF_STATE));
		digitMid.addTransition(new Transition(~(DIGIT & POINT & SEOF), DIGIT_MID, ERR_STATE));
		digitAfterPoint.addTransition(new Transition(DIGIT, DIGIT_AFTER_POINT, DIGIT_END));
		digitAfterPoint.addTransition(new Transition(~DIGIT, DIGIT_AFTER_POINT, ERR_STATE));		
		digitsEnd.addTransition (new Transition(SEOF, DIGIT_END, EOF_STATE));
		slashStart.addTransition(new Transition(0, SLASH_START, SLASH_STATE));				
		slashState.addTransition(new Transition(ASTERISKS, SLASH_STATE, SLASH_AST));
		slashState.addTransition(new Transition(SLASH, SLASH_STATE, DOUBLE_SLASH));
		slashAst.addTransition  (new Transition(ASTERISKS, SLASH_AST, SLASH_AST_AST));
		slashAst.addTransition  (new Transition(SEOF, SLASH_AST, ERR_STATE));
		slashAstAst.addTransition(new Transition(SEOF, SLASH_AST_AST, ERR_STATE));
		slashAstAst.addTransition(new Transition(~(SLASH & SEOF), SLASH_AST_AST, SLASH_AST));				
		/*add states to fsm*/
		fsm.addState(start);
		fsm.addState(slashStart);
		fsm.addState(identLiterDigit);		
		fsm.addState(digitMid);
		fsm.addState(digitAfterPoint);
		fsm.addState(digitsEnd);	
		fsm.addState(endState);
		fsm.addState(errorState);
		fsm.addState(slashState);
		fsm.addState(slashAst);
		fsm.addState(slashAstAst);
		fsm.addState(doubleSlash);		
	}

	
	@Override
    public int currLine() {
        return buffer.currentLineNum();
    }

    @Override
    public int currPos() {
        return buffer.currentPosInLine();
    }
 
}
