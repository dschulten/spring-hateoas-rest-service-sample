package de.escalon.rest.bt.dragon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThiefAccess {


	int counter = 0;

	String homeLocation = "Thief's Home";

	@Autowired
	DragonAccess dragonAccess;



	public ThiefAccess() {
		super();
	}

	public String getThiefLocation() {
		List<String> dragonHomes = dragonAccess.getHomeLocations();
		List<String> locations = new ArrayList<String>(dragonHomes);
		locations.add(homeLocation);
		String ret = locations.get(counter++);
		counter %= locations.size();
		return ret;
	}


}
