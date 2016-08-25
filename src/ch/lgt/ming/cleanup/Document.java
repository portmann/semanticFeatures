package ch.lgt.ming.cleanup;

import java.util.ArrayList;
import java.util.List;

import ch.lgt.ming.corenlp.StanfordCore;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Document {
	
	List<String> sentence = new ArrayList<String>();
	String documentText = "";
	
	public Document(String documentText){
		
		this.documentText = documentText;
		
		Annotation document = StanfordCore.pipeline.process(documentText);
		
		for (CoreMap sentenceStanford : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			// Put text contents into textSentenceID
			sentence.add(sentenceStanford.get(CoreAnnotations.TextAnnotation.class));
		}
	}
}
