package nju.zxl.signalevent.service.impl.workers;

import nju.zxl.signalevent.eo.OrRule;
import nju.zxl.signalevent.eo.Signal;
import nju.zxl.signalevent.util.DaoUtils;

import java.util.ArrayList;
import java.util.List;

public class ValueGenerator {
	
	DaoUtils daoUtils;
    public ValueGenerator(){
        daoUtils = new DaoUtils();
    }
    
	public int getSidbySignalinfo(String s){
		String[] a = s.split(",");	 
		int sid = -1;
		String sql = "select sid from `signal` where area_id =? and equip_id=? and info_id=? and act_id=?";
		sid = daoUtils.getForValue(sql, a[0], a[1], a[2], a[3]);
		return sid;	
	}
	
	public void insertValue(int orid,String value){
		String sql = "update or_rule set signal_value =? where orid =?";
		daoUtils.update(sql, value,orid);	
	}
	
	public int getAndruleAmount(){
		 int amount = 0;
 		 String sql = "select count(*) from `and_rule`";
 		 amount = Integer.parseInt(String.valueOf(daoUtils.getForValue(sql))); 
 		 return amount;
	}
	
	public List<OrRule> getOrrulelistbyArid(int arid){
		String sql = "select * from or_rule where arid=?";
		List<OrRule> list = daoUtils.getForList(OrRule.class,sql,arid);
        //System.out.println(list.size());
        return list;
	   		   
	}
	
	public List<OrRule> getOrrulelistbyEid(int eid){
		String sql = "select * from or_rule where eid=?";
		List<OrRule> list = daoUtils.getForList(OrRule.class,sql,eid);
        //System.out.println(list.size());
        return list;
	   		   
	}
	
	public List<OrRule> getOrrulelist(){
		String sql = "select * from or_rule";
		List<OrRule> list = daoUtils.getForList(OrRule.class,sql);
        //System.out.println(list.size());
        return list;
	}

	public List<Signal> getSignalBySid(int sid){
		String sql = "select * from `signal` where sid=?";
		List<Signal> list = daoUtils.getForList(Signal.class,sql,sid);
		//System.out.println(list.size());
		return list;
	}
	
	public double getSignalRate(int signal,int ifNecessary){
		List<OrRule> orlistSelected = new ArrayList<OrRule>();
		List<OrRule> orlist = getOrrulelist();
		for(OrRule or:orlist){
			if(ifNecessary == 1){
				if(or.getSignal_type()==1||or.getSignal_type()==2){
					orlistSelected.add(or);
				}
			}
			if(ifNecessary == 0){
				if(or.getSignal_type()==4){
					orlistSelected.add(or);
				}  								
			}		
		}
		double indexA = 0;
		double indexB = 0;
		for(OrRule or:orlistSelected){
			indexB++;
			if(or.getSid()==signal)
				indexA++;
		}
		double res = indexA/indexB;
		
		return res;
	}
	
	public double[] getInformationEntropy(List<OrRule> orlist,int ifNecessary){
		double[] res = new double[orlist.size()];
		double[] rates = new double[orlist.size()];
		int index = 0;
		for(OrRule or:orlist){
			rates[index] = getSignalRate(or.getSid(), ifNecessary); 
			res[index] = -Math.log(rates[index]);
			index++;
		}		
		return res;
	}
	
	//生成权值主方法
	public void getValues(){
		double coefficient = 0.8;
		int amount = getAndruleAmount();
		for(int m=1;m<=amount;m++){
			System.out.println("m="+m);
			List<OrRule> orlistbyarid = getOrrulelistbyArid(m);
			System.out.println(orlistbyarid.size());
			int ifNecessary = 0;
			if(orlistbyarid.get(0).getSignal_type()==1||orlistbyarid.get(0).getSignal_type()==2)
				ifNecessary = 1;
			
			List<OrRule> orlistbyeid = getOrrulelistbyEid(orlistbyarid.get(0).getEid());
			
			System.out.println("orlistbyeid's size is"+orlistbyeid.size());

			for(int j=0,len=orlistbyeid.size(); j<len;j++){
				if((ifNecessary==0&&(orlistbyeid.get(j).getSignal_type()==1||orlistbyeid.get(j).getSignal_type()==2))||(ifNecessary==1&&(orlistbyeid.get(j).getSignal_type()!=1&&orlistbyeid.get(j).getSignal_type()!=2))){
					orlistbyeid.remove(j);
					--len;
					--j;
				}
			}	
			double[] mediumValue = getInformationEntropy(orlistbyarid, ifNecessary);
			//计算同类型andrule数量
			int listLength = 1;
			int currentarid = orlistbyeid.get(0).getArid();
			for(OrRule or:orlistbyeid){
				if(or.getArid()!=currentarid){
					listLength++;
					currentarid = or.getArid();
				}
			}
			for(int j=0;j<mediumValue.length;j++){
				if(ifNecessary==1)
					mediumValue[j] = mediumValue[j]/(double)listLength/mediumValue.length*coefficient; 
				if(ifNecessary==0)
					mediumValue[j] = mediumValue[j]/(double)listLength/mediumValue.length*(1-coefficient); 
				
			}		
			int index = 0;
			for(OrRule or:orlistbyarid){
				insertValue(or.getOrid(), String.valueOf(mediumValue[index]));
				index++;
			}
		}	
	}
	

}
