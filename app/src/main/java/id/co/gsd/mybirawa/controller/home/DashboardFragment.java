package id.co.gsd.mybirawa.controller.home;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hookedonplay.decoviewlib.DecoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.checklist.ChecklistFirstActivity;
import id.co.gsd.mybirawa.controller.main.LoginActivity;
import id.co.gsd.mybirawa.controller.news.PopupActivity;
import id.co.gsd.mybirawa.model.ModelDashboard;
import id.co.gsd.mybirawa.util.DecoHelper;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private final static int TIME_OUT = 1000;
    private final float mSeriesMax = 100f;
    private float percent = 0f;
    private DecoHelper decoHelper;
    private ImageView btnNews;
    private ImageView btnLogout;
    private DecoView deco_harian, deco_mingguan, deco_2minggu, deco_bulan, deco_3bulan, deco_6bulan, deco_tahun, deco_punch;
    private ProgressBar progressBar;
    private TextView text_name, text_title;
    private TextView text_date;
    private LinearLayout lay_harian, lay_mingguan, lay_2minggu, lay_bulan, lay_3bulan, lay_6bulan, lay_tahun, lay_punch;
    private TextView det_harian, det_mingguan, det_2minggu, det_bulan, det_3bulan, det_6bulan, det_tahun, det_punch;
    private TextView perc_harian, perc_mingguan, perc_2minggu, perc_bulan, perc_3bulan, perc_6bulan, perc_tahun, perc_punch;
    private TextView pj_harian1, pj_mingguan1, pj_2minggu1, pj_bulanan1, pj_3bulanan1, pj_6bulanan1, pj_tahunan1, pj_punch1;
    private TextView pj_harian2, pj_mingguan2, pj_2minggu2, pj_bulanan2, pj_3bulanan2, pj_6bulanan2, pj_tahunan2, pj_punch2;
    private TextView pj_harian3, pj_mingguan3, pj_2minggu3, pj_bulanan3, pj_3bulanan3, pj_6bulanan3, pj_tahunan3, pj_punch3;
    private ModelDashboard model;
    private List<ModelDashboard> listModel;
    private SessionManager session;
    private String idUnit, roleId, userID, imeiID;
    private String tglServer, link;
    private RelativeLayout lay_dash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        session = new SessionManager(getActivity());
        decoHelper = new DecoHelper();
        progressBar = view.findViewById(R.id.pb_dashboard);
        progressBar.setVisibility(View.GONE);
        text_name = view.findViewById(R.id.tv_name_user);
        text_title = view.findViewById(R.id.tv_unit_user);
        btnNews = view.findViewById(R.id.btn_news);
        btnLogout = view.findViewById(R.id.btn_logout);
        text_date = view.findViewById(R.id.tv_today);
        lay_dash = view.findViewById(R.id.lay_dash);

        imeiID = session.getImei();

        //news
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnPopup = new Intent(getActivity(), PopupActivity.class);
                startActivity(btnPopup);
            }
        });

        //logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Anda yakin akan logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout(imeiID);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        text_name.setText(session.getName());
        text_title.setText(session.getRoleName());
        //layout_button
        lay_harian = view.findViewById(R.id.lay_harian);
        lay_mingguan = view.findViewById(R.id.lay_mingguan);
        lay_2minggu = view.findViewById(R.id.lay_2_mingguan);
        lay_bulan = view.findViewById(R.id.lay_bulanan);
        lay_3bulan = view.findViewById(R.id.lay_3bulanan);
        lay_6bulan = view.findViewById(R.id.lay_6bulanan);
        lay_tahun = view.findViewById(R.id.lay_tahunan);
        lay_punch = view.findViewById(R.id.lay_punch);
        //deco_view
        deco_harian = view.findViewById(R.id.deco_harian);
        deco_mingguan = view.findViewById(R.id.deco_mingguan);
        deco_2minggu = view.findViewById(R.id.deco_2mingguan);
        deco_bulan = view.findViewById(R.id.deco_bulanan);
        deco_3bulan = view.findViewById(R.id.deco_3bulanan);
        deco_6bulan = view.findViewById(R.id.deco_6bulanan);
        deco_tahun = view.findViewById(R.id.deco_tahunan);
        deco_punch = view.findViewById(R.id.deco_punch);
        //text_percent
        perc_harian = view.findViewById(R.id.tv_percent_harian);
        perc_mingguan = view.findViewById(R.id.tv_percent_mingguan);
        perc_2minggu = view.findViewById(R.id.tv_percent_2mingguan);
        perc_bulan = view.findViewById(R.id.tv_percent_bulanan);
        perc_3bulan = view.findViewById(R.id.tv_percent_3bulanan);
        perc_6bulan = view.findViewById(R.id.tv_percent_6bulanan);
        perc_tahun = view.findViewById(R.id.tv_percent_tahunan);
        perc_punch = view.findViewById(R.id.tv_percent_punch);
        //text_detail
        det_harian = view.findViewById(R.id.tv_detail_harian);
        det_mingguan = view.findViewById(R.id.tv_detail_mingguan);
        det_2minggu = view.findViewById(R.id.tv_detail_2mingguan);
        det_bulan = view.findViewById(R.id.tv_detail_bulanan);
        det_3bulan = view.findViewById(R.id.tv_detail_3bulanan);
        det_6bulan = view.findViewById(R.id.tv_detail_6bulanan);
        det_tahun = view.findViewById(R.id.tv_detail_tahunan);
        det_punch = view.findViewById(R.id.tv_detail_punch);
        //detai_pj
        pj_harian1 = view.findViewById(R.id.tv_harian_1);
        pj_harian2 = view.findViewById(R.id.tv_harian_2);
        pj_harian3 = view.findViewById(R.id.tv_harian_3);
        pj_mingguan1 = view.findViewById(R.id.tv_mingguan_1);
        pj_mingguan2 = view.findViewById(R.id.tv_mingguan_2);
        pj_mingguan3 = view.findViewById(R.id.tv_mingguan_3);
        pj_2minggu1 = view.findViewById(R.id.tv_2mingguan_1);
        pj_2minggu2 = view.findViewById(R.id.tv_2mingguan_2);
        pj_2minggu3 = view.findViewById(R.id.tv_2mingguan_3);
        pj_bulanan1 = view.findViewById(R.id.tv_bulanan_1);
        pj_bulanan2 = view.findViewById(R.id.tv_bulanan_2);
        pj_bulanan3 = view.findViewById(R.id.tv_bulanan_3);
        pj_3bulanan1 = view.findViewById(R.id.tv_3bulanan_1);
        pj_3bulanan2 = view.findViewById(R.id.tv_3bulanan_2);
        pj_3bulanan3 = view.findViewById(R.id.tv_3bulanan_3);
        pj_6bulanan1 = view.findViewById(R.id.tv_6bulanan_1);
        pj_6bulanan2 = view.findViewById(R.id.tv_6bulanan_2);
        pj_6bulanan3 = view.findViewById(R.id.tv_6bulanan_3);
        pj_tahunan1 = view.findViewById(R.id.tv_tahunan_1);
        pj_tahunan2 = view.findViewById(R.id.tv_tahunan_2);
        pj_tahunan3 = view.findViewById(R.id.tv_tahunan_3);
        pj_punch1 = view.findViewById(R.id.tv_punch_1);
        pj_punch2 = view.findViewById(R.id.tv_punch_2);
        pj_punch3 = view.findViewById(R.id.tv_punch_3);

        //get_data
        idUnit = session.getIdUnit();
        roleId = session.getRoleId();
        userID = session.getId();
        getHarian(idUnit, roleId);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getHarian(idUnit, roleId);
    }

    //GET DATA
    private void logout(String imei) {
        final String REQUEST_TAG = "get request";

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.LOGOUT + imei,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String logout = jsonObject.getString("logout");

                            if (logout.equals("Success")) {
                                session.logoutUser();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
        AppSingleton.getInstance(getActivity().getBaseContext()).addToRequestQueue(request, REQUEST_TAG);
    }

    //GET HARIAN
    private void getHarian(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);
        //HARIAN
        final StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_HARIAN + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reloadHarian();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);
                                    String tgl = object.getString("tanggal");

                                    //convert date server
                                    SimpleDateFormat fServer = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        Date dateServer = fServer.parse(tgl);
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id"));
                                        String date = sdf.format(dateServer);
                                        tglServer = date;
                                        text_date.setText(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id"));
                                    String date = sdf.format(new Date());
                                    if (!date.equals(tglServer)) {
                                        new AlertDialog.Builder(getActivity())
                                                .setMessage("Hai, jangan ubah tanggal di handphone kamu ya..")
                                                .setCancelable(false)
                                                .setNeutralButton("Ubah", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                    }
                                                })
                                                .show();
                                    }

                                    //set progress circle
                                    decoHelper.DecoBack(deco_harian, mSeriesMax);
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = (sudah / total) * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        decoHelper.DecoDataRed(deco_harian, mSeriesMax, perc_harian);
                                    } else if (percent >= 50.f && percent <= 75.f) {
                                        decoHelper.DecoDataYellow(deco_harian, mSeriesMax, perc_harian);
                                    } else {
                                        decoHelper.DecoDataGreen(deco_harian, mSeriesMax, perc_harian);
                                    }
                                    decoHelper.DecoEvent(deco_harian, mSeriesMax, percent);

                                    det_harian.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE2);
                                    if (jsonArray1.length() > 0) {
                                        for (int a = 0; a < jsonArray1.length(); a++) {
                                            JSONObject obj = jsonArray1.getJSONObject(a);
                                            String id = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_ID);
                                            String name = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_NAME);
                                            String blm = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_BELUM);
                                            model = new ModelDashboard(id, name, blm);
                                            listModel.add(model);
                                        }

                                        //set detail
                                        if (listModel.size() >= 2) {
                                            if (listModel.get(0).getPj_name().length() > 6) {
                                                pj_harian1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_harian2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_harian2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            } else {
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_harian1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_harian2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_harian1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_harian2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            }
                                            pj_harian3.setVisibility(View.VISIBLE);
                                        } else {
                                            pj_harian2.setVisibility(View.GONE);
                                            pj_harian3.setVisibility(View.GONE);
                                            if (listModel.size() <= 1) {
                                                if (listModel.get(0).getPj_name().length() > 6) {
                                                    pj_harian1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                } else {
                                                    pj_harian1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                }
                                            }
                                        }
                                        if (getActivity() != null && isAdded()) {
                                            lay_harian.setBackground(getResources().getDrawable(R.drawable.dash_active));
                                        }

                                        lay_harian.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID, idUnit);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID, roleId);
                                                intent.putExtra(ConstantUtils.PERIOD.TAG_ID, "1");
                                                intent.putExtra("selisih", "0");
                                                intent.putExtra("percent", round);
                                                intent.putExtra("judul", "Checklist Harian");
                                                session.setPeriodId("1");
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        if (getActivity() != null && isAdded()) {
                                            lay_harian.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                        }
                                        det_harian.setText("Detail : 0/0");
                                        pj_harian1.setVisibility(View.GONE);
                                        pj_harian2.setVisibility(View.GONE);
                                        pj_harian3.setVisibility(View.GONE);
                                    }
                                }
                                lay_harian.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                getMingguan(unit_id, role_id);
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }

    //GET MINGGUAN
    private void getMingguan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get Mingguan";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request2 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_MINGGUAN + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();

                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                reloadMingguan();
                            }

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);

                                    //set progress circle
                                    decoHelper.DecoBack(deco_mingguan, mSeriesMax);
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        decoHelper.DecoDataRed(deco_mingguan, mSeriesMax, perc_mingguan);
                                    } else if (percent >= 50.f && percent <= 75.f) {
                                        decoHelper.DecoDataYellow(deco_mingguan, mSeriesMax, perc_mingguan);
                                    } else {
                                        decoHelper.DecoDataGreen(deco_mingguan, mSeriesMax, perc_mingguan);
                                    }
                                    decoHelper.DecoEvent(deco_mingguan, mSeriesMax, percent);
                                    det_mingguan.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE2);
                                    if (jsonArray1.length() > 0) {
                                        for (int a = 0; a < jsonArray1.length(); a++) {
                                            JSONObject obj = jsonArray1.getJSONObject(a);
                                            String id = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_ID);
                                            String name = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_NAME);
                                            String blm = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_BELUM);
                                            model = new ModelDashboard(id, name, blm);
                                            listModel.add(model);
                                        }

                                        if (listModel.size() >= 2) {
                                            if (listModel.get(0).getPj_name().length() > 6) {
                                                pj_mingguan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_mingguan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_mingguan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            } else {
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_mingguan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_mingguan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_mingguan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_mingguan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            }
                                            pj_mingguan3.setVisibility(View.VISIBLE);
                                        } else {
                                            pj_mingguan2.setVisibility(View.GONE);
                                            pj_mingguan3.setVisibility(View.GONE);
                                            if (listModel.size() <= 1) {
                                                if (listModel.get(0).getPj_name().length() > 6) {
                                                    pj_mingguan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                } else {
                                                    pj_mingguan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                }
                                            }
                                        }

                                        if (getActivity() != null && isAdded()) {
                                            lay_mingguan.setBackground(getResources().getDrawable(R.drawable.dash_active));
                                        }
                                        lay_mingguan.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID, idUnit);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID, roleId);
                                                intent.putExtra(ConstantUtils.PERIOD.TAG_ID, "2");
                                                intent.putExtra("percent", round);
                                                intent.putExtra("judul", "Checklist 1 Mingguan");
                                                intent.putExtra("selisih", "7");
                                                session.setPeriodId("2");
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        if (getActivity() != null && isAdded()) {
                                            lay_mingguan.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                        }
                                        det_mingguan.setText("Detail : 0/0");
                                        pj_mingguan1.setVisibility(View.GONE);
                                        pj_mingguan2.setVisibility(View.GONE);
                                        pj_mingguan3.setVisibility(View.GONE);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                get2Mingguan(unit_id, role_id);
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                })

        {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request2, REQUEST_TAG);
    }

    // GET 2 MINGGUAN
    private void get2Mingguan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get 2 mingguan";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request3 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_2MINGGUAN + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                reload2Mingguan();
                            }

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);

                                    //set progress circle
                                    decoHelper.DecoBack(deco_2minggu, mSeriesMax);
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        decoHelper.DecoDataRed(deco_2minggu, mSeriesMax, perc_2minggu);
                                    } else if (percent >= 50.f && percent <= 75.f) {
                                        decoHelper.DecoDataYellow(deco_2minggu, mSeriesMax, perc_2minggu);
                                    } else {
                                        decoHelper.DecoDataGreen(deco_2minggu, mSeriesMax, perc_2minggu);
                                    }
                                    decoHelper.DecoEvent(deco_2minggu, mSeriesMax, percent);
                                    det_2minggu.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE2);
                                    if (jsonArray1.length() > 0) {
                                        for (int a = 0; a < jsonArray1.length(); a++) {
                                            JSONObject obj = jsonArray1.getJSONObject(a);
                                            String id = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_ID);
                                            String name = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_NAME);
                                            String blm = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_BELUM);
                                            model = new ModelDashboard(id, name, blm);
                                            listModel.add(model);
                                        }

                                        if (listModel.size() >= 2) {
                                            if (listModel.get(0).getPj_name().length() > 6) {
                                                pj_2minggu1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_2minggu2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_2minggu2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            } else {
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_2minggu1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_2minggu2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_2minggu1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_2minggu2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            }
                                            pj_2minggu3.setVisibility(View.VISIBLE);
                                        } else {
                                            pj_2minggu2.setVisibility(View.GONE);
                                            pj_2minggu3.setVisibility(View.GONE);
                                            if (listModel.size() <= 1) {
                                                if (listModel.get(0).getPj_name().length() > 6) {
                                                    pj_2minggu1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                } else {
                                                    pj_2minggu1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                }
                                            }
                                        }

                                        if (getActivity() != null && isAdded()) {
                                            lay_2minggu.setBackground(getResources().getDrawable(R.drawable.dash_active));
                                        }

                                        lay_2minggu.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID, idUnit);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID, roleId);
                                                intent.putExtra(ConstantUtils.PERIOD.TAG_ID, "3");
                                                intent.putExtra("percent", round);
                                                intent.putExtra("judul", "Checklist 2 Mingguan");
                                                intent.putExtra("selisih", "14");
                                                session.setPeriodId("3");
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        if (getActivity() != null && isAdded()) {
                                            lay_2minggu.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                        }
                                        det_2minggu.setText("Detail : 0/0");
                                        pj_2minggu1.setVisibility(View.GONE);
                                        pj_2minggu2.setVisibility(View.GONE);
                                        pj_2minggu3.setVisibility(View.GONE);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                getBulanan(unit_id, role_id);
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(request3, REQUEST_TAG);
    }

    //GET BULANAN
    private void getBulanan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request4 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_BULANAN + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();

                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reloadBulanan();
                            }

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);

                                    //set progress
                                    decoHelper.DecoBack(deco_bulan, mSeriesMax);
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        decoHelper.DecoDataRed(deco_bulan, mSeriesMax, perc_bulan);
                                    } else if (percent >= 50.f && percent <= 75.f) {
                                        decoHelper.DecoDataYellow(deco_bulan, mSeriesMax, perc_bulan);
                                    } else {
                                        decoHelper.DecoDataGreen(deco_bulan, mSeriesMax, perc_bulan);
                                    }
                                    decoHelper.DecoEvent(deco_bulan, mSeriesMax, percent);
                                    det_bulan.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE2);
                                    if (jsonArray1.length() > 0) {
                                        for (int a = 0; a < jsonArray1.length(); a++) {
                                            JSONObject obj = jsonArray1.getJSONObject(a);
                                            String id = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_ID);
                                            String name = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_NAME);
                                            String blm = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_BELUM);
                                            model = new ModelDashboard(id, name, blm);
                                            listModel.add(model);
                                        }

                                        if (listModel.size() >= 2) {
                                            if (listModel.get(0).getPj_name().length() > 6) {
                                                pj_bulanan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_bulanan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_bulanan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            } else {
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_bulanan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_bulanan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            }
                                            pj_bulanan3.setVisibility(View.VISIBLE);
                                        } else {
                                            pj_bulanan2.setVisibility(View.GONE);
                                            pj_bulanan3.setVisibility(View.GONE);
                                            if (listModel.size() <= 1) {
                                                if (listModel.get(0).getPj_name().length() > 6) {
                                                    pj_bulanan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                } else {
                                                    pj_bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                }
                                            }
                                        }

                                        if (getActivity() != null && isAdded()) {
                                            lay_bulan.setBackground(getResources().getDrawable(R.drawable.dash_active));
                                        }

                                        lay_bulan.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID, idUnit);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID, roleId);
                                                intent.putExtra(ConstantUtils.PERIOD.TAG_ID, "4");
                                                intent.putExtra("selisih", "30");
                                                intent.putExtra("percent", round);
                                                intent.putExtra("judul", "Checklist 1 Bulanan");
                                                session.setPeriodId("4");
                                                startActivity(intent);
                                            }
                                        });

                                    } else {

                                        if (getActivity() != null && isAdded()) {
                                            lay_bulan.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                        }

                                        det_bulan.setText("Detail : 0/0");
                                        pj_bulanan1.setVisibility(View.GONE);
                                        pj_bulanan2.setVisibility(View.GONE);
                                        pj_bulanan3.setVisibility(View.GONE);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                get3Bulanan(unit_id, role_id);
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(request4, REQUEST_TAG);
    }

    //GET 3 BULANAN
    private void get3Bulanan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request5 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_3BULANAN + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();

                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reload3Bulanan();
                            }

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);

                                    //set progress
                                    decoHelper.DecoBack(deco_3bulan, mSeriesMax);
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        decoHelper.DecoDataRed(deco_3bulan, mSeriesMax, perc_3bulan);
                                    } else if (percent >= 50.f && percent <= 75.f) {
                                        decoHelper.DecoDataYellow(deco_3bulan, mSeriesMax, perc_3bulan);
                                    } else {
                                        decoHelper.DecoDataGreen(deco_3bulan, mSeriesMax, perc_3bulan);
                                    }
                                    decoHelper.DecoEvent(deco_3bulan, mSeriesMax, percent);
                                    det_3bulan.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE2);
                                    if (jsonArray1.length() > 0) {
                                        for (int a = 0; a < jsonArray1.length(); a++) {
                                            JSONObject obj = jsonArray1.getJSONObject(a);
                                            String id = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_ID);
                                            String name = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_NAME);
                                            String blm = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_BELUM);
                                            model = new ModelDashboard(id, name, blm);
                                            listModel.add(model);
                                        }

                                        if (listModel.size() >= 2) {
                                            if (listModel.get(0).getPj_name().length() > 6) {
                                                pj_3bulanan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_3bulanan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_3bulanan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            } else {
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_3bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_3bulanan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_3bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_3bulanan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            }
                                            pj_3bulanan3.setVisibility(View.VISIBLE);
                                        } else {
                                            pj_3bulanan2.setVisibility(View.GONE);
                                            pj_3bulanan3.setVisibility(View.GONE);
                                            if (listModel.size() <= 1) {
                                                if (listModel.get(0).getPj_name().length() > 6) {
                                                    pj_3bulanan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                } else {
                                                    pj_3bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                }
                                            }
                                        }

                                        if (getActivity() != null && isAdded()) {
                                            lay_3bulan.setBackground(getResources().getDrawable(R.drawable.dash_active));
                                        }
                                        lay_3bulan.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID, idUnit);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID, roleId);
                                                intent.putExtra(ConstantUtils.PERIOD.TAG_ID, "5");
                                                intent.putExtra("percent", round);
                                                intent.putExtra("judul", "Checklist 3 Bulanan");
                                                intent.putExtra("selisih", "90");
                                                session.setPeriodId("5");
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        if (getActivity() != null && isAdded()) {
                                            lay_3bulan.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                        }
                                        det_3bulan.setText("Detail : 0/0");
                                        pj_3bulanan1.setVisibility(View.GONE);
                                        pj_3bulanan2.setVisibility(View.GONE);
                                        pj_3bulanan3.setVisibility(View.GONE);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                get6Bulanan(unit_id, role_id);
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(request5, REQUEST_TAG);
    }

    //GET 6BULANAN
    private void get6Bulanan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request6 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_6BULANAN + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();

                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reload6Bulanan();
                            }

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);

                                    //set progress
                                    decoHelper.DecoBack(deco_6bulan, mSeriesMax);
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        decoHelper.DecoDataRed(deco_6bulan, mSeriesMax, perc_6bulan);
                                    } else if (percent >= 50.f && percent <= 75.f) {
                                        decoHelper.DecoDataYellow(deco_6bulan, mSeriesMax, perc_6bulan);
                                    } else {
                                        decoHelper.DecoDataGreen(deco_6bulan, mSeriesMax, perc_6bulan);
                                    }
                                    decoHelper.DecoEvent(deco_6bulan, mSeriesMax, percent);
                                    det_6bulan.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE2);
                                    if (jsonArray1.length() > 0) {
                                        for (int a = 0; a < jsonArray1.length(); a++) {
                                            JSONObject obj = jsonArray1.getJSONObject(a);
                                            String id = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_ID);
                                            String name = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_NAME);
                                            String blm = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_BELUM);
                                            model = new ModelDashboard(id, name, blm);
                                            listModel.add(model);
                                        }

                                        if (listModel.size() >= 2) {
                                            if (listModel.get(0).getPj_name().length() > 6) {
                                                pj_6bulanan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_6bulanan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_6bulanan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            } else {
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_6bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_6bulanan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_6bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_6bulanan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            }
                                            pj_6bulanan3.setVisibility(View.VISIBLE);
                                        } else {
                                            pj_6bulanan2.setVisibility(View.GONE);
                                            pj_6bulanan3.setVisibility(View.GONE);
                                            if (listModel.size() <= 1) {
                                                if (listModel.get(0).getPj_name().length() > 6) {
                                                    pj_6bulanan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                } else {
                                                    pj_6bulanan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                }
                                            }
                                        }

                                        if (getActivity() != null && isAdded()) {
                                            lay_6bulan.setBackground(getResources().getDrawable(R.drawable.dash_active));
                                        }

                                        lay_6bulan.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID, idUnit);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID, roleId);
                                                intent.putExtra(ConstantUtils.PERIOD.TAG_ID, "6");
                                                intent.putExtra("percent", round);
                                                intent.putExtra("judul", "Checklist 6 Bulanan");
                                                intent.putExtra("selisih", "180");
                                                session.setPeriodId("6");
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        if (getActivity() != null && isAdded()) {
                                            lay_6bulan.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                        }

                                        det_6bulan.setText("Detail : 0/0");
                                        pj_6bulanan1.setVisibility(View.GONE);
                                        pj_6bulanan2.setVisibility(View.GONE);
                                        pj_6bulanan3.setVisibility(View.GONE);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                getTahunan(unit_id, role_id);
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(request6, REQUEST_TAG);
    }

    //GET TAHUNAN
    private void getTahunan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request7 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_TAHUN + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();

                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reloadTahunan();
                            }

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);

                                    //set progress
                                    decoHelper.DecoBack(deco_tahun, mSeriesMax);
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        decoHelper.DecoDataRed(deco_tahun, mSeriesMax, perc_tahun);
                                    } else if (percent >= 50.f && percent <= 75.f) {
                                        decoHelper.DecoDataYellow(deco_tahun, mSeriesMax, perc_tahun);
                                    } else {
                                        decoHelper.DecoDataGreen(deco_tahun, mSeriesMax, perc_tahun);
                                    }
                                    decoHelper.DecoEvent(deco_tahun, mSeriesMax, percent);
                                    det_tahun.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE2);
                                    if (jsonArray1.length() > 0) {
                                        for (int a = 0; a < jsonArray1.length(); a++) {
                                            JSONObject obj = jsonArray1.getJSONObject(a);
                                            String id = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_ID);
                                            String name = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_NAME);
                                            String blm = obj.getString(ConstantUtils.DASHBOARD.TAG_PJ_BELUM);
                                            model = new ModelDashboard(id, name, blm);
                                            listModel.add(model);
                                        }

                                        if (listModel.size() >= 2) {
                                            if (listModel.get(0).getPj_name().length() > 6) {
                                                pj_tahunan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_tahunan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_tahunan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            } else {
                                                if (listModel.get(1).getPj_name().length() > 6) {
                                                    pj_tahunan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_tahunan2.setText(listModel.get(1).getPj_name().substring(0, 6) + ".. : " + listModel.get(1).getPj_belum());
                                                } else {
                                                    pj_tahunan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                    pj_tahunan2.setText(listModel.get(1).getPj_name() + " : " + listModel.get(1).getPj_belum());
                                                }
                                            }
                                            pj_tahunan3.setVisibility(View.VISIBLE);
                                        } else {
                                            pj_tahunan3.setVisibility(View.GONE);
                                            pj_tahunan2.setVisibility(View.GONE);
                                            if (listModel.size() <= 1) {
                                                if (listModel.get(0).getPj_name().length() > 6) {
                                                    pj_tahunan1.setText(listModel.get(0).getPj_name().substring(0, 6) + ".. : " + listModel.get(0).getPj_belum());
                                                } else {
                                                    pj_tahunan1.setText(listModel.get(0).getPj_name() + " : " + listModel.get(0).getPj_belum());
                                                }
                                            }
                                        }

                                        if (getActivity() != null && isAdded()) {
                                            lay_tahun.setBackground(getResources().getDrawable(R.drawable.dash_active));
                                        }

                                        lay_tahun.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID, idUnit);
                                                intent.putExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID, roleId);
                                                intent.putExtra(ConstantUtils.PERIOD.TAG_ID, "7");
                                                intent.putExtra("percent", round);
                                                intent.putExtra("judul", "Checklist 1 Tahunan");
                                                session.setPeriodId("7");
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        if (getActivity() != null && isAdded()) {
                                            lay_tahun.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                        }
                                        det_tahun.setText("Detail : 0/0");
                                        pj_tahunan1.setVisibility(View.GONE);
                                        pj_tahunan2.setVisibility(View.GONE);
                                        pj_tahunan3.setVisibility(View.GONE);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                getWO(unit_id, role_id);
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        AppSingleton.getInstance(getActivity()).addToRequestQueue(request7, REQUEST_TAG);
    }

    //GET WO
    private void getWO(final String unit_id, String role_id) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);
        //lay_punch.setClickable(false);

        if (role_id.equals("1")) {
            link = ConstantUtils.URL.DASH_PUNCH_SPV + unit_id;
        } else {
            link = ConstantUtils.URL.DASH_PUNCH + unit_id + "/" + role_id;
        }
        final StringRequest request8 = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();
                            progressBar.setVisibility(View.GONE);
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                //Toast.makeText(ChecklistSecondActivity.this, "server error", Toast.LENGTH_SHORT).show();
                                reloadWO();
                            }

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int total = object.getInt(ConstantUtils.DASHBOARD.TAG_TOTAL);
                                    int belum = object.getInt(ConstantUtils.DASHBOARD.TAG_BELUM);
                                    int sudah = object.getInt(ConstantUtils.DASHBOARD.TAG_SUDAH);

                                    model = new ModelDashboard(sudah, belum, total);
                                    listModel.add(model);
                                }

                                int sudah = model.getSudah();
                                int belum = model.getBelum();
                                int total = model.getTotal();

                                decoHelper.DecoBack(deco_punch, mSeriesMax);
                                if (total == 0) {
                                    percent = 0f;
                                } else {
                                    percent = (float) sudah / total * 100.f;
                                }
                                final int round = Math.round(percent);
                                if (percent < 50.f) {
                                    decoHelper.DecoDataRed(deco_punch, mSeriesMax, perc_punch);
                                } else if (percent >= 50.f && percent <= 75.f) {
                                    decoHelper.DecoDataYellow(deco_punch, mSeriesMax, perc_punch);
                                } else {
                                    decoHelper.DecoDataGreen(deco_punch, mSeriesMax, perc_punch);
                                }
                                decoHelper.DecoEvent(deco_punch, mSeriesMax, percent);
                                det_punch.setText("Detail : " + Math.round(sudah) + "/" + Math.round(total));

                                pj_punch1.setVisibility(View.GONE);
                                pj_punch2.setVisibility(View.GONE);
                                pj_punch3.setVisibility(View.GONE);

                                lay_punch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        HomeActivity activity = (HomeActivity) getActivity();
                                        activity.navigation();
                                        if (session.getRoleId().equals("1")) {
                                            PunchlistSpvFragment fragment = new PunchlistSpvFragment();
                                            FragmentManager manager = getFragmentManager();
                                            manager.beginTransaction().replace(R.id.content, fragment).commit();
                                        } else {
                                            PunchlistFragment fragment = new PunchlistFragment();
                                            FragmentManager manager = getFragmentManager();
                                            manager.beginTransaction().replace(R.id.content, fragment).commit();
                                        }
                                    }
                                });
                            } else {
                                det_punch.setText("Detail : 0/0");
                                pj_punch1.setVisibility(View.VISIBLE);
                                pj_punch2.setVisibility(View.VISIBLE);
                                pj_punch3.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        // Adding JsonObject request to request queue
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppSingleton.getInstance(getActivity()).addToRequestQueue(request8, REQUEST_TAG);
            }
        }, TIME_OUT);
    }

    private void reloadHarian() {
        getHarian(idUnit, roleId);
    }

    private void reloadMingguan() {
        getMingguan(idUnit, roleId);
    }

    private void reload2Mingguan() {
        get2Mingguan(idUnit, roleId);
    }

    private void reloadBulanan() {
        getBulanan(idUnit, roleId);
    }

    private void reload3Bulanan() {
        get3Bulanan(idUnit, roleId);
    }

    private void reload6Bulanan() {
        get6Bulanan(idUnit, roleId);
    }

    private void reloadTahunan() {
        getTahunan(idUnit, roleId);
    }

    private void reloadWO() {
        getWO(idUnit, roleId);
    }
}