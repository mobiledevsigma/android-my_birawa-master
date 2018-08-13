package id.co.gsd.mybirawa.controller.checklist;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.DeviceTypeAdapter;
import id.co.gsd.mybirawa.model.ModelDeviceType;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * Created by Biting on 1/8/2018.
 */

public class ChecklistFirstActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textToolbar;
    private ProgressBar progressBar;
    private TextView tv_percent_bar;
    private SpinnerDialog spinnerGedung;
    private LinearLayout lay_spin_gedung;
    private TextView tv_spin_gedung;
    private SpinnerDialog spinnerLantai;
    private LinearLayout lay_spin_lantai;
    private TextView tv_spin_lantai;
    private ListView listView;
    private LinearLayout lay_no_data;
    private ProgressBar progressLoading;
    private ModelDeviceType model;
    private List<ModelDeviceType> listModel;
    private DeviceTypeAdapter adapter;
    private List<String> listName;
    private String unitID, roleID, periodID, selisih, idGedung, idLantai, judul;
    private int percent;
    private Handler handler;
    private Runnable runnable;
    private Timer timer;
    private int i = 0;
    private ArrayList<String> listGedung = new ArrayList<>();
    private ArrayList<String> listGedungID = new ArrayList<>();
    private ArrayList<String> listLantai = new ArrayList<>();
    private ArrayList<String> listLantaiID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_first);
        progressLoading = findViewById(R.id.progressBar2);
        toolbar = findViewById(R.id.toolbar);
        textToolbar = findViewById(R.id.tv_toolbar_checklist);
        progressBar = findViewById(R.id.bar_progress);
        tv_percent_bar = findViewById(R.id.tv_percent_bar);
        lay_spin_gedung = findViewById(R.id.lay_spin_gedung);
        tv_spin_gedung = findViewById(R.id.tv_spin_gedung);
        lay_spin_lantai = findViewById(R.id.lay_spin_lantai);
        tv_spin_lantai = findViewById(R.id.tv_spin_lantai);
        listView = findViewById(R.id.listview_pj);
        lay_no_data = findViewById(R.id.lay_no_data);

        progressBar.setSystemUiVisibility(View.GONE);
        progressLoading.setVisibility(View.GONE);

        Intent intent = getIntent();
        unitID = intent.getStringExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID);
        roleID = intent.getStringExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID);
        periodID = intent.getStringExtra(ConstantUtils.PERIOD.TAG_ID);
        percent = intent.getIntExtra("percent", 0);
        selisih = intent.getStringExtra("selisih");
        judul = intent.getStringExtra("judul");

        toolbar.setTitle("");
        textToolbar.setText(judul);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setBarProgress();
        getDataGedung(unitID, roleID, periodID, selisih);

        spinnerGedung = new SpinnerDialog(ChecklistFirstActivity.this, listGedung, "Pilih Gedung");
        lay_spin_gedung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerGedung.showSpinerDialog();
                Typeface externalFont = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    externalFont = getResources().getFont(R.font.nexa_light);
