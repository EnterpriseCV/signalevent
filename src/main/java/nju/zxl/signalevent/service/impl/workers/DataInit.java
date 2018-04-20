package nju.zxl.signalevent.service.impl.workers;

import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import nju.zxl.signalevent.eo.HistoryData;
import nju.zxl.signalevent.bean.Rule;
import nju.zxl.signalevent.eo.*;
import org.springframework.web.multipart.MultipartFile;

public class DataInit {
	DataOperation operation;
	public DataInit(){
		operation = new DataOperation();
	}

	public List<Rule> getRulesFromFile(MultipartFile file){
		List<Rule> ruleList = new ArrayList<Rule>();
		Sheet sheet;
		Workbook book;
		Cell cell1,cell2,cell3,cell4,cell5;
		try {
			book = Workbook.getWorkbook(file.getInputStream());
			sheet = book.getSheet(0);
			int index = -1;
			for (int i = 1; i < sheet.getRows(); i++) {
				cell1 = sheet.getCell(1, i);
				cell2 = sheet.getCell(2, i);
				cell3 = sheet.getCell(3, i);
				cell4 = sheet.getCell(4, i);
				Rule r = new Rule();
				r.setEventId(Integer.valueOf(cell1.getContents()));
				r.setSignals(cell2.getContents());
				r.setSignal_type(Integer.valueOf(cell3.getContents()));
				if (cell4.getContents().isEmpty()) {
					r.setOrder(-1);
				} else {
					r.setOrder(Integer.valueOf(cell4.getContents()));
				}
				ruleList.add(r);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ruleList;
	}

	public List<HistoryData> getHistoryDataFromFile(MultipartFile file){

		List<HistoryData> hdlist = new ArrayList<HistoryData>();
		Sheet sheet;
		Workbook book;
		Cell[] cells = new Cell[30];
		try {
			book = Workbook.getWorkbook(file.getInputStream());
			sheet = book.getSheet(0);
			int index = -1;
			for(int i=1;i<sheet.getRows();i++){
				for(int j=0;j<23;j++){
					cells[j]=sheet.getCell(j,i);
				}
				if(cells[15].getContents().contains(",")){
					index++;
					HistoryData hd =new HistoryData();
					hd.setId(index);
					hd.setSourceid(Integer.parseInt(cells[0].getContents()));
					hd.setTransf(cells[1].getContents());
					hd.setBay(cells[2].getContents());
					hd.setOccur_time(cells[3].getContents());
					hd.setContent(cells[4].getContents());
					hd.setSignal_type(cells[5].getContents());
					hd.setEvent_detail(cells[6].getContents());
					hd.setProv_bay(cells[7].getContents());
					hd.setBay_id(cells[8].getContents().trim());
					hd.setProv_device(cells[9].getContents());
					hd.setDevice_id(cells[10].getContents().trim());
					hd.setProv_template(cells[11].getContents());
					hd.setTemplate_id(cells[12].getContents().trim());
					hd.setAction_attr(cells[13].getContents());
					hd.setAction_attr_id(cells[14].getContents().trim());
					hd.setSignal_fid(cells[15].getContents().trim());
					hd.setEvent_type(cells[16].getContents());
					hd.setEvent_area(cells[17].getContents());
					hd.setEvent_vol(cells[18].getContents());
					hd.setEvent_equip(cells[19].getContents());
					hd.setEvent_info(cells[20].getContents());
					hd.setEvent_id(cells[21].getContents());
					hd.setEvent_mark(cells[22].getContents());
					hd.setHandle_tag(0);
					hd.setBatch_id(-1);
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
}
