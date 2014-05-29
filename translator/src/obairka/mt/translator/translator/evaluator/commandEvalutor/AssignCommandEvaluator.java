package obairka.mt.translator.translator.evaluator.commandEvalutor;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.commands.AssignCommand;
import obairka.mt.translator.translator.evaluator.CommandEvaluator;
import obairka.mt.translator.translator.evaluator.Evaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:04
 */
public class AssignCommandEvaluator implements CommandEvaluator {
    private final AssignCommand command;
    public AssignCommandEvaluator(AssignCommand assignCommand) {
        this.command = assignCommand;
    }

    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        char type = Evaluator.getTypeSymbol(command.getAssignVar().getType());
        int num = command.getAssignVar().getId();

        evaluator.evaluate(stringBuilder, context, command.getExpr() );

        StringBuilder storeString = new StringBuilder();

        stringBuilder.append("\t");
        storeString.append(Character.toLowerCase(type));
        storeString.append("store\t\t\t");

        //pop
        evaluator.getStackSizeCounter().decrement(command.getAssignVar().getType());

        storeString.append(num);

        stringBuilder.append(storeString);
        stringBuilder.append("\n");

    }
}
