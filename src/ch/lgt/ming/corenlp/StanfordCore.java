package ch.lgt.ming.corenlp;

import java.util.Properties;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class StanfordCore {

public static StanfordCoreNLP pipeline;
	
	public static void init(){	
		Properties props = new Properties();

		props.setProperty("customAnnotatorClass.tense", "ch.lgt.ming.corenlp.TenseAnnotator");
//		props.setProperty("annotators", "tokenize, ssplit");
//		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, tense");

		 pipeline = new StanfordCoreNLP(props);
	}
	
}	
