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
    private String senttense = "Not Defined.";

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
        List<String> pastModal = new ArrayList<>();
        pastModal.add("could");
        pastModal.add("would");
        List<String> futureModal = new ArrayList<>();
        futureModal.add("will");
        futureModal.add("'ll");
        futureModal.add("wo");
        tense.put("Present", present);
        tense.put("Past", past);
        tense.put("PastModal", pastModal);
        tense.put("FutureModal", futureModal);
        List<String> verb = new ArrayList<>();
        verb.add("VP");
        verb.add("SQ");
        verb.add("FRAG");
        tense.put("Verb",verb);
        List<String> hasverb = new ArrayList<>();
        hasverb.add("S");
        hasverb.add("X");
        hasverb.add("FRAG");
        hasverb.add("SBAR");
        hasverb.add("PRN");
        hasverb.add("VP");
        tense.put("Hasverb",hasverb);
    }

    public void goThroughTree(Tree tree){

        //PrintWriter output = new PrintWriter(System.out);
        //tree.pennPrint(output);


        //System.out.println("---------------------------------------------");
        //System.out.println("Verb is in: " + tree.value());
        //System.out.println("---------------------------------------------");

        for (Tree child: tree.children()) {
           // System.out.println(child.value());
            if (tense.get("Present").contains(child.value())) {
                System.out.println("The POS[verb] is: " + child.value() + child.yieldWords().toString());
                senttense = "Present";
                if (child.value().equals("MD"))
                {
                    //System.out.println(child.yieldWords().get(0).value());
                    if (tense.get("PastModal").contains(child.yieldWords().get(0).value().toLowerCase()))  senttense = "Past";
                    else if (tense.get("FutureModal").contains(child.yieldWords().get(0).value().toLowerCase()))  senttense = "Future";
                }
                break;
            } else if (tense.get("Past").contains(child.value())) {
                System.out.println("The POS[verb] is: " + child.value()+ child.yieldWords().toString());
                senttense = "Past";
                break;
            }
        }

    }


    public String getTense(Tree senttree){
        boolean mark = false;
        for (Tree child: senttree.children()){

            //System.out.println(child.value());


            if (tense.get("Verb").contains(child.value())){
                //System.out.println("--------------Verb is here!--------------------------");
                //PrintWriter output = new PrintWriter(System.out);
                //child.pennPrint(output);
                goThroughTree(child);
                if (!senttense.equals("Not Defined."))
                {
                    break;
                }
            }

            if (tense.get("Hasverb").contains(child.value())){
               //System.out.println("--------------Hasverb is here!--------------------------");
               // PrintWriter output = new PrintWriter(System.out);
               // child.pennPrint(output);
                senttree = child;
                mark = true;
                break;
            }
        }
        if (senttense.equals("Not Defined.")&&(!senttree.isLeaf())&& mark){
            getTense(senttree);
        }

        return senttense;
    }


    @Override
    public IdString extract(Annotation document){


        IdString sentID_Tense = new IdString();
        int sentenceIndex = 0;
        for(CoreMap sentence:document.get(CoreAnnotations.SentencesAnnotation.class)){
            Tree senttree = sentence.get(TreeAnnotation.class);
            senttense = getTense(senttree.children()[0]);
            if (senttense.equals("Not Defined.")) System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            sentID_Tense.putValue(sentenceIndex, senttense);
            sentenceIndex++;
            senttense = "Not Defined.";

        }
        return sentID_Tense;
    }
}
