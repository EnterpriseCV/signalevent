package nju.zxl.signalevent.service.impl.workers;

import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import nju.zxl.signalevent.eo.*;
import org.springframework.web.multipart.MultipartFile;

public class DataInit {
	DataOperation operation;
	public DataInit(){
		operation = new DataOperation();
	}

	public List<HistoryData> getHistoryDataFromFile(MultipartFile file){

		List<HistoryData> hdlist = new ArrayList<HistoryData>();
		Sheet sheet;
		Workbook book;
		Cell cell1,cell2,cell3,cell4,cell5,cell6,cell7,cell8,cell9,cell10,cell11,cell12,cell13,cell14,cell15,cell16;
		try {
			book = Workbook.getWorkbook(file.getInputStream());
			sheet = book.getSheet(0);
			int index = -1;
			for(int i=1;i<sheet.getRows();i++){
				cell1 = sheet.getCell(1,i);
				cell2 = sheet.getCell(2,i);
				cell3 = sheet.getCell(3,i);
				cell4 = sheet.getCell(4,i);
				cell5 = sheet.getCell(5,i);
				cell6 = sheet.getCell(6,i);
				cell7 = sheet.getCell(8,i);
				cell8 = sheet.getCell(9,i);
				cell9 = sheet.getCell(10,i);
				cell10 = sheet.getCell(11,i);
				cell11 = sheet.getCell(12,i);
				cell12 = sheet.getCell(13,i);
				cell13 = sheet.getCell(14,i);
				cell14 = sheet.getCell(15,i);
				cell15 = sheet.getCell(16,i);
				cell16 = sheet.getCell(0, i);
				if(cell15.getContents().contains(",")){
					index++;
					HistoryData hd =new HistoryData();
					hd.setId(index);
					hd.setSourceid(Integer.parseInt(cell16.getContents()));
					hd.setTransf(cell1.getContents());
					hd.setBay(cell2.getContents());
					hd.setOccur_time(cell3.getContents());
					hd.setContent(cell4.getContents());
					hd.setSignal_type(cell5.getContents());
					hd.setEvent_type(cell6.getContents());
					hd.setProv_bay(cell7.getContents());
					hd.setBay_id(cell8.getContents().trim());
					hd.setProv_device(cell9.getContents());
					hd.setDevice_id(cell10.getContents().trim());
					hd.setProv_template(cell11.getContents());
					hd.setTemplate_id(cell12.getContents().trim());
					hd.setAction_attr(cell13.getContents());
					hd.setAction_attr_id(cell14.getContents().trim());
					hd.setSignal_fid(cell15.getContents().trim());
					hd.setHandle_tag(0);
					hd.setTrigger_tag(0);
					hdlist.add(hd);
				}
			}

			book.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return hdlist;
	}



	public void inittriggerset(){
			List<AndRule> arlist = operation.getAndrulelist();
			for(AndRule ar:arlist){
				if(ar.getOrder()>0){
					List<OrRule> orlist = operation.getOrrulelistbyArid(ar.getArid());
					for(OrRule or:orlist){
						TriggerSet tso = new TriggerSet();
						tso.setEid(or.getEid());
						tso.setSid(or.getSid());
						tso.setOrder(ar.getOrder());
						operation.addTriggerSet(tso);
					}
				}
			}
			
		}

	public List getSignalsFromFile(MultipartFile file){

		ArrayList<Signal> slist = new ArrayList<Signal>();

		Sheet sheet;
		Workbook book;
		Cell cell1,cell2,cell3,cell4,cell5;
		try {
			book = Workbook.getWorkbook(file.getInputStream());
			sheet = book.getSheet(0);
			for(int i=1;i<sheet.getRows();i++){
				cell1 = sheet.getCell(1,i);
				cell2 = sheet.getCell(2,i);
				cell3 = sheet.getCell(3,i);
				cell4 = sheet.getCell(4,i);
				cell5 = sheet.getCell(0,i);
				Signal s = new Signal();
				s.setSid(Integer.valueOf(cell5.getContents()));
				s.setArea_id(Integer.valueOf(cell1.getContents()));
				s.setEquip_id(Integer.valueOf(cell2.getContents()));
				s.setInfo_id(Integer.valueOf(cell3.getContents()));
				s.setAct_id(Integer.valueOf(cell4.getContents()));
				slist.add(s);
			}
			book.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return slist;
	}

	public List getEventsFromFile(MultipartFile file){
		ArrayList<Event> elist = new ArrayList<Event>();
		Sheet sheet;
		Workbook book;
		Cell cell1,cell2,cell3,cell4,cell5;
		try {
			book = Workbook.getWorkbook(file.getInputStream());
			sheet = book.getSheet(0);
			for(int i=1;i<sheet.getRows();i++){
				cell1 = sheet.getCell(1,i);
				cell2 = sheet.getCell(2,i);
				cell3 = sheet.getCell(3,i);
				cell4 = sheet.getCell(4,i);
				cell5 = sheet.getCell(0,i);
				Event e = new Event();
				e.setEid(Integer.valueOf(cell5.getContents()));
				e.setArea_id(Integer.valueOf(cell1.getContents()));
				e.setVoltage_id(Integer.valueOf(cell2.getContents()));
				e.setEquipment_id(Integer.valueOf(cell3.getContents()));
				e.setInfo_id(Integer.valueOf(cell4.getContents()));
				e.setType_id(-1);
				e.setRank_id(-1);
				elist.add(e);
			}
			book.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return elist;
	}

	public List getAndOrRuleFromFile(MultipartFile f){
		List resultList = new ArrayList<>();
		List andRuleList = new ArrayList<>();
		List orRuleList = new ArrayList<>();


		return resultList;
	}



}
