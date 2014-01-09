package fr.sparna.rdf.skos.printer.reader;

import java.net.URI;
import java.util.UUID;

import org.openrdf.repository.Repository;

import fr.sparna.rdf.sesame.toolkit.query.SparqlPerformException;
import fr.sparna.rdf.skos.printer.schema.KosDisplay;

/**
 * Responsible for generating the body of a Display.
 * 
 * @author Thomas Francart.
 */
public abstract class AbstractKosDisplayGenerator {

	protected SKOSTags skosTags;
	
	protected Repository repository;
	
	protected String displayId;
	
	public AbstractKosDisplayGenerator(Repository repository, String displayId) {
		super();
		this.repository = repository;
		this.displayId = displayId;
	}
	
	/**
	 * Creates a geenrator with a randomly-created displayId
	 * @param repository
	 */
	public AbstractKosDisplayGenerator(Repository repository) {
		this(repository, UUID.randomUUID().toString());
	}

	public KosDisplay generateDisplay(String mainLang, final URI conceptScheme, BodyReader bodyReader) 
	throws SparqlPerformException {
		// prevent null language
		if(mainLang == null) {
			mainLang = "";
		}
		
		this.initTagsBundle(mainLang);
		
		// sets the ID on the generated display and return
		KosDisplay display = this.doGenerate(mainLang, conceptScheme, bodyReader);
		display.setDisplayId(this.displayId);
		return display;
	}
	
	protected void initTagsBundle(String lang) {
		// init tag resource bundle if not set
		if(this.skosTags == null) {
			skosTags = SKOSTags.getInstance(lang);
		}
	}
	
	protected abstract KosDisplay doGenerate(String mainLang, final URI conceptScheme, BodyReader bodyReader)
	throws SparqlPerformException ;

	public String getDisplayId() {
		return displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}	
	
}
