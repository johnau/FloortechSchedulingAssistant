package tech.jmcs.floortech.scheduling.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.datasource.model.BeamData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SheetData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SlabData;
import tech.jmcs.floortech.scheduling.app.datasource.model.TrussData;

import java.util.*;

/**
 * Class to store data extracted in an observable way for the DataFrame view and other data display views to use
 */
public class ExtractedDataHolderFX {
    protected static final Logger LOG = LoggerFactory.getLogger(ExtractedDataHolderFX.class);

    private ObjectProperty<Date> lastUpdated;

    private Map<Long, BeamData> beamDataMap;

    private Map<Long, TrussData> trussDataMap;

    private Map<Long, SheetData> sheetDataMap;

    private Map<Long, SlabData> slabDataMap;

    private Map<String, Map<Long, Map<String, Object>>> customData; // TODO: Ensure that (throughout the app) extractors have unique names.

    public ExtractedDataHolderFX() {
        LOG.debug("ObservableExtractedData constructing...");
        this.lastUpdated = new SimpleObjectProperty<>();
        this.lastUpdated.set(new Date());
    }

    public void clear() {
        if (this.beamDataMap != null) this.beamDataMap.clear();
        if (this.trussDataMap != null) this.trussDataMap.clear();
        if (this.sheetDataMap != null) this.sheetDataMap.clear();
        if (this.slabDataMap != null) this.slabDataMap.clear();
        if (this.customData != null) this.customData.clear();
        this.setLastUpdated();
    }
    public Date getLastUpdated() {
        return lastUpdated.get();
    }

    public ObjectProperty<Date> lastUpdatedProperty() {
        return lastUpdated;
    }

    public void setLastUpdated() {
        this.lastUpdated.set(new Date());
    }

    public Map<Long, BeamData> getBeamDataMap() {
        return beamDataMap;
    }

    public void setBeamDataMap(Map<Long, BeamData> beamDataMap) {
        this.beamDataMap = beamDataMap;
        this.setLastUpdated();
    }

    public Map<Long, TrussData> getTrussDataMap() {
        return trussDataMap;
    }

    public void setTrussDataMap(Map<Long, TrussData> trussDataMap) {
        this.trussDataMap = trussDataMap;
        this.setLastUpdated();
    }

    public Map<Long, SheetData> getSheetDataMap() {
        return sheetDataMap;
    }

    public void setSheetDataMap(Map<Long, SheetData> sheetDataMap) {
        this.sheetDataMap = sheetDataMap;
        this.setLastUpdated();
    }

    public Map<Long, SlabData> getSlabDataMap() {
        return slabDataMap;
    }

    public void setSlabDataMap(Map<Long, SlabData> slabDataMap) {
        this.slabDataMap = slabDataMap;
        this.setLastUpdated();
    }

    public Map<String, Map<Long, Map<String, Object>>> getCustomData() {
        return customData;
    }

    public void addCustomData(String name, Map<Long, Map<String, Object>> customData) {
        if (this.customData == null) {
            this.customData = new HashMap<>();
        }
        this.customData.put(name, customData);
        this.setLastUpdated();
    }

    public void setCustomData(Map<String, Map<Long, Map<String, Object>>> customData) {
        this.customData = customData;
        this.setLastUpdated();
    }

    //
//    public List<Map<Long, Map<String, Object>>> getCustomDataList() {
//        return customDataList;
//    }
//
//    public void addCustomData(Map<Long, Map<String, Object>> data) {
//        if (customDataList == null) {
//            customDataList = new ArrayList<>();
//        }
//        this.customDataList.add(data);
//        this.setLastUpdated();
//    }
//
//    public void setCustomDataList(List<Map<Long, Map<String, Object>>> customDataList) {
//        this.customDataList = customDataList;
//        this.setLastUpdated();
//    }

}
