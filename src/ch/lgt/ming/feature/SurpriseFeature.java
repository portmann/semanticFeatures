package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 6/25/2016.
 */
public class SurpriseFeature {

    private static Env env = TokenSequencePattern.getNewEnv();

    public SurpriseFeature(){

        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);

        env.bind("$UNSPECIFIED_NOUN", "[{word:/amaze\\w*|astonish\\w*|dumbfound\\w*|startl\\w*|stunn\\w*|surpris\\w*|aback|thunderstruck|wonder\\w*/; pos:/NN.*/}]");
        env.bind("$UNSPECIFIED_VERB", "[{word:/amaze\\w*|astonish\\w*|dumbfound\\w*|startl\\w*|stunn\\w*|surpris\\w*|aback|thunderstruck|wonder\\w*/; pos:/VB.*/}]");
        env.bind("$UNSPECIFIED_OTHERTYPE", "[{word:/amaz\\w*|astonish\\w*|dumbfound\\w*|startl\\w*|stunn\\w*|surpris\\w*|aback|thunderstruck|wonder\\w*/; pos:/JJ.*|RB.*/}]");

        env.bind("$DISAPPOINTMENT_NOUN", "[{word:/comedown|disappoint\\w*|discontent\\w*|disenchant\\w*|disgruntl\\w*|disillusion\\w*|frustrat\\w*|jilt\\w*/; pos:/NN.*/}]");
        env.bind("$DISAPPOINTMENT_VERB", "[{word:/comedown|disappoint\\w*|discontent\\w*|disenchant\\w*|disgruntl\\w*|disillusion\\w*|frustrat\\w*|jilt\\w*/; pos:/VB.*/}]");
        env.bind("$DISAPPOINTMENT_OTHERTYPE", "[{word:/comedown|disappoint\\w*|discontent\\w*|disenchant\\w*|disgruntl\\w*|disillusion\\w*|frustrat\\w*|jilt\\w*/; pos:/JJ.*|RB.*/}]");

        env.bind("$RELIEF_NOUN", "[{word:/relie\\w*/; pos:/NN.*/}]");
        env.bind("$RELIEF_VERB", "[{word:/relie\\w*/; pos:/VB.*/}]");
        env.bind("$RELIEF_OTHERTYPE", "[{word:/relie\\w*/; pos:/JJ.*|RB.*/}]");

    }


    public int Surprise_Unspecified_Noun(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$UNSPECIFIED_NOUN");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Unspecified_Verb(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$UNSPECIFIED_VERB");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Unspecified_OtherType(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$UNSPECIFIED_OTHERTYPE");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Disappointment_Noun(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$DISAPPOINTMENT_NOUN");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Disappointment_Verb(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$DISAPPOINTMENT_VERB");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Disappointment_OtherType(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$DISAPPOINTMENT_OTHERTYPE");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Relief_Noun(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$RELIEF_NOUN");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Relief_Verb(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$RELIEF_VERB");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Relief_OtherType(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$RELIEF_OTHERTYPE");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int SurpriceCount(CoreMap sentence){


        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$SURPRISE");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()) {
//            System.out.format("I found the text" +
//                            " \"%s\" starting at " +
//                            "index %d and ending at index %d.%n",
//                    matcher.group(),
//                    matcher.start(),
//                    matcher.end());
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
            count++;
        }
//        if (!found) {
//            System.out.format("No match found.%n");
//        }
        return count;
    }


    public int Surprise_Comparative(CoreMap sentence){

        int count = 0;
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        for (CoreLabel token: tokens){
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (pos.equals("JJR")){
                count++;
            }
        }
        return count;
    }


}
