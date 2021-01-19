package tech.jmcs.floortech.scheduling.app.filesearch;

public class FloortechDraftingFileDescriptor {

    private String id;
    private String regexFileNamePattern;
    private String extension;
    public FloortechDraftingFileDescriptor(String id, String regexFileNamePattern, String extension) {
        this.id = id;
        this.regexFileNamePattern = regexFileNamePattern;
        this.extension = extension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegexFileNamePattern() {
        return regexFileNamePattern;
    }

    public void setRegexFileNamePattern(String regexFileNamePattern) {
        this.regexFileNamePattern = regexFileNamePattern;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
