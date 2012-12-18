package org.springframework.hateoas.client;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.springframework.hateoas.Link;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JsonRelFinder implements RelFinder {

	private JsonNode json;

	public JsonRelFinder(JsonNode json) {
		super();
		this.json = json;
	}

	public MultiValueMap<String, Link> findRels() {
		MultiValueMap<String, Link> ret = new LinkedMultiValueMap<String, Link>();
		addRelsToResult(ret, json.findValues("links"));
		return ret;
	}

	private void addRelsToResult(MultiValueMap<String, Link> resultMap, List<JsonNode> jsonLinksList) {
		for (JsonNode jsonLinks : jsonLinksList) {
			if (jsonLinks != null && jsonLinks.isArray()) {
				for (JsonNode jsonLink : jsonLinks) {
					String rel = jsonLink.get("rel").getTextValue();
					Link link = new Link(jsonLink.get("href").getTextValue(), rel);
					resultMap.add(rel, link);
				}
			}
		}
	}

}
