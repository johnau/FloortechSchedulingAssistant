package tech.jmcs.floortech.scheduling.app.datasource.converter;

import tech.jmcs.floortech.scheduling.app.datasource.extractor.SlabListExtractor;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SlabData;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.types.MeasurementUnit;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SlabDataConverter extends DataFormatConverter<SlabData> {

    private final SettingsHolder settingsHolder;

    public SlabDataConverter(SettingsHolder settingsHolder) {
        this.settingsHolder = settingsHolder;
    }

    @Override
    public Map<String, Object> convert(Map<Long, SlabData> data) {

        String slabInternalEntryName = this.settingsHolder.getScheduleEntrySlabInternal();
        String slab2cRhsEntryName = this.settingsHolder.getScheduleEntrySlab2cRhs();
        String slab3cRhsEntryName = this.settingsHolder.getScheduleEntrySlab3cRhs();
        String slab2cInsituEntryName = this.settingsHolder.getScheduleEntrySlab2cInsitu();
        String slab3cInsituEntryName = this.settingsHolder.getScheduleEntrySlab3cInsitu();
        String slab4cInsituEntryName = this.settingsHolder.getScheduleEntrySlab4cInsitu();
        String slabThickAngleEntryName = this.settingsHolder.getScheduleEntrySlabThickAngle();
        String slabThinAngleEntryName = this.settingsHolder.getScheduleEntrySlabThinAngle();

        Map<String, Object> valuesMap = new HashMap<>();
        data.forEach( (i, slabData) -> {

            String name = slabData.getName();
            Double size = slabData.getSize();
            MeasurementUnit units = slabData.getMeasurementUnit();

            if (name.toUpperCase().equals(SlabListExtractor.INT_FLOOR.toUpperCase())) {
                valuesMap.put(slabInternalEntryName, size);
            } else if (name.toUpperCase().equals(SlabListExtractor.BALC_2C_RHS.toUpperCase())) {
                valuesMap.put(slab2cRhsEntryName, size);
            } else if (name.toUpperCase().equals(SlabListExtractor.BALC_3C_RHS.toUpperCase())) {
                valuesMap.put(slab3cRhsEntryName, size);
            } else if (name.toUpperCase().equals(SlabListExtractor.BALC_2C_INSITU.toUpperCase())) {
                valuesMap.put(slab2cInsituEntryName, size);
            } else if (name.toUpperCase().equals(SlabListExtractor.BALC_3C_INSITU.toUpperCase())) {
                valuesMap.put(slab3cInsituEntryName, size);
            } else if (name.toUpperCase().equals(SlabListExtractor.BALC_4C_INSITU.toUpperCase())) {
                valuesMap.put(slab4cInsituEntryName, size);
            } else if (name.toUpperCase().equals(SlabListExtractor.THICK_ANGLE.toUpperCase())) {
                valuesMap.put(slabThickAngleEntryName, size);
            } else if (name.toUpperCase().equals(SlabListExtractor.THIN_ANGLE.toUpperCase())) {
                valuesMap.put(slabThinAngleEntryName, size);
            }
        });

        return valuesMap;
    }
}
