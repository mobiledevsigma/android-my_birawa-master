package id.co.gsd.mybirawa.util;

/**
 * Created by Gilang on 12-Apr-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import id.co.gsd.mybirawa.util.connection.ConstantUtils;


public class SessionManager {

    private static final String PREFER_NAME = "MyBirawa";
    private static final String IS_LOGIN = "IsLoggedIn";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserSession(String id, String name, String username, String id_unit, String unit_name,
                               String role_id, String role_name, String area_name, String imei) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean("notifStatus", true);
        editor.putString(ConstantUtils.USER_DATA.TAG_USER_ID, id);
        editor.putString(ConstantUtils.USER_DATA.TAG_NAME, name);
        editor.putString(ConstantUtils.USER_DATA.TAG_USERNAME, username);
        editor.putString(ConstantUtils.USER_DATA.TAG_UNIT_ID, id_unit);
        editor.putString(ConstantUtils.USER_DATA.TAG_UNIT_NAME, unit_name);
        editor.putString(ConstantUtils.USER_DATA.TAG_ROLE_ID, role_id);
        editor.putString(ConstantUtils.USER_DATA.TAG_ROLE_NAME, role_name);
        editor.putString(ConstantUtils.USER_DATA.TAG_AREA_NAME, area_name);
        editor.putString(ConstantUtils.USER_DATA.TAG_IMEI, imei);

        // commit changes
        editor.commit();
    }

    public String getPeriodId() {
        return pref.getString("tempPeriod", "");
    }

    public void setPeriodId(String p) {

        editor.putString("tempPeriod", p);
        editor.commit();
    }

    public void setDeviceTypeId(String p) {

        editor.putString("tempDeviceType", p);
        editor.commit();
    }

    public String getDeviceTypeId(){
        return pref.getString("tempDeviceType", "");
    }

    public String getId() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_USER_ID, "");
    }

    public String getUserName() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_USERNAME, "");
    }

    public String getPassword() {
        return pref.getString("password", "");
    }

    public String getName() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_NAME, "");
    }
    public String getJabatan() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_JABATAN, "");
    }

    public String getIdUnit() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_UNIT_ID, "");
    }

    public String getNamaUnit() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_UNIT_NAME, "");
    }

    public String getRoleId() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_ROLE_ID, "");
    }

    public String getRoleName() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_ROLE_NAME, "");
    }
    public String getAreaName() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_AREA_NAME, "");
    }

    public String getImei() {
        return pref.getString(ConstantUtils.USER_DATA.TAG_IMEI, "");
    }

    public boolean isLogin() {
        return pref.getBoolean("IsUserLoggedIn", false);
    }


    public void setCheckedServer(boolean status) {

        editor.putBoolean("checked_server", status);
        editor.commit();
    }

    public boolean getNotificationStatus() {
        return pref.getBoolean("notifStatus", false);
    }


    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
