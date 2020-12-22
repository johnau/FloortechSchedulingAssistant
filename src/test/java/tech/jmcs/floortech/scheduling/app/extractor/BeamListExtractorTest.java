package tech.jmcs.floortech.scheduling.app.extractor;

import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.BeamListExtractor;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.DataExtractorFactory;
import tech.jmcs.floortech.scheduling.app.exception.DataExtractorException;
import tech.jmcs.floortech.scheduling.app.datasource.model.BeamData;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

class BeamListExtractorTest {

    private String getResourcePath() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return absolutePath;
    }

    /**
     * Test if a beam list is a valid format / layout
     */
    @Test
    public void testValidateBeamList() {
        Path excelFile = Paths.get(getResourcePath(), "19383", "Beam Listing 19383.xls");
        BeamListExtractor extractor = DataExtractorFactory.openExcelFileAsBeamList(excelFile);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            return;
        }

        Boolean valid = extractor.isValid();
        System.out.printf("File is valid: %s \n", valid.toString());
    }

    @Test
    public void testExtractBeamData() {
        Path excelFile = Paths.get(getResourcePath(), "19383", "Beam Listing 19383.xls");
        BeamListExtractor extractor = DataExtractorFactory.openExcelFileAsBeamList(excelFile);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            return;
        }

        try {
            extractor.extract();
        } catch (DataExtractorException e) {
            System.out.printf("Data Extractor Exception thrown: [%s] %s \n",  e.getDataSourceName(), e.getMessage());
        }

        ExtractedTableData<BeamData> data = extractor.getData();
        if (data == null) {
            System.out.println("Extraction was not be completed");
            return;
        }

        Map<Long, BeamData> dataMap = data.getData();
        dataMap.forEach( (id, bd) -> {
            String beamId = bd.getBeamId();
            String beamType = bd.getBeamType();
            Long beamLength = bd.getLength();
            Long beamQuantity = bd.getQuantity();

            System.out.printf("%d | %s ( %s ) x %d : %dmm \n", id, beamId, beamType, beamQuantity, beamLength);

        });

    }
}