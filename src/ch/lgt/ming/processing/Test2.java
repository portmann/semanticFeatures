package ch.lgt.ming.processing;

import java.io.File;
import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.extraction.sentnence.CompaniesAll;
import ch.lgt.ming.extraction.sentnence.Extractor;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.pipeline.Annotation;

public class Test2 {
	
	public static void main(String[] args) throws Exception {
		
		// variable declaration
		FileHandler fileHandler = new FileHandler();
		IdString documentText = new IdString(); 
		IdString sentenceCompany = new IdString(); 
		Extractor<IdString> companyExtractor = new CompaniesAll();
		
		// initialize corenlp
		StanfordCore.init();
		
		// load corpus
		String path = "corpus";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		//load just the first documents
		for (int i = 0; i < 2; i++) {
			documentText.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));

			System.out.println("Document: " + i + " done.");
		}
		
		//process documents
		for (Integer key : documentText.getMap().keySet()) {

			Annotation annotation = StanfordCore.pipeline.process(documentText.getValue(key));
			sentenceCompany = companyExtractor.extract(annotation);
			
			//show companies
			System.out.println(sentenceCompany.getMap());
			
		}
	}

}
