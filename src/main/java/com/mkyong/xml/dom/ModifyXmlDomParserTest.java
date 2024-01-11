package com.mkyong.xml.dom;

import org.w3c.dom.*;
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
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class ModifyXmlDomParserTest {

    private static final String FILENAME = "src/main/resources/staff-simple.xml";
    // xslt for pretty print only, no special task
    private static final String FORMAT_XSLT = "src/main/resources/xslt/staff-format.xslt";

    public static void main(String[] args) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try (InputStream is = new FileInputStream(FILENAME)) {

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(is);

            Element root = doc.getDocumentElement();

            NodeList listOfStaff = doc.getElementsByTagName("staff");
            //System.out.println(listOfStaff.getLength()); // 2


                // get first staff
                Node staff = listOfStaff.item(0);
                if (staff.getNodeType() == Node.ELEMENT_NODE) {

                    for (int j = 0; j < 10; j++) {
                        Node newStaff = staff.cloneNode(true);
                        newStaff.getAttributes().getNamedItem("id").setTextContent("id-"+j);
                        NodeList childNodes = newStaff.getChildNodes();
                        for (int k = 0; k < childNodes.getLength(); k++) {
                            Node item = childNodes.item(k);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("name".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent("Name-"+j);
                                }
                            }
                        }

                        root.appendChild(newStaff);
                        //staff.getAttributes().getNamedItem("name").setTextContent("name-"+j);
                    }

                }



            /*for (int i = 0; i < listOfStaff.getLength(); i++) {
                // get first staff
                Node staff = listOfStaff.item(i);
                if (staff.getNodeType() == Node.ELEMENT_NODE) {


                        for (int j = 0; j < 10; j++) {
                            Node newStaff = staff.cloneNode(true);
                            newStaff.getAttributes().getNamedItem("id").setTextContent("id-"+j);
                            root.appendChild(newStaff);
                            //staff.getAttributes().getNamedItem("name").setTextContent("name-"+j);
                        }

                }

            }*/

            // output to console
            // writeXml(doc, System.out);

            try (FileOutputStream output =
                         new FileOutputStream("c:\\test\\staff-modified-list.xml")) {
                writeXml(doc, output);
            }

        } catch (ParserConfigurationException | SAXException
                | IOException | TransformerException e) {
            e.printStackTrace();
        }

    }

    // write doc to output stream
    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException, UnsupportedEncodingException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        // The default add many empty new line, not sure why?
        // https://stackoverflow.com/questions/58478632/how-to-avoid-extra-blank-lines-in-xml-generation-with-java
        // https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
        // Transformer transformer = transformerFactory.newTransformer();

        // add a xslt to remove the extra newlines
        Transformer transformer = transformerFactory.newTransformer(
                new StreamSource(new File(FORMAT_XSLT)));

        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

}
