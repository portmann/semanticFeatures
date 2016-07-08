package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

/**
 * Created by Ming Deng on 6/25/2016.
 */
public class surprise {

    public boolean IsSurprise(CoreMap sentence){

        Boolean result = false;
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        for (CoreLabel token: tokens){

            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (pos.equals("JJR")){
                result = true;
                break;
            }
        }
        return result;

    }

}
