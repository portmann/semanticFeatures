package ch.lgt.ming.feature;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by Ming Deng on 5/9/2016.
 */
public class tense {

    private Map<String, List<String>> tense = new HashMap<>();
    private Tree tree;
    private String senttense = "NULL.";
    private Map<String, String> dep_word = new HashMap<>();
    private List<String> typedDep = new ArrayList<>();

    public tense(CoreMap sentence) {
        this.tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
        typedDep.add("aux");
        typedDep.add("auxpass");
        typedDep.add("root");
        typedDep.add("cop");
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
        verb.add("aux");
        verb.add("cop");
        verb.add("auxpass");
        tense.put("Verb", verb);


    }

    //Input: tree with nodes(NP,VP)
    //Output: the tense of the sentence
    //Find the tense of current layer

    public Map<String, String> getVerb(Tree tree) {

        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
        Collection<TypedDependency> td = gs.typedDependenciesCollapsed();

        for (TypedDependency td1 : td) {
//            System.out.println("----------------------------------------Begin-----------------------------------------");
            System.out.println(td1);
//            System.out.println(td1.);
//            System.out.println("-------------------------------------Relation-----------------------------------------");
            GrammaticalRelation relation = td1.reln();
            if (typedDep.contains(relation.getShortName())) {
                System.out.printf(td1.dep() + "----------------------------Found %s----------------------------\n",
                        relation.getShortName());
                dep_word.put(relation.getShortName(), td1.dep().value());
                dep_word.put(relation.getShortName() + "Tag", td1.dep().tag());
            }
//            System.out.println("relation.getShortName(): " + relation.getShortName());
//            System.out.println("relation.getLongName(): " + relation.getLongName());
//            System.out.println("relation.toString(): " + relation.toString());
//            System.out.println("relation.toPrettyString(): " + relation.toPrettyString());
//            System.out.println("------------------------------------Governor-----------------------------------------");
//            IndexedWord governor = td1.gov();
//            System.out.println(governor);
//            System.out.println(governor.value());
//            System.out.println(governor.tag());
//            System.out.println("------------------------------------Dependent-----------------------------------------");
//            IndexedWord dependent = td1.dep();
//            System.out.println(dependent);
//            System.out.println(dependent.value());
//            System.out.println(dependent.tag());
//            System.out.println("----------------------------------------End-------------------------------------------");

        }
        return dep_word;
    }

    //    public Map<String,String> getTense(){
    public String getTense() {

        Tree senttree = this.tree;
        Map<String, String> Verb = getVerb(senttree);
        System.out.println(Verb);
        if (Verb.containsKey("aux")) {
            switch (Verb.get("auxTag")) {
                case "MD": {
                    switch (Verb.get("aux")) {
                        case "could":
                        case "would":
                            senttense = "Past";
                            break;
                        case "can":
                            senttense = "Present";
                            break;
                        case "will":
                            senttense = "Future";
                            break;
                    }
                    break;
                }
                case "VB":
                case "VBP":
                case "VBZ":
                case "VBG":
                    senttense = "Present";
                    break;
                case "VBD":
                    senttense = "Past";
                    break;
                default:
                    senttense = "NULL";
            }
        } else if (Verb.containsKey("auxpass")) {
            switch (Verb.get("auxpassTag")) {
                case "VB":
                case "VBP":
                case "VBZ":
                case "VBG":
                    senttense = "Present";
                    break;
                case "VBD":
                    senttense = "Past";
                    break;
                default:
                    senttense = "NULL";
            }
        } else if (Verb.containsKey("cop")) {
            switch (Verb.get("copTag")) {
                case "VB":
                case "VBP":
                case "VBZ":
                case "VBG":
                    senttense = "Present";
                    break;
                case "VBD":
                    senttense = "Past";
                    break;
                default:
                    senttense = "NULL";
            }
        } else if (Verb.containsKey("root")) {
            switch (Verb.get("rootTag")) {
                case "VB":
                case "VBP":
                case "VBZ":
                case "VBG":
                    senttense = "Present";
                    break;
                case "VBD":
                    senttense = "Past";
                    break;
                default:
                    senttense = "NULL";
            }
        }


//        System.out.println(senttense);
        return senttense;

    }
}
