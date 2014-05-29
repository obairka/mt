package obairka.mt.translator.lexer.fsm;

import java.util.HashMap;
import java.util.Map;

/*
 * Flying Spaghetti Monster
 */
public class FSM<T> {	
	public void reset() {
		currentState = initial;
	}
	
	public FSM() {
		 this.states = new HashMap<String, State<T>>();
		 this.currentState = null;
	}

    public void addState(State<T> state) {
		boolean isInitial = (states.size() == 0);
		if (!states.containsKey(state.toString())) {
			states.put(state.toString(), state);		
		}
		if (isInitial) {
			initial = state.toString();
			setState(state.toString());
		}
	}

    void setState(String state){
		currentState = state;		
	}
	
	public T addEvent(int evtName) {
		State<T> state = states.get(currentState);
		T res = state.run();	
		if (res!=null) 
			return res;
		for (Map.Entry<Integer, Transition> entry : state.transitions.entrySet())
		{
			int ev = entry.getKey();
			if (ev == (ev&evtName)){
				Transition trans = state.transitions.get(ev);
				setState(trans.endState);			
				break;
			}
		}

		return res;
	}
	
	private String currentState;
	private String initial;
	private final Map<String, State<T>> states;
}
