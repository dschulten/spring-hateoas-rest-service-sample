package de.escalon.rest.bt.dragon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DragonAccess {

	private Map<Integer, Dragon> dragons = new HashMap<Integer, Dragon>();
	private List<String> locations = new ArrayList<String>();

	private static int count = 0;

	public synchronized int createDragon(Dragon dragon) {
		count ++;
		dragons.put(count, dragon);
		locations.add(dragon.getHomeLocation());
		return count;
	}

	public Dragon readDragon(int id) {
		return dragons.get(id);
	}

	public List<String> getHomeLocations() {
		return locations;
	}

}
