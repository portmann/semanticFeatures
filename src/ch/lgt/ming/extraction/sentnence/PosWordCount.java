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
public class PosWordCount{

    private static FileHandler fileHandler = new FileHandler();
    private static StringId positiveWords = new StringId();


    /**
     * This function extracts the number of positive words of the document according to L&MPos.txt
     *
     * @param document the annotation of the document
     *
     * @return number of negative words
     * */
    public static int extract(Annotation document) throws Exception {

        positiveWords.setMap(fileHandler.loadFileToMap("data/dictionaries/L&MPos.txt", true));

        int counts = 0;
        for (CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class)){
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            if (positiveWords.getMap().containsKey(word)) counts++;
        }
        return counts;

    }

}
