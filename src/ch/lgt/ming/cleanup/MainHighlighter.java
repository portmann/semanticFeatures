package ch.lgt.ming.cleanup;

package ch.lgt.news.highlighter;

import java.awt.Desktop;
import java.io.File;
import java.util.Scanner;

import ch.lgt.news.corpus.BlogEntry;
import ch.lgt.news.corpus.CorpusEngine;
import ch.lgt.news.corpus.Dictionary;
import ch.lgt.news.corpus.Sentence;
import ch.lgt.news.sentiment.HarvardWCSentiment;
import ch.lgt.news.sentiment.Sentiment;
import ch.lgt.news.sentiment.StanfordSentiment;
//import ch.lgt.news.sentiment.StanfordSentiment;
//import ch.lgt.news.standard.SQLHandler;
import ch.lgt.news.stanford.NLP;
import ch.lgt.news.standard.FileHandler;

public class MainHighlighter {

public static void main(String[] args) throws Exception {

//String sentimentString = "";

//This is how it works with the new datastructure
Scanner in = new Scanner(System.in);
System.out.println("DocumentId:");
int documentId = in.nextInt();
in.close();

NLP.init();

String dictionaryPath =
"Y:\\\\_LGTCP\\PF\\AAR\\AA\\13.INNOVATION\\SEMANTIC\\GrabberData\\BigDataSet_V3\\60k_dictionary_with_names.csv";
String rootPath =
"\\\\a2.loc\\dfs01\\_LGTCP\\PF\\AAR\\AA\\13.INNOVATION\\SEMANTIC\\GrabberData\\BigDataSet_V3";
String targetPath =
"\\\\a2.loc\\dfs01\\_LGTCP\\PF\\AAR\\AA\\13.INNOVATION\\SEMANTIC\\GrabberData\\BigDataSet_V3\\MarkedText";
String htmlString = "";
FileHandler fileHandler = new FileHandler();

Dictionary dictionary = new Dictionary(dictionaryPath);
Sentiment sentiment = new StanfordSentiment();

CorpusEngine corpusEngine = new CorpusEngine(dictionary, sentiment);
BlogEntry blog;
blog = new BlogEntry(documentId, rootPath);
blog.setSeededContent(corpusEngine.seedRawData(blog).getSeededContent());
corpusEngine.annotate(blog);
blog.setTokanizedContent(corpusEngine.tokanizeContent(blog).getTokanizedContent());
blog.setCodedContent(corpusEngine.codeContent(blog).getCodedContent());
blog.setSentences(corpusEngine.fillSentences(blog).getSentences());
htmlString = "<html><head><title>Sentiment</title><style
type='text/css'> .marked0 { background-color: #c01920 }"
+ " .marked1 { background-color: #f5bd35 } .marked2 {
background-color: #989898 } .marked3 { background-color: #52b5d4}"
+ " .marked4 { background-color: #94b758 }
</style></head><body><h1>Stanford</h1> ";

for (Sentence se : blog.getSentences())
{
//construct highlighted text
htmlString  = htmlString + "<span class='marked" +
se.getClassifiedSentiment() + "'>"
+ se.getSentenceText() + "</span>" + " ";
}
String fileName = targetPath + "\\highlighted_stanford"+"_"+
documentId +".html";
fileHandler.saveStringAsFile(fileName, htmlString);
File file = new File(fileName);
Desktop desktop = Desktop.getDesktop();
desktop.open(file);
//do it again for harvard
sentiment = new HarvardWCSentiment();
corpusEngine = new CorpusEngine(dictionary, sentiment);

blog = new BlogEntry(documentId, rootPath);
blog.setSeededContent(corpusEngine.seedRawData(blog).getSeededContent());
corpusEngine.annotate(blog);
blog.setTokanizedContent(corpusEngine.tokanizeContent(blog).getTokanizedContent());
blog.setCodedContent(corpusEngine.codeContent(blog).getCodedContent());
blog.setSentences(corpusEngine.fillSentences(blog).getSentences());
htmlString = "<html><head><title>Sentiment</title><style
type='text/css'> .marked0 { background-color: #c01920 }"
+ " .marked1 { background-color: #f5bd35 } .marked2 {
background-color: #989898 } .marked3 { background-color: #52b5d4}"
+ " .marked4 { background-color: #94b758 }
</style></head><body><h1>Harvard</h1> ";


for (Sentence se : blog.getSentences())
{
//construct highlighted text
htmlString  = htmlString + "<span class='marked" +
se.getClassifiedSentiment() + "'>"
+ se.getSentenceText() + "</span>" + " ";
}
fileName = targetPath + "\\highlighted_harvard"+"_"+ documentId +".html";
fileHandler.saveStringAsFile(fileName, htmlString);
file = new File(fileName);
desktop = Desktop.getDesktop();
desktop.open(file);
}}
