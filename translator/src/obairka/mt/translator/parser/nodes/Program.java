package obairka.mt.translator.parser.nodes;


import java.util.Collection;
import java.util.HashMap;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 12:01
 */
public class Program {
    private int functionCounter = 0;
    private Function mainFunction = null;

    private final HashMap<String, Function> functionHashMap = new HashMap<String, Function>();

    public Collection<Function> getFunctions() {
        return functionHashMap.values();
    }

    public void setMainFunction(Function mainFunction) {
        this.mainFunction = mainFunction;
        mainFunction.setFunctionId(0);
    }
    public void addFunction(Function function) {
        if (function != mainFunction && ( mainFunction != null) ) {
            ++functionCounter;
            function.setFunctionId(functionCounter);
        }
        functionHashMap.put(function.getFunctionName(), function);

    }

    public Function getFunction (String name) {
        return functionHashMap.get(name);
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Functions count : ");
        stringBuilder.append(functionHashMap.values().size());
        stringBuilder.append("\n");
        for (Function f : functionHashMap.values()) {
            stringBuilder.append(f.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!(obj instanceof Program))
            return false;
        final Program other = (Program) obj;
        final Collection<Function> functions = functionHashMap.values();
        final Collection<Function> otherFunctions = other.functionHashMap.values();


        return (functionCounter == other.functionCounter) && functions.containsAll(otherFunctions) && otherFunctions.containsAll(functions) &&

                ((mainFunction == null) && (other.mainFunction == null) || (mainFunction != null) && mainFunction.equals(other.mainFunction)

                );
    }
}
