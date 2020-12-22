package tech.jmcs.floortech.scheduling.ui.commitbutton.replacerowchoices;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import tech.jmcs.floortech.scheduling.app.schedulewriter.ExcelScheduleScanner;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class ReplaceRowChoicesPresenter implements Initializable {

    @Inject private SettingsHolder settingsHolder;

    @FXML private ChoiceBox<String> scheduleRowChoiceBox;

    private Path excelScheduleFilePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleDoneButtonAction(ActionEvent event) {
        resetView();
    }

    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        resetView();
    }

    public void setExcelScheduleFilePath(Path excelScheduleFilePath) {
        this.excelScheduleFilePath = excelScheduleFilePath;
    }

    public void resetView() {
        this.scheduleRowChoiceBox.getSelectionModel().clearSelection();
        this.excelScheduleFilePath = null;
    }


    private void setupChoiceBox() {
        try {
            ExcelScheduleScanner scheduleScanner = new ExcelScheduleScanner(this.excelScheduleFilePath, settingsHolder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.scheduleRowChoiceBox.getItems().addAll();
    }
}
