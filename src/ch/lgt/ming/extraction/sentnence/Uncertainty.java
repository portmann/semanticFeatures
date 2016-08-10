package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.feature.uncertainty;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Uncertainty implements Extractor<IdBoolean>{


    @Override
    public IdBoolean extract(Annotation document) {
        int sentenceIndex = 0;
        IdBoolean SentID_Uncertainty = new IdBoolean();
        for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){

            boolean isuncertainty = uncertainty.IsUncertainty1(sentence)|uncertainty.IsUncertainty2(sentence);
            SentID_Uncertainty.putValue(sentenceIndex, isuncertainty);
            sentenceIndex++;

        }
        return SentID_Uncertainty;
    }
}
