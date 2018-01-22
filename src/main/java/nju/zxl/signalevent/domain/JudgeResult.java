package nju.zxl.signalevent.domain;

public class JudgeResult {
    private int trigger_signal_id;
    private String batch_id;
    private int possible_event_id;
    private String possible_event_value;
    private int final_evnet_id;
    private String final_event_value;
    private int actual_event_id;

    public int getTrigger_signal_id() {
        return trigger_signal_id;
    }

    public void setTrigger_signal_id(int trigger_signal_id) {
        this.trigger_signal_id = trigger_signal_id;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
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

    public int getFinal_evnet_id() {
        return final_evnet_id;
    }

    public void setFinal_evnet_id(int final_evnet_id) {
        this.final_evnet_id = final_evnet_id;
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
}
