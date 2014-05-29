package obairka.mt.translator.translator.evaluator;

import obairka.mt.translator.parser.nodes.*;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:01
 */
public interface CommandEvaluator {
    void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context );
}
