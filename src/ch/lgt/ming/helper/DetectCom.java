package ch.lgt.ming.helper;

import ch.lgt.ming.cleanup.Document;
import com.sun.deploy.util.StringUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import javax.print.Doc;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Ming Deng on 10/7/2016.
 */
public class DetectCom {


    public static void main(String[] args) {

        String inputPath = "data/Empirical_Analysis/Seeking_AlphaSer";
        String outputPath = "data/Empirical_Analysis/Seeking_AlphaSer_Company/Apple";
        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();

        FileInputStream fileInputStream;


        for (int i = 1; i < listOfFiles.length; i++) {

            try {
                fileInputStream = new FileInputStream(inputPath + "/" + listOfFiles[i].getName());
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Document document = (Document) objectInputStream.readObject();
                String comName = detectCom(document, false);
                System.out.println(comName);
                if (comName.equalsIgnoreCase("apple")||comName.equalsIgnoreCase("aapl")) {
                    Files.copy(Paths.get(inputPath + "/" + listOfFiles[i].getName()),
                            Paths.get(outputPath + "/" + listOfFiles[i].getName()));
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

//        String inputPath = "data/Empirical_Analysis/Seeking_AlphaSer";
//
//        File folder = new File(inputPath);
//        File[] listOfFiles = folder.listFiles();
//
//        FileInputStream fileInputStream;
//
//        List<String> companies = Arrays.asList("amazon", "boeing", "delta", "facebook", "ford",
//                "goldman", "google", "intel", "microsoft", "netflix");
//        List<String> Folders = Arrays.asList("Amazon", "Boeing", "Delta_Airline", "Facebook", "Ford",
//                "Goldman_Sachs", "Google", "Intel", "Microsoft", "Netflix");
//
//
//        for (int i = 1; i < listOfFiles.length; i++) {
//
//            try {
//                fileInputStream = new FileInputStream(inputPath + "/" + listOfFiles[i].getName());
//                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//                Document document = (Document) objectInputStream.readObject();
//                String comName = detectCom(document, false);
//                System.out.println(comName);
//                for (int j = 1; j < companies.size(); j++) {
//                    String outputPath = "data/Empirical_Analysis/Seeking_AlphaSer_Company/" + Folders.get(j);
//                    if (comName.equalsIgnoreCase(companies.get(j))) {
//                        Files.copy(Paths.get(inputPath + "/" + listOfFiles[i].getName()),
//                                Paths.get(outputPath + "/" + listOfFiles[i].getName()));
//                    }
//                }
//                System.out.printf("%d is done\n", document.getIndex());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

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
