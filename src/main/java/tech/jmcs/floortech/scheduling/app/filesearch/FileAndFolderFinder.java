package tech.jmcs.floortech.scheduling.app.filesearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAndFolderFinder {
    protected static final Logger LOG = LoggerFactory.getLogger(FileAndFolderFinder.class);

    public FileAndFolderFinder() {
    }

    /**
     *
     */
    public Path findFolderByName(Path targetRootFolder, String name) {
        FolderNameFileVisitor fileVisitor = new FolderNameFileVisitor(name);
        try {
            Files.walkFileTree(targetRootFolder, fileVisitor);
            Path match = fileVisitor.getMatch();
            if (match != null) {
                LOG.debug("Found path: {}", match.toString());
                return match;
            }
        } catch (IOException e) {
            LOG.debug("Could not access folder");
        }
        
        return null;
    }


    public Map<String, List<Path>> findFilesByNameWithRegex(Path targetFolder, Map<String, String> fileNameRegexPatterns) {
        boolean folderExists = targetFolder.toFile().exists();
        if (!folderExists) {
            LOG.debug("Could not find files, path provided does not exist");
            return null;
        }

        Map<String, List<Path>> files = new HashMap<>();

        fileNameRegexPatterns.forEach( (id, rx) -> {
            List<Path> foundFiles;
            try (Stream<Path> stream = Files.list(targetFolder)) {
                Pattern pattern = Pattern.compile(rx);

                foundFiles = stream
                        .filter(file -> file.toFile().isFile())
                        .filter(file -> {
                            Matcher matcher = pattern.matcher(file.getFileName().toString());
                            boolean res = matcher.find();
                            return res;
                        })
                        .map(Path::toAbsolutePath)
                        .collect(Collectors.toList());
            } catch (IOException ex) {
                return;
            }

            List<Path> existing = files.putIfAbsent(id, foundFiles);
            if (existing != null) {
                LOG.debug("Duplicate ignored during drafting file find!");
            }
        });

        return files;

        //TODO: Handle custom extractors...
    }

}
