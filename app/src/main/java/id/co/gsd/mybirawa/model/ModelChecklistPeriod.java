package id.co.gsd.mybirawa.model;

/**
 * Created by LENOVO on 14/12/2017.
 */

public class ModelChecklistPeriod {
    private String period_id;
    private String period_name;
    private String period_desc;
    private String period_duration;
    private String device_type_id;
    private String device_type_name;

    public ModelChecklistPeriod(String period_id, String period_name, String period_desc, String period_duration, String device_type_id, String device_type_name) {
        this.period_id = period_id;
        this.period_name = period_name;
        this.period_desc = period_desc;
        this.period_duration = period_duration;
        this.device_type_id = device_type_id;
        this.device_type_name = device_type_name;
    }

    public String getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(String period_id) {
        this.period_id = period_id;
    }

    public String getPeriod_name() {
        return period_name;
    }

    public void setPeriod_name(String period_name) {
        this.period_name = period_name;
    }

    public String getPeriod_desc() {
        return period_desc;
    }

    public void setPeriod_desc(String period_desc) {
        this.period_desc = period_desc;
    }

    public String getPeriod_duration() {
        return period_duration;
    }

    public void setPeriod_duration(String period_duration) {
        this.period_duration = period_duration;
    }

    public String getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(String device_type_id) {
        this.device_type_id = device_type_id;
    }

    public String getDevice_type_name() {
        return device_type_name;
    }

    public void setDevice_type_name(String device_type_name) {
        this.device_type_name = device_type_name;
    }
}
