package tech.jmcs.floortech.scheduling.app.filesearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderNameFileVisitor extends SimpleFileVisitor<Path> {
    protected static final Logger LOG = LoggerFactory.getLogger(RegexFileVisitor.class);

    private final PathMatcher pathMatcher;

    private Path match;

    public FolderNameFileVisitor(String name) {
        pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + name);
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttr) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttr) {
        if (pathMatcher.matches(path.getFileName())) {
            match = path;
            return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
    }

    public Path getMatch() {
        return match;
    }
}