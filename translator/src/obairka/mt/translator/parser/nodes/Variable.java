package obairka.mt.translator.parser.nodes;

/**
 * User: obairka@gmail.com
 * Date: 14.05.14
 * Time: 9:47
 */
public class Variable {
    private static final int UNDEFINED_ID = -1;
    private final String name;
    private final Type type;
    private int id;

    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
        this.id = UNDEFINED_ID;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getId() {
        return  id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof Variable)){
            return false;
        }
        Variable other = (Variable) obj;
        return type.equals(other.getType()) && name.equals(other.getName());
    }

}
