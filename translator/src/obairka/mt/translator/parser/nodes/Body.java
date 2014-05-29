package obairka.mt.translator.parser.nodes;

import java.util.ArrayList;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 10:09
 */
public class Body {
    private final ArrayList<Command> commandArrayList = new ArrayList<Command>();

    public Command getCommand(int i) {
        return commandArrayList.get(i);
    }
    public int getSize() {
        return commandArrayList.size();
    }

    public void addCommand(Command command) {
        commandArrayList.add(command);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Body:\n");
        for (Command c : commandArrayList) {
            stringBuilder.append(c.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj){
            return false;
        }
        if (!(obj instanceof Body)){
            return false;
        }
        Body other = (Body) obj;

        if (commandArrayList.size() != other.commandArrayList.size()) return  false;
        for (int i = 0; i < commandArrayList.size(); ++i) {
            Command c = commandArrayList.get(i);
            Command d = other.commandArrayList.get(i);

            if (!c.equals(d)) {
                return false;
            }
        }
        return true;
    }

}
