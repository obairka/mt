package obairka.mt.translator.parser.nodes.expression;

import obairka.mt.translator.parser.exceptions.CastException;
import obairka.mt.translator.parser.nodes.Type;


/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 11:56
 */
public  class Op extends Expression {

    public OpType getOpType() {
        return opType;
    }

    private final OpType opType;
    private Expression leftNode = null;
    private Expression rightNode = null;

    public Op(OpType opType) {
        this.opType = opType;
    }

    public Expression getLeftNode() {
        return leftNode;
    }

    public Expression getRightNode() {
        return rightNode;
    }

    // TODO: change function implementation
    private void evaluateType() throws CastException {
        if ((leftNode != null) && (rightNode != null)) {
            if (leftNode.hasVoidType() || rightNode.hasVoidType() ){
                throw new CastException("Operand has VOID type");
            }
            if ( (leftNode.expressionType == rightNode.expressionType)  ) {
                expressionType = leftNode.expressionType;
                return;
            }

            if ( (leftNode.expressionType == Type.DOUBLE) && (rightNode.expressionType == Type.INT) )
            {
                // rightNode conversion
                rightNode =  new ConvertType(rightNode, Type.DOUBLE);
                expressionType = Type.DOUBLE;
                return;
            }
            if ( (rightNode.expressionType == Type.DOUBLE) && (leftNode.expressionType == Type.INT)){
                // leftNode conversion
                leftNode = new ConvertType(leftNode, Type.DOUBLE);
                expressionType = Type.DOUBLE;
                return;
            }
            // Type Cast Error!
            throw new CastException("Cast exception error :"+ leftNode.expressionType + " " + rightNode.expressionType);
        }
        else
        if (leftNode != null) {
            expressionType = leftNode.expressionType;
        }
        else
        if (rightNode != null) {
            expressionType = rightNode.expressionType;
        }
        else{
            expressionType = Type.VOID;
        }
    }

    public void setLeftNode(Expression leftNode)throws CastException{
        // TODO : expression type
        this.leftNode = leftNode;
        evaluateType();
    }

    public void setRightNode(Expression rightNode) throws CastException{
        this.rightNode = rightNode;
        evaluateType();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return  false;
        if (!(obj instanceof Op)) {
            return  false;
        }
        Op opExpr = (Op) obj;
        return
        super.equals(obj) &&
        (
           ((opExpr.leftNode == leftNode) && (leftNode == null) || opExpr.leftNode.equals(leftNode))
            &&
           ((opExpr.rightNode == rightNode) && (rightNode == null) || opExpr.rightNode.equals(rightNode))
        )   && (opType == opExpr.opType);
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\n");
        if (leftNode != null) {
            stringBuilder.append(leftNode.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append(expressionType);
        stringBuilder.append("\n");
        if (rightNode != null) {
            stringBuilder.append(rightNode.toString());
            stringBuilder.append("EXPRESSION TYPE:").append(expressionType);
        }
        return stringBuilder.toString();
    }
}
