package nju.zxl.signalevent.eo;

/**
*事件类
*@author toti
*@time 2017-11-5 11:55:36
*
*/ 
 
public class Event {
	int eid;
	int area_id;
	int voltage_id;
	int equipment_id;
	int info_id;
	int type_id;
	int rank_id;

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

	public int getVoltage_id() {
		return voltage_id;
	}

	public void setVoltage_id(int voltage_id) {
		this.voltage_id = voltage_id;
	}

	public int getEquipment_id() {
		return equipment_id;
	}

	public void setEquipment_id(int equipment_id) {
		this.equipment_id = equipment_id;
	}

	public int getInfo_id() {
		return info_id;
	}

	public void setInfo_id(int info_id) {
		this.info_id = info_id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public int getRank_id() {
		return rank_id;
	}

	public void setRank_id(int rank_id) {
		this.rank_id = rank_id;
	}
}
