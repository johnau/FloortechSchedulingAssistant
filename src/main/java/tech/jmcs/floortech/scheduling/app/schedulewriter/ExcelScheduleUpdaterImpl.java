package tech.jmcs.floortech.scheduling.app.schedulewriter;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.exception.ExcelScheduleWriterException;
import tech.jmcs.floortech.scheduling.app.util.ExcelCellAddress;
import tech.jmcs.floortech.scheduling.app.util.XLSHelper;
import tech.jmcs.floortech.scheduling.app.util.XLSUtility;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ExcelScheduleUpdaterImpl extends ExcelScheduleUpdater {
    private static final Logger LOG = LoggerFactory.getLogger(ExcelScheduleUpdaterImpl.class);

    // TODO: Move these three static variables to settings. (some settings created but not linked)
    public static final String SCHEDULING_SHEET_NAME = "Mat. Estim & Data Dump";
    protected static final Integer DATA_NAME_COLUMN = 1;
    protected static final Integer DATA_VALUE_COLUMN = 2;
    protected static final Integer DATA_UNIT_COLUMN = 3;
    protected static final Integer DATA_RATE_COLUMN = 4;
    protected static final Integer DATA_TOTAL_COLUMN = 5;
    protected static final Integer DATA_COMMENT_COLUMN = 6;

    protected Integer targetSheetNumber;

    protected Sheet targetSheet;

    protected Path excelSchedulePath;

    protected XLSUtility xls;

     public ExcelScheduleUpdaterImpl(Path excelSchedulePath) throws IOException, ExcelScheduleWriterException {
        if (excelSchedulePath == null || excelSchedulePath.toString().isEmpty()) {
            LOG.error("Could not create Excel Scheduling File Updater without a file path");
            throw new ExcelScheduleWriterException("Could not create Excel Scheduling File Updater without a file path");
        }
        this.excelSchedulePath = excelSchedulePath;

        this.xls = new XLSUtility(excelSchedulePath);

        this.targetSheetNumber = 0;

        this.targetSheet = null;
    }

    /**
     * Method for accessing the sheets that match the criteria for a scheduling sheet (currently just name) and
     * returning them, mapped by the sheet index in the workbook.
     *
     * Note: This method should be used to let user select which sheet to use if there are multiple matches.  The
     * default should be the first one (lowest index).
     * The selected sheet number should then be set with the method setTargetSheetNumber(Integer targetSheetNumber).
     * Now isValid() and updateSchedule() methods can be used without issue.
     *
     * @return
     */
    public Map<Integer, Sheet> getSchedulingSheets() {
        Map<Integer, Sheet> sheets = this.xls.getSheetsMatchingName(SCHEDULING_SHEET_NAME, true, true);
        return sheets;
    }

    /**
     * Set the target sheet, the Scheduling Sheet, that will be validated and updated.
     * @param targetSheetNumber
     */
    public void setTargetSheetNumber(Integer targetSheetNumber) {
        this.targetSheetNumber = targetSheetNumber;
    }

    /**
     * Method to check if the Target Scheduling Sheet meets the required format to be updated.
     * @return
     * @throws ExcelScheduleWriterException
     */
    @Override
    public boolean isValid() throws ExcelScheduleWriterException {
        LOG.warn("Validation method not yet implemented");
        // TODO: implement
        // confirm correct sheet (by Name of sheet and other markers)
        // consider how to check for all sheets that match and allow user to interface with which one is used.
        // default to the first occurrence of a match in the book.
        return true;
    }

    /**
     * Updates the Schedule based on the map of values received.  Names are looked up and values are placed in the
     * appropriate cell based on the VALUE_COLUMN setting.
     * updateErrors (list), noMatchProblems(Map), conflictDataProblems(Map) are updated during this method, they should
     * be retrieved and handled before calling the completeUpdate() method.
     * @param values
     * @throws ExcelScheduleWriterException
     * @return
     */
    @Override
    public ExcelScheduleUpdateConfirmer updateSchedule(Map<String, Object> values) throws ExcelScheduleWriterException {
        ExcelScheduleUpdateConfirmer confirmer = new ExcelScheduleUpdateConfirmerImpl(this);

        if (!this.isValid()) {
            LOG.warn("Schedule was not a valid format");
            confirmer.addUpdateError("Schedule was not the expected format");
            return null;
        }

        // TODO: Remove errors map or change it and remove problemsMap
        DataFormatter dataFormatter = new DataFormatter();

        LOG.debug("Loading sheet: {}", targetSheetNumber);
        try {
            if (this.targetSheet == null) this.targetSheet = this.xls.getSheet(targetSheetNumber, true);
            values.forEach( (name, value) -> {
                LOG.debug("Adding to schedule: {} = {}", name, value);
                String result = updateCellWithValue(name, value, this.targetSheet, dataFormatter);
                switch (result) {
                    case OK: //ok
                        break;
                    case NAME_NOT_FOUND: //cant find
                        confirmer.addNotFoundProblem(name, value); // store data name and value
                        break;
                    default:
                        confirmer.addConflictProblem(name, result); // currently stores data name and value, should store data name and already present value(??)
                        break;
                }
            });
        } catch (Exception e) {
            LOG.warn("Could not access requested sheet {}", targetSheetNumber);
            confirmer.addUpdateError("Could not access sheet " + targetSheetNumber + 1);
            return null;
        }

        if (confirmer.getNotFoundProblems().isEmpty() && confirmer.getConflictProblems().isEmpty() && confirmer.getUpdateErrors().isEmpty()) {
            LOG.debug("No issues to fix with update");
            return null;
        }
        LOG.debug("Some issues to fix with update, returning a confirmer");
        return confirmer;
    }

    /**
     * Complete the workbook update, save and close. Clears error and problems memory.
     * @throws IOException
     */
    @Override
    public void completeUpdate() throws IOException {
        try {
            this.xls.saveAndClose();
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public XLSUtility getXlsUtil() {
        return this.xls;
    }

    @Override
    public Integer getTargetSheetNumber() {
        return this.targetSheetNumber;
    }

    @Override
    public Sheet getTargetSheet() {
        return this.targetSheet;
    }

    // TODO: Change all of these methods to get column int will be from Settings, not from the static variables

    @Override
    public Integer getDataNameColumn() {
        return DATA_NAME_COLUMN;
    }

    @Override
    public Integer getDataValueColumn() {
        return DATA_VALUE_COLUMN;
    }

    @Override
    public Integer getDataUnitColumn() {
        return DATA_UNIT_COLUMN;
    }

    @Override
    public Integer getDataRateColumn() {
        return DATA_RATE_COLUMN;
    }

    @Override
    public Integer getDataTotalColumn() {
        return DATA_TOTAL_COLUMN;
    }

    @Override
    public Integer getDataCommentColumn() {
        return DATA_COMMENT_COLUMN;
    }


    /**
     * Constants for updateCellWithValue(.....) method
     */
    private static final String NAME_NOT_FOUND = "$NAME$";
    private static final String OK = "$OK$";
    private static final String CELL_DIDNT_EXIST = "$NO_CELL$";

    /**
     * Finds a cell in the item column by String name parameter match and fills the related value cell with the Object
     * value parameter.  Returns True if update was successful, else false.
     * @param name
     * @param value
     * @param schSheet
     * @param dataFormatter
     * @return Returns OK if OK, NAME_NOT_FOUND if could not find name, Cellvalue as string if value cell was not empty
     */
    protected String updateCellWithValue(String name, Object value, Sheet schSheet, DataFormatter dataFormatter) {
        // find target address
        ExcelCellAddress itemNameAddress;
        if (name.toUpperCase().contains("T-BAR")) {
            itemNameAddress = XLSHelper.findCellByName(schSheet, DATA_NAME_COLUMN, name, dataFormatter, true);
        } else {
            itemNameAddress = XLSHelper.findCellByName(schSheet, DATA_NAME_COLUMN, name, dataFormatter);
        }

        if (itemNameAddress == null) {
            LOG.debug("Could not find a place for [{} : {}]", name, value);
            return NAME_NOT_FOUND;
        }

        ExcelCellAddress valueAddress = new ExcelCellAddress(DATA_VALUE_COLUMN, itemNameAddress.getRow());

        Row targetRow = schSheet.getRow(valueAddress.getRow());
        Cell targetCell = XLSHelper.getCellByColumnIndex(targetRow, valueAddress.getCol());
        if (targetCell == null) {
            LOG.warn("Cell did not exist to update");
            // TODO: Create cell? It should exist, this will likely not get hit unless there is an error with the xls.
            return CELL_DIDNT_EXIST;
        }

        // check target address is empty
        if (!XLSHelper.cellIsEmpty(targetCell, dataFormatter)) {
            return dataFormatter.formatCellValue(targetCell);
        }

        // write value to target address
        XLSHelper.updateCellValue(targetCell, value);

        return OK;
    }

}
