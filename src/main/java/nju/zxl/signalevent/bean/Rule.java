package nju.zxl.signalevent.bean;

public class Rule {
    int eventId;
    String signals;
    int signal_type;
    int order;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getSignals() {
        return signals;
    }

    public void setSignals(String signals) {
        this.signals = signals;
    }

    public int getSignal_type() {
        return signal_type;
    }

    public void setSignal_type(int signal_type) {
        this.signal_type = signal_type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
