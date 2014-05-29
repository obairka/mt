package obairka.mt.translator.parser.nodes.commands;

import obairka.mt.translator.parser.nodes.Command;
import obairka.mt.translator.parser.nodes.Type;
import obairka.mt.translator.parser.nodes.Variable;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 15:00
 */
public class DeclareCommand implements Command {
    private final Variable variable;

    public DeclareCommand(Variable variable){
        this.variable = variable;
    }

    public Type getType() {
        return variable.getType();
    }

    @Override
    public String toString() {
        return  variable.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof DeclareCommand)){
            return false;
        }
        DeclareCommand other = (DeclareCommand) obj;
        return other.variable.equals(variable);
    }
}
