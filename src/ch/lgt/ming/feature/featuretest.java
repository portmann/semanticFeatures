package ch.lgt.ming.feature;

import ch.lgt.ming.feature.company;
import ch.lgt.ming.feature.merger;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Ming Deng on 6/9/2016.
 */
public class featuretest {

    public static void main(String[] args) throws IOException {

        System.out.println("--------------------------------------- Pipeline ------------------------------------------");
        Properties props = new Properties();
        props.setProperty("customAnnotatorClass.tense", "ch.lgt.ming.feature.TenseAnnotator");
//        props.setProperty("tense.key", "tense");
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, tense");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

/**
 * Different sentences to test company and merger
 */

//        String[] myString = {
//                "London Stock Exchange shareholders are to meet on Monday over whether to approve the merger " +
//                        "with Deutsche Börse amid uncertainty created by Britain’s decision to leave the EU.",
//                "German watchdog casts doubt on London Stock Exchange merger after Brexit",
//                "Lloyds TSB and HBOS will have a merger.",
//                "There is a merger between Lloyds TSB and HBOS.",
//                "There was a merger related with Lloyds TSB."
//        };  //sentences to test merger
//        String[] myString = {
//                "Sega Games announced today it has acquired Paris-based Amplitude Studios, saying the move will " +
//                        "strengthen its roster of PC games and development in Europe.",
//                "The acquisition of Amplitude Studios marks another purchase of a studio " +
//                        "that specializes in strategy games for the publisher.",
//                "Maersk might take over Hyundai Merchant Marine if the troubled Korean shipping company gets on its feet again."
//        };  //sentences to test acquisition
//        String[] myString = {
//
//                "If he did not decide to raise rates, markets would relax."
//
//        };

//        String[] myString = {
//                "I am so surprised about the news.",
//                "The 'Amazing' IPO Change That May Restart The Flow Of New Stocks.",
//                "Opinion: CFOs want a stunning 14% annual return on investments — and that’s holding back the economy",
//                "Gap shares slump as July sales disappoint, but analysts upbeat."
//        };//Strings to test surprise

//        String[] myString = {
//                "The expression is really vague.",
//                "Christopher Bishop, Chief Reinvention Officer of Improvising Careers said " +
//                        "it’s important to keep an eye on the “little things” of any war gaming " +
//                        "exercise like making sure teams are comfortable with ambiguity, being " +
//                        "resourceful, resilient, creative and passionate.",
//                "“We’ve had a pretty anomalously hot and dry stretch,” says Randy Graham, " +
//                        "meteorologist-in-charge at the Weather Services’ Salt Lake City office."
//        };//Strings to test uncertainty

        byte[] encoded = Files.readAllBytes(Paths.get("dictionaries/NERtest.txt"));
        String[] myString = new String[1];
        myString[0]= new String(encoded, StandardCharsets.UTF_8);


        for (int i = 0; i < myString.length; i++) {
            Annotation document = new Annotation(myString[i]);
//        Annotation document = new Annotation("The outcome is higher than expected.");
//        Annotation document = new Annotation( "If he did not decide to raise rates markets would relax.");
/**
 * Different sentences to test tense
 */
//            System.out.println("-------------------------------aux-----------------------------------");
//            Annotation document = new Annotation("I don't like animals.");
//            Annotation document = new Annotation("I am fine.");
//            Annotation document = new Annotation("I was happy.");
//            Annotation document = new Annotation("I will be happy.");
//            Annotation document = new Annotation("I have done that before.");
//            Annotation document = new Annotation("How do I use Stanford Parser's Typed Dependencies in Python?");
//            Annotation document = new Annotation("What flights did you have from Burbank to Tacoma Washington?");
//            Annotation document = new Annotation("What flights do you have from Burbank to Tacoma Washington?");
//            Annotation document = new Annotation("What flights will you have from Burbank to Tacoma Washington?");//
//            System.out.println("-------------------------------auxpass-----------------------------------");
//            Annotation document = new Annotation("I was found by him.");
//            System.out.println("------------------ Copula -----------------------------------");
//            Annotation document = new Annotation("Bill was an honest man.");
//            System.out.println("------------------ Root -----------------------------------");
//            Annotation document = new Annotation("I like animals.");
            pipeline.annotate(document);
            System.out.printf("-------------------------------------Sentence %d ---------------------------------\n",i);
            System.out.println("============================= Sentence Related Commands ==================================");
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            System.out.println("sentences: " + sentences + "||" + sentences.getClass());        //List<CoreMap>
            CoreMap sentence = sentences.get(0);
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){
                String ner = token.ner();
                System.out.println(ner);
            }
//        System.out.println("sentence: " + sentence + "||" + sentence.getClass());          //CoreMap
//        System.out.println("sentence.toShorterString(): " + sentence.toShorterString() + "||" + sentence.toShorterString().getClass());
//        String sentenceText = sentence.get(CoreAnnotations.TextAnnotation.class);
//        System.out.println("sentenceText: " + sentenceText + "||" + sentenceText.getClass());

//        System.out.println("============================= Token Related Commands ==================================");
//        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
//        CoreLabel token = tokens.get(0);
//        System.out.println("token: " + token + "||" + token.getClass());
//        System.out.println("token.toShorterString(): " + token.toShorterString() + "||" + token.toShorterString().getClass());
//
//        String word = token.get(CoreAnnotations.TextAnnotation.class);    //Text
//        System.out.println("word: " + word + "||" + word.getClass());
//        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);         //POS
//        System.out.println("pos: " + pos + "||" + pos.getClass());
//        Integer tokenindex = token.get(CoreAnnotations.IndexAnnotation.class);            //Index
//        System.out.println(tokenindex);
//            String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);      //NER
//            System.out.println(ner);
//        String tense = token.get(TenseAnnotation.class);       //Tense
//        System.out.println("tense: " + tense);

//            System.out.println("=============================== Tree Related Commands =================================");
//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//        tree.pennPrint();
//
//        System.out.println("tree.value: " + tree.value() + "||" + tree.getClass());
//        System.out.println("tree.depth: " + tree.depth());
//        System.out.println("tree.constituents: " + tree.constituents());
//        System.out.println("tree.taggedYield: " + tree.taggedYield());
//        System.out.println("tree.isLeaf: " + tree.isLeaf());
//        System.out.println("tree.toString: " + tree.toString());
//        System.out.println("tree.nodeString: " + tree.nodeString());

//        System.out.println("-------------------------------- TypedDependency ---------------------------------------");
//        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
//        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
//        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
//        Collection<TypedDependency> td = gs.typedDependenciesCollapsed();
//        System.out.println("td: " + td + "||" + td.getClass());
//
//        for (TypedDependency td1: td){
//            System.out.println("----------------------------------------Begin-----------------------------------------");
//            System.out.println(td1);
//            System.out.println("-------------------------------------Relation-----------------------------------------");
//            GrammaticalRelation relation = td1.reln();
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
//            if (relation.getShortName().equals("aux"))
//                System.out.println(dependent + ".................!!!!!........................");
//            System.out.println("----------------------------------------End-------------------------------------------");
//        }

//        System.out.println("--------------------------------- tree.taggedLabeledYield --------------------------------");
//        System.out.println("tree.taggedLabeledYield: " + tree.taggedLabeledYield());
//        System.out.println("tree.taggedLabeledYield().get(1): " + tree.taggedLabeledYield().get(1));
//        System.out.println(tree.taggedLabeledYield().get(1).getClass());
//        System.out.println(tree.taggedLabeledYield().get(1).get(CoreAnnotations.PartOfSpeechAnnotation.class));
//        System.out.println(tree.taggedLabeledYield().get(1).get(CoreAnnotations.TextAnnotation.class));
//        System.out.println(tree.taggedLabeledYield().get(1).get(CoreAnnotations.IndexAnnotation.class));
//        System.out.println("------------------------------------ tree.yield ------------------------------------------");
//        System.out.println("tree.yield(): " + tree.yield() + "||" + tree.yield().getClass());
//        System.out.println("tree.yield().size(): " + tree.yield().size());
//        System.out.println("tree.yield().contains(tree.yield().get(0)): " + tree.yield().contains(tree.yield().get(0)));
//
//        System.out.println(".....................................tree.yield.toString..................................");
//        System.out.println("tree.yield().toString(): " + tree.yield().toString() + "||" + tree.yield().toString().getClass());
//        System.out.println("tree.yield().toString().charAt(0): " + tree.yield().toString().charAt(0));
//        System.out.println("tree.yield().toString().length(): " + tree.yield().toString().length());
//
//        System.out.println("tree.yield().get(0): " + tree.yield().get(0) + "||" + tree.yield().get(0).getClass());
//        System.out.println("tree.yield().get(0).toString(): " + tree.yield().get(0).toString() +"||"+ tree.yield().get(0).toString().getClass());
//        System.out.println("tree.yield().get(0).value(): " + tree.yield().get(0).value() + "||" + tree.yield().get(0).value().getClass());
//        System.out.println("-------------------------------- tree.yieldWords -----------------------------------------");
//        System.out.println("tree.yieldWords: " + tree.yieldWords() + "||" + tree.yieldWords().getClass());
//        System.out.println("tree.yieldWords().toString(): " + tree.yieldWords().toString() + "||"+ tree.yieldWords().toString().getClass());
//        System.out.println("tree.yieldWords().get(1): " + tree.yieldWords().get(1) + "||" + tree.yieldWords().get(1).getClass());
//        System.out.println("tree.yieldWords().get(1).value(): " + tree.yieldWords().get(1).value() + "||" + tree.yieldWords().get(1).value().getClass());
//
//        System.out.println("------------------------------------- Children:S/SQ ----------------------------------------");
//        Tree[] children = tree.children();                                          //1
//        System.out.println("children.length: " + children.length);
//        Tree child = children[0];
//        child.pennPrint();
//        System.out.println("child.nodeString()" + child.nodeString() + "||" + child.nodeString().getClass());
//        System.out.println("--------------------------------- GrandChildren:NP/VP/. ----------------------------------");
//        Tree[] grandchildren = child.children();
//        System.out.println("grandchildren.length: " + grandchildren.length);                                   //3
//        for (Tree grandchildloop: grandchildren){
//            System.out.println("grandchild.value(): " + grandchildloop.value() + "||" + grandchildloop.value().getClass());
//            grandchildloop.pennPrint();
//        }
//        Tree grandchild = grandchildren[1];
//        System.out.println("grandchild.yieldWords(): " + grandchild.yieldWords());

//        System.out.println("=============================== SemanticGraph Related Commands ========================");
//        System.out.println("-------------------------------------Basic Dependence------------------------------------");
//        SemanticGraph semanticGraph = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
//        System.out.println("semanticGraph: " + semanticGraph + "||" + semanticGraph.getClass());
//        System.out.println("semanticGraph.toString(): " + semanticGraph.toString() + "||" + semanticGraph.toString().getClass());
//        System.out.println("semanticGraph.toCompactString(): " + semanticGraph.toCompactString() + "||" + semanticGraph.toCompactString().getClass());
//        System.out.println("semanticGraph.toPOSList(): " + semanticGraph.toPOSList() + "||" + semanticGraph.toPOSList().getClass());
//        System.out.println("semanticGraph.toFormattedString(): " + semanticGraph.toFormattedString() + "||" + semanticGraph.toFormattedString().getClass());
//        semanticGraph.prettyPrint();
//        System.out.println("-------------------------------------Collapse Dependence-----------------------------------");
//        SemanticGraph collapseGraph = sentence.get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class);
//        System.out.println("collapseGraph: " + collapseGraph + "||" + collapseGraph.getClass());
//        System.out.println("collapseGraph.getFirstRoot(): " + collapseGraph.getFirstRoot() + "||" + collapseGraph.getFirstRoot().getClass());

//
//            System.out.println("======================================== Company Name ====================================");
//            company company = new company();
//            System.out.println(company.extract(sentence));

//            System.out.println("======================================== Tense ===========================================");
//            tense tense1 = new tense(sentence);
//            System.out.println(tense1.getTense());

//            System.out.println("========================================= Merger =========================================");
//            merger merger = new merger();
//            System.out.println(merger.IsMerge(sentence));

//            System.out.println("========================================= Acquisition ======================================");
//            acquisition acquisition = new acquisition();
//            System.out.println(acquisition.IsAcquisition(sentence));

//            System.out.println("======================================== Surprise ========================================");
//            surprise surprise = new surprise();
//            System.out.println(surprise.IsSurprise1(sentence));

//            System.out.println("======================================== Uncertainty ========================================");
//            uncertainty uncertainty = new uncertainty();
//            System.out.println(uncertainty.IsUncertainty1(sentence));

        }
    }
}
