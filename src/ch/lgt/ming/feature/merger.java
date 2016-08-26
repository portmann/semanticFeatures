package ch.lgt.ming.feature;

import ch.lgt.ming.corenlp.TenseAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Ming Deng on 5/9/2016.
 */
public class merger {

    public static boolean IsMerge(CoreMap sentence) {

        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);
        env.bind("$MERGER", "/merger?d?/");
        env.bind("tense", TenseAnnotation.class);
        TokenSequencePattern pattern = TokenSequencePattern.compile(env,

//                "([{ner:ORGANIZATION}][]*([{word:/merger?d?/}]([]*[{ner:ORGANIZATION}])?))|" +
//                        "([{ner: ORGANIZATION}][]*[{ner: ORGANIZATION}][]*[{word:/merger?d?/}])|" +
//                        "([{word:/merger?d?/}][]*[{ner: ORGANIZATION}]([]*[{ner: ORGANIZATION}])?)"

//                "([ner:ORGANIZATION]+[]*($MERGER([]*[ner:ORGANIZATION]+)?))|" +
//                        "([ner: ORGANIZATION]+[]*[ner: ORGANIZATION]+[]*$MERGER)|" +
//                        "($MERGER[]*[ner: ORGANIZATION]+([]*[ner: ORGANIZATION]+)?)"
//
                "([ner:ORGANIZATION]+[tense:Past]*($MERGER([tense:Past]*[ner:ORGANIZATION]+)?))|" +
                        "([ner: ORGANIZATION]+[tense:Past]*[ner: ORGANIZATION]+[tense:Past]*$MERGER)|" +
                        "($MERGER[tense:Past]*[ner: ORGANIZATION]+([tense:Past]*[ner: ORGANIZATION]+)?)"

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
    }
}
