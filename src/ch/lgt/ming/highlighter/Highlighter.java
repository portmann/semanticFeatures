package ch.lgt.ming.highlighter;

import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.feature.SurpriseFeature;
import ch.lgt.ming.feature.UncertaintyFeature;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class Highlighter {

	// Highlighter highlights like
	// For highlighted text: *before_highlighted* *String* *after_highlighted*
	// For standard text: *before_standard* *String* *after_standard*

	public String before_highlighted = "<span style='background-color: #A9E2F3'>";
	public String after_highlighted = "</span>";
	public String before_standard = "";
	public String after_standard = "";

	public String highlight(Document document) {

		String highlightedString = "";

		boolean highlighted = false;

		UncertaintyFeature uncertatinty = new UncertaintyFeature();

		for (CoreMap sentenceStanford : document.getDocument().get(CoreAnnotations.SentencesAnnotation.class)) {

			if (uncertatinty.Uncertainty_Anxiety(sentenceStanford) > 0) {

				highlightedString = highlightedString + before_highlighted;
				highlightedString = highlightedString + sentenceStanford.get(CoreAnnotations.TextAnnotation.class);
				highlightedString = highlightedString + after_highlighted;
				highlighted = true;

			} else {

				highlightedString = highlightedString + before_standard;
				highlightedString = highlightedString + sentenceStanford.get(CoreAnnotations.TextAnnotation.class);
				highlightedString = highlightedString + after_standard;

			}
		}

		if (highlighted)
			return highlightedString;
		else
			return "";

	}

}