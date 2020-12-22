package tech.jmcs.floortech.scheduling.app.datasource.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.datasource.model.TrussData;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.types.EndCapType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrussDataConverter extends DataFormatConverter<TrussData> {
    protected static final Logger LOG = LoggerFactory.getLogger(TrussDataConverter.class);

    private static String TRUSS_TYPE_CW260 = "CW260";
    private static String TRUSS_TYPE_CW346 = "CW346";
    private static String TRUSS_TYPE_HJ200 = "HJ200";
    private static String TRUSS_TYPE_HJ300 = "HJ300";

    private SettingsHolder settingsHolder;

    public TrussDataConverter(SettingsHolder settingsHolder) {
        this.settingsHolder = settingsHolder;
    }

    @Override
    public Map<String, Object> convert(Map<Long, TrussData> dataMap) {

        Map<String, Object> valuesMap = new HashMap<>();

        // populate entries to collect
        valuesMap.put(this.settingsHolder.getScheduleEntryCw260Truss(), 0d);//
        valuesMap.put(this.settingsHolder.getScheduleEntryCw346Truss(), 0d);//
        valuesMap.put(this.settingsHolder.getScheduleEntryHj200Truss(), 0d);//
        valuesMap.put(this.settingsHolder.getScheduleEntryHj300Truss(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryConnectionEndcapsCw260(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryConnectionEndcapsCw346(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryConnectionEndcapsHj200(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryConnectionEndcapsHj300(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryStandardEndcapsCw260(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryStandardEndcapsCw346(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryStandardEndcapsHj200(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryStandardEndcapsHj300(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryTrussAirConPenoCw260(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryTrussAirConPenoCw346(), 0d);
        valuesMap.put(this.settingsHolder.getScheduleEntryTrussAirConPenoHj300(), 0d);

        valuesMap.forEach((entry, value) -> LOG.debug("Entry added: {}", entry));

        dataMap.forEach( (id, td) -> {
            LOG.debug("Processing: {} : {} {}mm {}", id, td.getTrussId(), td.getLength(), td.getType());
            String trussId = td.getTrussId();
            Long length = td.getLength();
            Long qty = td.getQty();
            String type = td.getType();
            EndCapType leftEndcap = td.getLeftEndcap();
            EndCapType rightEndcap = td.getRightEndcap();
            boolean airConPeno = td.hasAirconPenetration();
            List<Integer> penoWebCuts = td.getPenetrationWebCuts();
            Integer packingGroup = td.getPackingGroup();

            String trussTypeEntryName = "";
            String connectionEndcapEntryName = "";
            String standardEndcapEntryName = "";
            String trussAcPenoEntryName = "";
            if (type.equals(TRUSS_TYPE_CW260)) {
                LOG.debug("Handling CW260 truss");
                trussTypeEntryName = this.settingsHolder.getScheduleEntryCw260Truss();
                connectionEndcapEntryName = this.settingsHolder.getScheduleEntryConnectionEndcapsCw260();
                standardEndcapEntryName = this.settingsHolder.getScheduleEntryStandardEndcapsCw260();
                trussAcPenoEntryName = this.settingsHolder.getScheduleEntryTrussAirConPenoCw260();
            } else if (type.equals(TRUSS_TYPE_CW346)) {
                LOG.debug("Handling CW346 truss");
                trussTypeEntryName = this.settingsHolder.getScheduleEntryCw346Truss();
                connectionEndcapEntryName = this.settingsHolder.getScheduleEntryConnectionEndcapsCw346();
                standardEndcapEntryName = this.settingsHolder.getScheduleEntryStandardEndcapsCw346();
                trussAcPenoEntryName = this.settingsHolder.getScheduleEntryTrussAirConPenoCw346();
            } else if (type.equals(TRUSS_TYPE_HJ200)) {
                LOG.debug("Handling HJ200 truss");
                trussTypeEntryName = this.settingsHolder.getScheduleEntryHj200Truss();
                connectionEndcapEntryName = this.settingsHolder.getScheduleEntryConnectionEndcapsHj200();
                standardEndcapEntryName = this.settingsHolder.getScheduleEntryStandardEndcapsHj200();
//                trussAcPenoEntryName = "";
            } else if (type.equals(TRUSS_TYPE_HJ300)) {
                LOG.debug("Handling HJ300 truss");
                trussTypeEntryName = this.settingsHolder.getScheduleEntryHj300Truss();
                connectionEndcapEntryName = this.settingsHolder.getScheduleEntryConnectionEndcapsHj300();
                standardEndcapEntryName = this.settingsHolder.getScheduleEntryStandardEndcapsHj300();
                trussAcPenoEntryName = this.settingsHolder.getScheduleEntryTrussAirConPenoHj300();
            } else {
                LOG.debug("Type not recognized: {}", type);
            }

            LOG.debug("{} : {} | {} : {} | {} : {} | {} : {}", trussTypeEntryName, valuesMap.containsKey(trussTypeEntryName), connectionEndcapEntryName, valuesMap.containsKey(connectionEndcapEntryName),
                    standardEndcapEntryName, valuesMap.containsKey(standardEndcapEntryName), trussAcPenoEntryName, valuesMap.containsKey(trussAcPenoEntryName));

            valuesMap.computeIfPresent(trussTypeEntryName, (name, val) -> {
                if (val.getClass().equals(Double.class)) {
                    Double newValue = (double) val + (length * qty / 1000.0d);
                    LOG.debug("Updating truss length from : {} to {}", val, newValue);
                    return newValue;
                }
                else return val;
            });

            valuesMap.computeIfPresent(standardEndcapEntryName, (name, val) -> {
                if (val.getClass().equals(Double.class)) {
                    Double dVal = (Double) val;
                    if (leftEndcap.equals(EndCapType.STANDARD)) dVal = dVal + qty.intValue();
                    if (rightEndcap.equals(EndCapType.STANDARD)) dVal = dVal + qty.intValue();
                    LOG.debug("Updating Standard endcaps from : {} to : {}", val, dVal);
                    return dVal;
                } else return val;
            });

            valuesMap.computeIfPresent(connectionEndcapEntryName, (name, val) -> {
                if (val.getClass().equals(Double.class)) {
                    Double dVal = (Double) val;
                    if (!leftEndcap.equals(EndCapType.STANDARD)) dVal = dVal + qty.intValue();
                    if (!rightEndcap.equals(EndCapType.STANDARD)) dVal = dVal + qty.intValue();
                    LOG.debug("Updating Connection endcaps from : {} to : {}", val, dVal);
                    return dVal;
                } else return val;
            });

            valuesMap.computeIfPresent(trussAcPenoEntryName, (name, val) -> {
                if (val.getClass().equals(Double.class) && airConPeno) {
                    Double newValue = (Double) val + qty.intValue();
                    LOG.debug("Updating ac penos from : {} to : {}", val, newValue);
                    return newValue;
                } else return val;
            });

        });

        return valuesMap;
    }
}
