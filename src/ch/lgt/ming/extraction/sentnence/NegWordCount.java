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
public class NegWordCount{

    private static FileHandler fileHandler = new FileHandler();
    private static StringId negativeWords = new StringId();


    //Input: Annotation of the text
    //Output: Number of Negative Words in each Sentence
    public static int extract(Annotation document) throws Exception {

        negativeWords.setMap(fileHandler.loadFileToMap("dictionaries/L&MNeg.txt", true));

        int counts = 0;
        for (CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class)){
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            if (negativeWords.getMap().containsKey(word)) counts++;
        }
        return counts;
    }


}
