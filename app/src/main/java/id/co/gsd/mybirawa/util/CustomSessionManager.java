package id.co.gsd.mybirawa.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LENOVO on 10/09/2017.
 */

public class CustomSessionManager {
    // Sharedpref file name
    private static final String PREF_NAME = "BirawaCustomPref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static String ACTIVITY_KEY;
    public static String ACTIVITY_KEY_1;
    public static String ACTIVITY_KEY_2;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context cont;

    // Constructor
    public CustomSessionManager(Context context, String key) {
        cont = context;
        ACTIVITY_KEY = key;
        preferences = cont.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void destroySession() {
        editor.clear();
        editor.commit();
    }

    public void setData(String key, String value) {
        // Storing login value as TRUE
        editor.putString(ACTIVITY_KEY + key, value);
        editor.commit();
    }

    public String getData(String key) {
        return preferences.getString(ACTIVITY_KEY + key, "");
    }
}