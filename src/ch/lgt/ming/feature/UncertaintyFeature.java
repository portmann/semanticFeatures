package ch.lgt.ming.feature;

import ch.lgt.ming.corenlp.TenseAnnotation;
import ch.lgt.ming.datastore.StringId;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 6/25/2016.
 */
public class UncertaintyFeature {

    private static Env env = TokenSequencePattern.getNewEnv();
    private List<String> NegWordForNoun = Arrays.asList("little", "few");
    private List<String> NegWordForVerb = Arrays.asList("hardly", "barely", "rarely", "seldom", "scarcely");

    public UncertaintyFeature() {
        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);
        env.bind("tense", TenseAnnotation.class);
        env.bind("$UNSPECIFIED", "/vague\\w*|unforecast\\w*|unforeseen|unpredicted|" +
                "unquantifi\\w*|unreconciled|abeyance\\w*|almost|alteration\\w*|ambigu\\w*|anomal\\w*|" +
                "anticipat\\w*|apparent\\w*|appear\\w*|approximat\\w*|arbitrar\\w*|assum\\w*|" +
                "believ\\w*|cautious\\w*|clarification\\w*|conceivabl\\w*|conditional\\w*|" +
                "confus\\w*|contingen\\w*|could|crossroad\\w*|depend\\w*|destabliz\\w*|" +
                "deviat\\w*|differ\\w*|doubt\\w*|exposure\\w*|fluctuat\\w*|hidden|hinges|" +
                "imprecis\\w*|improbab\\w*|incompleteness|indefinite\\w*|indetermina\\w*|" +
                "inexact\\w*|instabilit\\w*|intangible\\w*|likelihood|may|maybe|might|nearly|" +
                "nonassessable|occasionally|ordinarily|pending|perhaps|possib\\w*|" +
                "precaution\\w*|predict\\w*|preliminar\\w*|presum\\w*|probab\\w*|random\\w*|" +
                "reassess\\w*|recalculat\\w*|reconsider\\w*|reexamin\\w*|reinterpret\\w*|" +
                "revise\\w*|risk\\w*|roughly|rumors|seems|seldom\\w*|sometime\\w*|somewhat|somewhere|" +
                "speculat\\w*|sporadic\\w*|sudden\\w*|suggest\\w*|susceptibility|tending|" +
                "tentative\\w*|turbulence|uncertain\\w*|unclear|unconfirmed|undecided|" +
                "undefined|undesignated|undetectable|undetermin\\w*|undocumented|unexpected\\w*|" +
                "unfamiliar\\w*|unguaranteed|unhedeged|unidentifi\\w*|unknown\\w*|unobservable|" +
                "unplanned|unpredict\\w*|unprove\\w*|unseasonabl\\w*|unsettled|unspecifi\\w*|" +
                "untested|unusual\\w*|unwritten|vagaries|variab\\w*|varian\\w*|variation\\w*|" +
                "varie\\w*|vary\\w*|volatil\\w*/");
        env.bind("$FEAR", "/afraid\\w*|aghast\\w*|alarm\\w*|dread\\w*|fear\\w*|fright\\w*|horr\\w*|panic\\w*|scare\\w*|terror\\w*/");
        env.bind("$HOPE", "/buoyan\\w*|confiden\\w*|faith\\w*|hop\\w*|optim\\w*/");
        env.bind("$ANXIETY", "/anguish\\w*|anxi\\w*|apprehens\\w*|diffiden\\w*|jitter\\w*|nervous\\w*|trepida\\w*|wari\\w*|wary|worr\\w*/");
        env.bind("$CONDITIONALITY1", "([{word:/[Ii]f/}]&[tense:Past])[]*[{word:/(then)|,/}]?");
        env.bind("$CONDITIONALITY2", "([{word:/[Ii]f/}]&[tense:Future])[]*[{word:/(then)|,/}]?");
        env.bind("$UNCERTAINTY", "$UNSPECIFIED|$FEAR|$HOPE|$ANXIETY");
    }

    public List<Integer> Uncertainty(CoreMap sentence, String Reg) {

        List<Integer> counts = new ArrayList<>();
        int noun_pos = 0;
        int noun_neg = 0;
        int verb_pos = 0;
        int verb_neg = 0;
        int othertype_pos = 0;
        int othertype_neg = 0;

        TokenSequencePattern pattern = TokenSequencePattern.compile(env, Reg);
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()) {
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println("Found Sentence：" + sentence.toString());
            System.out.println(matchedTokens.toString());

            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            TreebankLanguagePack tlp = new PennTreebankLanguagePack();
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
            Collection<TypedDependency> tds = gs.typedDependenciesCollapsed();

            for (CoreMap token : matchedTokens) {
                String POS = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                System.out.println("POS:" + POS);
                switch (POS) {
                    case "NN":
                    case "NNS":
                    case "NNP":
                    case "NNPS": {
                        for (TypedDependency td : tds) {
                            GrammaticalRelation relation = td.reln();
                            if (relation.getShortName().equals("neg")) {
                                System.out.println("find neg!!!");
                                IndexedWord governor = td.gov();
                                if (governor.value().equals(matcher.group())) {
                                    noun_neg++;
                                    System.out.println("Uncertainty " + Reg + " noun_neg++:" + noun_neg);
                                }
                            } else if (relation.getShortName().equals("amod")) {
                                System.out.println("find amod!!!");
                                IndexedWord governor = td.gov();
                                IndexedWord dependent = td.dep();
                                if (governor.value().equals(matcher.group()) && NegWordForNoun.contains(dependent.value().toLowerCase())) {
                                    noun_neg++;
                                    System.out.println("Uncertainty " + Reg + " noun_neg++:" + noun_neg);
                                }
                            }
                        }
                        if (noun_neg == 0) {
                            noun_pos++;
                            System.out.println("Uncertainty " + Reg + " noun_pos++:" + noun_pos);
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
                                System.out.println("find neg!!!");
                                IndexedWord governor = td.gov();
                                if (governor.value().equals(matcher.group())) {
                                    verb_neg++;
                                    System.out.println("Uncertainty " + Reg + " verb_neg++:" + verb_neg);
                                }
                            } else if (relation.getShortName().equals("advmod")) {
                                System.out.println("find advmod!!!");
                                IndexedWord governor = td.gov();
                                IndexedWord dependent = td.dep();
                                if (governor.value().equals(matcher.group()) && NegWordForVerb.contains(dependent.value().toLowerCase())) {
                                    verb_neg++;
                                    System.out.println("Uncertainty " + Reg + " verb_neg++:" + verb_neg);
                                }
                            }
                        }
                        if (verb_neg == 0) {
                            verb_pos++;
                            System.out.println("Uncertainty " + Reg + " verb_pos++:" + verb_pos);
                        }
                        break;
                    }

                    default: {
                        for (TypedDependency td : tds) {
                            GrammaticalRelation relation = td.reln();
                            if (relation.getShortName().equals("neg")) {
                                System.out.println("find neg!!!");
                                IndexedWord governor = td.gov();
                                if (governor.value().equals(matcher.group())) {
                                    othertype_neg++;
                                    System.out.println("Uncertainty " + Reg + " othertype_neg++:" + othertype_neg);
                                }
                            }
                        }
                        if (othertype_neg == 0) {
                            othertype_pos++;
                            System.out.println("Uncertainty " + Reg + " othertype_pos++:" + othertype_pos);
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

    public List<Integer> UncertaintyConditionality(CoreMap sentence, String Reg) throws Exception {

        FileHandler fileHandler = new FileHandler();
        StringId positiveWords = new StringId();
        StringId negativeWords = new StringId();
        positiveWords.setMap(fileHandler.loadFileToMap("dictionaries/L&MPos.txt", true));
        negativeWords.setMap(fileHandler.loadFileToMap("dictionaries/L&MNeg.txt", true));
        List<Integer> counts = new ArrayList<>();
        int conditionality = 0;
        int conditionality_pos = 0;
        int conditionality_neg = 0;

        TokenSequencePattern pattern = TokenSequencePattern.compile(env, Reg);
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()) {
            System.out.println("Found Sentence：" + sentence.toString());
            TokenSequencePattern pattern2 = TokenSequencePattern.compile(env, "/(then)|,/[]*");
            TokenSequenceMatcher matcher2 = pattern2.getMatcher(tokens);
            while (matcher2.find()) {
                System.out.format("I found the text" +
                                " \"%s\" starting at " +
                                "index %d and ending at index %d.%n",
                        matcher2.group(),
                        matcher2.start(),
                        matcher2.end());
                for (int i = matcher2.start(); i < matcher2.end(); i++){
                    String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                    if (positiveWords.getMap().containsKey(word)) {
                        conditionality_pos++;
                        System.out.println("Uncertainty " + Reg + " conditionality_pos++:" + conditionality_pos);
                    }
                    if (negativeWords.getMap().containsKey(word)){
                        conditionality_neg++;
                        System.out.println("Uncertainty " + Reg + " conditionality_neg++: "+ conditionality_neg);
                    }
                }
                if (conditionality_pos == 0 && conditionality_neg == 0) {
                    conditionality ++;
                    System.out.println("Uncertainty " + Reg + " conditionality ++:" + conditionality);
                }
            }
//            List<CoreMap> matchedTokens = matcher.groupNodes();
//            System.out.println(matchedTokens.toString());
        }
        counts.add(0, conditionality);
        counts.add(1, conditionality_pos);
        counts.add(2, conditionality_neg);
        return counts;
    }
}


