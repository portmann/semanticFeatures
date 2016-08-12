//package ch.lgt.ming.processing;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Iterator;
//
///**
// * Created by Ming Deng on 8/10/2016.
// */
//public class TestXLSX {
//    public static void main(String[] args) throws IOException {
//        File wordlist = new File("dictionaries/NER_ming.xlsx");
//        FileInputStream fis = new FileInputStream(wordlist);
//        XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
//        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
//        Iterator<Row> rowIterator = mySheet.iterator();
//
//        //Traversing over each row of XLSX file
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//            //For each row, iterate through each columns
//            Iterator<Cell> cellIterator = row.cellIterator();
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//                switch ((cell.getCellType())) {
//                    case Cell.CELL_TYPE_STRING:
//                        System.out.println(cell.getStringCellValue());
//                        break;
//                    case Cell.CELL_TYPE_NUMERIC:
//                        System.out.println(cell.getNumericCellValue());
//                        break;
//                    default:
//                }
//            }
//        }
//    }
//}
