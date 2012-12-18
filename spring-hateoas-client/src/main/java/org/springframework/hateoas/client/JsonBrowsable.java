package org.springframework.hateoas.client;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JsonBrowsable implements Browsable {

	MultiValueMap<String, Link> rels = new LinkedMultiValueMap<String, Link>();
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
		throw new UnsupportedOperationException("forms support not implemented for generic json");
	}

	public Link getRel(String rel) {
		List<Link> list = rels.get(rel);
		final Link ret;
		if (list == null) {
			ret = null;
		} else if (list.size() == 1) {
			ret = list.get(0);
		} else {
			throw new IllegalStateException("multiple rels found");
		}
		return ret;
	}

	public Object getParsedContent() {
		return json;
	}

	public String toString() {
		return json.toString();
	}

	public Map<String, List<Link>> getRels() {
		return rels;
	}
	

	public List<String> getForms() {
		return Collections.emptyList();
	}

}
