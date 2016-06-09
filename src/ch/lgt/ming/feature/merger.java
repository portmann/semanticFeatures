package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming Deng on 5/9/2016.
 */
public class merger {

    private List<String> merg = new ArrayList<>();
    private List<String> com = new ArrayList<>();
    private boolean mark = false;
    private boolean mark2 = false;


    public merger() {
        merg.add("merge");
        merg.add("merger");
    }

    public boolean IsMerge(CoreMap sentence) {


        String comname = new String();
        for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

            String word = token.get(CoreAnnotations.TextAnnotation.class);
            //System.out.println(word);
            String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            //System.out.println(ner);
            if (merg.contains(word.toLowerCase())) mark = true;
            if (ner.equals("ORGANIZATION")) {

                if (mark2){
                    comname = comname + " " + word;
                }else{
                    comname = word;
                }
                //System.out.println(word);
                mark2 = true;

            }
            else if (mark2) {
                com.add(comname);
                comname = "";
                mark2 = false;
            }

        }
        System.out.println(com);
//        System.out.println(mark);

        return mark;
    }
}
