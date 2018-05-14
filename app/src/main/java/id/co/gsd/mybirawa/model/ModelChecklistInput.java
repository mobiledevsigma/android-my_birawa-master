package id.co.gsd.mybirawa.model;

import id.co.gsd.mybirawa.R;

/**
 * Created by Biting on 12/15/2017.
 */

public class ModelChecklistInput {

    private String checklist_id;
    private String checklist_name;
    private String checklist_std;
    private String checklist_type;
    private String deskripsi_id;
    private String deskripsi;
    private String isSeparator;
    public int current = 0;
    public static final int[] RadioID= {-1, R.id.radioNo, R.id.radioYes};

    public ModelChecklistInput(String checklist_id, String checklist_name, String checklist_std, String checklist_type) {
        this.checklist_id = checklist_id;
        this.checklist_name = checklist_name;
        this.checklist_std = checklist_std;
        this.checklist_type = checklist_type;
    }

    public ModelChecklistInput(String deskripsi_id, String deskripsi) {
        this.deskripsi_id = deskripsi_id;
        this.deskripsi = deskripsi;
    }

    public ModelChecklistInput(String checklist_id, String checklist_name, String checklist_std, String checklist_type, String deskripsi_id, String deskripsi, String isSeparator) {
        this.checklist_id = checklist_id;
        this.checklist_name = checklist_name;
        this.checklist_std = checklist_std;
        this.checklist_type = checklist_type;
        this.deskripsi_id = deskripsi_id;
        this.deskripsi = deskripsi;
        this.isSeparator = isSeparator;
    }

    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    public String getChecklist_name() {
        return checklist_name;
    }

    public void setChecklist_name(String checklist_name) {
        this.checklist_name = checklist_name;
    }

    public String getChecklist_std() {
        return checklist_std;
    }

    public void setChecklist_std(String checklist_std) {
        this.checklist_std = checklist_std;
    }

    public String getChecklist_type() {
        return checklist_type;
    }

    public void setChecklist_type(String checklist_type) {
        this.checklist_type = checklist_type;
    }

    public String getDeskripsi_id() {
        return deskripsi_id;
    }

    public void setDeskripsi_id(String deskripsi_id) {
        this.deskripsi_id = deskripsi_id;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getIsSeparator() {
        return isSeparator;
    }

    public void setIsSeparator(String isSeparator) {
        this.isSeparator = isSeparator;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public static int[] getRadioID() {
        return RadioID;
    }
}
