package nju.zxl.signalevent.util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.util.*;

public class JxlUtils {
    public static void jxlListToExl(List<Map> tableContent, String outPutFileName) {
        WritableWorkbook book = null;
        try {
            File os = new File(System.getProperty("user.dir") + outPutFileName);
            if (!os.exists()) {
                // 如果指定文件不存在，则新建该文件
                os.createNewFile();
            }

            book = Workbook.createWorkbook(os);// 创建一个新的写入工作簿
            WritableSheet sheet = book.createSheet("sheet1", 1);

            List<String> tableHeader = new ArrayList<String>();
            if (tableContent.size() >= 1) {
                Map map = tableContent.get(0);
                Set keySet = map.keySet();
                for (Object keyName : keySet) {
                    tableHeader.add(keyName.toString());
                    System.out.println(keyName);
                }
            } else {
                return;
            }

            // 第一行写入表头
            for (int i = 0; i < tableHeader.size(); i++) {
                Label lable = new Label(i, 0, tableHeader.get(i));
                sheet.addCell(lable);
            }

            // 后续行写入数据
            for (int i = 0; i < tableContent.size(); i++) {
                Map map = tableContent.get(i);
                for (int j = 0; j < tableHeader.size(); j++) {
                    System.out.println(map.get(tableHeader.get(j)));
                    Label lable = new Label(j, i + 1, map.get(
                            tableHeader.get(j)).toString());
                    sheet.addCell(lable);
                }

            }

            book.write();
            System.out.println("工作簿写入数据成功！");
            book.close();// 关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Map> jxlExlToList(String inPutFileName) {
        Workbook book = null;
        List<Map> list = null;
        try {
            File os = new File(inPutFileName);
            if (!os.exists()) {
                // 如果指定文件不存在，返回null
                System.out.println("文件不存在");
                return list;
            }

            book = Workbook.getWorkbook(os);// 获取工作簿
            Sheet sheet = book.getSheet(0);
            int totalRows = sheet.getRows();
            int totalColumns = sheet.getColumns();
            System.out.println("the excel total rows = [" + totalRows + "]");
            Cell[] cell = sheet.getRow(0);
            if (totalColumns <= 0) {
                return null;
            }
            //读取第一行作为Map中的key
            List tableHeaderlist = new ArrayList();
            for (int i = 0; i < totalColumns; i++) {
                tableHeaderlist.add(cell[i].getContents());
            }

            //将每一行存为Map集合，然后存为list
            list = new ArrayList();
            Map rowData = new LinkedHashMap();
            for (int i = 1; i < totalRows; i++) {
                cell = sheet.getRow(i);
                rowData = new LinkedHashMap(totalColumns);
                //循环获取每一行记录的每一属性信息，且忽略第一行id
                for (int j = 1; j < totalColumns; j++) {
                    rowData.put(tableHeaderlist.get(j), cell[j].getContents());
                }
                list.add(rowData);
            }

            System.out.println("工作簿读取数据成功！");

            book.close();// 关闭
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
