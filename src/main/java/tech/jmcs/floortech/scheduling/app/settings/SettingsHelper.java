package tech.jmcs.floortech.scheduling.app.settings;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SettingsHelper {

    public static Properties settingsToPropertiesObject(SettingsHolder settingsHolder) {
        Properties properties = new Properties();

        Path jobFilesSchedulingRootPath = settingsHolder.getJobFilesSchedulingRootPath();
        Path jobFoldersDetailingRootPath = settingsHolder.getJobFoldersDetailingRootPath();

        Boolean builtInBeamExtractorEnabled = settingsHolder.isBuiltInBeamExtractorEnabled();
        String builtInBeamFileName = settingsHolder.getBeamDataFileName();
        String builtInBeamFileExt = settingsHolder.getBeamFileExtension();
        String beamScheduleSectionName = settingsHolder.getBeamScheduleSectionName();

        Boolean builtInSlabExtractorEnabled = settingsHolder.isBuiltInSlabExtractorEnabled();
        String builtInSlabFileName = settingsHolder.getSlabDataFileName();
        String builtInSlabFileExt = settingsHolder.getSlabFileExtension();
        String slabScheduleSectionName = settingsHolder.getSlabScheduleSectionName();

        Boolean builtInSheetExtractorEnabled = settingsHolder.isBuiltInSheetExtractorEnabled();
        String builtInSheetFileName = settingsHolder.getSheetDataFileName();
        String builtInSheetFileExt = settingsHolder.getSheetFileExtension();
        String sheetScheduleSectionName = settingsHolder.getSheetScheduleSectionName();

        Boolean builtInTrussExtractorEnabled = settingsHolder.isBuiltInTrussExtractorEnabled();
        String builtInTrussFileName = settingsHolder.getTrussDataFileName();
        String builtInTrussFileExt = settingsHolder.getTrussFileExtension();
        String trussScheduleSectionName = settingsHolder.getTrussScheduleSectionName();

        String scheduleSheetName = settingsHolder.getExcelScheduleSheetName();
        List<String> scheduleSections = settingsHolder.getExcelScheduleFileSections();
        Integer dataNameCol = settingsHolder.getExcelScheduleDataNameCol();
        Integer dataValueCol = settingsHolder.getExcelScheduleDataValueCol();
        Integer dataUnitCol = settingsHolder.getExcelScheduleDataUnitCol();
        Integer dataRateCol = settingsHolder.getExcelScheduleDataRateCol();
        Integer dataTotalCol = settingsHolder.getExcelScheduleDataTotalCol();
        Integer dataCommentCol = settingsHolder.getExcelScheduleDataCommentCol();

        String scheduleEntryCw260Truss = settingsHolder.getScheduleEntryCw260Truss();
        String scheduleEntryCw346Truss = settingsHolder.getScheduleEntryCw346Truss();
        String scheduleEntryHj200Truss = settingsHolder.getScheduleEntryHj200Truss();
        String scheduleEntryHj300Truss = settingsHolder.getScheduleEntryHj300Truss();
        String scheduleEntryConnectionEndcapsCw260 = settingsHolder.getScheduleEntryConnectionEndcapsCw260();
        String scheduleEntryConnectionEndcapsCw346 = settingsHolder.getScheduleEntryConnectionEndcapsCw346();
        String scheduleEntryConnectionEndcapsHj200 = settingsHolder.getScheduleEntryConnectionEndcapsHj200();
        String scheduleEntryConnectionEndcapsHj300 = settingsHolder.getScheduleEntryConnectionEndcapsHj300();
        String scheduleEntryStandardEndcapsCw260 = settingsHolder.getScheduleEntryStandardEndcapsCw260();
        String scheduleEntryStandardEndcapsCw346 = settingsHolder.getScheduleEntryStandardEndcapsCw346();
        String scheduleEntryStandardEndcapsHj200 = settingsHolder.getScheduleEntryStandardEndcapsHj200();
        String scheduleEntryStandardEndcapsHj300 = settingsHolder.getScheduleEntryStandardEndcapsHj300();
        String scheduleEntryTrussAirConPenoCw260 = settingsHolder.getScheduleEntryTrussAirConPenoCw260();
        String scheduleEntryTrussAirConPenoCw346 = settingsHolder.getScheduleEntryTrussAirConPenoCw346();
        String scheduleEntryTrussAirConPenoHj300 = settingsHolder.getScheduleEntryTrussAirConPenoHj300();
        String scheduleEntrySteelBlackKeyword = settingsHolder.getScheduleEntrySteelBlackKeyword();
        String scheduleEntrySteelGalvanisedKeyword = settingsHolder.getScheduleEntrySteelGalvanisedKeyword();
        String scheduleEntrySteelDimetKeyword = settingsHolder.getScheduleEntrySteelDimetKeyword();
        String scheduleEntrySteelEpoxyKeyword = settingsHolder.getScheduleEntrySteelEpoxyKeyword();
        String scheduleEntrySteelDuragalKeyword = settingsHolder.getScheduleEntrySteelDuragalKeyword();
        String scheduleEntrySlabInternal = settingsHolder.getScheduleEntrySlabInternal();
        String scheduleEntrySlab2cRhs = settingsHolder.getScheduleEntrySlab2cRhs();
        String scheduleEntrySlab3cRhs = settingsHolder.getScheduleEntrySlab3cRhs();
        String scheduleEntrySlab2cInsitu = settingsHolder.getScheduleEntrySlab2cInsitu();
        String scheduleEntrySlab3cInsitu = settingsHolder.getScheduleEntrySlab3cInsitu();
        String scheduleEntrySlab4cInsitu = settingsHolder.getScheduleEntrySlab4cInsitu();
        String scheduleEntrySlabThickAngle = settingsHolder.getScheduleEntrySlabThickAngle();
        String scheduleEntrySlabThinAngle = settingsHolder.getScheduleEntrySlabThinAngle();

        properties.setProperty(SettingsName.JOB_FILES_SCHEDULING_PATH.getSettingsFilePropertyName(), jobFilesSchedulingRootPath == null ? "" : jobFilesSchedulingRootPath.toString());
        properties.setProperty(SettingsName.JOB_FOLDERS_DETAILING_PATH.getSettingsFilePropertyName(), jobFoldersDetailingRootPath == null ? "" : jobFoldersDetailingRootPath.toString());

        properties.setProperty(SettingsName.BUILT_IN_BEAM_EXTRACTOR_ENABLED.getSettingsFilePropertyName(), builtInBeamExtractorEnabled == null ? "true" : builtInBeamExtractorEnabled.toString());
        properties.setProperty(SettingsName.BUILT_IN_BEAM_FILE_NAME_PATTERN.getSettingsFilePropertyName(), builtInBeamFileName == null ? "" : builtInBeamFileName);
        properties.setProperty(SettingsName.BUILT_IN_BEAM_FILE_EXT.getSettingsFilePropertyName(), builtInBeamFileExt == null ? "" : builtInBeamFileExt);
        properties.setProperty(SettingsName.BEAM_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName(), beamScheduleSectionName == null ? "" : beamScheduleSectionName);

        properties.setProperty(SettingsName.BUILT_IN_SLAB_EXTRACTOR_ENABLED.getSettingsFilePropertyName(), builtInSlabExtractorEnabled == null ? "true" : builtInSlabExtractorEnabled.toString());
        properties.setProperty(SettingsName.BUILT_IN_SLAB_FILE_NAME_PATTERN.getSettingsFilePropertyName(), builtInSlabFileName == null ? "" : builtInSlabFileName);
        properties.setProperty(SettingsName.BUILT_IN_SLAB_FILE_EXT.getSettingsFilePropertyName(), builtInSlabFileExt == null ? "" : builtInSlabFileExt);
        properties.setProperty(SettingsName.SLAB_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName(), slabScheduleSectionName == null ? "" : slabScheduleSectionName);

        properties.setProperty(SettingsName.BUILT_IN_SHEET_EXTRACTOR_ENABLED.getSettingsFilePropertyName(), builtInSheetExtractorEnabled == null ? "true" : builtInSheetExtractorEnabled.toString());
        properties.setProperty(SettingsName.BUILT_IN_SHEET_FILE_NAME_PATTERN.getSettingsFilePropertyName(), builtInSheetFileName == null ? "" : builtInSheetFileName);
        properties.setProperty(SettingsName.BUILT_IN_SHEET_FILE_EXT.getSettingsFilePropertyName(), builtInSheetFileExt == null ? "" : builtInSheetFileExt);
        properties.setProperty(SettingsName.SHEET_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName(), sheetScheduleSectionName == null ? "" : sheetScheduleSectionName);

        properties.setProperty(SettingsName.BUILT_IN_TRUSS_EXTRACTOR_ENABLED.getSettingsFilePropertyName(), builtInTrussExtractorEnabled == null ? "true" : builtInTrussExtractorEnabled.toString());
        properties.setProperty(SettingsName.BUILT_IN_TRUSS_FILE_NAME_PATTERN.getSettingsFilePropertyName(), builtInTrussFileName == null ? "" : builtInTrussFileName);
        properties.setProperty(SettingsName.BUILT_IN_TRUSS_FILE_EXT.getSettingsFilePropertyName(), builtInTrussFileExt == null ? "" : builtInTrussFileExt);
        properties.setProperty(SettingsName.TRUSS_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName(), trussScheduleSectionName == null ? "" : trussScheduleSectionName);

        properties.setProperty(SettingsName.EXCEL_SCHEDULE_SHEET_NAME.getSettingsFilePropertyName(), scheduleSheetName == null ? "" : scheduleSheetName);
        properties.setProperty(SettingsName.EXCEL_SCHEDULE_SECTION_NAMES.getSettingsFilePropertyName(), scheduleSections == null || scheduleSections.isEmpty() ? "" : String.join(";", scheduleSections));
        properties.setProperty(SettingsName.EXCEL_SCHEDULE_DATA_NAME_COL.getSettingsFilePropertyName(), String.valueOf(dataNameCol == null ? 1 : dataNameCol));
        properties.setProperty(SettingsName.EXCEL_SCHEDULE_DATA_VALUE_COL.getSettingsFilePropertyName(), String.valueOf(dataValueCol == null ? 2 : dataValueCol));
        properties.setProperty(SettingsName.EXCEL_SCHEDULE_DATA_UNIT_COL.getSettingsFilePropertyName(), String.valueOf(dataUnitCol == null ? 3 : dataUnitCol));
        properties.setProperty(SettingsName.EXCEL_SCHEDULE_DATA_RATE_COL.getSettingsFilePropertyName(), String.valueOf(dataRateCol == null ? 4 : dataRateCol));
        properties.setProperty(SettingsName.EXCEL_SCHEDULE_DATA_TOTAL_COL.getSettingsFilePropertyName(), String.valueOf(dataTotalCol == null ? 5 : dataTotalCol));
        properties.setProperty(SettingsName.EXCEL_SCHEDULE_DATA_COMMENT_COL.getSettingsFilePropertyName(), String.valueOf(dataCommentCol == null ? 6 : dataCommentCol));

        properties.setProperty(SettingsName.SCHEDULE_ENTRY_CW260_TRUSS.getSettingsFilePropertyName(), scheduleEntryCw260Truss == null ? "" : scheduleEntryCw260Truss);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_CW346_TRUSS.getSettingsFilePropertyName(), scheduleEntryCw346Truss == null ? "" : scheduleEntryCw346Truss);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_HJ200_TRUSS.getSettingsFilePropertyName(), scheduleEntryHj200Truss == null ? "" : scheduleEntryHj200Truss);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_HJ300_TRUSS.getSettingsFilePropertyName(), scheduleEntryHj300Truss == null ? "" : scheduleEntryHj300Truss);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_CW260.getSettingsFilePropertyName(), scheduleEntryConnectionEndcapsCw260 == null ? "" : scheduleEntryConnectionEndcapsCw260);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_CW346.getSettingsFilePropertyName(), scheduleEntryConnectionEndcapsCw346 == null ? "" : scheduleEntryConnectionEndcapsCw346);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_HJ200.getSettingsFilePropertyName(), scheduleEntryConnectionEndcapsHj200 == null ? "" : scheduleEntryConnectionEndcapsHj200);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_HJ300.getSettingsFilePropertyName(), scheduleEntryConnectionEndcapsHj300 == null ? "" : scheduleEntryConnectionEndcapsHj300);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_CW260.getSettingsFilePropertyName(), scheduleEntryStandardEndcapsCw260 == null ? "" : scheduleEntryStandardEndcapsCw260);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_CW346.getSettingsFilePropertyName(), scheduleEntryStandardEndcapsCw346 == null ? "" : scheduleEntryStandardEndcapsCw346);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_HJ200.getSettingsFilePropertyName(), scheduleEntryStandardEndcapsHj200 == null ? "" : scheduleEntryStandardEndcapsHj200);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_HJ300.getSettingsFilePropertyName(), scheduleEntryStandardEndcapsHj300 == null ? "" : scheduleEntryStandardEndcapsHj300);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_CW260.getSettingsFilePropertyName(), scheduleEntryTrussAirConPenoCw260 == null ? "" : scheduleEntryTrussAirConPenoCw260);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_CW346.getSettingsFilePropertyName(), scheduleEntryTrussAirConPenoCw346 == null ? "" : scheduleEntryTrussAirConPenoCw346);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_HJ300.getSettingsFilePropertyName(), scheduleEntryTrussAirConPenoHj300 == null ? "" : scheduleEntryTrussAirConPenoHj300);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STEEL_BLACK_KEYWORD.getSettingsFilePropertyName(), scheduleEntrySteelBlackKeyword == null ? "" : scheduleEntrySteelBlackKeyword);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STEEL_GALVANISED_KEYWORD.getSettingsFilePropertyName(), scheduleEntrySteelGalvanisedKeyword == null ? "" : scheduleEntrySteelGalvanisedKeyword);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STEEL_DIMET_KEYWORD.getSettingsFilePropertyName(), scheduleEntrySteelDimetKeyword == null ? "" : scheduleEntrySteelDimetKeyword);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STEEL_EPOXY_KEYWORD.getSettingsFilePropertyName(), scheduleEntrySteelEpoxyKeyword == null ? "" : scheduleEntrySteelEpoxyKeyword);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_STEEL_DURAGAL_KEYWORD.getSettingsFilePropertyName(), scheduleEntrySteelDuragalKeyword == null ? "" : scheduleEntrySteelDuragalKeyword);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_INTERNAL.getSettingsFilePropertyName(), scheduleEntrySlabInternal == null ? "" : scheduleEntrySlabInternal);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_2C_RHS.getSettingsFilePropertyName(), scheduleEntrySlab2cRhs == null ? "" : scheduleEntrySlab2cRhs);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_3C_RHS.getSettingsFilePropertyName(), scheduleEntrySlab3cRhs == null ? "" : scheduleEntrySlab3cRhs);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_2C_INSITU.getSettingsFilePropertyName(), scheduleEntrySlab2cInsitu == null ? "" : scheduleEntrySlab2cInsitu);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_3C_INSITU.getSettingsFilePropertyName(), scheduleEntrySlab3cInsitu == null ? "" : scheduleEntrySlab3cInsitu);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_4C_INSITU.getSettingsFilePropertyName(), scheduleEntrySlab4cInsitu == null ? "" : scheduleEntrySlab4cInsitu);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_THICK_ANGLE.getSettingsFilePropertyName(), scheduleEntrySlabThickAngle == null ? "" : scheduleEntrySlabThickAngle);
        properties.setProperty(SettingsName.SCHEDULE_ENTRY_SLAB_THIN_ANGLE.getSettingsFilePropertyName(), scheduleEntrySlabThinAngle == null ? "" : scheduleEntrySlabThinAngle);

        return properties;
    }

    public static void propertiesObjectToSettings(Properties properties, SettingsHolder settingsHolder) {
        // get from properties
        Path jobFilesSchedulingPath = Paths.get(properties.getProperty(SettingsName.JOB_FILES_SCHEDULING_PATH.getSettingsFilePropertyName()));
        Path jobFoldersDetailingPath = Paths.get(properties.getProperty(SettingsName.JOB_FOLDERS_DETAILING_PATH.getSettingsFilePropertyName()));

        Boolean builtInBeamExtractorEnabled = Boolean.parseBoolean(properties.getProperty(SettingsName.BUILT_IN_BEAM_EXTRACTOR_ENABLED.getSettingsFilePropertyName()));
        String builtInBeamFileNamePattern = properties.getProperty(SettingsName.BUILT_IN_BEAM_FILE_NAME_PATTERN.getSettingsFilePropertyName());
        String builtInBeamFileExt = properties.getProperty(SettingsName.BUILT_IN_BEAM_FILE_EXT.getSettingsFilePropertyName());
        String beamScheduleSectionName = properties.getProperty(SettingsName.BEAM_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName());

        Boolean builtInSheetExtractorEnabled = Boolean.parseBoolean(properties.getProperty(SettingsName.BUILT_IN_SHEET_EXTRACTOR_ENABLED.getSettingsFilePropertyName()));
        String builtInSheetFileNamePattern = properties.getProperty(SettingsName.BUILT_IN_SHEET_FILE_NAME_PATTERN.getSettingsFilePropertyName());
        String builtInSheetFileExt = properties.getProperty(SettingsName.BUILT_IN_SHEET_FILE_EXT.getSettingsFilePropertyName());
        String sheetScheduleSectionName = properties.getProperty(SettingsName.SHEET_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName());

        Boolean builtInSlabExtractorEnabled = Boolean.parseBoolean(properties.getProperty(SettingsName.BUILT_IN_SLAB_EXTRACTOR_ENABLED.getSettingsFilePropertyName()));
        String builtInSlabFileNamePattern = properties.getProperty(SettingsName.BUILT_IN_SLAB_FILE_NAME_PATTERN.getSettingsFilePropertyName());
        String builtInSlabFileExt = properties.getProperty(SettingsName.BUILT_IN_SLAB_FILE_EXT.getSettingsFilePropertyName());
        String slabScheduleSectionName = properties.getProperty(SettingsName.SLAB_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName());

        Boolean builtInTrussExtractorEnabled = Boolean.parseBoolean(properties.getProperty(SettingsName.BUILT_IN_TRUSS_EXTRACTOR_ENABLED.getSettingsFilePropertyName()));
        String builtInTrussFileNamePattern = properties.getProperty(SettingsName.BUILT_IN_TRUSS_FILE_NAME_PATTERN.getSettingsFilePropertyName());
        String builtInTrussFileExt = properties.getProperty(SettingsName.BUILT_IN_TRUSS_FILE_EXT.getSettingsFilePropertyName());
        String trussScheduleSectionName = properties.getProperty(SettingsName.TRUSS_SCHEDULE_SECTION_NAME.getSettingsFilePropertyName());

        String scheduleSheetName = properties.getProperty(SettingsName.EXCEL_SCHEDULE_SHEET_NAME.getSettingsFilePropertyName());

        String rawScheduleSections = properties.getProperty(SettingsName.EXCEL_SCHEDULE_SECTION_NAMES.getSettingsFilePropertyName());
        List<String> scheduleSections = new ArrayList<>();
        if (rawScheduleSections != null && !rawScheduleSections.isEmpty() && rawScheduleSections.contains(";")) {
            scheduleSections = Arrays.asList(rawScheduleSections.split(";"));
        }

        Integer dataNameCol = 0;
        Integer dataValueCol = 0;
        Integer dataUnitCol = 0;
        Integer dataRateCol = 0;
        Integer dataTotalCol = 0;
        Integer dataCommentCol = 0;
        try {
            dataNameCol = Integer.parseInt(properties.getProperty(SettingsName.EXCEL_SCHEDULE_DATA_NAME_COL.getSettingsFilePropertyName()));
            dataValueCol = Integer.parseInt(properties.getProperty(SettingsName.EXCEL_SCHEDULE_DATA_VALUE_COL.getSettingsFilePropertyName()));
            dataUnitCol = Integer.parseInt(properties.getProperty(SettingsName.EXCEL_SCHEDULE_DATA_UNIT_COL.getSettingsFilePropertyName()));
            dataRateCol = Integer.parseInt(properties.getProperty(SettingsName.EXCEL_SCHEDULE_DATA_RATE_COL.getSettingsFilePropertyName()));
            dataTotalCol = Integer.parseInt(properties.getProperty(SettingsName.EXCEL_SCHEDULE_DATA_TOTAL_COL.getSettingsFilePropertyName()));
            dataCommentCol = Integer.parseInt(properties.getProperty(SettingsName.EXCEL_SCHEDULE_DATA_COMMENT_COL.getSettingsFilePropertyName()));
        } catch (NumberFormatException nex) {
            // others following fail when one fails...
        }

        String scheduleEntryCw260Truss = properties.getProperty(SettingsName.SCHEDULE_ENTRY_CW260_TRUSS.getSettingsFilePropertyName());
        String scheduleEntryCw346Truss = properties.getProperty(SettingsName.SCHEDULE_ENTRY_CW346_TRUSS.getSettingsFilePropertyName());
        String scheduleEntryHj200Truss = properties.getProperty(SettingsName.SCHEDULE_ENTRY_HJ200_TRUSS.getSettingsFilePropertyName());
        String scheduleEntryHj300Truss = properties.getProperty(SettingsName.SCHEDULE_ENTRY_HJ300_TRUSS.getSettingsFilePropertyName());
        String scheduleEntryConnectionEndcapsCw260 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_CW260.getSettingsFilePropertyName());
        String scheduleEntryConnectionEndcapsCw346 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_CW346.getSettingsFilePropertyName());
        String scheduleEntryConnectionEndcapsHj200 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_HJ200.getSettingsFilePropertyName());
        String scheduleEntryConnectionEndcapsHj300 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_CONNECTION_EC_HJ300.getSettingsFilePropertyName());
        String scheduleEntryStandardEndcapsCw260 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_CW260.getSettingsFilePropertyName());
        String scheduleEntryStandardEndcapsCw346 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_CW346.getSettingsFilePropertyName());
        String scheduleEntryStandardEndcapsHj200 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_HJ200.getSettingsFilePropertyName());
        String scheduleEntryStandardEndcapsHj300 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STANDARD_EC_HJ300.getSettingsFilePropertyName());
        String scheduleEntryTrussAirConPenoCw260 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_CW260.getSettingsFilePropertyName());
        String scheduleEntryTrussAirConPenoCw346 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_CW346.getSettingsFilePropertyName());
        String scheduleEntryTrussAirConPenoHj300 = properties.getProperty(SettingsName.SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_HJ300.getSettingsFilePropertyName());
        String scheduleEntrySteelBlackKeyword = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STEEL_BLACK_KEYWORD.getSettingsFilePropertyName());
        String scheduleEntrySteelGalvanisedKeyword = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STEEL_GALVANISED_KEYWORD.getSettingsFilePropertyName());
        String scheduleEntrySteelDimetKeyword = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STEEL_DIMET_KEYWORD.getSettingsFilePropertyName());
        String scheduleEntrySteelEpoxyKeyword = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STEEL_EPOXY_KEYWORD.getSettingsFilePropertyName());
        String scheduleEntrySteelDuragalKeyword = properties.getProperty(SettingsName.SCHEDULE_ENTRY_STEEL_DURAGAL_KEYWORD.getSettingsFilePropertyName());
        String scheduleEntrySlabInternal = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_INTERNAL.getSettingsFilePropertyName());
        String scheduleEntrySlab2cRhs = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_2C_RHS.getSettingsFilePropertyName());
        String scheduleEntrySlab3cRhs = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_3C_RHS.getSettingsFilePropertyName());
        String scheduleEntrySlab2cInsitu = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_2C_INSITU.getSettingsFilePropertyName());
        String scheduleEntrySlab3cInsitu = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_3C_INSITU.getSettingsFilePropertyName());
        String scheduleEntrySlab4cInsitu = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_4C_INSITU.getSettingsFilePropertyName());
        String scheduleEntrySlabThickAngle = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_THICK_ANGLE.getSettingsFilePropertyName());
        String scheduleEntrySlabThinAngle = properties.getProperty(SettingsName.SCHEDULE_ENTRY_SLAB_THIN_ANGLE.getSettingsFilePropertyName());

        // set to settings holder
        settingsHolder.setBuiltInBeamExtractorEnabled(builtInBeamExtractorEnabled);
        settingsHolder.setBeamDataFileName(builtInBeamFileNamePattern);
        settingsHolder.setBeamFileExtension(builtInBeamFileExt);
        settingsHolder.setBeamScheduleSectionName(beamScheduleSectionName);

        settingsHolder.setBuiltInSheetExtractorEnabled(builtInSheetExtractorEnabled);
        settingsHolder.setSheetDataFileName(builtInSheetFileNamePattern);
        settingsHolder.setSheetFileExtension(builtInSheetFileExt);
        settingsHolder.setSheetScheduleSectionName(sheetScheduleSectionName);

        settingsHolder.setBuiltInSlabExtractorEnabled(builtInSlabExtractorEnabled);
        settingsHolder.setSlabDataFileName(builtInSlabFileNamePattern);
        settingsHolder.setSlabFileExtension(builtInSlabFileExt);
        settingsHolder.setSlabScheduleSectionName(slabScheduleSectionName);

        settingsHolder.setBuiltInTrussExtractorEnabled(builtInTrussExtractorEnabled);
        settingsHolder.setTrussDataFileName(builtInTrussFileNamePattern);
        settingsHolder.setTrussFileExtension(builtInTrussFileExt);
        settingsHolder.setTrussScheduleSectionName(trussScheduleSectionName);

        try {
            settingsHolder.setJobFilesSchedulingRootPath(jobFilesSchedulingPath);
        } catch (FileNotFoundException e) {
            // silently fail to set paths that dont exist
        }
        try {
            settingsHolder.setJobFoldersDetailingRootPath(jobFoldersDetailingPath);
        } catch (FileNotFoundException e) {
            // silently fail to set paths that dont exist
        }
        settingsHolder.setExcelScheduleFileSections(scheduleSections);
        settingsHolder.setExcelScheduleSheetName(scheduleSheetName);
        settingsHolder.setExcelScheduleDataNameCol(dataNameCol);
        settingsHolder.setExcelScheduleDataValueCol(dataValueCol);
        settingsHolder.setExcelScheduleDataUnitCol(dataUnitCol);
        settingsHolder.setExcelScheduleDataRateCol(dataRateCol);
        settingsHolder.setExcelScheduleDataTotalCol(dataTotalCol);
        settingsHolder.setExcelScheduleDataCommentCol(dataCommentCol);

        settingsHolder.setScheduleEntryCw260Truss(scheduleEntryCw260Truss);
        settingsHolder.setScheduleEntryCw346Truss(scheduleEntryCw346Truss);
        settingsHolder.setScheduleEntryHj200Truss(scheduleEntryHj200Truss);
        settingsHolder.setScheduleEntryHj300Truss(scheduleEntryHj300Truss);
        settingsHolder.setScheduleEntryConnectionEndcapsCw260(scheduleEntryConnectionEndcapsCw260);
        settingsHolder.setScheduleEntryConnectionEndcapsCw346(scheduleEntryConnectionEndcapsCw346);
        settingsHolder.setScheduleEntryConnectionEndcapsHj200(scheduleEntryConnectionEndcapsHj200);
        settingsHolder.setScheduleEntryConnectionEndcapsHj300(scheduleEntryConnectionEndcapsHj300);
        settingsHolder.setScheduleEntryStandardEndcapsCw260(scheduleEntryStandardEndcapsCw260);
        settingsHolder.setScheduleEntryStandardEndcapsCw346(scheduleEntryStandardEndcapsCw346);
        settingsHolder.setScheduleEntryStandardEndcapsHj200(scheduleEntryStandardEndcapsHj200);
        settingsHolder.setScheduleEntryStandardEndcapsHj300(scheduleEntryStandardEndcapsHj300);
        settingsHolder.setScheduleEntryTrussAirConPenoCw260(scheduleEntryTrussAirConPenoCw260);
        settingsHolder.setScheduleEntryTrussAirConPenoCw346(scheduleEntryTrussAirConPenoCw346);
        settingsHolder.setScheduleEntryTrussAirConPenoHj300(scheduleEntryTrussAirConPenoHj300);

        settingsHolder.setScheduleEntrySlabInternal(scheduleEntrySlabInternal);
        settingsHolder.setScheduleEntrySlab2cRhs(scheduleEntrySlab2cRhs);
        settingsHolder.setScheduleEntrySlab3cRhs(scheduleEntrySlab3cRhs);
        settingsHolder.setScheduleEntrySlab2cInsitu(scheduleEntrySlab2cInsitu);
        settingsHolder.setScheduleEntrySlab3cInsitu(scheduleEntrySlab3cInsitu);
        settingsHolder.setScheduleEntrySlab4cInsitu(scheduleEntrySlab4cInsitu);
        settingsHolder.setScheduleEntrySlabThickAngle(scheduleEntrySlabThickAngle);
        settingsHolder.setScheduleEntrySlabThinAngle(scheduleEntrySlabThinAngle);

        settingsHolder.setScheduleEntrySteelBlackKeyword(scheduleEntrySteelBlackKeyword);
        settingsHolder.setScheduleEntrySteelGalvanisedKeyword(scheduleEntrySteelGalvanisedKeyword);
        settingsHolder.setScheduleEntrySteelDimetKeyword(scheduleEntrySteelDimetKeyword);
        settingsHolder.setScheduleEntrySteelEpoxyKeyword(scheduleEntrySteelEpoxyKeyword);
        settingsHolder.setScheduleEntrySteelDuragalKeyword(scheduleEntrySteelDuragalKeyword);

    }
}
