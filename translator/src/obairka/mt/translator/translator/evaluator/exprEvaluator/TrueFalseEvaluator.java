package obairka.mt.translator.translator.evaluator.exprEvaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.translator.evaluator.Evaluator;
import obairka.mt.translator.translator.evaluator.ExpressionEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 1:34
 */
public class TrueFalseEvaluator implements ExpressionEvaluator {
    private boolean isTrue = false;

    public TrueFalseEvaluator(boolean b) {
        isTrue = b;
    }

    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        /*char  c;
        switch (etype){
            case BOOL: case INT:
                c = 'i';
            case DOUBLE:default:
                c = 'd';
        }

        stringBuilder.append("\t");
        stringBuilder.append(c);      */
        stringBuilder.append("\t");
        if (isTrue) {
            stringBuilder.append("iconst_1\n");
        }
        else{
            stringBuilder.append("iconst_0\n");
        }
        evaluator.getStackSizeCounter().increment(Type.INT);
    }
}
