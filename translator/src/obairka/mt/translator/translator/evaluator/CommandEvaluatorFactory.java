package obairka.mt.translator.translator.evaluator;

import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.commands.*;
import obairka.mt.translator.parser.nodes.expression.CallFunctionExpr;
import obairka.mt.translator.translator.evaluator.commandEvalutor.*;
import obairka.mt.translator.translator.evaluator.exprEvaluator.CallFunctionExprEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:01
 */
class CommandEvaluatorFactory {
    public CommandEvaluator getInstance(Command currentCommand) {

        if (currentCommand instanceof AssignCommand) {
            return new AssignCommandEvaluator((AssignCommand) currentCommand);
        }
        else
        if (currentCommand instanceof DeclareCommand){
            return new DeclareCommandEvaluator((DeclareCommand)currentCommand);
        }
        else
        if (currentCommand instanceof IfCondition){
            return new IfConditionEvaluator(((IfCondition) currentCommand));
        }
        else
        if (currentCommand instanceof WhileCommand){
            // TODO:
            return new WhileCommandEvaluator((WhileCommand) currentCommand);
        }
        else
        if (currentCommand instanceof ReturnCommand) {
            return new ReturnCommandEvaluator(((ReturnCommand) currentCommand));
        }
        else
        if (currentCommand instanceof PrintCommand) {
            return new PrintCommandEvaluator((PrintCommand) currentCommand);
        }
        else
        if (currentCommand instanceof CallFunctionExpr) {
            return new CallFunctionExprEvaluator(((CallFunctionExpr) currentCommand));
        }
        return null;
    }
}
