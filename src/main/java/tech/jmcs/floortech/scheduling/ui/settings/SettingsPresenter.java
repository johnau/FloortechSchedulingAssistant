package tech.jmcs.floortech.scheduling.ui.settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    @FXML private ListView<String> scheduleSectionsList;

    @FXML private CheckBox trussExtractorEnabledCheckbox;
    @FXML private CheckBox beamExtractorEnabledCheckbox;
    @FXML private CheckBox slabExtractorEnabledCheckbox;
    @FXML private CheckBox sheetExtractorEnabledCheckbox;

    @FXML private ChoiceBox<String> trussScheduleSectionChoiceBox;
    @FXML private ChoiceBox<String> beamScheduleSectionChoiceBox;
    @FXML private ChoiceBox<String> slabScheduleSectionChoiceBox;
    @FXML private ChoiceBox<String> sheetScheduleSectionChoiceBox;

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

    public void addListenerOnSave(Consumer<Boolean> consumer) {
        this.onSaveConsumers.add(consumer);
    }

    public void update() {
        this.updateSettingsFromMemory();
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

    private void saveSettingsToMemory() {
        this.settingsHolder.setAllSettings(
                Paths.get(this.schedulingFolderRootTextfield.getText()),
                Paths.get(this.detailingFoldersRootTextfield.getText()),
                this.beamExtractorEnabledCheckbox.isSelected(),
                this.beamScheduleSectionChoiceBox.getSelectionModel().getSelectedItem(),
                this.sheetExtractorEnabledCheckbox.isSelected(),
                this.sheetScheduleSectionChoiceBox.getSelectionModel().getSelectedItem(),
                this.slabExtractorEnabledCheckbox.isSelected(),
                this.slabScheduleSectionChoiceBox.getSelectionModel().getSelectedItem(),
                this.trussExtractorEnabledCheckbox.isSelected(),
                this.trussScheduleSectionChoiceBox.getSelectionModel().getSelectedItem(),
                this.excelScheduleSheetNameTextField.getText(),
                this.scheduleSectionsList.getItems() // ok to pass the observable list?...
        );
    }

    private void updateSettingsFromMemory() {
        Path detailingFoldersRootPath = this.settingsHolder.getJobFoldersDetailingRootPath();
        Path schedulingFilesRootPath = this.settingsHolder.getJobFilesSchedulingRootPath();

        Boolean trussExtractorEnabled = this.settingsHolder.isBuiltInTrussExtractorEnabled();
        String trussScheduleSectionName = this.settingsHolder.getTrussScheduleSectionName();
        Boolean slabExtractorEnabled = this.settingsHolder.isBuiltInSlabExtractorEnabled();
        String slabScheduleSectionName = this.settingsHolder.getSlabScheduleSectionName();
        Boolean sheetExtractorEnabled = this.settingsHolder.isBuiltInSheetExtractorEnabled();
        String sheetScheduleSectionName = this.settingsHolder.getSheetScheduleSectionName();
        Boolean beamExtractorEnabled = this.settingsHolder.isBuiltInBeamExtractorEnabled();
        String beamScheduleSectionName = this.settingsHolder.getBeamScheduleSectionName();

        String excelScheduleSheetName = this.settingsHolder.getExcelScheduleSheetName();
        List<String> scheduleSections = this.settingsHolder.getExcelScheduleFileSections();

        this.excelScheduleSheetNameTextField.setText(excelScheduleSheetName);
        this.scheduleSectionsList.getItems().clear();
        this.scheduleSectionsList.getItems().addAll(scheduleSections); // MUST BE SET BEFORE EXTRACTOR SCHEDULE CHOICE BOXES

        this.detailingFoldersRootTextfield.setText(detailingFoldersRootPath == null ? "" : detailingFoldersRootPath.toString());
        this.schedulingFolderRootTextfield.setText(schedulingFilesRootPath == null ? "" : schedulingFilesRootPath.toString());

        this.trussExtractorEnabledCheckbox.setSelected(trussExtractorEnabled == null || trussExtractorEnabled);
        this.trussScheduleSectionChoiceBox.setValue(trussScheduleSectionName == null ? "" : trussScheduleSectionName);
        this.slabExtractorEnabledCheckbox.setSelected(slabExtractorEnabled == null || slabExtractorEnabled);
        this.slabScheduleSectionChoiceBox.setValue(slabScheduleSectionName == null ? "" : slabScheduleSectionName);
        this.sheetExtractorEnabledCheckbox.setSelected(sheetExtractorEnabled == null || sheetExtractorEnabled);
        this.sheetScheduleSectionChoiceBox.setValue(sheetScheduleSectionName == null ? "" : sheetScheduleSectionName);
        StringBuilder b = new StringBuilder();
        this.beamScheduleSectionChoiceBox.getItems().forEach(c -> {
            b.append(c);
            b.append(", ");
        });
        LOG.debug("Beam extractor values {} + setting {}", b.toString(), beamScheduleSectionName);
        this.beamExtractorEnabledCheckbox.setSelected(beamExtractorEnabled == null || beamExtractorEnabled);
        this.beamScheduleSectionChoiceBox.setValue(beamScheduleSectionName == null ? "" : beamScheduleSectionName);


    }
}
