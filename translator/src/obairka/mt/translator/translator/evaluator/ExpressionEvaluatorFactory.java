package obairka.mt.translator.translator.evaluator;

import obairka.mt.translator.parser.nodes.expression.*;
import obairka.mt.translator.parser.nodes.expression.Expression;
import obairka.mt.translator.translator.evaluator.exprEvaluator.*;

/**
 * User: obairka@gmail.com
 * Date: 19.05.14
 * Time: 0:09
 */
class ExpressionEvaluatorFactory {
    public ExpressionEvaluator getInstance(Expression currentExpr) {
        if (currentExpr instanceof CallFunctionExpr) {
            return new CallFunctionExprEvaluator(((CallFunctionExpr) currentExpr)) ;
        }

        if (currentExpr instanceof ValExpr) {
            return new ValExprEvaluator(((ValExpr) currentExpr));
        }


        if (currentExpr instanceof Op) {
            // Op
            switch (((Op) currentExpr).getOpType()) {
                case SUB:
                    return new OpEvaluator("sub", (Op)currentExpr);
                case SUM:
                    return new OpEvaluator("add", (Op)currentExpr);
                case MUL:
                    return new OpEvaluator("mul", (Op)currentExpr);
                case DIV:
                    return new OpEvaluator("div", (Op)currentExpr);
                case POWER:
                    return new PowerOpEvaluator((Op) currentExpr) ;
            }
        }

        if (currentExpr instanceof CompareOp) {
            switch (((CompareOp) currentExpr).getCompareType()){
                case TRUE:  return new TrueFalseEvaluator(true);
                case FALSE: return new TrueFalseEvaluator(false);
            }
            return new CompareExprEvaluator((CompareOp)  currentExpr);
        }

        if (currentExpr instanceof ConvertType){
            return new ConvertTypeEvaluator((ConvertType)currentExpr);
        }

       return null;
    }

}
