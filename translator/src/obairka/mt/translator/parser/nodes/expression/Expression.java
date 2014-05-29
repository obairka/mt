package obairka.mt.translator.parser.nodes.expression;

import obairka.mt.translator.parser.nodes.Type;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 10:07
 */
public abstract class Expression {
    Type expressionType = Type.VOID;

    public Type getExprType() {
        return expressionType;
    }

    boolean hasVoidType() {
        return  (expressionType == Type.VOID);
    }

    public void setDoubleType() {
        expressionType = Type.DOUBLE;
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + expressionType.toString();
    }

    @Override
    public boolean equals(Object other){

        if (other == null) return  false;
        if (other instanceof Expression) {
            Expression ob = (Expression) other;
            return expressionType.equals(ob.getExprType());
        }
        return false;
    }
}
