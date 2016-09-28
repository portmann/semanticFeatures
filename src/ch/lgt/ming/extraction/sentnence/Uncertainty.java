package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdBoolean;
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


    public static List<Integer> extract(Annotation document, String Reg){

        UncertaintyFeature uncertainty = new UncertaintyFeature();
        List<Integer> counts = Arrays.asList(0,0,0,0,0,0);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (int i = 0; i < sentences.size(); i++) {
            List<Integer> counts2 = uncertainty.Uncertainty(sentences.get(i), Reg);
            for (int j = 0; j < 6; j++){
                counts.set(j, counts.get(j) + counts2.get(j));
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

    public static List<Integer> extractConditionality(Annotation document, String Reg) throws Exception {

        UncertaintyFeature uncertainty = new UncertaintyFeature();
        List<Integer> counts = new ArrayList<>();
        int conditionality = 0;
        int conditionality_pos = 0;
        int conditionality_neg = 0;

        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){

            List<Integer> counts2 = uncertainty.UncertaintyConditionality(sentence, Reg);
            conditionality += counts2.get(0);
            conditionality_pos += counts2.get(1);
            conditionality_neg += counts2.get(2);


        }

        counts.add(0, conditionality);
        counts.add(1, conditionality_pos);
        counts.add(2, conditionality_neg);
        return counts;
    }

}
