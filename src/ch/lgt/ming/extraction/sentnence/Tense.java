package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdString;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Ming Deng on 5/1/2016.
 */
public class Tense implements Extractor<IdString>{

    private Map<String,List<String>> tense = new HashMap<>();

    public Tense(){
        List<String> present = new ArrayList<>();
        present.add("VB");
        present.add("VBP");
        present.add("VBZ");
        present.add("VBG");
        present.add("MD");
        List<String> past = new ArrayList<>();
        past.add("VBD");
        past.add("VBN");
        List<String> future = new ArrayList<>();
        future.add("will");
        future.add("'ll");
        future.add("wo");
        tense.put("Present", present);
        tense.put("Past", past);
        tense.put("Future", future);
        List<String> verb = new ArrayList<>();
        verb.add("VP");
        verb.add("SQ");
        tense.put("Verb",verb);

    }

    @Override
    public IdString extract(Annotation document){

        IdString sentID_Tense = new IdString();
        int sentenceIndex = 0;
        String senttense = "Not defined";
        for(CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){

            Tree tree = sentence.get(TreeAnnotation.class); //ROOT
            Tree child = tree.children()[0];                //S
            Tree[] grandchildren = child.children();        //VP,NP,.
            for (Tree grandchild: grandchildren){

                if (tense.get("Verb").contains(grandchild.value())) {

                    for (Tree greatgrandchild: grandchild.children()){

                        if(tense.get("Present").contains(greatgrandchild.value())) {

                            senttense = "Present";
                            break;
                        }
                        else if (tense.get("Past").contains(greatgrandchild.value())){

                            senttense = "Past";
                            break;
                        }
                        else {
                            for (Word word: greatgrandchild.yieldWords()){

                                if (tense.get("Future").contains(word.value())){

                                    senttense = "Future";
                                    break;
                                }

                            }
                        }
                    }

                    break;
                }
            }
            sentID_Tense.putValue(sentenceIndex, senttense);
            sentenceIndex++;

        }

        return sentID_Tense;
    }
}
