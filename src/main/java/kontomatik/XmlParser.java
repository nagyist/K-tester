package kontomatik;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 16/07/16
 */
public class XmlParser {

    private DocumentBuilder db;

    public XmlParser() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            db = dbf.newDocumentBuilder();
        } catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Document createDocument(InputStream in) {
        InputSource is = new InputSource(in);
        is.setEncoding("UTF-8"); // probably unnecessary call
        try {
            return db.parse(is);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }



    public List<String> getBankCommandsList(InputStream in, String targetName) throws IOException {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = String.format("//target[@name='%s']", targetName);
            InputSource is = new InputSource(in);
            Node targetNode = (Node) xpath.evaluate(expression, is, XPathConstants.NODE);
            Node commandsNode = (Node) xpath.evaluate("commands", targetNode, XPathConstants.NODE);
            NodeList commandNodes = (NodeList) xpath.evaluate("command", commandsNode, XPathConstants.NODESET);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < commandNodes.getLength(); i++) {
                Node node = commandNodes.item(i);
                String name = (String) xpath.evaluate("@name", node, XPathConstants.STRING);
                list.add(name);
            }
            return list;

        } catch ( XPathExpressionException e) {
            e.printStackTrace();
            return null;
        } finally {
            in.close();
        }

    }
}
