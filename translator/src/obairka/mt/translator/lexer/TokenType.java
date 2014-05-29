package obairka.mt.translator.lexer;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 18:52
 */
public enum TokenType {
        MUL, DIV,
        PLUS, MINUS,
        POWER,
        ID,
        INT, // keyword: int double void bool
        DOUBLE,
        VOID,
        /*BOOL,*/
        TRUE,
        FALSE,
        MAIN,
        NUM,
        LBKT,
        RBKT,
        FLBKT,
        FRBKT,
        SEMICOLON,
        ASSIGN,
        EOF,
        ERROR,
        SKIP,
        COMMA,
        RETURN,
        PRINT,
        LESS,
        GREATER,
        EQUAL,
        IF,
        ELSE,
        WHILE
}
