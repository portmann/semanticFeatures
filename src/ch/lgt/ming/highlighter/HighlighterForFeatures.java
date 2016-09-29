package ch.lgt.ming.highlighter;

import ch.lgt.ming.cleanup.Corpus;
import ch.lgt.ming.cleanup.Document;
import ch.lgt.ming.cleanup.HTMLStrings;
import ch.lgt.ming.feature.SurpriseFeature;
import ch.lgt.ming.feature.UncertaintyFeature;
import ch.lgt.ming.helper.FileHandler;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ming Deng on 9/22/2016.
 */
public class HighlighterForFeatures {

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
        HighlighterForFeatures highlighterForFeatures = new HighlighterForFeatures();

        /**
         * Reading Documents.ser
         * */
		FileHandler fileHandler = new FileHandler();
		File folder = new File("data/corpus8/Facebook");
		File[] listOfFiles = folder.listFiles();
		Corpus corpus = new Corpus();

		FileInputStream fileInputStream = null;

		for (int i = 0; i < 10; i++){

			try {
				fileInputStream = new FileInputStream("data/corpus8/" + listOfFiles[i].getName());
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				Document document = (Document) objectInputStream.readObject();
                String higlightedText = highlighterForFeatures.highlight(document);
                if (!higlightedText.equals(""))

                {
                    higlightedText = htmlStrings.getBeforeTitle() +
                            htmlStrings.getAfterTitle() + higlightedText +
                            htmlStrings.getEnd();

                    System.out.println(document.getIndex() + " is done.");
                    filehandler.saveStringAsFile("data/highlighted2/Amazon/" + document.getIndex() + ".html", higlightedText);
                }

            } catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.printf("%d is done\n",i);
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
