package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdValue;
import ch.lgt.ming.datastore.StringId;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by Ming Deng on 5/1/2016.
 */
public class NegWordCount implements Extractor<IdValue> {

    private FileHandler fileHandler = new FileHandler();
    private StringId negativeWords = new StringId();

    public NegWordCount() throws Exception {
        // load positive words
        this.negativeWords.setMap(fileHandler.loadFileToMap("dictionaries/HARVNeg.txt", true));
    }

    @Override
    //Input: Annotation of the text
    //Output: Number of Negative Words in each Sentence
    public IdValue extract(Annotation document){

        IdValue sentenceNeg = new IdValue();
        int sentenceIndex = 0;
        for(CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){

            double SentenceCountNeg = 0;
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){

                String word = token.get(CoreAnnotations.TextAnnotation.class);
                if (negativeWords.getMap().containsKey(word)) SentenceCountNeg++;

            }
            sentenceNeg.putValue(sentenceIndex, SentenceCountNeg);
            sentenceIndex++;

        }
        return sentenceNeg;
    }
}
