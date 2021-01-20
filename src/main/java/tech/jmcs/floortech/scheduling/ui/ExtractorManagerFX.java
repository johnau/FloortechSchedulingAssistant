package tech.jmcs.floortech.scheduling.ui;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.*;
import tech.jmcs.floortech.scheduling.app.datasource.model.BeamData;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SlabData;
import tech.jmcs.floortech.scheduling.app.datasource.model.TrussData;
import tech.jmcs.floortech.scheduling.app.exception.DataExtractorException;
import tech.jmcs.floortech.scheduling.app.types.DataSourceExtractorType;
import tech.jmcs.floortech.scheduling.app.util.ExcelCellAddress;
import tech.jmcs.floortech.scheduling.app.util.XLSHelper;

import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

/**
 * Class to handle extraction of data sources
 *
 * Also handles the FX Components related to extraction
 *
 */
public class ExtractorManagerFX {
    protected static final Logger LOG = LoggerFactory.getLogger(ExtractorManagerFX.class);

    @Inject private ExtractorComponentHolderFX extractorHolder;
    @Inject private ExtractedDataHolderFX extractedDataHolder;

    public ExtractorManagerFX() {
        LOG.info("ExtractorManagerFX constructing...");
    }

    public List<String> processActiveExtractors() {
        List<String> extractorsList = new ArrayList<>();
        this.extractorHolder.getActiveExtractors().forEach( (name, descriptor) -> {
            DataSourceExtractorType type = descriptor.getType();
            String name2 = descriptor.getName();
            String filePathText = descriptor.getFilePathText();
            if (type.equals(DataSourceExtractorType.BEAM)
                    || type.equals(DataSourceExtractorType.SHEET)
                    || type.equals(DataSourceExtractorType.SLAB)
                    || type.equals(DataSourceExtractorType.TRUSS)) {
                boolean success = this.processBuiltInDataSourceExtraction(name2, type, filePathText);
                if (success) {
                    LOG.debug("Successfully extracted: {}", name2);
                    extractorsList.add("Successfully extracted '" + name2 + "' from: " + filePathText);
                } else {
                    LOG.warn("Failed to extract: {}", name2);
                    extractorsList.add("Failed to extract '" + name2 + "' from: " + filePathText);
                }
            }
            else if (type.equals(DataSourceExtractorType.GENERIC_SIMPLE)) {
                // process generic simple data sources
            }
        });
        return extractorsList;
    }

    private boolean processBuiltInDataSourceExtraction(String name, DataSourceExtractorType type, String filePath) {
        switch (type) {
            case BEAM: return beamExtract(name, type, filePath);
            case SHEET: return sheetExtract(name, type, filePath);
            case SLAB: return slabExtract(name, type, filePath);
            case TRUSS: return trussExtract(name, type, filePath);
            default: return false;
        }
    }

    private boolean beamExtract(String name, DataSourceExtractorType type, String filePath) {
        LOG.debug("Beam extractor: {} {} {}", name, type, filePath);
        if (filePath == null || filePath.isEmpty()) {
            LOG.debug("No file to extract");
            return false;
        }
        Path excelFile = Paths.get(filePath);
        BeamListExtractor extractor = DataExtractorFactory.openExcelFileAsBeamList(excelFile);
        if (extractor == null) {
            LOG.warn("Could not create extractor, is the file in open in excel? does it exist?");
            return false;
        }

        try {
            extractor.extract();
        } catch (DataExtractorException e) {
            LOG.warn("Data Extractor Exception thrown: [%s] %s \n",  e.getDataSourceName(), e.getMessage());
            return false;
        }

        ExtractedTableData<BeamData> data = extractor.getData();
        if (data == null) {
            LOG.warn("Extraction was not be completed");
            return false;
        }

        Map<Long, BeamData> dataMap = data.getData();
        this.extractedDataHolder.setBeamDataMap(dataMap);

        LOG.info("Extracted Beam Data with Built In Beam Extractor");

        return true;
    }

