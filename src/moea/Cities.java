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

    public static int[][] coasts = {{0,3,2,8,21,2,10},{3,0,14,13,10,4,35},{2,14,0,3,4,23,5},{8,13,3,0,38,67,48},{21,10,4,38,0,5,12},{2,4,23,67,5,0,51},{10,35,5,48,12,51,0}};

    public static int[][] distances = {{0,4727,1205,6363,3657,3130,2414},{4727,0,3588,2012,1842,6977,6501},{1205,3588,0,5163,2458,3678,3071},{6363,2012,5163,0,2799,8064,7727},{3657,1842,2458,2799,0,5330,4946},{3130,6977,3678,8064,5330,0,743},{2414,6501,3071,7727,4946,743,0}};

    public static void populateFromFile(String costs, String distances) throws IOException {
        ClassLoader classloader =
                org.apache.poi.poifs.filesystem.POIFSFileSystem.class.getClassLoader();
        URL res = classloader.getResource(
                "org/apache/poi/poifs/filesystem/POIFSFileSystem.class");
        String path = res.getPath();
        System.out.println("Core POI came from " + path);

        File inputCosts = new File(costs);

        FileInputStream fis = new FileInputStream(inputCosts);

        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);

        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

        Iterator<Row> rowIterator = mySheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()){
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.print(cell.getBooleanCellValue() + "\t");
                        break;
                    default:
                }
            }

        }
    }
}
