package nju.zxl.signalevent.util;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JxlUtils {

    public static String[][]  jxlExlToList(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        String[] fileNameArray = fileName.split("\\.");
        if(fileNameArray.length<1){
            return null;
        }
        String fileNameEnd = fileNameArray[fileNameArray.length-1];
        Workbook workBook;
        Sheet sheet;
        Row row;
        Cell cell;
        DataFormatter dataFormatter = new DataFormatter();
        int rowStart = 0,rowEnd = 0,cellStart = 0,cellEnd = 0;

        switch (fileNameEnd){
            case "xls":{
                workBook = new HSSFWorkbook(file.getInputStream());
                break;
            }
            case "xlsx":{
                workBook = new XSSFWorkbook(file.getInputStream());
                break;
            }
            default:{
                return null;
            }
        }

        if(workBook.getNumberOfSheets()>1){
            return null;
        }

        sheet = workBook.getSheetAt(0);
        rowStart=sheet.getFirstRowNum();
        rowEnd=sheet.getLastRowNum();
        cellStart = sheet.getRow(rowStart).getFirstCellNum();
        cellEnd = sheet.getRow(rowStart).getLastCellNum()-1;
        String[][] excelArray = new String[rowEnd-rowStart+1][cellEnd-cellStart+1];
        for(int i=rowStart;i<=rowEnd;i++){
            row = sheet.getRow(i);
            for(int j=cellStart;j<=cellEnd;j++){
                cell = row.getCell(j);
                excelArray[i][j] = dataFormatter.formatCellValue(cell);
            }
        }
        return excelArray;
    }

}
