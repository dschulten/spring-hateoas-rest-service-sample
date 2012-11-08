package org.springframework.hateoas.client;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpHeaders;

public class JsonBrowsable implements Browsable {

	Map<String, Link> rels = new LinkedHashMap<String, Link>();
	private JsonNode json;

	public void process(InputStream content, HttpHeaders httpHeaders) {
		try {
			// parse json
			// json rel finder
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory jsonFactory = mapper.getJsonFactory();
			jsonFactory.createJsonParser("{ \"k1\" : \"v1\" }");
			json = mapper.readTree(content);
			rels.putAll(new LinkHeaderRelFinder(httpHeaders).findRels());
			rels.putAll(new JsonRelFinder(json).findRels());
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	public FormRequest getFormRequest(String formName, Map<String, List<? extends Object>> args) {
		return null;
	}

	public Link getRel(String rel) {
		return rels.get(rel);
	}

	public Object getParsedContent() {
		return json;
	}

	public String toString() {
		return json.toString();
	}

	public Map<String, Link> getRels() {
		return rels;
	}

	public List<String> getForms() {
		return Collections.emptyList();
	}

}
