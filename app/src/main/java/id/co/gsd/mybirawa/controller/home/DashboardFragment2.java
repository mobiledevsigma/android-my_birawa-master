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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.checklist.ChecklistFirstActivity;
import id.co.gsd.mybirawa.controller.main.LoginActivity;
import id.co.gsd.mybirawa.controller.news.PopupActivity;
import id.co.gsd.mybirawa.model.ModelDashboard;
import id.co.gsd.mybirawa.util.DecoHelper;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment2 extends Fragment {

    private final static int TIME_OUT = 15000;
    private final float mSeriesMax = 100f;
    private float percent = 0f;
    private DecoHelper decoHelper;
    private ImageView btnNews;
    private ImageView btnLogout;
    private Spinner spinner;
    private PieView pie_harian, pie_mingguan, pie_2minggu, pie_bulan, pie_3bulan, pie_6bulan, pie_tahun, pie_punch;
    private ProgressBar progressBar;
    private TextView text_name, text_title, text_date;
    private LinearLayout lay_harian, lay_mingguan, lay_2minggu, lay_bulan, lay_3bulan, lay_6bulan, lay_tahun, lay_punch;
    private TextView det_harian, det_mingguan, det_2minggu, det_bulan, det_3bulan, det_6bulan, det_tahun, det_punch;
    private TextView pj_harian1, pj_mingguan1, pj_2minggu1, pj_bulanan1, pj_3bulanan1, pj_6bulanan1, pj_tahunan1, pj_punch1;
    private TextView pj_harian2, pj_mingguan2, pj_2minggu2, pj_bulanan2, pj_3bulanan2, pj_6bulanan2, pj_tahunan2, pj_punch2;
    private TextView pj_harian3, pj_mingguan3, pj_2minggu3, pj_bulanan3, pj_3bulanan3, pj_6bulanan3, pj_tahunan3, pj_punch3;
    private ModelDashboard model;
    private List<ModelDashboard> listModel;
    private SessionManager session;
    private String idUnit, roleId, userID, imeiID;
    private String link, urlHarian;
    private List<String> listPeriod;
    private int currentTime;
    private LinearLayout lay_option_hk;
    private StringRequest request, request2, request3, request4, request5, request6, request7, request8;
    private View dashRootView;
    private SkeletonScreen skeletonScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard2, container, false);

        session = new SessionManager(getActivity());
        progressBar = view.findViewById(R.id.pb_dashboard);
        progressBar.setVisibility(View.GONE);
        text_name = view.findViewById(R.id.tv_name_user);
        text_title = view.findViewById(R.id.tv_unit_user);
        btnNews = view.findViewById(R.id.btn_news);
        btnLogout = view.findViewById(R.id.btn_logout);
        text_date = view.findViewById(R.id.tv_today);
        lay_option_hk = view.findViewById(R.id.lay_option_hk);
        spinner = view.findViewById(R.id.spin_period);

        lay_option_hk.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        spinner.setSelection(0);

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
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout(imeiID);
                            }
                        })
                        .setNegativeButton("Tidak", null)
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
        //pie_view
        pie_harian = view.findViewById(R.id.pieHarian);
        pie_mingguan = view.findViewById(R.id.pieMingguan);
        pie_2minggu = view.findViewById(R.id.pie2Mingguan);
        pie_bulan = view.findViewById(R.id.pieBulanan);
        pie_3bulan = view.findViewById(R.id.pie3Bulanan);
        pie_6bulan = view.findViewById(R.id.pie6Bulanan);
        pie_tahun = view.findViewById(R.id.pieTahunan);
        pie_punch = view.findViewById(R.id.piePunch);

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

        dashRootView = view.findViewById(R.id.rootView);

        //get time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String time = sdf.format(cal.getTime());
        currentTime = Integer.parseInt(time);

        //get_data
        idUnit = session.getIdUnit();
        roleId = session.getRoleId();
        userID = session.getId();
        if (!roleId.equals("5")) {
            urlHarian = ConstantUtils.URL.DASH_HARIAN;
            getHarian(urlHarian, idUnit, roleId, 0, 0);
        } else {
            lay_option_hk.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelection(0);

            urlHarian = ConstantUtils.URL.DASH_HARIAN_HK + "1";
            getHarian(urlHarian, idUnit, roleId, 8, 10);
            //spinner
            listPeriod = new ArrayList<String>();
            listPeriod.add("08.00 - 09.59");
            listPeriod.add("10.00 - 11.59");
            listPeriod.add("12.00 - 13.59");
            listPeriod.add("14.00 - 15.59");
            listPeriod.add("16.00 - 16.59");
            listPeriod.add("17.00 - 18.59");
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listPeriod);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    urlHarian = ConstantUtils.URL.DASH_HARIAN_HK;
                    switch (i) {
                        case 0:
                            getHarian(urlHarian + "1", idUnit, roleId, 8, 10);
                            break;
                        case 1:
                            getHarian(urlHarian + "2", idUnit, roleId, 10, 12);
                            break;
                        case 2:
                            getHarian(urlHarian + "3", idUnit, roleId, 12, 14);
                            break;
                        case 3:
                            getHarian(urlHarian + "4", idUnit, roleId, 14, 16);
                            break;
                        case 4:
                            getHarian(urlHarian + "5", idUnit, roleId, 16, 17);
                            break;
                        case 5:
                            getHarian(urlHarian + "6", idUnit, roleId, 17, 19);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!roleId.equals("5")) {
            urlHarian = ConstantUtils.URL.DASH_HARIAN;
            getHarian(urlHarian, idUnit, roleId, 0, 0);
        } else {
            urlHarian = ConstantUtils.URL.DASH_HARIAN_HK + "1";
            getHarian(urlHarian, idUnit, roleId, 8, 10);
            spinner.setSelection(0);
        }
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
    private void getHarian(final String url, final String unit_id, final String role_id, final int jam_awal, final int jam_akhir) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

