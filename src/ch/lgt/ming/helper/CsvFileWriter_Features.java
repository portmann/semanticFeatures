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
    private static final String FILE_HEADER = "docID," +
            "Uncertainty_unspecified_noun,Uncertainty_unspecified_noun_neg," +
            "Uncertainty_unspecified_verb,Uncertainty_unspecified_verb_neg," +
            "Uncertainty_unspecified_othertype,Uncertainty_unspecified_othertype_neg," +
            "Uncertainty_fear_noun,Uncertainty_fear_noun_neg," +
            "Uncertainty_fear_verb,Uncertainty_fear_verb_neg," +
            "Uncertainty_fear_othertype,Uncertainty_fear_othertype_neg," +
            "Uncertainty_hope_noun,Uncertainty_hope_noun_neg," +
            "Uncertainty_hope_verb,Uncertainty_hope_verb_neg," +
            "Uncertainty_hope_othertype,Uncertainty_hope_othertype_neg," +
            "Uncertainty_anxiety_noun,Uncertainty_anxiety_noun_neg," +
            "Uncertainty_anxiety_verb,Uncertainty_anxiety_verb_neg," +
            "Uncertainty_anxiety_othertype,Uncertainty_anxiety_othertype_neg," +
            "Uncertainty_conditionality1,Uncertainty_conditionality1_pos,Uncertainty_conditionality1_neg," +
            "Uncertainty_conditionality2,Uncertainty_conditionality2_pos,Uncertainty_conditionality2_neg," +
            "Surprise_unspecified_noun,Surprise_unspecified_noun_neg," +
            "Surprise_unspecified_verb,Surprise_unspecified_verb_neg," +
            "Surprise_unspecified_othertype,Surprise_unspecified_othertype_neg," +
            "Surprise_disappointment_noun,Surprise_disappointment_noun_neg," +
            "Surprise_disappointment_verb,Surprise_disappointment_verb_neg," +
            "Surprise_disappointment_othertype,Surprise_disappointment_othertype_neg," +
            "Surprise_relief_noun,Surprise_relief_noun_neg," +
            "Surprise_relief_verb,Surprise_relief_verb_neg," +
            "Surprise_relief_othertype,Surprise_relief_othertype_neg," +
            "Surprise_comparative," +
            "Valence_Pos,Valence_Neg";

    // Variable declaration
    private static FileHandler fileHandler = new FileHandler();
    private static IdString docId_Text = new IdString();

    public static void main(String[] args) {
        CsvFileWriter_Features.writeCsvFileWriter("featureFiles/features50.csv", 1);
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

                fileWriter.append(String.valueOf(key));
                fileWriter.append(COMMA_DELIMITER);

                double start = System.currentTimeMillis();
                Annotation annotation = StanfordCore.pipeline.process(docId_Text.getValue(key));

                List<String> Uncertainty_Reg = Arrays.asList("$UNSPECIFIED", "$FEAR", "$HOPE", "$ANXIETY");
                List<String> Uncertainty_Reg2 = Arrays.asList("$CONDITIONALITY1", "$CONDITIONALITY2");
                List<String> Surprise_Reg = Arrays.asList("$UNSPECIFIED", "$DISAPPOINTMENT", "$RELIEF");

                for (int i = 0; i < Uncertainty_Reg.size(); i++){
                    List<Integer> result = Uncertainty.extract(annotation, Uncertainty_Reg.get(i));
                    for (int j = 0; j < 6; j++){
                        fileWriter.append((String.valueOf(result.get(j))));
                        fileWriter.append(COMMA_DELIMITER);
                    }
                }
                for (int i = 0; i < Uncertainty_Reg2.size(); i++){
                    List<Integer> result = Uncertainty.extractConditionality(annotation, Uncertainty_Reg2.get(i));
                    for (int j = 0; j < 3; j++){
                        fileWriter.append((String.valueOf(result.get(j))));
                        fileWriter.append(COMMA_DELIMITER);
                    }
                }
                for (int i = 0; i < Surprise_Reg.size(); i++){
                    List<Integer> result = Surprise.extract(annotation, Surprise_Reg.get(i));
                    for (int j = 0; j < 6; j++) {
                        fileWriter.append(String.valueOf(result.get(j)));
                        fileWriter.append(COMMA_DELIMITER);
                    }
                }
                fileWriter.append(String.valueOf(Surprise.extractComparative(annotation)));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(PosWordCount.extract(annotation)));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(NegWordCount.extract(annotation)));
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