    private boolean sheetExtract(String name, DataSourceExtractorType type, String filePath) {
        LOG.debug("Sheet extractor: {} {} {}", name, type, filePath);
        if (filePath == null || filePath.isEmpty()) {
            LOG.debug("No file to extract");
            return false;
        }
        Path excelPath = Paths.get(filePath);
        // using Generic Extractor for now

        /**
         * START of Setting up for use of Generic Extractor
         */
        Map<ExcelCellAddress, String> tableLayout = new HashMap<>();
        tableLayout.put(new ExcelCellAddress(0, 2), ExcelDataSourceExtractor.DATA_START);
        tableLayout.put(new ExcelCellAddress(0, 0), "Sheets");
        tableLayout.put(new ExcelCellAddress(0, 1), "Length");
        tableLayout.put(new ExcelCellAddress(1, 1), "ID");
        tableLayout.put(new ExcelCellAddress(2, 1), "Qty");

        String lenColName = "Length";
        String idColName = "ID";
        String qtyColName = "Quantity";

        Map<Integer, GenericExtractorColumnDescription> columnMap = new HashMap<>();
        GenericExtractorColumnDescription lenCol = new GenericExtractorColumnDescription(lenColName, 0, GenericExtractorColumnDataType.NUMERIC);
        GenericExtractorColumnDescription idCol = new GenericExtractorColumnDescription(idColName, 1, GenericExtractorColumnDataType.TEXT);
        GenericExtractorColumnDescription qtyCol = new GenericExtractorColumnDescription(qtyColName, 2, GenericExtractorColumnDataType.NUMERIC);

        columnMap.put(0, lenCol);
        columnMap.put(1, idCol);
        columnMap.put(2, qtyCol);

        List<Integer> validRowDataList = Arrays.asList(1, 2);

        List<Function<Row, String>> recordValidationFunctions = new ArrayList();
        Function<Row, String> recordValidator = (row) -> {
            Cell idCell = XLSHelper.getCellByColumnIndex(row, 1);
            if (idCell == null) return "The ID Cell did not exist";
            if (idCell.getCellType().equals(CellType.STRING)) {
                String cVal = idCell.getStringCellValue().toLowerCase();
                if (cVal.contains("z")) {
                    return null;
                }
            }
            return "The ID Cell did not contain the letter 'Z'. Sheet ID's start with 'Z'";
        };
        recordValidationFunctions.add(recordValidator);
        /**
         * END of Setting up for use of Generic Extractor
         */

        GenericExcelHorizontalTableDataExtractor extractor = DataExtractorFactory.openExcelFileAsGenericList(excelPath, tableLayout, columnMap, validRowDataList, null, recordValidationFunctions);

        try {
            extractor.extract();
        } catch (DataExtractorException e) {
            System.out.printf("Extraction error: %s \n", e.getMessage());
            return false;
        }

        ExtractedTableData<Map<String, Object>> data = extractor.getData();
        if (data == null) {
            System.out.println("Table data was null");
            return false;
        }
        Map<Long, Map<String, Object>> dataMap = data.getData();
        this.extractedDataHolder.addCustomData("Sheet", dataMap); // custom data always stored as Map<Long, Map<String, Object>>...

        LOG.info("Extracted Sheet Data with Built In (Hardcoded Generic) Sheet Extractor");

        return true;
    }

    private boolean slabExtract(String name, DataSourceExtractorType type, String filePath) {
        LOG.debug("Slab extractor: {} {} {}", name, type, filePath);
        if (filePath == null || filePath.isEmpty()) {
            LOG.debug("No file to extract");
            return false;
        }
        Path pdfPath = Paths.get(filePath);
        SlabListExtractor extractor = DataExtractorFactory.openPdfAsSlabList(pdfPath);
        if (extractor == null) {
            LOG.debug("Could not create extractor, is the file in open in excel? does it exist?");
            return false;
        }

        try {
            extractor.extract();
        } catch (DataExtractorException e) {
            LOG.debug("Could not complete extraction of slab data");
            return false;
        }

        ExtractedTableData<SlabData> data = extractor.getData();
        if (data == null) {
            LOG.warn("Extraction was not be completed");
            return false;
        }

        Map<Long, SlabData> dataMap = data.getData();
        this.extractedDataHolder.setSlabDataMap(dataMap);

        LOG.info("Extracted Slab Data with Built In Slab Extractor");

        return true;
    }

    private boolean trussExtract(String name, DataSourceExtractorType type, String filePath) {
        LOG.debug("Truss extractor: {} {} {}", name, type, filePath);
        if (filePath == null || filePath.isEmpty()) {
            LOG.debug("No file to extract");
            return false;
        }
        Path excelFile = Paths.get(filePath);
        TrussListExtractor extractor = DataExtractorFactory.openExcelFileAsTrussList(excelFile);
        if (extractor == null) {
            LOG.debug("Could not create extractor, is the file in open in excel? does it exist?");
            return false;
        }

        try {
            extractor.extract();
        } catch (DataExtractorException e) {
            LOG.debug("Could not extract Truss List Data from file: {}", filePath);
            return false;
        }

        ExtractedTableData<TrussData> data = extractor.getData();
        if (data == null) {
            LOG.warn("Extraction was not be completed");
            return false;
        }

        Map<Long, TrussData> dataMap = data.getData();
        this.extractedDataHolder.setTrussDataMap(dataMap);

        LOG.info("Extracted Truss Data with Built In Truss Extractor");

        return true;
    }

}
