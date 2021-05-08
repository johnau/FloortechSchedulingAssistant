package tech.jmcs.floortech.scheduling.ui.settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.schedulewriter.ExcelScheduleScanner;
import tech.jmcs.floortech.scheduling.app.types.FileType;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.settings.SettingsWriter;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class SettingsPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(SettingsPresenter.class);

    @Inject private SettingsHolder settingsHolder;
    @Inject private SettingsWriter settingsWriter;

    @FXML private AnchorPane mainAnchorPane;
    @FXML private TextField detailingFoldersRootTextfield;
    @FXML private TextField schedulingFolderRootTextfield;

    @FXML private TextField excelScheduleSheetNameTextField;

    @FXML private TableView<ColumnLayoutObject> columnLayoutTable;

    @FXML private ListView<String> scheduleSectionsList;

    @FXML private CheckBox trussExtractorEnabledCheckbox;
    @FXML private CheckBox beamExtractorEnabledCheckbox;
    @FXML private CheckBox slabExtractorEnabledCheckbox;
    @FXML private CheckBox sheetExtractorEnabledCheckbox;

    @FXML private ChoiceBox<String> trussScheduleSectionChoiceBox;
    @FXML private ChoiceBox<String> beamScheduleSectionChoiceBox;
    @FXML private ChoiceBox<String> slabScheduleSectionChoiceBox;
    @FXML private ChoiceBox<String> sheetScheduleSectionChoiceBox;

    @FXML private TextField trussDataFileNameTextField;
    @FXML private TextField trussDataFileExtensionTextField;
    @FXML private TextField beamDataFileNameTextField;
    @FXML private TextField beamDataFileExtensionTextField;
    @FXML private TextField slabDataFileNameTextField;
    @FXML private TextField slabDataFileExtensionTextField;
    @FXML private TextField sheetDataFileNameTextField;
    @FXML private TextField sheetDataFileExtensionTextField;

    @FXML private TextField trussEntry_cw260Field;
    @FXML private TextField trussEntry_stdEndcapCw260Field;
    @FXML private TextField trussEntry_necEndcapCw260Field;
    @FXML private TextField trussEntry_acPenoCw260Field;
    @FXML private TextField trussEntry_cw346Field;
    @FXML private TextField trussEntry_stdEndcapCw346Field;
    @FXML private TextField trussEntry_necEndcapCw346Field;
    @FXML private TextField trussEntry_acPenoCw346Field;
    @FXML private TextField trussEntry_hj200Field;
    @FXML private TextField trussEntry_stdEndcapHj200Field;
    @FXML private TextField trussEntry_necEndcapHj200Field;
    @FXML private TextField trussEntry_hj300Field;
    @FXML private TextField trussEntry_stdEndcapHj300Field;
    @FXML private TextField trussEntry_necEndcapHj300Field;
    @FXML private TextField trussEntry_acPenoHj300Field;

    @FXML private TextField beamEntry_blackKeywordField;
    @FXML private TextField beamEntry_galvanisedKeywordField;
    @FXML private TextField beamEntry_dimetKeywordField;
    @FXML private TextField beamEntry_epoxyKeywordField;

    @FXML private TextField slabEntry_int83mmField;
    @FXML private TextField slabEntry_balc2cRhsField;
    @FXML private TextField slabEntry_balc3cRhsField;
    @FXML private TextField slabEntry_balc2cInsituField;
    @FXML private TextField slabEntry_balc3cInsituField;
    @FXML private TextField slabEntry_balc4cInsituField;
    @FXML private TextField slabEntry_thickAngleField;
    @FXML private TextField slabEntry_thinAngleField;

    @FXML private TextField sheetEntry_sheetLengthKeywordField;

    @FXML private Button saveAndCloseButton;
    private List<Consumer<Boolean>> onSaveConsumers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.debug("SettingsPresenter initializing...");
        this.onSaveConsumers = new ArrayList<>();
        this.setupScheduleSectionsList();
        this.setupScheduleSectionChoiceBoxes();

        this.updateSettingsFromMemory();
    }

    @FXML
    public void handleChangeJobFoldersDetailingRootButtonAction(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();

        dirChooser.setTitle("Choose Detailing Root Directory...");
        if (!this.detailingFoldersRootTextfield.getText().isEmpty()) {
            String fieldText = this.detailingFoldersRootTextfield.getText();
            Path fieldPath = Paths.get(fieldText);
            File fieldFile = fieldPath.toFile();
            if (fieldPath != null && fieldFile.exists()) {
                if (fieldFile.isDirectory()) {
                    dirChooser.setInitialDirectory(fieldFile);
                } else {
                    dirChooser.setInitialDirectory(fieldPath.getParent().toFile());
                }
            }
        }
        File result = dirChooser.showDialog(this.mainAnchorPane.getScene().getWindow());
        if (result != null) {
            Path p = result.toPath();
            this.detailingFoldersRootTextfield.setText(p.toString());
        } else {
            LOG.debug("Choosing job folders detailing root aborted...");
        }
    }

    @FXML
    public void handleChangeJobFilesSchedulingRootButtonAction(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();

        dirChooser.setTitle("Choose Detailing Root Directory...");
        if (!this.schedulingFolderRootTextfield.getText().isEmpty()) {
            String fieldText = this.schedulingFolderRootTextfield.getText();
            Path fieldPath = Paths.get(fieldText);
            File fieldFile = fieldPath.toFile();
            if (fieldPath != null && fieldFile.exists()) {
                if (fieldFile.isDirectory()) {
                    dirChooser.setInitialDirectory(fieldFile);
                } else {
                    dirChooser.setInitialDirectory(fieldPath.getParent().toFile());
                }
            }
        }
        File result = dirChooser.showDialog(this.mainAnchorPane.getScene().getWindow());
        if (result != null) {
            Path p = result.toPath();
            this.schedulingFolderRootTextfield.setText(p.toString());
        } else {
            LOG.debug("Choosing job files scheduling root aborted...");
        }
    }

    @FXML
    public void handleEditSelectedColumnLayoutEntryButtonAction(ActionEvent event) {

    }

    @FXML
    public void handleTestTrussScheduleEntriesButtonAction(ActionEvent event) {
        LOG.debug("Handle Test Truss Schedule Entries not implemented");
    }

    @FXML
    public void handleTestSheetScheduleEntriesButtonAction(ActionEvent event) {
        LOG.debug("Handle Test Sheet Schedule Entries not implemented");
    }

    @FXML
    public void handleTestBeamScheduleEntriesButtonAction(ActionEvent event) {
        LOG.debug("Handle Test Beam Schedule Entries not implemented");
    }

    @FXML
    public void handleTestSlabScheduleEntriesButtonAction(ActionEvent event) {
        LOG.debug("Handle Test Slab Schedule Entries not implemented");
    }

    @FXML
    public void handleAddScheduleSectionButtonAction(ActionEvent event) {
        String result = this.showAddScheduleSectionInput();
        if (result != null) {
            if (!this.scheduleSectionsList.getItems().contains(result)) {
                this.scheduleSectionsList.getItems().add(result);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Already exists");
                alert.setHeaderText("Item already exists!");
                alert.setContentText("The Schedule Section list already contains: " + result);
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleRemoveScheduleSectionButtonAction(ActionEvent event) {
        int selIdx = this.scheduleSectionsList.getSelectionModel().getSelectedIndex();
        String selected = this.scheduleSectionsList.getSelectionModel().getSelectedItem();
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm remove");
        confirmAlert.setHeaderText(String.format("Remove: '%s'?", selected));
        confirmAlert.setContentText("Do you want to remove the entry?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            LOG.debug("Removing item {} : {}", selIdx, selected);
            this.scheduleSectionsList.getItems().remove(selIdx);
        }
    }

    @FXML
    public void handleTestScheduleSectionsButtonAction(ActionEvent event) {
        ObservableList<String> itemsList = this.scheduleSectionsList.getItems();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Excel Scheduling File for the test...");

        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel files", FileType.fileTypesMap.get("EXCEL"));
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All files", FileType.fileTypesMap.get("ALL"));

        fileChooser.getExtensionFilters().add(excelFilter);
        fileChooser.getExtensionFilters().add(allFilter);
        fileChooser.setSelectedExtensionFilter(excelFilter);

        File chosen = fileChooser.showOpenDialog(this.mainAnchorPane.getScene().getWindow());
        if (chosen != null) {
            try {
                ExcelScheduleScanner scanner = new ExcelScheduleScanner(chosen.toPath(), this.settingsHolder);
                List<String> notFound = scanner.checkScheduleContainsAll(itemsList);
                Alert alert;
                if (!notFound.isEmpty()) {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Entries not found");
                    alert.setHeaderText("Some of the entries were not found");
                    StringBuilder builder = new StringBuilder();
                    builder.append("Could not find the following Schedule sections: \n");
                    notFound.forEach( s -> {
                        builder.append(s);
                        builder.append(", \n");
                    });
                    builder.append("please check the list or the schedule file.");
                    alert.setContentText(builder.toString());
                } else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Test Success");
                    alert.setHeaderText("The Schedule Sections Test passed successfully");
                    alert.setContentText("All Schedule Sections were found in the selected Excel Schedule file");
                }
                alert.showAndWait();
            } catch (FileNotFoundException e) {
                LOG.warn("Could not find file just selected");
            }
        }
    }

    @FXML
    public void handleSaveSettingsAndCloseButtonAction(ActionEvent event) {
        boolean settingsOk = preCheckSettings();
        if (!settingsOk) return;

        saveSettingsToMemory();

        try {
            this.settingsWriter.writeSettingsFile();
        } catch (IOException e) {
            LOG.debug("Could not write settings file: {}", e.getMessage());
        }

        for (Consumer<Boolean> onSaveConsumer : this.onSaveConsumers) {
            onSaveConsumer.accept(true);
        }

        Stage stage = (Stage) this.mainAnchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleDiscardSettingsAndCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) this.mainAnchorPane.getScene().getWindow();
        stage.close();
    }

    public void addListenerOnSave(Consumer<Boolean> consumer) {
        this.onSaveConsumers.add(consumer);
    }

    public void update() {
        this.updateSettingsFromMemory();
    }

    private void setupScheduleSectionsList() {
        ObservableList<String> tableData = FXCollections.observableArrayList();
        this.scheduleSectionsList.setItems(tableData);
    }

    private void setupScheduleSectionChoiceBoxes() {
        this.trussScheduleSectionChoiceBox.setItems(this.scheduleSectionsList.getItems());
        this.beamScheduleSectionChoiceBox.setItems(this.scheduleSectionsList.getItems());
        this.slabScheduleSectionChoiceBox.setItems(this.scheduleSectionsList.getItems());
        this.sheetScheduleSectionChoiceBox.setItems(this.scheduleSectionsList.getItems());
    }

    private String showAddScheduleSectionInput() {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Enter a new Schedule Section Name (exact)");
        td.setTitle("Add Schedule Section...");
        Optional<String> result = td.showAndWait();
        if (result.isPresent()) {
            String entry = result.get();
            LOG.debug("Got entry name: {}", entry);
            return entry;
        }
        return null;
    }

    private boolean preCheckSettings() {
        String schedulingRoot = this.schedulingFolderRootTextfield.getText();
        if (!schedulingRoot.isEmpty()) {
            if (!Paths.get(schedulingRoot).toFile().exists()) {
                String contentText = "The path set for Job Scheduling Files Root is invalid. If you continue the path will not be saved. Would you like to continue?";
                Alert alert = new Alert(Alert.AlertType.WARNING, contentText, ButtonType.YES, ButtonType.NO);
                alert.setTitle("Invalid Path");
                alert.setHeaderText("Job Scheduling Files Path was not valid!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get().equals(ButtonType.NO)) {
                        return false;
                    }
                }
            }
        }

        String detailingRoot = this.detailingFoldersRootTextfield.getText();
        if (!detailingRoot.isEmpty()) { // check not empty
            if (!Paths.get(detailingRoot).toFile().exists()) {
                String contentText = "The path set for Job Detailing Files Root is invalid. If you continue the path will not be saved. Would you like to continue?";
                Alert alert = new Alert(Alert.AlertType.WARNING, contentText, ButtonType.YES, ButtonType.NO);
                alert.setTitle("Invalid Path");
                alert.setHeaderText("Job Detailing Files Path was not valid!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get().equals(ButtonType.NO)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void saveSettingsToMemory() {
        boolean abortExitSettingsWindow = false;

        // Job Files Scheduling Root Path
        try {
            this.settingsHolder.setJobFilesSchedulingRootPath(Paths.get(this.schedulingFolderRootTextfield.getText()));
        } catch (FileNotFoundException e) {
            LOG.debug("Did not set scheduling files root");
        }

        // Job Files Drafting Root Path
        try {
            this.settingsHolder.setJobFoldersDetailingRootPath(Paths.get(this.detailingFoldersRootTextfield.getText()));
        } catch (FileNotFoundException e) {
            LOG.debug("Did not set detailing files root");
        }

        this.settingsHolder.setExcelScheduleSheetName(this.excelScheduleSheetNameTextField.getText());

        this.settingsHolder.setBuiltInTrussExtractorEnabled(this.trussExtractorEnabledCheckbox.isSelected());
        this.settingsHolder.setTrussScheduleSectionName(this.trussScheduleSectionChoiceBox.getSelectionModel().getSelectedItem());
        this.settingsHolder.setScheduleEntryCw260Truss(this.trussEntry_cw260Field.getText());
        this.settingsHolder.setScheduleEntryStandardEndcapsCw260(this.trussEntry_stdEndcapCw260Field.getText());
        this.settingsHolder.setScheduleEntryConnectionEndcapsCw260(this.trussEntry_necEndcapCw260Field.getText());
        this.settingsHolder.setScheduleEntryTrussAirConPenoCw260(this.trussEntry_acPenoCw260Field.getText());
        this.settingsHolder.setScheduleEntryCw346Truss(this.trussEntry_cw346Field.getText());
        this.settingsHolder.setScheduleEntryStandardEndcapsCw346(this.trussEntry_stdEndcapCw346Field.getText());
        this.settingsHolder.setScheduleEntryConnectionEndcapsCw346(this.trussEntry_necEndcapCw346Field.getText());
        this.settingsHolder.setScheduleEntryTrussAirConPenoCw346(this.trussEntry_acPenoCw346Field.getText());
        this.settingsHolder.setScheduleEntryHj200Truss(this.trussEntry_hj200Field.getText());
        this.settingsHolder.setScheduleEntryStandardEndcapsHj200(this.trussEntry_stdEndcapHj200Field.getText());
        this.settingsHolder.setScheduleEntryConnectionEndcapsHj200(this.trussEntry_necEndcapHj200Field.getText());
        this.settingsHolder.setScheduleEntryHj300Truss(this.trussEntry_hj300Field.getText());
        this.settingsHolder.setScheduleEntryStandardEndcapsHj300(this.trussEntry_stdEndcapHj300Field.getText());
        this.settingsHolder.setScheduleEntryConnectionEndcapsHj300(this.trussEntry_necEndcapHj300Field.getText());
        this.settingsHolder.setScheduleEntryTrussAirConPenoHj300(this.trussEntry_acPenoHj300Field.getText());

        this.settingsHolder.setBuiltInBeamExtractorEnabled(this.beamExtractorEnabledCheckbox.isSelected());
        this.settingsHolder.setBeamScheduleSectionName(this.beamScheduleSectionChoiceBox.getSelectionModel().getSelectedItem());
        this.settingsHolder.setScheduleEntrySteelBlackKeyword(this.beamEntry_blackKeywordField.getText());
        this.settingsHolder.setScheduleEntrySteelGalvanisedKeyword(this.beamEntry_galvanisedKeywordField.getText());
        this.settingsHolder.setScheduleEntrySteelDimetKeyword(this.beamEntry_dimetKeywordField.getText());
        this.settingsHolder.setScheduleEntrySteelEpoxyKeyword(this.beamEntry_epoxyKeywordField.getText());

        this.settingsHolder.setBuiltInSlabExtractorEnabled(this.slabExtractorEnabledCheckbox.isSelected());
        this.settingsHolder.setSlabScheduleSectionName(this.slabScheduleSectionChoiceBox.getSelectionModel().getSelectedItem());
        this.settingsHolder.setScheduleEntrySlabInternal(this.slabEntry_int83mmField.getText());
        this.settingsHolder.setScheduleEntrySlab2cRhs(this.slabEntry_balc2cRhsField.getText());
        this.settingsHolder.setScheduleEntrySlab3cRhs(this.slabEntry_balc3cRhsField.getText());
        this.settingsHolder.setScheduleEntrySlab2cInsitu(this.slabEntry_balc2cInsituField.getText());
        this.settingsHolder.setScheduleEntrySlab3cInsitu(this.slabEntry_balc3cInsituField.getText());
        this.settingsHolder.setScheduleEntrySlab4cInsitu(this.slabEntry_balc4cInsituField.getText());
        this.settingsHolder.setScheduleEntrySlabThickAngle(this.slabEntry_thickAngleField.getText());
        this.settingsHolder.setScheduleEntrySlabThinAngle(this.slabEntry_thinAngleField.getText());

        this.settingsHolder.setBuiltInSheetExtractorEnabled(this.sheetExtractorEnabledCheckbox.isSelected());
        this.settingsHolder.setSheetScheduleSectionName(this.sheetScheduleSectionChoiceBox.getSelectionModel().getSelectedItem());
        this.settingsHolder.setScheduleEntrySheetLengthKeyword(this.sheetEntry_sheetLengthKeywordField.getText());

    }

    private void updateSettingsFromMemory() {
        Path detailingFoldersRootPath = this.settingsHolder.getJobFoldersDetailingRootPath();
        Path schedulingFilesRootPath = this.settingsHolder.getJobFilesSchedulingRootPath();

        Boolean trussExtractorEnabled = this.settingsHolder.isBuiltInTrussExtractorEnabled();
        String trussFileName = this.settingsHolder.getTrussDataFileName();
        String trussFileExt = this.settingsHolder.getTrussFileExtension();
        String trussScheduleSectionName = this.settingsHolder.getTrussScheduleSectionName();

        Boolean slabExtractorEnabled = this.settingsHolder.isBuiltInSlabExtractorEnabled();
        String slabFileName = this.settingsHolder.getSlabDataFileName();
        String slabFileExt = this.settingsHolder.getSlabFileExtension();
        String slabScheduleSectionName = this.settingsHolder.getSlabScheduleSectionName();

        Boolean sheetExtractorEnabled = this.settingsHolder.isBuiltInSheetExtractorEnabled();
        String sheetFileName = this.settingsHolder.getSheetDataFileName();
        String sheetFileExt = this.settingsHolder.getSheetFileExtension();
        String sheetScheduleSectionName = this.settingsHolder.getSheetScheduleSectionName();

        Boolean beamExtractorEnabled = this.settingsHolder.isBuiltInBeamExtractorEnabled();
        String beamFileName = this.settingsHolder.getBeamDataFileName();
        String beamFileExt = this.settingsHolder.getBeamFileExtension();
        String beamScheduleSectionName = this.settingsHolder.getBeamScheduleSectionName();

        String excelScheduleSheetName = this.settingsHolder.getExcelScheduleSheetName();
        List<String> scheduleSections = this.settingsHolder.getExcelScheduleFileSections();

        String cw260Length = this.settingsHolder.getScheduleEntryCw260Truss();
        String cw260StdEndcap = this.settingsHolder.getScheduleEntryStandardEndcapsCw260();
        String cw260NecEndcap = this.settingsHolder.getScheduleEntryConnectionEndcapsCw260();
        String cw260AcPeno = this.settingsHolder.getScheduleEntryTrussAirConPenoCw260();
        String cw346Length = this.settingsHolder.getScheduleEntryCw346Truss();
        String cw346StdEndcap = this.settingsHolder.getScheduleEntryStandardEndcapsCw346();
        String cw346NecEndcap = this.settingsHolder.getScheduleEntryConnectionEndcapsCw346();
        String cw346AcPeno = this.settingsHolder.getScheduleEntryTrussAirConPenoCw346();
        String hj200Length = this.settingsHolder.getScheduleEntryHj200Truss();
        String hj200StdEndcap = this.settingsHolder.getScheduleEntryStandardEndcapsHj200();
        String hj200NecEndcap = this.settingsHolder.getScheduleEntryConnectionEndcapsHj200();
        String hj300Length = this.settingsHolder.getScheduleEntryHj300Truss();
        String hj300StdEndcap = this.settingsHolder.getScheduleEntryStandardEndcapsHj300();
        String hj300NecEndcap = this.settingsHolder.getScheduleEntryConnectionEndcapsHj300();
        String hj300AcPeno = this.settingsHolder.getScheduleEntryTrussAirConPenoHj300();
        String beamBlackKeyword = this.settingsHolder.getScheduleEntrySteelBlackKeyword();
        String beamGalvKeyword = this.settingsHolder.getScheduleEntrySteelGalvanisedKeyword();
        String beamDimetKeyword = this.settingsHolder.getScheduleEntrySteelDimetKeyword();
        String beamEpoxyKeyword = this.settingsHolder.getScheduleEntrySteelEpoxyKeyword();
        String slabInt83mmSize = this.settingsHolder.getScheduleEntrySlabInternal();
        String slabBalc2cRhsSize = this.settingsHolder.getScheduleEntrySlab2cRhs();
        String slabBalc3cRhsSize = this.settingsHolder.getScheduleEntrySlab3cRhs();
        String slabBalc2cInsituSize = this.settingsHolder.getScheduleEntrySlab2cInsitu();
        String slabBalc3cInsituSize = this.settingsHolder.getScheduleEntrySlab3cInsitu();
        String slabBalc4cInsituSize = this.settingsHolder.getScheduleEntrySlab4cInsitu();
        String slabThickAngle = this.settingsHolder.getScheduleEntrySlabThickAngle();
        String slabThinAngle = this.settingsHolder.getScheduleEntrySlabThinAngle();
        String sheetLengthKeyword = "";

        this.excelScheduleSheetNameTextField.setText(excelScheduleSheetName);
        this.scheduleSectionsList.getItems().clear();
        this.scheduleSectionsList.getItems().addAll(scheduleSections); // MUST BE SET BEFORE EXTRACTOR SCHEDULE CHOICE BOXES

        this.detailingFoldersRootTextfield.setText(detailingFoldersRootPath == null ? "" : detailingFoldersRootPath.toString());
        this.schedulingFolderRootTextfield.setText(schedulingFilesRootPath == null ? "" : schedulingFilesRootPath.toString());

        this.trussExtractorEnabledCheckbox.setSelected(trussExtractorEnabled == null || trussExtractorEnabled);
        this.trussDataFileNameTextField.setText(trussFileName);
        this.trussDataFileExtensionTextField.setText(trussFileExt);
        this.trussScheduleSectionChoiceBox.setValue(trussScheduleSectionName == null ? "" : trussScheduleSectionName);

        this.slabExtractorEnabledCheckbox.setSelected(slabExtractorEnabled == null || slabExtractorEnabled);
        this.slabDataFileNameTextField.setText(slabFileName);
        this.slabDataFileExtensionTextField.setText(slabFileExt);
        this.slabScheduleSectionChoiceBox.setValue(slabScheduleSectionName == null ? "" : slabScheduleSectionName);

        this.sheetExtractorEnabledCheckbox.setSelected(sheetExtractorEnabled == null || sheetExtractorEnabled);
        this.sheetDataFileNameTextField.setText(sheetFileName);
        this.sheetDataFileExtensionTextField.setText(sheetFileExt);
        this.sheetScheduleSectionChoiceBox.setValue(sheetScheduleSectionName == null ? "" : sheetScheduleSectionName);

        this.beamExtractorEnabledCheckbox.setSelected(beamExtractorEnabled == null || beamExtractorEnabled);
        this.beamDataFileNameTextField.setText(beamFileName);
        this.beamDataFileExtensionTextField.setText(beamFileExt);
        this.beamScheduleSectionChoiceBox.setValue(beamScheduleSectionName == null ? "" : beamScheduleSectionName);

        this.trussEntry_cw260Field.setText(cw260Length);
        this.trussEntry_stdEndcapCw260Field.setText(cw260StdEndcap);
        this.trussEntry_necEndcapCw260Field.setText(cw260NecEndcap);
        this.trussEntry_acPenoCw260Field.setText(cw260AcPeno);
        this.trussEntry_cw346Field.setText(cw346Length);
        this.trussEntry_stdEndcapCw346Field.setText(cw346StdEndcap);
        this.trussEntry_necEndcapCw346Field.setText(cw346NecEndcap);
        this.trussEntry_acPenoCw346Field.setText(cw346AcPeno);
        this.trussEntry_hj200Field.setText(hj200Length);
        this.trussEntry_stdEndcapHj200Field.setText(hj200StdEndcap);
        this.trussEntry_necEndcapHj200Field.setText(hj200NecEndcap);
        this.trussEntry_hj300Field.setText(hj300Length);
        this.trussEntry_stdEndcapHj300Field.setText(hj300StdEndcap);
        this.trussEntry_necEndcapHj300Field.setText(hj300NecEndcap);
        this.trussEntry_acPenoHj300Field.setText(hj300AcPeno);

        this.beamEntry_blackKeywordField.setText(beamBlackKeyword);
        this.beamEntry_galvanisedKeywordField.setText(beamGalvKeyword);
        this.beamEntry_dimetKeywordField.setText(beamDimetKeyword);
        this.beamEntry_epoxyKeywordField.setText(beamEpoxyKeyword);

        this.slabEntry_int83mmField.setText(slabInt83mmSize);
        this.slabEntry_balc2cRhsField.setText(slabBalc2cRhsSize);
        this.slabEntry_balc3cRhsField.setText(slabBalc3cRhsSize);
        this.slabEntry_balc2cInsituField.setText(slabBalc2cInsituSize);
        this.slabEntry_balc3cInsituField.setText(slabBalc3cInsituSize);
        this.slabEntry_balc4cInsituField.setText(slabBalc4cInsituSize);
        this.slabEntry_thickAngleField.setText(slabThickAngle);
        this.slabEntry_thinAngleField.setText(slabThinAngle);

        this.sheetEntry_sheetLengthKeywordField.setText(sheetLengthKeyword);

    }
}
