package ch.lgt.ming.helper;

import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.*;
import ch.lgt.ming.extraction.sentnence.*;
import edu.stanford.nlp.pipeline.Annotation;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;


/**
 * Created by Ming Deng on 8/25/2016.
 */
public class CsvFileWriter_Features {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    private static FileHandler fileHandler = new FileHandler();
    //CSV file header
    private static final String FILE_HEADER = "docID," + "Date," +
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


    public static void main(String[] args) {
        /**
         * This part of code writes featuresBoris using annotated documents
        * */
//        String inputFilePath = "data/corpusBorisSer";
//        String outputFilePath = "data/featureFiles/featuresBoris.csv";
//        CsvFileWriter_Features.writeCsvFile(inputFilePath, outputFilePath, 10, true);

//        String inputFilePath = "data/corpus8/Amazon";
//        String outputFilePath = "data/featureFiles/featuresAmazon.csv";
//        CsvFileWriter_Features.writeCsvFile(inputFilePath, outputFilePath, 100, true);

        /**
         * This part of code writes featuresBoris using raw documents
         * */
        String inputFilePath = "data/corpusBoris";
        String outputFilePath = "data/featureFiles/featuresBoris.csv";
        CsvFileWriter_Features.writeCsvFile(inputFilePath, outputFilePath, 10, false);

//        String inputFilePath = "data/corpus5/Amazon";
//        String outputFilePath = "data/featureFiles/featuresAmazon.csv";
//        CsvFileWriter_Features.writeCsvFile(inputFilePath, outputFilePath, 100, false);
    }

    /**
     * This function writes the feature counts into a csv file, using annotated documents.
     *
     * @param inputFilePath The location of the folder where the documents are stored
     * @param outputFilePath The name of the csv file to be stored
     * @param numberOfDocs number of documents to write
     * @param annotatedDocument indicating if the input files are annotated documents(true) or raw documents(false)
     *
    * */

    public static void writeCsvFile(String inputFilePath, String outputFilePath, int numberOfDocs, boolean annotatedDocument){
        if (annotatedDocument){

            File folder = new File(inputFilePath);
            File[] listOfFiles = folder.listFiles();

            FileInputStream fileInputStream ;
            FileWriter fileWriter = null;

            try {
                fileWriter = new FileWriter(outputFilePath);

                fileWriter.append(FILE_HEADER);                                     //Write the CSV file header
                fileWriter.append(NEW_LINE_SEPARATOR);                              //Add a new line separator after the header
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                //Write new subjects to the CSV file
                for (int key = 0; key < numberOfDocs; key++){

                    fileInputStream = new FileInputStream(inputFilePath + "/" + listOfFiles[key].getName());
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    Document document = (Document) objectInputStream.readObject();

                    fileWriter.append(String.valueOf(document.getIndex()));
                    fileWriter.append(COMMA_DELIMITER);

                    String strDate = dateFormat.format(document.getDate());
                    fileWriter.append(strDate);
                    fileWriter.append(COMMA_DELIMITER);

                    Annotation annotation = document.getDocument();

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
                    System.out.printf("%d is done\n", key);
                }
                System.out.println("CSV file was created successfully!");

            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                System.out.println("Error in CsvFileWriter_Features!");
                e.printStackTrace();
            }finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("Error while flushing/closing fileWriter!");
                    e.printStackTrace();
                }
            }

        }else{

            File folder = new File(inputFilePath);
            File[] listOfFiles = folder.listFiles();

            FileInputStream fileInputStream ;
            FileWriter fileWriter = null;

            StanfordCore.init();

            /**
             * This part reads the Date from DataTime.ser
             * */
            Map<Integer,Date> DocTime = new HashMap<>();
            try {
                fileInputStream = new FileInputStream("data/corpus4/DataTime.ser");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                DocTime = (Map<Integer, Date>) objectInputStream.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fileWriter = new FileWriter(outputFilePath);

                fileWriter.append(FILE_HEADER);                                     //Write the CSV file header
                fileWriter.append(NEW_LINE_SEPARATOR);                              //Add a new line separator after the header
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                //Write new subjects to the CSV file
                for (int key = 0; key < numberOfDocs; key++){

                    Integer index = Integer.valueOf(listOfFiles[key].getName().substring(0, listOfFiles[key].getName().lastIndexOf('.')));
                    Document document = new Document(fileHandler.loadFileToString(listOfFiles[key].getPath()), index, DocTime.get(index));

                    fileWriter.append(String.valueOf(document.getIndex()));
                    fileWriter.append(COMMA_DELIMITER);

                    String strDate = dateFormat.format(document.getDate());
                    fileWriter.append(strDate);
                    fileWriter.append(COMMA_DELIMITER);

                    Annotation annotation = document.getDocument();

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
                    System.out.printf("%d is done\n", key);
                }
                System.out.println("CSV file was created successfully!");

            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                System.out.println("Error in CsvFileWriter_Features!");
                e.printStackTrace();
            }finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("Error while flushing/closing fileWriter!");
                    e.printStackTrace();
                }
            }
        }
    }
}
