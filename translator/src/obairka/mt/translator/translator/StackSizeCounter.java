package obairka.mt.translator.translator;

import obairka.mt.translator.parser.nodes.Type;

/**
 * User: obairka@gmail.com
 * Date: 25.05.14
 * Time: 22:19
 */
public class StackSizeCounter {
    private int currentSize = 0;
    private int maxSize = 0;

    public StackSizeCounter(){
        currentSize = maxSize = 0;
    }

    public void increment(int value) {
        currentSize += value;
        if (currentSize > maxSize) {
            maxSize = currentSize;
        }
    }

    void decrement(int value) {
        currentSize -= value;
    }

    public void increment(Type type) {
        increment(getTypeWeight(type));
    }


    public void decrement(Type type) {
        decrement(getTypeWeight(type));
    }

    private int getTypeWeight(Type type){
        switch (type){
            case DOUBLE:
                return 2;
            case INT:
                return 1;
            /*case BOOL:
                return 1; */
            case VOID:
            default:
                return 0;
        }
    }

    public int getMaxSize() {
        return maxSize;
    }
}
