package obairka.mt.translator;

import obairka.mt.translator.parser.Parser;
import obairka.mt.translator.parser.RecursiveParser;
import obairka.mt.translator.parser.nodes.Program;
import obairka.mt.translator.translator.JavaByteCodeTranslator;
import obairka.mt.translator.translator.Translator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * User: obairka@gmail.com
 * Date: 13.05.14
 * Time: 18:45
 */
public class Starter {
    public static void main(String args[]) {

        if (args.length != 1) {
            System.err.println("Example:");
            System.err.println("compiler path_to_source_file");
            return;
        }

        try {
            FileReader reader = new FileReader(args[0]);

            Parser parser = new RecursiveParser(reader);
            Program program = parser.parseProgram();
            File file = new File(args[0]);
            if (!file.exists()) {
                System.err.println("There is no such file");
                return;
            }
            if (!file.canRead()) {
                System.err.println("Error: check access to read file");
                return;
            }
            FileWriter fileWriter = new FileWriter(file.getParent() + "/Program.j");
            Translator translator = new JavaByteCodeTranslator(fileWriter);
            translator.generate(program);
            fileWriter.close();
            reader.close();

        } catch (Exception e) {
            System.err.println("#### ERROR ####");
            System.err.println(e.getMessage());
        }

    }
}
