package converter;

import loader.FileLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class FileProcessor {

    private static final String FOLDER_PATH = "C:\\repo\\jmeter\\logs\\";
    private static final String OUTPUT_PATH = "C:\\repo\\jmeter\\logs\\result\\";
    private static final String LINE_SEPARATOR = "line.separator";
    private static final String LOGIN_STRING = "Login";
    private static final String LOGOUT_STRING = "Logout";
    private static final String EMPTY_STRING = "";
    private static final String[] LOADERS = {"VCS", "LISA", "BOM"};
    private static final String[] IMPACT_RELEASE = {"online40210", "online40300", "online40100", "ONLINE_4.02.10.26"};

    private final FileLoader fileLoader;

    public FileProcessor(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    public void createOutputFolder() {
        Path path = Paths.get(OUTPUT_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> splitFileName(String fileName) {
        List<String> fileSplit = Arrays.asList(fileName.split("_"));
        return fileSplit;
    }

    public List<String> splitCsvFileName(String csvFileName) {
        return Arrays.asList(csvFileName.split("_"));
    }

    //TODO now working yet, not fully implemented
    public List<String> findDesiredFileFromImpactRelease(List<String> listOfFiles) {
        return listOfFiles.stream().filter(file -> file.contains(IMPACT_RELEASE[0]) || file.contains(IMPACT_RELEASE[3])).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    public void findUnwantedWord() throws IOException {

        createOutputFolder();
        for (String newFile : (fileLoader.loadFilesToList())) {
            BufferedReader br = new BufferedReader(new FileReader(FOLDER_PATH + newFile));
            System.out.println(br);
            File temporaryFile = new File(OUTPUT_PATH + newFile + ".csv");

            for (int index = 0; index < fileLoader.loadFilesToList().size(); index++) {
                PrintWriter pw = new PrintWriter(new FileWriter(temporaryFile));

                String fileContentToString = new String(Files.readAllBytes(Paths.get(FOLDER_PATH + newFile)));

                pw.println(removeUnwantedLinesFromTakenFile(fileContentToString));
                pw.flush();
                pw.close();
            }
            br.close();
        }
    }

    private String removeUnwantedLinesFromTakenFile(String fileContent) {
        String[] oneLine = fileContent.split(System.getProperty(LINE_SEPARATOR));

        for (int line = 0; line < oneLine.length; line++) {
            if (oneLine[line].contains(LOGIN_STRING) || oneLine[line].contains(LOGOUT_STRING)) {
                oneLine[line] = EMPTY_STRING;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();

        for (String s : oneLine)
            if (!s.equals(EMPTY_STRING)) {
                stringBuilder.append(s).append(System.getProperty(LINE_SEPARATOR));
            }
        return stringBuilder.toString().replaceAll(",", ";");
    }
}


