package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdValue;
import ch.lgt.ming.datastore.StringId;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by Ming Deng on 4/30/2016.
 */
public class PosWordCount implements Extractor<IdValue>{

    private FileHandler fileHandler = new FileHandler();
    private StringId positiveWords = new StringId();

    public PosWordCount() throws Exception {
        // load positive words
        this.positiveWords.setMap(fileHandler.loadFileToMap("dictionaries/L&MPos.txt", true));
    }

    @Override
    //Input: Annotation of the text
    //Output: Number of Positive Words in each Sentence
    public IdValue extract(Annotation document){

        IdValue sentencePos = new IdValue();
        int sentenceIndex = 0;
        for(CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){

            double SentenceCountPos = 0;
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){

                String word = token.get(CoreAnnotations.TextAnnotation.class);
                if (positiveWords.getMap().containsKey(word)) SentenceCountPos++;

            }
            sentencePos.putValue(sentenceIndex, SentenceCountPos);
       
            sentenceIndex++;

        }
        return sentencePos;
    }

    @Override
    public int extractCounts(Annotation document) {

        int counts = 0;
        for (CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class)){
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            if (positiveWords.getMap().containsKey(word)) counts++;
        }
        return counts;
    }
}
