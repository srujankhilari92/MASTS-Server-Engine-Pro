package config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ethernet.MastsvA;

/**
 * @Class Name : ModifyPom.java
 */
/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Sep 29, 2015 2:49:56 PM
 * @License : Copyright � 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
public class MastsvMP {

    /**
     * Method Name: main
     *
     * @param args Description:
     */
    public static void modify(String file) {
        // TODO Auto-generated method stub

        try {
            String filepath = file;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

        
            Node staff = doc.getElementsByTagName("properties").item(0);
            NodeList list = staff.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);
                if ("jetty.port".equals(node.getNodeName())) {
                    node.setTextContent(MastsvA.port);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Modpom");
        }

    }

}
