package ch.lgt.ming.cleanup;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.scoref.SimpleLinearClassifier;
import edu.stanford.nlp.util.CoreMap;

import javax.print.Doc;

/**
 * Document Class stores the information of each document including document text, sentence, token, Stanford Annotation,
 * Date of each doc, index of each doc.
* */
public class Document implements Serializable {

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
		File folder = new File("data/corpus6");
		File[] listOfFiles = folder.listFiles();

		StanfordCore.init();
		for (int i = 4000; i < 4210; i++) {
			Integer index = Integer.valueOf(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')));
			Document document = new Document(fileHandler.loadFileToString(listOfFiles[i].getPath()), index, DocTime.get(index));
			System.out.printf("%d is done\n", i);
			ObjectOutputStream objectOutputStream;
			try {
				objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/corpus7/" +
						listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')) + ".ser"));
				objectOutputStream.writeObject(document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

/**
 * Reading Documents.ser
* */
//		FileHandler fileHandler = new FileHandler();
//		File folder = new File("data/corpus7");
//		File[] listOfFiles = folder.listFiles();
//		Corpus corpus = new Corpus();
//
//		FileInputStream fileInputStream = null;
//
//		for (int i = 0; i < listOfFiles.length; i++){
//
//			try {
//				fileInputStream = new FileInputStream("data/corpus7/" + listOfFiles[i].getName());
//				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//				Document document = (Document) objectInputStream.readObject();
//				corpus.addDocument(document);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.out.printf("%d is done\n",i);
//		}
//	}

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
