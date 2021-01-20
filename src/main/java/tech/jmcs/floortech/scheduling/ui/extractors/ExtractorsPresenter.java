package tech.jmcs.floortech.scheduling.ui.extractors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.BeamListExtractor;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.DataExtractorFactory;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.SlabListExtractor;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.TrussListExtractor;
import tech.jmcs.floortech.scheduling.app.filesearch.FloortechDraftingFileDescriptor;
import tech.jmcs.floortech.scheduling.app.filesearch.FloortechFinder;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.types.DataSourceExtractorType;
import tech.jmcs.floortech.scheduling.app.util.PathUtilities;
import tech.jmcs.floortech.scheduling.ui.ExtractedDataHolderFX;
import tech.jmcs.floortech.scheduling.ui.ExtractorComponentHolderFX;
import tech.jmcs.floortech.scheduling.ui.QuickLookupDataHolderFX;
import tech.jmcs.floortech.scheduling.ui.StatusHolderFX;
import tech.jmcs.floortech.scheduling.ui.exceptions.SettingNotSetException;
import tech.jmcs.floortech.scheduling.ui.helper.StatusType;
import tech.jmcs.floortech.scheduling.ui.settings.SettingsPresenter;
import tech.jmcs.floortech.scheduling.ui.settings.SettingsView;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public class ExtractorsPresenter implements Initializable {
    protected static final Logger LOG = LoggerFactory.getLogger(ExtractorsPresenter.class);

    @Inject private StatusHolderFX statusHolder;
    @Inject private SettingsHolder settingsHolder;
    @Inject private ExtractedDataHolderFX extractedDataHolder;
    @Inject private ExtractorComponentHolderFX extractorHolder;
    @Inject private SettingsView settingsView;
    @Inject private QuickLookupDataHolderFX quickLookupDataHolder;

    @FXML private TextField detailingFolderPathTextfield;
    @FXML private VBox dataSourceVbox; // where data source rows are added

    private Path lastJobDetailingFolderChooserPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.debug("ExtractorsPresenter initializing...");
        setupBindingToSettings();
        setupJobNumberListener();
        toggleBuiltInExtractors();
        setupBindingToQuickLookupDataHolder();
    }

    @FXML
    public void handleAutoFillDraftingFilesButtonAction(ActionEvent event) {
        // clear all fields
        this.extractorHolder.clearAllFields();

        // get path from field
        String jobFolderPathString = this.detailingFolderPathTextfield.getText();

        // check if empty and abort
        if (jobFolderPathString.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "A valid path must be entered to Auto-Complete the extractor file locations");
            alert.setHeaderText("No path provided for Auto-Complete");
            alert.setTitle("No path provided");
            alert.showAndWait();
            return;
        }

        // check if exists and abort
        Path jobFolderPath = Paths.get(jobFolderPathString);
        if (!jobFolderPath.toFile().exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "The path provided: '" + jobFolderPathString + "', does not exist!");
            alert.setHeaderText("The provided path can not be found");
            alert.setTitle("Path not found");
            return;
        }

        // create finder
        FloortechFinder finder = new FloortechFinder(jobFolderPath, null);

        // create list of descriptors for file lookups
        List<FloortechDraftingFileDescriptor> fileDescriptorList = new ArrayList<>();

        String beamFileNameRegex = this.settingsHolder.getBeamDataFileName();
        String trussFileNameRegex = this.settingsHolder.getTrussDataFileName();
        String slabFileNameRegex = this.settingsHolder.getSlabDataFileName();
        String sheetFileNameRegex = this.settingsHolder.getSheetDataFileName();

        String beamFileExtension = this.settingsHolder.getBeamFileExtension();
        String trussFileExtension = this.settingsHolder.getTrussFileExtension();
        String slabFileExtension = this.settingsHolder.getSlabFileExtension();
        String sheetFileExtension = this.settingsHolder.getSheetFileExtension();

        FloortechDraftingFileDescriptor beamDescriptor = new FloortechDraftingFileDescriptor(DataSourceExtractorType.BEAM.getId(), beamFileNameRegex, beamFileExtension);
        FloortechDraftingFileDescriptor trussDescriptor = new FloortechDraftingFileDescriptor(DataSourceExtractorType.TRUSS.getId(), trussFileNameRegex, trussFileExtension);
        FloortechDraftingFileDescriptor slabDescriptor = new FloortechDraftingFileDescriptor(DataSourceExtractorType.SLAB.getId(), slabFileNameRegex, slabFileExtension);
        FloortechDraftingFileDescriptor sheetDescriptor = new FloortechDraftingFileDescriptor(DataSourceExtractorType.SHEET.getId(), sheetFileNameRegex, sheetFileExtension);

        boolean builtInBeamEnabled = this.settingsHolder.isBuiltInBeamExtractorEnabled();
        boolean builtInTrussEnabled = this.settingsHolder.isBuiltInTrussExtractorEnabled();
        boolean builtInSlabEnabled = this.settingsHolder.isBuiltInSlabExtractorEnabled();
        boolean builtInSheetEnabled = this.settingsHolder.isBuiltInSheetExtractorEnabled();

        if (builtInBeamEnabled) fileDescriptorList.add(beamDescriptor);
        if (builtInTrussEnabled) fileDescriptorList.add(trussDescriptor);
        if (builtInSlabEnabled) fileDescriptorList.add(slabDescriptor);
        if (builtInSheetEnabled) fileDescriptorList.add(sheetDescriptor);

        // use finder to find files, abort if error
        Map<String, List<Path>> filePathsFoundMap = null;
        try {
            filePathsFoundMap = finder.findDraftingFiles(jobFolderPath, fileDescriptorList);
        } catch (FileNotFoundException e) {

        } catch (SettingNotSetException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "The " + e.getMessage() + " setting is not set");
            alert.setTitle("Setting not set");
            alert.setHeaderText(e.getMessage() + " setting is not set");
            alert.showAndWait();
            return;
        }

        // abort if map was not returned
        if (filePathsFoundMap == null) {
            return;
        }

        // populate text fields
        filePathsFoundMap.forEach( (id, pathList) -> {
            List<Path> paths = checkFiles(pathList, id);

            if (id.equals(DataSourceExtractorType.BEAM.getId())) {
                addPathsToExtractor(DataSourceExtractorType.BEAM.getName(), paths);
            } else if (id.equals(DataSourceExtractorType.TRUSS.getId())) {
                addPathsToExtractor(DataSourceExtractorType.TRUSS.getName(), paths);
            } else if (id.equals(DataSourceExtractorType.SLAB.getId())) {
                addPathsToExtractor(DataSourceExtractorType.SLAB.getName(), paths);
            } else if (id.equals(DataSourceExtractorType.SHEET.getId())) {
                addPathsToExtractor(DataSourceExtractorType.SHEET.getName(), paths);
            }
        });
    }

    private void addPathsToExtractor(String extractorName, List<Path> paths) {
        int pathCount = paths.size();
        if (pathCount >= 1) {
            if (pathCount > 1) {
                //TODO: Alert user that there was more than one valid file detected
                String errorMsg = "There were " + pathCount + " files found that were valid for the " + extractorName + " extractor.  \\n" +
                        "The first found will be used";
                Alert alert = new Alert(Alert.AlertType.WARNING, errorMsg);
                alert.setTitle("Multiple file matches");
                alert.setHeaderText("Multiple valid file matches for the " + extractorName + " extractor");
                alert.showAndWait();
            }
            this.extractorHolder.getExtractorMap().get(extractorName).getFilePathTextProperty().set(paths.get(0).toString());


            // NOTE: Removed the functionality to add more paths for now, not needed, hopley trusses are included in the main truss list file.
//            int pathCount = paths.size();
//            if (pathCount > 1) {
//                for (int i = 1; i < pathCount; i++) {
//                    Path op = paths.get(i);
//                    this.extractorHolder.addAdditionalFilePathToExtractor(extractorName, op.toString());
//                }
//            }
        }
    }

    private List<Path> checkFiles(List<Path> potentialFiles, String extractorId) {
        List<Path> validFiles = new ArrayList<>();
        if (extractorId.equals(DataSourceExtractorType.BEAM.getId())) {
            for (Path potentialFile : potentialFiles) {
                BeamListExtractor extractor = DataExtractorFactory.openExcelFileAsBeamList(potentialFile);
                Boolean isValid = extractor.isValid();
                if (isValid) {
                    validFiles.add(potentialFile);
                } else {
                    List<String> errors = extractor.getValidationErrors();
                    for (String error : errors) {
                        this.statusHolder.addStatus(StatusType.NORMAL, error);
                    }
//                    displayValidationErrors("Built-In BEAM", errors, potentialFile);
                }
            }

        } else if (extractorId.equals(DataSourceExtractorType.TRUSS.getId())) {
            for (Path potentialFile : potentialFiles) {
                TrussListExtractor extractor = DataExtractorFactory.openExcelFileAsTrussList(potentialFile);
                Boolean isValid = extractor.isValid();
                if (isValid) {
                    validFiles.add(potentialFile);
                } else {
                    List<String> errors = extractor.getValidationErrors();
                    for (String error : errors) {
                        this.statusHolder.addStatus(StatusType.NORMAL, error);
                    }
//                    displayValidationErrors("Built-In TRUSS", errors, potentialFile);
                }
            }

        } else if (extractorId.equals(DataSourceExtractorType.SLAB.getId())) {
            for (Path potentialFile : potentialFiles) {
                SlabListExtractor extractor = DataExtractorFactory.openPdfAsSlabList(potentialFile);
                Boolean isValid = extractor.isValid();
                if (isValid) {
                    validFiles.add(potentialFile);
                } else {
                    List<String> errors = extractor.getValidationErrors();
                    for (String error : errors) {
                        this.statusHolder.addStatus(StatusType.NORMAL, error);
                    }
//                    displayValidationErrors("Built-In SLAB", errors, potentialFile);
                }
            }

        } else if (extractorId.equals(DataSourceExtractorType.SHEET.getId())) {
            //TODO: Handle sheet list with general extractor.  Needs to be placed in a stand alone class to be accessed.
//            for (Path potentialFile : potentialFiles) {
//
//                Boolean isValid = extractor.isValid();
//                if (isValid) {
//                    return potentialFile;
//                }
//            }

        }
        return validFiles;
    }

    private void displayValidationErrors(String extractorName, List<String> errors, Path potentialFile) {
        StringBuilder sb = new StringBuilder();
        int c = 0;
        sb.append("The following ");
        sb.append(errors.size());
        sb.append(" problems were found while validating the data file: '");
        sb.append(potentialFile.getFileName().toString());
        sb.append("' for extractor: '");
        sb.append(extractorName);
        sb.append("'\n\n");
        for (String e : errors) {
            c++;
            sb.append(c);
            sb.append(". ");
            sb.append(e);
            sb.append("\n");
        }
        sb.append("\nPlease fix the problems and try again.");

        Alert alert = new Alert(Alert.AlertType.WARNING, sb.toString());
        alert.setTitle("Issue with file detected");
        alert.setHeaderText("A file was found by filename pattern but the contents were not valid.");
        alert.showAndWait();

        //TODO: Fix this to only show some types of errors? or only display if a file could not be found at all?

    }

    @FXML
    public void handleAddExtractorButtonAction(ActionEvent event) {
        LOG.info("Handle Add Data Source Button Action Not Yet Implemented");
    }

    @FXML
    public void handleBrowseJobFolderButtonAction(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();

        chooser.setTitle("Choose Job Detailing Folder...");

        Path detailingRoot = this.settingsHolder.getJobFoldersDetailingRootPath();

        if (this.lastJobDetailingFolderChooserPath != null) { // set the last path used if exists
            chooser.setInitialDirectory(PathUtilities.getNearestDirectory(this.lastJobDetailingFolderChooserPath));
        } else if (detailingRoot != null && !detailingRoot.toString().isEmpty()) { // else set from settings
            chooser.setInitialDirectory(PathUtilities.getNearestDirectory(detailingRoot));
        }

        File dirChosen = chooser.showDialog(this.dataSourceVbox.getScene().getWindow());
        if (dirChosen != null) {
            this.detailingFolderPathTextfield.setText(dirChosen.getPath());
            this.lastJobDetailingFolderChooserPath = dirChosen.toPath();
        }
    }

    /**
     * Private method to add a change listener to the SettingsHolder's lastUpdated property.
     * This allows this view to react to changes in settings.
     */
    private void setupBindingToSettings() {
        SettingsPresenter settingsPresenter = (SettingsPresenter) settingsView.getPresenter();
        Consumer<Boolean> onSettingsSaveConsumer = aBoolean -> {
            toggleBuiltInExtractors();
        };
        settingsPresenter.addListenerOnSave(onSettingsSaveConsumer);
//        settingsHolder.getLastUpdatedProperty().addListener((observable, oldDate, newDate) -> {
//            if (newDate.after(oldDate)) toggleBuiltInExtractors();
//        });
    }

    /**
     * Toggles activated extractors based on settings
     */
    private void toggleBuiltInExtractors() {
        Boolean beamExtractorEnabled = this.settingsHolder.isBuiltInBeamExtractorEnabled();
        Boolean sheetExtractorEnabled = this.settingsHolder.isBuiltInSheetExtractorEnabled();
        Boolean slabExtractorEnabled = this.settingsHolder.isBuiltInSlabExtractorEnabled();
        Boolean trussExtractorEnabled = this.settingsHolder.isBuiltInTrussExtractorEnabled();

        VBox beamVbox = this.extractorHolder.getExtractorMap().get(DataSourceExtractorType.BEAM.getName()).getExtractorVbox();
        VBox sheetVbox = this.extractorHolder.getExtractorMap().get(DataSourceExtractorType.SHEET.getName()).getExtractorVbox();
        VBox slabVbox = this.extractorHolder.getExtractorMap().get(DataSourceExtractorType.SLAB.getName()).getExtractorVbox();
        VBox trussVbox = this.extractorHolder.getExtractorMap().get(DataSourceExtractorType.TRUSS.getName()).getExtractorVbox();

        if (beamExtractorEnabled) {// add the extractor to the view if not present
            Optional<Node> existing = this.dataSourceVbox.getChildren().stream().filter(f -> f.equals(beamVbox)).findFirst();
            if (!existing.isPresent()) this.dataSourceVbox.getChildren().add(beamVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.BEAM.getName(), true);
        } else { // remove the extractor from the view
            this.dataSourceVbox.getChildren().remove(beamVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.BEAM.getName(), false);
        }

        if (sheetExtractorEnabled) {
            Optional<Node> existing = this.dataSourceVbox.getChildren().stream().filter(f -> f.equals(sheetVbox)).findFirst();
            if (!existing.isPresent()) this.dataSourceVbox.getChildren().add(sheetVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.SHEET.getName(), true);
        } else {
            this.dataSourceVbox.getChildren().remove(sheetVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.SHEET.getName(), false);
        }

        if (slabExtractorEnabled) {
            Optional<Node> existing = this.dataSourceVbox.getChildren().stream().filter(f -> f.equals(slabVbox)).findFirst();
            if (!existing.isPresent()) this.dataSourceVbox.getChildren().add(slabVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.SLAB.getName(), true);
        } else {
            this.dataSourceVbox.getChildren().remove(slabVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.SLAB.getName(), false);
        }

        if (trussExtractorEnabled) {
            Optional<Node> existing = this.dataSourceVbox.getChildren().stream().filter(f -> f.equals(trussVbox)).findFirst();
            if (!existing.isPresent()) this.dataSourceVbox.getChildren().add(trussVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.TRUSS.getName(), true);
        } else {
            this.dataSourceVbox.getChildren().remove(trussVbox);
            this.extractorHolder.setExtractorEnabled(DataSourceExtractorType.TRUSS.getName(), false);
        }
    }

    /**
     * Private method to setup listener on the Job Number TextField for automatic lookup.
     * Change an icon when job number is valid and ready for lookup.
     */
    private void setupJobNumberListener() {
//        jobNumberTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (FloortechHelper.isValidJobNumber(newValue)) {
//                LOG.debug("Lookup job number: {}", newValue);
//                // TODO: Implement as a notification that files and folders have been located (async lookup)
//            } else {
//                LOG.debug("Job number entered is not valid: {}", newValue);
//            }
//        });
    }

    private void setupBindingToQuickLookupDataHolder() {
        quickLookupDataHolder.jobFolderProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.toString().isEmpty()) {
                this.detailingFolderPathTextfield.setText(newValue.toString());
            }
        });
    }
}