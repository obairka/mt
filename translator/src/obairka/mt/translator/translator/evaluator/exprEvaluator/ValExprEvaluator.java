package obairka.mt.translator.translator.evaluator.exprEvaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.Variable;
import obairka.mt.translator.parser.nodes.expression.ValExpr;
import obairka.mt.translator.translator.evaluator.Evaluator;
import obairka.mt.translator.translator.evaluator.ExpressionEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:33
 */
public class ValExprEvaluator implements ExpressionEvaluator {
    private final ValExpr valExpr;
    public ValExprEvaluator(ValExpr valExpr) {
        this.valExpr = valExpr;
    }
    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        String value = valExpr.getValue();

        Variable variable = context.get(value);

        Type type = valExpr.getExprType();

        if (null == variable) {

            if (type == Type.DOUBLE) {
                stringBuilder.append("\t");
                stringBuilder.append("ldc2_w\t\t\t\t");
                stringBuilder.append(new Double(value));
                evaluator.getStackSizeCounter().increment(Type.DOUBLE);
            } else {
                stringBuilder.append("\t");
                stringBuilder.append("bipush\t\t\t\t");
                stringBuilder.append(value);
                evaluator.getStackSizeCounter().increment(Type.INT);
            }
            stringBuilder.append("\n");
            return;
        }
        char c = Character.toLowerCase(Evaluator.getTypeSymbol(type));
        stringBuilder.append("\t");
        stringBuilder.append(c);
        stringBuilder.append("load\t\t\t");
        //push
        evaluator.getStackSizeCounter().increment(type);
        stringBuilder.append(variable.getId());
        stringBuilder.append("\n");
    }
}
