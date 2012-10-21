package org.springframework.hateoas.client;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.springframework.hateoas.Link;

public class JsonRelFinder implements RelFinder {

	private JsonNode json;

	public JsonRelFinder(JsonNode json) {
		super();
		this.json = json;
	}

	public Map<String, Link> findRels() {
		Map<String, Link> ret = new LinkedHashMap<String, Link>();

		JsonNode jsonLinks = json.findValue("links");
		if (jsonLinks != null && jsonLinks.isArray()) {
			for (JsonNode jsonLink : jsonLinks) {
				String rel = jsonLink.get("rel").getTextValue();
				Link link = new Link(jsonLink.get("href").getTextValue(), rel);
				ret.put(rel, link);
			}
		}
		return ret;
	}

}
