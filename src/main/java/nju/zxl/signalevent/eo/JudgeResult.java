package nju.zxl.signalevent.eo;
/**
*判断结果类
*@author toti
*@time 2018-1-18 16:46:40
*
*/ 

public class JudgeResult {
	int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTrigger_signal_id() {
		return trigger_signal_id;
	}
	public void setTrigger_signal_id(int trigger_signal_id) {
		this.trigger_signal_id = trigger_signal_id;
	}
	
	public int getPossible_event_id() {
		return possible_event_id;
	}
	public void setPossible_event_id(int possible_event_id) {
		this.possible_event_id = possible_event_id;
	}
	public String getPossible_event_value() {
		return possible_event_value;
	}
	public void setPossible_event_value(String possible_event_value) {
		this.possible_event_value = possible_event_value;
	}
	public int getFinal_event_id() {
		return final_event_id;
	}
	public void setFinal_event_id(int final_event_id) {
		this.final_event_id = final_event_id;
	}
	public String getFinal_event_value() {
		return final_event_value;
	}
	public void setFinal_event_value(String final_event_value) {
		this.final_event_value = final_event_value;
	}
	public int getActual_event_id() {
		return actual_event_id;
	}
	public void setActual_event_id(int actual_event_id) {
		this.actual_event_id = actual_event_id;
	}
	int trigger_signal_source_id;
	int trigger_signal_id;
	int batch_id;
	String related_signals;
	public int getTrigger_signal_source_id() {
		return trigger_signal_source_id;
	}
	public void setTrigger_signal_source_id(int trigger_signal_source_id) {
		this.trigger_signal_source_id = trigger_signal_source_id;
	}
	public String getRelated_signals() {
		return related_signals;
	}
	public void setRelated_signals(String related_signals) {
		this.related_signals = related_signals;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	int possible_event_id;
	String possible_event_value;
	int final_event_id;
	String final_event_value;
	int actual_event_id;
	String actual_event_info;
	public String getActual_event_info() {
		return actual_event_info;
	}
	public void setActual_event_info(String actual_event_info) {
		this.actual_event_info = actual_event_info;
	}
	public int getBatch_id() {
		return batch_id;
	}

}
