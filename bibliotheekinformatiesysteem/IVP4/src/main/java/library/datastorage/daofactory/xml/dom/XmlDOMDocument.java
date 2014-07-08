/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.xml.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 
 * @author Robin Schellius
 */
public class XmlDOMDocument {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMDocument.class);
	private final String xmlFilename = ".\\src\\resources\\library.xml";
	private Document document = null;

	public XmlDOMDocument() {
		logger.debug("Constructor");
	}

	public Document getDocument() {

		logger.debug("getDocument");

		if (document == null) {
			document = buildDocument(xmlFilename);
			// validateDocument(document, getValidationSchema());
		}

		logger.debug("returning document");
		return document;
	}

	public void writeDocument() {

		logger.debug("writing XML document to file");

		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(xmlFilename));
			transformer.transform(source, result);

			logger.debug("done writing file");

		} catch (TransformerConfigurationException e) {
			logger.error(e.getMessage());
		} catch (TransformerException e) {
			logger.error(e.getMessage());
		}
	}

	private Document buildDocument(String filename) {

		logger.debug("buildDocument " + filename);

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			File file = new File(filename);
			if (file.exists()) {
				document = builder.parse(new FileInputStream(file));

				// Here we should check if the document we found
				// has actually the layout/structure we expect.
				// e.g. rootelement = <library> etc.

			} else {
				logger.debug("File " + filename + " does not exist, creating new document");

				// Create a new initial document, with initial structure.
				// Here we should decide on the structure of the XML document. 
				// In a completely worked-out example, we should have elements for
				// <books> and <copies> too. We skip those here for simplicity reasons.
				//
				// One possible structure would be:
				//
				//	<library>
				//		<members>
				//			<member>
				//				member elements here
				//			</member>
				//		</members>
				//		<books>
				//			<book>
				//				book elements go here
				//			</book>
				//		</books>
				//	</library>
				//
				document = builder.newDocument();
				Element rootElement = document.createElement("library");
				document.appendChild(rootElement);

				Element membersElement = document.createElement("members");
				rootElement.appendChild(membersElement);

				logger.debug("Created new document");
			}
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return document;
	}

	private Schema getValidationSchema() {

		Schema schema = null;

		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			schema = factory.newSchema(new File("filename here"));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return schema;
	}

	private void validateDocument(Document doc, Schema schema) {

		Validator validator = schema.newValidator();

		try {
			validator.validate(new DOMSource(doc));
		} catch (SAXException | IOException e) {
			logger.error(e.getMessage());
		}
	}
}
