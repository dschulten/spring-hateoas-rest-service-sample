package org.springframework.hateoas.client;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;

public interface Browsable {

	// In theory, a response can have links in the headers
  // in that case it becomes a browsable. 
	void process(InputStream content, HttpHeaders httpHeaders);
	
	/**
	 * Returns the content as a content-specific java bean, usually in a parsed form, e.g. an XML DOM or a JsonNode.
	 * 
	 * @return
	 */
	Object getParsedContent();
	
	/**
	 * Returns the content as String. For single-part textual content types, this should be the response body text.
	 * 
	 * @return
	 */
	String toString();
	
	FormRequest getFormRequest(String formName, Map<String, List<? extends Object>> args);

	
	/**
	 * Gets Link for rel, if the document contains exactly one rel.
	 * 
	 * @param rel identifying the link
	 * @return
	 * @throws IllegalStateException if there is more than one matching rel in the document.
	 */
	Link getRel(String rel);

	/**
	 * 
	 * @return
	 */
	Map<String, List<Link>> getRels();

	List<String> getForms();

}
