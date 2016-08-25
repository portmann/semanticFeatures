package ch.lgt.ming.cleanup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ch.lgt.ming.helper.FileHandler;

public class Corpus {

	public Corpus(String path) {
		
		FileHandler fileHandler = new FileHandler();
		
		List<Document> documents = new ArrayList<Document>();
		  

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			
			try {
				
				documents.add(new Document(fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName())));
			
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
			System.out.println("Document: " + i + " done.");
		}
		
	}
}