package obairka.mt.translator.parser.nodes.commands;

import obairka.mt.translator.parser.nodes.*;
import obairka.mt.translator.parser.nodes.expression.CompareOp;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 10:18
 */
public class IfCondition implements Command {
    private boolean elseBlock = false;
    private final CompareOp compareOp;
    private final Context parentContext;
    private Context ifContext = null;
    private Context elseContext = null;
    private Body ifBody = null;
    private Body elseBody = null;

    public CompareOp getCompareOp() {
        return compareOp;
    }

    public IfCondition(Context parent, CompareOp compareOp) {
        ifContext = new Context(parent);
        this.parentContext = parent;
        this.compareOp = compareOp;
    }

    public Body getIfBody() {
        return ifBody;
    }
    public Body getElseBody() {
        return elseBody;
    }

    public void setIfBody(Body body) {
        ifBody = body;
    }

    public void setElseBody(Body elseBody) {
        this.elseBody = elseBody;
    }

    public Context getIfContext() {
        return ifContext;
    }

    public void switchElseBlock() {
        elseBlock = true;
    }

    public Context getElseContext() {
        if (null == elseContext && elseBlock) {
            elseContext = new Context(parentContext);
        }
        return elseContext;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("if (");
        stringBuilder.append(compareOp);
        stringBuilder.append("){\n");
        stringBuilder.append(ifBody);
        stringBuilder.append("} ");
        if (elseBlock)  {
            stringBuilder.append("else {\n");
            stringBuilder.append(elseBody);
            stringBuilder.append("}\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof IfCondition)){
            return false;
        }
        IfCondition other = (IfCondition) obj;
        boolean parentContextBool = parentContext.equals(other.parentContext);
        if (!parentContextBool) return false;
        boolean ifContextBool = ifContext.equals(other.ifContext);
        if (!ifContextBool) return false;
        boolean elseContextBool = (elseBlock ==other.elseBlock) &&
                elseBlock && (elseContext != null) &&  elseContext.equals(other.elseContext)
                ||
                !elseBlock && !other.elseBlock &&
                                            (elseContext == null) && (other.elseContext == null);
        if (!elseContextBool) return false;

        boolean ifBodyBool = (ifBody == other.ifBody) && (ifBody == null) ||
                            (ifBody != null) && ifBody.equals(other.ifBody);
        if (!ifBodyBool) return false;

        boolean elseBodyBool = (elseBody == other.elseBody) && (elseBody == null) ||
                (elseBody != null) && elseBody.equals(other.elseBody);


        return elseBodyBool && compareOp.equals(other.compareOp);
    }
}
