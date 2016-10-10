package ch.lgt.ming.helper;


import ch.lgt.ming.commonfactors.tfidf;
import ch.lgt.ming.datastore.IdString;
import java.io.IOException;


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
        CsvFileWriter_CommonFactor.writeCsvFileWriter("data/featureFiles/commonFactors499Boris.csv", 499, 50);
    }

    public static void writeCsvFileWriter(String fileName, int numberofDocs, int numberofKeyWords) throws IOException {

        for (int i = 0; i < numberofKeyWords; i++){
            FILE_HEADER += "," + i;
        }

        tfidf tfIdf = new tfidf(numberofDocs, "corpus2/test1");
        //tfidf tfIdf = new tfidf(corpus);
        tfIdf.ReadDict(true);
        tfIdf.ReadStopword();
        tfIdf.ReadIdf();

        System.out.println(tfIdf.getDocId_TokensList().getValue(0));
        IdistDouble DocId_TfidfList = tfIdf.calculateTfIdf();
        DocId_TfidfList.getValue(1);
        System.out.println(DocId_TfidfList.getValue(1));
        double cosinsimilarity = tfIdf.getCosineSimilarity(DocId_TfidfList.getValue(1),DocId_TfidfList.getValue(2));
        System.out.println(cosinsimilarity);
        tfIdf.getCosineMatrix(0.1);

        System.out.println(Dict.get(0));

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
//                List<String> keywords = tfIdf.getKeyWords(DocId_TfidfList.getValue(i), numberofKeyWords);

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
