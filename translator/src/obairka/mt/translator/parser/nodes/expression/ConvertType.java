package obairka.mt.translator.parser.nodes.expression;

import obairka.mt.translator.parser.nodes.Type;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 10:12
 */
public class ConvertType extends Expression {
    private final Expression nodeToConvert;
    private final Type typeToConvert;

    public ConvertType(Expression expression, Type type) {
        this.nodeToConvert = expression;
        this.typeToConvert = type;
    }

    public Type getTypeToConvert() {
        return typeToConvert;
    }

    public Expression getNodeToConvert() {
        return nodeToConvert;
    }

    public String toString() {
        return "Convert: "+nodeToConvert.toString() + " to " + typeToConvert.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (null != o) && (o instanceof ConvertType) && nodeToConvert.equals(((ConvertType) o).getNodeToConvert()) && (typeToConvert == ((ConvertType) o).typeToConvert);
    }

}
