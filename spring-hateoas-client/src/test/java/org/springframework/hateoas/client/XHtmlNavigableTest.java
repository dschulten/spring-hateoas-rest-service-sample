package org.springframework.hateoas.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.w3c.dom.Document;

public class XHtmlNavigableTest {

    XHtmlNavigable navigable;
    InputStream formInputStream;

    String formName = "search";
    String backRel = "back";

    String form = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n" + // formatter
            "<!DOCTYPE html>\r\n" + //
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + //
            "  <head>\r\n" + //
            "    <title>Simple Form</title>\r\n" + //
            "    <link rel=\"help\" title=\"Hilfe\" href=\"../../editorial/index.htm\" />" +//
            "  </head>\r\n" + //
            "  <body>\r\n" + //
            "    <form action=\"/search\" name=\"" + formName + "\">\r\n" + //
            "      <h1>Enter Person Id:</h1>\r\n" + //
            "      <input type=\"number\" name=\"customerId\" />\r\n" + //
            "      <input type=\"submit\" value=\"Find\" />\r\n" + //
            "    </form>\r\n" + //
            "    <a href=\"/\" rel=\"" + backRel + "\">Back</a>\r\n" + //
            "  </body>\r\n" + //
            "</html>";

    private Document document;

    @Before
    public void setUp() throws Exception {
        navigable = new XHtmlNavigable();
        formInputStream = new ByteArrayInputStream(form.getBytes());

        InputStream in = new ByteArrayInputStream(form.getBytes());
        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(in);
        } finally {
            in.close();
        }
    }

    @Test
    public void testRelsFromDocument() {
        Map<String, Link> relsFromDocument = navigable
                .relsFromDocument(document);
        assertTrue(relsFromDocument.containsKey(backRel));
        Link link = relsFromDocument.get(backRel);
        assertEquals("/", link.getHref());
        assertEquals(backRel, link.getRel());
    }

    @Test
    public void testRelsFromLinkHeaderSimpleLink() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String headerValue = "<http://example.com/TheBook/chapter2>; "
                + "rel=\"previous\"; " + "title=\"previous chapter\"";
        headers.set("Link", headerValue);

        Map<String, Link> relsFromLinkHeader = navigable
                .relsFromLinkHeader(headers);

        assertTrue("rel previous not found",
                relsFromLinkHeader.containsKey("previous"));
        Link link = relsFromLinkHeader.get("previous");
        assertEquals("http://example.com/TheBook/chapter2", link.getHref());
        assertEquals("previous", link.getRel());
    }

    @Test
    public void testRelsFromLinkHeaderExtensionRel() throws Exception {

        String extensionRel = "http://example.net/foo";
        HttpHeaders headers = new HttpHeaders();
        String extensionRelLink = "</>; rel=\"" + extensionRel + "\"";
        headers.set("Link", extensionRelLink);

        Map<String, Link> relsFromLinkHeader = navigable
                .relsFromLinkHeader(headers);

        assertTrue("rel " + extensionRel + " not found",
                relsFromLinkHeader.containsKey(extensionRel));
        Link link = relsFromLinkHeader.get(extensionRel);
        assertEquals("/", link.getHref());
        assertEquals(extensionRel, link.getRel());

    }

    @Test
    public void testRelsFromLinkHeaderMultiLink() throws Exception {

        String previousRel = "previous";
        String nextRel = "next";
        HttpHeaders headers = new HttpHeaders();
        String multiLink = "</TheBook/chapter2>;"
                + "rel=\"previous\"; title*=UTF-8'de'letztes%20Kapitel,"
                + "</TheBook/chapter4>;"
                + "rel=\"next\"; title*=UTF-8'de'n%c3%a4chstes%20Kapitel";
        headers.set("Link", multiLink);

        Map<String, Link> relsFromLinkHeader = navigable
                .relsFromLinkHeader(headers);

        assertTrue("rel " + previousRel + " not found",
                relsFromLinkHeader.containsKey(previousRel));
        Link linkPrevious = relsFromLinkHeader.get(previousRel);
        assertEquals("/TheBook/chapter2", linkPrevious.getHref());
        assertEquals(previousRel, linkPrevious.getRel());

        assertTrue("rel " + nextRel + " not found",
                relsFromLinkHeader.containsKey(nextRel));
        Link linkNext = relsFromLinkHeader.get(nextRel);
        assertEquals("/TheBook/chapter4", linkNext.getHref());
        assertEquals(nextRel, linkNext.getRel());

    }

    @Test
    public void testProcessFormWithoutMethodAsGet() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", "</search>; rel=\"self\"");
        navigable.process(formInputStream, headers);

        // the result of process() are rels:
        assertEquals("/search", navigable.getRel("self").getHref());
        assertEquals("/", navigable.getRel(backRel).getHref());
        assertEquals("../../editorial/index.htm", navigable.getRel("help").getHref());

        // and the ability to create formRequests:
        Map<String, List<? extends Object>> args = new HashMap<String, List<? extends Object>>();
        args.put("customerId", Arrays.asList(1));
        FormRequest formRequest = navigable.getFormRequest(formName, args);
        assertEquals(HttpMethod.GET, formRequest.getMethod());
        assertEquals("customerId=1", formRequest.getRequestBody());
        assertEquals("/search", formRequest.getURI().toString());
        assertEquals("UTF-8", formRequest.getEncoding());
        assertEquals("application/x-www-form-urlencoded", formRequest.getContentType());
    }

}
