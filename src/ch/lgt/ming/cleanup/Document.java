package ch.lgt.ming.cleanup;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Document Class stores the information of each document including document text, sentence, token, Stanford Annotation,
 * Date of each doc, index of each doc.
* */
public class Document implements Serializable {

	private Integer index;
	private Date date;
	private String documentText = "";
	private List<String> sentenceText = new ArrayList<>();
	private List<String> tokenTextList = new ArrayList<>();
	private Set<String> tokenTextSet = new HashSet<>();
	private Annotation document;
	private List<Double> tfidf;
	private List<Integer> closestDocuments;

	/**
	 * Creates Document without annotations
	 * */
	public Document(String documentText, Integer index, Date date, boolean annotation){

		this.index = index;
		this.date = date;
		this.documentText = documentText;

		String[] strings = documentText.replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
		this.tokenTextList = Arrays.asList(strings).stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList());
		this.tokenTextSet = new HashSet<>(this.tokenTextList);

		if (annotation){

			StanfordCore.init();
			this.document = StanfordCore.pipeline.process(documentText);
			for (CoreMap sentenceStanford : document.get(CoreAnnotations.SentencesAnnotation.class)) {
				// Put text contents into textSentenceID
				sentenceText.add(sentenceStanford.get(CoreAnnotations.TextAnnotation.class));
			}

		}

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
		for (int i = 0; i < 1520; i++) {
			Integer index = Integer.valueOf(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')));
			Document document = new Document(fileHandler.loadFileToString(listOfFiles[i].getPath()), index, DocTime.get(index),true);
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

	public List<String> getTokenTextList() {
		return tokenTextList;
	}

	public void setTokenTextList(List<String> tokenTextList) {
		this.tokenTextList = tokenTextList;
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

	public Set<String> getTokenTextSet() {
		return tokenTextSet;
	}

	public void setTokenTextSet(Set<String> tokenTextSet) {
		this.tokenTextSet = tokenTextSet;
	}

	public List<Double> getTfidf() {
		return tfidf;
	}

	public void setTfidf(List<Double> tfidf) {
		this.tfidf = tfidf;
	}

	public List<Integer> getClosestDocuments() {
		return closestDocuments;
	}

	public void setClosestDocuments(List<Integer> closestDocuments) {
		this.closestDocuments = closestDocuments;
	}
}
