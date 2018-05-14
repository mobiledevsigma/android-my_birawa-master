package id.co.gsd.mybirawa.controller.checklist;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Timer;
import java.util.TimerTask;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.DeviceTypeAdapter;
import id.co.gsd.mybirawa.model.ModelDeviceType;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ChecklistFirstTabChildFragment extends Fragment {
    String childname;
    String tabName;
    private ProgressBar progressBar;
    private ProgressBar progressLoading;
    private LinearLayout lay_spinner;
    private TextView tv_percent_bar;
    private TextView tv_spin_lantai;
    private SpinnerDialog spinnerDialog;
    private ListView listView;
    private String unitID, roleID, periodID, selisih;
    private List<ModelDeviceType> listModel;
    private ModelDeviceType model;
    private DeviceTypeAdapter adapter;
    private int percent;
    private ArrayList<String> listLantai = new ArrayList<>();
    private ArrayList<String> listLantaiID = new ArrayList<>();
    private Handler handler;
    private Runnable runnable;
    private Timer timer;
    private int i = 0;
    private LinearLayout lay_pj, lay_no;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist_tab_child_first, container, false);
        progressBar = view.findViewById(R.id.bar_progress);
        progressLoading = view.findViewById(R.id.progressBar);
        tv_percent_bar = view.findViewById(R.id.tv_percent_bar);
        lay_spinner = view.findViewById(R.id.lay_spin_lantai);
        tv_spin_lantai = view.findViewById(R.id.tv_spin_lantai);
        listView = view.findViewById(R.id.listViewtPJ);
        lay_no = view.findViewById(R.id.lay_no_data);

        progressLoading.setVisibility(View.GONE);
        progressBar.setSystemUiVisibility(View.GONE);

        Bundle bundle = getArguments();
        childname = bundle.getString("data");
        Intent intent = getActivity().getIntent();
        unitID = intent.getStringExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID);
        roleID = intent.getStringExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID);
        periodID = intent.getStringExtra(ConstantUtils.PERIOD.TAG_ID);
        selisih = intent.getStringExtra("selisih");
        percent = intent.getExtras().getInt("percent");

        setBarProgress();
        getData(unitID, roleID, periodID, selisih);

        spinnerDialog = new SpinnerDialog(getActivity(), listLantai, "Pilih Lantai");
        lay_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
                Typeface externalFont = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    externalFont = getResources().getFont(R.font.nexa_light);
                    ((TextView) view).setTypeface(externalFont);
                }
            }
        });
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int i) {
                tv_spin_lantai.setText(lantai);
                String idLantai = listLantaiID.get(i);
                System.out.println("lantaiii " + idLantai);
                getDataPJ(idLantai, unitID, roleID, periodID, selisih);
            }
        });

        return view;
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

    //GET DATA
    private void getDataPJ(final String lantai, final String unit, final String role, final String period, final String selisih) {
        final String REQUEST_TAG = "get request";
        progressLoading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.DEVICE_TYPE + lantai + "/" + unit + "/" + role + "/" + period + "/" + selisih,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("apa lu " +response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DEVICE.TAG_TITLE);
                            listModel = new ArrayList<ModelDeviceType>();

                            if (jsonArray.length() > 0) {
                                listView.setVisibility(View.VISIBLE);
                                lay_no.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String pjID = object.getString(ConstantUtils.DEVICE.TAG_PJ_ID);
                                    String pjName = object.getString(ConstantUtils.DEVICE.TAG_PJ_NAME);
                                    String pjBlm = object.getString(ConstantUtils.DEVICE.TAG_PJ_BLM);
                                    String icon = object.getString(ConstantUtils.DEVICE.TAG_PJ_ICON);
                                    model = new ModelDeviceType(pjID, pjName, pjBlm, icon);
                                    listModel.add(model);
                                }

                                adapter = new DeviceTypeAdapter(getActivity(), listModel);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(getActivity(), ChecklistSecondActivity.class);
                                        intent.putExtra("lantai", lantai);
                                        intent.putExtra("unit", unit);
                                        intent.putExtra("role", role);
                                        intent.putExtra(ConstantUtils.PERIOD.TAG_ID, period);
                                        intent.putExtra("selisih", selisih);
                                        intent.putExtra(ConstantUtils.DEVICE.TAG_PJ_ID, listModel.get(i).getPj_id());
                                        intent.putExtra(ConstantUtils.DEVICE.TAG_PJ_NAME, listModel.get(i).getPj_name());
                                        startActivity(intent);
                                    }
                                });
                                progressLoading.setVisibility(View.GONE);

                            } else {
                                progressLoading.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                lay_no.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
        System.out.println("hmm " + request);
    }

    //GET DATA
    private void getData(String unit, String role, String period, String hari) {
        final String REQUEST_TAG = "get request";
        progressLoading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.BUILDING + unit + "/" + role + "/" + period+ "/" + hari,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE);
                            listLantai.clear();
                            listLantaiID.clear();

                            System.out.println("aloha " + response);
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

                                        if (nama.equals(tabName)) {
                                            if (jsonArray1.length() > 0) {
                                                listLantai.add(Lname);
                                                listLantaiID.add(Lid);
                                            }
                                        }
                                    }
                                }
                                progressLoading.setVisibility(View.GONE);
                            } else {
                                progressLoading.setVisibility(View.GONE);
                                listLantaiID.clear();
                                listLantai.clear();
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }
}
