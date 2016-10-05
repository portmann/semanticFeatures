package ch.lgt.ming.helper;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.commonfactors.tfidf;
import ch.lgt.ming.datastore.IdListDouble;
import ch.lgt.ming.datastore.IdString;

import java.io.FileWriter;
import java.io.IOException;
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
    private static  String FILE_HEADER = "docID";

    public static void main(String[] args) throws IOException, ParseException {
        CsvFileWriter_CommonFactor.writeCsvFileWriter("data/featureFiles/commonFactors.csv", 50, 50);
    }

    /**
     * This function writes the csv file of common factors.
     *
     * @param fileName The address where the csv file will be written;
     * @param timeInterval The time interval considered for the corpus;
     * @param numberofKeyWords The number of keywords to be written.
     *
    * */

    public static void writeCsvFileWriter(String fileName, int timeInterval, int numberofKeyWords)
            throws IOException, ParseException {

        for (int i = 0; i < numberofKeyWords; i++){
            FILE_HEADER += "," + i;
        }

        Corpus corpus = new Corpus("data/corpusBoris");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-13");
        tfidf tfIdf = new tfidf(corpus, date, timeInterval);

        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            //Write new subjects to the CSV file
            //Process documents
            for (int i = 0; i < tfIdf.getNumberOfDocuments(); i++) {

                double start = System.currentTimeMillis();
                List<String> keywords = tfIdf.getKeyWords(tfIdf.getCorpus().getDocument(i).getTfidf(), numberofKeyWords);

                fileWriter.append(String.valueOf(i));
                for (int j = 0; j < numberofKeyWords; j++){
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
