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

public class Test2 {
	
	public static void main(String[] args) throws Exception {
		
		// variable declaration
		FileHandler fileHandler = new FileHandler();
		IdString documentText = new IdString(); 
		Extractor<IdListString> companyExtractor = new CompaniesAll();
		Extractor<IdBoolean> mergerExtractor = new Merger();

		// initialize corenlp
		StanfordCore.init();
		
		// load corpus
		String path = "corpus";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		//load just the first documents
		for (int i = 0; i < 5; i++) {
			documentText.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
		}

		//process documents
		for (Integer key : documentText.getMap().keySet()) {

			double time = System.currentTimeMillis();

			System.out.print("document " + key + " :");

			Annotation annotation = StanfordCore.pipeline.process(documentText.getValue(key));

			// Key - Company Name map
			IdListString sentenceCompany = companyExtractor.extract(annotation);
			List<List<String>> comlist = new ArrayList<>(sentenceCompany.getMap().values());
			List<String> comflat = comlist.stream()
							.flatMap(l -> l.stream())
							.collect(Collectors.toList());

			Set<String> comset = new HashSet<>(comflat);
			System.out.println(comset);
			// Key - IsMerger map
			IdBoolean isMerger = mergerExtractor.extract(annotation);
			System.out.println(isMerger);


			double time2 = System.currentTimeMillis();
			System.out.println(time2 - time);
		}





	}

}
