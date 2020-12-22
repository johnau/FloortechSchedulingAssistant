package tech.jmcs.floortech.scheduling.ui.commitbutton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.ExtractedDataToScheduleConverter;
import tech.jmcs.floortech.scheduling.app.datasource.model.BeamData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SheetData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SlabData;
import tech.jmcs.floortech.scheduling.app.datasource.model.TrussData;
import tech.jmcs.floortech.scheduling.app.exception.ExcelScheduleWriterException;
import tech.jmcs.floortech.scheduling.app.schedulewriter.ExcelScheduleScanner;
import tech.jmcs.floortech.scheduling.app.schedulewriter.ExcelScheduleUpdateConfirmer;
import tech.jmcs.floortech.scheduling.app.schedulewriter.ExcelScheduleUpdaterImpl;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.ui.DataTargetHolder;
import tech.jmcs.floortech.scheduling.ui.ExtractedDataHolderFX;
import tech.jmcs.floortech.scheduling.ui.commitbutton.conflictchoices.ConflictChoicesPresenter;
import tech.jmcs.floortech.scheduling.ui.commitbutton.conflictchoices.ConflictChoicesView;
import tech.jmcs.floortech.scheduling.ui.commitbutton.conflictchoices.ConflictSolutionObject;
import tech.jmcs.floortech.scheduling.ui.commitbutton.nomatchchoices.NoMatchChoicesPresenter;
import tech.jmcs.floortech.scheduling.ui.commitbutton.nomatchchoices.NoMatchChoicesView;
import tech.jmcs.floortech.scheduling.ui.commitbutton.nomatchchoices.NoMatchSolutionObject;

