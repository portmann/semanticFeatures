package ch.lgt.ming.extraction.sentnence;

import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.feature.Feature;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 10/13/2016.
 */
public class FeatureAll {
    public static void main(String[] args) {

        String inputPath = "data/Empirical_Analysis/ReutersSer_Company/Amazon";
        String outputPath = "data/Reuters_Company/Amazon";


        FileHandler fileHandler = new FileHandler();
        FileInputStream fileInputStream = null;

        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();

        for (int j = 0; j < listOfFiles.length; j++) {

            try {
                fileInputStream = new FileInputStream(inputPath + "/" + listOfFiles[j].getName());
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Document document = (Document) objectInputStream.readObject();
                Annotation annotation = document.getDocument();

                List<Integer> result = FeatureAllExtract((annotation));
                System.out.println("The result is:" + result);

                System.out.printf("%d : %d is done\n", j, document.getIndex());
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Integer> FeatureAllExtract(Annotation document){
        Feature feature = new Feature();
        List<Integer> counts = Arrays.asList(0,0,0);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (int i = 0; i < sentences.size(); i++) {
            int Positive = feature.FeatureExtract(sentences.get(i),"$VALUES_POSITIVE");
            int Surprise = feature.FeatureExtract(sentences.get(i),"$VALUES_SURPRISE");
            int Uncertainty = feature.FeatureExtract(sentences.get(i),"$VALUES_UNCERTAINTY");
            counts.set(0, counts.get(0) + Positive);
            counts.set(1, counts.get(1) + Surprise);
            counts.set(2, counts.get(2) + Uncertainty);
        }
        return counts;
    }
}
