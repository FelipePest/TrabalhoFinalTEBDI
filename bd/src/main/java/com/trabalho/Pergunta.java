package com.trabalho;


import org.apache.jena.query.*;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;

public class Pergunta {

    static ResultSet resultadoQuery;

    public static void run(String[] query){
            resultadoQuery = query1(query);
    }

    private static ResultSet query1(String[] query){

        String query1 = ""
        + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
        + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
        + "\n"
        + "select distinct ?resource VAR where {\n"
        + "  ?resource rdfs:label ?label.\n"
        + "  ?resource dbo:abstract ?abstract.\n"
        + "  FILTER(LANG(?label) = '' || LANGMATCHES(LANG(?label), 'en')).\n"
        + "  FILTER(LANG(?abstract) = '' || LANGMATCHES(LANG(?abstract), 'en')).\n"
        + "  FILTER (ALGO ) "
        + "  BINDER } ";


        for (int i =0; i < query.length; i++){

            query1 = query1.replace("ALGO", "regex(?abstract, " + "\"" + query[i] +  "\""+ " , 'i')  || regex(?label, " + "\"" + query[i] +  "\""+ " , 'i') || ALGO" );

            query1 = query1.replace("VAR", "?"+ i + " " + "VAR");
            query1 = query1.replace("BINDER", "BIND(IF(regex(?abstract, " + "\"" + query[i] +  "\""+ " , 'i')  || regex(?label, " + "\"" + query[i] +  "\""+ ", 'i'), 'TRUE', 'FALSE') as ?" + i + " ) \n BINDER");
            System.out.println(query1);

        }

        query1 = query1.replace("|| ALGO", "");

        query1 = query1.replace("BINDER", "");

        query1 = query1.replace("VAR", "");

        System.out.println(query1);

        ParameterizedSparqlString qs = new ParameterizedSparqlString(query1);


        QueryExecution exec = QueryExecution.service("http://dbpedia.org/sparql", qs.asQuery());

        ResultSet results = exec.execSelect();

        while (results.hasNext()) {

            System.out.println(results.next().get("?0").toString());
        }

        return results;

        //ResultSetFormatter.out(results);
    }
}
