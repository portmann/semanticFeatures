package ch.lgt.ming.cleanup;

import ch.lgt.ming.feature.SurpriseFeature;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class SurpriceDisappointmentHighlighter implements Highlighter{
	
	public SurpriceDisappointmentHighlighter(){
		
	}
	
	@Override
	public String highlight(Document document) {
		
		SurpriseFeature surprise = new SurpriseFeature();
		

		for (CoreMap sentenceStanford : document.getDocument().get(CoreAnnotations.SentencesAnnotation.class))
		{
			
			
			//if
			
			
		}
		

		return null;
		
	}
}
