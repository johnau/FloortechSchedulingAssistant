package tech.jmcs.floortech.scheduling.app;

import tech.jmcs.floortech.scheduling.app.datasource.converter.BeamDataConverter;
import tech.jmcs.floortech.scheduling.app.datasource.converter.SheetsDataConverter;
import tech.jmcs.floortech.scheduling.app.datasource.converter.SlabDataConverter;
import tech.jmcs.floortech.scheduling.app.datasource.converter.TrussDataConverter;
import tech.jmcs.floortech.scheduling.app.datasource.model.*;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;

import java.util.Map;

/**
 * Class to aggregate the converters for easier use.
 * Could be static right now, but leave as an injected object so that storage can be added?
 */
public class ExtractedDataToScheduleConverter {

    /**
     * Converts ExtractedData object to Map
     * @return Map of Values stored by schedule entry name
     */
    public Map<String, Object> convertBeamDataForSchedule(Map<Long, BeamData> data, SettingsHolder settingsHolder) {
        BeamDataConverter converter = new BeamDataConverter(settingsHolder);
        return converter.convert(data);
    }

    public Map<String, Object> convertTrussDataForSchedule(Map<Long, TrussData> data, SettingsHolder settingsHolder) {
        TrussDataConverter converter = new TrussDataConverter(settingsHolder);
        return converter.convert(data);
    }

    public Map<String, Object> convertSlabDataForSchedule(Map<Long, SlabData> data, SettingsHolder settingsHolder) {
        SlabDataConverter converter = new SlabDataConverter(settingsHolder);
        return converter.convert(data);
    }

    public Map<String, Object> convertSheetDataForSchedule(Map<Long, SheetData> data, SettingsHolder settingsHolder) {
        SheetsDataConverter converter = new SheetsDataConverter(settingsHolder);
        return converter.convert(data);
    }


}
