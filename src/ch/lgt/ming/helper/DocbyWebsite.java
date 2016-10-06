package ch.lgt.ming.helper;

import ch.lgt.ming.datastore.IdSetString;
import ch.lgt.ming.datastore.IdString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Ming Deng on 9/25/2016.
 */
public class DocbyWebsite {
    public static void main(String[] args) throws IOException {

        FileHandler fileHandler = new FileHandler();

        List<String> companies = Arrays.asList("amazon", "boeing", "delta", "facebook", "ford",
                "goldman", "google", "intel", "microsoft", "netflix");
        List<String> Folders = Arrays.asList("Amazon", "Boeing", "Delta_Airline", "Facebook", "Ford",
                "Goldman_Sachs", "Google", "Intel", "Microsoft", "Netflix");

        /**
         * This part of code
         * */
//        for (int i = 0; i < companies.size(); i++){

//            String path = "data/corpus4/" + Folders.get(i);
//            File folder = new File(path);
//            File[] listOfFiles = folder.listFiles();
//            FileHandler fileHandler = new FileHandler();
//            IdString DocId_Text = new IdString();
//
//            for (int j = 0; j < listOfFiles.length; j++) {
//                DocId_Text.putValue(j, fileHandler.loadFileToString(path + "/" + listOfFiles[j].getName()));
//            }
        /**
         * This line creates new directories in the new corpus folder
         * */
//            Files.createDirectories(Paths.get("data/highlighted2/" + Folders.get(i) ));
        /**
         * This part moves docs from seeking alpha to corpus5
         * */
//            for (int j = 0; j < listOfFiles.length; j++) {
//                if (DocId_Text.getValue(j).toLowerCase().contains("seeking alpha")){
//                    System.out.println("Found " + "seeking alpha");
//                    Files.copy(Paths.get(path + "/" + listOfFiles[j].getName()),
//                            Paths.get("data/corpus5/" + Folders.get(i) + "/" + listOfFiles[j].getName()));
//                }
//            }
//        }

//        String path = "data/DataForMing_V2";
//        File folder = new File(path);
//        File[] listOfFiles = folder.listFiles();
//        FileHandler fileHandler = new FileHandler();
//
//        for (File file:listOfFiles){
//
//            String docText = fileHandler.loadFileToString(path + "/" + file.getName());
//            if (docText.toLowerCase().contains("seeking alpha")){
//                System.out.println("Found " + "seeking alpha");
//                Files.copy(Paths.get(path + "/" + file.getName()),
//                        Paths.get("data/corpus6/" + file.getName()));
//            }
//        }

        /**
         * This part of code group all annotated documents from seeking alpha according to company names into folder corpus8
         * */
//        String path = "data/corpus5";
//        for (int i = 0; i < 10; i++) {
//            File folder = new File(path + "/" + Folders.get(i));
//            File[] listOfFiles = folder.listFiles();
//
//            for (File file : listOfFiles) {
//                Files.copy(Paths.get( "data/corpus7/" + file.getName().substring(0, file.getName().lastIndexOf('.')) + ".ser"),
//                        Paths.get("data/corpus8/" + Folders.get(i) + "/" + file.getName().substring(0, file.getName().lastIndexOf('.')) + ".ser"));
//            }
//        }

        /**
         * This part of code found all documents from Reuters
         *
         * */
        String path = "data/DataForMing_V2";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();


        for (File file : listOfFiles) {

            String docText = fileHandler.loadFileToString(path + "/" + file.getName());
            if (docText.toLowerCase().contains("reuters")) {
                System.out.println("Found " + "reuters");
                Files.copy(Paths.get(path + "/" + file.getName()),
                        Paths.get("data/Reuters/" + file.getName()));
            }
        }
    }
}
