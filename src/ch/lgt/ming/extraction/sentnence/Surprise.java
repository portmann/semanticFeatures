package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.datastore.IdListInt;
import ch.lgt.ming.feature.SurpriseFeature;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Surprise {

    public static void main(String[] args) throws IOException {

//		FileHandler fileHandler = new FileHandler();
		File folder = new File("data/corpus8/Amazon");
		File[] listOfFiles = folder.listFiles();

		FileInputStream fileInputStream ;

		for (int i = 0; i < 100; i++){

			try {
				fileInputStream = new FileInputStream("data/corpus8/Amazon/" + listOfFiles[i].getName());
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				Document document = (Document) objectInputStream.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

//        StanfordCore.init();
//        Annotation annotation =  StanfordCore.pipeline.process(fileHandler.loadFileToString("data/corpus5/Amazon/3312.html"));
//        Surprise.extract(annotation, "$SURPRISE", "amazon", 100);
//        Surprise.extract(annotation, "$SURPRISE");

    }

    /**
     * This function extract the counts of specific sentiments from a document.
     *
     * @param document the annotation of the text
     * @param reg regular expression of surprise sentiment
     *
     * @return a list of integer of length 6, represent the counts of "noun_pos","noun_neg",
     *          "verb_pos","verb_neg","othertype_pos","othertype_neg" of the document.
     *
     * */
    public static List<Integer> extract(Annotation document, String reg) {

        SurpriseFeature surprise = new SurpriseFeature();
        List<Integer> counts = Arrays.asList(0,0,0,0,0,0);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (int i = 0; i < sentences.size(); i++) {
            List<Integer> counts2 = surprise.Surprise(sentences.get(i), reg);
            for (int j = 0; j < 6; j++){
                counts.set(j, counts.get(j) + counts2.get(j));
            }
//            System.out.println(
//                "Sentence" + i + ": " +
//                        "noun_pos" + "," + counts.get(0) + "," +
//                        "noun_neg" + "," + counts.get(1) + "," +
//                        "verb_pos" + "," + counts.get(2) + "," +
//                        "verb_neg" + "," + counts.get(3) + "," +
//                        "othertype_pos" + "," + counts.get(4) + "," +
//                        "othertype_neg" + "," + counts.get(5));
        }
        return counts;
    }

    /**
     * This function extract the counts of specific sentiments from a document, only if the sentiments are within a
     * certain sentence distance of a specified company name.
     *
     * @param document the annotation of the text
     * @param reg regular expression of surprise sentiment
     * @param company company name to be detected
     * @param threshold sentence distance threshold
     *
     * @return a list of integer of length 6, represent the counts of "noun_pos","noun_neg",
     *          "verb_pos","verb_neg","othertype_pos","othertype_neg" of the document.
     *
     * */

    public static List<Integer> extract(Annotation document, String reg, String company, int threshold) {

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        SurpriseFeature surprise = new SurpriseFeature();

        List<Integer> counts = Arrays.asList(0,0,0,0,0,0);
        IdListInt SentId_counts = new IdListInt();
        List<Boolean> SentId_isCom = new ArrayList<>();

        for (int i = 0; i < sentences.size(); i++) {
            List<Integer> counts2 = surprise.Surprise(sentences.get(i), reg);
            SentId_counts.putValue(i, counts2);                                                 //Store the uncertainty counts of every sentence

            String sentenceText = sentences.get(i).get(CoreAnnotations.TextAnnotation.class);
            SentId_isCom.add(i, sentenceText.toLowerCase().contains(company));                  //Store the result if the company name appears in the sentence
        }

//        System.out.println(SentId_isCom);
        for (int i = 0; i < sentences.size(); i++) {
            Boolean b = true;
            for (int j = Math.max(0,i-threshold); j < Math.min(sentences.size(), i+threshold+1); j ++){
                b = b || SentId_isCom.get(j);
            }
            if (b){
                for (int j = 0; j < 6; j++){
                    counts.set(j, counts.get(j) + SentId_counts.getValue(i).get(j));            //Add the uncertainty counts of the sentence to total counts only if
                }                                                                               //it is near a company name
            }
            System.out.println(
                    "Sentence" + i + ": " +
                            "noun_pos" + "," + counts.get(0) + "," +
                            "noun_neg" + "," + counts.get(1) + "," +
                            "verb_pos" + "," + counts.get(2) + "," +
                            "verb_neg" + "," + counts.get(3) + "," +
                            "othertype_pos" + "," + counts.get(4) + "," +
                            "othertype_neg" + "," + counts.get(5));
        }
        return counts;
    }


    /**
     * This function extract the counts of comparative from a document.
     *
     * @param document the annotation of the text
     *
     * @return an integer of the counts value
     *
     * */

    public static int extractComparative(Annotation document) {
        SurpriseFeature surprise = new SurpriseFeature();
        int comparative = 0;
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            comparative += surprise.SurpriseComparative(sentence);
        }
        return comparative;
    }

    /**
     * This function extract the counts of comparative from a document, only if the comparative is within a
     * certain sentence distance of a specified company name.
     *
     * @param document the annotation of the text
     * @param com company name to be detected
     * @param threshold sentence distance threshold
     *
     * @return an integer of the counts value
     *
     * */
    public static int extractComparative(Annotation document, String com, int threshold) {

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        SurpriseFeature surprise = new SurpriseFeature();
        int comparative = 0;
        List<Integer> SentId_counts = new ArrayList<>();
        List<Boolean> SentId_isCom = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++) {

            int comparative2 = surprise.SurpriseComparative(sentences.get(i));
            String sentenceText = sentences.get(i).get(CoreAnnotations.TextAnnotation.class);
            SentId_counts.add(i, comparative2);
            SentId_isCom.add(i, sentenceText.toLowerCase().contains(com));

        }
        for (int i = 0; i < sentences.size(); i++) {
            Boolean b = true;
            for (int j = Math.max(0,i-threshold); j < Math.min(sentences.size(), i+threshold+1); j ++){
                b = b || SentId_isCom.get(j);
            }
            if (b){
                comparative += SentId_counts.get(i);
            }
        }

        return comparative;
    }

}
