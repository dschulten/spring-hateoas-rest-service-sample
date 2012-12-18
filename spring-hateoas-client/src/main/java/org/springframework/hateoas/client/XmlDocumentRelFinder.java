package org.springframework.hateoas.client;

import javax.xml.xpath.XPathExpressionException;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.util.Failure;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDocumentRelFinder implements RelFinder {

	private Document document;

	public XmlDocumentRelFinder(Document document) {
		super();
		this.document = document;
	}

	public MultiValueMap<String, Link> findRels() {

		MultiValueMap<String, Link> ret = new LinkedMultiValueMap<String, Link>();
		NodeList elementsWithRel = getElementsWithRelAttribute(document);
		for (int i = 0; i < elementsWithRel.getLength(); i++) {
			Node element = elementsWithRel.item(i);
			String href = XmlHelper.getAttribute("href", element.getAttributes());
			String rel = XmlHelper.getAttribute("rel", element.getAttributes());
			Link link = new Link(href, rel);
			ret.add(rel, link);
		}
		return ret;

	}

	private NodeList getElementsWithRelAttribute(Document document) {
		try {
			return XmlHelper.nodesForXPath(document, "//*[@rel]");
		} catch (XPathExpressionException e) {
			throw Failure.asUnchecked(e);
		}
	}

}
