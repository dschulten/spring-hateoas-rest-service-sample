package org.springframework.hateoas.client;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XHtmlNavigable implements BrowsableResource {

    private static final String HEADER_LINK = "Link:";
    private Document document;
    private Map<String, Link> rels = new HashMap<String, Link>();
    private static Log LOG = LogFactory.getLog(XHtmlNavigable.class);

    // private

    public void process(InputStream content, HttpHeaders headers) {
        try {
            Assert.notNull(headers);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            this.document = documentBuilder.parse(content);
            rels.putAll(relsFromLinkHeader(headers));
            rels.putAll(relsFromDocument(document));
        } catch (Exception e) {
            throw Failure.asUnchecked(e);
        }
    }

    Map<String, Link> relsFromDocument(Document document) {
        Map<String, Link> ret = new HashMap<String, Link>();
        NodeList elementsWithRel = getElementsWithRel(document);
        for (int i = 0; i < elementsWithRel.getLength(); i++) {
            Node element = elementsWithRel.item(i);
            String href = getAttribute("href", element.getAttributes());
            String rel = getAttribute("rel", element.getAttributes());
            Link link = new Link(href, rel);
            ret.put(rel, link);
        }
        return ret;
    }

    Map<String, Link> relsFromLinkHeader(HttpHeaders headers) {

        Map<String, Link> ret = new HashMap<String, Link>();

        List<String> linkHeaders = headers.get("Link");
        String uriReference = null;
        boolean trimTokens = true;
        boolean ignoreEmptyTokens = true;
        for (String linkHeader : linkHeaders) {
            Map<String, String> linkParams = new HashMap<String, String>();
            if (linkHeader.startsWith(HEADER_LINK)) {
                linkHeader = linkHeader.substring(HEADER_LINK.length()).trim();
            }

            String[] linkValues = linkHeader.split(",");
            for (String linkValue : linkValues) {
                String[] linkParts = StringUtils.tokenizeToStringArray(
                        linkValue, "<>;", trimTokens, ignoreEmptyTokens);

                // String[] linkParts = linkValue.split(";");
                uriReference = linkParts[0];

                // build link params
                for (int i = 1; i < linkParts.length; i++) {
                    String[] tokens = StringUtils.tokenizeToStringArray(
                            linkParts[i], "=\"", trimTokens, ignoreEmptyTokens);
                    linkParams.put(tokens[0], tokens[1]);
                }
                String rel = linkParams.get("rel");
                Link link = new Link(uriReference, rel);
                Link replaced = ret.put(rel, link);
                if (replaced != null) {
                    LOG.warn("found duplicate rel " + rel + ", replacing "
                            + replaced + " by " + link);
                }
            }
        }
        if (uriReference == null)
            throw new IllegalArgumentException(
                    "Link header found without URI-Reference");
        return ret;
    }

    public Link getRel(String rel) {
        return rels.get(rel);

    }

    public FormRequest getFormRequest(String formName,
            Map<String, List<? extends Object>> args) {
        try {
            Node form = getForm(formName);
            NamedNodeMap attributes = form.getAttributes();
            String actionString = getAttribute("action", attributes);
            String methodString = getAttribute("method", attributes);
            if (methodString == null)
                methodString = "get";

            HttpMethod httpMethod = HttpMethod.valueOf(methodString
                    .toUpperCase());
            URI uri = new URI(actionString);
            String encoding = "UTF-8";
            String contentType = "application/x-www-form-urlencoded";
            String requestBody = buildRequestBody(form, args, encoding);
            return new FormRequest(httpMethod, requestBody, uri, contentType,
                    encoding);
        } catch (Exception e) {
            throw Failure.asUnchecked(e);
        }
    }

    private NodeList getElementsWithRel(Document document) {
        try {
            return nodesForXPath(document, "//*[@rel]");
        } catch (XPathExpressionException e) {
            throw Failure.asUnchecked(e);
        }
    }

    private Node getForm(String formName) throws XPathExpressionException {
        String xPathString = "//form[@name='" + formName + "']";
        Node form = nodeForXPath(document, xPathString);
        return form;
    }

    private Node nodeForXPath(Node node, String xPathString)
            throws XPathExpressionException {
        QName returnType = XPathConstants.NODE;
        return (Node) getXPath(node, xPathString, returnType);
    }

    private NodeList nodesForXPath(Node node, String xPathString)
            throws XPathExpressionException {
        QName returnType = XPathConstants.NODESET;
        return (NodeList) getXPath(node, xPathString, returnType);
    }

    private Object getXPath(Node node, String xPathString, QName returnType)
            throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expression = xPath.compile(xPathString);
        return expression.evaluate(node, returnType);
    }

    private String getAttribute(String name, NamedNodeMap attributes) {
        Assert.notNull(attributes);
        Node attributeNode = attributes.getNamedItem(name);
        final String ret;
        if (attributeNode == null) {
            ret = null;
        } else {
            ret = attributeNode.getNodeValue();
        }
        return ret;
    }

    private String buildRequestBody(Node form,
            Map<String, List<? extends Object>> args, String encoding)
            throws XPathExpressionException, UnsupportedEncodingException {

        Map<String, List<? extends Object>> arguments = marshalArguments(form,
                args);
        StringBuilder sb = new StringBuilder();
        for (Entry<String, List<? extends Object>> entry : arguments.entrySet()) {
            String key = entry.getKey();
            List<? extends Object> values = entry.getValue();
            for (Object value : values) {
                if (sb.length() > 0)
                    sb.append("&");
                sb.append(key).append("=");
                sb.append(URLEncoder.encode(value.toString(), encoding));
            }
        }
        return sb.toString();
    }

    // /**
    // * Constructs a GET or POST request used to submit a form. The HTTP method
    // * used is taken from the form's @method attribute.
    // *
    // * @param form
    // * The &lt;form&gt; element from the current application state
    // * which is the form that should be submitted.
    // * @param context
    // * The URL used to retrieve the current application state (parsed
    // * response body). This is used if the form has a relative URL as
    // * its @action.
    // * @param args
    // * A map of input names to values. For each &lt;input&gt; or
    // * &lt;select&gt; element in the form, if its @name is a key in
    // * {@code args} then the value in {@code args} is used as the
    // * value in the form submission. If an input's @name is not found
    // * in {@code args} then it will be submitted with whatever
    // * default value is present.
    // * @return A request that can be issued to submit the form.
    // * @throws JDOMException
    // * @throws ParseException
    // * @throws IOException
    // */
    // public HttpUriRequest submitForm(Element form, URL context,
    // Map<String, String> args) throws JDOMException, ParseException,
    // IOException {
    // String formMethod = form.getAttributeValue("method");
    // String formAction = form.getAttributeValue("action");
    // URL derived = new URL(context, formAction);
    //
    // UrlEncodedFormEntity e = marshalArguments(form, args);
    //
    // if ("GET".equalsIgnoreCase(formMethod)) {
    // String reqUri = null;
    // if (e != null) {
    // String queryParams = EntityUtils.toString(e);
    // reqUri = String
    // .format("%s?%s", derived.toString(), queryParams);
    // } else {
    // reqUri = derived.toString();
    // }
    // return new HttpGet(reqUri);
    // } else if ("POST".equalsIgnoreCase(formMethod)) {
    // HttpPost post = new HttpPost(derived.toString());
    // post.setEntity(e);
    // return post;
    // }
    // throw new IllegalArgumentException("form method must be GET or POST");
    // }

    private Map<String, List<? extends Object>> marshalArguments(Node form,
            Map<String, List<? extends Object>> args)
            throws XPathExpressionException {
        // List<NameValuePair> keyvals = new ArrayList<NameValuePair>();
        Map<String, List<?>> ret = new HashMap<String, List<? extends Object>>();
        ret.putAll(marshalInputArguments(form, args));
        // TODO selectArguments!
        // marshalSelectArguments(form, args, keyvals);
        return ret;
    }

    private Map<String, List<?>> marshalInputArguments(Node form,
            Map<String, List<? extends Object>> args)
            throws XPathExpressionException {

        Map<String, List<?>> ret = new HashMap<String, List<?>>();

        String xPathString = "//input[@name]";
        NodeList nodeList = nodesForXPath(form, xPathString);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node input = nodeList.item(i);
            NamedNodeMap inputAttributes = input.getAttributes();
            String inputName = getAttribute("name", inputAttributes);
            Assert.notNull(inputName);
            if (args.containsKey(inputName)) {
                ret.put(inputName, args.get(inputName));
            } else {
                List<? extends Object> currValue = Arrays.asList(getAttribute(
                        "value", inputAttributes));
                if (currValue != null) {
                    ret.put(inputName, currValue);
                }
            }
        }
        return ret;
    }

    public Document parsedContent() {
        return document;
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
