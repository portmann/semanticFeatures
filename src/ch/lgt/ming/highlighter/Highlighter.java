package ch.lgt.ming.highlighter;

import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.cleanup.HTMLStrings;
import ch.lgt.ming.feature.SurpriseFeature;
import ch.lgt.ming.feature.UncertaintyFeature;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 9/22/2016.
 */
public class Highlighter {
    // Highlighter highlights like
    // For highlighted text: *before_highlighted* *String* *after_highlighted*
    // For standard text: *before_standard* *String* *after_standard*

    public String before_highlighted = "<span style='background-color: #F5B7B1'>";
    public String before_highlighted2 = "<span style='background-color: #AED6F1'>";
    public String before_highlighted3 = "<span style='background-color: #ABEBC6'>";
    public String after_highlighted = "</span>";
    public String before_standard = "";
    public String after_standard = "";

    public static void main(String[] args) throws IOException {

        List<String> companies = Arrays.asList("Amazon", "Boeing", "Delta", "Facebook", "Ford",
                "Goldman", "Google", "Intel", "Microsoft", "Netflix");
        List<String> Folders = Arrays.asList("Amazon", "Boeing", "Delta_Airline", "Facebook", "Ford",
                "Goldman_Sachs", "Google", "Intel", "Microsoft", "Netflix");

        HTMLStrings htmlStrings = new HTMLStrings();
        Highlighter highlighter = new Highlighter();

        FileHandler fileHandler = new FileHandler();
        FileInputStream fileInputStream = null;
        String inputPath = "data/Empirical_Analysis/ReutersSer_Company/Netflix";
        String outputPath = "data/Empirical_Analysis/Reuters_Company/Netflix";

            File folder = new File(inputPath);
            File[] listOfFiles = folder.listFiles();
            for (int j = 0; j < listOfFiles.length; j++) {

                try {
                    fileInputStream = new FileInputStream(inputPath + "/" + listOfFiles[j].getName());
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    Document document = (Document) objectInputStream.readObject();
                    String higlightedText = highlighter.highlight(document, "Apple");


                    higlightedText = htmlStrings.getBeforeTitle() + "Apple" +
                            htmlStrings.getAfterTitle() + higlightedText +
                            htmlStrings.getEnd();

                    fileHandler.saveStringAsFile(outputPath + "/" + document.getIndex() + ".html", higlightedText);

                    System.out.printf("%d is done\n", j);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//        for (int i = 0; i < 1; i++) {
//            File folder = new File("data/corpus8/" + Folders.get(i));
//            File[] listOfFiles = folder.listFiles();
//            for (int j = 0; j < listOfFiles.length; j++) {
//
//                try {
//                    fileInputStream = new FileInputStream("data/corpus8/" + Folders.get(i) + "/" + listOfFiles[j].getName());
//                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//                    Document document = (Document) objectInputStream.readObject();
//                    String higlightedText = highlighter.highlight(document, companies.get(i));
//
//
//                    higlightedText = htmlStrings.getBeforeTitle() + Folders.get(i) +
//                            htmlStrings.getAfterTitle() + higlightedText +
//                            htmlStrings.getEnd();
//
//                    fileHandler.saveStringAsFile("data/highlighted2/" + Folders.get(i) + "/" + document.getIndex() + ".html", higlightedText);
//
//                    System.out.printf("%d is done\n", j);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
    /**
     * This function highlights the company name, surprise and uncertainty feature in the documents
     * Below is the structure of the switch control flow
     * company name ->  true
     *                        surprise -> true
     *                                          uncertainty -> true   case 1    Company: Surprise & Uncertainty
     *
     *                                                      -> false  case 2    Company: Surprise
     *                                 -> false
     *                                          uncertainty -> true   case 3    Company: Uncertainty
     *
     *                                                      -> false  case 4    Company
     *              ->  false
     *                        surprise -> true
     *                                          uncertainty -> true   case 5    Surprise & Uncertainty
     *
     *                                                      -> false  case 6    Surprise
     *                                 -> false
     *                                          uncertainty -> true   case 7    Uncertainty
     *
     *                                                      -> false  case 8
     *
     *  Since "(" and ")" are transformed to "-LRB-" and "-RRB-"  by commands .get(CoreAnnotations.TextAnnotation.class),
     *  we need to convert them to their original form when write the text to a html file.
     *
     *  In the case where company name appears, we check the words one by one and add mark to the name which results in yellow
     *  background color and highlight it in bold.
     *
     *  In the case where surprise/uncertainty feature appears, we check the words one by one and highlight the key word in bold.
     *
     *  In the case where surprise_comparative feature appears, we check the pos one by one and highlight the comparative word in italic.
     *
     *  In the case where uncertainty_conditionality feature appears, we just highlight the whole sentence.
     *
     *  At the end of each case, we will add what appears in the sentence(the company name, surprise feature or uncertainty feature).
     *
     * @param document the annotation of the document
     * @param company the name string of the company
     *
     * @return the strings of highlighted document
     *
     * */

    public String highlight(Document document, String company) throws Exception {

        String highlightedString = "";
        SurpriseFeature surpriseFeature = new SurpriseFeature();
        UncertaintyFeature uncertatintyFeature = new UncertaintyFeature();

        for (CoreMap sentenceStanford : document.getDocument().get(CoreAnnotations.SentencesAnnotation.class)) {

            int c = 8;
            List<CoreLabel> tokens = sentenceStanford.get(CoreAnnotations.TokensAnnotation.class);
            String string = sentenceStanford.get(CoreAnnotations.TextAnnotation.class);

            List<Integer> result1 = surpriseFeature.Surprise(sentenceStanford, "$SURPRISE");
            Integer sum1 = result1.stream().mapToInt(i -> i.intValue()).sum();
            Integer comparative = surpriseFeature.SurpriseComparative(sentenceStanford);
            String matchedWord1 = "";

            List<Integer> result2 = uncertatintyFeature.Uncertainty(sentenceStanford, "$UNCERTAINTY");
            Integer sum2 = result2.stream().mapToInt(i -> i.intValue()).sum();
            List<Integer> result3 = uncertatintyFeature.UncertaintyConditionality(sentenceStanford, "$CONDITIONALITY");
            Integer sum3 = result3.stream().mapToInt(i -> i.intValue()).sum();
            String matchedWord2 = "";

            if (string.contains(company)) {
                if (sum1 > 0 || comparative > 0) {
                    matchedWord1 = surpriseFeature.getMatchedWord();
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 1;
                    } else c = 2;
                } else {
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 3;
                    } else c = 4;
                }
            } else {
                if (sum1 > 0 || comparative > 0) {
                    matchedWord1 = surpriseFeature.getMatchedWord();
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 5;
                    } else c = 6;
                } else {
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 7;
                    } else c = 8;
                }
            }
            switch (c) {
                case 1: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else{
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted + wordString +
                            "(" + company + ": Surprise & Uncertainty) " + after_highlighted;
                    break;
                }
                case 2: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted2 + wordString +
                            "(" + company + ": Surprise) " + after_highlighted;
                    break;
                }
                case 3: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted3 + wordString +
                            "(" + company + ": Uncertainty) " + after_highlighted;
                    break;
                }
                case 4: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += wordString;
                    break;
                }
                case 5: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(matchedWord1) || word.equals(matchedWord2)){
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted + wordString +
                            " (Surprise & Uncertainty) " + after_highlighted;
                    break;
                }
                case 6: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(matchedWord1) || word.equals(matchedWord2)){
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted2 + wordString +
                            " (Surprise) " + after_highlighted;
                    break;
                }
                case 7: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(matchedWord1) || word.equals(matchedWord2)){
                            wordString += "<b>" + word + "</b> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted3 + wordString +
                            " (Uncertainty) " + after_highlighted;
                    break;
                }
                case 8: {
                    highlightedString += before_standard + sentenceStanford.get(CoreAnnotations.TextAnnotation.class)
                            + after_standard;
                    break;
                }
            }
        }
        return highlightedString;
    }

    /**
     * This function highlights the company name, surprise and uncertainty feature in the documents
     * Below is the structure of the switch control flow
     * company name ->  true
     *                        surprise -> true
     *                                          uncertainty -> true   case 1    Company: Surprise & Uncertainty
     *
     *                                                      -> false  case 2    Company: Surprise
     *                                 -> false
     *                                          uncertainty -> true   case 3    Company: Uncertainty
     *
     *                                                      -> false  case 4    Company
     *              ->  false
     *                        surprise -> true
     *                                          uncertainty -> true   case 5    Surprise & Uncertainty
     *
     *                                                      -> false  case 6    Surprise
     *                                 -> false
     *                                          uncertainty -> true   case 7    Uncertainty
     *
     *                                                      -> false  case 8
     *
     *  Since "(" and ")" are transformed to "-LRB-" and "-RRB-"  by commands .get(CoreAnnotations.TextAnnotation.class),
     *  we need to convert them to their original form when write the text to a html file.
     *
     *  In the case where company name appears, we check the words one by one and add mark to the name which results in yellow
     *  background color and highlight it in bold.
     *
     *  In the case where surprise/uncertainty feature appears, we check the words one by one and highlight the key word in bold.
     *
     *  In the case where surprise_comparative feature appears, we check the pos one by one and highlight the comparative word in italic.
     *
     *  In the case where uncertainty_conditionality feature appears, we just highlight the whole sentence.
     *
     *  At the end of each case, we will add what appears in the sentence(the company name, surprise feature or uncertainty feature).
     *
     * @param document the annotation of the document
     * @param company the name string of the company
     *
     * @return the strings of highlighted document
     *
     * */

    public String highlight2(Document document, String company) throws Exception {

        String highlightedString = "";
        SurpriseFeature surpriseFeature = new SurpriseFeature();
        UncertaintyFeature uncertatintyFeature = new UncertaintyFeature();

        for (CoreMap sentenceStanford : document.getDocument().get(CoreAnnotations.SentencesAnnotation.class)) {

            int c = 8;
            List<CoreLabel> tokens = sentenceStanford.get(CoreAnnotations.TokensAnnotation.class);
            String string = sentenceStanford.get(CoreAnnotations.TextAnnotation.class);

            List<Integer> result1 = surpriseFeature.Surprise(sentenceStanford, "$SURPRISE");
            Integer sum1 = result1.stream().mapToInt(i -> i.intValue()).sum();
            Integer comparative = surpriseFeature.SurpriseComparative(sentenceStanford);
            String matchedWord1 = "";

            List<Integer> result2 = uncertatintyFeature.Uncertainty(sentenceStanford, "$UNCERTAINTY");
            Integer sum2 = result2.stream().mapToInt(i -> i.intValue()).sum();
            List<Integer> result3 = uncertatintyFeature.UncertaintyConditionality(sentenceStanford, "$CONDITIONALITY");
            Integer sum3 = result3.stream().mapToInt(i -> i.intValue()).sum();
            String matchedWord2 = "";

            if (string.contains(company)) {
                if (sum1 > 0 || comparative > 0) {
                    matchedWord1 = surpriseFeature.getMatchedWord();
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 1;
                    } else c = 2;
                } else {
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 3;
                    } else c = 4;
                }
            } else {
                if (sum1 > 0 || comparative > 0) {
                    matchedWord1 = surpriseFeature.getMatchedWord();
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 5;
                    } else c = 6;
                } else {
                    if (sum2 > 0 || sum3 > 0) {
                        matchedWord2 = uncertatintyFeature.getMatchedWord();
                        c = 7;
                    } else c = 8;
                }
            }
            switch (c) {
                case 1: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else{
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted + wordString +
                            "(" + company + ": Surprise & Uncertainty) " + after_highlighted;
                    break;
                }
                case 2: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted2 + wordString +
                            "(" + company + ": Surprise) " + after_highlighted;
                    break;
                }
                case 3: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted3 + wordString +
                            "(" + company + ": Uncertainty) " + after_highlighted;
                    break;
                }
                case 4: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b><mark>" + word + "</mark></b> ";
                        }else if ( word.equals(matchedWord1) || word.equals(matchedWord2)) {
                            wordString += "<b>" + word + "</b> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += wordString;
                    break;
                }
                case 5: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(matchedWord1) || word.equals(matchedWord2)){
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted + wordString +
                            " (Surprise & Uncertainty) " + after_highlighted;
                    break;
                }
                case 6: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        String pos = tokens.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(matchedWord1) || word.equals(matchedWord2)){
                            wordString += "<b>" + word + "</b> ";
                        }else if ( pos.equals("JJR")||pos.equals("RBR")) {
                            wordString += "<i>" + word + "</i> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted2 + wordString +
                            " (Surprise) " + after_highlighted;
                    break;
                }
                case 7: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(matchedWord1) || word.equals(matchedWord2)){
                            wordString += "<b>" + word + "</b> ";
                        }else {
                            wordString += word + " ";
                        }
                    }
                    highlightedString += before_highlighted3 + wordString +
                            " (Uncertainty) " + after_highlighted;
                    break;
                }
                case 8: {
                    highlightedString += before_standard + sentenceStanford.get(CoreAnnotations.TextAnnotation.class)
                            + after_standard;
                    break;
                }
            }
        }
        return highlightedString;
    }
}


