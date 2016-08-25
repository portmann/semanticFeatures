package ch.lgt.ming.processing;

import ch.lgt.ming.feature.company;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Ming Deng on 8/8/2016.
 */
//This is the test code for NER
public class Test4_companyNER {
    public static void main(String[] args) throws IOException {


        byte[] encoded1 = Files.readAllBytes(Paths.get("dictionaries/NERtest.txt"));
        byte[] encoded2 = Files.readAllBytes(Paths.get("dictionaries/NERtest2.txt"));
        byte[] encoded3 = Files.readAllBytes(Paths.get("dictionaries/Company name list.txt"));
        String[] myString = new String[3];
        myString[0] = new String(encoded1, StandardCharsets.UTF_8);
        myString[1] = new String(encoded2, StandardCharsets.UTF_8);
        myString[2] = new String(encoded3, StandardCharsets.UTF_8);

        System.out.println("--------------------------------------- Pipeline ------------------------------------------");
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document1 = new Annotation(myString[0]);
        pipeline.annotate(document1);
        Annotation document2 = new Annotation(myString[1]);
        pipeline.annotate(document2);
        Annotation document3 = new Annotation(myString[2]);
        pipeline.annotate(document3);

        System.out.println("-----------------------------------------Alcoa--------------------------------------------");
        for (CoreMap sentence: document1.get(CoreAnnotations.SentencesAnnotation.class)){
            company company = new company();
            System.out.println(company.extract(sentence));
        }

        System.out.println("-----------------------------------------alcoa--------------------------------------------");
        for (CoreMap sentence: document2.get(CoreAnnotations.SentencesAnnotation.class)){
            company company = new company();
            System.out.println(company.extract(sentence));
        }


        System.out.println("=----------------------------------------NER for column D----------------------------------");
        for (CoreMap sentence: document3.get(CoreAnnotations.SentencesAnnotation.class)){
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){
                System.out.println(token.ner());
            }
        }









    }
}
