package tech.jmcs.floortech.scheduling.ui.status;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusPresenter implements Initializable {

    @FXML private TextFlow statusTextFlow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bindToStatusHolder();
    }

    public void addStatus(String status) {

    }

    private boolean atLineLimit() {

        return false;
    }

    private void removeLineFromTop() {

    }

    private void bindToStatusHolder() {

    }


}
