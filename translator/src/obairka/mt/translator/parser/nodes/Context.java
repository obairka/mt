package obairka.mt.translator.parser.nodes;

import java.util.HashMap;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 9:48
 */
public class Context {
    private int idCounter = 0;
    private final HashMap<String, Variable> variableMap;

    public Context(Context other) {
        // copy
        variableMap = new HashMap<String, Variable>(other.variableMap);
        idCounter = other.size();
    }

    public Context() {
        variableMap = new HashMap<String, Variable>();
    }

    public void incrementIdCounter() {
        ++idCounter;
    }

    public void addVariable(Variable var) {
        int value;
        switch (var.getType()) {
            case DOUBLE:
                value = 2;
                break;
            case INT: /*case BOOL:*/
                value = 1;
                break;
            case VOID: default:
                value = 0; //error
        }


       // TODO : what is with context number??
        variableMap.put(var.getName(), var);
        var.setId(idCounter);
        idCounter+= value;
    }

    public Variable get(String name) {
        return variableMap.get(name);
    }

    int size() {
        return variableMap.size();
    }

    @Override
    public String toString() {
        return variableMap.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!(obj instanceof Context))
            return false;

        Context other = (Context) obj;
        return (idCounter == other.idCounter) && variableMap.equals(other.variableMap);
    }
}
