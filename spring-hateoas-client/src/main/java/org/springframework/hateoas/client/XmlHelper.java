package org.springframework.hateoas.client;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.util.Assert;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlHelper {
    public static Node nodeForXPath(Node node, String xPathString)
            throws XPathExpressionException {
        QName returnType = XPathConstants.NODE;
        return (Node) getXPath(node, xPathString, returnType);
    }

    public static NodeList nodesForXPath(Node node, String xPathString)
            throws XPathExpressionException {
        QName returnType = XPathConstants.NODESET;
        return (NodeList) getXPath(node, xPathString, returnType);
    }

    public static Object getXPath(Node node, String xPathString, QName returnType)
            throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expression = xPath.compile(xPathString);
        return expression.evaluate(node, returnType);
    }

    public static String getAttribute(String name, NamedNodeMap attributes) {
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

}
