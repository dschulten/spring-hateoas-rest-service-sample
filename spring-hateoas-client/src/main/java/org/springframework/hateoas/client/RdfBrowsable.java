package org.springframework.hateoas.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RdfBrowsable implements Browsable {

	private String lang = "RDF/XML-ABBREV";

	private Model model;

	public void process(InputStream content, HttpHeaders headers) {
		try {
			Assert.notNull(headers);
			model = ModelFactory.createDefaultModel();
			model.read(content, lang);
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	@Override
	public String toString() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		model.write(out, lang);
		return new String(out.toByteArray(), Charset.forName("UTF-8"));
	}

	public Object getParsedContent() {
		return model;
	}

	public FormRequest getFormRequest(String formName, Map<String, List<? extends Object>> args) {
		return null;
	}

	public Link getRel(String rel) {
		return null;
	}

	public Map<String, List<Link>> getRels() {
		return Collections.emptyMap();
	}

	public List<String> getForms() {
		return Collections.emptyList();
	}

}
