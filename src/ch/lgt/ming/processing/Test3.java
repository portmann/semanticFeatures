package ch.lgt.ming.processing;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.*;
import ch.lgt.ming.extraction.sentnence.*;
import ch.lgt.ming.helper.FileHandler;
import com.sun.org.apache.xpath.internal.SourceTree;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ming Deng on 5/1/2016.
 */
public class Test3 {
    public static void main(String[] args) throws Exception {

        // variable declaration
        FileHandler fileHandler = new FileHandler();
        PosWordCount posWordCount = new PosWordCount();
        NegWordCount negWordCount = new NegWordCount();
        NegationCount negationCount = new NegationCount();
        CompaniesAll companiesAll = new CompaniesAll();
        Tense tense = new Tense();

        Map<Integer,IdValue> Doc_SentPosCount = new HashMap<>();
        Map<Integer,IdValue> Doc_SentNegCount = new HashMap<>();
        Map<Integer,IdValue> Doc_SentNegationCount = new HashMap<>();
        Map<Integer,IdString> Doc_SentCompany = new HashMap<>();
        Map<Integer,IdString> Doc_SentTense = new HashMap<>();

        IdString documentText = new IdString();        // hashmap (key-value pair) of texts (i.e. text1, text2, etc.)
        StringId textSentenceId = new StringId();      // hashmap of all sentences contained in a text (indexing of sentences within a text)

        // exercise variable
        StringId companyId = new StringId();           // use NER (company is simply an example of an entity)
        IdListId documentCompanys = new IdListId();    // use NER

        // initialize corenlp
        StanfordCore.init();

        // load corpus
        String path = "corpus";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        // for (int i = 0; i < listOfFiles.length; i++) {
        for (int i = 0; i < 3; i++) {
            documentText.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));

            System.out.println("Document: " + i + " done.");
        }


        double start = System.currentTimeMillis();
        // set sentence index (uses Stanford sentence splitter "SentenceAnnotation.class")
        // Loop over all documents
        for (Integer key : documentText.getMap().keySet()) {
            Annotation document = StanfordCore.pipeline.process(documentText.getValue(key));
            //Annotation document = StanfordCore.pipeline.process("I didn't want my dog to eat my homework. I will got there tomorrow. Please give me some advice.");
            Doc_SentPosCount.put(key, posWordCount.extract(document));
            Doc_SentNegCount.put(key, negWordCount.extract(document));
            Doc_SentNegationCount.put(key, negationCount.extract(document));
            Doc_SentCompany.put(key, companiesAll.extract(document));
            Doc_SentTense.put(key, tense.extract(document));

            System.out.printf("--------------------------------------Document %d-------------------------------\n",key);
            System.out.println("PosCount: " + Doc_SentPosCount.get(key).getMap());
            System.out.println("NegCount: " + Doc_SentNegCount.get(key).getMap());
            System.out.println("NegationCount: " + Doc_SentNegationCount.get(key).getMap());
            System.out.println("Company: " + Doc_SentCompany.get(key).getMap());
            System.out.println("Tense: " + Doc_SentTense.get(key).getMap());

        }
        System.out.println("Whoop whoop!!");
        System.out.println(System.currentTimeMillis() - start);

    }
}
