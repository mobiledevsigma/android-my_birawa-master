package id.co.gsd.mybirawa.model;

import java.io.Serializable;

/**
 * Created by Biting on 12/15/2017.
 */

public class ModelDeviceDetail implements Serializable {
    private String device_type_id;
    private String device_id;
    private String device_code;
    private String device_name;
    private String device_merk;
    private String device_cap;
    private String device_year;
    private String device_rms;

    //perangkat
    public ModelDeviceDetail(String type_id, String device_id, String device_code, String device_name, String device_merk, String device_cap, String device_year, String device_rms) {
        this.device_type_id = type_id;
        this.device_id = device_id;
        this.device_code = device_code;
        this.device_name = device_name;
        this.device_merk = device_merk;
        this.device_cap = device_cap;
        this.device_year = device_year;
        this.device_rms = device_rms;
    }

    public String getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(String device_type_id) {
        this.device_type_id = device_type_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_merk() {
        return device_merk;
    }

    public void setDevice_merk(String device_merk) {
        this.device_merk = device_merk;
    }

    public String getDevice_cap() {
        return device_cap;
    }

    public void setDevice_cap(String device_cap) {
        this.device_cap = device_cap;
    }

    public String getDevice_year() {
        return device_year;
    }

    public void setDevice_year(String device_year) {
        this.device_year = device_year;
    }

    public String getDevice_rms() {
        return device_rms;
    }

    public void setDevice_rms(String device_rms) {
        this.device_rms = device_rms;
    }
}
