package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 7/12/2016.
 */
public class test {
    public static void main(String[] args) throws Exception {
//        System.out.println("--------------------------------------- Pipeline ------------------------------------------");
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//
///**
// * Different sentences to test company and merger
// */
//
//        String[] myString = {
//                "Winter is coming."
//        };  //sentences to test merger
//
//        for (int i = 0; i < myString.length; i++) {
//            Annotation document = new Annotation(myString[i]);
//            pipeline.annotate(document);
//            System.out.printf("-------------------------------------Sentence %d ---------------------------------\n",i);
//            System.out.println("============================= Sentence Related Commands ==================================");
//            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//            System.out.println("sentences: " + sentences + "||" + sentences.getClass());        //List<CoreMap>
//            CoreMap sentence = sentences.get(0);
//            IsM(sentence);
//        }
        TokenSequenceMatcherITest tt = new TokenSequenceMatcherITest();
        tt.setUp();
        tt.testTokenSequenceMatcher1();

    }

    /*public static boolean IsM(CoreMap sentence) {


        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);
        env.bind("$SEASON", "/spring|summer|fall|winter/");

        TokenSequencePattern pattern = TokenSequencePattern.compile(env,
                "$SEASON[]?"
        );
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        boolean found = false;
        while (matcher.find()) {
            System.out.format("I found the text" +
                            " \"%s\" starting at " +
                            "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
            found = true;
        }
        if (!found) {
            System.out.format("No match found.%n");
        }
        return found;
    }*/
}
