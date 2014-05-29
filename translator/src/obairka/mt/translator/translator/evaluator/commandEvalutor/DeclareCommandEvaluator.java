package obairka.mt.translator.translator.evaluator.commandEvalutor;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.commands.DeclareCommand;
import obairka.mt.translator.translator.evaluator.CommandEvaluator;
import obairka.mt.translator.translator.evaluator.Evaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:13
 */
public class DeclareCommandEvaluator implements CommandEvaluator {

    private final DeclareCommand command;
    public DeclareCommandEvaluator(DeclareCommand declareCommand) {
        this.command = declareCommand;
    }
    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        // TODO:
        evaluator.getStackSizeCounter().increment(command.getType());
        evaluator.getStackSizeCounter().decrement(command.getType());

    }
}
