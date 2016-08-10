package ch.lgt.ming.processing;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.*;
import ch.lgt.ming.extraction.sentnence.*;
import ch.lgt.ming.helper.FileHandler;
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

        Extractor<IdListString> companyExtractor = new CompaniesAll();
        Extractor<IdBoolean> mergerExtractor = new Merger();
        Extractor<IdString> tenseExtractor = new Tense();
        Extractor<IdBoolean> uncertaintyExtractor = new Uncertainty();
        Extractor<IdBoolean> surpriseExtractor = new Surprice();

        // exercise variable
        StringId companyId = new StringId();           // use NER (company is simply an example of an entity)
        IdListId documentCompanys = new IdListId();    // use NER

        // initialize corenlp
        StanfordCore.init();


//        String myString = "I was a student. I am a student. I will be a student.";

//        String myString =
//                "The expression is really vague. " +
//                "Eight years later, Congress is confounded with how to confront the ambiguity associated with consumer lending. " +
//                "“We’ve had a pretty anomalously hot and dry stretch, says Randy Graham, meteorologist-in-charge at the Weather " +
//                "Services’ Salt Lake City office.";//Strings to test uncertainty

        String myString =
                "I am so surprised about the news. The 'Amazing' IPO Change That May Restart The Flow Of New Stocks. " +
                "Opinion: CFOs want a stunning 14% annual return on investments — and that’s holding back the economy " +
                "Gap shares slump as July sales disappoint, but analysts upbeat."
        ;//Strings to test surprise

        Annotation document = StanfordCore.pipeline.process(myString);

//        IdString myTense = tenseExtractor.extract(document);
//        IdBoolean myUncertainty = uncertaintyExtractor.extract(document);
        IdBoolean mySurPrise = surpriseExtractor.extract(document);


//        System.out.println(myTense.getMap());
//        System.out.println(myUncertainty.getMap());
        System.out.println(mySurPrise.getMap());

    }
}
