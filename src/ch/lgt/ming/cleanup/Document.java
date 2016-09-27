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
