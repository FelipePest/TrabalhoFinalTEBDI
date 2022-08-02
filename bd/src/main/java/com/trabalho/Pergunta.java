package com.trabalho;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.jena.query.*;

import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.CollectionReferringAccumulator;
import com.trabalho.models.Resources;

public class Pergunta {

    public static ArrayList<Resources> resourceList = new ArrayList<>();

    public static ArrayList<Double> resultadoLIB = new ArrayList<>();

    public static void run(String[] query){
        int[] valores = query1(query);
        calculaLIB(valores);
        computaRanks();
        ordenaRecursos();
    }

    private static int[] query1(String[] query){

        String query1 = ""
        + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
        + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
        + "\n"
        + "select distinct ?resource VAR where {\n"
        + "  ?resource rdfs:label ?label.\n"
        + "  ?resource dbo:abstract ?abstract.\n"
        + "  FILTER(LANG(?label) = '' || LANGMATCHES(LANG(?label), 'en')).\n"
        + "  FILTER(LANG(?abstract) = '' || LANGMATCHES(LANG(?abstract), 'en')).\n"
        + "  FILTER (ALGO ) \n"
        + "  BINDER } ";


        for (int i =0; i < query.length; i++){

            query1 = query1.replace("ALGO", "regex(?abstract, " + "\"" + query[i] +  "\""+ " , 'i')  || regex(?label, " + "\"" + query[i] +  "\""+ " , 'i') || ALGO" );

            query1 = query1.replace("VAR", "?"+ i + " " + "VAR");

            query1 = query1.replace("BINDER", "BIND(IF(regex(?abstract, " + "\"" + query[i] +  "\""+ " , 'i')  || regex(?label, " + "\"" + query[i] +  "\""+ ", 'i'), 'TRUE', 'FALSE') as ?" + i + " ) \n BINDER");

        }

        query1 = query1.replace("|| ALGO", "");

        query1 = query1.replace("BINDER", "");

        query1 = query1.replace("VAR", "");

        System.out.println("\n" + query1 + "\n");

        ParameterizedSparqlString qs = new ParameterizedSparqlString(query1);


        QueryExecution exec = QueryExecution.service("http://dbpedia.org/sparql", qs.asQuery());

        ResultSet results = exec.execSelect();

        int[] myIntArray = new int[query.length];

        while (results.hasNext()){

            ArrayList<Boolean> bool = new ArrayList<>();

            QuerySolution set = results.next();

            for (int i = 0; i<query.length; i++){
                boolean tem = Boolean.parseBoolean(set.get(String.valueOf(i)).toString());
                if (tem){
                myIntArray[i] += 1;
                }
                bool.add(tem);
            }
            Resources resource = new Resources(set.get("resource").toString(), bool);
            
            resourceList.add(resource);

        }
    
        return myIntArray;
    }

    private static void calculaLIB(int[] valores){
        for (int i = 0; i <= valores.length -1; i++){
            double val = valores[i];
            double listSize = resourceList.size();
            double tempLIB = (double) -1*(val/listSize)*(1 - Math.log(val/listSize));
            resultadoLIB.add(tempLIB);
        }
    }

    private static void computaRanks(){
        for (int i = 0; i <= resourceList.size()-1;i++){
            for (int x=0; x <= resultadoLIB.size()-1;x++){
                Boolean tem = resourceList.get(i).getBool().get(x);
                Double valor = resourceList.get(i).getValue();
                if (tem){
                    valor = valor + 1 + resultadoLIB.get(x);
                    resourceList.get(i).setValue(valor);
                }else{
                    valor = valor + resultadoLIB.get(x);
                    resourceList.get(i).setValue(valor);
                }
            }
        }
    }

    private static void ordenaRecursos(){
        Collections.sort(resourceList, Resources.ResourceValueComparator);
        for (int i=0; i<= 4; i++){
            try {
                System.out.println(resourceList.get(i).getResources());
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

}
