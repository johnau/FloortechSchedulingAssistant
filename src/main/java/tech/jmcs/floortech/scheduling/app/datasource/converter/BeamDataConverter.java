package tech.jmcs.floortech.scheduling.app.datasource.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.datasource.model.BeamData;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.types.BeamTreatment;
import tech.jmcs.floortech.scheduling.app.types.DataSourceExtractorType;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class BeamDataConverter extends DataFormatConverter<BeamData> {
    private static final Logger LOG = LoggerFactory.getLogger(BeamDataConverter.class);

    private final SettingsHolder settingsHolder;

    public BeamDataConverter(SettingsHolder settingsHolder) {
        this.settingsHolder = settingsHolder;
    }

    /**
     * Convert data in an ExtractedTableData<BeamData> object into a Map<String, Object> where the Key is the
     * name of the cell in the Scheduling file and the Object is the value to be inserted into the associated cell.
     *
     * This method must take into account the Beam Treatment property, which may be changed by the user.
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> convert(Map<Long, BeamData> dataMap) {
//        String tableDataName = data.getName();
//        if (!tableDataName.toUpperCase().equals(DataSourceExtractorType.BEAM.getName())) {
//            // log warn
//            LOG.warn("Expected table name : BEAM LISTING but got: {}", tableDataName);
//        }

        Map<String, Object> valuesMap = new HashMap<>();

        // add up beam lengths by Beam Type and Beam Finish
        dataMap.forEach( (id, bd) -> {

            String bType = bd.getBeamType();
            BeamTreatment bTreatment = bd.getTreatment();

            String schedulingNameString = generateSchedulingNameString(bType, bTreatment).toUpperCase();

            Long bLen = bd.getLength();
            Long bQty = bd.getQuantity();
            LOG.debug("Found an entry: {}, with Len: {} and qty: {}", schedulingNameString, bLen, bQty);

            Long bTotal_mm = bLen * bQty; // storing value as mm
            Double bTotal_m = (bLen * bQty) / 1000d; // storing value as m

            Object existing = valuesMap.putIfAbsent(schedulingNameString, bTotal_m);
            if (existing != null) {
                valuesMap.computeIfPresent( schedulingNameString, (name, val) -> {
                    if (val.getClass().equals(Double.class)) {
                        Double lVal = (Double) val;
                        double newTotal = lVal + bTotal_m;
                        LOG.debug("Increased total of {} from: {} to: {} (+{}m)", schedulingNameString, lVal, newTotal, bTotal_m);
                        return newTotal;
                    }
                    LOG.debug("This Beam Data ({}) value {} was not type Long as expected. Actual type: {}", schedulingNameString, val, val.getClass().getName());
                    return val;
                });
            } else {
                LOG.debug("Created new beam entry: {} (Current length: {})", schedulingNameString, bTotal_m);
            }
        });

        return valuesMap;

    }

    private String generateSchedulingNameString(String bType, BeamTreatment treatment) {
        String treatmentStr = treatment.getName();

        return String.format("%s %s", bType, treatmentStr);
    }
}
