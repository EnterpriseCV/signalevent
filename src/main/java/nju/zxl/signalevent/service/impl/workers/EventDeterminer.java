package nju.zxl.signalevent.service.impl.workers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nju.zxl.signalevent.model.HistoryData;
import nju.zxl.signalevent.model.*;
import org.springframework.web.multipart.MultipartFile;

public class EventDeterminer {
	DataInit datainit;
	DataOperation operation;
	
	public EventDeterminer(){
		operation = new DataOperation();
		datainit = new DataInit();
	}
	
	public List<JudgeResult> judgeEvent(MultipartFile file){
		List<HistoryData> hdlist = datainit.getHistoryDataFromFile(file);
		List<JudgeResult> finaljrlist = new ArrayList<JudgeResult>();
		int index=0;
		for(int i=0;i<hdlist.size();i++){
			List<JudgeResult> jrlist = new ArrayList<JudgeResult>();
			HistoryData hd = hdlist.get(i);
			boolean ifmarked = ifHistoryDataMarked(hd);
			if(!ifmarked){
				List<HistoryData> firsttargetSiganls = gethistorylistbyfirstselection(i, hdlist);
				System.out.println("current signal is "+hd.getSourceid()+" first list'size is "+firsttargetSiganls.size());
				TriggerReturn tr = ifHistoryDataTriggered(hd.getSignal_fid(), firsttargetSiganls);
				boolean iftriggered = tr.isIftriggered();
				if(iftriggered){
					System.out.println("index="+index);
					hdlist = marktriggertag(hdlist,tr.getTriggersignalid());
					List<HistoryData> targetSiganls = getTargetSignals(i,hdlist);
					jrlist = getSimulatedevent(targetSiganls);
					hdlist = markList(targetSiganls,hdlist);
					index++;
					for(JudgeResult jr:jrlist){
						finaljrlist.add(jr);
					}
				}
			}
		}

		//进行非故障类事件的判断
		for(int i=0;i<hdlist.size();i++){
			List<JudgeResult> jrlist = new ArrayList<JudgeResult>();
			HistoryData hd = hdlist.get(i);
			boolean ifmarked = ifHistoryDataMarked(hd);
			if(!ifmarked){
				List<HistoryData> firsttargetSiganls = gethistorylistbyfirstselection(i, hdlist);
				System.out.println("current signal is "+hd.getSourceid()+" first list'size is "+firsttargetSiganls.size());

				boolean iftriggered = ifHistoryDataTriggered_notFailure(hd.getSignal_fid(), firsttargetSiganls);
				if(iftriggered){
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
		}
		//
		int marked = 0;
		for(int i=0;i<hdlist.size();i++){
			if(hdlist.get(i).getHandle_tag()==1){
				marked++;
				System.out.println("信号编号"+i+" 实际事件是:"+hdlist.get(i).getEvent_detail());
			}
		}
		System.out.println("未标记信号数:"+(hdlist.size()-marked));
		return finaljrlist;
	}

	private List<HistoryData> marktriggertag(List<HistoryData> hdlist,int[] triggersignalid) {
		for(HistoryData hd:hdlist){
			for(int i=0;i<triggersignalid.length;i++){
				if(hd.getId()==triggersignalid[i])
					hd.setTrigger_tag(1);
			}
		}
		return hdlist;
	}

	private boolean ifHistoryDataMarked(HistoryData hd) {
		if(hd.getHandle_tag()==1)
			return true;
		else
			return false;
	}

	public boolean ifHistoryDataProtected(String infostr) {
		int sid = operation.getSidbySignalinfo(infostr);
		boolean res = false;
		List<OrRule> orlist = operation.getOrrulelist();
		for(OrRule or:orlist){
			if((or.getSignal_type()==2||or.getSignal_type()==4)&&(sid == or.getSid())){
				res = true;
			}
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
		boolean ifsignaltriggered = true;
		int lastid = hdlist.size()-1;
		int index = i+1;
		while(ifsignaltriggered==true&&index<lastid-1){
			List<HistoryData> firstdhlist = gethistorylistbyfirstselection(index, hdlist);
			TriggerReturn tr = ifHistoryDataTriggered(hdlist.get(index).getSignal_fid(), firstdhlist);
			if(tr.isIftriggered())
				ifsignaltriggered = false;
			Date time2 = stringToDate(hdlist.get(index).getOccur_time());
			long l2 = time2.getTime();
			int seconds = (int)(l2-l1)/(1000);
			String list_transformer = hdlist.get(index).getTransf();
			String list_bay = hdlist.get(index).getBay_id();
			int ifmark = hdlist.get(index).getHandle_tag();
			int sid = operation.getSidbySignalinfo(hdlist.get(index).getSignal_fid());
			if((ifmark==0)&&seconds<=60&&((area_id.equals(list_bay)&&transformer_name.equals(list_transformer))||(sid==2215||sid==2217))){
				res.add(hdlist.get(index));
			}
			index++;

		}
		System.out.println("targetsignal's size is"+res.size());
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
					if(t1.equals(t2)&&(jg1.equals(jg2))&&(sb1.equals(sb2))&&(info1.equals(info2))&&((act1.equals("27"))||(act1.equals("29"))||(act1.equals("31")))&&(m1==0)){
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
		int eventamount = operation.getEventamount();
		double[][] possiblevalues = new double[eventamount][2];
		List<OrRule> orlist = operation.getOrrulelist();
		int[] tsid = new int[targetSiganls.size()];
		for(int i=0;i<targetSiganls.size();i++){
			int id = operation.getSidbySignalinfo(targetSiganls.get(i).getSignal_fid());
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
			jr.setTrigger_signal_source_id(targetSiganls.get(0).getSourceid());
			jr.setTrigger_signal_id(operation.getSidbySignalinfo(targetSiganls.get(0).getSignal_fid()));
			jr.setRelated_signals(signals);
			jr.setPossible_event_id((int)possiblevalues[i][0]);
			jr.setPossible_event_value(String.valueOf(possiblevalues[i][1]));
			jr.setFinal_event_id(simulateeventid);
			jr.setFinal_event_value(String.valueOf(simulatevalue));
			jr.setActual_event_id(-1);
			jr.setActual_event_info(targetSiganls.get(0).getEvent_detail());
			jrlist.add(jr);
		}
		return jrlist;
	}

	//此方法用于初步筛选某信号之后的目标信号集   筛选条件是：时间小于60s；同变电站；未被标记
	public List<HistoryData> gethistorylistbyfirstselection(int i,List<HistoryData> hdlist){
		List<HistoryData> res = new ArrayList<HistoryData>();
		String transformer_name = hdlist.get(i).getTransf();
		Date time1 = stringToDate(hdlist.get(i).getOccur_time());
		long l1 = time1.getTime();
		res.add(hdlist.get(i));
		for(int j=i+1;j<hdlist.size();j++){
			Date time2 = stringToDate(hdlist.get(j).getOccur_time());
			long l2 = time2.getTime();
			int seconds = (int)(l2-l1)/(1000);
			String list_transformer = hdlist.get(j).getTransf();
			int ifmark = hdlist.get(j).getHandle_tag();
			if((ifmark==0)&&(transformer_name.equals(list_transformer)&&seconds<=60)){
				res.add(hdlist.get(j));
			}
		}

		return res;
	}

	//此方法用于判断信号是否是触发信号 此处的hdlist是已经筛选过的信号，筛选条件是：时间小于60s；同变电站；未被标记   然后在该方法中进行进一步筛选同间隔
	public TriggerReturn ifHistoryDataTriggered(String infostr,List<HistoryData> hdlist){
//*
		TriggerReturn tr = new TriggerReturn();
		tr.setIftriggered(false);
//*

		int sid = operation.getSidbySignalinfo(infostr);
		List<TriggerSet> tslist = operation.getTriggerSet();
		boolean ifsignalintriggerset = false;
//		boolean ifsiganlTriggered = false;
		for(TriggerSet ts:tslist){
			if(sid==ts.getSid()){
				ifsignalintriggerset = true;
				break;
			}
		}

		if(ifsignalintriggerset){
			List<HistoryData> usefullist = new ArrayList<HistoryData>();
			String area_id = hdlist.get(0).getBay_id();
			for(HistoryData hd:hdlist){
				String list_bay = hd.getBay_id();
				if(area_id.equals(list_bay)){
					usefullist.add(hd);
				}
			}
//			System.out.println("usefullist's size is: "+usefullist.size());

			int[] eidlist = operation.getTriggerEidlist();
			for(int i=0;i<eidlist.length;i++){
				List<TriggerSet> tslistforeid = operation.getTriggerSetbyEid(eidlist[i]);
				TriggerReturn newtr = historydatalistContainsTriggerset(usefullist, tslistforeid);
				tr.setIftriggered(newtr.isIftriggered());
				tr.setTriggersignalid(newtr.getTriggersignalid());
				if(tr.isIftriggered()){
					System.out.println("break out");
					break;
				}
			}
		}

//		return ifsiganlTriggered;
		return tr;

	}


	//此方法用于判断信号是否是触发信号（非故障类事件） 此处的hdlist是已经筛选过的信号，筛选条件是：时间小于60s；同变电站；未被标记   然后在该方法中进行进一步筛选同间隔
	public boolean ifHistoryDataTriggered_notFailure(String infostr,List<HistoryData> hdlist){
		//*
		TriggerReturn tr = new TriggerReturn();
		tr.setIftriggered(false);
		//*
		int sid = operation.getSidbySignalinfo(infostr);
		List<Event> elist = operation.getEvent_notFailure();
		List<OrRule> orlist = new ArrayList<OrRule>();
		for(Event e:elist){
			List<OrRule> list = operation.getOrrulelistbyEid(e.getEid());
			for(OrRule or:list){
				orlist.add(or);
			}
		}
		List<TriggerSet> tslist = new ArrayList<TriggerSet>();
		//此index用于标记orrule转triggerset中的id属性
		int index = 0;
		for(OrRule or:orlist){
			TriggerSet ts = new TriggerSet();
			ts.setEid(or.getEid());
			ts.setId(index);
			ts.setOrder(1);
			ts.setSid(or.getSid());
			tslist.add(ts);
			index++;
		}

		boolean ifsiganlTriggered = false;
//			boolean ifsiganlTriggered = false;
		for(TriggerSet ts:tslist){
			if(sid==ts.getSid()){
				ifsiganlTriggered = true;
				break;
			}
		}

		return ifsiganlTriggered;

	}

	//判断一个筛选过的historydata中是否含有一整个triggerset序列
	public TriggerReturn historydatalistContainsTriggerset(List<HistoryData> hdlist,List<TriggerSet> tslist){
		TriggerReturn tr = new TriggerReturn();
		boolean ifcontain = true;
		int[] triggersetid =new int[tslist.size()];

		boolean[] ifhdlistcontainsts = new boolean[tslist.size()];
		int index = 0;
		int triggersetindex = 0;

//		int order_index = 1;
		for(TriggerSet ts:tslist){
//			int currentOrder = ts.getOrder();
			for(HistoryData hd:hdlist){
//				if(operation.getSidbySignalinfo(hd.getSignal_fid())==ts.getSid()&&order_index==currentOrder){
				if(operation.getSidbySignalinfo(hd.getSignal_fid())==ts.getSid()&&hd.getTrigger_tag()==0){
					ifhdlistcontainsts[index] = true;
					hd.setTrigger_tag(1);
					triggersetid[triggersetindex] = hd.getId();
					triggersetindex++;

//					order_index++;
					break;
				}
			}
			index++;
		}
		for(int i=0;i<tslist.size();i++){
			if(ifhdlistcontainsts[i]==false){
				ifcontain = false;
				break;
			}
		}
		tr.setIftriggered(ifcontain);
		tr.setTriggersignalid(triggersetid);

		return tr;

	}

	//该方法也未用过
	public List<HistoryData> getTriggerSignals(List<HistoryData> hdlist){
		List<HistoryData> res = new ArrayList<HistoryData>();
		int index = 0;
		for(HistoryData hd:hdlist){
			List<HistoryData> firsthdlist = gethistorylistbyfirstselection(index, hdlist);
			TriggerReturn tr = ifHistoryDataTriggered(hd.getSignal_fid(), firsthdlist);
			if(tr.isIftriggered()){
				res.add(hd);
				System.out.println(hd.getSourceid()+hd.getTransf()+hd.getId());
			}
			index++;
		}
		return res;
	}
}
