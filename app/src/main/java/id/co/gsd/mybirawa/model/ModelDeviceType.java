package id.co.gsd.mybirawa.model;

import java.io.Serializable;

/**
 * Created by Biting on 12/15/2017.
 */

public class ModelDeviceType implements Serializable {
    private String pj_id;
    private String pj_name;
    private String pj_belum;
    private String pj_icon;

    //perangkat_jenis
    public ModelDeviceType(String pj_id, String pj_name, String pj_belum, String pj_icon) {
        this.pj_id = pj_id;
        this.pj_name = pj_name;
        this.pj_belum = pj_belum;
        this.pj_icon = pj_icon;
    }

    public String getPj_id() {
        return pj_id;
    }

    public void setPj_id(String pj_id) {
        this.pj_id = pj_id;
    }

    public String getPj_name() {
        return pj_name;
    }

    public void setPj_name(String pj_name) {
        this.pj_name = pj_name;
    }

    public String getPj_belum() {
        return pj_belum;
    }

    public void setPj_belum(String pj_belum) {
        this.pj_belum = pj_belum;
    }

    public String getPj_icon() {
        return pj_icon;
    }

    public void setPj_icon(String pj_icon) {
        this.pj_icon = pj_icon;
    }
}
