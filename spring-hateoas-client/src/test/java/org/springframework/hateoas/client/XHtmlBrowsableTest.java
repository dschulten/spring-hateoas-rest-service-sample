package org.springframework.hateoas.client;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class XHtmlBrowsableTest {

	Browsable navigable;
	InputStream formInputStream;

	public static String FORM_NAME = "search";
	public static String BACK_REL = "back";

	public static String FORM = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n" + // formatter
			"<!DOCTYPE html>\r\n" + //
			"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + //
			"  <head>\r\n" + //
			"    <title>Simple Form</title>\r\n" + //
			"    <link rel=\"help\" title=\"Hilfe\" href=\"../../editorial/index.htm\" />" + //
			"  </head>\r\n" + //
			"  <body>\r\n" + //
			"    <form action=\"/search\" name=\"" + FORM_NAME + "\">\r\n" + //
			"      <h1>Enter Person Id:</h1>\r\n" + //
			"      <input type=\"number\" name=\"customerId\" />\r\n" + //
			"      <input type=\"submit\" value=\"Find\" />\r\n" + //
			"    </form>\r\n" + //
			"    <a href=\"/\" rel=\"" + BACK_REL + "\">Back</a>\r\n" + //
			"  </body>\r\n" + //
			"</html>";

	@Before
	public void setUp() throws Exception {
		navigable = new XHtmlBrowsable();
		formInputStream = new ByteArrayInputStream(FORM.getBytes());
	}

	@Test
	public void testProcessFormWithoutMethodAsGet() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Link", "</search>; rel=\"self\"");
		navigable.process(formInputStream, headers);

		// the result of process() are rels:
		assertEquals("/search", navigable.getRel("self").getHref());
		assertEquals("/", navigable.getRel(BACK_REL).getHref());
		assertEquals("../../editorial/index.htm", navigable.getRel("help").getHref());

		// and the ability to create formRequests:
		Map<String, List<? extends Object>> args = new HashMap<String, List<? extends Object>>();
		args.put("customerId", Arrays.asList(1));
		FormRequest formRequest = navigable.getFormRequest(FORM_NAME, args);
		assertEquals(HttpMethod.GET, formRequest.getMethod());
		assertEquals("customerId=1", formRequest.getRequestBody());
		assertEquals("/search", formRequest.getURI().toString());
		assertEquals("UTF-8", formRequest.getEncoding());
		assertEquals("application/x-www-form-urlencoded", formRequest.getContentType());
	}

}
