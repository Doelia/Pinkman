package edu.turtlekit3.warbot.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WarXmlWriter {
	
	public static void saveXmlFile(Document doc, String filePath) throws TransformerFactoryConfigurationError, TransformerException, IOException {
		saveXmlFile(doc, new File(filePath));
	}

	public static void saveXmlFile(Document doc, File file) throws TransformerFactoryConfigurationError, TransformerException, IOException {
		Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(file)));
	}

	public static Element createTextElement(Document doc, String nodeName, String content) {
		Element elt = doc.createElement(nodeName);
		elt.appendChild(doc.createTextNode(content));
		return elt;
	}
}
