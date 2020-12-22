package tech.jmcs.floortech.scheduling.app.datasource.converter;

import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.GenericExtractorColumnDataType;
import tech.jmcs.floortech.scheduling.app.datasource.extractor.GenericExtractorColumnDescription;
import tech.jmcs.floortech.scheduling.app.datasource.model.BeamData;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.types.BeamTreatment;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BeamDataConverterTest {

    @Test
    void testConvert() {
        BeamDataConverter converter = new BeamDataConverter(null);

//        ExtractedTableData<BeamData> beamData = new ExtractedTableData<>("BEAM");
//        Map<String, GenericExtractorColumnDataType> colTypes = new HashMap<>();

//        colTypes.put("Beam Type", GenericExtractorColumnDataType.TEXT);
//        colTypes.put("Quantity", GenericExtractorColumnDataType.NUMERIC);
//        colTypes.put("ID", GenericExtractorColumnDataType.TEXT);
//        colTypes.put("Length", GenericExtractorColumnDataType.NUMERIC);
//
//        beamData.setColumnTypes(colTypes);

        BeamData bd1 = createBeam("250 UB 26", "B01", 1000L, 1L, BeamTreatment.BLACK);
        BeamData bd2 = createBeam("250 UB 26", "B02", 2000L, 1L, BeamTreatment.BLACK);
        BeamData bd3 = createBeam("250 UB 26", "B03", 1000L, 1L, BeamTreatment.GALVANISED);
        BeamData bd4 = createBeam("250 UB 37", "B04", 1000L, 1L, BeamTreatment.GALVANISED);
        BeamData bd5 = createBeam("250 UB 37", "B05", 2000L, 1L, BeamTreatment.GALVANISED);

//        beamData.addData(bd1);
//        beamData.addData(bd2);
//        beamData.addData(bd3);
//        beamData.addData(bd4);
//        beamData.addData(bd5);

        Map<Long, BeamData> beamData = new HashMap<>();
        beamData.put(1L, bd1);
        beamData.put(2L, bd2);
        beamData.put(3L, bd3);
        beamData.put(4L, bd4);
        beamData.put(5L, bd5);

        Map<String, Object> result = converter.convert(beamData);
        result.forEach( (s, o) -> {

            System.out.printf("Result: %s : %s", s, o);

        });
    }

    private BeamData createBeam(String type, String id, Long length, Long qty, BeamTreatment treatment) {
        BeamData bd = new BeamData(type);
        bd.setBeamId(id);
        bd.setLength(length);
        bd.setQuantity(qty);
        bd.setTreatment(treatment);
        return bd;
    }
}