package loader;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileLoader {

    public static final String CSV_PATH_NAME = "C:\\repo\\jmeter\\logs\\result";
    private static final String PATH_NAME = "C:\\repo\\jmeter\\logs";
    private static final String MATCHING_STRING = "result_tree";


    public List<String> loadFilesToList() {
        File folder = new File(PATH_NAME);
        String[] fileList = folder.list();
        List<String> listOfFiles = Arrays.asList(fileList);
        return listOfFiles.stream().filter(file -> file.contains(MATCHING_STRING)).collect(Collectors.toList());
    }

    public List<String> loadCsvFiles() {
        File folder = new File(CSV_PATH_NAME);
        String[] fileList = folder.list();
        return Arrays.asList(fileList);
    }

}
