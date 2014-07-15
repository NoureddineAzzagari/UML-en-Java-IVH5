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
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XmlDOMDocument reads the XML input document that functions as a data source for the
 * XML DOM data access objects. It validates and builds the document object model (DOM). 
 * The DOM is used throughout the application to perform CRUD-operations on the XML data. 
 * It could be viewed as an in-memory database, and needs to be saved at regular times.
 *   
 * @author Robin Schellius
 */
public class XmlDOMDocument {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMDocument.class);

	// These filenames could be stored in an external configuration file instead of
	// hard-coding it here.
	private final String xmlFilename = ".\\resources\\library.xml";
	private final String xmlSchema = ".\\resources\\library.xsd";

	// The document that will contain the DOM.
	private Document document = null;

	public XmlDOMDocument() {
		// Empty constructor, no initialization done here.
	}

	/**
	 * Get the XML document that functions as the data source. We try to validate
	 * it, if the given XSD file is available.
	 * 
	 * @return
	 */
	public Document getDocument() {

		logger.debug("getDocument");

		if (document == null) {
			// First validate that the XML datasource conforms the schema.
			Schema schema = getValidationSchema();
			if (schema == null) {
				logger.error("Schema file not found or contains errors, XML file not validated!");
				// Here we could decide to cancel initialization of the application.
				// For now, we do not.
			} else {
				validateDocument(xmlFilename, schema);
			}

			// Whether validated or not, try to build the
			// Document Object Model from the inputfile
			document = buildDocument(xmlFilename);
			if (document == null) {
				logger.fatal("No XML data source found! Cannot read application data!");
				// Again, here we could decide to cancel initialization of the application.
				// A non-validated inputfile could lead to read-errors when reading domain
				// objects from the data source. For now, we continue. Fingers crossed.
			}
		}
		return document;
	}

	/**
	 * 
	 */
	public void writeDocument() {

		logger.debug("writing XML document to file " + xmlFilename);

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

	/**
	 * 
	 * @param filename
	 * @return
	 */
	private Document buildDocument(String filename) {

		logger.debug("buildDocument " + filename);

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			File file = new File(filename);
			if (file.exists()) {
				// file found, so parse it and build the document object model.
				document = builder.parse(new FileInputStream(file));
			} else {
				logger.fatal("XML datasource " + filename + " does not exist! No data read!");
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

	/**
	 * 
	 * @return
	 */
	private Schema getValidationSchema() {
		Schema schema = null;

		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			logger.debug("getValidationSchema " + xmlSchema);
			File xmlSchemaFile = new File(xmlSchema);
			if(xmlSchemaFile.exists()) {
				schema = factory.newSchema(xmlSchemaFile);
			}
		} catch (Exception e) {
			logger.error("Error reading schema file: " + e.getMessage());
		}

		return schema;
	}

	/**
	 * 
	 * @param doc
	 * @param schema
	 */
	private boolean validateDocument(String xmlFile, Schema schema) {
		logger.debug("validateDocument");

		boolean result = false;

		try {
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlFile));
			result = true;
			logger.debug("Valid XML: " + xmlFile);
		} catch (IOException e) {
			logger.error("I/O error: " + e.getMessage());
		} catch (SAXException e) {
			logger.error("Parse exception: " + e.getMessage());
		}
		return result;

	}
}
