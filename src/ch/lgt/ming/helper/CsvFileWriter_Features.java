//package ch.lgt.ming.helper;
//
//import ch.lgt.ming.cleanup.Corpus;
//import ch.lgt.ming.cleanup.Document;
//import ch.lgt.ming.corenlp.StanfordCore;
//import ch.lgt.ming.datastore.*;
//import ch.lgt.ming.extraction.sentnence.*;
//import edu.stanford.nlp.pipeline.Annotation;
//import java.io.*;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//
//import java.util.*;
//
//
///**
// * Created by Ming Deng on 8/25/2016.
// */
//public class CsvFileWriter_Features {
//
//    //Delimiter used in CSV file
//    private static final String COMMA_DELIMITER = ",";
//    private static final String NEW_LINE_SEPARATOR = "\n";
//
//    //CSV file header
//    private static final String FILE_HEADER = "docID," + "Date," +
//            "Uncertainty_unspecified_noun,Uncertainty_unspecified_noun_neg," +
//            "Uncertainty_unspecified_verb,Uncertainty_unspecified_verb_neg," +
//            "Uncertainty_unspecified_othertype,Uncertainty_unspecified_othertype_neg," +
//            "Uncertainty_fear_noun,Uncertainty_fear_noun_neg," +
//            "Uncertainty_fear_verb,Uncertainty_fear_verb_neg," +
//            "Uncertainty_fear_othertype,Uncertainty_fear_othertype_neg," +
//            "Uncertainty_hope_noun,Uncertainty_hope_noun_neg," +
//            "Uncertainty_hope_verb,Uncertainty_hope_verb_neg," +
//            "Uncertainty_hope_othertype,Uncertainty_hope_othertype_neg," +
//            "Uncertainty_anxiety_noun,Uncertainty_anxiety_noun_neg," +
//            "Uncertainty_anxiety_verb,Uncertainty_anxiety_verb_neg," +
//            "Uncertainty_anxiety_othertype,Uncertainty_anxiety_othertype_neg," +
//            "Uncertainty_conditionality1,Uncertainty_conditionality1_pos,Uncertainty_conditionality1_neg," +
//            "Uncertainty_conditionality2,Uncertainty_conditionality2_pos,Uncertainty_conditionality2_neg," +
//            "Surprise_unspecified_noun,Surprise_unspecified_noun_neg," +
//            "Surprise_unspecified_verb,Surprise_unspecified_verb_neg," +
//            "Surprise_unspecified_othertype,Surprise_unspecified_othertype_neg," +
//            "Surprise_disappointment_noun,Surprise_disappointment_noun_neg," +
//            "Surprise_disappointment_verb,Surprise_disappointment_verb_neg," +
//            "Surprise_disappointment_othertype,Surprise_disappointment_othertype_neg," +
//            "Surprise_relief_noun,Surprise_relief_noun_neg," +
//            "Surprise_relief_verb,Surprise_relief_verb_neg," +
//            "Surprise_relief_othertype,Surprise_relief_othertype_neg," +
//            "Surprise_comparative," +
//            "Valence_Pos,Valence_Neg";
//
//    // Variable declaration
//    private static FileHandler fileHandler = new FileHandler();
//    private static IdString docId_Text = new IdString();
//
//    public static void main(String[] args) {
//
//
//    	String path = "data/featureFiles/featuresBoris.csv";
//    	CsvFileWriter_Features.writeCsvFile(path, 25	);
//
////        CsvFileWriter_Features.writeCsvFile("data/featureFiles/Amazon.csv", 865);
////        CsvFileWriter_Features.writeCsvFile("data/featureFiles/Amazon.csv", 10);
//
//    }
//
//    /**
//     * This function writes the feature counts into a csv file, using annotated documents.
//     *
//     * @param fileName The name of the csv file to be stored
//     * @param numberofdocs number of documents to write
//     *
//    * */
//
//    public static void writeCsvFile(String fileName, int numberofdocs){
//
//        File folder = new File("data/corpus8/Amazon");
//        File[] listOfFiles = folder.listFiles();
//
//        FileInputStream fileInputStream ;
//        FileWriter fileWriter = null;
//
//        try {
//            fileWriter = new FileWriter(fileName);
//
//            fileWriter.append(FILE_HEADER);                                     //Write the CSV file header
//            fileWriter.append(NEW_LINE_SEPARATOR);                              //Add a new line separator after the header
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//            //Write new subjects to the CSV file
//            for (int key = 0; key < numberofdocs; key++){
//
//                fileInputStream = new FileInputStream("data/corpus8/Amazon/" + listOfFiles[key].getName());
//                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//                Document document = (Document) objectInputStream.readObject();
//
//                fileWriter.append(String.valueOf(document.getIndex()));
//                fileWriter.append(COMMA_DELIMITER);
//
//                String strDate = dateFormat.format(document.getDate());
//                fileWriter.append(strDate);
//                fileWriter.append(COMMA_DELIMITER);
//
//                Annotation annotation = document.getDocument();
//
//                List<String> Uncertainty_Reg = Arrays.asList("$UNSPECIFIED", "$FEAR", "$HOPE", "$ANXIETY");
//                List<String> Uncertainty_Reg2 = Arrays.asList("$CONDITIONALITY1", "$CONDITIONALITY2");
//                List<String> Surprise_Reg = Arrays.asList("$UNSPECIFIED", "$DISAPPOINTMENT", "$RELIEF");
//
//                for (int i = 0; i < Uncertainty_Reg.size(); i++){
//                    List<Integer> result = Uncertainty.extract(annotation, Uncertainty_Reg.get(i));
//                    for (int j = 0; j < 6; j++){
//                        fileWriter.append((String.valueOf(result.get(j))));
//                        fileWriter.append(COMMA_DELIMITER);
//                    }
//                }
//                for (int i = 0; i < Uncertainty_Reg2.size(); i++){
//                    List<Integer> result = Uncertainty.extractConditionality(annotation, Uncertainty_Reg2.get(i));
//                    for (int j = 0; j < 3; j++){
//                        fileWriter.append((String.valueOf(result.get(j))));
//                        fileWriter.append(COMMA_DELIMITER);
//                    }
//                }
//                for (int i = 0; i < Surprise_Reg.size(); i++){
//                    List<Integer> result = Surprise.extract(annotation, Surprise_Reg.get(i));
//                    for (int j = 0; j < 6; j++) {
//                        fileWriter.append(String.valueOf(result.get(j)));
//                        fileWriter.append(COMMA_DELIMITER);
//                    }
//                }
//                fileWriter.append(String.valueOf(Surprise.extractComparative(annotation)));
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(String.valueOf(PosWordCount.extract(annotation)));
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(String.valueOf(NegWordCount.extract(annotation)));
//                fileWriter.append(NEW_LINE_SEPARATOR);
//                System.out.printf("%d is done\n", key);
//            }
//            System.out.println("CSV file was created successfully!");
//
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }catch (Exception e) {
//            System.out.println("Error in CsvFileWriter_Features!");
//            e.printStackTrace();
//        }finally {
//            try {
//                fileWriter.flush();
//                fileWriter.close();
//            } catch (IOException e) {
//                System.out.println("Error while flushing/closing fileWriter!");
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * This function writes the feature counts into a csv file, using raw html documents.
//     *
//     * @param fileName The name of the csv file to be stored
//     * @param numberofdocs number of documents to write
//     *
//     * */
//
//    public static void writeCsvFile2(String fileName, int numberofdocs){
//
//        // Initialize corenlp
//        StanfordCore.init();
//
//        // Load corpus
//        String path = "data/corpusBoris";
//
//
//        File folder = new File(path);
//        File[] listOfFiles = folder.listFiles();
//
//        FileWriter fileWriter = null;
//        try {
//
//            fileWriter = new FileWriter(fileName);
//
//            //Write the CSV file header
//            fileWriter.append(FILE_HEADER);
//
//            //Add a new line separator after the header
//            fileWriter.append(NEW_LINE_SEPARATOR);
//
//            //Write new subjects to the CSV file
//            //Load the documents
//            for (int i = 0; i < numberofdocs; i++) {
//                docId_Text.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
//
//                fileWriter.append(listOfFiles[i].getName().replaceAll(".html", ""));
//                fileWriter.append(COMMA_DELIMITER);
//
//                double start = System.currentTimeMillis();
//                Annotation annotation = StanfordCore.pipeline.process(docId_Text.getValue(i));
//
//                List<String> Uncertainty_Reg = Arrays.asList("$UNSPECIFIED", "$FEAR", "$HOPE", "$ANXIETY");
//                List<String> Uncertainty_Reg2 = Arrays.asList("$CONDITIONALITY1", "$CONDITIONALITY2");
//                List<String> Surprise_Reg = Arrays.asList("$UNSPECIFIED", "$DISAPPOINTMENT", "$RELIEF");
//
//                for (int ii = 0; ii < Uncertainty_Reg.size(); ii++){
//                    List<Integer> result = Uncertainty.extract(annotation, Uncertainty_Reg.get(ii));
//                    for (int j = 0; j < 6; j++){
//                        fileWriter.append((String.valueOf(result.get(j))));
//                        fileWriter.append(COMMA_DELIMITER);
//                    }
//                }
//                for (int ii = 0; ii < Uncertainty_Reg2.size(); ii++){
//                    List<Integer> result = Uncertainty.extractConditionality(annotation, Uncertainty_Reg2.get(ii));
//                    for (int j = 0; j < 3; j++){
//                        fileWriter.append((String.valueOf(result.get(j))));
//                        fileWriter.append(COMMA_DELIMITER);
//                    }
//                }
//                for (int ii = 0; ii < Surprise_Reg.size(); ii++){
//                    List<Integer> result = Surprise.extract(annotation, Surprise_Reg.get(ii));
//                    for (int j = 0; j < 6; j++) {
//                        fileWriter.append(String.valueOf(result.get(j)));
//                        fileWriter.append(COMMA_DELIMITER);
//                    }
//                }
//                fileWriter.append(String.valueOf(Surprise.extractComparative(annotation)));
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(String.valueOf(PosWordCount.extract(annotation)));
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(String.valueOf(NegWordCount.extract(annotation)));
//                fileWriter.append(NEW_LINE_SEPARATOR);
//                double end = System.currentTimeMillis();
//                System.out.println(end-start);
//                System.out.println("Document " + i + " is done.");
//
//            }
//            System.out.println("CSV file was created successfully!");
//        } catch (Exception e) {
//            System.out.println("Error in CsvFileWriter_Features!");
//            e.printStackTrace();
//        } finally {
//            try {
//                fileWriter.flush();
//                fileWriter.close();
//            }catch (IOException e){
//                System.out.println("Error while flushing/closing fileWriter!");
//                e.printStackTrace();
//            }
//        }
//
//    }
//}
