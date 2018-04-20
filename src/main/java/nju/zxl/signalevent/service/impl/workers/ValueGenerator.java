package nju.zxl.signalevent.service.impl.workers;

import nju.zxl.signalevent.eo.AndRule;
import nju.zxl.signalevent.eo.OrRule;
import nju.zxl.signalevent.util.DaoUtils;

import java.util.ArrayList;
import java.util.List;
public class ValueGenerator {
	
	DaoUtils daoUtils;
	DataOperation operation;
    public ValueGenerator(){
        daoUtils = new DaoUtils();
        operation = new DataOperation();
    }
    
	
	public double getSignalRate(int signal,int ifNecessary){
		List<OrRule> orlistSelected = new ArrayList<OrRule>();
		List<OrRule> orlist = operation.getOrrulelist();
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
		int amount = operation.getAndruleAmount();
		List<AndRule> arList = operation.getAndrulelist();
		for(int m=0;m<amount;m++){
			System.out.println("m="+m);
			List<OrRule> orlistbyarid = operation.getOrrulelistbyArid(arList.get(m).getArid());
			System.out.println(orlistbyarid.size());
			int ifNecessary = 0;
			if(orlistbyarid.get(0).getSignal_type()==1||orlistbyarid.get(0).getSignal_type()==2)
				ifNecessary = 1;
			
			List<OrRule> orlistbyeid = operation.getOrrulelistbyEid(orlistbyarid.get(0).getEid());
			
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
				operation.insertValue(or.getOrid(), String.valueOf(mediumValue[index]));
				index++;
			}
		}	
	}
	

}
