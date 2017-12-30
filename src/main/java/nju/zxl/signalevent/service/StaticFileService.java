package nju.zxl.signalevent.service;

import nju.zxl.signalevent.bean.DataSignalBean;
import nju.zxl.signalevent.bean.SignalBean;
import nju.zxl.signalevent.dao.HistoryDao;
import nju.zxl.signalevent.dao.SignalDao;
import nju.zxl.signalevent.domain.History;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class StaticFileService {
    @Autowired
    private SignalDao sd;
    @Autowired
    private HistoryDao hd;

    private static StaticFileService sfs;

    @PostConstruct
    public void init(){
        sfs=this;
        sfs.sd=this.sd;
        sfs.hd=this.hd;
    }

    public static List<DataSignalBean> getDataSignalFromFile(MultipartFile file){
        ArrayList<DataSignalBean> dslist = new ArrayList<DataSignalBean>();

        Workbook wb;
        Sheet sheet;

        String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
        try {
            if (filetype.equals(".xls")) {
                wb = new HSSFWorkbook(file.getInputStream());
            }else if(filetype.equals(".xlsx")){
                wb = new XSSFWorkbook(file.getInputStream());
            }else{
                return new ArrayList<DataSignalBean>();
            }
        }catch (IOException e){
            e.printStackTrace();
            return new ArrayList<DataSignalBean>();
        }
        List<DataSignalBean> dsList = new ArrayList<DataSignalBean>();
        int count = 0;
        for(int i=0;i<wb.getNumberOfSheets();i++){
            sheet = wb.getSheetAt(i);
            for(int j=sheet.getFirstRowNum()+1;j<=sheet.getLastRowNum();j++){
                Row r = sheet.getRow(j);
                if(r.getCell(15).getStringCellValue().contains(",")){
                    DataSignalBean ds = new DataSignalBean();
                    ds.setId(count);
                    ds.setTS_name(r.getCell(1).getStringCellValue());
                    ds.setTime(stringToDate(r.getCell(3).getStringCellValue()));
                    ds.setActualEvent(r.getCell(6).getStringCellValue());
                    String signals[] = r.getCell(15).getStringCellValue().split(",");
                    SignalBean s = new SignalBean();
                    s.setJg_bh(Integer.parseInt(signals[0]));
                    s.setSb_bh(Integer.parseInt(signals[1]));
                    s.setInfo_bh(Integer.parseInt(signals[2]));
                    s.setAct_bh(Integer.parseInt(signals[3]));
                    ds.setS(s);
                    ds.setMark(0);
                    count++;
                    dsList.add(ds);
                }else continue;
            }
        }
        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dsList;
    }

    public static List<DataSignalBean> getDataSignalBeanFromHistory(){
        List<History> histories= sfs.hd.findAll();
        List<DataSignalBean> dsList = new ArrayList<DataSignalBean>();

        int count = 0;

        for(History h:histories){
            if(!h.getSignal_fid().isEmpty()&&h.getSignal_fid().contains(",")){
                DataSignalBean ds = new DataSignalBean();
                ds.setId(count);
                ds.setTS_name(h.getTransf());
                ds.setTime(stringToDate(h.getOccur_time()));
                ds.setActualEvent(h.getEvent_type());
                String signals[] = h.getSignal_fid().split(",");
                SignalBean s = new SignalBean();
                s.setJg_bh(Integer.parseInt(signals[0]));
                s.setSb_bh(Integer.parseInt(signals[1]));
                s.setInfo_bh(Integer.parseInt(signals[2]));
                s.setAct_bh(Integer.parseInt(signals[3]));
                ds.setS(s);
                ds.setMark(0);
                count++;
                dsList.add(ds);
            }
        }

        return dsList;
    }

    private static Date stringToDate(String contents) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d = new Date();
        try {
            d = sdf.parse(contents);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }
}
