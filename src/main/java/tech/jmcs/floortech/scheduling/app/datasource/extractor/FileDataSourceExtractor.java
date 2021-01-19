package tech.jmcs.floortech.scheduling.app.datasource.extractor;

import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class FileDataSourceExtractor<T> implements DataSourceExtractor<T> {

    private final Path sourceFilePath;
    protected ExtractedTableData<T> dataObject;
    protected List<String> validationErrors;

    protected FileDataSourceExtractor(Path sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
        this.validationErrors = new ArrayList<>();
    }

    public Path getSourceFilePath() {
        return sourceFilePath;
    }

    public List<String> getValidationErrors() {
        return this.validationErrors;
    }


}
