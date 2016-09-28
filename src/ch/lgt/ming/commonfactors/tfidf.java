package ch.lgt.ming.commonfactors;

import ch.lgt.ming.datastore.*;
import ch.lgt.ming.helper.FileHandler;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ming Deng on 8/15/2016.
 */

public class TFIDF {

    private int numberOfDocuments = 0;
    private String CorpusPath = new String();
    private List<String> Dict = new ArrayList<>();                                     //60k Dictionary - List of String
    private List<String> StopwordList = new ArrayList<>();                             //Stopword - List of stopwords
    private List<Double> idfList = new ArrayList<>();                                  //List of idf of each word based on the whole corpus
    private IdString DocId_Text = new IdString();                                      //Document Index - Document Text String
    private IdListString DocId_TokensList = new IdListString();                        //Document Index - List of tokens of this document
    private IdSetString DocId_TokensSet = new IdSetString();                           //Document Index - Set of tokens of this document
    private IdListDouble DocId_tfidfList = new IdListDouble();                         //Document Index - List of TFIDF of each word in the dictionary
    private List<Integer> DocId_DocName = new ArrayList<>();                           //Document Index - Document name(which is also an index)
    private List<Date> DocId_DocDate = new ArrayList<>();                              //Document Index - Document Date
    double [][] cosineSimMatrix;
    private List<Integer> similarDoc = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        TFIDF tfIdf = new TFIDF(100, "data/corpus5/Google");

        /**
         * This is to test function getKeyWords, kwyWordsOverlap,
        * */
        System.out.println(tfIdf.getKeyWords(tfIdf.getTfIdf().getValue(0),10));
        System.out.println(tfIdf.getKeyWords(tfIdf.getTfIdf().getValue(1),10));
        List<String> commonKeywords = tfIdf.getCommonKeyWords(tfIdf.getTfIdf().getValue(0), tfIdf.getTfIdf().getValue(1), 10);
        System.out.println("commonKeywords: " + commonKeywords);

        double ratio = tfIdf.keyWordsOverlap(tfIdf.getTfIdf().getValue(0),tfIdf.getTfIdf().getValue(1),50);
        System.out.println("keywords overlap ratio: " + ratio);

        double cosineSim = tfIdf.getCosineSimilarity(tfIdf.getTfIdf().getValue(0),tfIdf.getTfIdf().getValue(1));
        System.out.println("Cosine similarity: " + cosineSim);

