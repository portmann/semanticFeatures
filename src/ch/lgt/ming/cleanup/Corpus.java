package ch.lgt.ming.cleanup;

import java.io.*;
import java.util.*;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdListDouble;
import ch.lgt.ming.datastore.IdListString;
import ch.lgt.ming.datastore.IdSetString;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.util.CoreMap;

import javax.print.Doc;

public class Corpus implements Serializable{

	private List<Document> documents;
	private IdListDouble DocId_tfidfList = new IdListDouble();                         //Document Index - List of TFIDF of each word in the dictionary
	private List<Integer> DocId_DocName = new ArrayList<>();                           //Document Index - Document name(which is also an index)
	private List<Date> DocId_DocDate = new ArrayList<>();                              //Document Index - Document Date

	public Corpus(){
		documents = new ArrayList<>();
	}


	/**
	 * Constructor: create a corpus with a collection of documents
	 *
	 * @param path the path of the corpus
	 * @param annotation to decide if includes annotations in the documents
	 * */

	public Corpus(String path, boolean annotation) throws IOException {

		FileHandler fileHandler = new FileHandler();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		documents = new ArrayList<>();

		/**
		 * This part reads the Date from DataTime.ser
		* */
		Map<Integer,Date> DocTime = new HashMap<>();
		FileInputStream fileInputStream;
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

		/**
		 * This part stores documents into corpus
		 * */

		for (int i = 0; i < listOfFiles.length; i++) {

			try {
				Integer index = Integer.valueOf(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')));
				documents.add(new Document(fileHandler.loadFileToString(listOfFiles[i].getPath()),index, DocTime.get(index), annotation));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Document " + i + ": " + listOfFiles[i].getName() + " done.");
		}

	}

	public static void main(String[] args) throws IOException {
		/**
		 * This part of code
		* */

	}

	
	public int getDocCount(){
		
		return this.documents.size();
		
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public Document getDocument(int i){
		return documents.get(i);
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public void addDocument(Document document){
		this.documents.add(document);
	}

}