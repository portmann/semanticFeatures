package ch.lgt.ming.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.datastore.IdListId;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.datastore.IdValue;
import ch.lgt.ming.datastore.StringId;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Test {

	public static void main(String[] args) throws Exception {

		// variable declaration
		FileHandler fileHandler = new FileHandler();
		IdString documentText = new IdString();        // hashmap (key-value pair) of texts (i.e. text1, text2, etc.)
		StringId positiveWords = new StringId();       // hashmap containing positive words dictionary
		StringId negativeWords = new StringId();	   // hashmap containing negative words dictionary
		StringId textSentenceId = new StringId();      // hashmap of all sentences contained in a text (indexing of sentences within a text)

		// exercise variable
		StringId companyId = new StringId();           // use NER (company is simply an example of an entity)
		IdListId documentCompanies = new IdListId();
		IdValue sentencePosCount = new IdValue();
		IdValue sentenceNegCount = new IdValue();
		IdBoolean sentenceNegation = new IdBoolean();
		IdBoolean sentenceMerger = new IdBoolean();
		IdBoolean sentenceAcquisition = new IdBoolean();

		// initialize corenlp
		StanfordCore.init();

		// Define Set of Negation tokens
		Set<String> negation = new HashSet<>();
		negation.add("no");
		negation.add("not");
		negation.add("n't");

		// load corpus
		String path = "corpus";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		// for (int i = 0; i < listOfFiles.length; i++) {
		for (int i = 7; i < 8; i++) {
			documentText.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
			System.out.println("Document: " + i + " done.");
		}

		double start = System.currentTimeMillis();
		// load positive words
		positiveWords.setMap(fileHandler.loadFileToMap("dictionaries/HARVPos.txt", true));

		// load negative words
		negativeWords.setMap(fileHandler.loadFileToMap("dictionaries/HARVNeg.txt", true));

		// set sentence index (uses Stanford sentence splitter "SentenceAnnotation.class")
		int sentenceIndex = 0;
		int companyCount = 0;
		// Loop over all documents
		for (Integer key : documentText.getMap().keySet()) {

			Annotation document = StanfordCore.pipeline.process(documentText.getValue(key));

			// Loop over all sentences in a document
			List<Integer> CompIdinADoc = new ArrayList<>();
			for (CoreMap sentenceStanford : document.get(CoreAnnotations.SentencesAnnotation.class)) {
				// Put text contents into textSentenceID
				textSentenceId.putValue(sentenceStanford.get(CoreAnnotations.TextAnnotation.class), sentenceIndex);

				// Initialize countPos, countNeg, NegationCount(We need to count
				// these value for each sentence)
				double countPos = 0;
				double countNeg = 0;
				int negationCount = 0;
				// Loop over all tokens in a sentence
				for (CoreLabel token : sentenceStanford.get(CoreAnnotations.TokensAnnotation.class)) {
					String Word = token.get(CoreAnnotations.TextAnnotation.class);
					// System.out.println(Word);
					String Ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
					// System.out.println(Ner);

					// Find out company names by NamedEntityTagAnnotation.
					// Note that the class can only return all organization names,
					// not only company names.
					if (Ner.equals("ORGANIZATION") && !(companyId.getMap().keySet().contains(Word))) { //If there's a new token which labeled as "ORGANIZATION"
						companyId.putValue(Word, companyCount);                                        //and is not contained in companyID, we then add it to the Map
						companyCount++;                                       
					}
					// Add the id of the company into CompIdinADoc list if it appears in our companyId list
					if (companyId.getMap().keySet().contains(Word)) {
						CompIdinADoc.add(companyId.getMap().get(Word));
					}
					
					// Count positive/negative words by checking if they appear
					// in the HARV dictionary
					if (positiveWords.getMap().containsKey(Word.toLowerCase())) {
						countPos++;
					}
					if (negativeWords.getMap().containsKey(Word.toLowerCase())) {
						countNeg++;
					}

					// Count the number of negations in a sentences by checking
					// if the token is in negation set
					if (negation.contains(Word)) {
						negationCount++;
					}
				}
				// For each sentence, record what we have done before
				sentencePosCount.putValue(sentenceIndex, countPos);
				sentenceNegCount.putValue(sentenceIndex, countNeg);
				sentenceNegation.putValue(sentenceIndex, negationCount != 0);
				System.out.println("Sentence: " + sentenceIndex + " done.");
				sentenceIndex++;
			}
			documentCompanies.putValue(key,CompIdinADoc);
		}

		// ******************************
		// ***Highlighted: START HERE**
		// ******************************
		// Pos-Neg words count
		System.out.println("1. sentencePosCount: SentenceID = number of positive words in this sentence " + sentencePosCount.getMap());
		System.out.println("2. sentenceNegCount: SentenceID = number of negetive words in this sentence " + sentenceNegCount.getMap());

		// NER
		// companyId.put(companyName, companyId);
		System.out.println("3. companyId: Name of Organization = ID of Organization " + companyId.getMap());
		System.out.println("4. documentCompanys: ID of Document = A list of ID of Organizations in this Document" + documentCompanies.getMap());
		//according to their order of appearance

		// Sentence-negation
		System.out.println("5. sentenceNegation: ID of Sentence = existence of Negation" + sentenceNegation.getMap());

		System.out.println(System.currentTimeMillis()-start);
		System.out.println("Whoop whoop!!");

	}
}
