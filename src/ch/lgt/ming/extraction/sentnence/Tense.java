package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.feature.tense;
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
    private tense tense = new tense();

    @Override
    public IdString extract(Annotation document){

        int sentenceindex = 0;
        IdString sentID_Tense = new IdString();
        for(CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)){

            tense.setTree(sentence);
            String senttense = tense.getTense();
            sentID_Tense.putValue(sentenceindex,senttense);
            sentenceindex++;

        }
        return sentID_Tense;

    }
}
