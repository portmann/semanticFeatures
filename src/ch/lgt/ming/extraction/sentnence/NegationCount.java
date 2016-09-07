package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdValue;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ming Deng on 5/1/2016.
 */
public class NegationCount implements Extractor<IdValue> {
    // Define Set of Negation tokens
    private Set<String> negation = new HashSet<>();

    public NegationCount(){

        negation.add("no");
        negation.add("not");
        negation.add("n't");

    }

    @Override
    //Input: Annotation of the text
    //Output: Number of the Negation in each Sentence
    public IdValue extract(Annotation document){

        IdValue sentID_negationCount = new IdValue();
        int sentenceIndex = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){

            double negationCount = 0;
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){

                String word = token.get(CoreAnnotations.TextAnnotation.class);
                if (negation.contains(word)) negationCount++;

            }
            sentID_negationCount.putValue(sentenceIndex,negationCount);
            sentenceIndex++;

        }
        return sentID_negationCount;
    }

}
