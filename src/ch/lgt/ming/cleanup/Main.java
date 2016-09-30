//package ch.lgt.ming.cleanup;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import ch.lgt.ming.corenlp.StanfordCore;
//import ch.lgt.ming.helper.FileHandler;
//import ch.lgt.ming.highlighter.Highlighter;
//
//public class Main {
//
//	public static void main(String[] args) throws IOException {
//
////		StanfordCore.init();
//
//		Corpus corpus = new Corpus("corpus4/Facebook");
//		Highlighter highlighter = new Highlighter();
//		FileHandler filehandler = new FileHandler();
//		HTMLStrings htmlStrings = new HTMLStrings();
//
//		String title = "Facebook";
//
//        for (int i = 0; i < corpus.getDocCount(); i++) {
//
//			try {
//				String higlightedText = highlighter.highlight(corpus.getDocuments().get(i),"Facebook");
//
//
//				if (!higlightedText.equals(""))
//
//				{
//					higlightedText = htmlStrings.getBeforeTitle() + title +
//                                    htmlStrings.getAfterTitle() + higlightedText +
//                                    htmlStrings.getEnd();
//
//					filehandler.saveStringAsFile("highlighted2/Facebook/" + i + ".html", higlightedText);
//
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//		System.out.println("Done: Higlighter.");
//
//	}
//
//}
