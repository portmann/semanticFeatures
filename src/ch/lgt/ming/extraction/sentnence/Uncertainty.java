package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdListInt;
import ch.lgt.ming.feature.UncertaintyFeature;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Uncertainty {

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

    public static List<Integer> extract(Annotation document, String reg){

        UncertaintyFeature uncertainty = new UncertaintyFeature();
        List<Integer> counts = Arrays.asList(0,0,0,0,0,0);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (int i = 0; i < sentences.size(); i++) {
            List<Integer> counts2 = uncertainty.Uncertainty(sentences.get(i), reg);
            for (int j = 0; j < 6; j++){
                counts.set(j, counts.get(j) + counts2.get(j));
            }
//            System.out.println(
//                "Sentence" + i + ": " +
//                    "noun_pos" + "," + counts.get(0) + "," +
//                    "noun_neg" + "," + counts.get(1) + "," +
//                    "verb_pos" + "," + counts.get(2) + "," +
//                    "verb_neg" + "," + counts.get(3) + "," +
//                    "othertype_pos" + "," + counts.get(4) + "," +
//                    "othertype_neg" + "," + counts.get(5)
//            );
        }
        return counts;
    }

    /**
     * This function extract the counts of specific sentiments from a document.
     *
     * @param document the annotation of the text
     * @param reg regular expression of uncertainty sentiment
     * @param company company name to be detected
     * @param threshold sentence distance threshold
     *
     * @return a list of integer of length 6, represent the counts of "noun_pos","noun_neg",
     *          "verb_pos","verb_neg","othertype_pos","othertype_neg" of the document.
     *
     * */

    public static List<Integer> extract(Annotation document, String reg, String company, int threshold){

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        UncertaintyFeature uncertainty = new UncertaintyFeature();

        List<Integer> counts = Arrays.asList(0,0,0,0,0,0);
        IdListInt SentId_counts = new IdListInt();
        List<Boolean> SentId_isCom = new ArrayList<>();

        for (int i = 0; i < sentences.size(); i++) {

            List<Integer> counts2 = uncertainty.Uncertainty(sentences.get(i), reg);
            SentId_counts.putValue(i, counts2);                                                 //Store the uncertainty counts of every sentence

            String sentenceText = sentences.get(i).get(CoreAnnotations.TextAnnotation.class);
            SentId_isCom.add(i, sentenceText.toLowerCase().contains(company));                  //Store the result if the company name appears in the sentence

        }
        for (int i = 0; i < sentences.size(); i++) {
            Boolean b = true;
            for (int j = Math.max(0, i - threshold); j < Math.min(sentences.size(), i + threshold + 1); j++) {
                b = b || SentId_isCom.get(j);
            }
            if (b) {
                for (int j = 0; j < 6; j++) {
                    counts.set(j, counts.get(j) + SentId_counts.getValue(i).get(j));            //Add the uncertainty counts of the sentence to total counts only if
                }                                                                               //it is near a company name
            }
//            System.out.println(
//                "Sentence" + i + ": " +
//                        "noun_pos" + "," + counts.get(0) + "," +
//                        "noun_neg" + "," + counts.get(1) + "," +
//                        "verb_pos" + "," + counts.get(2) + "," +
//                        "verb_neg" + "," + counts.get(3) + "," +
//                        "othertype_pos" + "," + counts.get(4) + "," +
//                        "othertype_neg" + "," + counts.get(5)
//            );
        }
            return counts;
    }

    /**
     * This function extract the counts of specific sentiments from a document, only if the sentiments are within a
     * certain sentence distance of a specified company name.
     *
     * @param document the annotation of the text
     * @param reg regular expression of surprise sentiment
     *
     * @return a list of integer of length 3, represent the counts of "conditionality", "conditionality_pos", "conditionality_neg"
     *
     * */

    public static List<Integer> extractConditionality(Annotation document, String reg) throws Exception {

        UncertaintyFeature uncertainty = new UncertaintyFeature();
        List<Integer> counts =  Arrays.asList(0,0,0);
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)) {
            List<Integer> counts2 = uncertainty.UncertaintyConditionality(sentence, reg);
            for (int i = 0; i < 3; i++) {
                counts.set(i, counts.get(i) + counts2.get(i));
            }
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
     * @return a list of integer of length 3, represent the counts of "conditionality", "conditionality_pos", "conditionality_neg"
     *
     * */

    public static List<Integer> extractConditionality(Annotation document, String reg, String company, int threshold)
            throws Exception {

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        UncertaintyFeature uncertainty = new UncertaintyFeature();

        List<Integer> counts = Arrays.asList(0,0,0);
        IdListInt SentId_counts = new IdListInt();
        List<Boolean> SentId_isCom = new ArrayList<>();

        for (int i = 0; i < sentences.size(); i++){
            List<Integer> counts2 = uncertainty.UncertaintyConditionality(sentences.get(i), reg);
            SentId_counts.putValue(i, counts2);                                                 //Store the uncertainty counts of every sentence

            String sentenceText = sentences.get(i).get(CoreAnnotations.TextAnnotation.class);
            SentId_isCom.add(i, sentenceText.toLowerCase().contains(company));                  //Store the result if the company name appears in the sentence

        }

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
//            System.out.println(
//                "Sentence" + i + ": " +
//                    "conditionality" + "," + counts.get(0) + "," +
//                    "conditionality_pos" + "," + counts.get(1) + "," +
//                    "conditionality_neg" + "," + counts.get(2)
//            );
        }

        return counts;
    }

}
