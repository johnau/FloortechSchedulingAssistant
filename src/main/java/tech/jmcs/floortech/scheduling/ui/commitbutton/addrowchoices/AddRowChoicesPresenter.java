package tech.jmcs.floortech.scheduling.ui.commitbutton.addrowchoices;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;

import javax.inject.Inject;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class AddRowChoicesPresenter implements Initializable {

    @Inject private SettingsHolder settingsHolder;

    @FXML private ChoiceBox<String> scheduleSectionChoiceBox;

    private Path excelScheduleFilePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupChoiceBox();
    }

    @FXML
    public void handleDoneButtonAction(ActionEvent event) {

    }

    @FXML
    public void handleCancelButtonAction(ActionEvent event) {

    }

    public void setExcelScheduleFilePath(Path excelScheduleFilePath) {
        this.excelScheduleFilePath = excelScheduleFilePath;
    }

    public void resetView() {
        this.scheduleSectionChoiceBox.getSelectionModel().clearSelection();
        this.excelScheduleFilePath = null;
    }

    private void setupChoiceBox() {
        List<String> sections = settingsHolder.getExcelScheduleFileSections();
        this.scheduleSectionChoiceBox.getItems().addAll(sections);
    }
}
