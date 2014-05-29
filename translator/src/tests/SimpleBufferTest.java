package tests;

import static org.junit.Assert.*;

import obairka.mt.translator.buffer.SimpleBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 19:47
 */
public class SimpleBufferTest {
    private final String bufferStringExample = "buffer string example\n" +
                                               "buffer string example\n" +
                                               "buffer string example";
    private SimpleBuffer simpleBuffer = null;

    @Before
    public void setUp() throws Exception {
        Reader stringReader = new StringReader(bufferStringExample);
        int bufferSize = 4;
        simpleBuffer = new SimpleBuffer(stringReader, bufferSize);
    }

    @After
    public void tearDown() throws Exception {
        simpleBuffer = null;
    }


    @Test
    public void testNextChar() throws Exception {
        int idx = 0;
        while (SimpleBuffer.EOF != simpleBuffer.peekChar() ) {
            int currChar = simpleBuffer.nextChar();
            assertEquals( bufferStringExample.charAt(idx), currChar);
            ++idx;
        }
        assertTrue(idx == bufferStringExample.length());
    }

    @Test
    public void testPeekChar() throws Exception {
        int idx = 0;
        while (SimpleBuffer.EOF != simpleBuffer.peekChar() ) {
            int currChar = simpleBuffer.peekChar();
            assertEquals( bufferStringExample.charAt(idx), currChar);
            ++idx;
            simpleBuffer.nextChar();
        }
        assertTrue(idx == bufferStringExample.length());
    }

}