        /**
         * This is to test if there's huge difference between using cosine similarity or key words intersection to decide
         * the closest predecessor.
        * */
        for (int i = 0; i < tfIdf.getNumberOfDocuments(); i++){
            System.out.printf("-----------------------Doc %d-----------------------------------\n",i);
            int n = tfIdf.getClosestPredecessor(tfIdf.getTfIdf().getValue(i), tfIdf.getDocId_DocName().get(i),
                    tfIdf.getDocId_DocDate().get(i), 50, 5, true);
            int m = tfIdf.getClosestPredecessor(tfIdf.getTfIdf().getValue(i), tfIdf.getDocId_DocName().get(i),
                    tfIdf.getDocId_DocDate().get(i), 50, 5, false);
            List<String> commonKeywords1 = tfIdf.getCommonKeyWords(tfIdf.getTfIdf().getValue(i),
                    tfIdf.getTfIdf().getValue(tfIdf.getDocId_DocName().indexOf(n)), 50);
            System.out.println(commonKeywords1);
            List<String> commonKeywords2 = tfIdf.getCommonKeyWords(tfIdf.getTfIdf().getValue(i),
                    tfIdf.getTfIdf().getValue(tfIdf.getDocId_DocName().indexOf(m)), 50);
            System.out.println(commonKeywords2);
        }

//        IdListDouble DocId_TfidfList = tfIdf.getTfIdf();
//        System.out.println(DocId_TfidfList.getValue(1));
//        double cosinsimilarity = tfIdf.getCosineSimilarity(DocId_TfidfList.getValue(0),DocId_TfidfList.getValue(1));
//        System.out.println(cosinsimilarity);
//        List<Pair<Integer,Integer>> similarPair = tfIdf.getCosineMatrix(0.7);
//        System.out.printf("The cosine similarity of the following documents are lager than %f\n", 0.7);
//        for (int i = 0; i < similarPair.size(); i++){
//            System.out.printf(similarPair.get(i) + ": %f\n",tfIdf.getCosineSimMatrix()[similarPair.get(i).getLeft()][similarPair.get(i).getRight()]);
//        }
//        Kmeans kmeans = new Kmeans(tfIdf.getDict().size(),3, tfIdf.getNumberOfDocuments());
//        IdListDouble Centroids = kmeans.randInitialization(0,1);
//        System.out.println(DocId_TfidfList.getValue(1));
//        kmeans.KmeansIteration(DocId_TfidfList,Centroids,10);
//
//        tfIdf.getKeyWords(DocId_TfidfList.getValue(0), 10);
    }
    /**
     *  Constructor: calculate the tf-idf of a specific Corpus
     *
     *  @param numberOfDocuments number of documents in the corpus
     *  @param corpusPath the path of the corpus
    */
    public TFIDF(int numberOfDocuments, String corpusPath) throws IOException {

        this.numberOfDocuments = numberOfDocuments;
        this.CorpusPath = corpusPath;
        ReadDict();
        ReadStopword();
        ReadIdf();
        DocProcess();
        getTfIdf();
    }

    /**
     * This function reads dictionary into the Dict variable of the class.
    */
    public void ReadDict() throws IOException {
        FileHandler fileHandler = new FileHandler();
        String myString[] = fileHandler.loadFileToString("data/dictionaries/60k_dictionary_with_names.csv").
                toLowerCase().split(System.getProperty("line.separator"));
        Dict = Arrays.asList(myString);
//        System.out.println(Dict);
//        System.out.println(Dict.get(0).equals("the"));
//        System.out.print("SIZE:" + Dict.size());
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
        To store all documents into an IdListString/IdSetString format,
        i.e. Document Index - List/Set of tokens of this document
    */
    public void DocProcess() throws IOException {

        FileHandler fileHandler = new FileHandler();

        //Load corpus
        File folder = new File(CorpusPath);
        File[] listOfFiles = folder.listFiles();

        Map<Integer,Date> map = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("data/corpus4/DataTime.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (Map<Integer, Date>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Load the documents
        for (int i = 0; i < numberOfDocuments; i++) {
            DocId_Text.putValue(i, fileHandler.loadFileToString(CorpusPath + "/" + listOfFiles[i].getName()));
//            System.out.println(DocId_Text.getValue(i));
            String[] strings = DocId_Text.getValue(i).replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
            List<String> listoftokens = Arrays.asList(strings).stream()
//                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            Set<String> setoftokens = new HashSet<>(listoftokens);
//            System.out.println(setoftokens);
            DocId_TokensList.putValue(i,listoftokens);
            DocId_TokensSet.putValue(i,setoftokens);
            Integer index = Integer.valueOf(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().lastIndexOf('.')));
            DocId_DocName.add(i, index);
            DocId_DocDate.add(i, map.get(index));
        }


//        System.out.println(DocId_DocDate);
//        Date startDate = DocId_DocDate.get(1);
//        Date endDate = DocId_DocDate.get(2);
//        long startTime = startDate.getTime();
//        long endTime = endDate.getTime();
//        long diffTime = endTime - startTime;
//        long diffDays = diffTime / (1000 * 60 * 60 * 24);
//        System.out.println(diffDays);
//
//        Date d = DateUtil.addDays(startDate,1);
//        System.out.println(d);
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
        for (String s: DocId_TokensList.getValue(i) ){
            if (s.equalsIgnoreCase(termToCheck)){
//                System.out.println(s);
                count++;
            }
        }
        return count/DocId_TokensList.getValue(i).size();
    }

    /**
     *  This function return the inverse term frequency of termToCheck among all documents
     *
     *  @param termToCheck the term we need to check in the corpus
     *
     *  @return  the inverse document frequency of termToCheck in the corpus
     *
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
        return 1+Math.log(numberOfDocuments/count);                      //If every document contains the termToCheck,
    }                                                                    //then the idf should be 1. idf is decreasing with count.


    //Get the TFIDF of all documents
    public IdListDouble getTfIdf(){

        for (int key: DocId_Text.getMap().keySet()){                     //Loop over all documents. For each document, construct a vector to store the tdidf.
            List<Double> tdidfVectors = new ArrayList<>(Dict.size());
            for (int i = 0; i < Dict.size(); i++){                       //Loop over the dictionary. For each word, calculate its tf and idf
                if((DocId_TokensSet.getValue(key).contains(Dict.get(i).toLowerCase()))
                        &&(!StopwordList.contains(Dict.get(i).toLowerCase()))){
                    double tf = tf(key, Dict.get(i));
//                    double idf = idf(s);                               //Only calculate TFIDF when the word is in the document
                    double idf = idfList.get(i);
                    double tfidf = tf*idf;
//                    System.out.printf("Document %d, The tf of '%s' is '%f'\n", key, s, tf);
//                    System.out.printf("Document %d, The idf of '%s' is '%f'\n", key, s, idf);
//                    System.out.printf("Document %d, The TFIDF of '%s' is '%f'\n", key, s, TFIDF);
                    tdidfVectors.add(i,tfidf);
//                    System.out.println(TFIDF);
//                    System.out.println(count);
                }
                else tdidfVectors.add(i, 0.0);                        //Or set TFIDF to be zero
            }
            DocId_tfidfList.putValue(key, tdidfVectors);
        }
        return DocId_tfidfList;
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
     *
     *
    * */
    public List<Pair<Integer,Integer>> getCosineMatrix(double threshold){

        List<Pair<Integer,Integer>> SimilarDocIndex = new ArrayList<>();
        cosineSimMatrix = new double[numberOfDocuments][numberOfDocuments];
        IdListDouble TFIDF = getTfIdf();
        for (int i = 0; i < numberOfDocuments; i++)
            for (int j = i + 1; j < numberOfDocuments; j++){
                cosineSimMatrix[i][j] = getCosineSimilarity(TFIDF.getValue(i),TFIDF.getValue(j));
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
            for (int j = 0; j < numberOfDocuments && DocId_DocName.get(j) != id; j++){
                if (DocId_DocDate.get(j).equals(date1)){
                    if (isCosine){
                        double ratio2 = getCosineSimilarity(tfidf, DocId_tfidfList.getValue(j));
                        if (ratio2 > ratio){
                            ratio = ratio2;
                            index = DocId_DocName.get(j);
                        }
                    }else {
                        double ratio2 = keyWordsOverlap(tfidf, DocId_tfidfList.getValue(j),n);
                        if (ratio2 > ratio){
                            ratio = ratio2;
                            index = DocId_DocName.get(j);
                        }
                    }
                }
            }
        }
        System.out.printf("The closest predecessor of doc %d is %d\n", id, index);
        return index;
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

        List<String> CommonFactors = getKeyWords(DocId_tfidfList.getValue(1), numberofKeyWords);
        for (int i = 0; i < numberOfDocuments; i++) {
            List<String> keywords = getKeyWords(DocId_tfidfList.getValue(i), numberofKeyWords);
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

    public IdListString getDocId_TokensList() {
        return DocId_TokensList;
    }

    public IdString getDocId_Text() {
        return DocId_Text;
    }

    public IdSetString getDocId_TokensSet() {
        return DocId_TokensSet;
    }

    public List<Double> getIdfList() {
        return idfList;
    }

    public IdListDouble getDocId_tfidfList() {
        return DocId_tfidfList;
    }

    public double[][] getCosineSimMatrix() {
        return cosineSimMatrix;
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public List<Date> getDocId_DocDate() {
        return DocId_DocDate;
    }

    public List<Integer> getDocId_DocName() {
        return DocId_DocName;
    }

}

