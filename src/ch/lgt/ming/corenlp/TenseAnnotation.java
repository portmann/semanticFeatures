package ch.lgt.ming.corenlp;

import edu.stanford.nlp.ling.CoreAnnotation;


/**
 * Created by Ming Deng on 7/28/2016.
 */
public class TenseAnnotation implements CoreAnnotation<String> {

    public Class<String> getType() {
        return String.class;
    }

}
