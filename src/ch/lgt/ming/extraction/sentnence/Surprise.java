package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdInt;
import ch.lgt.ming.feature.surprise;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by Ming Deng on 8/10/2016.
 */
public class Surprise implements Extractor<IdInt>{

    private static surprise surprise = new surprise();


    @Override
    //Input: Annotation of the text
    //Output: SentID_#ofSurprice
    public IdInt extract(Annotation document) {
        int surpriseCount = 0;
        int surprise_comparativeCount = 0;
        int sentenceIndex = 0;
        IdInt SentID_Surprise = new IdInt();
        for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){


            surpriseCount = surprise.SurpriceCount(sentence);
            surprise_comparativeCount = surprise.Surprise_Comparative(sentence);
            SentID_Surprise.putValue(sentenceIndex, surpriseCount+surprise_comparativeCount);
            sentenceIndex++;

        }
        return SentID_Surprise;
    }

    @Override
    //Input: Annotation of the text
    //Output: Number of Surprise in the whole document
    public int extractCounts(Annotation document){

        int surpriseCount = 0;
        int surprise_comparativeCount = 0;
        int count = 0;
        for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){

            surpriseCount = surprise.SurpriceCount(sentence);
            surprise_comparativeCount = surprise.Surprise_Comparative(sentence);
            count += (surpriseCount + surprise_comparativeCount);

        }

        return count;

    }

    public static int extractSurprise_Unspecified(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += surprise.Surprise_Unspecified(sentence);
        }
        return counts;
    }

    public static int extractSurprise_Disappointment(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += surprise.Surprise_Disappointment(sentence);
        }
        return counts;
    }

    public static int extractSurprise_Relief(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += surprise.Surprise_Relief(sentence);
        }
        return counts;
    }

    public static int extractSurprise_Comparative(Annotation document){

        int counts = 0;
        for (CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            counts += surprise.Surprise_Comparative(sentence);
        }
        return counts;
    }


}
