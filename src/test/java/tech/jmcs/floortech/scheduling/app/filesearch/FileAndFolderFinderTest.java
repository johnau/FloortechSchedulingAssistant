package tech.jmcs.floortech.scheduling.app.filesearch;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FileAndFolderFinderTest {

    @Test
    void findFilesByNameWithRegexTest() {

        FileAndFolderFinder finder = new FileAndFolderFinder();

        Map<String, String> fileRegexPatterns = new HashMap<>();
        String xlsExt = "[.](?i)xls(?-i)";
        String pdfExt = "[.](?i)pdf(?-i)";
        fileRegexPatterns.put("truss.builtin", "[\\w\\s\\-\\,\\(\\)\\[\\]#.]*" + "(?i)- 17974(?-i)" + xlsExt);
        fileRegexPatterns.put("beam.builtin_1", "(?i)beam listing(?-i)[\\s]*[0-9]{5}" + xlsExt);
        fileRegexPatterns.put("beam.builtin_2", "(?i)beam listing 17974(?-i)" + xlsExt);
        fileRegexPatterns.put("slab.builtin", "^(?i)17974(?-i)" + pdfExt);
//        fileRegexPatterns.add("(?i)sheet listing(?-i)[\\w\\-. ]*[.][\\w]{1,5}");

        Path jobFolder = Paths.get("D:\\appdev\\floortech_env\\Floortech\\drafting\\jobs\\1DETAILING\\FLOORTECH JULY'19-ON\\17974");
        if (!jobFolder.toFile().exists()) {
            return;
        }

        Map<String, List<Path>> result = finder.findFilesByNameWithRegex(jobFolder, fileRegexPatterns);

        result.forEach( (fileName, foundFiles) -> {
            System.out.printf("%s files matching %s: \n", foundFiles.size(), fileName);
            for (Path foundFile : foundFiles) {
                System.out.println(" " + fileName + " : " + foundFile);
            }
        });
    }

    @Test
    void findFolderByName() {
        FileAndFolderFinder finder = new FileAndFolderFinder();
        Path target = Paths.get("D:\\appdev\\floortech_env\\Floortech\\drafting\\jobs\\1DETAILING");
        if (!target.toFile().exists()) {
            return;
        }

        Path result = finder.findFolderByName(target, "17991");
        System.out.printf("Found path: %s \n", result.toString());
    }
}