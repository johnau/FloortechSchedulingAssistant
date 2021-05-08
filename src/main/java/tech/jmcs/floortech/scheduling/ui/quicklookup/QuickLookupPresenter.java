package tech.jmcs.floortech.scheduling.ui.quicklookup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.filesearch.FloortechFinder;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.util.FloortechHelper;
import tech.jmcs.floortech.scheduling.ui.QuickLookupDataHolderFX;
import tech.jmcs.floortech.scheduling.ui.StatusHolderFX;
import tech.jmcs.floortech.scheduling.ui.exceptions.SettingNotSetException;
import tech.jmcs.floortech.scheduling.ui.helper.StatusType;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class QuickLookupPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(QuickLookupPresenter.class);

    @Inject private StatusHolderFX statusHolder;
    @Inject private SettingsHolder settingsHolder;
    @Inject private QuickLookupDataHolderFX quickLookupDataHolder;

    @FXML private TextField jobNumberTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.info("QuickLookupPresenter initializing...");
        setupJobNumberListener();
    }

    @FXML
    public void handleKeyPressOnJobNumberFieldAction(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.doAutoFillAction();
        }
    }

    @FXML
    public void handleAutoFillButtonAction(ActionEvent event) {
        doAutoFillAction();
    }

    private void doAutoFillAction() {
        String jobNumberStr = this.jobNumberTextField.getText();
        boolean valid = FloortechHelper.isValidJobNumber(jobNumberStr);
        if (!valid) {
            LOG.debug("Could not auto-fill, job number was invalid");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Job Number");
            alert.setHeaderText("Invalid Job Number!");
            alert.setContentText("Can not Auto-fill data with invalid job number.  A valid job number is 5 or 6 digits long");
            alert.showAndWait();

            return;
        }

        this.statusHolder.addStatus(StatusType.NORMAL, "Scanning for job files and folders...");

        FloortechFinder fileFinder = new FloortechFinder(this.settingsHolder.getJobFoldersDetailingRootPath(), this.settingsHolder.getJobFilesSchedulingRootPath());
        Path jobFolderPath = null;
        try {
            jobFolderPath = fileFinder.findJobFolder(jobNumberStr);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "The path provided for Detailing Job Folders '" + e.getMessage() +"'was invalid. \n Please check settings.");
            alert.setTitle("Invalid path");
            alert.setHeaderText("Invalid Detailing Job Folders Path");
            alert.showAndWait();
            return;
        } catch (SettingNotSetException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "The " + e.getMessage() + " setting is not set");
            alert.setTitle("Setting not set");
            alert.setHeaderText(e.getMessage() + " setting is not set");
            alert.showAndWait();
            return;
        }

        if (jobFolderPath == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Could not find Detailing folder for job: " + jobNumberStr);
            alert.setTitle("Folder not found");
            alert.setHeaderText("Can't find folder for " + jobNumberStr);
            alert.showAndWait();
            return;
        }

        this.quickLookupDataHolder.setJobFolder(jobFolderPath);
        this.statusHolder.addStatus(StatusType.IMPORTANT, "Found job folder @ " + jobFolderPath);



        // lookup scheduling file based on job number
        // populate text field
        // lookup job folder based on job number
        // populate text field
        // process files in folder and assign to enabled data source extractors.
        // populate text fields
    }

    /**
     * Private method to setup listener on the Job Number TextField for automatic lookup.
     */
    private void setupJobNumberListener() {
        jobNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (FloortechHelper.isValidJobNumber(newValue)) {
                LOG.debug("Lookup job number: {}", newValue);
                // TODO: Implement as a notification that files and folders have been located (async lookup)
            } else {
                LOG.debug("Job number entered is not valid: {}", newValue);
            }
        });
    }
}
