package tech.jmcs.floortech.scheduling.app.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Settings Memory class
 * Holds settings to be accessed throughout the app (Class is container managed and injected, should not be instantiated
 * Will be instantiated when a Settings Loader is used.
 */
public class SettingsHolder {
    protected static final Logger LOG = LoggerFactory.getLogger(SettingsHolder.class);

    private Path jobFilesSchedulingRootPath;
    private Path jobFoldersDetailingRootPath;

    private String excelScheduleSheetName;
    private Integer excelScheduleDataNameCol;
    private Integer excelScheduleDataValueCol;
    private Integer excelScheduleDataUnitCol;
    private Integer excelScheduleDataRateCol;
    private Integer excelScheduleDataTotalCol;
    private Integer excelScheduleDataCommentCol;

    private List<String> excelScheduleFileSections;

    private Boolean builtInTrussExtractorEnabled;
    private String trussScheduleSectionName;
    private Boolean builtInBeamExtractorEnabled;
    private String beamScheduleSectionName;
    private Boolean builtInSheetExtractorEnabled;
    private String sheetScheduleSectionName;
    private Boolean builtInSlabExtractorEnabled;
    private String slabScheduleSectionName;

    private String scheduleEntryCw260Truss;
    private String scheduleEntryCw346Truss;
    private String scheduleEntryHj200Truss;
    private String scheduleEntryHj300Truss;
    private String scheduleEntryStandardEndcapsCw260;
    private String scheduleEntryStandardEndcapsCw346;
    private String scheduleEntryStandardEndcapsHj200;
    private String scheduleEntryStandardEndcapsHj300;
    private String scheduleEntryConnectionEndcapsCw260;
    private String scheduleEntryConnectionEndcapsCw346;
    private String scheduleEntryConnectionEndcapsHj200;
    private String scheduleEntryConnectionEndcapsHj300;
    private String scheduleEntryTrussAirConPenoCw260;
    private String scheduleEntryTrussAirConPenoCw346;
    private String scheduleEntryTrussAirConPenoHj300;

    private String scheduleEntrySteelBlackKeyword;
    private String scheduleEntrySteelGalvanisedKeyword;
    private String scheduleEntrySteelDimetKeyword;
    private String scheduleEntrySteelEpoxyKeyword;
    private String scheduleEntrySteelDuragalKeyword;

    private String scheduleEntrySlabInternal;
    private String scheduleEntrySlab2cRhs;
    private String scheduleEntrySlab3cRhs;
    private String scheduleEntrySlab2cInsitu;
    private String scheduleEntrySlab3cInsitu;
    private String scheduleEntrySlab4cInsitu;
    private String scheduleEntrySlabThickAngle;
    private String scheduleEntrySlabThinAngle;

    private List<String> dataSourceFileNamesMap;

    /**
     * Constructor
     */
    public SettingsHolder() {
        LOG.debug("Settings Holder instantiated (Empty)");
        this.dataSourceFileNamesMap = new ArrayList<>();
        this.excelScheduleFileSections = new ArrayList<>();
    }

    /**
     * Set all method
     * @param jobFilesSchedulingRootPath
     * @param jobFoldersDetailingRootPath
     * @param builtInBeamExtractorEnabled
     * @param builtInSheetExtractorEnabled
     * @param builtInSlabExtractorEnabled
     * @param builtInTrussExtractorEnabled
     */
    public void setAllSettings(Path jobFilesSchedulingRootPath, Path jobFoldersDetailingRootPath,
                               Boolean builtInBeamExtractorEnabled,
                               String beamScheduleSection,
                               Boolean builtInSheetExtractorEnabled,
                               String sheetScheduleSection,
                               Boolean builtInSlabExtractorEnabled,
                               String slabScheduleSection,
                               Boolean builtInTrussExtractorEnabled,
                               String trussScheduleSection,
                               String scheduleSheetName,
                               List<String> excelScheduleSections) {
        try {
            this.setJobFilesSchedulingRootPath(jobFilesSchedulingRootPath);
        } catch (FileNotFoundException e) {
        }
        try {
            this.setJobFoldersDetailingRootPath(jobFoldersDetailingRootPath);
        } catch (FileNotFoundException e) {
        }

        this.setBuiltInBeamExtractorEnabled(builtInBeamExtractorEnabled);
        this.setBeamScheduleSectionName(beamScheduleSection);
        this.setBuiltInSheetExtractorEnabled(builtInSheetExtractorEnabled);
        this.setSheetScheduleSectionName(sheetScheduleSection);
        this.setBuiltInSlabExtractorEnabled(builtInSlabExtractorEnabled);
        this.setSlabScheduleSectionName(slabScheduleSection);
        this.setBuiltInTrussExtractorEnabled(builtInTrussExtractorEnabled);
        this.setTrussScheduleSectionName(trussScheduleSection);

        this.setExcelScheduleSheetName(scheduleSheetName);
        this.setExcelScheduleFileSections(excelScheduleSections);
//        LOG.debug("Set: js path: {} jd path{} beam {} sheet {} slab {} truss {} ", jobFilesSchedulingRootPath, jobFoldersDetailingRootPath, builtInBeamExtractorEnabled, builtInSheetExtractorEnabled, builtInSlabExtractorEnabled, builtInTrussExtractorEnabled);
    }

    // Getters & Setters
    public Path getJobFilesSchedulingRootPath() {
        return jobFilesSchedulingRootPath;
    }

    public void setJobFilesSchedulingRootPath(Path jobFilesSchedulingRootPath) throws FileNotFoundException {
        if (jobFilesSchedulingRootPath == Paths.get("")) {
            this.jobFilesSchedulingRootPath = null;
            return;
        }
        if (this.jobFilesSchedulingRootPath != null && this.jobFilesSchedulingRootPath.equals(jobFilesSchedulingRootPath)) {
            return;
        }
        File f = jobFilesSchedulingRootPath.toFile();
        if (!f.exists() || !f.isDirectory()) {
            LOG.debug("The folder did not exist");
            throw new FileNotFoundException("Folder not found");
        }
        this.jobFilesSchedulingRootPath = jobFilesSchedulingRootPath;
    }

    public Path getJobFoldersDetailingRootPath() {
        return jobFoldersDetailingRootPath;
    }

    public void setJobFoldersDetailingRootPath(Path jobFoldersDetailingRootPath) throws FileNotFoundException {
        if (jobFoldersDetailingRootPath == null) {
            this.jobFoldersDetailingRootPath = Paths.get("");
            return;
        }
        if (this.jobFoldersDetailingRootPath != null && this.jobFoldersDetailingRootPath.equals(jobFoldersDetailingRootPath)) {
            return;
        }
        File f = jobFoldersDetailingRootPath.toFile();
        if (!f.exists() || !f.isDirectory()) {
            LOG.debug("The folder did not exist");
            throw new FileNotFoundException("Folder not found");
        }
        this.jobFoldersDetailingRootPath = jobFoldersDetailingRootPath;
    }

    public Boolean isBuiltInTrussExtractorEnabled() {
        return builtInTrussExtractorEnabled;
    }

    public void setBuiltInTrussExtractorEnabled(Boolean builtInTrussExtractorEnabled) {
        if (builtInTrussExtractorEnabled == null) {
            return;
        }
        if (this.builtInTrussExtractorEnabled != builtInTrussExtractorEnabled) {
            this.builtInTrussExtractorEnabled = builtInTrussExtractorEnabled;
        }
    }

    public Boolean isBuiltInBeamExtractorEnabled() {
        return builtInBeamExtractorEnabled;
    }

    public void setBuiltInBeamExtractorEnabled(Boolean builtInBeamExtractorEnabled) {
        if (builtInBeamExtractorEnabled == null) {
            return;
        }
        if (this.builtInBeamExtractorEnabled != builtInBeamExtractorEnabled) {
            this.builtInBeamExtractorEnabled = builtInBeamExtractorEnabled;
        }
    }

    public Boolean isBuiltInSheetExtractorEnabled() {
        return builtInSheetExtractorEnabled;
    }

    public void setBuiltInSheetExtractorEnabled(Boolean builtInSheetExtractorEnabled) {
        if (builtInSheetExtractorEnabled == null) {
            return;
        }
        if (this.builtInSheetExtractorEnabled != builtInSheetExtractorEnabled) {
            this.builtInSheetExtractorEnabled = builtInSheetExtractorEnabled;
        }
    }

    public Boolean isBuiltInSlabExtractorEnabled() {
        return builtInSlabExtractorEnabled;
    }

    public void setBuiltInSlabExtractorEnabled(Boolean builtInSlabExtractorEnabled) {
        if (builtInSlabExtractorEnabled == null) {
            return;
        }
        if (this.builtInSlabExtractorEnabled != builtInSlabExtractorEnabled) {
            this.builtInSlabExtractorEnabled = builtInSlabExtractorEnabled;
        }
    }

    public String getTrussScheduleSectionName() {
        return trussScheduleSectionName;
    }

    public void setTrussScheduleSectionName(String trussScheduleSectionName) {
        if (trussScheduleSectionName == null) {
            return;
        }
        this.trussScheduleSectionName = trussScheduleSectionName;
    }

    public String getBeamScheduleSectionName() {
        return beamScheduleSectionName;
    }

    public void setBeamScheduleSectionName(String beamScheduleSectionName) {
        if (beamScheduleSectionName == null) {
            return;
        }
        this.beamScheduleSectionName = beamScheduleSectionName;
    }

    public String getSheetScheduleSectionName() {
        return sheetScheduleSectionName;
    }

    public void setSheetScheduleSectionName(String sheetScheduleSectionName) {
        if (sheetScheduleSectionName == null) {
            return;
        }
        this.sheetScheduleSectionName = sheetScheduleSectionName;
    }

    public String getSlabScheduleSectionName() {
        return slabScheduleSectionName;
    }

    public void setSlabScheduleSectionName(String slabScheduleSectionName) {
        if (slabScheduleSectionName == null) {
            return;
        }
        this.slabScheduleSectionName = slabScheduleSectionName;
    }

    public String getExcelScheduleSheetName() {
        return excelScheduleSheetName;
    }

    public void setExcelScheduleSheetName(String excelScheduleSheetName) {
        if (excelScheduleSheetName == null) {
            return;
        }
        this.excelScheduleSheetName = excelScheduleSheetName;
    }

    public Integer getExcelScheduleDataNameCol() {
        return excelScheduleDataNameCol;
    }

    public void setExcelScheduleDataNameCol(Integer excelScheduleDataNameCol) {
        if (excelScheduleDataNameCol == null){
            return;
        }
        this.excelScheduleDataNameCol = excelScheduleDataNameCol;
    }

    public Integer getExcelScheduleDataValueCol() {
        return excelScheduleDataValueCol;
    }

    public void setExcelScheduleDataValueCol(Integer excelScheduleDataValueCol) {
        if (excelScheduleDataValueCol == null) {
            return;
        }
        this.excelScheduleDataValueCol = excelScheduleDataValueCol;
    }

    public Integer getExcelScheduleDataUnitCol() {
        return excelScheduleDataUnitCol;
    }

    public void setExcelScheduleDataUnitCol(Integer excelScheduleDataUnitCol) {
        if (excelScheduleDataUnitCol == null) {
            return;
        }
        this.excelScheduleDataUnitCol = excelScheduleDataUnitCol;
    }

    public Integer getExcelScheduleDataRateCol() {
        return excelScheduleDataRateCol;
    }

    public void setExcelScheduleDataRateCol(Integer excelScheduleDataRateCol) {
        if (excelScheduleDataRateCol == null) {
            return;
        }
        this.excelScheduleDataRateCol = excelScheduleDataRateCol;
    }

    public Integer getExcelScheduleDataTotalCol() {
        return excelScheduleDataTotalCol;
    }

    public void setExcelScheduleDataTotalCol(Integer excelScheduleDataTotalCol) {
        if (excelScheduleDataTotalCol == null) {
            return;
        }
        this.excelScheduleDataTotalCol = excelScheduleDataTotalCol;
    }

    public Integer getExcelScheduleDataCommentCol() {
        return excelScheduleDataCommentCol;
    }

    public void setExcelScheduleDataCommentCol(Integer excelScheduleDataCommentCol) {
        if (excelScheduleDataCommentCol == null) {
            return;
        }
        this.excelScheduleDataCommentCol = excelScheduleDataCommentCol;
    }

    public List<String> getExcelScheduleFileSections() {
        return excelScheduleFileSections;
    }

    public void addExcelScheduleFileSections(String section) {
        if (section == null) {
            return;
        }
        if (!this.excelScheduleFileSections.contains(section.toUpperCase())) {
            this.excelScheduleFileSections.add(section.toUpperCase());
        }
    }

    public void setExcelScheduleFileSections(List<String> excelScheduleFileSections) {
        if (excelScheduleFileSections != null) {
            List<String> upperCaseList = excelScheduleFileSections.stream().map(m -> m.toUpperCase()).collect(Collectors.toList());
            this.excelScheduleFileSections = upperCaseList;
        } else {
            this.excelScheduleFileSections = new ArrayList();
        }
    }

    public String getScheduleEntryCw260Truss() {
        return scheduleEntryCw260Truss;
    }

    public void setScheduleEntryCw260Truss(String scheduleEntryCw260Truss) {
        if (scheduleEntryCw260Truss == null) {
            return;
        }
        this.scheduleEntryCw260Truss = scheduleEntryCw260Truss;
    }

    public String getScheduleEntryCw346Truss() {
        return scheduleEntryCw346Truss;
    }

    public void setScheduleEntryCw346Truss(String scheduleEntryCw346Truss) {
        if (scheduleEntryCw346Truss == null) {
            return;
        }
        this.scheduleEntryCw346Truss = scheduleEntryCw346Truss;
    }

    public String getScheduleEntryHj200Truss() {
        return scheduleEntryHj200Truss;
    }

    public void setScheduleEntryHj200Truss(String scheduleEntryHj200Truss) {
        if (scheduleEntryHj200Truss == null) {
            return;
        }
        this.scheduleEntryHj200Truss = scheduleEntryHj200Truss;
    }

    public String getScheduleEntryHj300Truss() {
        return scheduleEntryHj300Truss;
    }

    public void setScheduleEntryHj300Truss(String scheduleEntryHj300Truss) {
        if (scheduleEntryHj300Truss == null) {
            return;
        }
        this.scheduleEntryHj300Truss = scheduleEntryHj300Truss;
    }

    public String getScheduleEntryStandardEndcapsCw260() {
        return scheduleEntryStandardEndcapsCw260;
    }

    public void setScheduleEntryStandardEndcapsCw260(String scheduleEntryStandardEndcapsCw260) {
        if (scheduleEntryStandardEndcapsCw260 == null) {
            return;
        }
        this.scheduleEntryStandardEndcapsCw260 = scheduleEntryStandardEndcapsCw260;
    }

    public String getScheduleEntryStandardEndcapsCw346() {
        return scheduleEntryStandardEndcapsCw346;
    }

    public void setScheduleEntryStandardEndcapsCw346(String scheduleEntryStandardEndcapsCw346) {
        if (scheduleEntryStandardEndcapsCw346 == null) {
            return;
        }
        this.scheduleEntryStandardEndcapsCw346 = scheduleEntryStandardEndcapsCw346;
    }

    public String getScheduleEntryStandardEndcapsHj200() {
        return scheduleEntryStandardEndcapsHj200;
    }

    public void setScheduleEntryStandardEndcapsHj200(String scheduleEntryStandardEndcapsHj200) {
        if (scheduleEntryStandardEndcapsHj200 == null) {
            return;
        }
        this.scheduleEntryStandardEndcapsHj200 = scheduleEntryStandardEndcapsHj200;
    }

    public String getScheduleEntryStandardEndcapsHj300() {
        return scheduleEntryStandardEndcapsHj300;
    }

    public void setScheduleEntryStandardEndcapsHj300(String scheduleEntryStandardEndcapsHj300) {
        if (scheduleEntryStandardEndcapsHj300 == null) {
            return;
        }
        this.scheduleEntryStandardEndcapsHj300 = scheduleEntryStandardEndcapsHj300;
    }

    public String getScheduleEntryConnectionEndcapsCw260() {
        return scheduleEntryConnectionEndcapsCw260;
    }

    public void setScheduleEntryConnectionEndcapsCw260(String scheduleEntryConnectionEndcapsCw260) {
        if (scheduleEntryConnectionEndcapsCw260 == null) {
            return;
        }
        this.scheduleEntryConnectionEndcapsCw260 = scheduleEntryConnectionEndcapsCw260;
    }

    public String getScheduleEntryConnectionEndcapsCw346() {
        return scheduleEntryConnectionEndcapsCw346;
    }

    public void setScheduleEntryConnectionEndcapsCw346(String scheduleEntryConnectionEndcapsCw346) {
        if (scheduleEntryConnectionEndcapsCw346 == null) {
            return;
        }
        this.scheduleEntryConnectionEndcapsCw346 = scheduleEntryConnectionEndcapsCw346;
    }

    public String getScheduleEntryConnectionEndcapsHj200() {
        return scheduleEntryConnectionEndcapsHj200;
    }

    public void setScheduleEntryConnectionEndcapsHj200(String scheduleEntryConnectionEndcapsHj200) {
        if (scheduleEntryConnectionEndcapsHj200 == null) {
            return;
        }
        this.scheduleEntryConnectionEndcapsHj200 = scheduleEntryConnectionEndcapsHj200;
    }

    public String getScheduleEntryConnectionEndcapsHj300() {
        return scheduleEntryConnectionEndcapsHj300;
    }

    public void setScheduleEntryConnectionEndcapsHj300(String scheduleEntryConnectionEndcapsHj300) {
        if (scheduleEntryConnectionEndcapsHj300 == null) {
            return;
        }
        this.scheduleEntryConnectionEndcapsHj300 = scheduleEntryConnectionEndcapsHj300;
    }

    public String getScheduleEntryTrussAirConPenoCw260() {
        return scheduleEntryTrussAirConPenoCw260;
    }

    public void setScheduleEntryTrussAirConPenoCw260(String scheduleEntryTrussAirConPenoCw260) {
        if (scheduleEntryTrussAirConPenoCw260 == null) {
            return;
        }
        this.scheduleEntryTrussAirConPenoCw260 = scheduleEntryTrussAirConPenoCw260;
    }

    public String getScheduleEntryTrussAirConPenoCw346() {
        return scheduleEntryTrussAirConPenoCw346;
    }

    public void setScheduleEntryTrussAirConPenoCw346(String scheduleEntryTrussAirConPenoCw346) {
        if (scheduleEntryTrussAirConPenoCw346 == null) {
            return;
        }
        this.scheduleEntryTrussAirConPenoCw346 = scheduleEntryTrussAirConPenoCw346;
    }

    public String getScheduleEntryTrussAirConPenoHj300() {
        return scheduleEntryTrussAirConPenoHj300;
    }

    public void setScheduleEntryTrussAirConPenoHj300(String scheduleEntryTrussAirConPenoHj300) {
        if (scheduleEntryTrussAirConPenoHj300 == null) {
            return;
        }
        this.scheduleEntryTrussAirConPenoHj300 = scheduleEntryTrussAirConPenoHj300;
    }

    public String getScheduleEntrySteelBlackKeyword() {
        return scheduleEntrySteelBlackKeyword;
    }

    public void setScheduleEntrySteelBlackKeyword(String scheduleEntrySteelBlackKeyword) {
        if (scheduleEntrySteelBlackKeyword == null) {
            return;
        }
        this.scheduleEntrySteelBlackKeyword = scheduleEntrySteelBlackKeyword;
    }

    public String getScheduleEntrySteelGalvanisedKeyword() {
        return scheduleEntrySteelGalvanisedKeyword;
    }

    public void setScheduleEntrySteelGalvanisedKeyword(String scheduleEntrySteelGalvanisedKeyword) {
        if (scheduleEntrySteelGalvanisedKeyword == null) {
            return;
        }
        this.scheduleEntrySteelGalvanisedKeyword = scheduleEntrySteelGalvanisedKeyword;
    }

    public String getScheduleEntrySteelDimetKeyword() {
        return scheduleEntrySteelDimetKeyword;
    }

    public void setScheduleEntrySteelDimetKeyword(String scheduleEntrySteelDimetKeyword) {
        if (scheduleEntrySteelDimetKeyword == null) {
            return;
        }
        this.scheduleEntrySteelDimetKeyword = scheduleEntrySteelDimetKeyword;
    }

    public String getScheduleEntrySteelEpoxyKeyword() {
        return scheduleEntrySteelEpoxyKeyword;
    }

    public void setScheduleEntrySteelEpoxyKeyword(String scheduleEntrySteelEpoxyKeyword) {
        if (scheduleEntrySteelEpoxyKeyword == null) {
            return;
        }
        this.scheduleEntrySteelEpoxyKeyword = scheduleEntrySteelEpoxyKeyword;
    }

    public String getScheduleEntrySteelDuragalKeyword() {
        return scheduleEntrySteelDuragalKeyword;
    }

    public void setScheduleEntrySteelDuragalKeyword(String scheduleEntrySteelDuragalKeyword) {
        if (scheduleEntrySteelDuragalKeyword == null) {
            return;
        }
        this.scheduleEntrySteelDuragalKeyword = scheduleEntrySteelDuragalKeyword;
    }

    public String getScheduleEntrySlabInternal() {
        return scheduleEntrySlabInternal;
    }

    public void setScheduleEntrySlabInternal(String scheduleEntrySlabInternal) {
        if (scheduleEntrySlabInternal == null) {
            return;
        }
        this.scheduleEntrySlabInternal = scheduleEntrySlabInternal;
    }

    public String getScheduleEntrySlab2cRhs() {
        return scheduleEntrySlab2cRhs;
    }

    public void setScheduleEntrySlab2cRhs(String scheduleEntrySlab2cRhs) {
        if (scheduleEntrySlab2cRhs == null) {
            return;
        }
        this.scheduleEntrySlab2cRhs = scheduleEntrySlab2cRhs;
    }

    public String getScheduleEntrySlab3cRhs() {
        return scheduleEntrySlab3cRhs;
    }

    public void setScheduleEntrySlab3cRhs(String scheduleEntrySlab3cRhs) {
        if (scheduleEntrySlab3cRhs == null) {
            return;
        }
        this.scheduleEntrySlab3cRhs = scheduleEntrySlab3cRhs;
    }

    public String getScheduleEntrySlab2cInsitu() {
        return scheduleEntrySlab2cInsitu;
    }

    public void setScheduleEntrySlab2cInsitu(String scheduleEntrySlab2cInsitu) {
        if (scheduleEntrySlab2cInsitu == null) {
            return;
        }
        this.scheduleEntrySlab2cInsitu = scheduleEntrySlab2cInsitu;
    }

    public String getScheduleEntrySlab3cInsitu() {
        return scheduleEntrySlab3cInsitu;
    }

    public void setScheduleEntrySlab3cInsitu(String scheduleEntrySlab3cInsitu) {
        if (scheduleEntrySlab3cInsitu == null) {
            return;
        }
        this.scheduleEntrySlab3cInsitu = scheduleEntrySlab3cInsitu;
    }

    public String getScheduleEntrySlab4cInsitu() {
        return scheduleEntrySlab4cInsitu;
    }

    public void setScheduleEntrySlab4cInsitu(String scheduleEntrySlab4cInsitu) {
        if (scheduleEntrySlab4cInsitu == null) {
            return;
        }
        this.scheduleEntrySlab4cInsitu = scheduleEntrySlab4cInsitu;
    }

    public String getScheduleEntrySlabThickAngle() {
        return scheduleEntrySlabThickAngle;
    }

    public void setScheduleEntrySlabThickAngle(String scheduleEntrySlabThickAngle) {
        if (scheduleEntrySlabThickAngle == null) {
            return;
        }
        this.scheduleEntrySlabThickAngle = scheduleEntrySlabThickAngle;
    }

    public String getScheduleEntrySlabThinAngle() {
        return scheduleEntrySlabThinAngle;
    }

    public void setScheduleEntrySlabThinAngle(String scheduleEntrySlabThinAngle) {
        if (scheduleEntrySlabThinAngle == null) {
            return;
        }
        this.scheduleEntrySlabThinAngle = scheduleEntrySlabThinAngle;
    }
}
