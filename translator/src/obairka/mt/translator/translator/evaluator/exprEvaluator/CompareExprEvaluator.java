package obairka.mt.translator.translator.evaluator.exprEvaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.expression.CompareOp;
import obairka.mt.translator.translator.evaluator.Evaluator;
import obairka.mt.translator.translator.evaluator.ExpressionEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 0:55
 */
public class CompareExprEvaluator implements ExpressionEvaluator {

    private final CompareOp compareOp;

    public CompareExprEvaluator(  CompareOp compareOp) {

        this.compareOp = compareOp;
    }


    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {

        char type = Character.toLowerCase(Evaluator.getTypeSymbol(compareOp.getExprType()));
        evaluator.evaluate(stringBuilder, context, compareOp.getLeft());
        evaluator.evaluate(stringBuilder, context, compareOp.getRight());
        stringBuilder.append("\t");
        stringBuilder.append(type);
        if (compareOp.getExprType() == Type.INT)
            stringBuilder.append("sub");
        else
            stringBuilder.append("cmpl");
        stringBuilder.append("\n");
        evaluator.getStackSizeCounter().decrement(compareOp.getExprType());


    }
}
