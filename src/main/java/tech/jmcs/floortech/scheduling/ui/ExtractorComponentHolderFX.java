package tech.jmcs.floortech.scheduling.ui;

import com.sun.xml.internal.ws.util.StringUtils;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.types.DataSourceExtractorType;
import tech.jmcs.floortech.scheduling.app.types.FileType;
import tech.jmcs.floortech.scheduling.app.util.PathUtilities;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExtractorComponentHolderFX {
    protected static final Logger LOG = LoggerFactory.getLogger(ExtractorComponentHolderFX.class);

    @Inject private SettingsHolder settingsHolder;

    private Map<String, DataExtractorDescriptorFX> dataExtractorMapFX;

    private Path lastPathChosen;

    public ExtractorComponentHolderFX() {
        LOG.info("ExtractorComponentHolderFX constructing...");
        this.dataExtractorMapFX = new HashMap<>();
    }

    /**
     * Get the Map of Extractor Descriptors
     * @return Map of Extractor Descriptors
     */
    public Map<String, DataExtractorDescriptorFX> getExtractorMap() {
        return dataExtractorMapFX;
    }

    /**
     * Get the Map of Active Extractor Descriptors
     * @return Map of Active Extractor Descriptors
     */
    public Map<String, DataExtractorDescriptorFX> getActiveExtractors() {
        return this.dataExtractorMapFX.entrySet().stream()
                .filter(f -> f.getValue().isEnabled())
                .collect(Collectors.toMap(
                        m -> m.getKey(),
                        m -> m.getValue())
                );
    }

    public void addAdditionalFilePathToExtractor(String name) {
        this.addAdditionalFilePathToExtractor(name, "");
    }

    public void addAdditionalFilePathToExtractor(String name, String path) {
        Optional<DataExtractorDescriptorFX> _extractor = this.dataExtractorMapFX.entrySet().stream()
                .filter(f -> f.getKey().equals(name))
                .map(m -> m.getValue())
                .findFirst();
        if (_extractor.isPresent()) {
            DataExtractorDescriptorFX extractor = _extractor.get();
            VBox filePathsVbox = extractor.getFilePathVbox();
            filePathsVbox.getChildren().add(this.buildFilePathHbox(extractor.getType(), path));
        } else {
            LOG.debug("No extractor present with the name {}", name);
        }
    }

    /**
     * Enable or disable an extractor.
     * @param name
     * @param enabled
     */
    public void setExtractorEnabled(String name, Boolean enabled) {
        Optional<DataExtractorDescriptorFX> extractor = dataExtractorMapFX.entrySet().stream()
                .filter(f -> f.getKey().toUpperCase().equals(name.toUpperCase()))
                .map(m -> m.getValue())
                .findFirst();
        if (extractor.isPresent()) {
            extractor.get().setEnabled(enabled);
        }
    }

    /**
     * Build All Extractors (present in Settings)
     */
    public void buildAll() {
        buildAllBuiltIns();
    }

    private void buildAllBuiltIns() {
        this.buildBuiltInExtractor(DataSourceExtractorType.BEAM);
        this.buildBuiltInExtractor(DataSourceExtractorType.SLAB);
        this.buildBuiltInExtractor(DataSourceExtractorType.SHEET);
        this.buildBuiltInExtractor(DataSourceExtractorType.TRUSS);
    }

    private HBox buildFilePathHbox(DataSourceExtractorType type) {
        return this.buildFilePathHbox(type, "");
    }

    private HBox buildFilePathHbox(DataSourceExtractorType type, String fieldText) {
        HBox filePathHbox = new HBox();
        filePathHbox.setPrefHeight(HBox.USE_COMPUTED_SIZE);
        filePathHbox.setPrefWidth(HBox.USE_COMPUTED_SIZE);
        filePathHbox.getStyleClass().add("row-container");

        Label fieldLabel = new Label();
        fieldLabel.setText("File path: ");
        fieldLabel.getStyleClass().add("field-label");

        TextField pathField = new TextField();
        pathField.setPromptText("eg. C:\\..\\jobs\\19000\\" + FileType.fileTypesMap.get(type.getFileType()));
        pathField.getStyleClass().add("field");
        pathField.setId("pathField");
        if (fieldText != null && !fieldText.isEmpty()) pathField.setText(fieldText);

        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            if (lastPathChosen == null) {
                Path detailingRoot = settingsHolder.getJobFoldersDetailingRootPath();
                if (detailingRoot != null && !detailingRoot.toString().isEmpty()) {
                    fileChooser.setInitialDirectory(PathUtilities.getNearestDirectory(detailingRoot));
                }
            } else {
                fileChooser.setInitialDirectory(PathUtilities.getNearestDirectory(lastPathChosen));
            }

//          fileChooser.setInitialFileName(); // TODO: Generate expected filenames and set
            FileChooser.ExtensionFilter extFilterXls = new FileChooser.ExtensionFilter(StringUtils.capitalize(type.getFileType()) + " Files", FileType.fileTypesMap.get(type.getFileType()));
            FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All Files", FileType.fileTypesMap.get("ALL"));
            fileChooser.getExtensionFilters().add(extFilterXls);
            fileChooser.getExtensionFilters().add(extFilterAll);
            fileChooser.setSelectedExtensionFilter(extFilterXls);
            fileChooser.setTitle(type.getName() + " | Choose Data File for Extractor...");

            File fileChosen = fileChooser.showOpenDialog(browseButton.getScene().getWindow());
            if (fileChosen != null) {
                String filePath = fileChosen.getPath();
                pathField.setText(filePath);
                lastPathChosen = Paths.get(filePath);
            }
        });

        Button clearButton = new Button("x");
        clearButton.setOnAction(event -> pathField.clear());

        filePathHbox.getChildren().addAll(fieldLabel, pathField, browseButton);

        return filePathHbox;
    }

    private void buildBuiltInExtractor(DataSourceExtractorType type) {
        DataExtractorDescriptorFX extractorDescriptor = new DataExtractorDescriptorFX();

        VBox vbox = new VBox();
        vbox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        vbox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        vbox.getStyleClass().add("small-padded");

        Label headerLabel = new Label();
        headerLabel.setText(type.getName() + " Extractor (Built-in)");
        headerLabel.getStyleClass().add("sub-header");

        VBox filePathsVbox = new VBox();
        filePathsVbox.setPrefHeight(HBox.USE_COMPUTED_SIZE);
        filePathsVbox.setPrefWidth(HBox.USE_COMPUTED_SIZE);

        HBox filePathHbox = buildFilePathHbox(type);
        Optional<Node> _pathField = filePathHbox.getChildren().stream().filter(f -> f.getId() != null && f.getId().equals("pathField")).findFirst();
        TextField pathField = (TextField) _pathField.get(); // no checks

        filePathsVbox.getChildren().add(filePathHbox);

        // NOTE: removed all add file functionality, multiple files of one type will not be present or needed.

//        HBox addFileButtonHbox = new HBox();
//        addFileButtonHbox.setPrefHeight(HBox.USE_COMPUTED_SIZE);
//        addFileButtonHbox.setPrefWidth(HBox.USE_COMPUTED_SIZE);
//        addFileButtonHbox.getStyleClass().add("row-container");
//
//        Button addFileButton = new Button("Add another file +");
//        addFileButton.setOnAction(event -> {
//            HBox additionalFilePathBox = this.buildFilePathHbox(type);
//            filePathsVbox.getChildren().add(additionalFilePathBox);
//        });
//
//        addFileButtonHbox.getChildren().add(addFileButton);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

//        vbox.getChildren().addAll(headerLabel, filePathsVbox, addFileButtonHbox, separator);
        vbox.getChildren().addAll(headerLabel, filePathsVbox, separator);

        extractorDescriptor.setName(type.getName());
        extractorDescriptor.setType(type);
        extractorDescriptor.setExtractorVbox(vbox);
        extractorDescriptor.setFilePathVbox(filePathsVbox);
        extractorDescriptor.setFilePathTextProperty(pathField.textProperty());
        extractorDescriptor.setCustomExtractorDetails(null);
        extractorDescriptor.setEnabled(true);

        this.dataExtractorMapFX.put(extractorDescriptor.getName(), extractorDescriptor);
//        this.enabledDataSources.put(extractorDescriptor.getName(), true);
    }

    public void clearAllFields() {
        this.dataExtractorMapFX.forEach( (name, descriptor) -> {
            descriptor.getFilePathTextProperty().set("");
            LOG.debug("Cleared file path for {}", name);
        });
    }
}
