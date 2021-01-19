package tech.jmcs.floortech.scheduling.app.filesearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.ui.exceptions.SettingNotSetException;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloortechFinder {
    protected static final Logger LOG = LoggerFactory.getLogger(FloortechFinder.class);

    private static final String MATCH_ANY_VALID_FILENAME_CHARACTER_PATTERN = "([^\\<\\>\\:\\\"\\/\\\\\\|\\?\\*]*)";

    private final Path jobFoldersDetailingRoot;
    private final Path jobFilesSchedulingRoot;

    public FloortechFinder(Path jobFoldersDetailingRoot, Path jobFilesSchedulingRoot) {
        this.jobFoldersDetailingRoot = jobFoldersDetailingRoot;
        this.jobFilesSchedulingRoot = jobFilesSchedulingRoot;
    }

    public Path findJobFolder(String jobNumber) throws FileNotFoundException, SettingNotSetException {
        if (this.jobFoldersDetailingRoot == null) {
            throw new SettingNotSetException("Detailing Root Path");
        }

        FileAndFolderFinder finder = new FileAndFolderFinder();

        if (this.jobFoldersDetailingRoot.toString().isEmpty() || !this.jobFoldersDetailingRoot.toFile().exists()) {
            throw new FileNotFoundException(this.jobFoldersDetailingRoot.toString());
        }
        Path foundPath = finder.findFolderByName(this.jobFoldersDetailingRoot, jobNumber);

        return foundPath;
    }

    public Map<String, List<Path>> findDraftingFiles(Path jobFolderPath, List<FloortechDraftingFileDescriptor> fileDescriptorList) throws FileNotFoundException, SettingNotSetException {
        if (this.jobFoldersDetailingRoot == null) {
            throw new SettingNotSetException("Detailing Root Path");
        }

        FileAndFolderFinder finder = new FileAndFolderFinder();
        Map<String, String> fileRegexMap = new HashMap<>();
        fileDescriptorList.forEach( d -> {
            String extractorId = d.getId();
            String ext = d.getExtension();
            String pattern = d.getRegexFileNamePattern();

            LOG.debug("Pattern before: {}", pattern);


            pattern = pattern.replaceAll("[\\*]", MATCH_ANY_VALID_FILENAME_CHARACTER_PATTERN);
            pattern = pattern.replaceAll("[\\<][\\#][fF][\\>]", jobFolderPath.getFileName().toString());
            pattern = pattern.replaceAll("^[!]", "^");
            pattern = "(?i)" + pattern + "(?-i)";

            LOG.debug("Pattern after: {}", pattern);

            StringBuilder string = new StringBuilder();
            string.append(pattern);
            string.append("[.](?i)");
            string.append(ext.toLowerCase());
            string.append("(?-i)");

            LOG.debug("Full pattern: {}", string.toString());

            fileRegexMap.put(extractorId, string.toString());
        });

        Map<String, List<Path>> result = finder.findFilesByNameWithRegex(jobFolderPath, fileRegexMap);

        return result;
    }

}
