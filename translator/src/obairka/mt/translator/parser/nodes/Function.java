package obairka.mt.translator.parser.nodes;

import java.util.ArrayList;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 9:57
 */
public class Function {
    private static final int UNDEFINED_ID = -1;
    private int functionId = UNDEFINED_ID;

    private boolean returnStatementWas = true;
    private Type returnType = Type.VOID;
    private Body body = null;
    private String functionName = null;

    private final Context context = new Context();

    private final ArrayList<Variable> arguments = new ArrayList<Variable>();

    private int localSize = 0;

    public void incrementLocal(Type type) {
        int value;
        switch (type) {
            case DOUBLE:
                value = 2;
                break;
            case INT: /*case BOOL:*/
                value = 1;
                break;
            case VOID: default:
                value = 0;
        }
        localSize += value;
    }

    public int getLocalSize() {
        return localSize;
    }

    public Type getReturnType() {
        return returnType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setReturnType(Type type) {
        returnType = type;
        if (returnType != Type.VOID) {
            returnStatementWas = false;
        }
    }

    public void setReturnStatementWas(boolean b) {
        returnStatementWas = b;
    }

    public boolean isReturnStatementWas() {
        return returnStatementWas;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public Context getContext() {
        return context;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }


    public void addArgument(Variable v) {
        context.addVariable(v);
        arguments.add(v);
        incrementLocal(v.getType());
    }

    public Type getArgumentType(int argument_idx) {
        return arguments.get(argument_idx).getType();
    }
    public int getArgumentsCount() {
        return arguments.size();
    }

    public boolean isMain()  {
        return "main".equals(functionName);
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(returnType);
        stringBuilder.append(" ");
        stringBuilder.append(functionName);
        stringBuilder.append("(");
        for (Variable v : arguments) {
            stringBuilder.append(v.toString());
            stringBuilder.append(" ");
        }
        stringBuilder.append(")");
        stringBuilder.append("\n");
        if (body != null)
        stringBuilder.append(body.toString());
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!(obj instanceof Function))
            return false;

        Function other = (Function) obj;

        boolean  bodyBool =  (body == other.body) && (body == null) ||
                (body != null) && body.equals(other.body);
        boolean functionNameBool = ( (other.functionName == null) &&(functionName == null)) ||
                                    (functionName != null) && functionName.equals(other.functionName);



        return (localSize == other.localSize) && bodyBool && functionNameBool && context.equals(other.context) && arguments.equals(other.arguments);
    }
}
