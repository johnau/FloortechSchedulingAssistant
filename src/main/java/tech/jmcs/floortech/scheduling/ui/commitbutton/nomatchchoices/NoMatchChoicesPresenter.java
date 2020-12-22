package tech.jmcs.floortech.scheduling.ui.commitbutton.nomatchchoices;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.ui.commitbutton.addrowchoices.AddRowChoicesView;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * View to help place items that could not be automatically placed.
 *
 * Displays items in table and allows selection of choice which loads another view.
 */
public class NoMatchChoicesPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(NoMatchChoicesPresenter.class);

    @Inject private AddRowChoicesView addRowChoicesView;

    @FXML private TableView<NoMatchSolutionObject> itemsTable;
    @FXML private TableColumn<String, NoMatchSolutionObject> itemNameCol;
    @FXML private TableColumn<String, NoMatchSolutionObject> solutionCol;
    @FXML private TableColumn<String, NoMatchSolutionObject> locationCol;
    @FXML private HBox solutionButtonHbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.solutionButtonHbox.setDisable(true);
        setupTableView();
    }

    public void update(Map<String, Object> noMatchItems) {
        this.itemsTable.getItems().clear();
        this.solutionButtonHbox.setDisable(true);
        this.setNoMatchItems(noMatchItems);
    }

    public void setNoMatchItems(Map<String, Object> noMatchItems) {
        List<NoMatchSolutionObject> tableData = noMatchItems.keySet().stream()
                .map(m -> new NoMatchSolutionObject(m, "Ignore"))
                .collect(Collectors.toList());

        this.itemsTable.getItems().addAll(tableData);
    }

    public Map<String, NoMatchSolutionObject> getNoMatchItemSolutions() {
        ObservableList<NoMatchSolutionObject> editedItems = this.itemsTable.getItems();
        return editedItems.stream()
                .collect(Collectors.toMap(
                        m -> m.getItemName(),
                        m -> m)
                );
    }

    @FXML
    public void handleAddNewRowToScheduleButtonAction(ActionEvent event) {
        Scene scene = new Scene(addRowChoicesView.getView());


    }

    @FXML
    public void handleReplaceRowInScheduleButtonAction(ActionEvent event) {

    }

    @FXML
    public void handleIgnoreItemButtonAction(ActionEvent event) {

    }

    @FXML
    public void handleContinueButtonAction(ActionEvent event) {
        ((Stage) this.itemsTable.getScene().getWindow()).close();
    }

    private void setupTableView() {
        this.itemsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.solutionButtonHbox.setDisable(true);
            } else {
                this.solutionButtonHbox.setDisable(false);
            }
        });

        this.itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        this.solutionCol.setCellValueFactory(new PropertyValueFactory<>("solution"));
        this.locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
    }
}
