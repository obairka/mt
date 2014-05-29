package obairka.mt.translator.parser.nodes.commands;

import obairka.mt.translator.parser.nodes.Body;
import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.Context;
import obairka.mt.translator.parser.nodes.expression.CompareOp;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 10:35
 */
public class WhileCommand implements Command {
    private Body body;
    private final CompareOp compareOp;
    private final Context context;

    public WhileCommand(Context context, CompareOp compareOp){
        this.context = new Context(context);
        this.compareOp = compareOp;
    }

    public void setBody(Body body) {
        this.body = body;
    }
    public Context getContext() {
        return context;
    }
    public CompareOp getCompareOp() {
        return compareOp;
    }
    public Body getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "WHILE ("+ compareOp.toString() + ")\n" + body.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof WhileCommand)){
            return false;
        }
        WhileCommand other = (WhileCommand) obj;

        boolean ifContextBool = context.equals(other.context);
        if (!ifContextBool) return false;
        boolean bodyBool = (body == other.body) && (body == null) ||
                (body != null) && body.equals(other.body);

        return bodyBool && compareOp.equals(other.compareOp);
    }
}
