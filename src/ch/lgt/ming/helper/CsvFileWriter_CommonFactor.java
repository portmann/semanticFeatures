package ch.lgt.ming.helper;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.cleanup.Document2;
import ch.lgt.ming.commonfactors.tfidf;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ming Deng on 8/29/2016.
 */
public class CsvFileWriter_CommonFactor {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static  String FILE_HEADER = "date,docID,refDocID";

    public static void main(String[] args) throws IOException, ParseException {
        CsvFileWriter_CommonFactor.writeCsvFileWriter("data/featureFiles/commonFactors5.csv", 50, 20);
    }

    /**
     * This function writes the csv file of common factors.
     *
     * @param outputFilePath The address where the csv file will be written;
     * @param timeInterval The time interval considered for the corpus;
     * @param numberOfKeyWords The number of keywords to be written.
     *
    * */

    public static void writeCsvFileWriter(String outputFilePath, int timeInterval, int numberOfKeyWords)
            throws IOException, ParseException {

        for (int i = 0; i < numberOfKeyWords; i++){
            FILE_HEADER += "," + i;
        }

        Corpus corpus = new Corpus("data/corpusBoris");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-13");
        tfidf tfIdf = new tfidf(corpus, date, timeInterval);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(outputFilePath);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            //Write new subjects to the CSV file
            //Process documents
            for (int i = 0; i < tfIdf.getNumberOfDocuments(); i++) {

                double start = System.currentTimeMillis();

                Document2 doc = tfIdf.getCorpus().getDocument(i);
                List<String> keywords = tfIdf.getKeyWords(doc.getTfidf(), numberOfKeyWords);
                int pre = tfIdf.getClosestPredecessor2(doc.getTfidf(), doc.getIndex(), doc.getDate(),numberOfKeyWords, timeInterval, false, 0.25);
         
                String strDate = dateFormat.format(doc.getDate());
                fileWriter.append(String.valueOf(strDate));
                fileWriter.append(COMMA_DELIMITER);      
                fileWriter.append(String.valueOf(doc.getIndex()));
                fileWriter.append(COMMA_DELIMITER);                
                fileWriter.append(String.valueOf(pre));

                for (int j = 0; j < numberOfKeyWords; j++){
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(keywords.get(j));
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
                double end = System.currentTimeMillis();
                System.out.println(end-start);
                System.out.println("Document " + i + " is done.");

            }

            System.out.println("CSV file was created successfully!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter_Features!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            }catch (IOException e){
                System.out.println("Error while flushing/closing fileWriter!");
                e.printStackTrace();
            }
        }

    }

    public static String getFileHeader() {
        return FILE_HEADER;
    }
}
