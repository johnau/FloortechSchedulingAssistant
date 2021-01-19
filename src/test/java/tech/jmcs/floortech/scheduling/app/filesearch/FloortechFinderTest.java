package tech.jmcs.floortech.scheduling.app.filesearch;

import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.ui.exceptions.SettingNotSetException;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FloortechFinderTest {

    @Test
    void findDraftingFiles() {
        Path jobDetailingRoot = Paths.get("D:\\appdev\\floortech_env\\Floortech\\drafting\\jobs\\1DETAILING");
        Path jobFolderPath = Paths.get("D:\\appdev\\floortech_env\\Floortech\\drafting\\jobs\\1DETAILING\\FLOORTECH JULY'19-ON\\17978");
        FloortechFinder finder = new FloortechFinder(jobDetailingRoot,null);
        List<FloortechDraftingFileDescriptor> descriptors = new ArrayList<>();
        FloortechDraftingFileDescriptor beamDescriptor = new FloortechDraftingFileDescriptor("beam.test", "beam listing <#f>", "xls");
        FloortechDraftingFileDescriptor trussDescriptor = new FloortechDraftingFileDescriptor("truss.test", "* <#f>", "xls");
        FloortechDraftingFileDescriptor slabDescriptor = new FloortechDraftingFileDescriptor("slab.test", "!<#f>", "pdf");
        descriptors.add(beamDescriptor);
        descriptors.add(trussDescriptor);
        descriptors.add(slabDescriptor);

        try {
            Map<String, List<Path>> result = finder.findDraftingFiles(jobFolderPath, descriptors);
            result.forEach( (id, paths) -> {
                System.out.printf("ID: %s has %s paths:   ", id, paths.size());
                for (Path path : paths) {
                    System.out.printf("%s, ", path);
                }
                System.out.println("");
            });
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            fail();
        } catch (SettingNotSetException e) {
            System.out.println(e.getMessage());
            fail();
        }

    }
}