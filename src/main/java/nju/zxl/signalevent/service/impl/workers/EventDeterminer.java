package nju.zxl.signalevent.service.impl.workers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import nju.zxl.signalevent.eo.HistoryData;
import nju.zxl.signalevent.eo.JudgeResult;
import nju.zxl.signalevent.eo.OrRule;
import nju.zxl.signalevent.eo.TriggerSignal;
import nju.zxl.signalevent.util.DaoUtils;
import org.springframework.web.multipart.MultipartFile;

public class EventDeterminer {
	ValueGenerator vg;
	DaoUtils daoUtils;
	
	public EventDeterminer(){
		vg = new ValueGenerator();
		daoUtils = new DaoUtils();
	}
	
	public List<JudgeResult> judgeEvent(MultipartFile file){
		List<HistoryData> hdlist = getSignalsfromData(file);
		List<JudgeResult> finaljrlist = new ArrayList<JudgeResult>();
		int index=0;
		for(int i=0;i<hdlist.size();i++){
			List<JudgeResult> jrlist = new ArrayList<JudgeResult>();
			HistoryData hd = hdlist.get(i);
			boolean ifmarked = ifHistoryDataMarked(hd);
			if(!ifmarked){
				boolean ifnecessary = ifHistoryDataNecessaryNew(hd.getSignal_fid());
				if(ifnecessary){
					System.out.println("index="+index);
					List<HistoryData> targetSiganls = getTargetSignals(i,hdlist);
					jrlist = getSimulatedevent(targetSiganls);
					hdlist = markList(targetSiganls,hdlist);
					index++;
					for(JudgeResult jr:jrlist){
						finaljrlist.add(jr);
					}
				}
			}
			System.out.println("当前已完成:"+i+"/"+hdlist.size());
		}
		int marked = 0;
		for(int i=0;i<hdlist.size();i++){
        			if(hdlist.get(i).getHandle_tag()==1){
        				marked++;
        				System.out.println("信号编号"+i+" 实际事件是:"+hdlist.get(i).getEvent_type());
        			}
        		}	
		System.out.println("未标记信号数:"+(hdlist.size()-marked));
		return finaljrlist;
	}
		
	
	
	private void dataOutput(List<JudgeResult> jrlist) {  
		String excelName = "E:judgeeventoutput.xls";  
		try {  
			File excelFile = new File(excelName);  
			// 如果文件存在就删除它  
			if (excelFile.exists())  
				excelFile.delete();  
			// 打开文件  
			WritableWorkbook book = Workbook.createWorkbook(excelFile);  
			// 生成名为“第一页”的工作表，参数0表示这是第一页  
			WritableSheet sheet = book.createSheet(" 第一页 ", 0);  
			// 以及单元格内容为 
			Label label0,label1,label2,label3,label4,label5,label6;
			label0 = new Label(0, 0, "源文件触发信号编号");  
			label1 = new Label(1, 0, "源文件事件相关信号编号集合");
			label2 = new Label(2, 0, "可能事件编号");
			label3 = new Label(3, 0, "可能事件权值");
			label4 = new Label(4, 0, "最终预测事件编号");
			label5 = new Label(5, 0, "最终预测事件权值");
			label6 = new Label(6, 0, "实际事件");
			
			// 将定义好的单元格添加到工作表中  
			sheet.addCell(label0);
			sheet.addCell(label1);
			sheet.addCell(label2);
			sheet.addCell(label3);
			sheet.addCell(label4);
			sheet.addCell(label5);
			sheet.addCell(label6);
			 
			int index = 1;
			for(JudgeResult jr:jrlist){
				if(Double.parseDouble(jr.getPossible_event_value())!=0){
					label0 = new Label(0, index, String.valueOf(jr.getTrigger_signal_id()));
					label1 = new Label(1, index, jr.getBatch_id());
					label2 = new Label(2, index, String.valueOf(jr.getPossible_event_id()));
					label3 = new Label(3, index, String.valueOf(jr.getPossible_event_value()));
					label4 = new Label(4, index, String.valueOf(jr.getFinal_event_id()));
					label5 = new Label(5, index, String.valueOf(jr.getFinal_event_value()));
					label6 = new Label(6, index, String.valueOf(jr.getActual_event_id()));
					sheet.addCell(label0);
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
					index++;
				}		
			}
			  			
			// 写入数据并关闭文件  
			book.write();  
			book.close();  
			System.out.println("Excel创建成功");  
		} catch (Exception e) {  
			System.out.println(e);  
		}  
	}  
	
	private boolean ifHistoryDataMarked(HistoryData hd) {
		if(hd.getHandle_tag()==1)
			return true;
		else
			return false;
	}
	
	public boolean ifHistoryDataProtected(String infostr) {
		int sid = vg.getSidbySignalinfo(infostr);
		boolean res = false;
		List<OrRule> orlist = vg.getOrrulelist();
		for(OrRule or:orlist){
			if((or.getSignal_type()==2||or.getSignal_type()==4)&&(sid == or.getSid())){
				res = true;	
			}		
		}
		return res;
	}
	
	public List<TriggerSignal> getTriggerSiganls() {
		String sql = "select * from `trigger_signal`";
		List<TriggerSignal>	list = daoUtils.getForList(TriggerSignal.class, sql);  
		//System.out.println(list.size());
		return list;
	}
	
	public boolean ifHistoryDataNecessary(String infostr) {
		int sid = vg.getSidbySignalinfo(infostr);
		boolean res = false;
		List<OrRule> orlist = vg.getOrrulelist();
		for(OrRule or:orlist){
			if((or.getSignal_type()==1||or.getSignal_type()==2)&&(sid == or.getSid())){
				res = true;	
			}		
		}		
		return res;
	}
	
