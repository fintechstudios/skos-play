package fr.sparna.rdf.skos.printer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.sparna.commons.xml.ClasspathURIResolver;
import fr.sparna.commons.xml.XSLProcessor;
import fr.sparna.commons.xml.fop.FopProcessor;
import fr.sparna.rdf.skos.printer.schema.KosDocument;

public class DisplayPrinter {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static String STYLESHEET_DISPLAY_TO_HTML = "stylesheets/display-to-html.xsl";
	public static String STYLESHEET_DISPLAY_TO_FOP = "stylesheets/display-to-fop.xsl";
	
	private static String LANG_PARAM = "lang";
	private static List<String> SUPPORTED_LANGUAGES = Arrays.asList(new String[]{ "en", "fr" });
	
	
	protected boolean debug = true;
	protected String debugPath = null;
	
	protected Map<String, Object> transformerParams = new HashMap<String, Object>();
	
	
	public void printToPdf(
			KosDocument document,
			File outputFile,
			String lang
	) throws FOPException, TransformerException, IOException, JAXBException {
		Marshaller m = createMarshaller();
		debugJAXBMarshalling(m, document);
		
		FopProcessor p = new FopProcessor();
		p.setDebugFo(debug);
		p.setDebugPath(this.debugPath);
		StreamSource xslSource = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(STYLESHEET_DISPLAY_TO_FOP));

		// set a classpath URI resolver so that the XSL can resolve the labels file in the "stylesheets" classpath folder
		XSLProcessor xslProc = XSLProcessor.createDefaultProcessor();
		xslProc.setUriResolver(new ClasspathURIResolver("stylesheets"));
		
		// Transformer t = XSLProcessor.createDefaultProcessor().createTransformer(xslSource);
		Transformer t = xslProc.createTransformer(xslSource);
		
		if(this.transformerParams != null) {
			for (String aKey : this.transformerParams.keySet()) {
				t.setParameter(aKey, this.transformerParams.get(aKey));
			}
		}
		// add the language as a parameter
		t.setParameter(LANG_PARAM, selectLanguage(lang));
		
		p.processToFile(
				new JAXBSource(m, document),
				t,
				outputFile
		);
	}
	
	public void printToPdf(
			KosDocument document,
			OutputStream os,
			String lang
	) throws FOPException, TransformerException, IOException, JAXBException {
		Marshaller m = createMarshaller();
		debugJAXBMarshalling(m, document);
		
		FopProcessor p = new FopProcessor();
		p.setDebugFo(debug);
		p.setDebugPath(this.debugPath);
		StreamSource xslSource = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(STYLESHEET_DISPLAY_TO_FOP));
		
		// set a classpath URI resolver so that the XSL can resolve the labels file in the "stylesheets" classpath folder
		XSLProcessor xslProc = XSLProcessor.createDefaultProcessor();
		xslProc.setUriResolver(new ClasspathURIResolver("stylesheets"));		
		Transformer t = xslProc.createTransformer(xslSource);
		
		if(this.transformerParams != null) {
			for (String aKey : this.transformerParams.keySet()) {
				t.setParameter(aKey, this.transformerParams.get(aKey));
			}
		}
		// add the language as a parameter
		t.setParameter(LANG_PARAM, selectLanguage(lang));
				
		p.process(
				new JAXBSource(m, document),
				t,
				os
		);
	}
	
	public void printToHtml(
			KosDocument document,
			File htmlFile,
			String lang
	) throws FileNotFoundException, JAXBException, TransformerException {
		
		if(!htmlFile.exists()) {
			try {
				if(htmlFile.getParentFile() != null) {
					htmlFile.getParentFile().mkdirs();
				}
				htmlFile.createNewFile();
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
		}
		
		printToHtml(
				document,
				new BufferedOutputStream(new FileOutputStream(htmlFile)),
				lang
		);
	}
	
	public void printToHtml(
			KosDocument document,
			OutputStream os,
			String lang
	) throws FileNotFoundException, JAXBException, TransformerException {
		Marshaller m = createMarshaller();
		debugJAXBMarshalling(m, document);
		
		try {
			StreamSource xslSource = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(STYLESHEET_DISPLAY_TO_HTML));
			
			// set a classpath URI resolver so that the XSL can resolve the labels file in the "stylesheets" classpath folder
			XSLProcessor xslProc = XSLProcessor.createDefaultProcessor();
			xslProc.setUriResolver(new ClasspathURIResolver("stylesheets"));		
			Transformer t = xslProc.createTransformer(xslSource);
			
			if(this.transformerParams != null) {
				for (String aKey : this.transformerParams.keySet()) {
					t.setParameter(aKey, this.transformerParams.get(aKey));
				}
			}
			// add the language as a parameter
			t.setParameter(LANG_PARAM, selectLanguage(lang));
			
			t.transform(new JAXBSource(m, document), new StreamResult(os));
		} finally {
			if(os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException ignore) {
					ignore.printStackTrace();
				}
			}
		}	
	}
	
	private Marshaller createMarshaller() {
		try {
			return JAXBContext.newInstance("fr.sparna.rdf.skos.printer.schema").createMarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void debugJAXBMarshalling(Marshaller m, KosDocument document) throws JAXBException {
		if(debug) {
			File debugFile = new File(((debugPath != null)?debugPath:"")+".DisplayPrinter-debug.xml");
			log.debug("Will debug JAXB Marshalling in "+debugFile.getAbsolutePath());
			m.setProperty("jaxb.formatted.output", true);
			m.marshal(document, debugFile);
		}		
	}
	
	private String selectLanguage(String originalLang) {
		if(SUPPORTED_LANGUAGES.contains(originalLang)) {
			return originalLang;
		} else {
			return "en";
		}
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Map<String, Object> getTransformerParams() {
		return transformerParams;
	}

	public void setTransformerParams(Map<String, Object> transformerParams) {
		this.transformerParams = transformerParams;
	}

	public String getDebugPath() {
		return debugPath;
	}

	public void setDebugPath(String debugPath) {
		this.debugPath = debugPath;
	}	
}
