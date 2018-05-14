package id.co.gsd.mybirawa.model;

/**
 * Created by Biting on 3/5/2018.
 */

public class ModelHistory {

    private String namaUser;
    private String tglSubmit;
    private String desc;

    public ModelHistory(String namaUser, String tglSubmit, String desc) {
        this.namaUser = namaUser;
        this.tglSubmit = tglSubmit;
        this.desc = desc;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getTglSubmit() {
        return tglSubmit;
    }

    public void setTglSubmit(String tglSubmit) {
        this.tglSubmit = tglSubmit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
