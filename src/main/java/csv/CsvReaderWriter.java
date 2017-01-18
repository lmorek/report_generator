package csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.MappingStrategy;
import loader.FileLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvReaderWriter {

    private final static Logger LOGGER = LogManager.getLogger(CsvReaderWriter.class.getName());

    private static String inputFilePath = "C:\\repo\\jmeter\\logs\\result\\";
    public final String fileName = "4.01.10_impactq.got.volvo.net_20160911-161409 _4_threads_result_tree.jtl.csv";
    private static String outputFilePath = null;

    private FileReader fileReader;
    private CSVReader csvReader;
    private FileWriter fileWriter;
    private CSVWriter csvWriter;

    private CsvFileBase fileBase;
    private Class type;
    private CsvFile file;
    private FileLoader loader;

    public CsvReaderWriter(CsvFile file) {
        //this.type = file.getClass();
        this.file = file;
    }

    private void closeReader() {
        try {
            fileReader.close();
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.error("Error when closing CSV files for reading");
            e.printStackTrace();
        }
    }

    private void closeWriter() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.error("Error when closing CSF files");
            e.printStackTrace();
        }
    }

    public void close() {
        closeReader();
        closeWriter();
    }

    private void initializeReader() {
        if (fileReader != null) {
            closeReader();
        }
        try {

            fileReader = new FileReader(inputFilePath + fileName);
            System.out.println("Took fileName:" + fileName);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error when reading file" + fileName);
            e.printStackTrace();
        }
        csvReader = new CSVReader(fileReader, ';', CSVWriter.NO_QUOTE_CHARACTER);
    }
    private void initializeWriter(){
        if(fileWriter != null) {
            closeWriter();
        }
        try {
            fileWriter = new FileWriter(inputFilePath+"result.csv");
        } catch (IOException e) {
            LOGGER.error("Error when opening file fro writing");
            e.printStackTrace();
        }
        csvWriter = new CSVWriter(fileWriter, ';', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);
    }

    public List<CsvFile> read() {
        initializeReader();
        CsvToBean<CsvFile> csvToBeanList = new CsvToBean<>();
        List<CsvFile> beanList = csvToBeanList.parse(setMappingStrategy(), csvReader);
        LOGGER.debug(String.format("Read %d line from %s file", beanList.size(), fileBase.getFileName()));
        System.out.println("Lines from files:"+beanList.size());
        return beanList;
    }

    private MappingStrategy<CsvFile> setMappingStrategy() {
        ColumnPositionMappingStrategy<CsvFile> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(CsvFile.class);
        strategy.setColumnMapping(fileBase.getMappingStrategy());
        return strategy;

    }


}
