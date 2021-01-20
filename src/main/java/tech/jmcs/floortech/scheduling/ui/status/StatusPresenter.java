package tech.jmcs.floortech.scheduling.ui.status;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tech.jmcs.floortech.scheduling.ui.StatusHolderFX;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class StatusPresenter implements Initializable {

    @Inject private StatusHolderFX statusHolder;

    @FXML private VBox statusBox;
    @FXML private ListView<String> statusList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bindToStatusHolder();
        AnchorPane.setLeftAnchor(this.statusBox, 0d);
        AnchorPane.setRightAnchor(this.statusBox, 0d);
        AnchorPane.setTopAnchor(this.statusBox, 0d);
        AnchorPane.setBottomAnchor(this.statusBox, 0d);

    }

    private void bindToStatusHolder() {
        this.statusList.setItems(this.statusHolder.getStatusWindowBuffer());
        this.statusHolder.getStatusWindowBuffer().addListener((ListChangeListener<String>) c -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    this.statusList.scrollTo(this.statusList.getItems().size() - 1);
                }
            }
        });
    }

}
