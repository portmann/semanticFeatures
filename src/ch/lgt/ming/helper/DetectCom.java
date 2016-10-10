package ch.lgt.ming.helper;

import ch.lgt.ming.cleanup.Document;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import javax.print.Doc;
import java.io.*;
import java.util.*;

/**
 * Created by Ming Deng on 10/7/2016.
 */
public class DetectCom {


    public static void main(String[] args) {
        File folder = new File("data/corpus7");
        File[] listOfFiles = folder.listFiles();

        FileInputStream fileInputStream;

        List<String> companies = Arrays.asList("amazon", "boeing", "delta", "facebook", "ford",
                "goldman", "google", "intel", "microsoft", "netflix");
        Map<String,List<Integer>> comIndex = new HashMap<>();

        for (int i = 0; i < companies.size(); i++){

            try {
                fileInputStream = new FileInputStream("data/corpus7" + listOfFiles[i].getName());
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Document document = (Document) objectInputStream.readObject();
                Annotation annotation = document.getDocument();
                detectCom(annotation);

                System.out.printf("%d is done\n",document.getIndex());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    public static String detectCom(Annotation annotation){

        String comName = "";
        List<String> comNames = new ArrayList<>();
        Set<String> comNamesSet = new HashSet<>();
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (int i = 0; i < sentences.size(); i++){
            for (CoreLabel token: sentences.get(i).get(CoreAnnotations.TokensAnnotation.class)){
                String pos = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if (pos.equals("ORGANIZATION")){
                    comNames.add(token.get(CoreAnnotations.TextAnnotation.class));
                }
            }
        }

//        System.out.println(comNames);
        comNamesSet = new HashSet<>(comNames);
        Map<String,Integer> comNamesCount = new HashMap<>();
        for (String com: comNamesSet){
            int occurrences = Collections.frequency(comNames, com);
            comNamesCount.put(com,occurrences);
        }
        System.out.println("The company is: "+  maxMap(comNamesCount));
        comName = maxMap(comNamesCount);
        System.out.println(comNamesCount);
    return comName;
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
