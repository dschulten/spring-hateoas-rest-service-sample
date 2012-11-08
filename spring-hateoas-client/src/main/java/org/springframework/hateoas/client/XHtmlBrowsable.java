package org.springframework.hateoas.client;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class XHtmlBrowsable implements Browsable {

	private Document document;
	private Map<String, Link> rels = new HashMap<String, Link>();

	public void process(InputStream content, HttpHeaders headers) {
		try {
			Assert.notNull(headers);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			this.document = documentBuilder.parse(content);

			rels.putAll(new LinkHeaderRelFinder(headers).findRels());
			rels.putAll(new XmlDocumentRelFinder(document).findRels());
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	@Override
	public String toString() {
		DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
		LSSerializer serializer = domImplLS.createLSSerializer();
		String str = serializer.writeToString(document);
		return str;
	}

	public Link getRel(String rel) {
		return rels.get(rel);

	}

	public FormRequest getFormRequest(String formName, Map<String, List<? extends Object>> args) {
		try {
			Node form = getForm(formName);
			NamedNodeMap attributes = form.getAttributes();
			String actionString = XmlHelper.getAttribute("action", attributes);
			String methodString = XmlHelper.getAttribute("method", attributes);
			if (methodString == null)
				methodString = "get";

			HttpMethod httpMethod = HttpMethod.valueOf(methodString.toUpperCase());
			URI uri = new URI(actionString);
			String encoding = "UTF-8";
			String contentType = "application/x-www-form-urlencoded";
			MultiValueMap<String, String> requestValues = buildRequestValues(form, args, encoding);
			return new FormRequest(httpMethod, requestValues, uri, contentType, encoding);
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	private Node getForm(String formName) throws XPathExpressionException {
		String xPathString = "//form[@name='" + formName + "']";
		Node form = XmlHelper.nodeForXPath(document, xPathString);
		return form;
	}

	public List<String> getForms() {
		try {
			String xPathString = "//form";
			NodeList forms = XmlHelper.nodesForXPath(document, xPathString);
			List<String> formNames = new ArrayList<String>();
			for (int i = 0; i < forms.getLength(); i++) {
				Node form = forms.item(i);
				NamedNodeMap attributes = form.getAttributes();
				String name = XmlHelper.getAttribute("name", attributes);
				formNames.add(name);
			}
			return formNames;
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	private MultiValueMap<String, String> buildRequestValues(Node form, Map<String, List<? extends Object>> args,
			String encoding) throws XPathExpressionException, UnsupportedEncodingException {

		MultiValueMap<String, String> requestValues = new LinkedMultiValueMap<String, String>();

		Map<String, List<? extends Object>> arguments = marshalArguments(form, args);
		for (Entry<String, List<? extends Object>> entry : arguments.entrySet()) {
			String key = entry.getKey();
			List<? extends Object> values = entry.getValue();
			for (Object value : values) {
				requestValues.add(key, value.toString());
			}
		}
		return requestValues;
	}

	private Map<String, List<? extends Object>> marshalArguments(Node form, Map<String, List<? extends Object>> args)
			throws XPathExpressionException {
		// List<NameValuePair> keyvals = new ArrayList<NameValuePair>();
		Map<String, List<?>> ret = new HashMap<String, List<? extends Object>>();
		ret.putAll(marshalInputArguments(form, args));
		// TODO selectArguments!
		// marshalSelectArguments(form, args, keyvals);
		return ret;
	}

	private Map<String, List<?>> marshalInputArguments(Node form, Map<String, List<? extends Object>> args)
			throws XPathExpressionException {

		Map<String, List<?>> ret = new HashMap<String, List<?>>();

		String xPathString = "//input[@name]";
		NodeList nodeList = XmlHelper.nodesForXPath(form, xPathString);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node input = nodeList.item(i);
			NamedNodeMap inputAttributes = input.getAttributes();
			String inputName = XmlHelper.getAttribute("name", inputAttributes);
			Assert.notNull(inputName);
			if (args.containsKey(inputName)) {
				ret.put(inputName, args.get(inputName));
			} else {
				List<? extends Object> currValue = Arrays.asList(XmlHelper.getAttribute("value", inputAttributes));
				if (currValue != null) {
					ret.put(inputName, currValue);
				}
			}
		}
		return ret;
	}

	public Object getParsedContent() {
		return document;
	}

	public Map<String, Link> getRels() {
		return rels;
	}

	// private void marshalSelectArguments(Element form, Map<String, String>
	// args,
	// List<NameValuePair> keyvals) throws JDOMException {
	// XPath xpath = XhtmlParser.getXPath("xhtml", "//xhtml:select");
	// for (Object o : xpath.selectNodes(form)) {
	// Element select = (Element) o;
	// String selectName = select.getAttributeValue("name");
	// if (selectName != null && args.containsKey(selectName)) {
	// addAvailableOption(select, args.get(selectName), keyvals);
	// } else if (selectName != null) {
	// addDefaultOption(select, keyvals);
	// }
	// }
	// }
	//
	// private void addAvailableOption(Element select, String chosenValue,
	// List<NameValuePair> keyvals) throws JDOMException {
	// String selectName = select.getAttributeValue("name");
	// String expr = String.format("//xhtml:option[@value='%s']", chosenValue);
	// XPath optionPath = XhtmlParser.getXPath("xhtml", expr);
	// if (optionPath.selectSingleNode(select) == null) {
	// String msg = String
	// .format("value '%s' was not one of the available options for select '%s'",
	// chosenValue, selectName);
	// throw new IllegalArgumentException(msg);
	// }
	// keyvals.add(new BasicNameValuePair(selectName, chosenValue));
	// }
	//
	// private void addDefaultOption(Element select, List<NameValuePair>
	// keyvals)
	// throws JDOMException {
	// String selectName = select.getAttributeValue("name");
	// XPath defaultPath = XhtmlParser.getXPath("xhtml",
	// "//xhtml:option[@selected]");
	// Element defaultOption = (Element) defaultPath.selectSingleNode(select);
	// if (defaultOption != null) {
	// keyvals.add(new BasicNameValuePair(selectName, defaultOption
	// .getAttributeValue("value", "")));
	// }
	// }
}
