package tech.jmcs.floortech.scheduling.app.types;

public enum DataSourceExtractorType {
    TRUSS ("Truss", "EXCEL", "truss.built_in"),
    SLAB ("Slab", "PDF", "slab.built_in"),
    BEAM ("Beam", "EXCEL", "beam.built_in"),
    SHEET ("Sheet", "EXCEL", "sheet.built_in"),
    GENERIC_SIMPLE ("Generic", "*", "generic"); // generics get their own id... "generic.<name>"

    private final String name;
    private final String fileType;
    private final String id;

    DataSourceExtractorType(String niceName, String fileType, String id) {

        this.name = niceName;
        this.fileType = fileType;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getFileType() {
        return fileType;
    }

    public String getId() {
        return id;
    }
}
