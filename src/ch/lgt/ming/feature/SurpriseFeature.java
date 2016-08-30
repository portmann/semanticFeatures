package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.patterns.surface.SurfacePattern;
import edu.stanford.nlp.util.CoreMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 6/25/2016.
 */
public class SurpriseFeature {

    private static Env env = TokenSequencePattern.getNewEnv();

    public SurpriseFeature(){

        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);
        env.bind("$UNSPECIFIED", "/amaz\\w*|astonish\\w*|dumbfound\\w*|startl\\w*|stunn\\w*|surpris\\w*|aback|thunderstruck|wonder\\w*/");
        env.bind("$DISAPPOINTMENT", "/comedown|disappoint\\w*|discontent\\w*|disenchant\\w*|disgruntl\\w*|disillusion\\w*|frustrat\\w*|jilt\\w*/");
        env.bind("$RELIEF", "/relie\\w*/");
        env.bind("$SURPRISE", "$UNSPECIFIED|$DISAPPOINTMENT|$RELIEF");

    }




    public int Surprise_Unspecified(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$UNSPECIFIED");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
//            List<CoreMap> matchedTokens = matcher.groupNodes();
//            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Disappointment(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$DISAPPOINTMENT");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
//            List<CoreMap> matchedTokens = matcher.groupNodes();
//            System.out.println(matchedTokens.toString());
        }
        return count;

    }

    public int Surprise_Relief(CoreMap sentence){

        int count = 0;
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$RELIEF");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        while (matcher.find()){
            count++;
//            List<CoreMap> matchedTokens = matcher.groupNodes();
//            System.out.println(matchedTokens.toString());
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
//            List<CoreMap> matchedTokens = matcher.groupNodes();
//            System.out.println(matchedTokens.toString());
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
