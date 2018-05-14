package id.co.gsd.mybirawa.model;

/**
 * Created by Biting on 1/26/2018.
 */

public class ModelPunchlistSpv {

    private String punchID;
    private String punchNo;
    private String roleName;
    private String gedungName;
    private String lantaiName;
    private String itemName;
    private String keluhan;
    private String status;
    private String img1;
    private String img2;
    private String desc;
    private String orderDate;
    private String submitdate;

    public ModelPunchlistSpv(String punchID, String punchNo, String roleName, String gedungName, String lantaiName, String itemName,
                             String keluhan, String status, String img1, String img2, String desc, String orderDate, String submitdate) {
        this.punchID = punchID;
        this.punchNo = punchNo;
        this.roleName = roleName;
        this.gedungName = gedungName;
        this.lantaiName = lantaiName;
        this.itemName = itemName;
        this.keluhan = keluhan;
        this.status = status;
        this.img1 = img1;
        this.img2 = img2;
        this.desc = desc;
        this.orderDate = orderDate;
        this.submitdate = submitdate;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getGedungName() {
        return gedungName;
    }

    public void setGedungName(String gedungName) {
        this.gedungName = gedungName;
    }

    public String getLantaiName() {
        return lantaiName;
    }

    public void setLantaiName(String lantaiName) {
        this.lantaiName = lantaiName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getSubmitdate() {
        return submitdate;
    }

    public void setSubmitdate(String submitdate) {
        this.submitdate = submitdate;
    }
}
