package tech.jmcs.floortech.scheduling.ui.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.ExtractedDataToScheduleConverter;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.settings.SettingsLoader;
import tech.jmcs.floortech.scheduling.app.settings.SettingsWriter;
import tech.jmcs.floortech.scheduling.ui.*;
import tech.jmcs.floortech.scheduling.ui.commitbutton.CommitButtonView;
import tech.jmcs.floortech.scheduling.ui.commitbutton.addrowchoices.AddRowChoicesView;
import tech.jmcs.floortech.scheduling.ui.commitbutton.conflictchoices.ConflictChoicesView;
import tech.jmcs.floortech.scheduling.ui.commitbutton.nomatchchoices.NoMatchChoicesView;
import tech.jmcs.floortech.scheduling.ui.dataframe.DataFrameView;
import tech.jmcs.floortech.scheduling.ui.datatarget.DataTargetView;
import tech.jmcs.floortech.scheduling.ui.extractbutton.ExtractButtonView;
import tech.jmcs.floortech.scheduling.ui.extractors.ExtractorsView;
import tech.jmcs.floortech.scheduling.ui.helper.StatusType;
import tech.jmcs.floortech.scheduling.ui.quicklookup.QuickLookupView;
import tech.jmcs.floortech.scheduling.ui.settings.SettingsView;
import tech.jmcs.floortech.scheduling.ui.settingsbutton.SettingsButtonView;
import tech.jmcs.floortech.scheduling.ui.status.StatusView;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(DashboardPresenter.class);

    /**
     * All container held classes injected here to preload
     */
    @Inject private SettingsView settingsView;
    @Inject private SettingsButtonView settingsButtonView;
    @Inject private QuickLookupView quickLookupView;
    @Inject private ExtractorsView extractorsView;
    @Inject private ExtractButtonView extractButtonView;
    @Inject private DataTargetView dataTargetView;
    @Inject private CommitButtonView commitButtonView;
    @Inject private DataFrameView dataFrameView;
    @Inject private NoMatchChoicesView noMatchChoiceView;
    @Inject private ConflictChoicesView conflictChoicesView;
    @Inject private AddRowChoicesView addRowChoicesView;
    @Inject private StatusView statusView;

    @Inject private SettingsLoader settingsLoader;
    @Inject private SettingsWriter settingsWriter;
    @Inject private SettingsHolder settingsHolder;
    @Inject private ExtractedDataToScheduleConverter extractedDataConverter;
    @Inject private ExtractorComponentHolderFX extractorHolder;
    @Inject private ExtractorManagerFX extractorManager;
    @Inject private ExtractedDataHolderFX extractedDataHolder;
    @Inject private DataTargetHolder targetHolder;
    @Inject private QuickLookupDataHolderFX quickLookupDataHolder;
    @Inject private StatusHolderFX statusHolder;

    @FXML private VBox leftPanelVbox; // left panel
    @FXML private VBox leftVbox1;
    @FXML private VBox leftVbox2;
    @FXML private VBox leftVbox3;
    @FXML private VBox leftVbox4;
    @FXML private VBox leftVbox5;
    @FXML private AnchorPane rightPanelAnchorPane; // right panel
    @FXML private HBox toolbarHbox; // top right panel (alongside header / title)
    @FXML private AnchorPane statusContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) { // resource bundle populated from *.properties
        LOG.info("DashboardPresenter initializing...");
        loadSettings();
        buildExtractorViews(); // call after settings are loaded
        setupDashboard();
        this.statusHolder.addStatus(StatusType.NORMAL, "Loaded Floortech Data Scraper.");
    }

    private void loadSettings() {
        try {
            this.settingsLoader.load();
        } catch (IOException e) {
            LOG.warn("Could not load settings: {}", e.getMessage());
        }
    }

    private void buildExtractorViews() {
        this.extractorHolder.buildAll();
    }

    private void setupDashboard() {
        settingsButtonView.getViewAsync(this.toolbarHbox.getChildren()::add);
        quickLookupView.getViewAsync(this.leftVbox1.getChildren()::add);
        extractorsView.getViewAsync(this.leftVbox2.getChildren()::add);
        extractButtonView.getViewAsync(this.leftVbox3.getChildren()::add);
        dataTargetView.getViewAsync(this.leftVbox4.getChildren()::add);
        commitButtonView.getViewAsync(this.leftVbox5.getChildren()::add);
        dataFrameView.getViewAsync(this.rightPanelAnchorPane.getChildren()::add);
        statusView.getViewAsync(this.statusContainer.getChildren()::add);
    }

}