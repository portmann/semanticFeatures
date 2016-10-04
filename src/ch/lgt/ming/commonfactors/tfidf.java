package ch.lgt.ming.commonfactors;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.cleanup.Document2;
import ch.lgt.ming.datastore.*;
import ch.lgt.ming.helper.FileHandler;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ming Deng on 8/15/2016.
 */

public class tfidf {

    private Corpus corpus = new Corpus();
    private int numberOfDocuments = 0;
    private List<String> Dict = new ArrayList<>();                                     //60k Dictionary - List of String
    private List<String> StopwordList = new ArrayList<>();                             //Stopword - List of stopwords
    private List<Double> idfList = new ArrayList<>();                                  //List of idf of each word based on the whole corpus
    private List<Integer> DocId_DocName = new ArrayList<>();
    private Date date = new Date();
    private int timeInterval;
    double [][] cosineSimMatrix;

    public static void main(String[] args) throws IOException, ParseException {

        Corpus corpus = new Corpus("data/corpusBoris");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-13");
        tfidf tfIdf = new tfidf(corpus, corpus.getDocCount(), date, 50);
        tfIdf.calculateClosestPredecessor();

        /**
         * This is to test function getKeyWords, kwyWordsOverlap,
        * */
        System.out.println(tfIdf.getKeyWords(tfIdf.corpus.getDocument(0).getTfidf(),10));
        System.out.println(tfIdf.getKeyWords(tfIdf.corpus.getDocument(1).getTfidf(),10));
        List<String> commonKeywords = tfIdf.getCommonKeyWords(tfIdf.corpus.getDocument(0).getTfidf(), tfIdf.corpus.getDocument(1).getTfidf(), 50);
        System.out.println("commonKeywords: " + commonKeywords);
//
//        double ratio = tfIdf.keyWordsOverlap(tfIdf.corpus.getDocument(0).getTfidf(),tfIdf.corpus.getDocument(1).getTfidf(),50);
//        System.out.println("keywords overlap ratio: " + ratio);
//
//        double cosineSim = tfIdf.getCosineSimilarity(tfIdf.corpus.getDocument(0).getTfidf(),tfIdf.corpus.getDocument(1).getTfidf());
//        System.out.println("Cosine similarity: " + cosineSim);

        /**
         * This is to test if there's huge difference between using cosine similarity or key words intersection to decide
         * the closest predecessor.
        * */
//        for (int i = 0; i < tfIdf.getNumberOfDocuments(); i++){
//            System.out.printf("-----------------------Doc %d-----------------------------------\n",i);
//            int n = tfIdf.getClosestPredecessor(tfIdf.corpus.getDocument(i).getTfidf(), tfIdf.corpus.getDocument(i).getIndex(),
//                    tfIdf.corpus.getDocument(0).getDate(), 50, 5, true);
//            int m = tfIdf.getClosestPredecessor(tfIdf.corpus.getDocument(i).getTfidf(), tfIdf.corpus.getDocument(i).getIndex(),
//                    tfIdf.corpus.getDocument(0).getDate(), 50, 5, false);
//            List<String> commonKeywords1 = tfIdf.getCommonKeyWords(tfIdf.corpus.getDocument(i).getTfidf(),
//                    tfIdf.corpus.getDocument(tfIdf.getDocId_DocName().indexOf(n)).getTfidf(), 50);
//            System.out.println(commonKeywords1);
//            List<String> commonKeywords2 = tfIdf.getCommonKeyWords(tfIdf.corpus.getDocument(i).getTfidf(),
//                    tfIdf.corpus.getDocument(tfIdf.getDocId_DocName().indexOf(m)).getTfidf(), 50);
//            System.out.println(commonKeywords2);
//        }
    }


    /**
     *  Constructor: calculate the tf-idf of a specific Corpus
     *
     *  @param corpus the corpus to process
     */

