package obairka.mt.translator.translator.evaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:07
 */
public interface ExpressionEvaluator {
    void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context);
}
