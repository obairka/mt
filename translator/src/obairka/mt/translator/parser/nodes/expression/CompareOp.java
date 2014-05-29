package obairka.mt.translator.parser.nodes.expression;

import obairka.mt.translator.parser.exceptions.CastException;
import obairka.mt.translator.parser.nodes.Type;

/**
 * User: obairka@gmail.com
 * Date: 18.05.14
 * Time: 16:17
 */
public class CompareOp extends Expression {
    private Expression left;
    private Expression right;

    private final CompareType compareType;


    public CompareOp(CompareType compareType){
        expressionType = Type.VOID;
        this.compareType = compareType;
    }

    public CompareType getCompareType() {
        return compareType;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public void setLeft(Expression left) throws CastException {
        this.left = left;
        evaluateType();
    }

    public void setRight(Expression right) throws CastException{
        this.right = right;
        evaluateType();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return  false;
        if (!(obj instanceof CompareOp)) {
            return  false;
        }
        CompareOp compareOp = (CompareOp) obj;
        return ((compareOp.left == left) && (left == null) || compareOp.left.equals(left))
               && ((compareOp.right == right) && (right == null) || compareOp.right.equals(right)) && (compareType == compareOp.compareType);
    }


    private void evaluateType() throws CastException {
        if ((left != null) && (right != null)) {
            if (left.hasVoidType() || right.hasVoidType() ){
                throw new CastException("Operand has VOID type");
            }
            if ( (left.expressionType == right.expressionType)  ) {
                expressionType = left.expressionType;
                return;
            }

            if ( (left.expressionType == Type.DOUBLE) && (right.expressionType == Type.INT) )
            {
                // rightNode conversion
                right = new ConvertType(right, Type.DOUBLE);
                expressionType = Type.DOUBLE;
                return;
            }
            if ( (right.expressionType == Type.DOUBLE) && (left.expressionType == Type.INT)){
                // leftNode conversion
                left =  new ConvertType(left, Type.DOUBLE);
                expressionType = Type.DOUBLE;
                return;
            }
            // Type Cast Error!
            throw new CastException("Cast exception error :"+ left.expressionType + " " + right.expressionType);
        }
        else
        if (left != null) {
            expressionType = left.expressionType;
        }
        else
        if (right != null) {
            expressionType = right.expressionType;
        }
        else{
            expressionType = Type.VOID;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");
        if (left != null) {
            stringBuilder.append(left.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append(compareType.toString());
        stringBuilder.append("\n");
        if (right != null) {
            stringBuilder.append(right.toString());
            stringBuilder.append("\n");
            stringBuilder.append("EXPRESSION TYPE:").append(expressionType);
        }
        return stringBuilder.toString();
    }
}
