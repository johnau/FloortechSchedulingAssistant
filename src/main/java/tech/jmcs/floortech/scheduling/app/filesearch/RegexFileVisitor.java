package tech.jmcs.floortech.scheduling.app.filesearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class RegexFileVisitor extends SimpleFileVisitor<Path> {
    protected static final Logger LOG = LoggerFactory.getLogger(RegexFileVisitor.class);

    private final PathMatcher pathMatcher;
    private List<Path> matches;

    public RegexFileVisitor(String pattern) {
        pathMatcher = FileSystems.getDefault().getPathMatcher("regex:" + pattern);
        matches = new ArrayList<>();
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttr) {
        if (pathMatcher.matches(path.getFileName())) {
            matches.add(path);
            LOG.debug(path.toString());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttr) {
        if (pathMatcher.matches(path.getFileName())) {
        }
        return FileVisitResult.CONTINUE;
    }

    public List<Path> getMatches() {
        return matches;
    }
}