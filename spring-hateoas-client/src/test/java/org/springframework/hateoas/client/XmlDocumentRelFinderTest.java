package org.springframework.hateoas.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import static org.springframework.hateoas.client.XHtmlBrowsableTest.BACK_REL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.w3c.dom.Document;

public class XmlDocumentRelFinderTest {

    private Document document;

    @Before
    public void setUp() throws Exception {

        InputStream in = new ByteArrayInputStream(
                XHtmlBrowsableTest.FORM.getBytes());
        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(in);
        } finally {
            in.close();
        }
    }

    @Test
    public void testFindRels() throws Exception {
        XmlDocumentRelFinder xmlDocumentRelFinder = new XmlDocumentRelFinder(
                document);
        Map<String, Link> relsFromDocument = xmlDocumentRelFinder.findRels();
        assertTrue(relsFromDocument.containsKey(BACK_REL));
        Link link = relsFromDocument.get(BACK_REL);
        assertEquals("/", link.getHref());
        assertEquals(BACK_REL, link.getRel());
    }

}
