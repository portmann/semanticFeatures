package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.feature.UncertaintyFeature;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Uncertainty {

    private static UncertaintyFeature uncertainty = new UncertaintyFeature();

    public static int extractUncertainty_Unspecified(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += uncertainty.Uncertainty_Unspecified_Noun(sentence);
        }
        return counts;
    }

    public static int extractUncertainty_Fear(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += uncertainty.Uncertainty_Fear_Noun(sentence);
        }
        return counts;
    }

    public static int extractUncertainty_Hope(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += uncertainty.Uncertainty_Hope_Noun(sentence);
        }
        return counts;
    }

    public static int extractUncertainty_Anxiety(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += uncertainty.Uncertainty_Anxiety_Noun(sentence);
        }
        return counts;
    }

    public static int extractUncertainty(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += uncertainty.UncertaintyCount(sentence);
        }
        return counts;
    }

    public static int extractUncertainty_Conditionality1(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += uncertainty.Uncertainty_conditionality1(sentence);
        }
        return counts;
    }

    public static int extractUncertainty_Conditionality2(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += uncertainty.Uncertainty_conditionality2(sentence);
        }
        return counts;
    }

}
