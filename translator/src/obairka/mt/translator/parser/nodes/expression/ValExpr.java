package obairka.mt.translator.parser.nodes.expression;

import obairka.mt.translator.parser.nodes.Type;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 11:53
 */
public class ValExpr extends Expression {
    public static final ValExpr ZERO = new ValExpr(Type.INT, "0");
    private final String value;
    public ValExpr(Type type, String val) {
        expressionType = type;
        value = val;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getExprType() +" " +value;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return  false;
        if (other instanceof ValExpr) {
            ValExpr otherExpr = (ValExpr) other;


            return super.equals(other) &&
                    (((value == null) && (otherExpr.value == null)) || (value != null) && value.equals(otherExpr.value));
        }
        return false;
    }
}
