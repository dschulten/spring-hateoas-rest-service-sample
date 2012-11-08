package org.springframework.hateoas.client;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

	private List<Action> script = new ArrayList<Action>();

	public void addAction(Action action) {
		script.add(action);
	}

	public List<Action> getScript() {
		return script;
	}


}
