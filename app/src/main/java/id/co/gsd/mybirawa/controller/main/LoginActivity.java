package id.co.gsd.mybirawa.controller.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.home.HomeActivity;
import id.co.gsd.mybirawa.controller.sc.MenuActivity;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import id.co.gsd.mybirawa.util.firebase.MyFirebaseInstanceIDService;

public class LoginActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private MyFirebaseInstanceIDService firebaseInstanceIDService;
    private String versionApk, imeiID;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(LoginActivity.this);
        progressDialog = new ProgressDialog(LoginActivity.this);

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        versionApk = getString(R.string.version_device);

        Button btnLogin = findViewById(R.id.btn_login);
        final EditText username = findViewById(R.id.edit_username);
        final EditText password = findViewById(R.id.edit_password);

        if (session.isLogin()) {

            if(session.getRoleId().equals("12") || session.getRoleId().equals("14") || session.getRoleId().equals("11") || session.getRoleId().equals("98")){
                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }

        }

        firebaseInstanceIDService = new MyFirebaseInstanceIDService();
        firebaseInstanceIDService.onTokenRefresh();
        final String token = firebaseInstanceIDService.token;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uName = username.getText().toString();
                String pass = password.getText().toString();
                if (uName.equals("") || pass.equals("")) {
                    Toast.makeText(getBaseContext(), "Harap lengkapi username dan password", Toast.LENGTH_LONG).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        imeiID = telephonyManager.getDeviceId();
                        System.out.println("login " + imeiID);
                        getData(uName, pass, versionApk, token, imeiID);
                    } else {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                    }
                }
            }
        });
    }


    private void getData(final String username, final String password, final String version, final String token, final String imei) {
        final String REQUEST_TAG = "get request";
        progressDialog.setMessage("Loggin In");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.URL.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("login " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("success").equals("1")) {
                                if (jsonObject.getString(ConstantUtils.USER_DATA.TAG_STATUS).equals("1")) {
                                    String id_user = jsonObject.getString(ConstantUtils.USER_DATA.TAG_USER_ID);
                                    String username = jsonObject.getString(ConstantUtils.USER_DATA.TAG_USERNAME);
                                    String nama = jsonObject.getString(ConstantUtils.USER_DATA.TAG_NAME);
                                    String jabatan = jsonObject.getString(ConstantUtils.USER_DATA.TAG_JABATAN);
                                    String id_unit = jsonObject.getString(ConstantUtils.USER_DATA.TAG_UNIT_ID);
                                    String nama_unit = jsonObject.getString(ConstantUtils.USER_DATA.TAG_UNIT_NAME);
                                    String role_id = jsonObject.getString(ConstantUtils.USER_DATA.TAG_ROLE_ID);
                                    String role_name = jsonObject.getString(ConstantUtils.USER_DATA.TAG_ROLE_NAME);
                                    String area_name = jsonObject.getString(ConstantUtils.USER_DATA.TAG_AREA_NAME);

                                    session.setUserSession(id_user, nama, username, id_unit, nama_unit, role_id, role_name, area_name, imei);

                                    if(role_id.equals("12") || session.getRoleId().equals("14") || session.getRoleId().equals("11") || session.getRoleId().equals("98")){
                                        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }

                                } else {
                                    Toast.makeText(getBaseContext(), "User sudah tidak aktif", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "Periksa Username dan Password Anda", Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(ConstantUtils.LOGIN.TAG_USERNAME, username);
                params.put(ConstantUtils.LOGIN.TAG_PASSWORD, password);
                params.put(ConstantUtils.LOGIN.TAG_VERSION, version);
                params.put(ConstantUtils.LOGIN.TAG_TOKEN, token);
                params.put(ConstantUtils.LOGIN.TAG_IMEI, imei);
                return params;
            }
        };
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(LoginActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik lagi untuk ke menu utama", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
}
