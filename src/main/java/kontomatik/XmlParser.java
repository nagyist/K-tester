package kontomatik;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 16/07/16
 */
public class XmlParser {

    private XPath xpath = XPathFactory.newInstance().newXPath();
    private Document document;

    public Document getDocument() {
        return document;
    }

    public XmlParser(InputStream in) throws IOException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(in);
            is.setEncoding("UTF-8"); // probably unnecessary call
            document = db.parse(is);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        } finally {
            in.close();
        }
    }

    public String getCommandId() throws IOException {
        String result;
        try {
            Node commandNode = (Node) xpath.evaluate("/reply/command", document, XPathConstants.NODE);
            result = (String) xpath.evaluate("@id", commandNode, XPathConstants.STRING);
            if (result == null || result.isEmpty()) throw new IOException("\'id\' attribute not found");
            else
                return result;
        } catch (XPathExpressionException e) {
            throw new IOException(e);
        }
    }

    public String getState() throws IOException {
        String result;
        try {
            Node replyNode = (Node) xpath.evaluate("/reply/command", document, XPathConstants.NODE);
            result = (String) xpath.evaluate("@state", replyNode, XPathConstants.STRING);
            if (result == null || result.isEmpty()) throw new IOException("\'status\' attribute not found");
            else
                return result;
        } catch (XPathExpressionException e) {
            throw new IOException(e);
        }
    }


    public List<String> getBankCommandsList(String targetName) throws IOException {
        try {
            String expression = String.format("//target[@name='%s']", targetName);
            Node targetNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
            Node commandsNode = (Node) xpath.evaluate("commands", targetNode, XPathConstants.NODE);
            NodeList commandNodes = (NodeList) xpath.evaluate("command", commandsNode, XPathConstants.NODESET);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < commandNodes.getLength(); i++) {
                Node node = commandNodes.item(i);
                String name = (String) xpath.evaluate("@name", node, XPathConstants.STRING);
                list.add(name);
            }
            return list;

        } catch (XPathExpressionException e) {
            throw new IOException(e);
        }
    }

    public static void writeToOutputStream(Document doc, PrintWriter writer) throws TransformerException {
        DOMSource source = new DOMSource(doc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, new StreamResult(writer));
    }


    public static void documentToFile(Document doc, String fileName) throws TransformerException {
        File file = new File(fileName);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        DOMSource source = new DOMSource(doc);
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

    }
}
