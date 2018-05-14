package id.co.gsd.mybirawa.controller.sc;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {


    SessionManager session;


    String tempJenis1,tempBulan1,tempJenis2,tempBulan2,tempArea,tempUnit;
    TextView jenis1Text,jenis2Text,bulan1Text,bulan2Text,areaText,unitText;
    ImageView jenis1btn,jenis2Btn,bulan1Btn,bulan2Btn,areaBtn,unitBtn;
    Button stokBtn,transaksiBtn;

    ArrayList<HashMap<String,String>> listDataJenis =  new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String,String>> listDataArea =  new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String,String>> listDataUnit =  new ArrayList<HashMap<String,String>>();

    ArrayList<String> bulan= new ArrayList<>();
    ArrayList<String> jenis= new ArrayList<>();
    ArrayList<String> area= new ArrayList<>();
    ArrayList<String> unit= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
    Typeface font,fontbold;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        TextView toolbar = (TextView) view.findViewById(R.id.request);
        toolbar.setTypeface(fontbold);

        session = new SessionManager(getActivity());

        jenis1Text = view.findViewById(R.id.jenis);
        jenis2Text = view.findViewById(R.id.jenisText2);
        bulan1Text = view.findViewById(R.id.bulan);
        bulan2Text = view.findViewById(R.id.bulanText2);
        areaText = view.findViewById(R.id.areaText);
        unitText = view.findViewById(R.id.fmText);

        jenis1btn = view.findViewById(R.id.jenisButton);
        jenis2Btn = view.findViewById(R.id.jenisButton2);
        bulan1Btn = view.findViewById(R.id.bulanButton);
        bulan2Btn = view.findViewById(R.id.bulanButton2);
        areaBtn  = view.findViewById(R.id.areaButton);
        unitBtn = view.findViewById(R.id.fmButton);

        stokBtn = view.findViewById(R.id.stokBtn);
        transaksiBtn = view.findViewById(R.id.transBtn);

        tempJenis1 = "";
        tempJenis2 = "";
        tempArea = "";
        tempBulan1 = "";
        tempBulan2 = "";
        tempUnit = "";

        CardView v1 = view.findViewById(R.id.view);
        CardView v2 = view.findViewById(R.id.view2);

        if(session.getRoleId().equals("98")){
            v1.setVisibility(View.GONE);
        }else{
            v2.setVisibility(View.GONE);
        }


        getJenisNAreaList();
        initBulan();



        jenis1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),jenis,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        jenis1Text.setText(jenis.get(i));
                        tempJenis1 = listDataJenis.get(i).get("id");
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        jenis1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),jenis,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        jenis1Text.setText(jenis.get(i));
                        tempJenis1 = listDataJenis.get(i).get("id");
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        //----------------------------------------

        jenis2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),jenis,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        jenis2Text.setText(jenis.get(i));
                        tempJenis2 = listDataJenis.get(i).get("id");
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        jenis2Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),jenis,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        jenis2Text.setText(jenis.get(i));
                        tempJenis2 = listDataJenis.get(i).get("id");
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        //----------------------------------------

        bulan1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),bulan,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        bulan1Text.setText(bulan.get(i));
                        tempBulan1 = ""+(i+1);
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        bulan1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),bulan,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        bulan1Text.setText(bulan.get(i));
                        tempBulan1 = ""+(i+1);
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });


        //----------------------------------------

        bulan2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),bulan,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        bulan2Text.setText(bulan.get(i));
                        tempBulan2 = ""+(i+1);
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        bulan2Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),bulan,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        bulan2Text.setText(bulan.get(i));
                        tempBulan2 = ""+(i+1);
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        //----------------------------------------

        unitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),unit,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        unitText.setText(unit.get(i));
                        tempUnit = listDataUnit.get(i).get("id");
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        unitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),unit,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        unitText.setText(unit.get(i));
                        tempUnit = listDataUnit.get(i).get("id");
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        //----------------------------------------

        areaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),area,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        areaText.setText(area.get(i));
                        tempArea = listDataArea.get(i).get("id");
                        getUnitList(tempArea);
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        areaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogs = new SpinnerDialog(getActivity(),area,"Select item");
                spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String value, int i) {
                        areaText.setText(area.get(i));
                        tempArea = listDataArea.get(i).get("id");
                        getUnitList(tempArea);
                    }
                });
                spinnerDialogs.showSpinerDialog();
            }
        });

        stokBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tempJenis1.equals("") || tempBulan1.equals("")){
                    Toast.makeText(getActivity(),"Input belum lengkap",Toast.LENGTH_SHORT).show();
                }else{
                    submitStok(tempBulan1,tempJenis1);
                }
            }
        });

        transaksiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tempJenis2.equals("") || tempBulan2.equals("") || tempArea.equals("") || tempUnit.equals("")){
                    Toast.makeText(getActivity(),"Input belum lengkap",Toast.LENGTH_SHORT).show();
                }else{
                    submitTransaksi(tempBulan2,tempJenis2,tempArea,tempUnit);
                }
            }
        });

        return view;
    }

    public void initBulan(){
        bulan.add("Januari");
        bulan.add("Februari");
        bulan.add("Maret");
        bulan.add("April");
        bulan.add("Mei");
        bulan.add("Juni");
        bulan.add("Juli");
        bulan.add("Agustus");
        bulan.add("September");
        bulan.add("Oktober");
        bulan.add("November");
        bulan.add("Desember");
    }


    private void getJenisNAreaList() {
        // refreshLayout.setRefreshing(true);
        final String REQUEST_TAG = "get request news";

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scReportParam/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_jenis");
                            listDataJenis = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id",object.getString("id")) ;
                                temp.put("name",object.getString("name")) ;
                                listDataJenis.add(temp);
                                jenis.add(object.getString("name"));
                            }


                            JSONArray jsonArray2 = jsonObject.getJSONArray("list_area");
                            listDataArea = new ArrayList<HashMap<String, String>>();

                            for (int i = 0; i < jsonArray2.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray2.getJSONObject(i);
                                temp.put("id",object.getString("id")) ;
                                temp.put("name",object.getString("name")) ;
                                listDataArea.add(temp);
                                area.add(object.getString("name"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }


    private void getUnitList(String idArea) {
        // refreshLayout.setRefreshing(true);
        final String REQUEST_TAG = "get request news";

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scReportUnit/"+idArea,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_unit");
                            listDataUnit = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            unit = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id",object.getString("id")) ;
                                temp.put("name",object.getString("nama_unit")) ;
                                listDataUnit.add(temp);
                                unit.add(object.getString("nama_unit"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }

    private void submitStok(final String bulan,final String jenis) {
        // refreshLayout.setRefreshing(true);
        final String REQUEST_TAG = "get request news";

        final StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/reportTransaksi/"+session.getIdUnit()+"/-/"+jenis+"/"+bulan+"/"+session.getRoleId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            System.out.println(response);
                            if(status.equals("1")){
                                Toast.makeText(getActivity(),"File has been exported..",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(),"File cannot be exported..",Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {

                            if(response!=null){

                                try{
                                    String url = ConstantUtils.URL.SERVER+"api_data/reportTransaksi/"+session.getIdUnit()+"/-/"+jenis+"/"+bulan+"/"+session.getRoleId();
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                    request.setDescription("Laporan Transaksi Birawa sc Bulan_"+bulan+".xlsx");
                                    request.setTitle("Laporan Transaksi Birawa sc Bulan_"+bulan+".xlsx");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        request.allowScanningByMediaScanner();
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    }
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Laporan Transaksi Birawa sc Bulan_"+bulan+".xlsx");

                                    DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                    Toast.makeText(getActivity(), "Berkas telah berhasil di unduh. Silahkan cek download untuk melihat berkas.", Toast.LENGTH_LONG).show();
                                }catch (Exception ex){
                                    System.out.println(ex);
                                }
                            }else{
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println(request);
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }


    private void submitTransaksi(final String bulan,final String jenis,String area,String unit) {
        // refreshLayout.setRefreshing(true);
        final String REQUEST_TAG = "get request news";

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/reportTransaksi/"+unit+"/"+area+"/"+jenis+"/"+bulan+"/"+session.getRoleId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            System.out.println(response);
                            if(status.equals("1")){
                                Toast.makeText(getActivity(),"File has been exported..",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(),"File cannot be exported..",Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            if(response!=null){

                                try{
                                    String url = ConstantUtils.URL.SERVER+"api_data/reportTransaksi/"+session.getIdUnit()+"/-/"+jenis+"/"+bulan+"/"+session.getRoleId();
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                    request.setDescription("Laporan Transaksi Birawa sc Bulan_"+bulan+".xlsx");
                                    request.setTitle("Laporan Transaksi Birawa sc Bulan_"+bulan+".xlsx");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        request.allowScanningByMediaScanner();
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    }
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Laporan Transaksi Birawa sc Bulan_"+bulan+".xlsx");

                                    DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                    Toast.makeText(getActivity(), "Berkas telah berhasil di unduh. Silahkan cek download untuk melihat berkas.", Toast.LENGTH_LONG).show();
                                }catch (Exception ex){
                                    System.out.println(ex);
                                }
                            }else{
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println(request);
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }

}
