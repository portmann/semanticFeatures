package ch.lgt.ming.processing;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.datastore.IdValue;
import ch.lgt.ming.extraction.sentnence.Surprise;
import ch.lgt.ming.extraction.sentnence.Uncertainty;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Ming Deng on 8/25/2016.
 */
public class TestAnnotationTime {

    public static void main(String[] args) throws IOException {

        System.out.println("--------------------------------------- Pipeline ------------------------------------------");
        Properties props = new Properties();
//        props.setProperty("customAnnotatorClass.tense", "ch.lgt.ming.corenlp.TenseAnnotator");
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, tense");
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Load corpus
        String path = "corpus";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();


        FileHandler fileHandler = new FileHandler();
        IdString docId_Text = new IdString();

        IdValue time = new IdValue();

        for (int i = 0; i < 10; i++) {
            docId_Text.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
        }

        //Process documents
        for (Integer key : docId_Text.getMap().keySet()) {

            double start = System.currentTimeMillis();
            Annotation document = new Annotation(docId_Text.getValue(key));
            pipeline.annotate(document);
            double end = System.currentTimeMillis();
            time.putValue(key, end-start);
            System.out.println(end-start);
            System.out.println("Document " + key + " is done.");

        }
        System.out.println(Ave(time));


    }

    public static double Ave(IdValue x){
        double sum = 0;
        for (double i:x.getMap().values()){
            sum += i;
        }
        return sum/x.getMap().size();
    }
}