    public tfidf(Corpus corpus, int numberOfDocuments, Date date, int timeInterval) throws IOException {

        for (int i = -timeInterval; i < timeInterval+1; i++){
            Date date1 = DateUtil.addDays(date, i);
            for (int j = 0; j < numberOfDocuments; j++){
                if (corpus.getDocument(j).getDate().equals(date1)){
                    this.corpus.addDocument(corpus.getDocument(j));
                }
            }
        }
        this.numberOfDocuments = this.corpus.getDocCount();
        this.date = date;
        System.out.println(this.numberOfDocuments);
        ReadDict(false);
        ReadStopword();
        ReadIdf();
        calculateTfIdf();
    }

    /**
     * This function reads dictionary into the Dict variable of the class.
    */
    public void ReadDict(boolean b) throws IOException {
        if (b) {
            FileHandler fileHandler = new FileHandler();
            String myString[] = fileHandler.loadFileToString("data/dictionaries/60k_dictionary_with_names.csv").
                    toLowerCase().split(System.getProperty("line.separator"));
            Dict = Arrays.asList(myString);
//        System.out.println(Dict);
//        System.out.println(Dict.get(0).equals("the"));
//        System.out.print("SIZE:" + Dict.size());
        }else {
            Set<String> corpusSet = new HashSet<>();
            for (int i = 0; i < numberOfDocuments; i++){
                 corpusSet.addAll(corpus.getDocument(i).getTokenTextSet());
            }
            Dict = new ArrayList<>(corpusSet);
            System.out.println(Dict.size());

        }
    }

    /**
     * This function reads the word-list for stop-words into the StopwordList variable of the class.
     */
    public void ReadStopword() throws IOException {
        FileHandler fileHandler = new FileHandler();
        String myString[] = fileHandler.loadFileToString("data/dictionaries/stopwords.txt").
                toLowerCase().split(System.getProperty("line.separator"));
        StopwordList = Arrays.asList(myString);
//        System.out.println(StopwordList);
    }

    /**
     * This function reads the idf value into the idfList variable of the class.
     */
    public void ReadIdf() throws IOException {

        FileHandler fileHandler = new FileHandler();
        String myString[] = fileHandler.loadFileToString("data/dictionaries/idfs.txt").split(System.getProperty("line.separator"));
        List<String> strings = Arrays.asList(myString);
        for(int i = 0; i < strings.size(); i++){
            idfList.add(i, Double.valueOf(strings.get(i)));
        }

    }

    /**
     *  This function returns the term frequency(how many times does each term appear in a document)
     *  of termToCheck in the ith document.
     *
     *  @param i the index of the document
     *  @param termToCheck the term we need to check in the document
     *
     *  @return  the term frequency of termToCheck in the ith document
    */
    public double tf(int i, String termToCheck){
        double count = 0;
        for (String s: corpus.getDocument(i).getTokenTextList()){
            if (s.equalsIgnoreCase(termToCheck)){
//                System.out.println(s);
                count++;
            }
        }
        return count/corpus.getDocument(i).getTokenTextList().size();
    }

    /**
     *  This function returns the inverse term frequency of termToCheck among all documents
     *
     *  @param termToCheck the term we need to check in the corpus
     *
     *  @return  the inverse document frequency of termToCheck in the corpus
     *
    */
    public double idf(String termToCheck){
        double count = 0;                                                                                               //count denotes the number of documents that contains termToCheck
        for (int i = 0; i < numberOfDocuments; i++){                                                                 //For every document
            if (corpus.getDocument(i).getTokenTextSet().contains(termToCheck)) {                         //If the document contains termToCheck then count++
                    count++;
                    break;
            }
        }
        return 1 + Math.log(numberOfDocuments/count);                                                                     //If every document contains the termToCheck,
    }                                                                                                                   //then the idf should be 1. idf is decreasing with count.


