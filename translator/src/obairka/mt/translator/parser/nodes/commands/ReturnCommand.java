package obairka.mt.translator.parser.nodes.commands;

import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.expression.Expression;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 10:15
 */
public class ReturnCommand implements Command {
    private Expression expr;
    public ReturnCommand(Expression expr) {
        this.expr = expr;
    }
    public ReturnCommand() {

    }

    public Expression getExpr() {
        return expr;
    }

    public void setExpr(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        if (expr == null) return "return;";
        return "return "+ expr.toString() + ";";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!(obj instanceof ReturnCommand))
            return false;

        ReturnCommand other = (ReturnCommand) obj;
        return expr == null && other.expr == null || other.expr.equals(expr);
    }
}
