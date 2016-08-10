package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.feature.surprise;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Surprice implements Extractor<IdBoolean>{


    @Override
    public IdBoolean extract(Annotation document) {
        int sentenceIndex = 0;
        IdBoolean SentID_Surprice = new IdBoolean();
        for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){

            boolean isSurprice = surprise.IsSurprise1(sentence)|surprise.IsSurprise2(sentence);
            SentID_Surprice.putValue(sentenceIndex, isSurprice);
            sentenceIndex++;

        }
        return SentID_Surprice;
    }
}