    /**
     * This function calculates the tf-idf vector of all the documents
     */
    public void calculateTfIdf(){
        for (int i = 0; i < numberOfDocuments; i++){                                                                    //Loop over all documents. For each document, construct a vector to store the tdidf.
//            long start = System.currentTimeMillis();
            List<Double> tfidfVector = new ArrayList<>(Dict.size());
            for (int j = 0; j < Dict.size(); j++){                                                                      //Loop over the dictionary. For each word, calculate its tf and idf
                if (corpus.getDocument(i).getTokenTextSet().contains(Dict.get(j).toLowerCase())&&                       //Only calculate tf-idf when the word is in the document
                        !StopwordList.contains(Dict.get(j).toLowerCase())){                                             //and it's not a stop-word
                    double tf = tf(i, Dict.get(j));
                    double idf = idf(Dict.get(j));
//                    double idf = idfList.get(j);
                    double tfidf = tf*idf;
//                    System.out.printf("Document %d, The tf of '%s' is '%f'\n", i, j, tf);
//                    System.out.printf("Document %d, The idf of '%s' is '%f'\n", i, j, idf);
//                    System.out.printf("Document %d, The tfidf of '%s' is '%f'\n", i, j, tfidf);
                    tfidfVector.add(j,tfidf);
//                    System.out.println(TFIDF);
//                    System.out.println(count);
                } else tfidfVector.add(j, 0.0);                                              //Or set tf-idf to be zero
            }
//            this.DocId_tfidf.putValue(i, tfidfVector);
            corpus.getDocument(i).setTfidf(tfidfVector);
//            long end = System.currentTimeMillis();
//            System.out.println(end - start);
            System.out.println("Document " + i + ": " + corpus.getDocument(i).getIndex() + " done.");
        }
    }

