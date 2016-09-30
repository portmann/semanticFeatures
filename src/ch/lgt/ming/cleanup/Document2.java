
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
public class Document2 implements Serializable {

    private Integer index;
    private Date date;
    private String documentText = "";
    private List<String> sentenceText = new ArrayList<>();
    private List<String> tokenTextList = new ArrayList<>();
    private Set<String> tokenTextSet = new HashSet<>();
	private List<Double> tfidf;
	private List<Integer> closestDocuments;

    /**
     * Creates Document without annotations
     * */
    public Document2(String documentText, Integer index, Date date){

        this.index = index;
        this.date = date;
        this.documentText = documentText;

        String[] strings = documentText.replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
        this.tokenTextList = Arrays.asList(strings).stream()
//                .map(String::toLowerCase)
                .collect(Collectors.toList());
        this.tokenTextSet = new HashSet<>(this.tokenTextList);

    }

    public static void main(String[] args) throws IOException, ParseException {

    }
    public String getDocumentText() {
        return documentText;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
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
