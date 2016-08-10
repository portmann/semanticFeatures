package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.feature.merger;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by Ming Deng on 8/8/2016.
 */
public class Merger implements Extractor<IdBoolean> {

    @Override
    public IdBoolean extract(Annotation document) {

        int sentenceIndex = 0;
        IdBoolean SentID_Merger = new IdBoolean();
        for(CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){

            boolean isMerger = merger.IsMerge(sentence);
            SentID_Merger.putValue(sentenceIndex,isMerger);
            sentenceIndex++;

        }
        return  SentID_Merger;
    }
}