    /**
     * This function calculates the cosine similarity between two documents
     *
     * @param point1 the coordinate of the first document
     * @param point2 the coordinate of the second document
     *
     * @return the cosine similarity between the two documents
    */
    public static double getCosineSimilarity(List<Double> point1, List<Double> point2){

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        double cosineSimilarity;

        for (int i = 0; i < point1.size(); i++){
            dotProduct += point1.get(i) * point2.get(i);
            norm1 += Math.pow(point1.get(i),2);
            norm2 += Math.pow(point2.get(i),2);
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
     *  This function returns the matrix of the cosine similarity of the whole corpus
     *
     *  @param threshold the threshold for cosine similarity
     *
     *  @return pairs of similar documents
     *
    * */
    public List<Pair<Integer,Integer>> getCosineMatrix(double threshold){

        List<Pair<Integer,Integer>> SimilarDocIndex = new ArrayList<>();
        cosineSimMatrix = new double[numberOfDocuments][numberOfDocuments];
        for (int i = 0; i < numberOfDocuments; i++)
            for (int j = i + 1; j < numberOfDocuments; j++){
                cosineSimMatrix[i][j] = getCosineSimilarity(corpus.getDocument(i).getTfidf(),corpus.getDocument(j).getTfidf());
                if (cosineSimMatrix[i][j] > threshold){
                    SimilarDocIndex.add(new Pair<>(i,j));
                }
            }
        System.out.println("The cosine similarity matrix is: ");
        System.out.println(Arrays.deepToString(cosineSimMatrix));
        return SimilarDocIndex;
    }

    /**
     * This function gets the first n keywords according to its tf-idf value
     *
     * @param tfidf the tf-idf vector of the document
     * @param n number of keywords considered
     *
     * @return  List of the first n keywords
    */

    public List<String> getKeyWords(List<Double> tfidf, int n){

        ArrayIndexComparator comparator = new ArrayIndexComparator(tfidf);
        Integer[] indices = comparator.createIndexArray();
        Arrays.sort(indices, comparator);
        List<String> keywords = new ArrayList<>();
        for(int i = 0; i < n; i++){
            keywords.add(i, Dict.get(indices[i]));
//            System.out.println("index: " + indices[i] + " " + Dict.get(indices[i]) + " " + tfidf.get(indices[i]));
        }
        return  keywords;
    }

    /**
     * This function gets the ratio of overlap of same tokens of the first n keywords
     *
     * @param tfidf1 the first tf-idf vector
     * @param tfidf2 the second tf-idf vector
     * @param n number of keywords considered
     * @return ratio of overlap
     */

    public double keyWordsOverlap(List<Double> tfidf1, List<Double> tfidf2, int n){

        List<String> KeyWords1 = getKeyWords(tfidf1,n);
        List<String> KeyWords2 = getKeyWords(tfidf2,n);
        KeyWords1.retainAll(KeyWords2);
//        System.out.println(KeyWords1);
        double overlap = KeyWords1.size()/(double)n;
        return overlap;
    }

    /**
     * This function gets the common key words of two documents based on their tf-idf vector
     *
     * @param tfidf1 the first tf-idf vector
     * @param tfidf2 the second tf-idf vector
     * @param n number of keywords considered
     *
     * @return list of common key words
     */

    public List<String> getCommonKeyWords(List<Double> tfidf1, List<Double> tfidf2, int n){

        List<String> KeyWords1 = getKeyWords(tfidf1,n);
        List<String> KeyWords2 = getKeyWords(tfidf2,n);
        KeyWords1.retainAll(KeyWords2);
        return KeyWords1;
    }

    /**
     * This function gets the closest predecessor of current document based on the intersection of first n keywords
     *
     * @param  tfidf tfidf of the document
     * @param  id index of current document
     * @param  date date of current document
     * @param  n number of keywords considered
     * @param  timeHorizon time horizon considered
     *
     * @return index of the closest predecessor
     */

    public int getClosestPredecessor(List<Double> tfidf, int id, Date date, int n, int timeHorizon, boolean isCosine){

        double ratio = 0;
        Integer index = id;
        for (int i = 0; i < timeHorizon + 1; i++){
            Date date1 = DateUtil.addDays(date, -i);
            for (int j = 0; j <  numberOfDocuments && corpus.getDocument(j).getIndex() != id; j++){
                if (corpus.getDocument(j).getDate().equals(date1)){
                    if (isCosine){
                        double ratio2 = getCosineSimilarity(tfidf, corpus.getDocument(j).getTfidf());
                        if (ratio2 > ratio){
                            ratio = ratio2;
                            index = corpus.getDocument(j).getIndex();
                        }
                    }else {
                        double ratio2 = keyWordsOverlap(tfidf, corpus.getDocument(j).getTfidf(), n);
                        if (ratio2 > ratio){
                            ratio = ratio2;
                            index = corpus.getDocument(j).getIndex();
                        }
                    }
                }
            }
        }
//        System.out.printf("The closest predecessor of doc %d within %d days is %d\n", id, timeHorizon, index);
        return index;
    }

    public void calculateClosestPredecessor(){
        for (int i = 0; i < numberOfDocuments; i++){
            Document2 document = corpus.getDocument(i);
            List<Integer> closestDocuments = new ArrayList<>();
            for (int j = 0; j < 8; j++){
                int index = getClosestPredecessor(document.getTfidf(), document.getIndex(), document.getDate(), 50,j, false);
                closestDocuments.add(j, index);
            }
            document.setClosestDocuments(closestDocuments);
        }
    }

    /**
     * This function gets the common factors of the corpus
     *
     * @param numberofKeyWords number of keywords considered
     *
     * @return list of common key words
     */
    @Deprecated
    public List<String> getCommonFactors(int numberofKeyWords){

        List<String> CommonFactors = getKeyWords(corpus.getDocument(1).getTfidf(), numberofKeyWords);
        for (int i = 0; i < numberOfDocuments; i++) {
            List<String> keywords = getKeyWords(corpus.getDocument(i).getTfidf(), numberofKeyWords);
            CommonFactors.retainAll(keywords);
        }
        System.out.println("Common Factors: " + CommonFactors);
        return  CommonFactors;

    }
    public List<String> getDict() {
        return Dict;
    }

    public List<String> getStopwordList() {
        return StopwordList;
    }

    public List<Double> getIdfList() {
        return idfList;
    }

    public List<Integer> getDocId_DocName() {
        return DocId_DocName;
    }

    public double[][] getCosineSimMatrix() {
        return cosineSimMatrix;
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public Date getDate() {
        return date;
    }

    public int getTimeInterval() {
        return timeInterval;
    }
}

