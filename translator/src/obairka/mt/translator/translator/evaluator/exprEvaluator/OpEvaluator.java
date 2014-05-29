package obairka.mt.translator.translator.evaluator.exprEvaluator;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.expression.Op;
import obairka.mt.translator.translator.evaluator.Evaluator;
import obairka.mt.translator.translator.evaluator.ExpressionEvaluator;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 0:44
 */

// + - * /
public class OpEvaluator implements ExpressionEvaluator {
    private final String opCommand;
    private final Op operation;

    public OpEvaluator(String opCommand, Op op ){
        this.opCommand = opCommand;
        operation = op;
    }
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context){
        Type exprOpType = operation.getExprType();
        char typeCharacter = Character.toLowerCase(Evaluator.getTypeSymbol(exprOpType));
        evaluator.evaluate(stringBuilder, context, operation.getLeftNode() );
        evaluator.evaluate(stringBuilder, context, operation.getRightNode() );

        stringBuilder.append("\t");
        stringBuilder.append(typeCharacter);
        stringBuilder.append(opCommand);
        evaluator.getStackSizeCounter().decrement(exprOpType);
        stringBuilder.append("\n");
    }
}
