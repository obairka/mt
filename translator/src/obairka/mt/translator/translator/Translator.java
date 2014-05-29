package obairka.mt.translator.translator;

import obairka.mt.translator.parser.nodes.Program;

import java.io.IOException;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 18:48
 */
public interface Translator {
    public void generate(Program program) throws IOException;
}
