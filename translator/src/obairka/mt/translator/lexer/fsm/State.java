package obairka.mt.translator.lexer.fsm;
import java.util.HashMap;
import java.util.Map;

public class State<T> { 
	private final String stateName;
	final Map<Integer, Transition> transitions;
	private final Executable<T> runCode;
	
	public State(String stateName, Executable<T> code) {
		this.stateName = stateName;
		this.runCode = code;
		this.transitions = new HashMap<Integer, Transition>();
	}
	public void addTransition(Transition trans) {
		transitions.put(trans.evtName, trans);
    }
	public T run() {
		if (null != runCode)
			return runCode.execute();
		return null;
	}
	@Override
	public String toString() {
		return stateName;
	}
}
