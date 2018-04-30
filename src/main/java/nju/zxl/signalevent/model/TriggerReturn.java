package nju.zxl.signalevent.model;

//判断信号是否是触发信号的返回值，属性包括是否触发的布尔值与触发信号集合的id集合
public class TriggerReturn {
	boolean iftriggered;
	int[] triggersignalid;
	public boolean isIftriggered() {
		return iftriggered;
	}
	public void setIftriggered(boolean iftriggered) {
		this.iftriggered = iftriggered;
	}
	public int[] getTriggersignalid() {
		return triggersignalid;
	}
	public void setTriggersignalid(int[] triggersignalid) {
		this.triggersignalid = triggersignalid;
	}
	

}
