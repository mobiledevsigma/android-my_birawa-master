package id.co.gsd.mybirawa.controller.checklist;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.DeviceTypeAdapter;
import id.co.gsd.mybirawa.adapter.TimeAdapter;
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
    private LinearLayout lay_time;
    private RecyclerView listView_time;
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
    private String batas_bawah, batas_atas;
    private String unitID, roleID, periodID, selisih, idGedung, idLantai, judul;
    private int percent;
    private Handler handler;
    private Runnable runnable;
    private Timer timer;
    private int currentTime;
    private int i = 0;
    private TimeAdapter timeAdapter;
    private ArrayList<String> listTime = new ArrayList<String>();
    private ArrayList<String> listGedung = new ArrayList<String>();
    private ArrayList<String> listGedungID = new ArrayList<String>();
    private ArrayList<String> listLantai = new ArrayList<String>();
    private ArrayList<String> listLantaiID = new ArrayList<String>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private StringRequest requestGedung, requestLantai, requestPJ;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_first);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressLoading = findViewById(R.id.progressBar2);
        toolbar = findViewById(R.id.toolbar);
        textToolbar = findViewById(R.id.tv_toolbar_checklist);
        lay_time = findViewById(R.id.lay_time);
        listView_time = findViewById(R.id.listView_time);
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

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String time = sdf.format(cal.getTime());
        currentTime = Integer.parseInt(time);
        setLayoutTime();

        setBarProgress();
        getDataGedung(unitID, roleID, periodID, selisih);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataGedung(unitID, roleID, periodID, selisih);
                tv_spin_gedung.setText("Pilih Gedung");
                tv_spin_lantai.setText("Pilih Lantai");
                listView.setVisibility(View.GONE);
                lay_no_data.setVisibility(View.VISIBLE);
            }
        });

        spinnerGedung = new SpinnerDialog(ChecklistFirstActivity.this, listGedung, "Pilih Gedung");
        lay_spin_gedung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerGedung.showSpinerDialog();
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

        View dashRootView = findViewById(R.id.rootView);
//        skeletonScreen = Skeleton.bind(dashRootView)
//                .load(R.layout.view_dashboard_shimmer)
//                .duration(1000)
//                .color(R.color.shimmer_color)
//                .angle(0)
//                .show();
//
//        MyHandler myHandler = new MyHandler(this);
//        myHandler.sendEmptyMessageDelayed(1, 3000);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<ChecklistFirstActivity> activityWeakReference;

        MyHandler(ChecklistFirstActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (activityWeakReference.get() != null) {
                activityWeakReference.get().skeletonScreen.hide();
            }
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        requestGedung.cancel();
//        requestLantai.cancel();
//        requestPJ.cancel();
//    }

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

    //SET LAYOUT TIME
    private void setLayoutTime() {
        listTime.add("08.00");
        listTime.add("10.00");
        listTime.add("12.00");
        listTime.add("14.00");
        listTime.add("16.00");
        listTime.add("17.00");
        lay_time.setVisibility(View.GONE);
        if (roleID.equals("5")) {
            for (int i = 0; i < listTime.size(); i++) {
                String times1 = listTime.get(i).substring(0, 2);
                int timer = Integer.parseInt(times1);
                if (currentTime >= timer) { //jika di dalam waktu checklist
                    if (i == listTime.size() - 1) { //jika i sama dengan waktu terakhir
                        System.out.println("masuk");
                        String times2 = "20";
                        int timer2 = 20;
                        if (currentTime < timer2) {
                            batas_bawah = times1;
                            batas_atas = times2;
                        } else {
                            lay_no_data.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (i < listTime.size()) {
                            String times2 = listTime.get(i + 1).substring(0, 2);
                            int timer2 = Integer.parseInt(times2);
                            if (currentTime < timer2) {
                                batas_bawah = times1;
                                batas_atas = times2;
                            } else {
                                lay_no_data.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (currentTime < timer + 4) {
                                System.out.println("pass3 " + currentTime);
                            } else {
                                System.out.println("pass4");
                            }
                        }
                    }
                } else {
                    lay_no_data.setVisibility(View.VISIBLE);
                }
            }

            LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getBaseContext());
            MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            timeAdapter = new TimeAdapter(this, listTime);
            listView_time.setHasFixedSize(true);
            listView_time.setAdapter(timeAdapter);
            listView_time.setLayoutManager(MyLayoutManager);
            lay_time.setVisibility(View.VISIBLE);
        } else {
            lay_time.setVisibility(View.GONE);
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
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        String url = "";
        if (role.equals("5")) {
            url = ConstantUtils.URL.BUILDING_HK + unit + "/" + batas_atas + "/" + batas_bawah;
        } else {
            url = ConstantUtils.URL.BUILDING + unit + "/" + role + "/" + period + "/" + hari;
        }

        requestGedung = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                getLoadErrorGedung();
                            }

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE);
                            listName = new ArrayList<String>();
                            listGedungID.clear();
                            listGedung.clear();

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
        requestGedung.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(ChecklistFirstActivity.this).addToRequestQueue(requestGedung, REQUEST_TAG);
    }

    //GET DATA LANTAI
    private void getDataLantai(final String geID, String unit, String role, String period, String hari) {
        final String REQUEST_TAG = "get request";
        progressLoading.setVisibility(View.VISIBLE);
        lay_spin_lantai.setEnabled(false);

        String url = "";
        if (role.equals("5")) {
            url = ConstantUtils.URL.FLOOR_HK + unit + "/" + geID + "/" + batas_atas + "/" + batas_bawah;
        } else {
            url = ConstantUtils.URL.FLOOR + unit + "/" + role + "/" + period + "/" + geID + "/" + hari;
        }

        requestLantai = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                getLoadErrorLantai();
                            }

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE2);
                            listLantai.clear();
                            listLantaiID.clear();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String Lid = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_ID);
                                    String Lname = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_NAME);

                                    if (jsonArray.length() > 0) {
                                        listLantai.add(Lname);
                                        listLantaiID.add(Lid);
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
        requestLantai.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(ChecklistFirstActivity.this).addToRequestQueue(requestLantai, REQUEST_TAG);
    }

    //GET DATA
    private void getDataPJ(final String lantai, final String unit, final String role, final String period, final String selisih) {
        final String REQUEST_TAG = "get request";
        progressLoading.setVisibility(View.VISIBLE);

        String url = "";
        if (role.equals("5")) {
            //url = ConstantUtils.URL.DEVICE_TYPE_HK + lantai + "/" + unit + "/" + "12" + "/" + "10";
            url = ConstantUtils.URL.DEVICE_TYPE_HK + lantai + "/" + unit + "/" + batas_atas + "/" + batas_bawah;
        } else {
            url = ConstantUtils.URL.DEVICE_TYPE + lantai + "/" + unit + "/" + role + "/" + period + "/" + selisih;
        }

        requestPJ = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
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
        //set time out
        requestPJ.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(ChecklistFirstActivity.this).addToRequestQueue(requestPJ, REQUEST_TAG);
    }
}
