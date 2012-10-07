package org.springframework.hateoas.client;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.util.Failure;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDocumentRelFinder implements RelFinder {

    private Document document;

    public XmlDocumentRelFinder(Document document) {
        super();
        this.document = document;
    }



    public Map<String, Link> findRels() {

        Map<String, Link> ret = new HashMap<String, Link>();
        NodeList elementsWithRel = getElementsWithRel(document);
        for (int i = 0; i < elementsWithRel.getLength(); i++) {
            Node element = elementsWithRel.item(i);
            String href = XmlHelper.getAttribute("href", element.getAttributes());
            String rel = XmlHelper.getAttribute("rel", element.getAttributes());
            Link link = new Link(href, rel);
            ret.put(rel, link);
        }
        return ret;

    }

    private NodeList getElementsWithRel(Document document) {
        try {
            return XmlHelper.nodesForXPath(document, "//*[@rel]");
        } catch (XPathExpressionException e) {
            throw Failure.asUnchecked(e);
        }
    }

}
