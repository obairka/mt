package obairka.mt.translator.translator;

import obairka.mt.translator.parser.nodes.*;
import obairka.mt.translator.translator.evaluator.Evaluator;

import java.io.IOException;
import java.io.Writer;

/**
 * User: obairka@gmail.com
 * Date: 18.05.14
 * Time: 22:44
 */
public class JavaByteCodeTranslator  implements Translator {
    private static final String METACLASSINFO =
            ".source \tProgram.java\n"+
                    ".class \tProgram\n"+
                    ".super \tjava/lang/Object\n";
    private static final String INIT_FUNCTION =
            ".method \t<init>()V\n" +
                    "\t.limit stack 1\n" +
                    "\t.limit locals 1\n" +
                    "\taload_0\n" +
                    "\tinvokespecial java/lang/Object/<init>()V\n"+
                    "\treturn\n"+
                    ".end method\n";
    private static final String FUNCTION_PREFIX = "public static";
    private static final String MAIN_FUNCTION_TOP ="public static main([Ljava/lang/String;)V";
    private static final String METHOD = ".method";
    private static final String END_METHOD = ".end method";
    private static final String LIMIT_STACK = ".limit stack";
    private static final String LIMIT_LOCALS = ".limit locals";


    private final Writer writer;
    private final Evaluator evaluator;

    public JavaByteCodeTranslator(Writer writer) {
        this.writer = writer;
        evaluator = new Evaluator();
    }
    private void appendFunction(StringBuilder programBuilder,  Function function) {
        StringBuilder functionHeaderBuilder = new StringBuilder();
        StackSizeCounter stackSizeCounter = new StackSizeCounter();
        functionHeaderBuilder.append(METHOD);
        functionHeaderBuilder.append("\t\t\t\t");
        if (function.getFunctionName().equals("main")){
            functionHeaderBuilder.append(MAIN_FUNCTION_TOP + "\n");
        } else {
            char returnType = Evaluator.getTypeSymbol(function.getReturnType());
            functionHeaderBuilder.append(FUNCTION_PREFIX);
            functionHeaderBuilder.append(" ");
            functionHeaderBuilder.append(function.getFunctionName());
            functionHeaderBuilder.append("(");
            for (int i = 0; i < function.getArgumentsCount(); ++i) {
                Type type = function.getArgumentType(i);
                stackSizeCounter.increment(type);
                char argType = Evaluator.getTypeSymbol(type);
                functionHeaderBuilder.append(argType);
            }
            functionHeaderBuilder.append(")");
            functionHeaderBuilder.append(returnType);
            functionHeaderBuilder.append("\n");
        }
        StringBuilder bodyBuilder = new StringBuilder(); // :D
        evaluator.setCurrentStackSizeCounter(stackSizeCounter);
        evaluator.evaluateBody(bodyBuilder, function.getContext(), function.getBody());

        functionHeaderBuilder.append(LIMIT_STACK);                  functionHeaderBuilder.append("\t\t");
        functionHeaderBuilder.append(stackSizeCounter.getMaxSize());functionHeaderBuilder.append("\n");
        functionHeaderBuilder.append(LIMIT_LOCALS);                 functionHeaderBuilder.append("\t\t");
        functionHeaderBuilder.append(function.getLocalSize() + 1);      functionHeaderBuilder.append("\n");


        functionHeaderBuilder.append(bodyBuilder.toString());

        if (Type.VOID == function.getReturnType()) {
            functionHeaderBuilder.append("return\n");
        }

        functionHeaderBuilder.append(END_METHOD);
        functionHeaderBuilder.append("\n");
        functionHeaderBuilder.append("\n");

        programBuilder.append(functionHeaderBuilder.toString());
    }


    @Override
    public void generate(Program program) throws IOException {
        StringBuilder programBuilder = new StringBuilder();
        programBuilder.append(METACLASSINFO);
        programBuilder.append(INIT_FUNCTION);
        programBuilder.append("\n");

        for (Function f : program.getFunctions()){
            appendFunction(programBuilder, f);
        }

        writer.append(programBuilder.toString());
    }
}
