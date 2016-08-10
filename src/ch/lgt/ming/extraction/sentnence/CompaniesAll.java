package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.datastore.IdListString;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.feature.company;
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
public class CompaniesAll implements Extractor<IdListString> {

	@Override
	public IdListString extract(Annotation document) {

		int sentenceIndex = 0;
		boolean mark = false;
		IdListString NOSCompany = new IdListString();
		List<String> com = new ArrayList<>();   //An ArrayList of company names of the sentence.

		for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {

			String comname = "";
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

				String word = token.get(CoreAnnotations.TextAnnotation.class);
				String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

				if (ner.equals("ORGANIZATION")) {

					if (mark){
						comname = comname + " " + word;
					}else{
						comname = word;
					}
					//System.out.println(word);
					mark = true;

				}
				else if (mark) {
					com.add(comname);
					comname = "";
					mark = false;
				}
			}
			NOSCompany.putValue(sentenceIndex, com);
			sentenceIndex++;
		}
		return NOSCompany;
	}
}
