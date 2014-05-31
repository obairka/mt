package obairka.mt.translator.parser;

import com.sun.org.apache.bcel.internal.generic.CASTORE;
import obairka.mt.translator.lexer.*;
import obairka.mt.translator.parser.exceptions.*;
import obairka.mt.translator.parser.nodes.*;
import obairka.mt.translator.parser.nodes.commands.*;
import obairka.mt.translator.parser.nodes.expression.*;

import java.io.Reader;
import java.util.ArrayList;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 12:12
 */

public class RecursiveParser implements Parser{

    private final Lexer lexer;
    private Program currentProgram;
    private Function currentFunction = null;

    public RecursiveParser(Reader reader) {
        lexer = new SimpleLexer(reader);
    }

    private static boolean isConversionPossible(Type first, Type second) {
        if ( (first == second) || (first == Type.VOID) || (second == Type.VOID))
            return false;
        return true;// DOUBLE -> INT, INT->DOUBLE, BOOL->INT, BOOL->DOUBLE
    }

    @Override
    public Program parseProgram() throws ParseException {
        Program program = new Program();
        currentProgram = program;
        Function function;
        while (null != (function = parseFunction())){
            program.addFunction(function);
        }
        // check if null but it is not end
        if (!lexer.peekToken().hasType(TokenType.EOF)) {
             throw new ParseException(ParseException.EOF_EXPECTED, "parseProgram:", lexer.currLine(), lexer.currPos());
        }
        currentProgram = null;
        return program;
    }


    Function parseFunction() throws ParseException {
        Function function = new Function();
        currentFunction = function;
        // parse return type
        Type returnType = parseType();

        if (null == returnType) {
            return null;
        }
        function.setReturnType(returnType);
        Token currToken = lexer.peekToken(); // ID or MAIN
        if (currToken.hasType(TokenType.ID) || currToken.hasType(TokenType.MAIN)) {
            lexer.nextToken();
            function.setFunctionName(currToken.getAttr());
            if (currToken.hasType(TokenType.MAIN)) {
                // check return type
                if(function.getReturnType()!=Type.VOID){
                    throw new ParseException(ParseException.WRONG_RETURN_TYPE, "Main function", lexer.currLine(), lexer.currPos());
                }
                function.setFunctionName("main");
                function.getContext().incrementIdCounter();
                currentProgram.setMainFunction(function);
            }

            currToken = lexer.peekToken();

            if (!currToken.hasType(TokenType.LBKT)) {
                throw new ParseException(ParseException.LBKT_EXPECTED,function.getFunctionName(),lexer.currLine(), lexer.currPos());
            }

            lexer.nextToken(); // it was (
            parseParamList();  //  currentFunction will be used

            currToken = lexer.peekToken();
            if (!currToken.hasType(TokenType.RBKT)) {
                throw new ParseException(ParseException.RBKT_EXPECTED, function.getFunctionName(), lexer.currLine(), lexer.currPos());
            }
            lexer.nextToken(); // it was )
            currToken = lexer.peekToken();
            if (!currToken.hasType(TokenType.FLBKT)){
                throw new ParseException(ParseException.FLBKT_EXPECTED, function.getFunctionName(), lexer.currLine(), lexer.currPos());
            }
            lexer.nextToken(); // it was {

            Body body = parseBody(function.getContext());
            function.setBody(body);
            currToken = lexer.peekToken();

            if (!currToken.hasType(TokenType.FRBKT)) {
                throw new ParseException(ParseException.FRBKT_EXPECTED, function.getFunctionName(),lexer.currLine(), lexer.currPos());
            }
            lexer.nextToken();
            if (!function.isReturnStatementWas()) {
                throw new ParseException(ParseException.RETURN_EXPECTED, function.getFunctionName(), lexer.currLine(), lexer.currPos());
            }
            return function;
        }

        lexer.nextToken();
        throw new ParseException(ParseException.FUNCTION_EXPECTED, "parseFunction:"+lexer.peekToken(), lexer.currLine(), lexer.currPos()) ;

    }


    void parseParamList() throws ParseException {
        Variable par = parseParams();
        if (null == par) {// there is no params
            return;
        }
        if (currentFunction.isMain()) {
            throw new ParseException(ParseException.MAIN_SIGNATURE, currentFunction.getFunctionName(), lexer.currLine(), lexer.currPos());
        }
        currentFunction.addArgument(par);
        while (lexer.peekToken().hasType(TokenType.COMMA)) {
            lexer.nextToken();
            par = parseParams();
            if (null == par) {
                throw new ParseException(ParseException.PARAM_WRONG_DECL, currentFunction.getFunctionName(), lexer.currLine(), lexer.currPos());
            }
            currentFunction.addArgument(par);
        }
    }


