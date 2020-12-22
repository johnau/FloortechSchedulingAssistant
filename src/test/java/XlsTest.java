import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import tech.jmcs.floortech.scheduling.app.util.XLSHelper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class XlsTest {

    private String getResourcePath() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return absolutePath;
    }

    @Test
    void testXlsRead() {
        Path excelFile = Paths.get(getResourcePath(), "SHEET LISTING.xls");
        File f = excelFile.toFile();
        if (!f.exists()) {
            System.out.println("Could not find file");
            fail();
        }

        Workbook workbook;
        Iterator<Sheet> sheetIterator;
        Sheet firstSheet;

        try {
            workbook = WorkbookFactory.create(f);
            workbook.close();
        } catch (IOException e) {
            System.out.println("Could not get workbook");
            fail();
            return;
        }

        sheetIterator = workbook.sheetIterator();
        try {
            firstSheet = workbook.getSheetAt(0);
        } catch (IllegalArgumentException aex) {
            fail("Sheet out of range?");
            return;
        }

        String name = firstSheet.getSheetName();
        System.out.printf("Found sheet: %s \n", name);

        Row firstRow = firstSheet.getRow(0);
        Cell firstCell = XLSHelper.getCellByColumnIndex(firstRow, 0);
        if (firstCell == null) fail("Could not get the first cell");

        DataFormatter dataFormatter = new DataFormatter();
        String title = dataFormatter.formatCellValue(firstCell);
        System.out.printf("Table title: %s \n", title);

        int sc = 0;
        while (sheetIterator.hasNext()) {
            Sheet nSheet = sheetIterator.next();
            System.out.printf("At sheet %d: '%s'", ++sc, nSheet.getSheetName());
        }

    }

    @Test
    void randomTest() {
        try {
            Long l = Long.parseLong("");
        } catch (NumberFormatException nex) {
            System.out.println("Could not convert empty string to long");
        }
    }

    @Test
    void testAddCellsAndWrite() {
        // open existing excel file
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        Path excelFile = Paths.get(absolutePath, "ADDROW.xls");
        File f = excelFile.toFile();
        if (!f.exists()) {
            System.out.println("Could not find file");
            fail();
        }

        FileInputStream fis;
        Workbook workbook;
        Iterator<Sheet> sheetIterator;
        Sheet firstSheet;

        try {
            fis = new FileInputStream(f);
            workbook = WorkbookFactory.create(fis);
        } catch (FileNotFoundException e) {
            fail();
            return;
        } catch (IOException e) {
            System.out.println("Could not get workbook");
            fail();
            return;
        }

        sheetIterator = workbook.sheetIterator();
        try {
            firstSheet = workbook.getSheetAt(0);
        } catch (IllegalArgumentException aex) {
            fail("Sheet out of range?");
            return;
        }

        int rowCount = firstSheet.getLastRowNum();
        System.out.printf("Row count is: %s", rowCount);

        // insert three rows
        firstSheet.shiftRows(120, rowCount, 3);

        // add three consecutive rows with data

        for (int i = 0; i < 3; i++) {
            Row newRow = firstSheet.createRow(120 + i);
            Cell c1 = newRow.createCell(1);
            Cell c2 = newRow.createCell(2);
            c1.setCellValue(new Random().nextLong());
            c2.setCellValue(new Random().nextLong());
        }

        rowCount = firstSheet.getLastRowNum();
        System.out.printf("Row count is: %s", rowCount);

        // insert row
        firstSheet.shiftRows(115, rowCount, 1);

        // create row contents
        Row newRow = firstSheet.createRow(115);
        Cell c1 = newRow.createCell(1);
        Cell c2 = newRow.createCell(2);
        c1.setCellValue(new Random().nextLong());
        c2.setCellValue(new Random().nextLong());

        try {
            fis.close();
        } catch (IOException e) {
            fail();
            return;
        }

        // write
        if (workbook != null) {
            try (FileOutputStream fos = new FileOutputStream(f)) {
                workbook.write(fos);
                workbook.close();
            } catch (FileNotFoundException e) {
                System.out.printf("Could not find workbook: %s \n", e.getMessage());
                fail();
            } catch (IOException e) {
                System.out.printf("Could not access workbook: %s \n", e.getMessage());
            }
        }
    }
}
