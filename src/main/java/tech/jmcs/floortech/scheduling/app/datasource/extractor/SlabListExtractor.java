package tech.jmcs.floortech.scheduling.app.datasource.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jmcs.floortech.scheduling.app.datasource.model.ExtractedTableData;
import tech.jmcs.floortech.scheduling.app.datasource.model.SlabData;
import tech.jmcs.floortech.scheduling.app.types.BalconyType;
import tech.jmcs.floortech.scheduling.app.exception.DataExtractorException;
import tech.jmcs.floortech.scheduling.app.exception.SlabDataException;
import tech.jmcs.floortech.scheduling.app.types.DataSourceExtractorType;
import tech.jmcs.floortech.scheduling.app.types.MeasurementUnit;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts the slab data from the drawing pdf file
 */
public class SlabListExtractor extends PdfTextDataSourceExtractor<SlabData> {
    private static final Logger LOG = LoggerFactory.getLogger(SlabListExtractor.class);

    public static final String NUMBER_PATTERN_COMPONENT = "[0-9]{0,3}[,\\s]?[0-9]{1,4}[.][0-9]{1,2}"; // Supports values to 999,999 (with or without comma or space for thousands separator)
    public static final String M_SQRD_PATTERN_COMPONENT = "[mM][2²]?";
    public static final String M_LINR_PATTERN_COMPONENT = "[mM]";
    public static final Pattern FLOOR_AREA_PATTERN = Pattern.compile(String.format("FLOOR AREA[\\s]*=[\\s]*(%s)%s", NUMBER_PATTERN_COMPONENT, M_SQRD_PATTERN_COMPONENT));
    public static final Pattern BALCONY_AREAS_PATTERN = Pattern.compile(String.format("BALCONY AREA[\\s]*[(]([0-9][cC][\\s]*[a-zA-Z]+)[)][\\s]*=[\\s]*(%s)%s", NUMBER_PATTERN_COMPONENT, M_SQRD_PATTERN_COMPONENT));
    public static final Pattern THICK_ANGLE_PATTERN = Pattern.compile(String.format("THICK ANGLE[\\s]*=[\\s]*(%s)%s", NUMBER_PATTERN_COMPONENT, M_LINR_PATTERN_COMPONENT));
    public static final Pattern THIN_ANGLE_PATTERN = Pattern.compile(String.format("THIN ANGLE[\\s]*=[\\s]*(%s)%s", NUMBER_PATTERN_COMPONENT, M_LINR_PATTERN_COMPONENT));

    public static final String BALC_2C_INSITU = "Balcony Slab - 2c Insitu";
    public static final String BALC_3C_INSITU = "Balcony Slab - 3c Insitu";
    public static final String BALC_4C_INSITU = "Balcony Slab - 4c Insitu";
    public static final String BALC_2C_RHS = "Balcony Slab - 2c RHS";
    public static final String BALC_3C_RHS = "Balcony Slab - 3c RHS";
    public static final String INT_FLOOR = "Internal FLoor Slab";
    public static final String THICK_ANGLE = "Thick Edging Angle";
    public static final String THIN_ANGLE = "Thin Edging Angle";

    protected SlabListExtractor(Path pdfPath) throws IOException {
        super(pdfPath);
    }

    @Override
    public Boolean isValid() {
        Matcher floorAreaMatcher = FLOOR_AREA_PATTERN.matcher(this.pdfText);
//        Matcher balconyAreasMatcher = BALCONY_AREAS_PATTERN.matcher(this.pdfText);
        Matcher thickAngleMatcher = THICK_ANGLE_PATTERN.matcher(this.pdfText);
        Matcher thinAngleMatcher = THIN_ANGLE_PATTERN.matcher(this.pdfText);

        boolean foundFloorArea = floorAreaMatcher.find();
        boolean foundThickAngle = thickAngleMatcher.find();
        boolean foundThinAngle = thinAngleMatcher.find();
        LOG.debug("Found Floor Area: {}, Thick Angle: {}, and Thin Angle: {}", foundFloorArea, foundThickAngle, foundThinAngle);
        if (!foundFloorArea || !foundThickAngle || !foundThinAngle) {
            if (!foundFloorArea) this.validationErrors.add("Floor area could not be read from PDF: " + this.getSourceFilePath().getFileName().toString());
            if (!foundThickAngle) this.validationErrors.add("Thick Angle could not be read from PDF: " + this.getSourceFilePath().getFileName().toString());
            if (!foundThinAngle) this.validationErrors.add("Thin Angle could not be read from PDF: " + this.getSourceFilePath().getFileName().toString());
            return false;
        }
        return true;
    }

