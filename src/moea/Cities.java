package moea;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class Cities {

    public static int[][] costs = {{0,3,2,8,21,2,10},{3,0,14,13,10,4,35},{2,14,0,3,4,23,5},{8,13,3,0,38,67,48},{21,10,4,38,0,5,12},{2,4,23,67,5,0,51},{10,35,5,48,12,51,0}};

    public static int[][] distances = {{0,4727,1205,6363,3657,3130,2414},{4727,0,3588,2012,1842,6977,6501},{1205,3588,0,5163,2458,3678,3071},{6363,2012,5163,0,2799,8064,7727},{3657,1842,2458,2799,0,5330,4946},{3130,6977,3678,8064,5330,0,743},{2414,6501,3071,7727,4946,743,0}};

    public static void populateFromFile(String costsString, String distancesString) throws IOException {
        //Costs
        File inputFile = new File(costsString);
        FileInputStream fis = new FileInputStream(inputFile);
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
        Iterator<Row> rowIterator = mySheet.iterator();
        int[][] inputCoasts = new int[48][48];
        int rowCount = 0;
        int cellCount = 0;
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            cellIterator.next();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    inputCoasts[rowCount][cellCount] = (int) cell.getNumericCellValue();
                    inputCoasts[cellCount][rowCount] = (int) cell.getNumericCellValue();
                    cellCount++;
                }
            }
            rowCount++;
            cellCount = 0;
        }
        costs = inputCoasts;
        //Distances
        inputFile = new File(distancesString);
        fis = new FileInputStream(inputFile);
        myWorkBook = new XSSFWorkbook (fis);
        mySheet = myWorkBook.getSheetAt(0);
        rowIterator = mySheet.iterator();
        int[][] inputDist = new int[48][48];
        rowCount = 0;
        cellCount = 0;
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            cellIterator.next();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    inputDist[rowCount][cellCount] = (int) cell.getNumericCellValue();
                    inputDist[cellCount][rowCount] = (int) cell.getNumericCellValue();
                    cellCount++;
                }
            }
            rowCount++;
            cellCount = 0;
        }
        distances = inputDist;
    }
}
