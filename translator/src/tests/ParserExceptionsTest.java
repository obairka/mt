package tests;

import obairka.mt.translator.parser.Parser;
import obairka.mt.translator.parser.RecursiveParser;
import obairka.mt.translator.parser.exceptions.ParseException;
import obairka.mt.translator.parser.nodes.Program;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;

import static junit.framework.Assert.fail;

/**
 * User: obairka@gmail.com
 * Date: 26.05.14
 * Time: 15:26
 */
@RunWith(JUnit4.class)
public class ParserExceptionsTest {
    private void parse(String string) throws ParseException  {
        Parser parser = new RecursiveParser(new StringReader(string));
        parser.parseProgram();
    }

    @Test(expected= ParseException.class)
    public void badMainFunctionSignature1() throws ParseException{
        String programStr = "int main() {}\n";
        parse(programStr);
    }

    @Test(expected= ParseException.class)
    public void badMainFunctionSignature2() throws ParseException{
        String programStr = "void main(int a) {}\n";

        parse(programStr);
    }

    @Test(expected= ParseException.class)
    public void badAssignCommand1() throws ParseException{
        String programStr = "void main(int a; a = ;) {}\n";

        parse(programStr);
    }

    @Test(expected= ParseException.class)
    public void badAssignCommand2() throws ParseException{
        String programStr = "void main(int a; a = das;) {}\n";

        parse(programStr);
    }


    @Test(expected= ParseException.class)
    public void badAssignCommand3() throws ParseException{
        String programStr = "void f() {} void main(int a; a = f();) {}\n";

        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badReturnCommand() throws ParseException{
        String programStr = "void main()  { return 100;}\n";

        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badExpression() throws ParseException{

        String programStr = "void f(int a)  { a = 2 +  2* ;}\n";

        parse(programStr);

    }
    @Test(expected = ParseException.class)
    public void badCommand() throws ParseException{
        String programStr = "void f(int a)  { a = 2 + 2 return }\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badCondition() throws ParseException{
        String programStr = "void f (int a)  { if (a > ) { }}\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badCondition2() throws ParseException{
        String programStr = "void f (int a)  { if (2.3 < ==328791+) { }}\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badCondition3() throws ParseException{
        String programStr = "void f (int a)  { if (2.3 < a) { }; }\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badCondition4() throws ParseException{
        String programStr = "void f (int a)  { if (2.3 < a) { } else  print(a); }\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badDeclaration() throws ParseException{
        String programStr = "void f (int a)  { int int;}\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badDeclaration2() throws ParseException{
        String programStr = "void f (int a)  { int c = a;}\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badFunctionDef() throws ParseException{
        String programStr = "void print (int a)  { }\n";
        parse(programStr);
    }
    @Test(expected = ParseException.class)
    public void badFunctionCall() throws ParseException{
        String programStr = "void f (int a)  {} void g() { f();}\n";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void badFunctionCall2() throws ParseException{
        String programStr = "void f (int a)  {} void g() { f(2.3, 5.6);}\n";
        parse(programStr);
    }


    @Test(expected = ParseException.class)
    public void badCompareExpressionUse() throws ParseException{
        try {
        String programStr = "void f (int a) { a = 3 < 5;}";
        parse(programStr);
        }catch (ParseException exc){
            //exc.printStackTrace();
            throw exc;
        }
    }
    @Test(expected = ParseException.class)
    public void badCompareExpressionUse2() throws ParseException{
        try {
            String programStr = "void f (int a) { print(a<2);}";
            parse(programStr);
        }catch (ParseException exc){
            //exc.printStackTrace();
            throw exc;
        }
    }
    @Test(expected = ParseException.class)
    public void badCompareExpressionUse3() throws ParseException{
        try {
            String programStr = "void f (int a) { } void main() { f(a < 3);}";
            parse(programStr);
        }catch (ParseException exc){
           // exc.printStackTrace();
            throw exc;
        }
    }

    @Test(expected = ParseException.class)
    public void badIfCommand() throws ParseException{
        try {
            String programStr = "void f (int a) { if (){} }";
            parse(programStr);
        }   catch (ParseException ex) {
            //ex.printStackTrace();
            throw ex;
        }
    }

    @Test(expected = ParseException.class)
    public void badWhileCommand() throws ParseException{
        String programStr = "void f (int a) { while (){} }";
        parse(programStr);
    }

    @Test(expected = ParseException.class)
    public void noReturn() throws ParseException{
        try {
            String programStr = "int f (int a) {  }";
            parse(programStr);
        }
        catch (ParseException exc){
            //exc.printStackTrace();
            if (!exc.isThisCode(ParseException.RETURN_EXPECTED)) {
                 fail("Expected error " + ParseException.RETURN_EXPECTED+ "; Actual " + exc.getCode() + ";");
            }
            throw exc;
        }
    }
}
