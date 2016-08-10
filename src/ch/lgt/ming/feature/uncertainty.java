package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 6/25/2016.
 */
public class uncertainty {

    public static Boolean IsUncertainty1(CoreMap sentence){

        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);
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
        env.bind("$HOPE", "/buoyan\\w*|confident\\w*|faith\\w*|hop\\w*|potim\\w*/");
        env.bind("$ANXIETY", "/anguish\\w*|anxi\\w*|apprehens\\w*|diffiden\\w*|jitter\\w*|nervous\\w*|trepida\\w*|wari\\w*|wary|worried\\w*|worry\\w*/");
        env.bind("$UNCEARTAINTY","$UNSPECIFIED|$FEAR|$HOPE|$ANXIETY");

        TokenSequencePattern pattern = TokenSequencePattern.compile(env,"$UNCEARTAINTY");
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        boolean found = false;
        while (matcher.find()) {
//            System.out.format("I found the text" +
//                            " \"%s\" starting at " +
//                            "index %d and ending at index %d.%n",
//                    matcher.group(),
//                    matcher.start(),
//                    matcher.end());
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
            found = true;
        }
//        if (!found) {
//            System.out.format("No match found.%n");
//        }
        return found;
    }

    public static Boolean IsUncertainty2(CoreMap sentence){

        TokenSequencePattern pattern = TokenSequencePattern.compile(
                "[{word:/[Ii]f/}][]*[{word:/then/}]?"

        );
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
        boolean found = false;
        while (matcher.find()) {
//            System.out.format("I found the text" +
//                            " \"%s\" starting at " +
//                            "index %d and ending at index %d.%n",
//                    matcher.group(),
//                    matcher.start(),
//                    matcher.end());
            List<CoreMap> matchedTokens = matcher.groupNodes();
            System.out.println(matchedTokens.toString());
            found = true;
        }
//        if (!found) {
//            System.out.format("No match found.%n");
//        }
        return found;

    }
}
