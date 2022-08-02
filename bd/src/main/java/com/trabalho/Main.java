package com.trabalho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static String query;

    private static List<String> stopwords;

    private static String[] queryTratada;

    public static void main(String[] args) throws IOException {
    
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Digite a query: ");
            query = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        query = query.toLowerCase();

       loadStopWords();

       query = removeAll();

       queryTratada = query.split(" ");

       Pergunta.run(queryTratada);



  
    }

    public static void loadStopWords() throws IOException{
        stopwords = Files.readAllLines(Paths.get("src/main/java/com/trabalho/resources/english_stopwords.txt"));
    }

    public static String removeAll(){
        ArrayList<String> allWords = Stream.of(query.split(" "))
        .collect(Collectors.toCollection(ArrayList<String>::new));

        allWords.removeAll(stopwords);

        return allWords.stream().collect(Collectors.joining(" "));
    }
}
