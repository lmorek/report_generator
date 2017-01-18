import converter.FileProcessor;
import csv.CsvParser;
import loader.FileLoader;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        new FileProcessor(new FileLoader()).findUnwantedWord();
//        new FileProcessor(new FileLoader()).splitFileName();
        new CsvParser(new FileLoader()).writeBeanToCsv();
    }
}
