package tech.jmcs.floortech.scheduling.app.datasource.converter;

import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;

import java.util.Map;

public abstract class DataFormatConverter<T> {

    protected DataFormatConverter() {

    }

    /**
     * Convert data from an ExtractedTableData object to a Map of values stored by target cell name.
     * The Target Cell Name will match a cell in the Scheduling Excel sheet and be used to place the value.
     * @param data
     * @return
     */
    public abstract Map<String, Object> convert(Map<Long, T> data);

}
