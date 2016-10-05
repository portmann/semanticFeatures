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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import ch.lgt.ming.corenlp.StanfordCore;

/**
 * Created by Ming Deng on 9/22/2016.
 */
public class HighlighterBoris {
	
	
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
    	
    	StanfordCore.init();

        HTMLStrings htmlStrings = new HTMLStrings();
        HighlighterBoris highlighter = new HighlighterBoris();

        FileHandler fileHandler = new FileHandler();

        for (int i = 0; i < 10; i++) {
            File folder = new File("data/corpusBoris/");
            File[] listOfFiles = folder.listFiles();
//          for (int j = 0; j < listOfFiles.length; j++){
            for (int j = 0; j < listOfFiles.length; j++) {

                try {
                	
                	String pathTemp = "data/corpusBoris/" + listOfFiles[j].getName();
                	String out = String.join("\n", Files.readAllLines(Paths.get(pathTemp)));
                	                	
                    Document document = new Document (out, Integer.parseInt(listOfFiles[j].getName().replace(".html", "")), new Date(1));
                    		
                    String higlightedText = highlighter.highlight(document, "Dummy String to highlight");
                    if (!higlightedText.equals("")) {

                        higlightedText = htmlStrings.getBeforeTitle() + "Boris Corpus" +
                                htmlStrings.getAfterTitle() + higlightedText +
                                htmlStrings.getEnd();

                        fileHandler.saveStringAsFile("data/highlighted3/SURPRISE_RELIEF" + document.getIndex() + ".html", higlightedText);
                    }

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
        }
    }

    public String highlight(Document document, String company) throws Exception {

        String highlightedString = "";
        SurpriseFeature surpriseFeature = new SurpriseFeature();
        UncertaintyFeature uncertatintyFeature = new UncertaintyFeature();

        boolean highlighted = false;

        for (CoreMap sentenceStanford : document.getDocument().get(CoreAnnotations.SentencesAnnotation.class)) {

            int c = 8;
            List<CoreLabel> tokens = sentenceStanford.get(CoreAnnotations.TokensAnnotation.class);
            String string = sentenceStanford.get(CoreAnnotations.TextAnnotation.class);

            List<Integer> result1 = surpriseFeature.Surprise(sentenceStanford, "$UNSPECIFIED");
            Integer sum1 = result1.stream().mapToInt(i -> i.intValue()).sum();
            Integer comparative = 0;
            String matchedWord1 = "";

            List<Integer> result2 = surpriseFeature.Surprise(sentenceStanford, "$UNSPECIFIED");
            Integer sum2 = result2.stream().mapToInt(i -> i.intValue()).sum();
            
          //List<Integer> result3 = uncertatintyFeature.UncertaintyConditionality(sentenceStanford, "$RELIEF");
            List<Integer> result3 = surpriseFeature.Surprise(sentenceStanford, "$UNSPECIFIED");
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
                    highlighted = true;
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
                    highlighted = true;
                    break;
                }
                case 3: {
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
                    highlightedString += before_highlighted3 + wordString +
                            "(" + company + ": Uncertainty) " + after_highlighted;
                    highlighted = true;
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
                    highlightedString = wordString;
                    highlighted = true;
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
                    highlighted = true;
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
                    highlighted = true;
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
                    highlighted = true;
                    break;
                }
                case 8: {
                    highlightedString += before_standard + sentenceStanford.get(CoreAnnotations.TextAnnotation.class)
                            + after_standard;
                    break;
                }
            }
        }
        if (highlighted)
            return highlightedString;
        else
            return "";
    }
}
