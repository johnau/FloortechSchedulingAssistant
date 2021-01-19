package tech.jmcs.floortech.scheduling.ui.helper;

import javafx.scene.control.Label;
import tech.jmcs.floortech.scheduling.app.types.DataSourceExtractorType;

public class ExtractorHelper {

    private final DataSourceExtractorType type;

    public ExtractorHelper(DataSourceExtractorType type) {
        this.type = type;
    }

    public Label buildHeaderLabel() {
        Label headerLabel = new Label();
        headerLabel.setText(type.getName() + " Extractor (Built-in)");
        headerLabel.getStyleClass().add("sub-header");
        return headerLabel;
    }


}
