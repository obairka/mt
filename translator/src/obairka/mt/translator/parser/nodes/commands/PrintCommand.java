package obairka.mt.translator.parser.nodes.commands;

import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.Variable;
import obairka.mt.translator.parser.nodes.expression.Expression;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 10:17
 */
public class PrintCommand implements Command {
    /*
    private final Variable variable;
    public PrintCommand(Variable v) {
        variable = v;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public String toString() {
        return "print("+ variable.getName() + ");";
    }

    @Override
    public boolean equals(Object obj) {

        return (obj !=null )&&(obj instanceof  PrintCommand) && variable.equals(((PrintCommand) obj).getVariable());
    }
    */

    private final Expression expression;

    public PrintCommand(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "print("+ expression.toString() + ");";
    }

    @Override
    public boolean equals(Object obj) {

        return (obj !=null )&&(obj instanceof  PrintCommand) && expression.equals(((PrintCommand) obj).expression);
    }
}
