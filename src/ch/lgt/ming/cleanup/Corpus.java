package ch.lgt.ming.cleanup;

import java.io.*;
import java.util.*;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.util.CoreMap;

public class Corpus implements Serializable{

	List<Document> documents;

	public Corpus(String path) throws IOException {

		FileHandler fileHandler = new FileHandler();

		documents = new ArrayList<>();

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		Map<Integer,Date> DocTime = new HashMap<>();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream("data/corpus4/DataTime.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			DocTime = (Map<Integer, Date>) objectInputStream.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(DocTime);

		for (int i = 0; i < 100; i++) {

			try {

				Integer index = Integer.valueOf(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')));
				documents.add(new Document(fileHandler.loadFileToString(listOfFiles[i].getPath()),index, DocTime.get(index)));

			} catch (IOException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}

			System.out.println("Document: " + listOfFiles[i].getName() + " done.");
		}

	}

	public static void main(String[] args) throws IOException {
		StanfordCore.init();
		Corpus corpus = new Corpus("data/corpus5/Amazon");
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/corpus5/Amazon100.ser"));
			objectOutputStream.writeObject(corpus);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Corpus corpus = null;
//		FileInputStream fileInputStream = null;
//		try {
//			fileInputStream = new FileInputStream("data/corpus4/Amazon100.ser");
//			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//			corpus = (Corpus) objectInputStream.readObject();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println(corpus.getDocumentId());
//		System.out.println(corpus.getDocuments().get(3).getTokenText());

	}
	
	public int getDocCount(){
		
		return this.documents.size();
		
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

}