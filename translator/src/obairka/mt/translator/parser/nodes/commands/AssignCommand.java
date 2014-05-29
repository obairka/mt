package obairka.mt.translator.parser.nodes.commands;

import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.expression.Expression;
import obairka.mt.translator.parser.nodes.Variable;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 10:05
 */

public class AssignCommand implements Command {
    private final Variable assignVar;
    private final Expression expr;

    public AssignCommand(Variable var, Expression expr) {
        this.assignVar = var;
        this.expr = expr;
    }

    public Variable getAssignVar() {
        return assignVar;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return assignVar.getName() + " = " + expr.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof AssignCommand)){
            return false;
        }
        AssignCommand other = (AssignCommand) obj;
        boolean  exprBool =    other.expr.equals(expr);
        boolean  assignBool =  other.assignVar.equals(assignVar);

        return  exprBool && assignBool;
    }

}
