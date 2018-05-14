package id.co.gsd.mybirawa.model;

/**
 * Created by Biting on 12/14/2017.
 */

public class ModelBuilding {

    private String gedungID;
    private String gedungMame;
    private String gedungAddress;
    private String gedungLatitude;
    private String gedungLongitude;
    private String lantaiID;
    private String lantaiName;

    public ModelBuilding(String gedungID, String gedungMame, String gedungAddress, String gedungLatitude, String gedungLongitude, String lantaiID, String lantaiName) {
        this.gedungID = gedungID;
        this.gedungMame = gedungMame;
        this.gedungAddress = gedungAddress;
        this.gedungLatitude = gedungLatitude;
        this.gedungLongitude = gedungLongitude;
        this.lantaiID = lantaiID;
        this.lantaiName = lantaiName;
    }

    public String getGedungID() {
        return gedungID;
    }

    public void setGedungID(String gedungID) {
        this.gedungID = gedungID;
    }

    public String getGedungMame() {
        return gedungMame;
    }

    public void setGedungMame(String gedungMame) {
        this.gedungMame = gedungMame;
    }

    public String getGedungAddress() {
        return gedungAddress;
    }

    public void setGedungAddress(String gedungAddress) {
        this.gedungAddress = gedungAddress;
    }

    public String getGedungLatitude() {
        return gedungLatitude;
    }

    public void setGedungLatitude(String gedungLatitude) {
        this.gedungLatitude = gedungLatitude;
    }

    public String getGedungLongitude() {
        return gedungLongitude;
    }

    public void setGedungLongitude(String gedungLongitude) {
        this.gedungLongitude = gedungLongitude;
    }

    public String getLantaiID() {
        return lantaiID;
    }

    public void setLantaiID(String lantaiID) {
        this.lantaiID = lantaiID;
    }

    public String getLantaiName() {
        return lantaiName;
    }

    public void setLantaiName(String lantaiName) {
        this.lantaiName = lantaiName;
    }
}
