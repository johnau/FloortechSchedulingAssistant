package tech.jmcs.floortech.scheduling.app.datasource.converter;

import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.app.datasource.model.TrussData;
import tech.jmcs.floortech.scheduling.app.settings.SettingsHolder;
import tech.jmcs.floortech.scheduling.app.types.EndCapType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrussDataConverterTest {

    @Test
    void testConvert() {
        SettingsHolder mockSettings = new SettingsHolder();
        mockSettings.setScheduleEntryCw260Truss("CW260 Beam");
        mockSettings.setScheduleEntryCw346Truss("CW346 Beam");
        mockSettings.setScheduleEntryHj200Truss("Cut 200mm Hopley Trusses");
        mockSettings.setScheduleEntryHj300Truss("Cut 200mm Hopley Trusses");
        mockSettings.setScheduleEntryConnectionEndcapsCw260("UB connection brackets (260mm)");
        mockSettings.setScheduleEntryConnectionEndcapsCw346("UB connection brackets (346mm)");
        mockSettings.setScheduleEntryConnectionEndcapsHj200("UB connection brackets (200HJ)");
        mockSettings.setScheduleEntryConnectionEndcapsHj300("UB connection brackets (200HJ)");
        mockSettings.setScheduleEntryStandardEndcapsCw260("End Caps 260mm");
        mockSettings.setScheduleEntryStandardEndcapsCw346("End Caps 346mm");
        mockSettings.setScheduleEntryStandardEndcapsHj200("Standard End caps (200HJ)");
        mockSettings.setScheduleEntryStandardEndcapsHj300("Standard End caps (200HJ)");
        mockSettings.setScheduleEntryTrussAirConPenoCw260("AIR CON FRAMEWORK Fitted (CW260)");
        mockSettings.setScheduleEntryTrussAirConPenoCw346("AIR CON FRAMEWORK Fitted (CW346)");
        mockSettings.setScheduleEntryTrussAirConPenoHj300("AIR CON FRAMEWORK Fitted (300HJ)");
        

        TrussDataConverter converter = new TrussDataConverter(mockSettings);

        Map<Long, TrussData> dataMap = new HashMap<>();
        dataMap.put(1l, createTrussData("CW01", "CW260", 1000l, 5l, EndCapType.TYPE_A, EndCapType.TYPE_B, false));
        dataMap.put(2l, createTrussData("CW02", "CW260", 1500l, 1l, EndCapType.TYPE_C, EndCapType.TYPE_D, false));
        dataMap.put(3l, createTrussData("CW03", "CW260", 1300l, 3l, EndCapType.STANDARD, EndCapType.STANDARD, false));
        dataMap.put(4l, createTrussData("CW04", "CW260", 1000l, 2l, EndCapType.STANDARD, EndCapType.TYPE_A, false));

        Map<String, Object> convertedData = converter.convert(dataMap);

        convertedData.forEach( (name, value) -> {
            System.out.printf("::: %s : %s \n", name, value);
        });

    }

    private TrussData createTrussData(String id, String type, Long length, Long qty, EndCapType left, EndCapType right, boolean hasPeno) {
        TrussData data = new TrussData();
        data.setLeftEndcap(left);
        data.setRightEndcap(right);
        data.setLength(length);
        data.setTrussId(id);
        data.setQty(qty);
        data.setHasAirconPenetration(hasPeno);
        data.setType(type);

        return data;
    }
}