import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CommitButtonPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(CommitButtonPresenter.class);

    @Inject private NoMatchChoicesView noMatchChoicesView;
    @Inject private ConflictChoicesView conflictChoiceView;

    @Inject private ExtractedDataHolderFX extractedDataHolder;
    @Inject private ExtractedDataToScheduleConverter dataConverter;
    @Inject private SettingsHolder settingsHolder;
    @Inject private DataTargetHolder dataTargetHolder;

    private Map<String, Object> scheduleDataMap;
    private Path checkedSchedulePath;

    private Scene noMatchConfirmScene;
    private Scene conflictConfirmScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.info("CommitButtonPresenter initializing...");
    }

    @FXML
    public void handleCommitButtonAction(ActionEvent event) {
        scheduleDataMap = new HashMap<>();
        LOG.debug("Beginning commit to schedule");
        try {
            preCheckScheduleFile();
            convertData();
            updateScheduleFile();
            openExcelFile();
        } catch (FileNotFoundException nex) {
            LOG.debug("Failed commit to schedule: {}", nex.getMessage());
        } catch (CommitToScheduleException cex) {
            LOG.debug("Commit Aborted!");
        }
    }

    private void preCheckScheduleFile() throws FileNotFoundException, CommitToScheduleException {
        String dataTargetPath = this.dataTargetHolder.getDataTargetPath();
        if (dataTargetPath == null || dataTargetPath.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File Path Not Set");
            alert.setHeaderText("File Path Not Set");
            alert.setContentText("Please choose an Excel Scheduling File (in Data Target section) for the extracted data.");
            alert.showAndWait();
            throw new CommitToScheduleException("File not set");
        }
        Path schedulePath = Paths.get(this.dataTargetHolder.getDataTargetPath());

        LOG.debug("Beginning pre-check of schedule file: {}", schedulePath);

        File scheduleFile = schedulePath.toFile();
        if (!scheduleFile.exists() || !scheduleFile.isFile()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("File not found");
            alert.setHeaderText("Could not access Schedule File");
            alert.setContentText(String.format("File path: %s \n \nWas not found", schedulePath.toString()));
            alert.showAndWait();
            throw new FileNotFoundException("File not found");
        }

        LOG.debug("File found");

        try {
            ExcelScheduleScanner scanner = new ExcelScheduleScanner(schedulePath, this.settingsHolder);
//            scanner.checkScheduleContainsAll();
//            scanner.scanForStuff()


        } catch (FileNotFoundException e) {
            throw e;
        }

        checkedSchedulePath = schedulePath;

        LOG.debug("Confirmed schedule file '{}' ok", schedulePath);
    }

    private void convertData() {
        LOG.debug("Converting data...");
        Map<Long, BeamData> beamData = this.extractedDataHolder.getBeamDataMap();
        Map<Long, TrussData> trussData = this.extractedDataHolder.getTrussDataMap();
        Map<Long, SlabData> slabData = this.extractedDataHolder.getSlabDataMap();
        Map<Long, SheetData> sheetData = this.extractedDataHolder.getSheetDataMap();
        Map<String, Map<Long, Map<String, Object>>> customDatas = this.extractedDataHolder.getCustomData();

        Map<String, Object> beamDataConverted = beamData == null ? null : dataConverter.convertBeamDataForSchedule(beamData, this.settingsHolder);
        Map<String, Object> trussDataConverted = trussData == null ? null : dataConverter.convertTrussDataForSchedule(trussData, this.settingsHolder);
        Map<String, Object> slabDataConverted = slabData == null ? null : dataConverter.convertSlabDataForSchedule(slabData, this.settingsHolder);
        Map<String, Object> sheetDataConverted = sheetData == null ? null : dataConverter.convertSheetDataForSchedule(sheetData, this.settingsHolder);

        if (beamDataConverted != null) this.scheduleDataMap.putAll(beamDataConverted);
        if (trussDataConverted != null) this.scheduleDataMap.putAll(trussDataConverted);
        if (slabDataConverted != null) this.scheduleDataMap.putAll(slabDataConverted);
//      if (sheetDataConverted != null) this.scheduleDataMap.putAll(sheetDataConverted);

        LOG.debug("Converted data");
    }

    private void updateScheduleFile() throws CommitToScheduleException {
        LOG.debug("Updating the schedule file");
        try {
            ExcelScheduleUpdaterImpl scheduleUpdater = new ExcelScheduleUpdaterImpl(checkedSchedulePath);
            scheduleUpdater.setTargetSheetNumber(0);
            ExcelScheduleUpdateConfirmer confirmer = scheduleUpdater.updateSchedule(this.scheduleDataMap);
            if (confirmer != null) {
                LOG.debug("Some issues during update");

                Map<String, Object> noMatchProblems = confirmer.getNotFoundProblems();
                if (!noMatchProblems.isEmpty()) {
                    LOG.debug("There are {} issues with the update, some items not found on schedule", noMatchProblems.size());
                    noMatchProblems.forEach( (name, value) -> LOG.debug("Could not match: {}", name));
                    Map<String, NoMatchSolutionObject> noMatchSolutions = showNoMatchConfirmView(noMatchProblems);
                    if (noMatchSolutions != null) {
                        noMatchSolutions.forEach((name, solution) -> {
                            LOG.debug("Item {} set to: {} @ {}", name, solution.getSolution(), solution.getLocation().toString());
                        });

                        //            confirmer.forceOverwrite_replaceCell(key, realValue, new ExcelCellAddress(2, 140));
                        //            confirmer.forceOverwrite_addNewCell(key, realValue, new ExcelCellAddress(2, 140));
                    }

                }

                Map<String, Object> conflictProblems = confirmer.getConflictProblems();
                // prompt user about issues and offer solutions
                if (!conflictProblems.isEmpty()) {
                    LOG.debug("There are {} issues with the update, some items have conflicting values on the schedule", conflictProblems.size());
                    conflictProblems.forEach( (name, value) -> LOG.debug("Conflicting entry: {}", name));
                    Map<String, ConflictSolutionObject> conflictSolutions = showConflictConfirmView(conflictProblems);
                    if (conflictSolutions != null) {
                        conflictSolutions.forEach((name, solution) -> {
                            LOG.debug("Item {} set to: {}", name, solution.getSolution());
                        });

                        //            confirmer.forceOverwrite_replaceCurrentValue(name, realValue);
                        //            confirmer.forceOverwrite_addToCurrentValue(name, realValue);
                    }
                }

                List<String> errors = confirmer.getUpdateErrors();
                // prompt user about fatal errors

            } else {
                LOG.debug("No issues during update");
            }

            scheduleUpdater.completeUpdate();

        } catch (IOException e) {
            LOG.debug("IO Exception thrown: {}", e.getMessage());
            throw new CommitToScheduleException("Could not access the file");
        } catch (ExcelScheduleWriterException e) {
            LOG.debug("Schedule Writer Exception thrown: {}", e.getMessage());
            throw new CommitToScheduleException("Could not write to Excel Schedule: " + e.getMessage());
        }
    }

    private Map<String, NoMatchSolutionObject> showNoMatchConfirmView(Map<String, Object> noMatchItems) {
        NoMatchChoicesPresenter presenter = (NoMatchChoicesPresenter) this.noMatchChoicesView.getPresenter();
        presenter.update(noMatchItems);

        Stage noMatchConfirmStage = new Stage();
        if (this.noMatchConfirmScene == null) {
            this.noMatchConfirmScene = new Scene(this.noMatchChoicesView.getView());
        }
        noMatchConfirmStage.setScene(noMatchConfirmScene);
        noMatchConfirmStage.setWidth(600);
        noMatchConfirmStage.setHeight(600);
        noMatchConfirmStage.setResizable(false);
        noMatchConfirmStage.initModality(Modality.APPLICATION_MODAL);
        noMatchConfirmStage.initStyle(StageStyle.UTILITY);
        noMatchConfirmStage.setOnCloseRequest(event -> {
            LOG.debug("Can't close window by x");
            event.consume();
        });
        noMatchConfirmStage.showAndWait();

        return presenter.getNoMatchItemSolutions();
    }

    private Map<String, ConflictSolutionObject> showConflictConfirmView(Map<String, Object> conflictItems) {
        ConflictChoicesPresenter presenter = (ConflictChoicesPresenter) this.conflictChoiceView.getPresenter();
        presenter.update(conflictItems);

        Stage conflictConfirmStage = new Stage();
        if (this.conflictConfirmScene == null) {
            this.conflictConfirmScene = new Scene(this.conflictChoiceView.getView());
        }
        conflictConfirmStage.setScene(this.conflictConfirmScene);
        conflictConfirmStage.setWidth(600);
        conflictConfirmStage.setHeight(600);
        conflictConfirmStage.setResizable(false);
        conflictConfirmStage.initModality(Modality.APPLICATION_MODAL);
        conflictConfirmStage.initStyle(StageStyle.UTILITY);
        conflictConfirmStage.setOnCloseRequest(event -> {
            LOG.debug("Can't close window by x");
            event.consume();
        });
        conflictConfirmStage.showAndWait();

        return presenter.getConflictItemSolutions();
    }

    private void showUpdateErrorMessages() {

    }

    private void openExcelFile() {
        try {
            Desktop.getDesktop().open(this.checkedSchedulePath.toFile());
        } catch (IOException e) {
            LOG.debug("Could not open the excel file.");
        }
    }

}
