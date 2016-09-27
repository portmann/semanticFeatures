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


        List<String> companies = Arrays.asList("amazon","boeing","delta","facebook","ford",
                "goldman","google","intel","microsoft","netflix");
        List<String> Folders = Arrays.asList("Amazon","Boeing","Delta_Airline","Facebook","Ford",
                "Goldman_Sachs","Google","Intel","Microsoft","Netflix");
        for (int i = 0; i < companies.size(); i++){

            String path = "data/corpus4/" + Folders.get(i);
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            FileHandler fileHandler = new FileHandler();
            IdString DocId_Text = new IdString();

//            Load the documents
            for (int j = 0; j < listOfFiles.length; j++) {
                DocId_Text.putValue(j, fileHandler.loadFileToString(path + "/" + listOfFiles[j].getName()));
            }

//                Files.createDirectories(Paths.get("data/corpus5/" + Folders.get(i) ));
            for (int j = 0; j < listOfFiles.length; j++) {
                if (DocId_Text.getValue(j).toLowerCase().contains("seeking alpha")){
                    System.out.println("Found " + "seeking alpha");
                    Files.copy(Paths.get(path + "/" + listOfFiles[j].getName()),
                            Paths.get("data/corpus5/" + Folders.get(i) + "/" + listOfFiles[j].getName()));
                }
            }
        }
    }
}
