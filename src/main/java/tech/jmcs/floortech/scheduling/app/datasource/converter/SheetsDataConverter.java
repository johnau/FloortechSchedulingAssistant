package tech.jmcs.floortech.scheduling.app.datasource.converter;

import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SheetData;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;

import java.util.Map;

public class SheetsDataConverter extends DataFormatConverter<SheetData> {

    private final SettingsHolder settingsHolder;

    public SheetsDataConverter(SettingsHolder settingsHolder) {
        this.settingsHolder = settingsHolder;
    }

    @Override
    public Map<String, Object> convert(Map<Long, SheetData> data) {
        /**
         * Need to collect:
         * - Need to collect qty of each sheet length
         */

        return null;
    }
}
