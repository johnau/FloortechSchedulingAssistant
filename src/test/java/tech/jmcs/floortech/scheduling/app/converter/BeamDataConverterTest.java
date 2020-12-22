package tech.jmcs.floortech.scheduling.app.converter;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.app.datasource.converter.BeamDataConverter;
import tech.jmcs.floortech.scheduling.app.datasource.model.BeamData;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.types.BeamTreatment;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Deprecated
class BeamDataConverterTest {

//    @Test
    void testConvertBeamData() {
//        BeamDataConverter converter = new BeamDataConverter();
//
//        ExtractedTableData<BeamData> mockData = new ExtractedTableData<>("BEAM LISTING");
//
//        BeamData mockBeam1 = new BeamData("250 UB 26");
//        mockBeam1.setBeamId("B01");
//        mockBeam1.setLength(501L);
//        mockBeam1.setQuantity(1L);
//
//        BeamData mockBeam2 = new BeamData("250 UB 26");
//        mockBeam2.setBeamId("B02");
//        mockBeam2.setLength(1054L);
//        mockBeam2.setQuantity(1L);
//
//        BeamData mockBeam3 = new BeamData("250 UB 26");
//        mockBeam3.setBeamId("B03");
//        mockBeam3.setLength(3003L);
//        mockBeam3.setQuantity(1L);
//        mockBeam3.setTreatment(BeamTreatment.GALVANISED);
//
//        BeamData mockBeam4 = new BeamData("250 UC 89");
//        mockBeam4.setBeamId("B04");
//        mockBeam4.setLength(4004L);
//        mockBeam4.setQuantity(1L);
//
//        BeamData mockBeam5 = new BeamData("310 UB 40");
//        mockBeam5.setBeamId("B05");
//        mockBeam5.setLength(5005L);
//        mockBeam5.setQuantity(1L);
//
//        BeamData mockBeam6 = new BeamData("RHS 75x50x1.6");
//        mockBeam6.setBeamId("BB01");
//        mockBeam6.setLength(5000L);
//        mockBeam6.setQuantity(10L);
//
//        BeamData mockBeam7 = new BeamData("250 UC 89");
//        mockBeam7.setBeamId("B06");
//        mockBeam7.setLength(6097L);
//        mockBeam7.setQuantity(1L);
//
//        mockData.addData(mockBeam1);
//        mockData.addData(mockBeam2);
//        mockData.addData(mockBeam3);
//        mockData.addData(mockBeam4);
//        mockData.addData(mockBeam5);
//        mockData.addData(mockBeam6);
//        mockData.addData(mockBeam7);
//
//        Map<String, Object> converted = converter.convert(mockData);
//
//        converted.forEach( (name, val) -> {
//            double rounded = 0d;
//            switch (name) {
//                case "250 UB 26 GALVANISED":
//                    rounded = Precision.round((Double) val,3);
//                    assertEquals(rounded, 3.003d);
//                    break;
//                case "250 UB 26 BLACK":
//                    rounded = Precision.round((Double) val,3);
//                    assertEquals(rounded, 1.555d);
//                    break;
//                case "310 UB 40 BLACK":
//                    rounded = Precision.round((Double) val,3);
//                    assertEquals(rounded, 5.005d);
//                    break;
//                case "RHS 75X50X1.6 BLACK":
//                    rounded = Precision.round((Double) val,3);
//                    assertEquals(rounded, 50.000d);
//                    break;
//                case "250 UC 89 BLACK":
//                    rounded = Precision.round((Double) val,3);
//                    assertEquals(rounded, 10.101d);
//                    break;
//            }
//
//            System.out.printf("Found beam: %s with total len: %s \n", name, rounded);
//        });


    }
}