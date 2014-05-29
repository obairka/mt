package obairka.mt.translator.translator.evaluator.commandEvalutor;

import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.Variable;
import obairka.mt.translator.parser.nodes.commands.PrintCommand;
import obairka.mt.translator.parser.nodes.expression.Expression;
import obairka.mt.translator.translator.evaluator.CommandEvaluator;
import obairka.mt.translator.translator.evaluator.Evaluator;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:14
 */
public class PrintCommandEvaluator implements CommandEvaluator {

    private static final String PRINT_FUNCTION_INVOKE_PREFIX =
            "invokevirtual\t\tjava/io/PrintStream/println(";

    private final PrintCommand command;
    public PrintCommandEvaluator(PrintCommand printCommand) {
        this.command = printCommand;
    }
    @Override
    public void evaluate(Evaluator evaluator, StringBuilder stringBuilder, Context context) {
        stringBuilder.append("getstatic\t\t\tjava/lang/System/out Ljava/io/PrintStream;\n");
        Expression expression = command.getExpression();
        char type = Evaluator.getTypeSymbol(expression.getExprType());

        evaluator.evaluate(stringBuilder,context,expression);

        stringBuilder.append("\t"+PRINT_FUNCTION_INVOKE_PREFIX);
        stringBuilder.append(type);
        stringBuilder.append(")V\n");
    }
}
