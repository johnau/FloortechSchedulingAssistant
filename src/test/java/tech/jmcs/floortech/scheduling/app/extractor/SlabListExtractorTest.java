package tech.jmcs.floortech.scheduling.app.extractor;

import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.DataExtractorFactory;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.SlabListExtractor;
import tech.jmcs.floortech.scheduling.app.exception.DataExtractorException;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SlabData;
import tech.jmcs.floortech.scheduling.app.types.MeasurementUnit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SlabListExtractorTest {
    private String getResourcePath() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return absolutePath;
    }

    @Test
    void testValidationSlabData() {
        Path pdfPath = Paths.get(getResourcePath(), "19383", "19383.pdf");
        SlabListExtractor extractor = DataExtractorFactory.openPdfAsSlabList(pdfPath);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            return;
        }

        Boolean valid = extractor.isValid();
        System.out.printf("File is valid: %s \n", valid.toString());
    }

    @Test
    void testVExtractSlabData() {
        Path pdfPath = Paths.get(getResourcePath(), "19383", "19383.pdf");
        Path pdfPath1 = Paths.get(getResourcePath(), "TestJob.pdf");

        extractPdfSlabData(pdfPath);
        extractPdfSlabData(pdfPath1);
    }

    private void extractPdfSlabData(Path pdfPath) {
        SlabListExtractor extractor = DataExtractorFactory.openPdfAsSlabList(pdfPath);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            fail();
        }

        try {
            extractor.extract();
        } catch (DataExtractorException e) {
            System.out.println("ERROR!");
            fail();
        }

        ExtractedTableData<SlabData> data = extractor.getData();
        Map<Long, SlabData> dataMap = data.getData();

        dataMap.forEach( (id, sd) -> {

            String name = sd.getName();
            Double size = sd.getSize();
            MeasurementUnit mUnit = sd.getMeasurementUnit();


            System.out.printf("%s : %s %s\n", name, size, mUnit.getName());
        });
    }
}