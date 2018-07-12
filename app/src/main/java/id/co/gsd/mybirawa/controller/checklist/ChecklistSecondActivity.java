package id.co.gsd.mybirawa.controller.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.TimeAdapter;
import id.co.gsd.mybirawa.model.ModelDeviceDetail;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class ChecklistSecondActivity extends AppCompatActivity {

    public int countTab = 0;
    private Toolbar toolbar;
    private TextView textToolbar;
    private ChecklistSecondTabParentFragment fragmentParent;
    private ProgressBar progressBar;
    private List<ModelDeviceDetail> listModel;
    private ModelDeviceDetail model;
    private String PJID;
    String lantai, unit, role, period, selisih, percent, judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_second);

        toolbar = findViewById(R.id.toolbar);
        textToolbar = findViewById(R.id.tv_toolbar_checklist);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        getIDs();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent dataIntent = getIntent();
        lantai = dataIntent.getStringExtra("lantai");
        unit = dataIntent.getStringExtra("unit");
        role = dataIntent.getStringExtra("role");
        period = dataIntent.getStringExtra(ConstantUtils.PERIOD.TAG_ID);
        selisih = dataIntent.getStringExtra("selisih");
        PJID = dataIntent.getStringExtra(ConstantUtils.DEVICE.TAG_PJ_ID);
        String PJName = dataIntent.getStringExtra(ConstantUtils.DEVICE.TAG_PJ_NAME);
        percent = dataIntent.getStringExtra("percent");
        judul = dataIntent.getStringExtra("judul");

        if (role.equals("5")) {
            getDataHK(lantai, unit, role, period, PJID);
        } else {
            getData(lantai, unit, role, period, PJID, selisih);
        }

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

    //GET DATA NON HK
    private void getData(final String lantai, final String unit, final String role, final String period, final String pj_id, final String selisih) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.DEVICE_DETAIL + lantai + "/" + unit + "/" + role + "/" + period + "/" + pj_id + "/" + selisih,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0,9).equals("<!DOCTYPE")) {
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                getLoadError();
                            }
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
                                        //fragmentParent.addPage(listModel.get(a).getDevice_name(), listModel.get(a).getDevice_id());
                                        fragmentParent.addPage(listModel.get(a).getDevice_code(), listModel.get(a).getDevice_id());
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
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Time Out Error", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Server Error", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof NetworkError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Something wrong with connection", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ParseError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, JSON Parse Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);
    }

    private void getLoadError() {
        if (role.equals("5")) {
            getDataHK(lantai, unit, role, period, PJID);
        } else {
            getData(lantai, unit, role, period, PJID, selisih);
        }
    }

    //GET DATA HK
    private void getDataHK(final String lantai, final String unit, final String role, final String period, final String pj_id) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.DEVICE_DETAIL_HK + lantai + "/" + unit + "/" + role + "/" + period + "/" + pj_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.contains("<!DOCTYPE")) {
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                getLoadError();
                            }
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
                                        //fragmentParent.addPage(listModel.get(a).getDevice_id(), listModel.get(a).getDevice_id());
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
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Time Out Error", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Server Error", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof NetworkError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, Something wrong with connection", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ParseError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChecklistSecondActivity.this, "Oops, JSON Parse Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);
    }
}
