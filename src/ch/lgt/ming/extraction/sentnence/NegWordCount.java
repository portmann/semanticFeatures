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

   /**
    * This function extracts the number of negative words of the document according to L&MNeg.txt
    *
    * @param document the annotation of the document
    *
    * @return number of negative words
   * */

    public static int extract(Annotation document) throws Exception {

        negativeWords.setMap(fileHandler.loadFileToMap("data/dictionaries/LMNeg.txt", true));

        int counts = 0;
        for (CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class)){
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            if (negativeWords.getMap().containsKey(word)) counts++;
        }
        return counts;
    }


}
