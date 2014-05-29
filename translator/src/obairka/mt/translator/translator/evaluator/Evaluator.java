package obairka.mt.translator.translator.evaluator;

import obairka.mt.translator.parser.nodes.Body;
import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.expression.CompareOp;
import obairka.mt.translator.parser.nodes.expression.Expression;
import obairka.mt.translator.translator.StackSizeCounter;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:06
 */
public class Evaluator {
    private int labelCount = 0;
    private final CommandEvaluatorFactory commandEvaluatorFactory = new CommandEvaluatorFactory();
    private final ExpressionEvaluatorFactory expressionEvaluatorFactory = new ExpressionEvaluatorFactory();

    private StackSizeCounter stackSizeCounter = null;

    public void setCurrentStackSizeCounter(StackSizeCounter s){
        stackSizeCounter = s;
    }

    public StackSizeCounter getStackSizeCounter() {
        return stackSizeCounter;
    }

    public void evaluate(StringBuilder stringBuilder, Context context, Expression expression) {
        ExpressionEvaluator expressionEvaluator = expressionEvaluatorFactory.getInstance(expression);
        expressionEvaluator.evaluate(this, stringBuilder, context);
    }
    void evaluate(StringBuilder stringBuilder, Context context, Command command) {
        CommandEvaluator commandEvaluator = commandEvaluatorFactory.getInstance(command);
        commandEvaluator.evaluate(this, stringBuilder,context);
    }

    public int getNextLabelNum() {
        return ++labelCount;
    }

    public void evaluateBody(final StringBuilder stringBuilder, final Context context, final Body body) {
        for (int i = 0; i < body.getSize(); ++i) {
            evaluate(stringBuilder, context, body.getCommand(i));
        }
    }

    public static char getTypeSymbol(Type type) {
        switch (type){
            case INT:
                return 'I';
            case DOUBLE:
                return 'D';

            case VOID:
            default:
                return 'V';
            /*case BOOL:
            default:
                return 'Z';*/

        }
    }


    public static String getCompareCommandString(CompareOp compareOp) {
        // compare with 0
        // a < b => a-b < 0  iflt
        // a > b => a-b > 0  ifgt
        // TODO:
        switch (compareOp.getCompareType()) {
            case LESS:
                return "ifge";
            case GREATER:
                return "ifle";
            case EQ: default:
                return "ifne";
        }
    }

}
