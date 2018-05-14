package id.co.gsd.mybirawa.model;

/**
 * Created by Biting on 12/29/2017.
 */

public class ModelDashboard {

    private int sudah;
    private int belum;
    private int total;
    private String pj_id;
    private String pj_name;
    private String pj_belum;

    public ModelDashboard(int sudah, int belum, int total) {
        this.sudah = sudah;
        this.belum = belum;
        this.total = total;
    }

    public ModelDashboard(String pj_id, String pj_name, String pj_belum) {
        this.pj_id = pj_id;
        this.pj_name = pj_name;
        this.pj_belum = pj_belum;
    }

    public int getSudah() {
        return sudah;
    }

    public void setSudah(int sudah) {
        this.sudah = sudah;
    }

    public int getBelum() {
        return belum;
    }

    public void setBelum(int belum) {
        this.belum = belum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
}
