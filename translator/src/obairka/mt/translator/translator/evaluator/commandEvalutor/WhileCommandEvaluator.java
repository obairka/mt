package obairka.mt.translator.translator.evaluator.commandEvalutor;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.commands.WhileCommand;
import obairka.mt.translator.translator.evaluator.CommandEvaluator;
import obairka.mt.translator.translator.evaluator.Evaluator;

/**
 * User: obairka@gmail.com
 * Date: 29.05.14
 * Time: 22:35
 */
public class WhileCommandEvaluator implements CommandEvaluator {
    private final WhileCommand command;
    public WhileCommandEvaluator(WhileCommand command){
        this.command = command;
    }
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context ){
        // label
        stringBuilder.append("WHILE_");
        int whileLabel =  evaluator.getNextLabelNum();
        stringBuilder.append(whileLabel).append(":\n");

        evaluator.evaluate(stringBuilder, context, command.getCompareOp());
        String ifOperationString = Evaluator.getCompareCommandString(command.getCompareOp());
        stringBuilder.append("\t").append(ifOperationString);
        stringBuilder.append(" done").append(whileLabel).append("\n");
        // body
        evaluator.evaluateBody(stringBuilder, context, command.getBody());
        //goto WHILE label
        stringBuilder.append("\tgoto WHILE_").append(whileLabel).append("\n\n");
        // done label:
        stringBuilder.append("done").append(whileLabel).append(":\n\n");
    }
}
