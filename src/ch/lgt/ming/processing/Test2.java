package ch.lgt.ming.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.datastore.IdListString;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.extraction.sentnence.CompaniesAll;
import ch.lgt.ming.extraction.sentnence.Extractor;
import ch.lgt.ming.extraction.sentnence.Merger;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.pipeline.Annotation;

//Test code for companynames and merger
public class Test2 {
	
	public static void main(String[] args) throws Exception {
		
		// variable declaration
		FileHandler fileHandler = new FileHandler();
		IdString docId_Text = new IdString();


		// initialize corenlp
		StanfordCore.init();
		
		// load corpus
		String path = "corpus";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		//load the documents
		for (int i = 0; i < 10; i++) {
			docId_Text.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
		}

		//process documents
		for (Integer key : docId_Text.getMap().keySet()) {

			double start = System.currentTimeMillis();


			Annotation annotation = StanfordCore.pipeline.process(docId_Text.getValue(key));

//			// Key - Company Name map
//			IdListString sentenceCompany = companyExtractor.extract(annotation);
//			List<List<String>> comlist = new ArrayList<>(sentenceCompany.getMap().values());
//			List<String> comflat = comlist.stream()
//							.flatMap(l -> l.stream())
//							.collect(Collectors.toList());
//
//			Set<String> comset = new HashSet<>(comflat);
//			System.out.println(comset);
//			// Key - IsMerger map
//			IdBoolean isMerger = mergerExtractor.extract(annotation);
//			System.out.println(isMerger);


			System.out.print("document " + key + " is done.");
			double end = System.currentTimeMillis();
			System.out.println(end - start);
		}





	}

}
