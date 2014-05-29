package obairka.mt.translator.translator.evaluator.commandEvalutor;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.commands.ReturnCommand;
import obairka.mt.translator.parser.nodes.expression.Expression;
import obairka.mt.translator.translator.evaluator.CommandEvaluator;
import obairka.mt.translator.translator.evaluator.Evaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:25
 */
public class ReturnCommandEvaluator implements CommandEvaluator {
    private final ReturnCommand command;
    public ReturnCommandEvaluator(ReturnCommand returnCommand){
        this.command = returnCommand;
    }

    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        Expression expr = command.getExpr();
        if (null == expr) {
            stringBuilder.append("return\n");
            return;
        }

        evaluator.evaluate(stringBuilder, context, expr );

        char type = Character.toLowerCase(Evaluator.getTypeSymbol(expr.getExprType()));
        evaluator.getStackSizeCounter().increment(expr.getExprType());
        stringBuilder.append(type);
        stringBuilder.append("return");
        stringBuilder.append("\n");
        evaluator.getStackSizeCounter().decrement(expr.getExprType());
    }
}