//                    ((TextView) view).setTypeface(externalFont);
                }
            }
        });
        spinnerGedung.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int i) {
                tv_spin_gedung.setText(lantai);
                idGedung = listGedungID.get(i);
                getDataLantai(idGedung, unitID, roleID, periodID, selisih);
                tv_spin_lantai.setText("Pilih Lantai");
                listView.setVisibility(View.GONE);
                lay_no_data.setVisibility(View.VISIBLE);
            }
        });

        spinnerLantai = new SpinnerDialog(ChecklistFirstActivity.this, listLantai, "Pilih Lantai");
        lay_spin_lantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerLantai.showSpinerDialog();
                Typeface externalFont = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    // externalFont = getResources().getFont(R.font.nexa_light);
                    //((TextView) view).setTypeface(externalFont);
                }
            }
        });
        spinnerLantai.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int i) {
                tv_spin_lantai.setText(lantai);
                idLantai = listLantaiID.get(i);
                listView.setVisibility(View.GONE);
                lay_no_data.setVisibility(View.VISIBLE);
                getDataPJ(idLantai, unitID, roleID, periodID, selisih);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    //SET HORIZONTAL BAR
    private void setBarProgress() {
        if (percent < 50) {
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_red));
        } else if (percent >= 50 && percent <= 75) {
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_yellow));
        } else {
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_green));
        }
        progressBar.setMax(100);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (i <= percent) {
                    progressBar.setProgress(i);
                    tv_percent_bar.setText(String.format("%.0f%%", i * 1f));
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    timer.cancel();
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               i = i + 1;
                               handler.post(runnable);
                           }
                       }, 100, 50
        );
    }

    private void getLoadErrorGedung() {
        getDataGedung(unitID, roleID, periodID, selisih);
    }

    private void getLoadErrorLantai() {
        getDataLantai(idGedung, unitID, roleID, periodID, selisih);
    }

    private void getLoadErrorPJ() {
        getDataPJ(idLantai, unitID, roleID, periodID, selisih);
    }

    //GET DATA GEDUNG
    private void getDataGedung(String unit, String role, String period, String hari) {
        final String REQUEST_TAG = "get request";
        progressLoading.setVisibility(View.VISIBLE);
        lay_spin_gedung.setEnabled(false);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.BUILDING + unit + "/" + role + "/" + period + "/" + hari,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                getLoadErrorGedung();
                            }

                            System.out.println("checkFirst " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE);
                            listName = new ArrayList<String>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String Gid = object.getString(ConstantUtils.BUILDING.TAG_BUILDING_ID);
                                    String nama = object.getString(ConstantUtils.BUILDING.TAG_NAME);
                                    String alamat = object.getString(ConstantUtils.BUILDING.TAG_ADDRESS);
                                    String latit = object.getString(ConstantUtils.BUILDING.TAG_LAT);
                                    String longi = object.getString(ConstantUtils.BUILDING.TAG_LONG);

                                    listGedungID.add(Gid);
                                    listGedung.add(nama);

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE2);
                                    for (int a = 0; a < jsonArray1.length(); a++) {
                                        JSONObject obj = jsonArray1.getJSONObject(a);
                                        String Lid = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_ID);
                                        String Lname = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_NAME);
                                    }
                                }

                                progressLoading.setVisibility(View.GONE);
                                lay_spin_gedung.setEnabled(true);
                            } else {
                                progressLoading.setVisibility(View.GONE);
                                Toast.makeText(ChecklistFirstActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressLoading.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressLoading.setVisibility(View.GONE);
                        Toast.makeText(ChecklistFirstActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        //set time out
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(ChecklistFirstActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    //GET DATA LANTAI
    private void getDataLantai(final String geID, String unit, String role, String period, String hari) {
        final String REQUEST_TAG = "get request";
        progressLoading.setVisibility(View.VISIBLE);
        lay_spin_lantai.setEnabled(false);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.BUILDING + unit + "/" + role + "/" + period + "/" + hari,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                System.out.println("zaa " + response.substring(0, 9));
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                getLoadErrorLantai();
                            }

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE);
                            listLantai.clear();
                            listLantaiID.clear();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String Gid = object.getString(ConstantUtils.BUILDING.TAG_BUILDING_ID);
                                    String nama = object.getString(ConstantUtils.BUILDING.TAG_NAME);
                                    String alamat = object.getString(ConstantUtils.BUILDING.TAG_ADDRESS);
                                    String latit = object.getString(ConstantUtils.BUILDING.TAG_LAT);
                                    String longi = object.getString(ConstantUtils.BUILDING.TAG_LONG);

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE2);
                                    for (int a = 0; a < jsonArray1.length(); a++) {
                                        JSONObject obj = jsonArray1.getJSONObject(a);
                                        String Lid = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_ID);
                                        String Lname = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_NAME);

                                        if (Gid.equals(geID)) {
                                            if (jsonArray1.length() > 0) {
                                                listLantai.add(Lname);
                                                listLantaiID.add(Lid);
                                            }
                                        }
                                    }
                                }
                                progressLoading.setVisibility(View.GONE);
                                lay_spin_lantai.setEnabled(true);
                            } else {
                                progressLoading.setVisibility(View.GONE);
                                listLantaiID.clear();
                                listLantai.clear();
                                Toast.makeText(ChecklistFirstActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressLoading.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressLoading.setVisibility(View.GONE);
                        Toast.makeText(ChecklistFirstActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        //set time out
        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(ChecklistFirstActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    //GET DATA
    private void getDataPJ(final String lantai, final String unit, final String role, final String period, final String selisih) {
        final String REQUEST_TAG = "get request";
        progressLoading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.DEVICE_TYPE + lantai + "/" + unit + "/" + role + "/" + period + "/" + selisih,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                System.out.println("zaa " + response.substring(0, 9));
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                getLoadErrorPJ();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DEVICE.TAG_TITLE);
                            listModel = new ArrayList<ModelDeviceType>();

                            if (jsonArray.length() > 0) {
                                listView.setVisibility(View.VISIBLE);
                                lay_no_data.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String pjID = object.getString(ConstantUtils.DEVICE.TAG_PJ_ID);
                                    String pjName = object.getString(ConstantUtils.DEVICE.TAG_PJ_NAME);
                                    String pjBlm = object.getString(ConstantUtils.DEVICE.TAG_PJ_BLM);
                                    String icon = object.getString(ConstantUtils.DEVICE.TAG_PJ_ICON);
                                    model = new ModelDeviceType(pjID, pjName, pjBlm, icon);
                                    listModel.add(model);
                                }

                                adapter = new DeviceTypeAdapter(ChecklistFirstActivity.this, listModel);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(ChecklistFirstActivity.this, ChecklistSecondActivity.class);
                                        intent.putExtra("lantai", lantai);
                                        intent.putExtra("unit", unit);
                                        intent.putExtra("role", role);
                                        intent.putExtra(ConstantUtils.PERIOD.TAG_ID, period);
                                        intent.putExtra("selisih", selisih);
                                        intent.putExtra(ConstantUtils.DEVICE.TAG_PJ_ID, listModel.get(i).getPj_id());
                                        intent.putExtra(ConstantUtils.DEVICE.TAG_PJ_NAME, listModel.get(i).getPj_name());
                                        intent.putExtra("percent", percent);
                                        intent.putExtra("judul", judul);
                                        startActivity(intent);
                                    }
                                });
                                progressLoading.setVisibility(View.GONE);

                            } else {
                                progressLoading.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                lay_no_data.setVisibility(View.VISIBLE);
                                Toast.makeText(ChecklistFirstActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressLoading.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            progressLoading.setVisibility(View.GONE);
                            Toast.makeText(ChecklistFirstActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            progressLoading.setVisibility(View.GONE);
                            Toast.makeText(ChecklistFirstActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            progressLoading.setVisibility(View.GONE);
                            Toast.makeText(ChecklistFirstActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof NetworkError) {
                            progressLoading.setVisibility(View.GONE);
                            Toast.makeText(ChecklistFirstActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ParseError) {
                            progressLoading.setVisibility(View.GONE);
                            Toast.makeText(ChecklistFirstActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(ChecklistFirstActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }
}
