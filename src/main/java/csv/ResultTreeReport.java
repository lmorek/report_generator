package csv;


public class ResultTreeReport implements CsvFileBase {

    private final static String FILE_NAME = "";
    private final static String[] MAPPING_STRATEGY = {"timeStamp", "loadTime", "searchResultName", "statusCode", "statusTextCode", "testThreads", "text", "statusValue", "someValue", "latencyTime", "impactVersion","modelType","threadsCount"};


    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    @Override
    public String[] getMappingStrategy() {
        return MAPPING_STRATEGY;
    }
}
