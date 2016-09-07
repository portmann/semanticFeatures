package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.feature.SurpriseFeature;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Surprise {

    public static List<Integer> extract(Annotation document, String Reg) {

        SurpriseFeature surprise = new SurpriseFeature();
        List<Integer> counts = new ArrayList<>();
        int noun_pos = 0;
        int noun_neg = 0;
        int verb_pos = 0;
        int verb_neg = 0;
        int othertype_pos = 0;
        int othertype_neg = 0;

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (int i = 0; i < sentences.size(); i++) {
            List<Integer> counts2 = surprise.Surprise(sentences.get(i), Reg);
            noun_pos += counts2.get(0);
            noun_neg += counts2.get(1);
            verb_pos += counts2.get(2);
            verb_neg += counts2.get(3);
            othertype_pos += counts2.get(4);
            othertype_neg += counts2.get(5);
            System.out.println(
                    "Sentence" + i + ": " +
                    noun_pos + "," +
                    noun_neg + "," +
                    verb_pos + "," +
                    verb_neg + "," +
                    othertype_pos + "," +
                    othertype_neg);
        }
        counts.add(0, noun_pos);
        counts.add(1, noun_neg);
        counts.add(2, verb_pos);
        counts.add(3, verb_neg);
        counts.add(4, othertype_pos);
        counts.add(5, othertype_neg);
        return counts;
    }

    public static int extractComparative(Annotation document) {
        SurpriseFeature surprise = new SurpriseFeature();
        int comparative = 0;

        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            comparative += surprise.SurpriseComparative(sentence);
        }
        return comparative;
    }

}
