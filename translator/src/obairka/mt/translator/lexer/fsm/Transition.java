package obairka.mt.translator.lexer.fsm;

public class Transition {
	final int evtName;
	private final String startState;
    final String endState;

    public Transition(int evtName, String startState, String endState) {
	    this.evtName = evtName;
	    this.startState = startState;
	    this.endState = endState;
    }

    public String getStartState() {
        return startState;
    }
}
