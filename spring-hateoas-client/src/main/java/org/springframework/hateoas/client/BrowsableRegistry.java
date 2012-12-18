package org.springframework.hateoas.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.util.Failure;

public class BrowsableRegistry {

	Map<String, Class<? extends Browsable>> browsables = new HashMap<String, Class<? extends Browsable>>();

	public BrowsableRegistry() {
		super();
		this.browsables.put("text/html", XHtmlBrowsable.class);
		this.browsables.put("application/json", JsonBrowsable.class);
		this.browsables.put("application/rdf+xml", RdfBrowsable.class);
	}

	public Browsable getBrowsable(String mimeType) {
		try {
			Class<? extends Browsable> browsableClass = browsables.get(mimeType);

			if (browsableClass == null)
				throw new IllegalStateException("no Browsable class defined for " + mimeType);

			return browsableClass.newInstance();
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}

	}

	void addNavigator(String mimeType, Class<? extends Browsable> navigatorClass) {
		browsables.put(mimeType, navigatorClass);
	}
}
