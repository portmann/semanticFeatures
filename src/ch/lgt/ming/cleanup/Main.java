package ch.lgt.ming.cleanup;

import java.io.IOException;
import java.util.Properties;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.helper.FileHandler;
import ch.lgt.ming.highlighter.Highlighter;

public class Main {

	public static void main(String[] args) {

		StanfordCore.init();

		Corpus corpus = new Corpus("corpus");
		Highlighter highlighter = new Highlighter();
		FileHandler filehandler = new FileHandler();
		HTMLStrings htmlStrings = new HTMLStrings();

		String title = "Uncertainty_conditionality1";

		for (int i = 0; i < corpus.getDocCount(); i++) {

			try {
				String higlightedText = highlighter.highlight(corpus.getDocuments().get(i));

				if (!higlightedText.equals(""))

				{

					higlightedText = htmlStrings.getBeforeTitle() + title + htmlStrings.afterTitle + higlightedText
							+ htmlStrings.getEnd();

					filehandler.saveStringAsFile("highlighted/Uncertainty_conditionality1/" + i + ".html", higlightedText);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("Done: Higlighter.");

	}

}
