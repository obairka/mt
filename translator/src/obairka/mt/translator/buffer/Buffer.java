package obairka.mt.translator.buffer;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 18:47
 */
public interface Buffer {
    public int peekChar();
    public int nextChar();
    public int currentLineNum();
    public int currentPosInLine();

    public static final int EOF = -1;
}
