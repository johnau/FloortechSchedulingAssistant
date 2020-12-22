package tech.jmcs.floortech.scheduling.app.datasource.converter;

import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;

import java.util.Map;

// TODO: Implement Generic Data Converter
public class GenericDataConverter extends DataFormatConverter<Map<String, Object>> {

    @Override
    public Map<String, Object> convert(Map<Long, Map<String, Object>> data) {
        return null;
    }

}
