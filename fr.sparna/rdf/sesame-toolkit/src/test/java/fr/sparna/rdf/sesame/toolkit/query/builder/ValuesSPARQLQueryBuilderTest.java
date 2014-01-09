package fr.sparna.rdf.sesame.toolkit.query.builder;

import java.util.Arrays;

import org.junit.Test;
import org.openrdf.model.Value;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import fr.sparna.rdf.sesame.toolkit.handler.DebugHandler;
import fr.sparna.rdf.sesame.toolkit.query.Perform;
import fr.sparna.rdf.sesame.toolkit.query.builder.SparqlQueryBuilder;
import fr.sparna.rdf.sesame.toolkit.query.SelectSparqlHelper;

public class ValuesSPARQLQueryBuilderTest {

	@Test
	public void test1() throws Exception {
		String sparql = "SELECT DISTINCT ?type WHERE { ?x a ?type }";
		
		Repository r = new SailRepository(new MemoryStore());
		r.initialize();
		
		ValuesSparqlQueryBuilder builder = new ValuesSparqlQueryBuilder(
				new SparqlQueryBuilder(sparql),
				"x",
				Arrays.asList(new Value[] { r.getValueFactory().createURI("http://www.exemple.fr")})
		);
		System.out.println(builder.getSPARQL());
		Perform.on(r).select(new SelectSparqlHelper(builder, new DebugHandler()));
	}
	
}
