package tech.jmcs.floortech.scheduling.ui.extractbutton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.ui.ExtractorManagerFX;
import tech.jmcs.floortech.scheduling.ui.StatusHolderFX;
import tech.jmcs.floortech.scheduling.ui.helper.StatusType;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ExtractButtonPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(ExtractButtonPresenter.class);

    @Inject private StatusHolderFX statusHolder;
    @Inject private ExtractorManagerFX extractorManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.info("ExtractButtonPresenter initializing...");
    }

    @FXML
    public void handleExtractDataButtonAction(ActionEvent event) {
        this.statusHolder.addStatus(StatusType.NORMAL, "Begin extracting...");
        List<String> extractorsList = extractorManager.processActiveExtractors();
        for (String s : extractorsList) {
            this.statusHolder.addStatus(StatusType.NORMAL, s);
        }
        this.statusHolder.addStatus(StatusType.NORMAL, "Completed extracting.");
    }
}
