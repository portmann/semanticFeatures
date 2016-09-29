package ch.lgt.ming.highlighter;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.cleanup.HTMLStrings;
import ch.lgt.ming.helper.FileHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 9/22/2016.
 */
public class HighlighterForCompany {
    // Highlighter highlights like
    // For highlighted text: *before_highlighted* *String* *after_highlighted*
    // For standard text: *before_standard* *String* *after_standard*

    public String before_highlighted = "<span style='background-color: #A9E2F3'>";
    public String before_highlighted2 = "<span style='background-color: #77ff33'>";
    public String after_highlighted = "</span>";
    public String before_standard = "";
    public String after_standard = "";

    private static List<String> companies = Arrays.asList("Microsoft","Google","Facebook","Amazon","Intel",
            "Goldman","Delta","Boeing","Ford","Netflix");
    private static List<String> Folders = Arrays.asList("Microsoft","Google","Facebook","Amazon","Intel",
            "Goldman_Sachs","Delta_Airline","Boeing","Ford","Netflix");

    public static void main(String[] args) throws IOException {
        HTMLStrings htmlStrings = new HTMLStrings();
        FileHandler filehandler = new FileHandler();
        HighlighterForCompany highlighterForCompany = new HighlighterForCompany();
        for (int i = 0; i < companies.size(); i++){
            Corpus corpus = new Corpus("data/corpus4/" + Folders.get(i), false);
            for (int j = 0; j < corpus.getDocCount(); j++){
                String higlightedText = highlighterForCompany.highlight(corpus.getDocuments().get(j),companies.get(i));
                if (!higlightedText.equals(""))

                {
                    higlightedText = htmlStrings.getBeforeTitle() + Folders.get(i) +
                            htmlStrings.getAfterTitle() + higlightedText +
                            htmlStrings.getEnd();


//                        System.out.println(corpus.getDocumentId().get(j));
//
//
//                    filehandler.saveStringAsFile("data/highlighted2/" + corpus.getDocumentId().get(j),higlightedText);

                }
            }
        }
    }

    public String highlight(Document document, String company) {

        String highlightedString = "";

        boolean highlighted = false;

        for (int i = 0; i < document.getTokenTextList().size(); i++) {

            if (document.getTokenTextList().get(i).equals(company)) {

                highlightedString = highlightedString + before_highlighted;
                highlightedString = highlightedString + document.getTokenTextList().get(i) + " ";
                highlightedString = highlightedString + after_highlighted;
                highlighted = true;

            }
            else {

				highlightedString = highlightedString + before_standard;
				highlightedString = highlightedString + document.getTokenTextList().get(i) + " ";
				highlightedString = highlightedString + after_standard;

			}
        }

        if (highlighted)
            return highlightedString;
        else
            return "";

    }
}
