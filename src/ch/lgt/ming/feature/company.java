package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming Deng on 5/9/2016.
 */
public class company {

    private boolean mark = false;
    private List<String> com = new ArrayList<>();   //An ArrayList of company names of the sentence.

    public List<String> extract(CoreMap sentence) {

        String comname = "";

        for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

            String word = token.get(CoreAnnotations.TextAnnotation.class);
            String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

            if (ner.equals("ORGANIZATION")) {

                if (mark){
                    comname = comname + " " + word;
                }else{
                    comname = word;
                }
                //System.out.println(word);
                mark = true;

            }
            else if (mark) {
                com.add(comname);
                comname = "";
                mark = false;
            }
        }
        return com;
    }
}
