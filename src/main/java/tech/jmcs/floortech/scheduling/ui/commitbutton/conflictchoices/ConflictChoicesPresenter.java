package tech.jmcs.floortech.scheduling.ui.commitbutton.conflictchoices;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tech.jmcs.floortech.scheduling.ui.commitbutton.nomatchchoices.NoMatchSolutionObject;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ConflictChoicesPresenter implements Initializable {

    @FXML private TableView<ConflictSolutionObject> itemsTable;
    @FXML private TableColumn<String, ConflictSolutionObject> itemNameCol;
    @FXML private TableColumn<String, ConflictSolutionObject> existingValueCol;
    @FXML private TableColumn<String, ConflictSolutionObject> solutionCol;
    @FXML private HBox solutionButtonHbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.solutionButtonHbox.setDisable(true);
        setupTable();
    }

    public void update(Map<String, Object> conflictItems) {
        this.itemsTable.getItems().clear();
        this.solutionButtonHbox.setDisable(true);
        this.setConflictItems(conflictItems);
    }

    public void setConflictItems(Map<String, Object> conflictItems) {
        List<ConflictSolutionObject> tableData = conflictItems.entrySet().stream()
                .map(m -> new ConflictSolutionObject(m.getKey(), m.getValue().toString(), "Ignore"))
                .collect(Collectors.toList());

        this.itemsTable.getItems().addAll(tableData);
    }

    public Map<String, ConflictSolutionObject> getConflictItemSolutions() {
        return null;
    }

    @FXML
    public void handleAddToExistingValueButtonAction(ActionEvent event) {

    }

    @FXML
    public void handleReplaceExistingValueButtonAction(ActionEvent event) {

    }

    @FXML
    public void handleIgnoreValueButtonAction(ActionEvent event) {

    }

    @FXML
    public void handleContinueButtonAction(ActionEvent event) {
        ((Stage) this.itemsTable.getScene().getWindow()).close();
    }

    private void setupTable() {
        this.itemsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.solutionButtonHbox.setDisable(true);
            } else {
                this.solutionButtonHbox.setDisable(false);
            }
        });

        this.itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        this.existingValueCol.setCellValueFactory(new PropertyValueFactory<>("existingValue"));
        this.solutionCol.setCellValueFactory(new PropertyValueFactory<>("solution"));
    }
}
