package com.mkyong.xml.jdom;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.XMLConstants;
import java.io.*;
import java.util.List;

public class XmlUtilJDom {

    private static final String FILENAME = "src/main/resources/hexaware.xml";

    public static void main(String[] args) throws JDOMException, IOException {

        //writeSimpleXml();

        writeXml(System.out);
        /**
         * <?xml version="1.0" encoding="UTF-8"?>
         * <company>
         *   <staff id="1001">
         *     <name>mkyong</name>
         *     <role>support</role>
         *     <salary curreny="USD">5000</salary>
         *     <!--for special characters like < &, need CDATA-->
         *     <bio><![CDATA[HTML tag <code>testing</code>]]></bio>
         *   </staff>
         *   <staff id="1002">
         *     <name>yflow</name>
         *     <role>admin</role>
         *     <salary curreny="EUD">8000</salary>
         *     <bio><![CDATA[a & b]]></bio>
         *   </staff>
         * </company>
         */

    }

    private static void writeXml(OutputStream output) throws IOException {

        Document doc = new Document();
        doc.setRootElement(new Element("company"));

        Element staff = new Element("staff");
        // add xml attribute
        staff.setAttribute("id", "1001");

        staff.addContent(new Element("name").setText("mkyong"));
        staff.addContent(new Element("role").setText("support"));
        staff.addContent(new Element("salary")
                .setAttribute("curreny", "USD").setText("5000"));

        // add xml comments
        staff.addContent(new Comment("for special characters like < &, need CDATA"));

        // add xml CDATA
        staff.addContent(new Element("bio")
                .setContent(new CDATA("HTML tag <code>testing</code>")));

        // append child to root
        doc.getRootElement().addContent(staff);

        Element staff2 = new Element("staff");
        staff2.setAttribute("id", "1002");
        staff2.addContent(new Element("name").setText("yflow"));
        staff2.addContent(new Element("role").setText("admin"));
        staff2.addContent(new Element("salary")
                .setAttribute("curreny", "EUD").setText("8000"));
        // add xml CDATA
        staff2.addContent(new Element("bio")
                .setContent(new CDATA("a & b")));

        // append child to root
        doc.getRootElement().addContent(staff2);

        XMLOutputter xmlOutputter = new XMLOutputter();
        // change xml encoding
        xmlOutputter.setFormat(Format.getPrettyFormat().setEncoding("ISO-8859-1"));
        //xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(doc, output);

        // output to any OutputStream
        /*try(FileOutputStream fileOutputStream =
                    new FileOutputStream("c:\\test\\file.xml")){
            xmlOutputter.output(doc, fileOutputStream);
        }*/

        // output to any Writer
        try(FileWriter fileWriter =
                    new FileWriter("c:\\test\\file.xml")){
            xmlOutputter.output(doc, fileWriter);
        }

    }
    private static void readXML() {

        try {

            SAXBuilder sax = new SAXBuilder();

            // https://rules.sonarsource.com/java/RSPEC-2755
            // prevent xxe
            sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // XML is a local file
            Document doc = sax.build(new File(FILENAME));

            Element rootNode = doc.getRootElement();
            List<Element> list = rootNode.getChildren("staff");

            for (Element target : list) {

                String id = target.getAttributeValue("id");
                String name = target.getChildText("name");
                String role = target.getChildText("role");
                String salary = target.getChildText("salary");
                String currency = "";
                if (salary != null && salary.length() > 1) {
                    // access attribute
                    currency = target.getChild("salary").getAttributeValue("currency");
                }
                String bio = target.getChildText("bio");

                System.out.printf("Staff id : %s%n", id);
                System.out.printf("Name : %s%n", name);
                System.out.printf("Role : %s%n", role);
                System.out.printf("Salary [Currency] : %,.2f [%s]%n", Float.parseFloat(salary), currency);
                System.out.printf("Bio : %s%n%n", bio);

            }

        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }

    }

}