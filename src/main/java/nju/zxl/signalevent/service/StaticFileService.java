package nju.zxl.signalevent.service;

import nju.zxl.signalevent.bean.DataSignalBean;
import nju.zxl.signalevent.bean.SignalBean;
import nju.zxl.signalevent.dao.HistoryDao;
import nju.zxl.signalevent.dao.SignalDao;
import nju.zxl.signalevent.domain.Event;
import nju.zxl.signalevent.domain.HistoryData;
import nju.zxl.signalevent.domain.Signal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
        List<DataSignalBean> dslist = new ArrayList<DataSignalBean>();

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
                if(r.getLastCellNum()<16)continue;
                if(r.getCell(16).getStringCellValue().contains(",")){
                    DataSignalBean ds = new DataSignalBean();
                    ds.setId(count);
                    if(r.getCell(1)!=null) {
                        ds.setTS_name(r.getCell(1).getStringCellValue());
                    }
                    if(r.getCell(3)!=null)
                    ds.setTime((r.getCell(3).getDateCellValue()));
                    if(r.getCell(6)!=null)
                    ds.setActualEvent(r.getCell(6).getStringCellValue());
                    if(r.getCell(16)!=null) {
                        String signals[] = r.getCell(16).getStringCellValue().split(",");
                        SignalBean s = new SignalBean();
                        s.setJg_bh(Integer.parseInt(signals[0]));
                        s.setSb_bh(Integer.parseInt(signals[1]));
                        s.setInfo_bh(Integer.parseInt(signals[2]));
                        s.setAct_bh(Integer.parseInt(signals[3]));
                        ds.setS(s);
                    }
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
        List<HistoryData> histories= sfs.hd.findAll();
        List<DataSignalBean> dsList = new ArrayList<DataSignalBean>();

        int count = 0;

        for(HistoryData h:histories){
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

    public static List<Signal> getSignalFromFile(MultipartFile file){
        List<Signal> slist = new ArrayList<Signal>();

        Workbook wb;
        Sheet sheet;

        String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
        try {
            if (filetype.equals(".xls")) {
                wb = new HSSFWorkbook(file.getInputStream());
            }else if(filetype.equals(".xlsx")){
                wb = new XSSFWorkbook(file.getInputStream());
            }else{
                return new ArrayList<Signal>();
            }
        }catch (IOException e){
            e.printStackTrace();
            return new ArrayList<Signal>();
        }

        for(int i=0;i<wb.getNumberOfSheets();i++){
            sheet = wb.getSheetAt(i);
            for(int j=sheet.getFirstRowNum()+1;j<=sheet.getLastRowNum();j++){
                Row r = sheet.getRow(j);
                Signal s = new Signal();
                s.setXh((int)r.getCell(0).getNumericCellValue());
                s.setJg_bh((int)r.getCell(1).getNumericCellValue());
                s.setSb_bh((int)r.getCell(2).getNumericCellValue());
                s.setInfo_bh((int)r.getCell(3).getNumericCellValue());
                s.setAct_bh((int)r.getCell(4).getNumericCellValue());
                slist.add(s);
            }
        }
        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return slist;
    }

    public static List<Event> getEventFromFile(MultipartFile file){
        List<Event> elist = new ArrayList<Event>();

        Workbook wb;
        Sheet sheet;

        String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
        try {
            if (filetype.equals(".xls")) {
                wb = new HSSFWorkbook(file.getInputStream());
            }else if(filetype.equals(".xlsx")){
                wb = new XSSFWorkbook(file.getInputStream());
            }else{
                return elist;
            }
        }catch (IOException e){
            e.printStackTrace();
            return elist;
        }

        for(int i=0;i<wb.getNumberOfSheets();i++){
            sheet = wb.getSheetAt(i);
            for(int j=sheet.getFirstRowNum()+1;j<=sheet.getLastRowNum();j++){
                Row r = sheet.getRow(j);
                Event e = new Event();
                e.setId((int)r.getCell(0).getNumericCellValue());
                e.setInterval((int)r.getCell(1).getNumericCellValue());
                e.setVoltage((int)r.getCell(2).getNumericCellValue());
                e.setEquipment((int)r.getCell(3).getNumericCellValue());
                e.setInformation((int)r.getCell(4).getNumericCellValue());
                elist.add(e);
            }
        }
        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return elist;
    }

    public static List<EventRule> getEventRuleFromFile(MultipartFile file){
        List<EventRule> erlist = new ArrayList<EventRule>();
        Workbook wb;
        Sheet sheet;

        String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
        try {
            if (filetype.equals(".xls")) {
                wb = new HSSFWorkbook(file.getInputStream());
            }else if(filetype.equals(".xlsx")){
                wb = new XSSFWorkbook(file.getInputStream());
            }else{
                return erlist;
            }
        }catch (IOException e){
            e.printStackTrace();
            return erlist;
        }

        for(int i=0;i<wb.getNumberOfSheets();i++){
            sheet = wb.getSheetAt(i);
            for(int j=sheet.getFirstRowNum()+1;j<=sheet.getLastRowNum();j++){
                Row r = sheet.getRow(j);
                EventRule er = new EventRule();
                er.setId((int)r.getCell(0).getNumericCellValue());
                er.setEventId((int)r.getCell(1).getNumericCellValue());
                String[] signals = r.getCell(2).getStringCellValue().split("\\|");
                for(int k=0;k<signals.length;k++){
                    String[] info=signals[k].split(",");
                    signals[k]=Integer.toString(sfs.sd.findByInfo(
                       Integer.parseInt(info[0]),Integer.parseInt(info[1]),
                            Integer.parseInt(info[2]),Integer.parseInt(info[3])
                    ).getXh());
                }
                StringBuilder sb = new StringBuilder();
                if(signals.length>0){
                    sb.append(signals[0]);
                }
                for(int k=1;k<signals.length;k++){
                    sb.append('|');
                    sb.append(signals[k]);
                }
                er.setSignals(sb.toString());
                er.setType((int)r.getCell(3).getNumericCellValue());
                erlist.add(er);
            }
        }
        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return erlist;
    }
}
