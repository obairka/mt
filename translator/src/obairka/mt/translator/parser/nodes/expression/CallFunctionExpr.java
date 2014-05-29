package obairka.mt.translator.parser.nodes.expression;

import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.Function;
import obairka.mt.translator.parser.nodes.Type;
import java.util.ArrayList;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 11:58
 */
public class CallFunctionExpr extends Expression  implements Command {
    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    private Function function;
    private final int functionId;
    private ArrayList<Expression> arguments = null;

    public Expression getArgumentExpression(int i ) {
        return arguments.get(i);
    }

    public CallFunctionExpr(int id, Type returnType) {
        this.functionId = id;
        this.expressionType = returnType;
    }

    public void setArguments(ArrayList<Expression> expressionArrayList)  {

        arguments = expressionArrayList;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return  false;
        if (other instanceof CallFunctionExpr) {
            CallFunctionExpr otherExpr = (CallFunctionExpr) other;
            return super.equals(other) && (functionId == otherExpr.functionId) &&
                    ((arguments == null) && (otherExpr.arguments == null) || (arguments!=null)&&arguments.equals(otherExpr.arguments));
        }
        return false;
    }
}
