package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdString;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

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
	public IdString extract(Annotation document) {

		IdString sentenceCompany = new IdString();
		
		int sentenceIndex = 0;
		String companyname = new String();
		boolean f = false;

		for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {

			int index = 0;
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

				String word = token.get(CoreAnnotations.TextAnnotation.class);
				String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
				int tokenIndex = token.get(CoreAnnotations.IndexAnnotation.class);
				if (ner.equals("ORGANIZATION")) {
					//System.out.println(word);
					index = tokenIndex;
					companyname = companyname + " " + word;
				}
					//if (tokenIndex != index + 1) company.add(word);
					//else if (tokenIndex == index + 1) company.add(word);
				else if (tokenIndex == index + 1) {

					sentenceCompany.putValue(sentenceIndex, companyname);
					companyname = "";
				}
			}
			sentenceIndex++;
		}
		return sentenceCompany;
	}
}
