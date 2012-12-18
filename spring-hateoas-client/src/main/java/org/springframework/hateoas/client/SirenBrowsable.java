package org.springframework.hateoas.client;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;

public class SirenBrowsable implements Browsable {

	public void process(InputStream content, HttpHeaders httpHeaders) {
		// TODO Auto-generated method stub

	}

	public FormRequest getFormRequest(String formName, Map<String, List<? extends Object>> args) {
		// TODO Auto-generated method stub
		return null;
	}

	public Link getRel(String rel) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getParsedContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, List<Link>> getRels() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

}
