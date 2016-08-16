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

/**
 * Created by Ming Deng on 8/15/2016.
 */

public class tfidf {

    private IdString docId_Text = new IdString();
    private List<String> stopwords = new ArrayList<>();
    private String corpus_location;
    private List<String> Dict = new ArrayList<>();
    private IdSetString DocID_Tokens = new IdSetString();
    private IdListDouble DocId_tfidfArray = new IdListDouble();


    public static void main(String[] args) throws IOException {
        tfidf tfIdf = new tfidf();
        List<String> Dict =  tfIdf.ReadDict();
        IdSetString DocID_Tokens =  tfIdf.DocProcess();
        IdListDouble TFIDF = tfIdf.getTfIdf();
    }


    public tfidf(){

    }

    public tfidf(String corpus_location, String stopword_filename){

        this.corpus_location = corpus_location;

    }

    public  List<String> ReadDict() throws IOException {
        FileHandler fileHandler = new FileHandler();
        String myString[] = fileHandler.loadFileToString("dictionaries/60k_dictionary_with_names.csv").
                toLowerCase().split(System.getProperty("line.separator"));
        Dict = Arrays.asList(myString);
        System.out.println(Dict);
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
        for (int i = 0; i < 1; i++) {
            docId_Text.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
//            System.out.println(docId_Text.getValue(i));
            String[] strings = docId_Text.getValue(i).replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
            List<String> listoftokens = Arrays.asList(strings);
            Set<String> setoftokens = new HashSet<>(listoftokens);
    //            System.out.println(setoftokens);
            DocID_Tokens.putValue(i,setoftokens);
        }

        return DocID_Tokens;

    }


    public IdListDouble getTfIdf(){

        for (int key: DocID_Tokens.getMap().keySet()){         //Loop over document
            List<Double> tdidfvectors = new ArrayList<>(Dict.size());
//            System.out.println(Dict.size());
            int count = 0;
            for (String s: Dict){       //Loop over words in dictionary
//                System.out.println(DocID_Tokens.getValue(key));
                if(DocID_Tokens.getValue(key).contains(s.toLowerCase())){
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
            System.out.println(tdidfvectors);
            DocId_tfidfArray.putValue(key,tdidfvectors);
        }
        return DocId_tfidfArray;
    }


    public double tf(int key, String termToCheck){
        double count = 0;
        for (String s: DocID_Tokens.getValue(key) ){
            if (s.equalsIgnoreCase(termToCheck)){
                count++;
            }
        }
        return count/DocID_Tokens.getValue(key).size();
    }

    public double idf(String termToCheck){
        double count = 0;
        for (int key: DocID_Tokens.getMap().keySet()){
            for (String s: DocID_Tokens.getValue(key)){
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return 1+Math.log(DocID_Tokens.getMap().keySet().size()/count);
    }

    public double cosinesim(List<Double> tfidf1, List<Double> tfidf2){
        return 0.0;

    }

}

