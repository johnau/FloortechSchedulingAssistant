package tech.jmcs.floortech.scheduling.app.extractor;

import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.DataExtractorFactory;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.TrussListExtractor;
import tech.jmcs.floortech.scheduling.app.exception.DataExtractorException;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.datasource.model.TrussData;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

class TrussListExtractorTest {
    private String getResourcePath() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return absolutePath;
    }

    /**
     * Test for checking if truss list is valid format / layout
     */
    @Test
    void testValidateTrussList() {
        Path excelFile = Paths.get(getResourcePath(), "19383", "LOT 5017 JOSEPH BANK BOULEVARD, BANKSIA GROVE - 19383_EXTRA.xls");
        TrussListExtractor extractor = DataExtractorFactory.openExcelFileAsTrussList(excelFile);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            return;
        }

        Boolean valid = extractor.isValid();
        System.out.printf("File is valid: %s \n", valid.toString());
    }

    /**
     * Test for checking if truss list is valid format / layout
     */
    @Test
    void testValidateTrussList3() {
        Path excelFile = Paths.get(getResourcePath(), "Truss List With Hopley.XLS");
        TrussListExtractor extractor = DataExtractorFactory.openExcelFileAsTrussList(excelFile);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            return;
        }

        Boolean valid = extractor.isValid();
        System.out.printf("File is valid: %s \n", valid.toString());
    }

    @Test
    void testExtractTrussListData() {
        Path excelFile = Paths.get(getResourcePath(), "19383", "LOT 5017 JOSEPH BANK BOULEVARD, BANKSIA GROVE - 19383.xls");
//        Path excelFile = Paths.get("D:\\appdev\\floortech_env\\Floortech\\drafting\\jobs\\1DETAILING\\FLOORTECH JULY'19-ON\\17974\\LOT 246 (#22) TRINGA CIRCLE, WATERFORD - 17974.XLS");
//        Path excelFile = Paths.get("D:\\appdev\\floortech_env\\Floortech\\drafting\\jobs\\1DETAILING\\FLOORTECH JULY'19-ON\\17974\\LOT 246 (#22) TRINGA CIRCLE, WATERFORD - 17974.XLS");
        TrussListExtractor extractor = DataExtractorFactory.openExcelFileAsTrussList(excelFile);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            return;
        }

        try {
            extractor.extract();
        } catch (DataExtractorException e) {
            System.out.println("Could not extract Truss List Data");
            return;
        }

        ExtractedTableData<TrussData> data = extractor.getData();
        Map<Long, TrussData> dataMap = data.getData();
        dataMap.forEach((id, td) -> {
            String trussId = td.getTrussId();
            List<Integer> webCuts = td.getPenetrationWebCuts();
            String webCutsStr = "";
            if (webCuts.size() == 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Web cuts: ");
                sb.append(webCuts.get(0));
                sb.append("+");
                sb.append(webCuts.get(1));
                webCutsStr = sb.toString();
            }

            boolean hasPeno = td.hasAirconPenetration();
            String leftEc = td.getLeftEndcap().getName();
            Long length = td.getLength();
            Long qty = td.getQty();
            String rightEc = td.getRightEndcap().getName();
            String type = td.getType();
//            td.getPackingGroup();

            System.out.printf("%d | %s [%s] %d %dmm L:%s R:%s | Peno: %s, %s \n", id, trussId, type, qty, length, leftEc, rightEc, hasPeno, webCutsStr);
        });

    }

    @Test
    void testExtractTrussListData2() {
        System.out.println("NEED TO IMPLEMENT THIS TEST");

        // test for new truss list with packing group

    }

    @Test
    void testTrussListTypeDetect() {

    }

        /**
     * Test for checking if truss list is valid format / layout (For new truss list with Truss Packing Group)
     */
    @Test
    void testValidateTrussList2() {
        Path excelFile = Paths.get(getResourcePath(), "19383", "LOT 5017 JOSEPH BANK BOULEVARD, BANKSIA GROVE - 19383_EXTRA.xls");
        TrussListExtractor extractor = DataExtractorFactory.openExcelFileAsTrussList2(excelFile);
        if (extractor == null) {
            System.out.println("Could not create extractor, is the file in open in excel? does it exist?");
            return;
        }

        Boolean valid = extractor.isValid();
        System.out.printf("File is valid: %s \n", valid.toString());

//        extractor.closeFile();
    }

}