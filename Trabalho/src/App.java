import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;

public class App {

public static void main(String[] args) {
 

        String query1 = ""
        + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
        + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
        + "\n"
        + "select distinct ?resource ?abstract ?label where {\n"
        + "  ?resource rdfs:label ?label.\n"
        + "  ?resource dbo:abstract ?abstract.\n"
        + "  FILTER(LANG(?label) = '' || LANGMATCHES(LANG(?label), 'en')).\n"
        + "  FILTER(LANG(?abstract) = '' || LANGMATCHES(LANG(?abstract), 'en')).\n"
        + "  FILTER (regex(?abstract, \"ENTITY\" , 'i')  || regex(?label, \"ENTITY\" , 'i'))} ";

        String query1a = query1.replace("ENTITY", "Brazil");


        System.out.println(query1a);

        ParameterizedSparqlString qs = new ParameterizedSparqlString(query1a);


        QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

        ResultSet results = exec.execSelect();

        while (results.hasNext()) {

            System.out.println(results.next().get("resource").toString());
        }

        //ResultSetFormatter.out(results);
    }

    
}
