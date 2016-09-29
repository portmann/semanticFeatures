package ch.lgt.ming.helper;

import ch.lgt.ming.commonfactors.tfidf;
import ch.lgt.ming.datastore.IdListDouble;
import ch.lgt.ming.datastore.IdString;

import java.io.FileWriter;
import java.io.IOException;
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

    // Variable declaration
    private static IdString docId_Text = new IdString();

    public static String getFileHeader() {
        return FILE_HEADER;
    }

    public static void main(String[] args) throws IOException {
        CsvFileWriter_CommonFactor.writeCsvFileWriter("data/featureFiles/commonFactors499.csv", 499, 50);
    }


    public static void writeCsvFileWriter(String fileName, int numberofDocs, int numberofKeyWords) throws IOException {

        for (int i = 0; i < numberofKeyWords; i++){
            FILE_HEADER += "," + i;
        }

//        TFIDF tfIdf = new TFIDF(numberofDocs, "corpus2/test1");
        tfidf tfIdf = new tfidf(numberofDocs, "data/corpus");
        tfIdf.ReadDict();
        tfIdf.ReadStopword();
        tfIdf.ReadIdf();
        tfIdf.DocProcess();
//        System.out.println(tfIdf.getDocId_TokensList().getValue(0));
        IdListDouble DocId_TfidfList = tfIdf.getTfIdf();
//        DocId_TfidfList.getValue(1);
//        System.out.println(DocId_TfidfList.getValue(1));
//        double cosinsimilarity = tfIdf.getCosineSimilarity(DocId_TfidfList.getValue(1),DocId_TfidfList.getValue(2));
//        System.out.println(cosinsimilarity);
//        tfIdf.getCosineMatrix(0.1);

//        System.out.println(Dict.get(0));

        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            //Write new subjects to the CSV file
            //Process documents
            for (int i = 0; i < numberofDocs; i++) {

                double start = System.currentTimeMillis();
                List<String> keywords = tfIdf.getKeyWords(DocId_TfidfList.getValue(i), numberofKeyWords);

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
}
