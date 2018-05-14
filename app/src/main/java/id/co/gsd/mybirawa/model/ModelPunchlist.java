package id.co.gsd.mybirawa.model;

/**
 * Created by LENOVO on 14/12/2017.
 */

public class ModelPunchlist {
    private String punchID;
    private String punchNo;
    private String punchKeluhan;
    private String perangkatID;
    private String perangkat_name;
    private String gedung_name;
    private String lantai_name;
    private String order_date;
    private String status;
    private String img1;
    private String img2;
    private String desc;

    public ModelPunchlist(String punchID, String punchNo, String punchKeluhan, String perangkatID, String perangkat_name,
                          String gedung_name, String lantai_name, String order_date, String status, String img1, String img2, String desc) {
        this.punchID = punchID;
        this.punchNo = punchNo;
        this.punchKeluhan = punchKeluhan;
        this.perangkatID = perangkatID;
        this.perangkat_name = perangkat_name;
        this.gedung_name = gedung_name;
        this.lantai_name = lantai_name;
        this.order_date = order_date;
        this.status = status;
        this.img1 = img1;
        this.img2 = img2;
        this.desc = desc;
    }

    public String getPunchID() {
        return punchID;
    }

    public void setPunchID(String punchID) {
        this.punchID = punchID;
    }

    public String getPunchNo() {
        return punchNo;
    }

    public void setPunchNo(String punchNo) {
        this.punchNo = punchNo;
    }

    public String getPunchKeluhan() {
        return punchKeluhan;
    }

    public void setPunchKeluhan(String punchKeluhan) {
        this.punchKeluhan = punchKeluhan;
    }

    public String getPerangkatID() {
        return perangkatID;
    }

    public void setPerangkatID(String perangkatID) {
        this.perangkatID = perangkatID;
    }

    public String getPerangkat_name() {
        return perangkat_name;
    }

    public void setPerangkat_name(String perangkat_name) {
        this.perangkat_name = perangkat_name;
    }

    public String getGedung_name() {
        return gedung_name;
    }

    public void setGedung_name(String gedung_name) {
        this.gedung_name = gedung_name;
    }

    public String getLantai_name() {
        return lantai_name;
    }

    public void setLantai_name(String lantai_name) {
        this.lantai_name = lantai_name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
