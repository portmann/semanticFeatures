package ch.lgt.ming.feature;

import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.helper.FileHandler;
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

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 10/13/2016.
 */
public class Feature {

    private static Env env = TokenSequencePattern.getNewEnv();
    private List<String> NegWordForNoun = Arrays.asList("no","little", "few");
    private List<String> NegWordForVerb = Arrays.asList("not","hardly", "barely", "rarely", "seldom", "scarcely");
    private List<String> NegWordForOthers = Arrays.asList("not", "hardly", "barely", "rarely", "seldom", "scarcely");
    private String matchedWord = "";

    public Feature() {

        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);

        env.bind("$UNSPECIFIED_SURPRISE", "/amaze\\w*|amazing|astonish\\w*|dumbfound\\w*|startl\\w*|stunn\\w*|surpris\\w*|aback|thunderstruck|wonder\\w*/");
        env.bind("$DISAPPOINTMENT", "/comedown|disappoint\\w*|discontent\\w*|disenchant\\w*|disgruntl\\w*|disillusion\\w*|frustrat\\w*|jilt\\w*/");
        env.bind("$RELIEF", "/relief|relieve|relieved/");
        env.bind("$COMPARATIVE","[{pos:/JJR|RBR/}]");
        env.bind("$SURPRISE","$UNSPECIFIED_SURPRISE|$DISAPPOINTMENT|$RELIEF|$COMPARATIVE");

        env.bind("$UNSPECIFIED_UNCERTAINTY", "/vague\\w*|unforecast\\w*|unforeseen|unpredicted|" +
                "unquantifi\\w*|unreconciled|abeyance\\w*|almost|alteration\\w*|ambigu\\w*|anomal\\w*|" +
                "anticipat\\w*|apparent\\w*|appear\\w*|approximat\\w*|arbitrar\\w*|assum\\w*|" +
                "believ\\w*|cautious\\w*|clarification\\w*|conceivabl\\w*|conditional\\w*|" +
                "confus\\w*|contingen\\w*|could|crossroad\\w*|depend\\w*|destabliz\\w*|" +
                "deviat\\w*|differ|differed|differing|differs|doubt\\w*|exposure\\w*|fluctuat\\w*|hidden|hinges|" +
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
        env.bind("$UNCERTAINTY", "$UNSPECIFIED_UNCERTAINTY|$FEAR|$HOPE|$ANXIETY");

        env.bind("$CONDITIONALITY1", "([{word:/[Ii]f/}]&[tense:Past])[]*[{word:/(then)|,/}]?");
        env.bind("$CONDITIONALITY2", "([{word:/[Ii]f/}]&[tense:Future])[]*[{word:/(then)|,/}]?");
        env.bind("$CONDITIONALITY", "$CONDITIONALITY1|$CONDITIONALITY2");

        env.bind("$POSITIVE","/above|grow|grows|growing|grew|increas.*|jump.*|" +
                "rally|rise|rose|rising|strong|soar.*|surge.*|/");
        env.bind("$VALUES","/sale.*|value.*|market value.*|share.*|profit.*|revenue.*|earning.*|turnover/");

        env.bind("$VALUES_SURPRISE","($VALUES[!{word:/[,.]/}]*?$SURPRISE)|($SURPRISE[!{word:/[,.]/}]*?$VALUES)");
        env.bind("$VALUES_UNCERTAINTY","($VALUES[]*?$UNCERTAINTY)|($UNCERTAINTY[]*?$VALUES)");
        env.bind("$VALUES_POSITIVE","($VALUES[!{word:/[,.]/}]*?$POSITIVE)|($POSITIVE[!{word:/[,.]/}]*?$VALUES)");

    }

    public static void main(String[] args) {
        String inputPath = "data/Empirical_Analysis/ReutersSer_Company/Amazon";
        String outputPath = "data/Reuters_Company/Amazon";


        FileHandler fileHandler = new FileHandler();
        FileInputStream fileInputStream = null;

        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();

        Feature feature = new Feature();

        for (int j = 0; j < listOfFiles.length; j++) {

            try {
                fileInputStream = new FileInputStream(inputPath + "/" + listOfFiles[j].getName());
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Document document = (Document) objectInputStream.readObject();
                Annotation annotation = document.getDocument();
                List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

                for (int i = 0; i < sentences.size(); i++) {
                    feature.FeatureExtract(sentences.get(i),"$VALUES_SURPRISE");
                }

                System.out.printf("%d : %d is done\n", j, document.getIndex());
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public int FeatureExtract(CoreMap sentence, String reg) {

        int count = 0;

        TokenSequencePattern pattern = TokenSequencePattern.compile(env, reg);
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()) {
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println("Found Sentence：" + sentence.toString());
            System.out.println(matchedTokens);
            count++;
        }


        return count;
    }

    public String getMatchedWord() {
        return matchedWord;
    }
}
