package obairka.mt.translator.parser;
import obairka.mt.translator.parser.exceptions.ParseException;
import obairka.mt.translator.parser.nodes.*;
import obairka.mt.translator.parser.nodes.commands.*;
import obairka.mt.translator.parser.nodes.expression.*;

import java.util.ArrayList;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 18:47
 */
public interface Parser {
    Program parseProgram()          throws ParseException;
}