//        skeletonScreen = Skeleton.bind(dashRootView)
//                .load(R.layout.view_dashboard_shimmer)
//                .duration(1000)
//                .color(R.color.shimmer_color)
//                .angle(0)
//                .show();

        request = new StringRequest(Request.Method.GET, url + "/" + unit_id + "/" + role_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reloadHarian(url, unit_id, role_id, jam_awal, jam_akhir);
                            }
                            System.out.println("tah respon " + response);

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
                                    String tglServer = "";
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
                                        System.out.println("date " + date + " server " + tglServer);
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
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = (sudah / total) * 100.f;
                                    }
                                    final int round = Math.round(percent);

                                    if (percent < 50.f) {
                                        pie_harian.setMaxPercentage(100);
                                        pie_harian.setPercentage(percent);
                                        pie_harian.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                    } else if (percent <= 75.f) {
                                        pie_harian.setMaxPercentage(100);
                                        pie_harian.setPercentage(percent);
                                        pie_harian.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));
                                    } else {
                                        pie_harian.setMaxPercentage(100);
                                        pie_harian.setPercentage(percent);
                                        pie_harian.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));
                                    }

                                    PieAngleAnimation animation = new PieAngleAnimation(pie_harian);
                                    animation.setDuration(2500); //This is the duration of the animation in millis
                                    //pie_harian.startAnimation(animation);

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

                                        if (role_id.equals("5")) {
                                            if (currentTime >= jam_awal && currentTime < jam_akhir) {
                                                lay_harian.setBackground(getResources().getDrawable(R.drawable.dash_active));
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
                                                final String jam = jam_awal + ".00";
                                                lay_harian.setBackground(getResources().getDrawable(R.drawable.dash_nonactive));
                                                lay_harian.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Toast.makeText(getActivity(), "Maaf Pengisian checklist pada jam " + jam + " sudah melewati / belum memasuki waktu yang ditentukan",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                Toast.makeText(getActivity(), "Maaf Pengisian checklist pada jam " + jam + " sudah melewati / belum memasuki waktu yang ditentukan",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
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
                                        }
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
                        Toast.makeText(getActivity().getApplicationContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //GET MINGGUAN
    private void getMingguan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get Mingguan";

        request2 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_MINGGUAN + unit_id + "/" + role_id,
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

                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        pie_mingguan.setMaxPercentage(100);
                                        pie_mingguan.setPercentage(percent);
                                        pie_mingguan.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_mingguan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_mingguan.startAnimation(animation);
                                    } else if (percent <= 75.f) {
                                        pie_mingguan.setMaxPercentage(100);
                                        pie_mingguan.setPercentage(percent);
                                        pie_mingguan.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_mingguan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_mingguan.startAnimation(animation);
                                    } else {
                                        pie_mingguan.setMaxPercentage(100);
                                        pie_mingguan.setPercentage(percent);
                                        pie_mingguan.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_mingguan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_mingguan.startAnimation(animation);
                                    }
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

        request2.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    // GET 2 MINGGUAN
    private void get2Mingguan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get 2 mingguan";

        request3 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_2MINGGUAN + unit_id + "/" + role_id,
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
                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        pie_2minggu.setMaxPercentage(100);
                                        pie_2minggu.setPercentage(percent);
                                        pie_2minggu.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_2minggu);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_2minggu.startAnimation(animation);
                                    } else if (percent <= 75.f) {
                                        pie_2minggu.setMaxPercentage(100);
                                        pie_2minggu.setPercentage(percent);
                                        pie_2minggu.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_2minggu);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_2minggu.startAnimation(animation);
                                    } else {
                                        pie_2minggu.setMaxPercentage(100);
                                        pie_2minggu.setPercentage(percent);
                                        pie_2minggu.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_2minggu);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_2minggu.startAnimation(animation);
                                    }
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

        request3.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //GET BULANAN
    private void getBulanan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";

        request4 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_BULANAN + unit_id + "/" + role_id,
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

                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        pie_bulan.setMaxPercentage(100);
                                        pie_bulan.setPercentage(percent);
                                        pie_bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_bulan.startAnimation(animation);
                                    } else if (percent <= 75.f) {
                                        pie_bulan.setMaxPercentage(100);
                                        pie_bulan.setPercentage(percent);
                                        pie_bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_bulan.startAnimation(animation);
                                    } else {
                                        pie_bulan.setMaxPercentage(100);
                                        pie_bulan.setPercentage(percent);
                                        pie_bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_bulan.startAnimation(animation);
                                    }
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

        request4.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //GET 3 BULANAN
    private void get3Bulanan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";

        request5 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_3BULANAN + unit_id + "/" + role_id,
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

                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        pie_3bulan.setMaxPercentage(100);
                                        pie_3bulan.setPercentage(percent);
                                        pie_3bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_3bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_3bulan.startAnimation(animation);
                                    } else if (percent <= 75.f) {
                                        pie_3bulan.setMaxPercentage(100);
                                        pie_3bulan.setPercentage(percent);
                                        pie_3bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_3bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_3bulan.startAnimation(animation);
                                    } else {
                                        pie_3bulan.setMaxPercentage(100);
                                        pie_3bulan.setPercentage(percent);
                                        pie_3bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_3bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_3bulan.startAnimation(animation);
                                    }
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

        request5.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //GET 6BULANAN
    private void get6Bulanan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";

        request6 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_6BULANAN + unit_id + "/" + role_id,
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

                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    //percent = sudah / total * 100.f;
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        pie_6bulan.setMaxPercentage(100);
                                        pie_6bulan.setPercentage(percent);
                                        pie_6bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_6bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_6bulan.startAnimation(animation);
                                    } else if (percent <= 75.f) {
                                        pie_6bulan.setMaxPercentage(100);
                                        pie_6bulan.setPercentage(percent);
                                        pie_6bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_6bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_6bulan.startAnimation(animation);
                                    } else {
                                        pie_6bulan.setMaxPercentage(100);
                                        pie_6bulan.setPercentage(percent);
                                        pie_6bulan.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_6bulan);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_6bulan.startAnimation(animation);
                                    }
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

        request6.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //GET TAHUNAN
    private void getTahunan(final String unit_id, final String role_id) {
        final String REQUEST_TAG = "get request";

        request7 = new StringRequest(Request.Method.GET, ConstantUtils.URL.DASH_TAHUN + unit_id + "/" + role_id,
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

                                    final float sudah = total - belum;
                                    if (total == 0) {
                                        percent = 0f;
                                    } else {
                                        percent = sudah / total * 100.f;
                                    }
                                    final int round = Math.round(percent);
                                    if (percent < 50.f) {
                                        pie_tahun.setMaxPercentage(100);
                                        pie_tahun.setPercentage(percent);
                                        pie_tahun.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_tahun);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_tahun.startAnimation(animation);
                                    } else if (percent <= 75.f) {
                                        pie_tahun.setMaxPercentage(100);
                                        pie_tahun.setPercentage(percent);
                                        pie_tahun.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_tahun);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_tahun.startAnimation(animation);
                                    } else {
                                        pie_tahun.setMaxPercentage(100);
                                        pie_tahun.setPercentage(percent);
                                        pie_tahun.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));

                                        PieAngleAnimation animation = new PieAngleAnimation(pie_tahun);
                                        animation.setDuration(2500); //This is the duration of the animation in millis
                                        //pie_tahun.startAnimation(animation);
                                    }
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

        request7.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //GET WO
    private void getWO(final String unit_id, String role_id) {
        final String REQUEST_TAG = "get request";

        if (role_id.equals("1")) {
            link = ConstantUtils.URL.DASH_PUNCH_SPV + unit_id;
        } else {
            link = ConstantUtils.URL.DASH_PUNCH + unit_id + "/" + role_id;
        }
        request8 = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.DASHBOARD.TAG_TITLE);
                            listModel = new ArrayList<ModelDashboard>();
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

                                if (total == 0) {
                                    percent = 0f;
                                } else {
                                    percent = (float) sudah / total * 100.f;
                                }
                                final int round = Math.round(percent);
                                if (percent < 50.f) {
                                    pie_punch.setMaxPercentage(100);
                                    pie_punch.setPercentage(percent);
                                    pie_punch.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                    PieAngleAnimation animation = new PieAngleAnimation(pie_punch);
                                    animation.setDuration(2500); //This is the duration of the animation in millis
                                    //pie_punch.startAnimation(animation);
                                } else if (percent <= 75.f) {
                                    pie_punch.setMaxPercentage(100);
                                    pie_punch.setPercentage(percent);
                                    pie_punch.setPercentageBackgroundColor(getResources().getColor(R.color.colorYellow));

                                    PieAngleAnimation animation = new PieAngleAnimation(pie_punch);
                                    animation.setDuration(2500); //This is the duration of the animation in millis
                                    //pie_punch.startAnimation(animation);
                                } else {
                                    pie_punch.setMaxPercentage(100);
                                    pie_punch.setPercentage(percent);
                                    pie_punch.setPercentageBackgroundColor(getResources().getColor(R.color.colorGreen));

                                    PieAngleAnimation animation = new PieAngleAnimation(pie_punch);
                                    animation.setDuration(2500); //This is the duration of the animation in millis
                                    //pie_punch.startAnimation(animation);
                                }
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
                            progressBar.setVisibility(View.GONE);
                            //skeletonScreen.hide();

                            //showCase();

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

        request8.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding JsonObject request to request queue
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppSingleton.getInstance(getActivity()).addToRequestQueue(request8, REQUEST_TAG);
            }
        }, 1000);
    }

    private void reloadHarian(String url, String unit, String role, int awal, int akhir) {
        getHarian(url, unit, role, awal, akhir);
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

    private void showCase() {
//        new MaterialShowcaseView.Builder(getActivity())
//                .setTarget(lay_harian)
//                .setDismissText("GOT IT")
//                .setContentText("Menampilkan data checklist untuk masing-masing periode ")
//                .show();

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "1");

        sequence.setConfig(config);

        sequence.addSequenceItem(lay_harian,
                "This is button one", "GOT IT");

        sequence.addSequenceItem(pie_harian,
                "This is button two", "GOT IT");

//        sequence.addSequenceItem(mButtonThree,
//                "This is button three", "GOT IT");

        sequence.start();
    }
}