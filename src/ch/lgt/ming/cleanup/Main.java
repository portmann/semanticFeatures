package ch.lgt.ming.cleanup;

import java.io.IOException;
import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.helper.FileHandler;
import ch.lgt.ming.highlighter.Highlighter;

public class Main {

	public static void main(String[] args) {

		StanfordCore.init();

		Corpus corpus = new Corpus("corpus3");
		Highlighter highlighter = new Highlighter();
		FileHandler filehandler = new FileHandler();
		HTMLStrings htmlStrings = new HTMLStrings();

		String title = "Uncertainty_conditionality";


		for (int i = 0; i < corpus.getDocCount(); i++) {

			try {
				String higlightedText = highlighter.highlight(corpus.getDocuments().get(i));

				if (!higlightedText.equals(""))

				{

					higlightedText = htmlStrings.getBeforeTitle() + title + htmlStrings.afterTitle + higlightedText
							+ htmlStrings.getEnd();

<<<<<<< HEAD
					filehandler.saveStringAsFile("highlighted/Uncertainty_conditionality_401_499/" + i + ".html", higlightedText);
=======
					filehandler.saveStringAsFile("highlighted/Uncertainty_conditionality2_301_400/" + i + ".html", higlightedText);

>>>>>>> origin/master
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("Done: Higlighter.");

	}

}