    Variable parseParams() throws ParseException {
        Type type = parseType();  // param type
        if (null == type) return null;   // there is no param
        if (type == Type.VOID) {
            throw new ParseException(ParseException.VOID_DECLARE, "parseParams",lexer.currLine(), lexer.currPos());
        }
        Token name = lexer.peekToken();
        if (!name.hasType(TokenType.ID)) {
            throw new ParseException(ParseException.PARAM_EXPECTED, "parseParams", lexer.currLine(),  lexer.currPos());
        }
        lexer.nextToken();
        return new Variable(type, name.getAttr());
    }


    Body parseBody(Context context) throws ParseException {
        Body body = new Body();
        do {
            Command command = null;
            Token currToken = lexer.peekToken();
            switch (currToken.getTokenType()) {
                case IF:
                    command = parseIfCondition(context);
                    break;
                case WHILE:
                    command = parseWhile(context);
                    break;
                case PRINT:
                    command = parsePrint(context);
                    if (!lexer.peekToken().hasType(TokenType.SEMICOLON))  {
                        throw new ParseException(ParseException.SEMICOLON_EXPECTED, "print", lexer.currLine(), lexer.currPos());
                    }
                    lexer.nextToken();
                    break;
                case RETURN:
                case ID :
                    // maybe declare command or call function
                /*case BOOL:*/
                case INT :
                case DOUBLE:
                    // assign command
                    command = parseCommand(context);
                    if (!lexer.peekToken().hasType(TokenType.SEMICOLON))  {
                        throw new ParseException(ParseException.SEMICOLON_EXPECTED, "command", lexer.currLine(), lexer.currPos());
                    }
                    lexer.nextToken();
                    break;
                default:

//                    throw new ParseException("Parse Body error ["+lexer.peekToken()+"]"  , lexer.currLine(), lexer.currPos());
            }
            if (null == command) {
                if (0 == body.getSize()) { // body без команд
                    break;
                }
                throw new ParseException(ParseException.COMMAND_EXPECTED, "parseBody", lexer.currLine(), lexer.currPos());
            }
            body.addCommand(command);

        } while(!lexer.peekToken().hasType(TokenType.FRBKT) && !lexer.peekToken().hasType(TokenType.ERROR)
                && !lexer.peekToken().hasType(TokenType.EOF));
        return body;
    }


