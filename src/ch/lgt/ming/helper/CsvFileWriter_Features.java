package ch.lgt.ming.helper;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.*;
import ch.lgt.ming.extraction.sentnence.*;
import edu.stanford.nlp.pipeline.Annotation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ming Deng on 8/25/2016.
 */
public class CsvFileWriter_Features {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "docID,Uncertainty_unspecified,Uncertainty_fear," +
            "Uncertainty_hope,Uncertainty_anxiety,Uncertainty_conditionality1," +
            "Uncertainty_conditionality2,Surprise_unspecified,Surprise_disappointment," +
            "Surprise_relief,Surprise_comparative,Pos,Neg";

    // Variable declaration
    private static FileHandler fileHandler = new FileHandler();
    private static IdString docId_Text = new IdString();

    public static void main(String[] args) {
        CsvFileWriter_Features.writeCsvFileWriter("featureFiles/features50.csv", 50);
    }

    public static void writeCsvFileWriter(String fileName, int numberofdocs){

        // Initialize corenlp
        StanfordCore.init();

        // Load corpus
        String path = "corpus";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();


        FileWriter fileWriter = null;
        try {

            Extractor<IdValue> negWordExtractor = new NegWordCount();
            Extractor<IdValue> posWordExtractor = new PosWordCount();
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write new subjects to the CSV file
            //Load the documents
            for (int i = 0; i < numberofdocs; i++) {
                docId_Text.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
            }

            //Process documents
            for (Integer key : docId_Text.getMap().keySet()) {

                double start = System.currentTimeMillis();
                Annotation annotation = StanfordCore.pipeline.process(docId_Text.getValue(key));
                int Surprise_Unspecified = Surprise.extractSurprise_Unspecified(annotation);
                int Surprise_Disappointment = Surprise.extractSurprise_Disappointment(annotation);
                int Surprise_Relief = Surprise.extractSurprise_Relief(annotation);
                int Surprise_Comparative = Surprise.extractSurprise_Comparative(annotation);
                int Uncertainty_Unspecified = Uncertainty.extractUncertainty_Unspecified(annotation);
                int Uncertainty_Fear = Uncertainty.extractUncertainty_Fear(annotation);
                int Uncertainty_Hope = Uncertainty.extractUncertainty_Hope(annotation);
                int Uncertainty_Anxiety = Uncertainty.extractUncertainty_Anxiety(annotation);
                int Uncertainty_Conditional1 = Uncertainty.extractUncertainty_Conditionality1(annotation);
                int Uncertainty_Conditional2 = Uncertainty.extractUncertainty_Conditionality2(annotation);
                int posWordcounts = posWordExtractor.extractCounts(annotation);
                int negWordcounts = negWordExtractor.extractCounts(annotation);
                fileWriter.append(String.valueOf(key));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Uncertainty_Unspecified));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Uncertainty_Fear));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Uncertainty_Hope));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Uncertainty_Anxiety));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Uncertainty_Conditional1));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Uncertainty_Conditional2));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Surprise_Unspecified));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Surprise_Disappointment));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Surprise_Relief));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(Surprise_Comparative));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(posWordcounts));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(negWordcounts));
                fileWriter.append(NEW_LINE_SEPARATOR);
                double end = System.currentTimeMillis();
                System.out.println(end-start);
                System.out.println("Document " + key + " is done.");

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
