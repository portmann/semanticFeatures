package ch.lgt.ming.feature;

import ch.lgt.ming.corenlp.TenseAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 6/25/2016.
 */
public class SurpriseFeature {

    private static Env env = TokenSequencePattern.getNewEnv();
    private List<String> NegWordForNoun = Arrays.asList("no","little", "few");
    private List<String> NegWordForVerb = Arrays.asList("not","hardly", "barely", "rarely", "seldom", "scarcely");
    private List<String> NegWordForOthers = Arrays.asList("not", "hardly", "barely", "rarely", "seldom", "scarcely");
    private String matchedWord = "";

    public SurpriseFeature() {

        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);

        env.bind("$UNSPECIFIED", "/amaze\\w*|amazing|astonish\\w*|dumbfound\\w*|startl\\w*|stunn\\w*|surpris\\w*|aback|thunderstruck|wonder\\w*/");
        env.bind("$DISAPPOINTMENT", "/comedown|disappoint\\w*|discontent\\w*|disenchant\\w*|disgruntl\\w*|disillusion\\w*|frustrat\\w*|jilt\\w*/");
        env.bind("$RELIEF", "/relief|relieve|relieved/");
        env.bind("$SURPRISE","$UNSPECIFIED|$DISAPPOINTMENT|$RELIEF");

    }

    public static void main(String[] args) {
        System.out.println("--------------------------------------- Pipeline ------------------------------------------");
        Properties props = new Properties();
        props.setProperty("customAnnotatorClass.tense", "ch.lgt.ming.corenlp.TenseAnnotator");
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, tense");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String[] myString = {
                "Amazon shares soar on surprise profit , market value above Wal Mart 's | " +
                        "Reuters By Anya George Tharakan and Mari Saito Amazon.com Inc ( AMZN.O ) " +
                        "shares surged more than 17 percent on Thursday as the online retailer " +
                        "posted an unexpected quarterly profit , pushing its market value above" +
                        " that of Wal-Mart Stores Inc ( WMT.N ) , the world 's largest retailer ."
        };
        for (int i = 0; i < myString.length; i++) {
            System.out.printf("-------------------------------------Sentence %d ---------------------------------\n",i);
            Annotation document = new Annotation(myString[i]);
            pipeline.annotate(document);
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            System.out.println("sentences: " + sentences);
            CoreMap sentence = sentences.get(0);
            SurpriseFeature surprise = new SurpriseFeature();
//            surprise.Surprise(sentence, "$UNSPECIFIED");
            surprise.Surprise(sentence, "$SURPRISE");

        }
    }

    /**
     * This function extract the counts of specific sentiments from a document.
     *
     * @param sentence the annotation of the sentence
     * @param reg regular expression of surprise sentiment
     *
     * @return a list of integer of length 6, represent the counts of "noun_pos","noun_neg",
     *          "verb_pos","verb_neg","othertype_pos","othertype_neg" of the document.
     *
     * */

    public List<Integer> Surprise(CoreMap sentence, String reg) {

        List<Integer> counts = new ArrayList<>();
        int noun_pos = 0;
        int noun_neg = 0;
        int verb_pos = 0;
        int verb_neg = 0;
        int othertype_pos = 0;
        int othertype_neg = 0;

        TokenSequencePattern pattern = TokenSequencePattern.compile(env, reg);
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()) {
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println("Found Sentence：" + sentence.toString());
            System.out.println(matchedTokens);
            matchedWord = matchedTokens.get(0).get(CoreAnnotations.TextAnnotation.class);
            System.out.println(matchedWord);

            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            TreebankLanguagePack tlp = new PennTreebankLanguagePack();
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
            Collection<TypedDependency> tds = gs.typedDependenciesCollapsed();

            for (CoreMap token : matchedTokens) {
                String POS = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                System.out.println("POS: " + POS);
                switch (POS) {
                    case "NN":
                    case "NNS":
                    case "NNP":
                    case "NNPS": {
                        for (TypedDependency td : tds) {
                            GrammaticalRelation relation = td.reln();
                            if (relation.getShortName().equals("neg")) {
//                                System.out.println("find neg!!!");
                                IndexedWord governor = td.gov();
                                if (governor.value().equals(matcher.group())) {
                                    noun_neg++;
//                                    System.out.println("Surprise " + reg + " noun_neg++:" + noun_neg);
                                }
                            } else if (relation.getShortName().equals("amod")||relation.getShortName().equals("dep")) {
//                                System.out.println("find amod/dep!!!");
                                IndexedWord governor = td.gov();
                                IndexedWord dependent = td.dep();
                                if (governor.value().equals(matcher.group()) && NegWordForNoun.contains(dependent.value().toLowerCase())) {
                                    noun_neg++;
//                                    System.out.println("Surprise " + reg + " noun_neg++:" + noun_neg);
                                }
                            }
                        }
                        if (noun_neg == 0) {
                            noun_pos++;
//                            System.out.println("Surprise " + reg + " noun_pos++:" + noun_pos);
                        }
                        break;
                    }
                    case "VB":
                    case "VBD":
                    case "VBG":
                    case "VBN":
                    case "VBP":
                    case "VBZ": {
                        for (TypedDependency td : tds) {
                            GrammaticalRelation relation = td.reln();
                            if (relation.getShortName().equals("neg")) {
//                                System.out.println("find neg!!!");
                                IndexedWord governor = td.gov();
                                if (governor.value().equals(matcher.group())) {
                                    verb_neg++;
//                                    System.out.println("Surprise " + reg + " verb_neg++:" + verb_neg);
                                }
                            } else if (relation.getShortName().equals("advmod")||relation.getShortName().equals("dep")) {
//                                System.out.println("find advmod/dep!!!");
                                IndexedWord governor = td.gov();
                                IndexedWord dependent = td.dep();
                                if (governor.value().equals(matcher.group()) && NegWordForVerb.contains(dependent.value().toLowerCase())) {
                                    verb_neg++;
//                                    System.out.println("Surprise " + reg + " verb_neg++:" + verb_neg);
                                }
                            }
                        }
                        if (verb_neg == 0) {
                            verb_pos++;
//                            System.out.println("Surprise " + reg + " verb_pos++:" + verb_pos);
                        }
                        break;
                    }
                    default: {
                        for (TypedDependency td : tds) {
                            GrammaticalRelation relation = td.reln();
                            if (relation.getShortName().equals("neg")) {
//                                System.out.println("find neg!!!");
                                IndexedWord governor = td.gov();
                                if (governor.value().equals(matcher.group())) {
                                    othertype_neg++;
//                                    System.out.println("Surprise " + reg + " othertype_neg++:" + othertype_neg);
                                }
                            }else if (relation.getShortName().equals("advmod")||relation.getShortName().equals("dep")) {
//                                System.out.println("find advmod/dep!!!");
                                IndexedWord governor = td.gov();
                                IndexedWord dependent = td.dep();
                                if (governor.value().equals(matcher.group()) && NegWordForOthers.contains(dependent.value().toLowerCase())) {
                                    othertype_neg++;
//                                    System.out.println("Surprise " + reg + " othertype_neg++:" + othertype_neg);
                                }
                            }
                        }
                        if (othertype_neg == 0) {
                            othertype_pos++;
//                            System.out.println("Surprise " + reg + " othertype_pos++:" + othertype_pos);
                        }
                        break;
                    }
                }
            }
        }

        counts.add(0, noun_pos);
        counts.add(1, noun_neg);
        counts.add(2, verb_pos);
        counts.add(3, verb_neg);
        counts.add(4, othertype_pos);
        counts.add(5, othertype_neg);
        return counts;
    }

    public int SurpriseComparative(CoreMap sentence) {
        int count = 0;
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        for (CoreLabel token : tokens) {
            String POS = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (POS.equals("JJR")||POS.equals("RBR")) {
                System.out.println("Found Sentence：" + sentence.toString());
                count++;
                System.out.println("SurpreiseComparative count++" + count);
            }
        }
        return count;
    }

    public String getMatchedWord() {
        return matchedWord;
    }
}