    PrintCommand parsePrint(Context context) throws ParseException {
        Token currToken = lexer.peekToken();
        if (TokenType.PRINT != currToken.getTokenType()) {
            // TODO: вообще говоря такой ошибки не должно быть
            throw  new ParseException(ParseException.PRINT_EXPECTED, "parsePrint", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        if (lexer.peekToken().getTokenType() != TokenType.LBKT) {
            throw new ParseException(ParseException.LBKT_EXPECTED, "print", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();

        // Делаем parse(Expression)
        Expression expression = parseExpression(context);
        if (null == expression || expression instanceof CompareOp) {
            throw new ParseException(ParseException.WRONG_ARGUMENT, "print", lexer.currLine(), lexer.currPos());
        }
        if (lexer.peekToken().getTokenType() != TokenType.RBKT) {
            throw new ParseException(ParseException.RBKT_EXPECTED, "print", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        return new PrintCommand(expression);
    }


    IfCondition parseIfCondition(Context context) throws ParseException {
        //parse if
        Token currToken = lexer.peekToken();
        if (TokenType.IF != currToken.getTokenType()) {
            throw  new ParseException(ParseException.IF_EXPECTED, "parseIfCondition", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        if (lexer.peekToken().getTokenType() != TokenType.LBKT) {
            throw new ParseException(ParseException.LBKT_EXPECTED, "parseIfCondition", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        // Boolean-Expression
        Expression expr = parseExpression(context);
        if (expr == null || !(expr instanceof CompareOp)) {
            throw new ParseException(ParseException.BOOL_EXPR_EXPECTED, "parseIfCondition", lexer.currLine(), lexer.currPos());
        }

        CompareOp compareOp = (CompareOp) expr;

        if (lexer.peekToken().getTokenType() != TokenType.RBKT) {
            throw new ParseException(ParseException.RBKT_EXPECTED,  "parseIfCondition", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        if (lexer.peekToken().getTokenType() != TokenType.FLBKT) {
            throw new ParseException(ParseException.FLBKT_EXPECTED, "parseIfCondition", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();

        IfCondition condition = new IfCondition(context, compareOp);


        Body ifBody = parseBody(condition.getIfContext());

        condition.setIfBody(ifBody);

        if (lexer.peekToken().getTokenType() != TokenType.FRBKT) {
            throw new ParseException(ParseException.FRBKT_EXPECTED, "parseIfCondition", lexer.currLine(), lexer.currPos());
        }

        lexer.nextToken();

        if (lexer.peekToken().getTokenType() != TokenType.ELSE) {
            return condition;
        }
        condition.switchElseBlock();
        lexer.nextToken();
        if (lexer.peekToken().getTokenType() != TokenType.FLBKT) {
            throw new ParseException(ParseException.FLBKT_EXPECTED, "parseElse", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();

        Body elseBody = parseBody(condition.getElseContext());

        condition.setElseBody(elseBody);

        if (lexer.peekToken().getTokenType() != TokenType.FRBKT) {
            throw new ParseException(ParseException.FRBKT_EXPECTED,"parseElse", lexer.currLine(), lexer.currPos());
        }

        lexer.nextToken();

        return condition;
    }


    WhileCommand parseWhile(Context context) throws ParseException {
        //parse if
        Token currToken = lexer.peekToken();
        if (TokenType.WHILE != currToken.getTokenType()) {
            throw  new ParseException(ParseException.WHILE_EXPECTED, "parseWhile", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        if (lexer.peekToken().getTokenType() != TokenType.LBKT) {
            throw new ParseException(ParseException.LBKT_EXPECTED, "parseWhile", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        // Boolean-Expression
        Expression expr = parseExpression(context);
        if (expr == null || ! (expr  instanceof CompareOp)) {
            throw new ParseException(ParseException.BOOL_EXPR_EXPECTED, "parseWhile", lexer.currLine(), lexer.currPos());
        }
        CompareOp compareOp = (CompareOp) expr;

        if (lexer.peekToken().getTokenType() != TokenType.RBKT) {
            throw new ParseException(ParseException.RBKT_EXPECTED, "parseWhile" , lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();
        if (lexer.peekToken().getTokenType() != TokenType.FLBKT) {
            throw new ParseException(ParseException.FLBKT_EXPECTED,  "parseWhile", lexer.currLine(), lexer.currPos());
        }
        lexer.nextToken();

        WhileCommand whileCommand = new WhileCommand(context, compareOp);

        Body whileBody = parseBody(whileCommand.getContext());

        if (lexer.peekToken().getTokenType() != TokenType.FRBKT) {
            throw new ParseException(ParseException.FRBKT_EXPECTED, "parseWhile", lexer.currLine(), lexer.currPos());
        }

        lexer.nextToken();

        whileCommand.setBody(whileBody);
        return whileCommand;
    }


    Command parseCommand(Context context) throws ParseException {
        Token currToken = lexer.peekToken();
        TokenType tokenType = currToken.getTokenType();
        switch (tokenType) {
            case INT: case DOUBLE:
                // parse type
                DeclareCommand declareCommand;
                Type type = parseType();

                currToken = lexer.peekToken();
                if (currToken.hasType(TokenType.ID)) {
                    lexer.nextToken();
                    if (null != context.get(currToken.getAttr())) {
                        // TODO:
                        // Возможно это повторное объявление в блоке, тогда все нормально
                        // а если не в блоке, то все плохо
                        //throw new ParseException(ParseException.REDECLARING_VAR, currToken.getAttr(), lexer.currLine(), lexer.currPos());
                    }
                    Variable v = new Variable(type, currToken.getAttr());
                    declareCommand = new DeclareCommand(v);
                    context.addVariable(v);
                    currentFunction.incrementLocal(type);
                }
                else {
                    throw new ParseException(ParseException.WRONG_VAR_DECLARE, currToken.toString(), lexer.currLine(), lexer.currPos());
                }
                return declareCommand;
            case ID :
                AssignCommand assignCommand;
                // Look Up for var
                Variable variable = context.get(currToken.getAttr());
                if (null == variable) {
                    // may be it function call
                    Expression expr = parseExpression(context);

                    if ( (expr != null) && (expr instanceof CallFunctionExpr)) {
                        return (CallFunctionExpr) expr;
                    }
                    else {
                        throw new ParseException(ParseException.UNKNOWN_ID, currToken.getAttr(), lexer.currLine(), lexer.currPos());
                    }
                }
                lexer.nextToken();
                currToken = lexer.peekToken();

                if (currToken.hasType(TokenType.ASSIGN)) {
                    lexer.nextToken();
                    Expression expression = parseExpression(context);
                    if (null == expression){
                        throw new ParseException(ParseException.EXPR_EXPECTED, "assign command", lexer.currLine(), lexer.currPos());
                    }
                    if (expression instanceof CompareOp) {
                        // нельзя присваивать
                        throw new ParseException(ParseException.BOOL_EXPR_WRONG_USE, "assign command", lexer.currLine(), lexer.currPos());
                    }

                    if (  expression.getExprType() != variable.getType() ) {
                        if (!RecursiveParser.isConversionPossible(variable.getType(), expression.getExprType()))
                        {
                            throw new ParseException(ParseException.CAST_ERROR, variable.getType() + " vs " + expression.getExprType() , lexer.currLine(),
                                    lexer.currPos());
                        }
                        // add type conversion
                        expression = new ConvertType(expression, variable.getType());
                    }
                    assignCommand = new AssignCommand(variable, expression);

                }
                else {
                    throw new ParseException(ParseException.ASSIGN_EXPECTED, "assign command", lexer.currLine(), lexer.currPos());
                }
                // TODO : [a;]  просто выражение, это команда???
                return assignCommand;
            case RETURN:
                lexer.nextToken();
                Expression expression = parseExpression(context);

                ReturnCommand returnCommand = new ReturnCommand();

                if (null != expression) {

                    if (expression instanceof CompareOp) {
                        // нельзя присваивать
                        throw new ParseException(ParseException.BOOL_EXPR_WRONG_USE, "return", lexer.currLine(), lexer.currPos());
                    }

                    if (currentFunction.getReturnType() != expression.getExprType()) {
                        if (!RecursiveParser.isConversionPossible(currentFunction.getReturnType(), expression.getExprType())) {
                            throw  new ParseException(ParseException.WRONG_RETURN_TYPE, currentFunction.getFunctionName(), lexer.currLine(), lexer.currPos());
                        }
                        expression =  new ConvertType(expression, currentFunction.getReturnType());
                    }
                    returnCommand.setExpr(expression);
                }
                else {
                    if (currentFunction.getReturnType() != Type.VOID) {
                        throw  new ParseException(ParseException.WRONG_RETURN_TYPE, currentFunction.getFunctionName(), lexer.currLine(), lexer.currPos());
                    }
                }
                currentFunction.setReturnStatementWas(true);
                return returnCommand;
            default:
                throw new ParseException(ParseException.COMMAND_EXPECTED, "parseCommand", lexer.currLine(), lexer.currPos()) ;
        }
    }


    ArrayList<Expression> parseArgumentList(Function function, Context context) throws ParseException {
        Expression  expression = parseExpression(context);


        ArrayList<Expression> arrayList = new ArrayList<Expression>();
        if (null == expression) {
            if (function.getArgumentsCount() != 0) {
                throw new ParseException(ParseException.WRONG_ARG_TYPE, "expected: 0", lexer.currLine(), lexer.currPos());
            }
            return arrayList;
        }
        if (expression instanceof CompareOp) {
            throw new ParseException(ParseException.BOOL_EXPR_WRONG_USE, function.getFunctionName(), lexer.currLine(), lexer.currPos());
        }
        // find and look if types match
        int idx = 0;

        if (expression.getExprType() != function.getArgumentType(idx)  ) {
            // error
            if (!RecursiveParser.isConversionPossible( function.getArgumentType(idx), expression.getExprType()) )
                throw new ParseException(ParseException.WRONG_ARG_TYPE, function.getFunctionName(), lexer.currLine(), lexer.currPos()) ;

            // convert argument
            expression =   new ConvertType(expression, function.getArgumentType(idx));
        }
        arrayList.add(expression);
        Token currToken = lexer.peekToken();

        while (currToken.getTokenType()== TokenType.COMMA) {
             ++idx;
            lexer.nextToken();
            expression = parseExpression(context);

            if (null == expression) {
                throw new ParseException(ParseException.ARG_EXPECTED, function.getFunctionName(), lexer.currLine(), lexer.currPos());
            }
            if (expression instanceof CompareOp) {
                throw new ParseException(ParseException.BOOL_EXPR_WRONG_USE, function.getFunctionName(), lexer.currLine(), lexer.currPos());
            }

            if (idx >= function.getArgumentsCount()) {
                throw new  ParseException(ParseException.WRONG_ARGUMENT, function.getFunctionName(), lexer.currLine(), lexer.currPos()) ;
            }
            if (expression.getExprType() != function.getArgumentType(idx) ) {
                // error
                throw new ParseException(ParseException.WRONG_ARGUMENT, function.getFunctionName(), lexer.currLine(), lexer.currPos()) ;
            }
            arrayList.add(expression);
            currToken = lexer.peekToken();
        }
        return  arrayList;
    }


    Type parseType() {
        Token currToken = lexer.peekToken();
        switch (currToken.getTokenType()) {
            case INT :
                lexer.nextToken();
                return Type.INT;
            case DOUBLE:
                lexer.nextToken();
                return Type.DOUBLE;
            case VOID:
                lexer.nextToken();
                return Type.VOID;
            /*case BOOL:
                lexer.nextToken();
                return Type.BOOL;
            */
            default:
                return null;
        }
    }


    Expression parseExpression(Context context) throws ParseException {
        Expression expr = parseTerm(context);

        if (null == expr) {
            return null;
        }
        Token currToken = lexer.peekToken();

        CompareOp compareOp = null;
        switch (currToken.getTokenType()) {
            case LESS:
                compareOp = new CompareOp(CompareType.LESS);
                lexer.nextToken();
                break;
            case GREATER:
                compareOp = new CompareOp(CompareType.GREATER);
                lexer.nextToken();
                break;
            case EQUAL:
                compareOp = new CompareOp(CompareType.EQ);
                lexer.nextToken();
        }

        if (null != compareOp){
            try {
                compareOp.setLeft(expr);
                Expression right = parseExpression(context);
                if (null == right) {
                    throw new ParseException(ParseException.INCOMPLETE_BOOL_EXPR, compareOp.toString(), lexer.currLine(), lexer.currPos()) ;
                }
                compareOp.setRight(right);
                return compareOp;
            } catch (CastException e) {
                throw new ParseException(ParseException.CAST_ERROR, e.getMessage(), lexer.currLine(), lexer.currPos()) ;
            }

        }

        while (currToken.hasType(TokenType.PLUS) || currToken.hasType(TokenType.MINUS)) {
            lexer.nextToken();
            Op opNode;
            if (currToken.hasType(TokenType.PLUS))  {
                opNode = new Op(OpType.SUM);
            }
            else {
                opNode = new Op(OpType.SUB);
            }
            Expression right = parseTerm(context);
            if (null != right) {
                try {
                    opNode.setLeftNode(expr);
                    opNode.setRightNode(right);
                } catch (CastException err) {
                    throw new ParseException(ParseException.CAST_ERROR,err.getMessage(), lexer.currLine(), lexer.currPos()) ;
                }
                expr = opNode; // новый корень
            }
            else{
                throw new ParseException(ParseException.INCOMPLETE_EXPR, "+-",lexer.currLine(), lexer.currPos());
            }
            currToken = lexer.peekToken();
        }
        return expr;
    }


    Expression parseTerm(Context context) throws ParseException {
        Expression expr = parseFactor(context);
        if (null == expr) return null;
        Token currToken = lexer.peekToken();
        while (currToken.hasType(TokenType.MUL) || currToken.hasType(TokenType.DIV)) {
            lexer.nextToken();
            Op opNode;
            if (currToken.hasType(TokenType.MUL))  {
                opNode = new Op(OpType.MUL);
            }
            else {
                opNode = new Op(OpType.DIV);
            }
            Expression right = parseTerm(context);
            if (null != right) {
                try {
                    opNode.setLeftNode(expr);
                    opNode.setRightNode(right);
                } catch (CastException err) {
                    throw new ParseException(ParseException.CAST_ERROR, err.getMessage(), lexer.currLine(), lexer.currPos()) ;
                }
                expr = opNode; // новый корень
            }
            else{
                throw new ParseException(ParseException.INCOMPLETE_EXPR, "*/", lexer.currLine(), lexer.currPos());
            }
            currToken = lexer.peekToken();
        }
        return expr;
    }


    Expression parseFactor(Context context) throws ParseException {
        Expression expr = parsePower(context);
        if (null == expr) return null;
        Token currToken = lexer.peekToken();
        while (currToken.hasType(TokenType.POWER) ) {
            lexer.nextToken();
            Op opNode = new Op(OpType.POWER);
            Expression right = parseTerm(context);

            if (null != right) {
                right.setDoubleType(); // POW operates only with double
                try {
                    opNode.setLeftNode(expr);
                    opNode.setRightNode(right);
                } catch (CastException err) {
                    throw new ParseException(ParseException.CAST_ERROR, err.getMessage(), lexer.currLine(), lexer.currPos()) ;
                }
                expr = opNode; // новый корень
            }
            else{
                throw new ParseException(ParseException.INCOMPLETE_EXPR, "^", lexer.currLine(), lexer.currPos());
            }
            currToken = lexer.peekToken();
        }
        return expr;
    }


    Expression parsePower(Context context) throws ParseException {
        Token currToken = lexer.peekToken();
        if (currToken.hasType(TokenType.MINUS)) {
            lexer.nextToken();
            Expression atom = parseAtom(context);
            if (null == atom) {
                throw new ParseException(ParseException.INCOMPLETE_EXPR, "unary -", lexer.currLine(), lexer.currPos());
            }
            else {
                Op unary = new Op(OpType.SUB);
                try {
                    unary.setLeftNode(ValExpr.ZERO);
                    unary.setRightNode(atom);
                }
                catch (CastException err) {
                    throw new ParseException(ParseException.CAST_ERROR, err.getMessage(), lexer.currLine(), lexer.currPos());
                }
                return unary;
            }

        }
        else {
            return parseAtom(context);
        }
    }


    Expression parseAtom(Context context) throws ParseException {
        Token atom = lexer.peekToken();
        switch (atom.getTokenType()) {
            case NUM:
                lexer.nextToken();
                Type type = Type.INT;
                if (atom.getAttr().contains(".")) {
                    type = Type.DOUBLE;
                }
                return new ValExpr(type, atom.getAttr());
            case ID :
                // check ()
                lexer.nextToken();
                Token currentToken = lexer.peekToken();

                if (currentToken.getTokenType() != TokenType.LBKT) {
                    // check variable
                    Variable v = context.get(atom.getAttr());
                    if (null == v) {
                        throw new ParseException(ParseException.UNKNOWN_ID, atom.getAttr(), lexer.currLine(), lexer.currPos());
                    }
                    return new ValExpr(v.getType(), v.getName());
                }

                lexer.nextToken();
                currentToken = lexer.peekToken();
                String functionName = atom.getAttr();
                Function function = currentProgram.getFunction(functionName);
                if (null == function ) {
                    throw new ParseException(ParseException.UNKNOWN_FUNCTION, functionName , lexer.currLine(), lexer.currPos());
                }

                CallFunctionExpr callFunctionExpr = new CallFunctionExpr(function.getFunctionId(), function.getReturnType());
                callFunctionExpr.setFunction(function);

                ArrayList<Expression> expressionArrayList = parseArgumentList(function, context);

                // check arguments
                callFunctionExpr.setArguments(expressionArrayList);

                currentToken = lexer.peekToken();
                if (currentToken.getTokenType() != TokenType.RBKT) {
                    throw new ParseException(ParseException.RBKT_EXPECTED, functionName, lexer.currLine(), lexer.currPos());
                }
                lexer.nextToken();
                return callFunctionExpr;
            case LBKT:
                lexer.nextToken();
                Expression expression = parseExpression(context);
                if ((null != expression) && expression instanceof CompareOp) {
                    throw new ParseException(ParseException.BOOL_EXPR_WRONG_USE, "atom", lexer.currLine(), lexer.currPos());
                }
                if (lexer.peekToken().getTokenType() != TokenType.RBKT) {
                    throw new ParseException(ParseException.RBKT_EXPECTED, "atom", lexer.currLine(), lexer.currPos());
                }
                lexer.nextToken();
                return expression;
            case TRUE:
                lexer.nextToken();
                return new CompareOp(CompareType.TRUE);
            case FALSE:
                lexer.nextToken();
                return new CompareOp(CompareType.FALSE);
            default:
                return null;
        }
    }
}
