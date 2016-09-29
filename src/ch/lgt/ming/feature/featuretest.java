package ch.lgt.ming.feature;

import ch.lgt.ming.corenlp.TenseAnnotation;
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

    public static void main(String[] args) throws Exception {

        System.out.println("--------------------------------------- Pipeline ------------------------------------------");
        Properties props = new Properties();
        props.setProperty("customAnnotatorClass.tense", "ch.lgt.ming.corenlp.TenseAnnotator");
//        props.setProperty("tense.key", "tense");
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, tense");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

//  Different sentences to test company and merger

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


        String[] myString = {
                "I am so surprised about the amazing news. ",
                "The 'Amazing' IPO Change That May Restart The Flow Of New Stocks.",
                "Opinion: CFOs want a stunning 14% annual return on investments — and that’s holding back the economy",
                "Gap shares slump as July sales disappoint, but analysts upbeat."
        };//Strings to test surprise

//        String[] myString = {
//                "The expression is really vague.",
//                "The news worries me a lot and I am quite anxious and worried about the result.",
//                "Christopher Bishop, Chief Reinvention Officer of Improvising Careers said " +
//                        "it’s important to keep an eye on the “little things” of any war gaming " +
//                        "exercise like making sure teams are comfortable with ambiguity, being " +
//                        "resourceful, resilient, creative and passionate.",
//                "“We’ve had a pretty anomalously hot and dry stretch,” says Randy Graham, " +
//                        "meteorologist-in-charge at the Weather Services’ Salt Lake City office."
//        };//Strings to test uncertainty

//        String[] myString = {
//
//                "If I am president, I will make you vice president.",
//                "If I am president, then I will make you vice president.",
//                "If I was president, I would make you vice president.",
//                "If I was president, then I would make you vice president."
//
//        }; //Strings to test uncertainty_conditional

//        String[] myString = {
//
//                "I stared at her in amazement.",
//                "The result amazed me.",
//                "That is amazing.",
//
//                "I stared at her in disappointment.",
//                "The result disappointed me.",
//                "That is disappointing.",
//
//                "I stared at her in relief.",
//                "The result relieved me.",
//                "That is relieving."
//
//        }; //Strings to test surprise

//                String[] myString = {
//
//                "I stared at her with little surprise.",
//                "The result barely amazed me.",
//                "That is not amazing.",
//
//                "I stared at her in disappointment.",
//                "The result scarcely disappointed me.",
//                "That is so disappointing.",
//
//                "I stared at her in relief.",
//                "The result relieved me.",
//                "That is relieving."
//
//        }; //Strings to test surprise_negation


//        String[] myString = {
//
//                "I stared at her with confusion.",
//                "The result confused me.",
//                "That is confusing.",
//
//                "I stared at her in anxiety.",
//                "The result worried me.",
//                "That is worrying.",
//
//                "I stared at her with fear.",
//                "The result frightened me.",
//                "That is scary.",
//
//                "I stared at her with confidence.",
//                "I hope this will work.",
//                "That is optimal."
//
//        }; //Strings to test uncertainty

//        String[] myString = {
//
//                "I stared at her with confusion.",
//                "The result confused me.",
//                "That is confusing.",
//
//                "I stared at her in anxiety.",
//                "The result worried me.",
//                "That is worrying.",
//
//                "I stared at her with fear.",
//                "The result frightened me.",
//                "That is scary.",
//
//                "I stared at her with confidence.",
//                "I hope this will work.",
//                "That is optimal."
//
//        }; //Strings to test uncertainty_negation


//        String[] myString = {
//                //aux
//                "I don't like cheese.",
//                "I am not fine.",
//                "I was happy.",
//                "I will be happy.",
//                "I have done that before.",
//                "How do I use Stanford Parser's Typed Dependencies in Python?",
//                "What flights did you have from Burbank to Tacoma Washington?",
//                "What flights do you have from Burbank to Tacoma Washington?",
//                "What flights will you have from Burbank to Tacoma Washington?",
//                //auxpass
//                "I was found by him.",
//                //copula
//                "Bill was an honest man.",
//                //root
//                "I like animals."
//        };//Strings to test tense

        for (int i = 0; i < myString.length; i++) {
            //Annotate document
            Annotation document = new Annotation(myString[i]);
            pipeline.annotate(document);


            System.out.printf("-------------------------------------Sentence %d ---------------------------------\n",i);
//            System.out.println("============================= Sentence Related Commands ==================================");
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            System.out.println("sentences: " + sentences + "||" + sentences.getClass());        //List<CoreMap>
            CoreMap sentence = sentences.get(0);


//            System.out.println("sentence: " + sentence + "||" + sentence.getClass());          //CoreMap
//            System.out.println("sentence.toShorterString(): " + sentence.toShorterString() + "||" + sentence.toShorterString().getClass());
//            String sentenceText = sentence.get(CoreAnnotations.TextAnnotation.class);
//            System.out.println("sentenceText: " + sentenceText + "||" + sentenceText.getClass());

//            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                System.out.println("============================= Token Related Commands ==================================");
                List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
                CoreLabel token = tokens.get(0);
//                System.out.println("token: " + token + "||" + token.getClass());
//                System.out.println("token.toShorterString(): " + token.toShorterString() + "||" + token.toShorterString().getClass());

//                String word = token.get(CoreAnnotations.TextAnnotation.class);                                                //Text
//                System.out.println("word: " + word + "||" + word.getClass());
//                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);                                         //POS
//                System.out.println("pos: " + pos + "||" + pos.getClass());
//                Integer tokenindex = token.get(CoreAnnotations.IndexAnnotation.class);                                        //Index
//                System.out.println(tokenindex);
//                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);                                       //NER
//                System.out.println(ner);
                String tense = token.get(TenseAnnotation.class);                                                              //Tense
                System.out.println("tense: " + tense);
//                }
//
//            System.out.println("======================================== Company Name ====================================");
//            company company = new company();
//            System.out.println(company.extract(sentence));

//            System.out.println("======================================== Tense ===========================================");
//            tense tense1 = new tense(sentence);
//            System.out.println(tense1.getTense());

//            System.out.println("========================================= Merger =========================================");
//            merger merger = new merger();
//            System.out.println(merger.isMerger(sentence));

//            System.out.println("========================================= Acquisition ======================================");
//            acquisition acquisition = new acquisition();
//            System.out.println(acquisition.isAcquisition(sentence));

            System.out.println("======================================== Surprise ========================================");
            SurpriseFeature surprise = new SurpriseFeature();
            surprise.Surprise(sentence, "$UNSPECIFIED");




//            System.out.println("======================================== Uncertainty ========================================");
//            UncertaintyFeature uncertainty = new UncertaintyFeature();
//            uncertainty.UncertaintyConditionality(sentence, "$CONDITIONALITY1");
//            uncertainty.UncertaintyConditionality(sentence, "$CONDITIONALITY2");




        }
    }
}
