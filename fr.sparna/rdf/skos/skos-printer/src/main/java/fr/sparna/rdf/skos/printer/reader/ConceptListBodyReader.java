package fr.sparna.rdf.skos.printer.reader;

import java.io.File;
import java.net.URI;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.sparna.commons.lang.StringUtil;
import fr.sparna.rdf.sesame.toolkit.query.Perform;
import fr.sparna.rdf.sesame.toolkit.query.SPARQLPerformException;
import fr.sparna.rdf.sesame.toolkit.repository.RepositoryBuilder;
import fr.sparna.rdf.skos.printer.DisplayPrinter;
import fr.sparna.rdf.skos.printer.schema.ConceptBlock;
import fr.sparna.rdf.skos.printer.schema.Display;
import fr.sparna.rdf.skos.printer.schema.DisplayBody;
import fr.sparna.rdf.skos.printer.schema.DisplayHeader;
import fr.sparna.rdf.skos.printer.schema.Section;
import fr.sparna.rdf.skos.toolkit.GetConceptsInSchemeHelper;
import fr.sparna.rdf.skos.toolkit.SKOS;

public class ConceptListBodyReader extends AbstractBodyReader {
	
	public static final List<String> EXPANDED_SKOS_PROPERTIES = Arrays.asList(new String[] {
			SKOS.NOTATION,
			SKOS.ALT_LABEL,
			SKOS.BROADER,
			SKOS.NARROWER,
			SKOS.RELATED,			
			SKOS.DEFINITION,
			SKOS.SCOPE_NOTE,
			SKOS.EXAMPLE,
			SKOS.CHANGE_NOTE,			
			SKOS.HISTORY_NOTE,
			SKOS.EDITORIAL_NOTE,
	});
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected ConceptBlockReader cbReader;

	public ConceptListBodyReader(Repository r, ConceptBlockReader cbReader) {
		super(r);
		this.cbReader = cbReader;
	}

	@Override
	public DisplayBody doRead(final String lang, final URI conceptScheme) 
	throws SPARQLPerformException {

		// init ConceptBlockReader
		this.cbReader.initInternal(tagsBundle, lang, conceptScheme);
		
		// build our display
		DisplayBody body = new DisplayBody();

		final List<QueryResultRow> queryResultRows = new ArrayList<QueryResultRow>();
		
		GetConceptsInSchemeHelper helper = new GetConceptsInSchemeHelper(
				lang,
				conceptScheme
		) {
			@Override
			protected void handleConcept(
					Resource concept,
					Literal label
			) throws TupleQueryResultHandlerException {
				QueryResultRow es = new QueryResultRow();
				es.conceptURI = concept.stringValue();
				es.prefLabel = (label != null)?label.stringValue():null;	
				queryResultRows.add(es);
			}
		};
		
		Perform.on(repository).select(helper);		

		// setup Collator
		final Collator collator = Collator.getInstance(new Locale(lang));
		collator.setStrength(Collator.SECONDARY);
		Collections.sort(queryResultRows, new Comparator<QueryResultRow>() {

			@Override
			public int compare(QueryResultRow o1, QueryResultRow o2) {
				if(o1 == null && o2 == null) return 0;
				if(o1 == null) return -1;
				if(o2 == null) return 1;
				return collator.compare(o1.prefLabel, o2.prefLabel);
			}
			
		});
		
		boolean addSections = queryResultRows.size() > 200;
		log.debug("Processing "+queryResultRows.size()+" entries.");
		if(addSections) {
			log.debug("Will add sections to the output");
			Section currentSection = null;
			for (QueryResultRow aRow : queryResultRows) {
				ConceptBlock cb = this.cbReader.readConceptBlock(aRow.conceptURI, aRow.prefLabel, true);

				String entrySectionTitle = StringUtil.withoutAccents(aRow.prefLabel).toUpperCase().substring(0, 1);
				if(currentSection == null || !entrySectionTitle.equals(currentSection.getTitle())) {
					// on est passé à une nouvelle section
					
					// on ajoute la section courante maintenant remplie
					body.getSection().add(currentSection);
					
					// et on créé une nouvelle section
					currentSection = new Section();
					fr.sparna.rdf.skos.printer.schema.List newList = new fr.sparna.rdf.skos.printer.schema.List();
					currentSection.setList(newList);
					currentSection.setTitle(entrySectionTitle);
				}
				currentSection.getList().getListItem().add(SchemaFactory.createListItem(cb));
			}
			// ajouter la dernière section
			body.getSection().add(currentSection);
		} else {
			log.debug("No sections added to output");
			Section s = new Section();
			fr.sparna.rdf.skos.printer.schema.List list = new fr.sparna.rdf.skos.printer.schema.List();
			s.setList(list);
			for (QueryResultRow aRow : queryResultRows) {
				ConceptBlock cb = this.cbReader.readConceptBlock(aRow.conceptURI, aRow.prefLabel, true);
				list.getListItem().add(SchemaFactory.createListItem(cb));
			}
			body.getSection().add(s);
		}
		
		return body;
	}
	
	class QueryResultRow {
		String prefLabel;
		String conceptURI;
	}

	public void setTagsBundle(ResourceBundle tagsBundle) {
		this.tagsBundle = tagsBundle;
	}

	public static void main(String... args) throws Exception {	
//		Repository r = RepositoryBuilder.fromRdf(
//				"@prefix skos: <"+SKOS.NAMESPACE+"> ."+"\n" +
//				"@prefix test: <http://www.test.fr/skos/> ."+"\n" +
//				"test:_1 a skos:Concept ; skos:inScheme test:_scheme ; skos:prefLabel \"C-1-pref\"@fr; skos:altLabel \"A-1-alt\"@fr ." +
//				"test:_2 a skos:Concept ; skos:inScheme test:_scheme ; skos:prefLabel \"B-2-pref\"@fr ." +
//				"test:_3 a skos:Concept ; skos:inScheme test:_anotherScheme ; skos:prefLabel \"D-3-pref\"@fr ."
//		);
		
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
		org.apache.log4j.Logger.getLogger("fr.sparna.rdf").setLevel(org.apache.log4j.Level.TRACE);
		
		Repository r = RepositoryBuilder.fromString(args[0]);
		
		// build display result
		Display display = new Display();
		
		// build and set header
		HeaderReader headerReader = new HeaderReader(r);
		DisplayHeader header = headerReader.read("fr", (args.length > 1)?URI.create(args[1]):null);
		display.setHeader(header);
		
		ConceptListBodyReader reader = new ConceptListBodyReader(r, new ConceptBlockReader(r, EXPANDED_SKOS_PROPERTIES));
		display.setBody(reader.readBody("fr", (args.length > 1)?URI.create(args[1]):null));

		DisplayPrinter printer = new DisplayPrinter();
		printer.setDebug(true);
		// ask for 2-columns !
		printer.getTransformerParams().put("column-count", 2);
		printer.printToHtml(display, new File("display-test.html"));
		printer.printToPdf(display, new File("display-test.pdf"));
		
//		Marshaller m = JAXBContext.newInstance("fr.sparna.rdf.skos.printer.schema").createMarshaller();
//		m.setProperty("jaxb.formatted.output", true);
//		m.marshal(display, System.out);
	}
	
}