package ch.lgt.ming.cleanup;

import java.util.ArrayList;
import java.util.List;

import ch.lgt.ming.corenlp.StanfordCore;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Document {
	
	List<String> sentenceText = new ArrayList<String>();
	String documentText = "";
	Annotation document;
	
	public Document(String documentText){
		
		this.documentText = documentText;
		
		this.document = StanfordCore.pipeline.process(documentText);
		
		for (CoreMap sentenceStanford : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			// Put text contents into textSentenceID
			sentenceText.add(sentenceStanford.get(CoreAnnotations.TextAnnotation.class));
		}
	}

	public List<String> getSentence() {
		return sentenceText;
	}

	public void setSentence(List<String> sentence) {
		this.sentenceText = sentence;
	}

	public String getDocumentText() {
		return documentText;
	}

	public void setDocumentText(String documentText) {
		this.documentText = documentText;
	}

	public Annotation getDocument() {
		return document;
	}

	public void setDocument(Annotation document) {
		this.document = document;
	}
	
	
}
