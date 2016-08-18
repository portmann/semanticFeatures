package ch.lgt.ming.commonfactors;

import ch.lgt.ming.datastore.*;
import ch.lgt.ming.helper.FileHandler;
import com.sun.org.apache.xpath.internal.SourceTree;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentProcessor;
import edu.stanford.nlp.process.PTBTokenizer;

import javax.print.Doc;
import javax.print.DocPrintJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Ming Deng on 8/15/2016.
 */

public class tfidf {

    private int numberOfDocument = 0;
    private IdString docId_Text = new IdString();
    private List<String> stopwords = new ArrayList<>();
    private List<String> Dict = new ArrayList<>();
    private IdSetString DocId_Tokens = new IdSetString();
    private IdListDouble DocId_tfidfArray = new IdListDouble();
    private List<Integer> similarDoc = new ArrayList<>();



    public static void main(String[] args) throws IOException {
        tfidf tfIdf = new tfidf(5);
        List<String> Dict =  tfIdf.ReadDict();
        IdSetString DocID_Tokens =  tfIdf.DocProcess();
        IdListDouble TFIDF = tfIdf.getTfIdf();
        double cosinsimilarity = tfIdf.cosineSimilarity(TFIDF.getValue(4),TFIDF.getValue(4));
        System.out.println(cosinsimilarity);
    }


    public tfidf(int numberOfDocument){
        this.numberOfDocument = numberOfDocument;

    }

    public tfidf(int numberOfDocument, String stopword_filename){
        this.numberOfDocument = numberOfDocument;
    }

    public  List<String> ReadDict() throws IOException {
        FileHandler fileHandler = new FileHandler();
        String myString[] = fileHandler.loadFileToString("dictionaries/60k_dictionary_with_names.csv").
                toLowerCase().split(System.getProperty("line.separator"));
        Dict = Arrays.asList(myString);
//        System.out.println(Dict);
//        System.out.println(Dict.get(0).equals("the"));
//        System.out.print("SIZE:" + Dict.size());
        return Dict;
    }

    public IdSetString DocProcess() throws IOException {

        FileHandler fileHandler = new FileHandler();

        // load corpus
        String path = "corpus";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        //load the documents
        for (int i = 0; i < numberOfDocument; i++) {
            docId_Text.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
//            System.out.println(docId_Text.getValue(i));
            String[] strings = docId_Text.getValue(i).replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
            List<String> listoftokens = Arrays.asList(strings);
            Set<String> setoftokens = new HashSet<>(listoftokens);
    //            System.out.println(setoftokens);
            DocId_Tokens.putValue(i,setoftokens);
        }

        return DocId_Tokens;

    }

    public double tf(int key, String termToCheck){
        double count = 0;
        for (String s: DocId_Tokens.getValue(key) ){
            if (s.equalsIgnoreCase(termToCheck)){
                count++;
            }
        }
        return count/DocId_Tokens.getValue(key).size();
    }

    public double idf(String termToCheck){
        double count = 0;                                               //count is the number of documents that contains termToCheck
        for (int key: DocId_Tokens.getMap().keySet()){                  //For every document
            for (String s: DocId_Tokens.getValue(key)){                 //If the document contains termToCheck then count++
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return 1+Math.log(DocId_Tokens.getMap().keySet().size()/count);
    }

    public IdListDouble getTfIdf(){

        for (int key: DocId_Tokens.getMap().keySet()){         //Loop over document
            List<Double> tdidfvectors = new ArrayList<>(Dict.size());
//            System.out.println(Dict.size());
            int count = 0;
            for (String s: Dict){       //Loop over words in dictionary
//                System.out.println(DocID_Tokens.getValue(key));
                if(DocId_Tokens.getValue(key).contains(s.toLowerCase())){
                    double tf = tf(key,s);
                    double idf = idf(s);
                    double tfidf = tf*idf;
                    tdidfvectors.add(count,tfidf);
//                    System.out.println(tfidf);
//                    System.out.println(count);
                }
                else tdidfvectors.add(count,0.0);
                count++;
            }
//            Collections.sort(tdidfvectors);
//            Collections.reverse(tdidfvectors);
//            System.out.println(tdidfvectors);
            DocId_tfidfArray.putValue(key,tdidfvectors);
        }
        return DocId_tfidfArray;
    }

    public double cosineSimilarity(List<Double> tfidf1, List<Double> tfidf2){

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        double cosineSimilarity = 0.0;

        for (int i = 0; i < tfidf1.size(); i++){
            dotProduct += tfidf1.get(i) * tfidf2.get(i);
            norm1 += Math.pow(tfidf1.get(i),2);
            norm2 += Math.pow(tfidf2.get(i),2);
        }

        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);

        if (norm1 != 0.0 | norm2 != 0.0) {
            cosineSimilarity = dotProduct / (norm1 * norm2);

        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }

    public void getSimilarDoc(double threshold){

        double [][] cosineSimMatrix = new double[][];
        for (int i = 0; i < numberOfDocument; i++)
            for (int j = 0; j < numberOfDocument; j++){

            }
    }
    public void getKeyWords(){

    }



}

