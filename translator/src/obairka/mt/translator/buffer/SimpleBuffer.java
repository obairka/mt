package obairka.mt.translator.buffer;

import java.io.IOException;
import java.io.Reader;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 19:21
 */
public class SimpleBuffer implements  Buffer {
    private Reader reader = null;
    private int currLineNum = 1;
    private int currPosInLine = 0;
    private int currIdx = 0;
    private int currSize = 0;
    private final char[] buffer;
    private boolean wasEof = false;

    public SimpleBuffer(Reader reader, int size) {
        this.reader = reader;
        this.buffer = new char[size];
        read();
    }

    @Override
    public int peekChar() {
        if (currIdx == currSize) {
            if (!read()) {
                return Buffer.EOF;
            }
        }
        return buffer[currIdx];
    }

    @Override
    public int nextChar() {
        ++currPosInLine;
        if (currIdx == currSize) {
            if (!read()) {
                return Buffer.EOF;
            }
        }
        if ('\n' == buffer[currIdx]) {
            ++currLineNum;
            currPosInLine = 0;
        }
        return buffer[currIdx++];
    }

    @Override
    public int currentLineNum() {
        return currLineNum;
    }

    @Override
    public int currentPosInLine() {
        return currPosInLine;
    }

    private boolean read() {
        if (wasEof) {
            return false;
        }
        try {
            int count = reader.read(buffer, 0, buffer.length);
            if (count <= 0) {
                wasEof = true;
                return false;
            }
            currSize = count;
        }
        catch (IOException err) {
            // TODO:
        }
        currIdx = 0;
        return true;
    }
}

