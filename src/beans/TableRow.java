package beans;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TableRow {
    private String expression;

    public TableRow(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            return String.valueOf(engine.eval(expression));
        } catch (ScriptException e) {
            return "NULL";
        }
    }
}
