package ch.lgt.ming.helper;

import ch.lgt.ming.cleanup.Document;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Ming Deng on 9/25/2016.
 */
public class DocClassification {

    public static void main(String[] args) throws IOException {

        List<String> websites = Arrays.asList("| Seeking Alpha", "| Reuters", "Zero Hedge");
        List<String> companies = Arrays.asList("amazon", "boeing", "delta", "facebook", "ford",
                "goldman", "google", "intel", "microsoft", "netflix");
        List<String> Folders = Arrays.asList("Amazon", "Apple", "Boeing", "Delta_Airline", "Disney", "Facebook", "Ford",
                "Goldman_Sachs", "Google", "Intel", "Microsoft", "Netflix", "Verizon", "Yahoo");



        /**
         * This part of file tests the function DocByWeb
         * */

        String inputFilePath = "data/DataForMing_V2";
        String outputFilePath = "data/Empirical_Analysis/ZeroHedge";
        String website = "zero hedge";

        DocByWeb(inputFilePath,outputFilePath,website);

        /**
         * This part of file tests the function DocSerByCom
         * */

//        String inputFilePath = "data/Empirical_Analysis/Seeking_AlphaSer";
//        String outputFilePath = "data/Empirical_Analysis/Seeking_AlphaSer_Company/Apple";
//        String comNames = "apple, aapl";
//
//        DocSerByCom(inputFilePath, outputFilePath, comNames);




        /**
         * This line creates new directories in the new corpus folder
         * */
//            Files.createDirectories(Paths.get("data/highlighted2/" + Folders.get(i) ));

    }
    /**
     * This function classifies the files from a certain website according to the company names
     *
     * @param inputFilePath The address of the .Ser documents
     * @param outputFilePath The address of the folder where documents are going to be put in
     * @param WebName The name of the website
     * */
    public static void DocByWeb(String inputFilePath, String outputFilePath, String WebName) throws IOException {

        FileHandler fileHandler = new FileHandler();
        File folder = new File(inputFilePath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            String docText = fileHandler.loadFileToString(inputFilePath + "/" + file.getName());

            if (docText.toLowerCase().contains(WebName)) {
                System.out.println("Found " + WebName);
                Files.copy(Paths.get(inputFilePath + "/" + file.getName()),
                        Paths.get(outputFilePath + "/" + file.getName()));
            }
        }
    }

    /**
     * This function classifies the files from a certain website according to the companies
     *
     * @param inputFilePath The address of the .Ser documents of a certain website
     * @param outputFilePath The address of the folder where documents are going to be put in
     * @param ComNames A string of the possible references to the company
    * */
    public static void DocSerByCom(String inputFilePath, String outputFilePath, String ComNames) throws IOException {

        File folder = new File(inputFilePath);
        File[] listOfFiles = folder.listFiles();

        FileInputStream fileInputStream;

        for (int i = 1; i < listOfFiles.length; i++) {

            try {
                fileInputStream = new FileInputStream(inputFilePath + "/" + listOfFiles[i].getName());
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Document document = (Document) objectInputStream.readObject();
                String comName = detectCom(document, false);
                System.out.println(comName);
                if (ComNames.contains(comName.toLowerCase())) {
                    Files.copy(Paths.get(inputFilePath + "/" + listOfFiles[i].getName()),
                            Paths.get(outputFilePath + "/" + listOfFiles[i].getName()));
                }

                System.out.printf("%d is done\n", document.getIndex());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function decides the main company related with the document
     *
     * @param document the document being examined
     * @param fs if fs is ture, then only consider the company name that appeared in the title; otherwise consider the
     *           whole text
     *
     * @return the name of the main company
     * */
    public static String detectCom(Document document, boolean fs) {
        List<String> ignoreList = Arrays.asList("reuters","bloomberg","co","thomson","journal");

        if (fs) {

            String comName;
            List<String> comNames = new ArrayList<>();
            Annotation annotation = document.getDocument();
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            CoreMap sentence = sentences.get(0);
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String pos = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                if (pos.equals("ORGANIZATION")&&(!ignoreList.contains(word.toLowerCase()))) {
                    comNames.add(token.get(CoreAnnotations.TextAnnotation.class));
                }
            }

            Map<String, Integer> comNamesCount = new HashMap<>();

            for (String com:comNames){
                int counts = org.apache.commons.lang3.StringUtils.countMatches(document.getDocumentText().toLowerCase(),com.toLowerCase());
                comNamesCount.put(com,counts);
            }
            System.out.println("The company is: " + maxMap(comNamesCount));
            comName = maxMap(comNamesCount);
            System.out.println(comNamesCount);
            return comName;
        } else {

            String comName;
            List<String> comNames = new ArrayList<>();
            Set<String> comNamesSet;
            Annotation annotation = document.getDocument();
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            for (int i = 0; i < sentences.size(); i++) {
                for (CoreLabel token : sentences.get(i).get(CoreAnnotations.TokensAnnotation.class)) {
                    String pos = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    if (pos.equals("ORGANIZATION")&&(!ignoreList.contains(word.toLowerCase()))) {
                        comNames.add(token.get(CoreAnnotations.TextAnnotation.class));
                    }
                }
            }

//        System.out.println(comNames);
            comNamesSet = new HashSet<>(comNames);
            Map<String, Integer> comNamesCount = new HashMap<>();
            for (String com : comNamesSet) {
                int occurrences = Collections.frequency(comNames, com);
                comNamesCount.put(com, occurrences);
            }
            System.out.println("The company is: " + maxMap(comNamesCount));
            comName = maxMap(comNamesCount);
            System.out.println(comNamesCount);
            return comName;
        }

    }

    public static String maxMap(Map<String,Integer> map){
        String com = "";
        Integer counts = 0;
        for (String company: map.keySet()){
            if (counts < map.get(company)) {
                com = company;
                counts = map.get(company);
            }
        }
        return com;
    }


}
