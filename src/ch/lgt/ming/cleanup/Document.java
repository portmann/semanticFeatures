package ch.lgt.ming.cleanup;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ming Deng on 9/30/2016.
 */
public class Document implements Serializable{

	String documentText = "";
	List<String> sentenceText = new ArrayList<>();
	List<String> tokenText = new ArrayList<>();
	Annotation document;
	Date date;
	Integer index;

	/**
	 * Creates Document with annotations
	 * */
	public Document(String documentText, Integer index, Date date){

		this.documentText = documentText;

		this.document = StanfordCore.pipeline.process(documentText);

		for (CoreMap sentenceStanford : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			// Put text contents into textSentenceID
			sentenceText.add(sentenceStanford.get(CoreAnnotations.TextAnnotation.class));
		}

		String[] strings = documentText.replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
		this.tokenText = Arrays.asList(strings).stream()
//				.map(String::toLowerCase)
				.collect(Collectors.toList());

		this.index = index;
		this.date = date;

	}

	/**
	 * Creates Document without annotations
	 * */
	public Document(String documentText, Integer index, Date date, boolean annotation){
		this.documentText = documentText;

		String[] strings = documentText.replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
		this.tokenText = Arrays.asList(strings).stream()
//				.map(String::toLowerCase)
				.collect(Collectors.toList());

		this.index = index;
		this.date = date;
	}

	public static void main(String[] args) throws IOException, ParseException {

	/**
	 * Writing Documents.ser
	 * */
		Map<Integer, Date> DocTime = new HashMap<>();
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

		FileHandler fileHandler = new FileHandler();
		File folder = new File("data/corpusBoris");
		File[] listOfFiles = folder.listFiles();

		StanfordCore.init();
		for (int i = 172; i < 1520; i++) {
			Integer index = Integer.valueOf(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')));
			Document document = new Document(fileHandler.loadFileToString(listOfFiles[i].getPath()), index, DocTime.get(index));
			System.out.printf("%d is done\n", i);
			ObjectOutputStream objectOutputStream;
			try {
				objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/corpusBoris2/" +
						listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')) + ".ser"));
				objectOutputStream.writeObject(document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Reading Documents.ser
		 * */
//		File folder = new File("data/corpus7");
//		File[] listOfFiles = folder.listFiles();
//
//		FileInputStream fileInputStream;
//
//		for (int i = 0; i < 10; i++){
//
//			try {
//				fileInputStream = new FileInputStream("data/corpus7/" + listOfFiles[i].getName());
//				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//				Document document = (Document) objectInputStream.readObject();
//				System.out.println(document.getDocument());
//				System.out.printf("%d is done\n",i);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public String getDocumentText() {
		return documentText;
	}

	public void setDocumentText(String documentText) {
		this.documentText = documentText;
	}

	public Annotation getDocument() {
		return document;
	}

	public void setDocument(Annotation document) {
		this.document = document;
	}

	public List<String> getSentenceText() {
		return sentenceText;
	}

	public void setSentenceText(List<String> sentence) {
		this.sentenceText = sentence;
	}

	public List<String> getTokenText() {
		return tokenText;
	}

	public void setTokenText(List<String> tokenText) {
		this.tokenText = tokenText;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}

