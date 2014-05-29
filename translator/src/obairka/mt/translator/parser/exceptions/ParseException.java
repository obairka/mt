package obairka.mt.translator.parser.exceptions;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 13:28
 */
public class ParseException extends Exception {
    public static final String EOF_EXPECTED 		= "EOF EXPECTED";
    public static final String WRONG_RETURN_TYPE 	= "WRONG RETURN TYPE";
    public static final String LBKT_EXPECTED 		= "( EXPECTED";
    public static final String RBKT_EXPECTED 		= ") EXPECTED";
    public static final String FLBKT_EXPECTED 		= "{ EXPECTED";
    public static final String FRBKT_EXPECTED 		= "} EXPECTED";
    public static final String RETURN_EXPECTED 	    = "RETURN EXPECTED";
    public static final String FUNCTION_EXPECTED 	= "FUNCTION EXPECTED";
    public static final String MAIN_SIGNATURE 		= "WRONG MAIN FUNCTION SIGNATURE";
    public static final String ARG_EXPECTED 		= "ARGUMENT EXPECTED";
    public static final String VOID_DECLARE 		= "VARIABLE DECLARATION OF VOID TYPE";
    public static final String PARAM_EXPECTED 		= "PARAMETER EXPECTED (AFTER COMMA)";
    public static final String PARAM_WRONG_DECL	    = "PARAMETER EXPECTED";
    public static final String SEMICOLON_EXPECTED 	= "SEMICOLON EXPECTED";
    public static final String COMMAND_EXPECTED 	= "COMMAND EXPECTED";
    public static final String PRINT_EXPECTED 		= "PRINT EXPECTED";
    public static final String WRONG_ARGUMENT 		= "WRONG ARGUMENT";
    public static final String IF_EXPECTED 		    = "IF COMMAND EXPECTED";
    public static final String BOOL_EXPR_EXPECTED 	= "BOOL EXPRESSION EXPECTED";
    public static final String WHILE_EXPECTED 		= "WHILE COMMAND EXPECTED";
    public static final String REDECLARING_VAR		= "VARIABLE REDECLARATION";
    public static final String WRONG_VAR_DECLARE 	= "WRONG VARIABLE DECLARATION";
    public static final String EXPR_EXPECTED 		= "EXPRESSION EXPECTED";
    public static final String BOOL_EXPR_WRONG_USE =  "BOOL EXPRESSION IS NOT EXPECTED HERE";
    public static final String CAST_ERROR          =  "CAST ERROR";
    public static final String ASSIGN_EXPECTED 	    = "= EXPECTED";
    public static final String WRONG_ARG_TYPE 		= "WRONG ARGUMENT TYPE";
    public static final String INCOMPLETE_BOOL_EXPR = "INCOMPLETE BOOL EXPRESSION";
    public static final String INCOMPLETE_EXPR 	    = "INCOMPLETE EXPRESSION";
    public static final String UNKNOWN_FUNCTION 	= "UNKNOWN FUNCTION";
    public static final String UNKNOWN_ID 	        = "UNKNOWN IDENTIFIER";

    private final String code;

    private static String messageBuilder(String code, String msg, int line, int offset) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error code: ").append(code).append(";\n");
        stringBuilder.append("Message: ").append(msg).append("\n");
        stringBuilder.append("Line: ").append(line).append("; Pos: ").append(offset).append(";\n");
        return stringBuilder.toString();
    }

    public ParseException(String code, String msg, int line, int offset) {
        super(messageBuilder(code,msg,line,offset)) ;
        this.code = code;
    }

    public boolean isThisCode(String code) {
        return this.code.equals(code);
    }

    public String getCode() {
        return code;
    }

}
