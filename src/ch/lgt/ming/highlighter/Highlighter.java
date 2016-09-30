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

        for (int i = 0; i < 10; i++) {
            File folder = new File("data/corpus8/" + Folders.get(i));
            File[] listOfFiles = folder.listFiles();
//            for (int j = 0; j < listOfFiles.length; j++){
            for (int j = 0; j < 10; j++) {

                try {
                    fileInputStream = new FileInputStream("data/corpus8/" + Folders.get(i) + "/" + listOfFiles[j].getName());
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    Document document = (Document) objectInputStream.readObject();
                    String higlightedText = highlighter.highlight(document, companies.get(i));
                    if (!higlightedText.equals("")) {

                        higlightedText = htmlStrings.getBeforeTitle() + Folders.get(i) +
                                htmlStrings.getAfterTitle() + higlightedText +
                                htmlStrings.getEnd();

                        fileHandler.saveStringAsFile("data/highlighted2/" + Folders.get(i) + "/" + document.getIndex() + ".html", higlightedText);
                    }

                    System.out.printf("%d is done\n", j);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String highlight(Document document, String company) {

        String highlightedString = "";
        SurpriseFeature surpriseFeature = new SurpriseFeature();
        UncertaintyFeature uncertatintyFeature = new UncertaintyFeature();

        boolean highlighted = false;

        for (CoreMap sentenceStanford : document.getDocument().get(CoreAnnotations.SentencesAnnotation.class)) {

            int c = 8;
            List<CoreLabel> tokens = sentenceStanford.get(CoreAnnotations.TokensAnnotation.class);
            String string = sentenceStanford.get(CoreAnnotations.TextAnnotation.class);

            List<Integer> result1 = surpriseFeature.Surprise(sentenceStanford, "$SURPRISE");
            Integer sum1 = result1.stream().mapToInt(i -> i.intValue()).sum();

            List<Integer> result2 = uncertatintyFeature.Uncertainty(sentenceStanford, "$UNCERTAINTY");
            Integer sum2 = result2.stream().mapToInt(i -> i.intValue()).sum();

            if (string.contains(company)) {
                if (sum1 > 0) {
                    if (sum2 > 0) {
                        c = 1;
                    } else c = 2;
                } else {
                    if (sum2 > 0) {
                        c = 3;
                    } else c = 4;
                }
            } else {
                if (sum1 > 0) {
                    if (sum2 > 0) {
                        c = 5;
                    } else c = 6;
                } else {
                    if (sum2 > 0) {
                        c = 7;
                    } else c = 8;
                }
            }
            switch (c) {
                case 1: {
                    String wordString = "";
                    for (int i = 0; i < tokens.size(); i++){
                        String word = tokens.get(i).get(CoreAnnotations.TextAnnotation.class);
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b>" + word + "</b> ";
                        }else {
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
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b>" + word + "</b> ";
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
                        if (word.equals("-LRB-")) word = "(";
                        if (word.equals("-RRB-")) word = ")";
                        if (word.equals(company)){
                            wordString += "<b>" + word + "</b> ";
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
                    highlightedString += before_highlighted + sentenceStanford.get(CoreAnnotations.TextAnnotation.class) +
                            " (Surprise & Uncertainty) " + after_highlighted;
                    highlighted = true;
                    break;
                }
                case 6: {
                    highlightedString += before_highlighted2 + sentenceStanford.get(CoreAnnotations.TextAnnotation.class) +
                            " (Surprise) " + after_highlighted;
                    highlighted = true;
                    break;
                }
                case 7: {
                    highlightedString += before_highlighted3 + sentenceStanford.get(CoreAnnotations.TextAnnotation.class) +
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