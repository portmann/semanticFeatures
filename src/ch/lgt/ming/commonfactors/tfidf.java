package ch.lgt.ming.commonfactors;

import ch.lgt.ming.datastore.*;
import ch.lgt.ming.helper.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Ming Deng on 8/15/2016.
 */

public class tfidf {

    private int numberOfDocuments = 0;
    private String CorpusPath = new String();
    private IdString DocId_Text = new IdString();                                      //Document Index - Document Text String
    private List<String> Dict = new ArrayList<>();                                     //60k Dictionary - List of String
    private IdListString DocId_TokensList = new IdListString();                        //Document Index - List of tokens of this document
    private IdSetString DocId_TokensSet = new IdSetString();                           //Document Index - Set of tokens of this document
    private IdListDouble DocId_tfidfList = new IdListDouble();                         //Document Index - List of tfidf of each token
    private List<Integer> similarDoc = new ArrayList<>();



    public static void main(String[] args) throws IOException {
        tfidf tfIdf = new tfidf(3, "corpus2");
        List<String> Dict =  tfIdf.ReadDict();
        IdSetString DocID_Tokens =  tfIdf.DocProcess();
        IdListDouble TFIDF = tfIdf.getTfIdf();
        System.out.println(TFIDF.getValue(1));
        double cosinsimilarity = tfIdf.cosineSimilarity(TFIDF.getValue(1),TFIDF.getValue(2));
        System.out.println(cosinsimilarity);
        tfIdf.getSimilarDoc(0.1);
        tfIdf.getMaxIndex(TFIDF.getValue(2));

        System.out.println(Dict.get(0));
    }

    public tfidf(int numberOfDocuments, String CorpusPath){
        this.numberOfDocuments = numberOfDocuments;
        this.CorpusPath = CorpusPath;

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
        File folder = new File(CorpusPath);
        File[] listOfFiles = folder.listFiles();

        //load the documents
        for (int i = 0; i < numberOfDocuments; i++) {
            DocId_Text.putValue(i, fileHandler.loadFileToString(CorpusPath + "/" + listOfFiles[i].getName()));
//            System.out.println(DocId_Text.getValue(i));
            String[] strings = DocId_Text.getValue(i).replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
            List<String> listoftokens = Arrays.asList(strings);
            Set<String> setoftokens = new HashSet<>(listoftokens);
    //            System.out.println(setoftokens);
            DocId_TokensList.putValue(i,listoftokens);
            DocId_TokensSet.putValue(i,setoftokens);
        }

        return DocId_TokensSet;

    }

    public double tf(int key, String termToCheck){                      //term frequency: how many times does each term
        double count = 0;                                               //appears in a document.
        for (String s: DocId_TokensList.getValue(key) ){
            if (s.equalsIgnoreCase(termToCheck)){
                count++;
            }
        }
        return count/DocId_TokensList.getValue(key).size();
    }

    public double idf(String termToCheck){
        double count = 0;                                                //count is the number of documents that contains termToCheck
        for (int key: DocId_TokensSet.getMap().keySet()){                //For every document
            for (String s: DocId_TokensSet.getValue(key)){               //If the document contains termToCheck then count++
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return 1+Math.log(numberOfDocuments/count);
    }

    public IdListDouble getTfIdf(){

        for (int key: DocId_Text.getMap().keySet()){                     //Loop over all documents
            List<Double> tdidfVectors = new ArrayList<>(Dict.size());    //For each document, construct a vector to store the tdidf
            int count = 0;                                               //count is the index of word in the 60k dictionary
            for (String s: Dict){                                        //Loop over all words in dictionary
                if(DocId_TokensSet.getValue(key).contains(s.toLowerCase())){
                    double tf = tf(key,s);
                    double idf = idf(s);                                 //Only calculate tfidf when the word is in the document
                    double tfidf = tf*idf;
                    tdidfVectors.add(count,tfidf);
//                    System.out.println(tfidf);
//                    System.out.println(count);
                }
                else tdidfVectors.add(count,0.0);                        //Or set tfidf to be zero
                count++;
            }
//            Collections.sort(tdidfVectors);
//            Collections.reverse(tdidfVectors);
//            System.out.println(tdidfVectors);
            DocId_tfidfList.putValue(key, tdidfVectors);
        }
        return DocId_tfidfList;
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


        IdListDouble TFIDF = getTfIdf();
        double [][] cosineSimMatrix = new double[numberOfDocuments][numberOfDocuments];
        for (int i = 0; i < numberOfDocuments; i++)
            for (int j = 0; j < numberOfDocuments; j++){
                cosineSimMatrix[i][j] = cosineSimilarity(TFIDF.getValue(i),TFIDF.getValue(j));
            }
        System.out.println(Arrays.deepToString(cosineSimMatrix));
    }


    public void getKeyWords(){

    }

    public void getMaxIndex(List<Double> vector){
        IntStream.range(0,vector.size())
                .reduce((a,b)-> vector.get(a)<vector.get(b) ? b : a)
                .ifPresent(ix -> System.out.println("Index " + ix + " Value " + vector.get(ix)));

    }



}

