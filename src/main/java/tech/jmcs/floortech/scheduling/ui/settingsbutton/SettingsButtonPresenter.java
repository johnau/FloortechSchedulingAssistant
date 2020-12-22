package tech.jmcs.floortech.scheduling.ui.settingsbutton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.ui.settings.SettingsPresenter;
import tech.jmcs.floortech.scheduling.ui.settings.SettingsView;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsButtonPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(SettingsButtonPresenter.class);

    @Inject private SettingsView settingsView;

    private Scene settingsScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.info("SettingsButtonPresenter initializing...");
    }

    @FXML
    public void handleSettingsButtonAction(ActionEvent event) {
        ((SettingsPresenter) this.settingsView.getPresenter()).update(); // update the settings view

        Stage settingsStage = new Stage();
        if (this.settingsScene == null) {
            this.settingsScene = new Scene(this.settingsView.getView());
        }
        settingsStage.setScene(settingsScene);
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setTitle("Settings");
        settingsStage.setWidth(800);
        settingsStage.setHeight(1000);
        settingsStage.setResizable(true);
        settingsStage.show();
    }
}
