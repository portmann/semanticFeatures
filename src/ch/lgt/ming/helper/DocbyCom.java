package ch.lgt.ming.helper;

import ch.lgt.ming.datastore.IdListString;
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
 * Created by Ming Deng on 9/22/2016.
 */
public class DocbyCom {

    public static void main(String[] args) throws IOException {
        // Load corpus
        String path = "data/DataForMing_V2";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        FileHandler fileHandler = new FileHandler();
        IdString DocId_Text = new IdString();
        IdSetString DocId_TokensSet = new IdSetString();
        List<String> companies = Arrays.asList("microsoft","google","facebook","amazon","intel",
                "goldman","delta","boeing","ford","netflix");
        List<String> Folders = Arrays.asList("Microsoft","Google","Facebook","Amazon","Intel",
                "Goldman_Sachs","Delta_Airline","Boeing","Ford","Netflix");


        //Load the documents
        for (int i = 0; i < listOfFiles.length; i++) {
            DocId_Text.putValue(i, fileHandler.loadFileToString(path + "/" + listOfFiles[i].getName()));
            String[] strings = DocId_Text.getValue(i).replaceAll("[[^a-zA-Z_]&&\\S]", "").split("\\W+");
            List<String> listoftokens = Arrays.asList(strings).stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            Set<String> setoftokens = new HashSet<>(listoftokens);
//            System.out.println(setoftokens);
            DocId_TokensSet.putValue(i, setoftokens);
        }
            //Categorize the documents by companies

            for (int i = 0; i < listOfFiles.length; i++) {
                for (int j = 0; j < 10; j ++){
//            System.out.println(DocId_TokensSet.getValue(i));
                if (DocId_TokensSet.getValue(i).contains(companies.get(j))){
                    System.out.println("Found " + companies.get(j));
                    Files.copy(Paths.get(path + "/" + listOfFiles[i].getName()),
                            Paths.get("data/corpus6/" + Folders.get(j) + "/" + listOfFiles[i].getName()));
                }
            }
        }
    }
}
