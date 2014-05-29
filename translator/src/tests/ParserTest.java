package tests;

import obairka.mt.translator.parser.Parser;
import obairka.mt.translator.parser.RecursiveParser;
import obairka.mt.translator.parser.nodes.*;
import obairka.mt.translator.parser.nodes.commands.*;
import obairka.mt.translator.parser.nodes.expression.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 15:13
 */
@RunWith(JUnit4.class)
public class ParserTest {
    @Test
    public void testIfCondition() {

        try {
            String programString =
                    "void main() {\n" +
                            "int a;\n" +
                            "double b;\n" +
                            "a = 1000;\n" +
                            "b = a / 100.0 ^ (5-2);\n" +
                            "if (a==b) {\n" +
                            " print(a);\n" +
                            "}\n" +
                            "else {\n" +
                            "int b;\n" +
                            "b=5;\n" +
                            " print(b);\n" +
                            "}\n" +
                            "return;}";
            Function mainFunction = new Function();
            mainFunction.setFunctionName("main");
            mainFunction.setReturnType(Type.VOID);

            Body body  = new Body();
            mainFunction.setBody(body);

            Variable varA = new Variable(Type.INT, "a");
            Variable varB = new Variable(Type.DOUBLE, "b");
            DeclareCommand declareCommandA = new DeclareCommand(varA);
            body.addCommand(declareCommandA);
            DeclareCommand declareCommandB = new DeclareCommand(varB);
            body.addCommand(declareCommandB);

            mainFunction.getContext().addVariable(varA);
            mainFunction.getContext().addVariable(varB);
            AssignCommand assignCommandA = new AssignCommand(varA, new ValExpr(Type.INT, "1000"));
            body.addCommand(assignCommandA);

            Op subOp = new Op(OpType.SUB) ;
            subOp.setLeftNode(new ValExpr(Type.INT, "5"));
            subOp.setRightNode(new ValExpr(Type.INT, "2"));


            Op powerOp = new Op(OpType.POWER);
            subOp.setDoubleType();
            powerOp.setLeftNode(new ValExpr(Type.DOUBLE, "100.0"));
            powerOp.setRightNode(subOp);


            Op divOp = new Op(OpType.DIV);
            divOp.setLeftNode(new ValExpr(Type.INT, "a"));
            divOp.setRightNode(powerOp);

            AssignCommand assignCommandB = new AssignCommand(varB, divOp);
            body.addCommand(assignCommandB);

            CompareOp equal = new CompareOp(CompareType.EQ);
            equal.setLeft(new ValExpr(Type.INT, "a"));
            equal.setRight(new ValExpr(Type.DOUBLE, "b"));

            IfCondition ifCondition = new IfCondition(mainFunction.getContext(), equal);
            Body ifBody = new Body();
            ifBody.addCommand(new PrintCommand(new ValExpr(varA.getType(), varA.getName())));
            ifCondition.setIfBody(ifBody);
            ifCondition.switchElseBlock();
            Body elseBody = new Body();
            Variable newB = new Variable(Type.INT, "b");

            DeclareCommand declareCommand = new DeclareCommand(newB);
            ifCondition.getElseContext().addVariable(newB);
            elseBody.addCommand(declareCommand);
            AssignCommand assignCommandNewB = new AssignCommand(newB, new ValExpr(Type.INT, "5"));
            elseBody.addCommand(assignCommandNewB);
            elseBody.addCommand(new PrintCommand(new ValExpr(newB.getType(), newB.getName())));
            ifCondition.setElseBody(elseBody);

            body.addCommand(ifCondition);
            body.addCommand(new ReturnCommand( ));
            // Local Size
            mainFunction.incrementLocal(Type.DOUBLE);
            mainFunction.incrementLocal(Type.INT);
            mainFunction.incrementLocal(Type.INT);

            Program program = new Program();
            program.addFunction(mainFunction);
            program.setMainFunction(mainFunction);

            Parser parser = new RecursiveParser(new StringReader(programString));

            Program parsedProgram = parser.parseProgram();

            assertEquals(program, parsedProgram);

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testFewFunctions() {
        try{
            String funcProgram =
                    "void function1(int a, double b) {\n" +
                            " print(a);\n" +
                            " print(b);" +
                            "}\n" +
                            "void main() {\n" +
                            " return;\n" +
                            "}\n" +
                            "int function2(int b) {\n" +
                            " return b;\n" +
                            "}\n";

            Function mainFunction = new Function();
            mainFunction.setFunctionName("main");
            mainFunction.setReturnType(Type.VOID);
            Body mainBody = new Body();
            ReturnCommand returnCommand = new ReturnCommand( );
            mainBody.addCommand(returnCommand);
            mainFunction.setBody(mainBody);

            Function function1 = new Function();
            function1.setReturnType(Type.VOID);
            function1.setFunctionName("function1");
            Variable a = new Variable(Type.INT, "a");
            Variable b = new Variable(Type.DOUBLE, "b");
            function1.addArgument(a);
            function1.addArgument(b);

            Body function1Body = new Body();
            function1Body.addCommand(new PrintCommand(new ValExpr(a.getType(), a.getName())));
            function1Body.addCommand(new PrintCommand(new ValExpr(b.getType(), b.getName())));

            function1.setBody(function1Body);

            Function function2 = new Function();
            function2.setFunctionName("function2");
            function2.setReturnType(Type.INT);

            Variable bool = new Variable(Type.INT, "b");
            function2.addArgument(bool);
            Body function2Body = new Body();
            ValExpr boolExpr = new ValExpr(bool.getType(), bool.getName());
            function2Body.addCommand(new ReturnCommand(boolExpr));
            function2.setBody(function2Body);

            Program program = new Program();

            program.addFunction(function1);
            program.setMainFunction(mainFunction);
            program.addFunction(mainFunction);
            program.addFunction(function2);

            Parser parser = new RecursiveParser(new StringReader(funcProgram));
            Program parsedProgram = parser.parseProgram();
            assertEquals(program, parsedProgram);

        }catch (Exception exc) {
            fail();
        }
    }

    @Test
    public void testConvertExpression() {
        try {
            String boolExprProgram = "void main() {\n" +
                    "int a;" +
                    "a = 3.3;\n" +
                    "}\n";

            ValExpr v3 = new ValExpr(Type.DOUBLE, "3.3");
            Variable variable = new Variable(Type.INT, "a");
            DeclareCommand declareCommand = new DeclareCommand(variable);

            ConvertType convertType = new ConvertType(v3, Type.INT);
            AssignCommand assignCommand = new AssignCommand(variable, convertType);
            Body body = new Body();
            body.addCommand(declareCommand);
            body.addCommand(assignCommand);


            Program program = new Program();

            Function function = new Function();
            function.getContext().addVariable(variable);
            function.setFunctionName("main");
            function.setReturnType(Type.VOID);
            function.setBody(body);

            function.incrementLocal(Type.INT);
            program.addFunction(function);
            program.setMainFunction(function);

            Parser parser = new RecursiveParser(new StringReader(boolExprProgram));

            Program parsedProgram = parser.parseProgram();

            assertEquals(program, parsedProgram);


        } catch (Exception e) {
            fail();
        }
    }

}
