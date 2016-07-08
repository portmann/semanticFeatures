package ch.lgt.ming.feature;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

/**
 * Created by Ming Deng on 6/25/2016.
 */
public class acquisition {

    public Boolean IsAcquisition(CoreMap sentence){

        TokenSequencePattern pattern = TokenSequencePattern.compile(

                "([{ner:ORGANIZATION}][]*([{word:/acquired?/}]|[{word:/buy/}]|[{word:/bought/}]|[{word:/purchased?/}]|[{word:/takeover/}]|([{word:/taken?/}]|[{word:/took/}][{word:/over/}])|[{word:/acquisition/}])([]*[{ner:ORGANIZATION}])?)|" +
                "([{ner: ORGANIZATION}][]*[{ner: ORGANIZATION}][]*([{word:/acquired?/}]|[{word:/buy/}]|[{word:/bought/}]|[{word:/purchased?/}]|[{word:/takeover/}]|([{word:/taken?/}]|[{word:/took/}][{word:/over/}])|[{word:/acquisition/}]))|" +
                "(([{word:/acquired?/}]|[{word:/buy/}]|[{word:/bought/}]|[{word:/purchased?/}]|[{word:/takeover/}]|([{word:/taken?/}]|[{word:/took/}][{word:/over/}])|[{word:/acquisition/}])[]*[{ner: ORGANIZATION}]([]*[{ner: ORGANIZATION}])?)"

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
