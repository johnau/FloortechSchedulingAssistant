package tech.jmcs.floortech.scheduling.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.nio.file.Path;
import java.util.Map;

public class QuickLookupDataHolderFX {

    private final ObjectProperty<Path> jobFolder = new SimpleObjectProperty<Path>(null, "jobFolder");
    private final ObservableMap<String, Path> draftingFilesMap = FXCollections.observableHashMap();

    public QuickLookupDataHolderFX() {
    }

    public Path getJobFolder() {
        return this.jobFolder.get();
    }

    public void setJobFolder(Path jobFolder) {
        this.jobFolder.set(jobFolder);
    }

    public ObjectProperty<Path> jobFolderProperty() {
        return jobFolder;
    }

    public Map<String, Path> getDraftingFilesMap() {
        return draftingFilesMap;
    }

    public void addDraftingFile(String id, Path filePath) {
        this.draftingFilesMap.put(id, filePath);
    }

    public void setDraftingFilesMap(Map<String, Path> draftingFilesMap) {
        this.draftingFilesMap.clear();
        this.draftingFilesMap.putAll(draftingFilesMap);
    }

    public ObservableMap<String, Path> draftingFilesMapProperty() {
        return draftingFilesMap;
    }
}
