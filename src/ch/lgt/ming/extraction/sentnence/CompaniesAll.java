package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdString;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * This class extracts all companies from each sentence and stores it in a
 * <SentenceId_CompanyName> data structure
 *
 * @author Samuel Portmann
 * @version 1.0
 * @since 2016-04-22
 */
public class CompaniesAll implements Extractor<IdString> {

	@Override
	public IdString extract(Annotation a) {

		IdString sentenceCompany = new IdString(); 
		
		int sentenceIndex = 0;
		for (CoreMap sentenceStanford : a.get(CoreAnnotations.SentencesAnnotation.class)) {

			for (CoreLabel token : sentenceStanford.get(CoreAnnotations.TokensAnnotation.class)) {

				String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
				if (ner.equals("ORGANIZATION")) 
					sentenceCompany.putValue(sentenceIndex, token.get(CoreAnnotations.TextAnnotation.class));                                       
                                     
				}
			sentenceIndex++;
		}

		return sentenceCompany;
	}

}
