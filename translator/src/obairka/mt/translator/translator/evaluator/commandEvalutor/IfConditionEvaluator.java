package obairka.mt.translator.translator.evaluator.commandEvalutor;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.commands.IfCondition;
import obairka.mt.translator.translator.evaluator.CommandEvaluator;
import obairka.mt.translator.translator.evaluator.Evaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:28
 */
public class IfConditionEvaluator implements CommandEvaluator {
    private final IfCondition command;
    public IfConditionEvaluator(IfCondition ifCondition) {
        this.command = ifCondition;
    }
    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        // TODO:

        evaluator.evaluate(stringBuilder, context, command.getCompareOp() );

        String ifOperationString = Evaluator.getCompareCommandString(command.getCompareOp());
        stringBuilder.append("\t").append(ifOperationString);
        stringBuilder.append("\t\t\t\t");
        stringBuilder.append("LABEL_FALSE_");
        int falseLabel =  evaluator.getNextLabelNum();
        stringBuilder.append(falseLabel);
        stringBuilder.append("\n");

        evaluator.evaluateBody(stringBuilder, command.getIfContext(), command.getIfBody());


        stringBuilder.append("\tgoto LABEL_CONT_");
        int contLabel = evaluator.getNextLabelNum();
        stringBuilder.append(contLabel);
        stringBuilder.append("\n");



        stringBuilder.append("\tLABEL_FALSE_");
        stringBuilder.append(falseLabel);
        stringBuilder.append(":\n");

        if (command.getElseBody() != null) {
            evaluator.evaluateBody(stringBuilder, command.getElseContext(), command.getElseBody());
        }
        else {
            stringBuilder.append("\tgoto LABEL_CONT_");
            stringBuilder.append(contLabel);
            stringBuilder.append("\n");
        }

        stringBuilder.append("\tLABEL_CONT_");
        stringBuilder.append(contLabel);
        stringBuilder.append(":\n");

    }
}
