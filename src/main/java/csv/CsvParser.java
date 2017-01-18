package csv;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import loader.FileLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParser {
    private FileLoader fileLoader;

    private String fullPath = "C:\\repo\\jmeter\\logs\\result\\";
    private String outputPath = "C:\\repo\\jmeter\\logs\\output\\";
    private final String[] MAPPING_STRATEGY = {"timeStamp", "loadTime", "searchResultName", "statusCode", "statusTextCode", "textThreads", "text", "statusValue", "someValue", "value", "impactUrl", "latencyTime", "impactVersion", "modelType", "threadsCount"};
    //private String csvFileName = "4.01.10_impactq.got.volvo.net_20160911-161409 _4_threads_result_tree.jtl - Copy.csv";

    public CsvParser(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    public List<CsvFile> getCsvFilesFromList() {
        CsvToBean<CsvFile> csv = new CsvToBean<>();
        List<CsvFile> result = new ArrayList<>();
        for (String file : fileLoader.loadCsvFiles()) {
            CSVReader csvReader = null;
            try {
                csvReader = new CSVReader((new FileReader(fullPath + file)), ';');
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            List<CsvFile> list = csv.parse(setColumnMappingStrategy(), csvReader);
            for (Object object : list) {
                CsvFile fileName = (CsvFile) object;
                if (file.contains("_4_")) {
                    fileName.setThreadsCount("4");
                }
                if (file.contains("_8_")) {
                    fileName.setThreadsCount("8");
                }
                if (file.contains("4.01.10_")) {
                    fileName.setImpactVersion("4.01.10");
                }
                if (file.contains("4.02.10.26_")) {
                    fileName.setImpactVersion("4.02.10.26");
                }
            }
            result.addAll(list);
        }
        return result;
    }

    public List<CsvFile> filterCsvFile(List<CsvFile> list) {


        for (Object object : list) {
            CsvFile file = (CsvFile) object;
            if (file.getSearchResultName() != null) {
                if (file.getSearchResultName().contains("_BOM")) {
                    file.setModelType("BOM");
                    System.out.println(file);
                }
                if (file.getSearchResultName().contains("_LISA")) {
                    file.setModelType("LISA");
                    System.out.println(file);
                }
                if (file.getSearchResultName().contains("_VCS")) {
                    file.setModelType("VCS");
                    System.out.println(file);
                } else {
                    file.setModelType("Other");
                    System.out.println(file);
                }
            }
        }
        return list;
    }

    private ColumnPositionMappingStrategy<CsvFile> setColumnMappingStrategy() {
        ColumnPositionMappingStrategy<CsvFile> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(CsvFile.class);
        strategy.setColumnMapping(MAPPING_STRATEGY);
        return strategy;
    }

    public List<String> csvObjectToStringConverter() {
        List<String> strings = new ArrayList<>(filterCsvFile(getCsvFilesFromList()).size());
        strings.addAll(filterCsvFile(getCsvFilesFromList()).stream().map(object -> Objects.toString(object, null)).collect(Collectors.toList()));
        return strings;
    }

    public void writeBeanToCsv() {
        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter(outputPath + "example1.csv"), ';');
            BeanToCsv<CsvFile> beanToCsv = new BeanToCsv<>();
            List<CsvFile> csvList = filterCsvFile(getCsvFilesFromList());
            beanToCsv.write(setColumnMappingStrategy(), csvWriter, csvList);
        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            try {
                if (csvWriter != null) csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
