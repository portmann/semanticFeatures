package ch.lgt.ming.corenlp;

import java.util.Properties;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class StanfordCore {

public static StanfordCoreNLP pipeline;
	
	public static void init(){	
		Properties props = new Properties();
		
		props.setProperty("annotators", "tokenize, ssplit");
		//props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		
		 pipeline = new StanfordCoreNLP(props);
	}
	
}	