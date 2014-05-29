package obairka.mt.translator.translator.evaluator.exprEvaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.expression.Op;
import obairka.mt.translator.translator.evaluator.Evaluator;
import obairka.mt.translator.translator.evaluator.ExpressionEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:59
 */
public class PowerOpEvaluator implements ExpressionEvaluator{

    private static final String POW_FUNCTION_INVOKE = "invokestatic \t java/lang/Math/pow(DD)D";
    private final Op powerOp;
    public PowerOpEvaluator(Op op) {
        powerOp = op;
    }

    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        // TODO : only double ?
        evaluator.evaluate(stringBuilder, context, powerOp.getLeftNode());
        evaluator.evaluate(stringBuilder, context, powerOp.getRightNode());
        stringBuilder.append("\t");
        stringBuilder.append(POW_FUNCTION_INVOKE);
        evaluator.getStackSizeCounter().decrement(Type.DOUBLE);
        stringBuilder.append("\n");
    }
}
