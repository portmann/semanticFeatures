package ch.lgt.ming.processing;

import ch.lgt.ming.corenlp.StanfordCore;
import ch.lgt.ming.datastore.IdBoolean;
import ch.lgt.ming.datastore.IdListString;
import ch.lgt.ming.datastore.IdString;
import ch.lgt.ming.extraction.sentnence.CompaniesAll;
import ch.lgt.ming.extraction.sentnence.Extractor;
import ch.lgt.ming.extraction.sentnence.Merger;
import ch.lgt.ming.helper.CsvFileWriter;
import ch.lgt.ming.helper.FileHandler;
import com.google.protobuf.ByteString;
import edu.stanford.nlp.pipeline.Annotation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Ming Deng on 8/25/2016.
 */
public class Features2csv {
    public static void main(String[] args) throws IOException {

       CsvFileWriter.writeCsvFileWriter("featureFiles/features.csv", 20);

    }

}
