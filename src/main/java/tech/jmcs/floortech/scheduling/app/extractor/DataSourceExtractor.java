package tech.jmcs.floortech.scheduling.app.extractor;

import tech.jmcs.floortech.scheduling.app.extractor.exception.DataExtractorException;
import tech.jmcs.floortech.scheduling.app.extractor.model.ExtractedTableData;

/**
 * Interface for Data Source Extractor
 */
public interface DataSourceExtractor<T> {

    /**
     * Method to check the data source is valid
     * @return
     */
    public Boolean isValid();

    /**
     * Method to extract data from the data source into an ExtractedDataObject
     */
    public void extract() throws DataExtractorException;

    /**
     * Method to get the Data and close files / free memory
     * @return
     */
    public ExtractedTableData<T> getDataAndFinish();

}
