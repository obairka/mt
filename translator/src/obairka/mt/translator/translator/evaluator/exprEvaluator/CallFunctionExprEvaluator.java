package obairka.mt.translator.translator.evaluator.exprEvaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Function;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.expression.CallFunctionExpr;
import obairka.mt.translator.parser.nodes.expression.Expression;
import obairka.mt.translator.translator.evaluator.CommandEvaluator;
import obairka.mt.translator.translator.evaluator.Evaluator;
import obairka.mt.translator.translator.evaluator.ExpressionEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:42
 */
public class CallFunctionExprEvaluator implements ExpressionEvaluator, CommandEvaluator {
    private final CallFunctionExpr expr;
    public CallFunctionExprEvaluator(CallFunctionExpr callFunctionExpr) {
        this.expr = callFunctionExpr;
    }


    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        Function function = expr.getFunction();
        for (int i = 0; i < function.getArgumentsCount(); ++i) {
            Expression expression = expr.getArgumentExpression(i);
            evaluator.evaluate(stringBuilder,context, expression);
        }

        stringBuilder.append("\tinvokestatic\t\t");
        stringBuilder.append("Program/");
        stringBuilder.append(function.getFunctionName());
        stringBuilder.append("(");
        for (int i = 0; i < function.getArgumentsCount(); ++i) {
            char c = Evaluator.getTypeSymbol(function.getArgumentType(i));
            stringBuilder.append(c);
        }
        stringBuilder.append(")");
        stringBuilder.append(Evaluator.getTypeSymbol(function.getReturnType()));
        evaluator.getStackSizeCounter().increment(function.getReturnType());
        evaluator.getStackSizeCounter().decrement(function.getReturnType());

        stringBuilder.append("\n");
    }
}
