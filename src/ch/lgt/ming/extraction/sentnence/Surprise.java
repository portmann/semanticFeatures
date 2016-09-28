package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdListId;
import ch.lgt.ming.feature.SurpriseFeature;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Surprise {

    public static void main(String[] args) throws IOException {
        Corpus corpus = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream("data/corpus4/Amazon100.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			corpus = (Corpus) objectInputStream.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//
//        FileHandler fileHandler = new FileHandler();
//        StanfordCore.init();
//        Annotation annotation =  StanfordCore.pipeline.process(fileHandler.loadFileToString("data/corpus4/Amazon/573.html"));
//        Surprise.extract(annotation, "$SURPRISE", 100, "amazon");
//        Surprise.extract(annotation, "$SURPRISE");

//        for (int i = 0; i < corpus.getDocCount(); i++){
//            System.out.printf("-------------------------------------Document %d ---------------------------------\n",i);
//            Surprise.extract(corpus.getDocuments().get(i).getDocument(), "$SURPRISE", 5, "amazon");
//            Surprise.extract(corpus.getDocuments().get(i).getDocument(), "$SURPRISE");

//        }
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
     * @param com company name to be detected
     * @param threshold sentence distance threshold
     *
     * @return a list of integer of length 6, represent the counts of "noun_pos","noun_neg",
     *          "verb_pos","verb_neg","othertype_pos","othertype_neg" of the document.
     *
     * */

    public static List<Integer> extract(Annotation document, String reg, String com, Integer threshold) {

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        SurpriseFeature surprise = new SurpriseFeature();

        List<Integer> counts = Arrays.asList(0,0,0,0,0,0);
        IdListId SentId_counts = new IdListId();
        List<Boolean> SentId_isCom = new ArrayList<>();

        for (int i = 0; i < sentences.size(); i++) {
            List<Integer> counts2 = surprise.Surprise(sentences.get(i), reg);
            String sentenceText = sentences.get(i).get(CoreAnnotations.TextAnnotation.class);
            SentId_counts.putValue(i, counts2);
            SentId_isCom.add(i, sentenceText.toLowerCase().contains(com));
        }

//        System.out.println(SentId_isCom);
        for (int i = 0; i < sentences.size(); i++) {
            Boolean b = true;
            for (int j = Math.max(0,i-threshold); j < Math.min(sentences.size(), i+threshold+1); j ++){
                b = b || SentId_isCom.get(j);
            }
            if (b){
                for (int j = 0; j < 6; j++){
                    counts.set(j, counts.get(j) + SentId_counts.getValue(i).get(j));
                }
            }
//            System.out.println(
//                    "Sentence" + i + ": " +
//                            "noun_pos" + "," + counts.get(0) + "," +
//                            "noun_neg" + "," + counts.get(1) + "," +
//                            "verb_pos" + "," + counts.get(2) + "," +
//                            "verb_neg" + "," + counts.get(3) + "," +
//                            "othertype_pos" + "," + counts.get(4) + "," +
//                            "othertype_neg" + "," + counts.get(5));
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
