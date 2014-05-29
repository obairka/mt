package obairka.mt.translator.translator.evaluator.exprEvaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.expression.ConvertType;
import obairka.mt.translator.translator.evaluator.Evaluator;
import obairka.mt.translator.translator.evaluator.ExpressionEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 10:35
 */
public class ConvertTypeEvaluator implements ExpressionEvaluator {
    private static int conversionString(StringBuilder stringBuilder, ConvertType convertType) {
        //
        switch (convertType.getTypeToConvert()) {
            case DOUBLE:
                switch (convertType.getNodeToConvert().getExprType()){
                    case INT:
                        stringBuilder.append("i2d");
                        stringBuilder.append("\n");
                        return 2; // add 1
                    /*case BOOL: */case VOID: case DOUBLE:
                    default:
                        // TODO:
                    return 0;
                }
            case INT:
                switch (convertType.getNodeToConvert().getExprType()) {
                    case DOUBLE:
                        stringBuilder.append("d2i");
                        stringBuilder.append("\n");
                        return 1;

                    /*case BOOL: */case VOID: case INT:
                    default:
                        // TODO:
                        return 0;
                }
            default: return 0;
        }
    }

    private final ConvertType convertType;

    public ConvertTypeEvaluator(ConvertType convertType) {
        this.convertType = convertType;
    }

    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {

        evaluator.evaluate(stringBuilder, context, convertType.getNodeToConvert());

        int stackSize = ConvertTypeEvaluator.conversionString(stringBuilder, convertType);


        evaluator.getStackSizeCounter().decrement(convertType.getNodeToConvert().getExprType());
        evaluator.getStackSizeCounter().increment(stackSize);

        stringBuilder.append("\n");
        // increment stacksize
    }
}
