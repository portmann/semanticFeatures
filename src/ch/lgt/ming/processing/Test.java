package ch.lgt.ming.processing;

import java.io.File;
import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.datastore.IdListId;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.datastore.IdValue;
import ch.lgt.ming.datastore.StringId;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Test {

	public static void main(String[] args) throws Exception {

		// variable declaration
		FileHandler fileHandler = new FileHandler();
		IdString documentText = new IdString();   // hashmap (key-value pair) of texts (i.e. text1, text2, etc.)
		StringId positiveWords = new StringId();  // hashmap containing positive words dictionary
		StringId negativeWords = new StringId();
		StringId textSentenceId = new StringId(); // hashmap of all sentences contained in a text (indexing of sentences within a text)
		
		// exercise variable
		StringId companyId = new StringId(); // use NER (company is simply an example of an entity)
		IdListId documentCompanys = new IdListId();   // use NER
		IdValue sentencePosCount = new IdValue();
		IdValue sentenceNegCount = new IdValue();
		IdBoolean sentenceNegation = new IdBoolean();

		// initialize corenlp
		StanfordCore.init();
		
		// load corpus
		String path = "corpus";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			documentText.putValue(
					i,
					fileHandler.loadFileToString(path + "/"
							+ listOfFiles[i].getName()));
			
			System.out.println("Document: " + i + " done.");
		}
		
		// load positive words
		positiveWords.setMap(fileHandler.loadFileToMap("dictionaries/HARVPos.txt", true));
		
		// load negative words
		negativeWords.setMap(fileHandler.loadFileToMap("dictionaries/HARVNeg.txt", true));
		
		// set sentence index (uses Stanford sentence splitter "SentenceAnnotation.class")
		int sentenceIndex = 0;
		for (Integer key : documentText.getMap().keySet()) {
			
			Annotation annotation = StanfordCore.pipeline.process(documentText.getValue(key));
		
			for (CoreMap sentenceStanford : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				
				textSentenceId.putValue(sentenceStanford.toString(), sentenceIndex);
				sentenceIndex++;
				System.out.println("Sentence: " + sentenceIndex + " done.");
			}
		}
		
		//******************************
		// ***Highlighted: START HERE**
		//******************************
		// Pos-Neg words count
		//sentencePosCount.put(sentenceId, count);
		//sentenceNegCount.put(sentenceId, count);
		
		// NER
		//companyId.put(companyName, companyId);
		//documentCompanys.add(documentId, companyId);
		
		// Sentence-negation
		//sentenceNegation.put(sentenceId, negFlag);
		
		System.out.println("Whoop whoop!!");

	}
}