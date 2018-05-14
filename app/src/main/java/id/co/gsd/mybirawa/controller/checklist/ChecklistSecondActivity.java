package id.co.gsd.mybirawa.controller.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.model.ModelDeviceDetail;
import id.co.gsd.mybirawa.util.CameraManager;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class ChecklistSecondActivity extends AppCompatActivity {

    public int countTab = 0;
    private SessionManager session;
    private CameraManager camMan;
    private Toolbar toolbar;
    private TextView textToolbar;
    private ChecklistSecondTabParentFragment fragmentParent;
    private ProgressBar progressBar;
    private List<ModelDeviceDetail> listModel;
    private ModelDeviceDetail model;
    private String PJID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_second);

        toolbar = findViewById(R.id.toolbar);
        textToolbar = findViewById(R.id.tv_toolbar_checklist);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        getIDs();

        session = new SessionManager(ChecklistSecondActivity.this);
        camMan = new CameraManager();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent dataIntent = getIntent();
        String lantai = dataIntent.getStringExtra("lantai");
        String unit = dataIntent.getStringExtra("unit");
        String role = dataIntent.getStringExtra("role");
        String period = dataIntent.getStringExtra(ConstantUtils.PERIOD.TAG_ID);
        String selisih = dataIntent.getStringExtra("selisih");
        PJID = dataIntent.getStringExtra(ConstantUtils.DEVICE.TAG_PJ_ID);
        String PJName = dataIntent.getStringExtra(ConstantUtils.DEVICE.TAG_PJ_NAME);
        getData(lantai, unit, role, period, PJID, selisih);

        toolbar.setTitle("");
        textToolbar.setText(PJName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getIDs() {
        fragmentParent = (ChecklistSecondTabParentFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragmentParent);
    }

    //GET DATA
    private void getData(final String lantai, final String unit, final String role, final String period, final String pj_id, final String selisih) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.DEVICE_DETAIL + lantai + "/" + unit + "/" + role + "/" + period + "/" + pj_id + "/" + selisih,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DEVICE.TAG_TITLE2);
                            listModel = new ArrayList<ModelDeviceDetail>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String pjID = object.getString(ConstantUtils.DEVICE.TAG_PJ_ID);
                                    String pID = object.getString(ConstantUtils.DEVICE.TAG_PERANGKAT_ID);
                                    String pCode = object.getString(ConstantUtils.DEVICE.TAG_PERANGKAT_CODE);
                                    String pName = object.getString(ConstantUtils.DEVICE.TAG_PERANGKAT_NAME);
                                    String pMerk = object.getString(ConstantUtils.DEVICE.TAG_PERANGKAT_MERK);
                                    String pCap = object.getString(ConstantUtils.DEVICE.TAG_PERANGKAT_CAP);
                                    String pYear = object.getString(ConstantUtils.DEVICE.TAG_PERANGKAT_YEAR);
                                    String pRms = object.getString(ConstantUtils.DEVICE.TAG_PERANGKAT_RMS);
                                    model = new ModelDeviceDetail(pjID, pID, pCode, pName, pMerk, pCap, pYear, pRms);
                                    listModel.add(model);
                                }

                                for (int a = 0; a < listModel.size(); a++) {
                                    if (!listModel.get(a).getDevice_name().equals("")) {
                                        fragmentParent.addPage(listModel.get(a).getDevice_name(), listModel.get(a).getDevice_id());
                                        countTab++;
                                    } else {
                                        Toast.makeText(ChecklistSecondActivity.this, "Page name is empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);
    }
}
