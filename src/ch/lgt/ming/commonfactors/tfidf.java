package ch.lgt.ming.commonfactors;

import ch.lgt.ming.datastore.*;
import ch.lgt.ming.helper.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    public List<String> getDict() {
        return Dict;
    }

    public IdListString getDocId_TokensList() {
        return DocId_TokensList;
    }

    public IdString getDocId_Text() {
        return DocId_Text;
    }

    public IdSetString getDocId_TokensSet() {
        return DocId_TokensSet;
    }

    public IdListDouble getDocId_tfidfList() {
        return DocId_tfidfList;
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public static void main(String[] args) throws IOException {
        tfidf tfIdf = new tfidf(3, "corpus2");
        tfIdf.ReadDict();
        tfIdf.DocProcess();
        IdListDouble DocId_TfidfList = tfIdf.getTfIdf();
        System.out.println(DocId_TfidfList.getValue(1));
        double cosinsimilarity = tfIdf.cosineSimilarity(DocId_TfidfList.getValue(1),DocId_TfidfList.getValue(2));
        System.out.println(cosinsimilarity);
        tfIdf.getSimilarDoc(0.1);
//        tfIdf.getKeyWords(DocId_TfidfList.getValue(1), 15);
    }
/**
*   Constructor: calculate the tfidf of a specific Corpus
*/
    public tfidf(int numberOfDocuments, String CorpusPath){
        this.numberOfDocuments = numberOfDocuments;
        this.CorpusPath = CorpusPath;

    }

/**
    To read dictionary into a Dict.
*/
    public void ReadDict() throws IOException {
        FileHandler fileHandler = new FileHandler();
        String myString[] = fileHandler.loadFileToString("dictionaries/60k_dictionary_with_names.csv").
                toLowerCase().split(System.getProperty("line.separator"));
        Dict = Arrays.asList(myString);
//        System.out.println(Dict);
//        System.out.println(Dict.get(0).equals("the"));
//        System.out.print("SIZE:" + Dict.size());
    }
/**
    To store all documents into an IdListString/IdSetString format,
    i.e. Document Index - List/Set of tokens of this document
*/
    public void DocProcess() throws IOException {

        FileHandler fileHandler = new FileHandler();

        //Load corpus
        File folder = new File(CorpusPath);
        File[] listOfFiles = folder.listFiles();

        //Load the documents
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
    }

/**
    Return the term frequency of termToCheck in the ith document
*/
    public double tf(int i, String termToCheck){                        //term frequency: how many times does each term
        double count = 0;                                               //appears in a document.
        for (String s: DocId_TokensList.getValue(i) ){
            if (s.equalsIgnoreCase(termToCheck)){
                count++;
            }
        }
        return count/DocId_TokensList.getValue(i).size();
    }

/**
    Return the inverse term frequency of termToCheck among all documents
*/
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


    //Get the tfidf of all documents
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

/**
    Calculate the cosine similarity between two documents
*/
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

/**
*   Return a matrix of the cosine similarity of the whole corpus
* */
    public List<Pair<Integer,Integer>> getSimilarDoc(double threshold){

        List<Pair<Integer,Integer>> SimilarDocIndex = new ArrayList<>();
        IdListDouble TFIDF = getTfIdf();
        double [][] cosineSimMatrix = new double[numberOfDocuments][numberOfDocuments];
        for (int i = 0; i < numberOfDocuments; i++)
            for (int j = 0; j < numberOfDocuments; j++){
                cosineSimMatrix[i][j] = cosineSimilarity(TFIDF.getValue(i),TFIDF.getValue(j));
                if (cosineSimMatrix[i][j] > threshold){
                    SimilarDocIndex.add(new Pair<>(i,j));
                }
            }
        System.out.println(Arrays.deepToString(cosineSimMatrix));
        return SimilarDocIndex;
    }

/**
    Get the first n keywords according to the tfidf
*/

    public List<String> getKeyWords(List<Double> tfidf, int n){

        ArrayIndexComparator comparator = new ArrayIndexComparator(tfidf);
        Integer[] indices = comparator.createIndexArray();
        Arrays.sort(indices, comparator);
        List<String> keywords = new ArrayList<>();
        for(int i = 0; i < n; i++){
            keywords.add(i,Dict.get(indices[i]));
//            System.out.println(Dict.get(indices[i]));
        }
        return  keywords;
    }

}