	public boolean ifHistoryDataNecessaryNew(String infostr) {
		List<TriggerSignal> tslist= getTriggerSiganls();
		int sid = vg.getSidbySignalinfo(infostr);
		boolean res = false;
		for(TriggerSignal ts:tslist){
			if(sid == ts.getSid())
				res = true;
		}	
		return res;
	}
	
	private List<HistoryData> getTargetSignals(int i, List<HistoryData> hdlist) {
		List<HistoryData> res = new ArrayList<HistoryData>();
		String transformer_name = hdlist.get(i).getTransf();
		String area_id = hdlist.get(i).getBay_id();
		Date time1 = stringToDate(hdlist.get(i).getOccur_time());
		long l1 = time1.getTime();
		res.add(hdlist.get(i));
		for(int j=i+1;j<hdlist.size();j++){
			Date time2 = stringToDate(hdlist.get(j).getOccur_time());
			long l2 = time2.getTime();
			int seconds = (int)(l2-l1)/(1000);
			String list_transformer = hdlist.get(j).getTransf();
			String list_bay = hdlist.get(j).getBay_id();
			int ifmark = hdlist.get(j).getHandle_tag();
			int sid = vg.getSidbySignalinfo(hdlist.get(j).getSignal_fid());
			if((ifmark==0)&&((area_id.equals(list_bay)&&transformer_name.equals(list_transformer)&&seconds<=60)||(sid==2215||sid==2217))){
				if(ifHistoryDataNecessary(hdlist.get(j).getSignal_fid()))
					break;
				res.add(hdlist.get(j));
			}
		}
		System.out.println(res.size());
		return res;
	}

	private Date stringToDate(String contents) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d = new Date();
		try {
			d = sdf.parse(contents);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public int getEventamount() {
		String sql = "select count(distinct eid) from `or_rule`";
		int res = Integer.parseInt((daoUtils.getForValue(sql)).toString());
		//System.out.println(res);	
		return res;
	}
	
	private List<HistoryData> markList(List<HistoryData> targetSiganls, List<HistoryData> hdlist) {
		for(HistoryData hd:targetSiganls){
			int id = hd.getId();			
			hdlist.get(id).setHandle_tag(1);
	//		hdlist.get(id).setSimulatedEvent(simulatedevent);
			boolean ifprotected = ifHistoryDataProtected(hdlist.get(id).getSignal_fid());
			if(ifprotected){
				for(int i=id+1;i<hdlist.size()-1;i++){
					String t1 = hdlist.get(i).getTransf();
					String t2 = hd.getTransf();
					String jg1 = hdlist.get(i).getBay_id();
					String jg2 = hd.getBay_id();
					String sb1 = hdlist.get(i).getDevice_id();
					String sb2 = hd.getDevice_id();
					String info1 = hdlist.get(i).getTemplate_id();
					String info2 = hd.getTemplate_id();
					String act1 = hdlist.get(i).getAction_attr_id();
					int m1 = hdlist.get(i).getHandle_tag();
					if(t1.equals(t2)&&(jg1.equals(jg2))&&(sb1.equals(sb2))&&(info1.equals(info2))&&(act1.equals("2"))&&(m1==0)){
						hdlist.get(i).setHandle_tag(1);
		//				hdlist.get(i).setSimulatedEvent(simulatedevent);
						break;
					}
				}		
			}	
		}
		return hdlist;
	}
	
	private List<JudgeResult> getSimulatedevent(List<HistoryData> targetSiganls) {
		List<JudgeResult> jrlist = new ArrayList<JudgeResult>();
		int eventamount = getEventamount();
		double[][] possiblevalues = new double[eventamount][2];
		List<OrRule> orlist = vg.getOrrulelist();
		int[] tsid = new int[targetSiganls.size()];
		for(int i=0;i<targetSiganls.size();i++){
			int id = vg.getSidbySignalinfo(targetSiganls.get(i).getSignal_fid());
			tsid[i] = id;
		}
		double simulatevalue = 0;
		int  simulateeventid = -1;
		double infovalue = 0;
		int eid = orlist.get(0).getEid();
		int index = 0;
		for(OrRule or:orlist){
			int currentEventid = or.getEid();
			if(eid!=currentEventid){
				possiblevalues[index][0] = eid;
				possiblevalues[index][1] = infovalue;
				index++;
				infovalue = 0;
				eid = currentEventid;
			}
			
			for(int j=0;j<tsid.length;j++){
				if(or.getSid()==tsid[j])
					infovalue += Double.parseDouble(or.getSignal_value());
			}
					
			if(infovalue > simulatevalue){
				simulatevalue = infovalue;
				simulateeventid = currentEventid;
			}		
		}
		possiblevalues[index][0] = eid;
		possiblevalues[index][1] = infovalue;
		System.out.println("the event is "+simulateeventid);
		System.out.println("the value is "+simulatevalue);
		
		String signals = "";
		for(int i=0;i<targetSiganls.size();i++){
			signals = signals+String.valueOf(targetSiganls.get(i).getId())+";";
		}
		signals = signals.substring(0, signals.length()-1);
		
		for(int i=0;i<possiblevalues.length;i++){
			JudgeResult jr = new JudgeResult();
			jr.setTrigger_signal_id(targetSiganls.get(0).getId());
			jr.setBatch_id(signals);
			jr.setPossible_event_id((int)possiblevalues[i][0]);
			jr.setPossible_event_value(String.valueOf(possiblevalues[i][1]));
			jr.setFinal_event_id(simulateeventid);
			jr.setFinal_event_value(String.valueOf(simulatevalue));
			jr.setActual_event_id(-1);
			jrlist.add(jr);
		}
		
		return jrlist;
	}
	
	public List<HistoryData> getSignalsfromData(MultipartFile file){
		
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
}
