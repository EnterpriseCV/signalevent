package nju.zxl.signalevent.service.impl.workers;

import java.util.ArrayList;
import java.util.List;

import nju.zxl.signalevent.model.HistoryData;
import nju.zxl.signalevent.bean.Rule;
import nju.zxl.signalevent.model.*;
import org.springframework.web.multipart.MultipartFile;

public class DataInit {
	DataOperation operation;
	public DataInit(){
		operation = new DataOperation();
	}

	public List<Rule> getRulesFromFile(MultipartFile file){
		List<Rule> ruleList = new ArrayList<Rule>();

		return ruleList;
	}

	public List<HistoryData> getHistoryDataFromFile(MultipartFile file){

		List<HistoryData> hdlist = new ArrayList<HistoryData>();

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

		return slist;
	}

	public List getEventsFromFile(MultipartFile file){
		ArrayList<Event> elist = new ArrayList<Event>();

		return elist;
	}
}
