package ch.lgt.ming.highlighter;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.cleanup.HTMLStrings;
import ch.lgt.ming.feature.SurpriseFeature;
import ch.lgt.ming.feature.UncertaintyFeature;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 9/22/2016.
 */
public class HighlighterForSurprise {

	// Highlighter highlights like
	// For highlighted text: *before_highlighted* *String* *after_highlighted*
	// For standard text: *before_standard* *String* *after_standard*

	public String before_highlighted = "<span style='background-color: #A9E2F3'>";
    public String before_highlighted2 = "<span style='background-color: #77ff33'>";
    public String before_highlighted3 = "<span style='background-color: #FF0000'>";

    public String after_highlighted = "</span>";
	public String before_standard = "";
	public String after_standard = "";

//    private static List<String> companies = Arrays.asList("Microsoft","Google","Facebook","Amazon","Intel",
//            "Goldman","Delta","Boeing","Ford","Netflix");
    private static List<String> Folders = Arrays.asList("Microsoft","Google","Facebook","Amazon","Intel",
            "Goldman_Sachs","Delta_Airline","Boeing","Ford","Netflix");

    public static void main(String[] args) throws IOException {
        HTMLStrings htmlStrings = new HTMLStrings();
        FileHandler filehandler = new FileHandler();
        HighlighterForSurprise highlighterForSurprise = new HighlighterForSurprise();

        Corpus corpus = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("data/corpus4/Amazon100.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            corpus = (Corpus) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < companies.size(); i++){
//            Corpus corpus = new Corpus("data/corpus4/" + Folders.get(i));
            for (int j = 0; j < corpus.getDocCount(); j++){
                String higlightedText = highlighterForSurprise.highlight(corpus.getDocuments().get(j));
                if (!higlightedText.equals(""))

                {
                    higlightedText = htmlStrings.getBeforeTitle() +
                            htmlStrings.getAfterTitle() + higlightedText +
                            htmlStrings.getEnd();

                    System.out.println(corpus.getDocuments().get(j).getIndex());

                    filehandler.saveStringAsFile("data/highlighted2/Amazon/Surprise/" + corpus.getDocuments().get(j).getIndex() + ".html", higlightedText);

                }
            }
        }

//    }

	public String highlight(Document document) {


        SurpriseFeature surpriseFeature = new SurpriseFeature();
        UncertaintyFeature uncertatintyFeature = new UncertaintyFeature();
		String highlightedString = "";

		boolean highlighted = false;


		for (CoreMap sentenceStanford : document.getDocument().get(CoreAnnotations.SentencesAnnotation.class)) {

            List<Integer> result1 = surpriseFeature.Surprise(sentenceStanford, "$SURPRISE");
            Integer sum1 = result1.stream().mapToInt(i -> i.intValue()).sum();

            List<Integer> result2 = uncertatintyFeature.Uncertainty(sentenceStanford, "$UNCERTAINTY");
            Integer sum2 = result2.stream().mapToInt(i -> i.intValue()).sum();

			if (sum1 > 0) {

				highlightedString = highlightedString + before_highlighted;
				highlightedString = highlightedString + sentenceStanford.get(CoreAnnotations.TextAnnotation.class);
				highlightedString = highlightedString + " (SURPRISE) " +  after_highlighted;
				highlighted = true;

			}else if (sum2 > 0) {

				highlightedString = highlightedString + before_highlighted2;
				highlightedString = highlightedString + sentenceStanford.get(CoreAnnotations.TextAnnotation.class);
				highlightedString = highlightedString + " (UNCERTAINTY) " + after_highlighted;
				highlighted = true;

			}else {

				highlightedString = highlightedString + before_standard;
				highlightedString = highlightedString + sentenceStanford.get(CoreAnnotations.TextAnnotation.class);
				highlightedString = highlightedString + after_standard;

			}
		}

		if (highlighted)
			return highlightedString;
		else
			return "";

	}
}
