package nju.zxl.signalevent.eo;

public class HistoryData {
    int id;
    int sourceid;
    public int getSourceid() {
		return sourceid;
	}

	public void setSourceid(int sourceid) {
		this.sourceid = sourceid;
	}

	String transf;
    String bay;
    String occur_time;
    String content;
    String signal_type;
    String event_type;
    String prov_bay;
    String bay_id;
    String prov_device;
    String device_id;
    String prov_template;
    String template_id;
    String action_attr;
    String action_attr_id;
    String signal_fid;
    int handle_tag;
    int batch_id;//批次
    int trigger_tag;//用于标记是否是触发信号，不用于目标信号集合中的第二次触发信号判断

    public int getTrigger_tag() {
        return trigger_tag;
    }

    public void setTrigger_tag(int trigger_tag) {
        this.trigger_tag = trigger_tag;
    }


    public int getHandle_tag() {
        return handle_tag;
    }

    public void setHandle_tag(int handle_tag) {
        this.handle_tag = handle_tag;
    }

    public int getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransf() {
        return transf;
    }

    public void setTransf(String transf) {
        this.transf = transf;
    }

    public String getBay() {
        return bay;
    }

    public void setBay(String bay) {
        this.bay = bay;
    }

    public String getOccur_time() {
        return occur_time;
    }

    public void setOccur_time(String occur_time) {
        this.occur_time = occur_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignal_type() {
        return signal_type;
    }

    public void setSignal_type(String signal_type) {
        this.signal_type = signal_type;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getProv_bay() {
        return prov_bay;
    }

    public void setProv_bay(String prov_bay) {
        this.prov_bay = prov_bay;
    }

    public String getBay_id() {
        return bay_id;
    }

    public void setBay_id(String bay_id) {
        this.bay_id = bay_id;
    }

    public String getProv_device() {
        return prov_device;
    }

    public void setProv_device(String prov_device) {
        this.prov_device = prov_device;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getProv_template() {
        return prov_template;
    }

    public void setProv_template(String prov_template) {
        this.prov_template = prov_template;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getAction_attr() {
        return action_attr;
    }

    public void setAction_attr(String action_attr) {
        this.action_attr = action_attr;
    }

    public String getAction_attr_id() {
        return action_attr_id;
    }

    public void setAction_attr_id(String action_attr_id) {
        this.action_attr_id = action_attr_id;
    }

    public String getSignal_fid() {
        return signal_fid;
    }

    public void setSignal_fid(String signal_fid) {
        this.signal_fid = signal_fid;
    }
}