    @Override
    public void extract() throws DataExtractorException {
        if (!isValid()) {
            LOG.warn("The PDF file was not valid for Slab Data.");
            throw new SlabDataException("The PDF is not valid for Slab data");
        }

        ExtractedTableData<SlabData> tableData = new ExtractedTableData<>(DataSourceExtractorType.SLAB.getName());

        SlabData slabData = new SlabData();

        Matcher floorAreaMatcher = FLOOR_AREA_PATTERN.matcher(this.pdfText);
        Double floorArea = processSingularDoubleValues(floorAreaMatcher);

        Matcher balconyAreasMatcher = BALCONY_AREAS_PATTERN.matcher(this.pdfText);
        Map<String, Double> balconyAreas = processBalconyDoubleValues(balconyAreasMatcher);
        Double rhs2c = 0d;
        Double rhs3c = 0d;
        Double insitu2c = 0d;
        Double insitu3c = 0d;
        Double insitu4c = 0d;

        for (Map.Entry<String, Double> entry : balconyAreas.entrySet()) {
            String type = entry.getKey();
            Double value = entry.getValue();
            BalconyType balcType = processBalconyType(type);
            switch (balcType) {
                case RHS_2C:
                    rhs2c = value;
                    break;
                case RHS_3C:
                    rhs3c = value;
                    break;
                case INSITU_2C:
                    insitu2c = value;
                    break;
                case INSITU_3C:
                    insitu3c = value;
                    break;
                case INSITU_4C:
                    insitu4c = value;
                    break;
            }
        }

        Matcher thickAngleMatcher = THICK_ANGLE_PATTERN.matcher(this.pdfText);
        Double thickAngleLength = processSingularDoubleValues(thickAngleMatcher);

        Matcher thinAngleMatcher = THIN_ANGLE_PATTERN.matcher(this.pdfText);
        Double thinAngleLength = processSingularDoubleValues(thinAngleMatcher);

        SlabData balc2cInsitu = new SlabData();
        balc2cInsitu.setName(BALC_2C_INSITU);
        balc2cInsitu.setSize(insitu2c);
        balc2cInsitu.setMeasurementUnit(MeasurementUnit.M2);

        SlabData balc3cInsitu = new SlabData();
        balc3cInsitu.setName(BALC_3C_INSITU);
        balc3cInsitu.setSize(insitu3c);
        balc3cInsitu.setMeasurementUnit(MeasurementUnit.M2);

        SlabData balc4cInsitu = new SlabData();
        balc4cInsitu.setName(BALC_4C_INSITU);
        balc4cInsitu.setSize(insitu4c);
        balc4cInsitu.setMeasurementUnit(MeasurementUnit.M2);

        SlabData balc2cRhs = new SlabData();
        balc2cRhs.setName(BALC_2C_RHS);
        balc2cRhs.setSize(rhs2c);
        balc2cRhs.setMeasurementUnit(MeasurementUnit.M2);

        SlabData balc3cRhs = new SlabData();
        balc3cRhs.setName(BALC_3C_RHS);
        balc3cRhs.setSize(rhs3c);
        balc3cRhs.setMeasurementUnit(MeasurementUnit.M2);

        SlabData intFloor = new SlabData();
        intFloor.setName(INT_FLOOR);
        intFloor.setSize(floorArea);
        intFloor.setMeasurementUnit(MeasurementUnit.M2);

        SlabData thickAngle = new SlabData();
        thickAngle.setName(THICK_ANGLE);
        thickAngle.setSize(thickAngleLength);
        thickAngle.setMeasurementUnit(MeasurementUnit.LM);

        SlabData thinAngle = new SlabData();
        thinAngle.setName(THIN_ANGLE);
        thinAngle.setSize(thinAngleLength);
        thinAngle.setMeasurementUnit(MeasurementUnit.LM);

        tableData.addData(balc2cInsitu);
        tableData.addData(balc3cInsitu);
        tableData.addData(balc4cInsitu);
        tableData.addData(balc2cRhs);
        tableData.addData(balc3cRhs);
        tableData.addData(intFloor);
        tableData.addData(thinAngle);
        tableData.addData(thickAngle);

//        slabData.setBalcony2cInsituArea(insitu2c);
//        slabData.setBalcony2cRhsArea(rhs2c);
//        slabData.setBalcony3cInsituArea(insitu3c);
//        slabData.setBalcony3cRhsArea(rhs3c);
//        slabData.setBalcony4cInsituArea(insitu4c);
//        slabData.setFloorArea(floorArea);
//        slabData.setThickAngle(thickAngleLength);
//        slabData.setThinAngle(thinAngleLength);

//        tableData.addData(slabData);

        this.dataObject = tableData;
    }

    private Map<String, Double> processBalconyDoubleValues(Matcher matcher) {
        Map<String, Double> dataMap = new HashMap<>();

        while (matcher.find()) {
            String type = matcher.group(1);
            String val = matcher.group(2);

            try {
                Double dVal = Double.parseDouble(val);
                dataMap.put(type, dVal);
            } catch (NumberFormatException nex) {
                LOG.warn("Could not parse as double: " + val);
            }
        }

        return dataMap;
    }

    private BalconyType processBalconyType(String type) {
        /**
         * Example Data: (Reg ex gets inside the brackets)
         *         BALCONY AREA (2c RHS) = 2.00m²
         *         BALCONY AREA (3c RHS) = 3.00m²
         *         BALCONY AREA (2c INSITU) = 4.00m²
         *         BALCONY AREA (3c INSITU) = 5.00m²
         *         BALCONY AREA (4c INSITU) = 6.00m²
         */

        type = type.toUpperCase();
        BalconyType balcType = BalconyType.fromName(type);
        if (balcType == null) {
            balcType = BalconyType.NONE;
        }

        return balcType;
    }

    private Double processSingularDoubleValues(Matcher matcher) {
        while (matcher.find()) {
            String val = matcher.group(1);
            LOG.debug("Found double value: {}", val);
            try {
                Double dVal = Double.parseDouble(val);

                return dVal;
            } catch (NumberFormatException nex) {
                LOG.warn("Could not parse as double: " + val);
            }
        }
        return null;
    }

    @Override
    public ExtractedTableData<SlabData> getData() {

        return this.dataObject;
    }
}
