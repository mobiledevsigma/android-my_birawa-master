package id.co.gsd.mybirawa.controller.sc;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import java.util.HashMap;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.NonScrollListView;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateStockFragment extends Fragment {


    SessionManager session;

    //Variable Listview
    NonScrollListView listmenu;
    ImageView logout;
    private FloatingActionButton fab;

    ArrayList<String> dftralamat= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
    ImageView almts;
    TextView pilihalamat;
    ArrayList<HashMap<String,String>> listDataAlamat;
    ArrayList<HashMap<String,String>> listData;
    ProgressBar pbar;
    int selectedPosition;

    //ArrayList<dataModel> listData;
    private static Adapter_updatestok adapter;
    Typeface font,fontbold;

    public UpdateStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_stok, container,false);

        session = new SessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        selectedPosition = 0;
        listmenu=(NonScrollListView)view.findViewById(R.id.list_stok);

        pbar = view.findViewById(R.id.pb);

        TextView toolbar3 = (TextView) view.findViewById(R.id.item_jenis1);
        toolbar3.setTypeface(fontbold);

      //  Adapter_updatestok adapter=new Adapter_updatestok (getActivity(),jumlahstok,nama_barang);
       // listmenu.setAdapter(adapter);

        pilihalamat = (TextView) view.findViewById(R.id.jenisitem2);
        pilihalamat.setTypeface(fontbold);

        spinnerDialogs = new SpinnerDialog(getActivity(),dftralamat,"Select item");
        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String value, int position) {
                //Toast.makeText(getActivity(),""+lantai,Toast.LENGTH_SHORT).show();

                String idAlamat = listDataAlamat.get(position).get("id_alamat");
                //String idAlamat = listDataAlamat.get(position).get("fm_bm");
                showListStock(idAlamat);
                pilihalamat.setText(value);
                selectedPosition = position;
            }
        });

        almts = (ImageView) view.findViewById(R.id.pilihanjenis);
        almts.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs.showSpinerDialog();

            }
        });

        pilihalamat.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs.showSpinerDialog();

            }
        });

        TextView txtTitle_tiga = (TextView) view.findViewById(R.id.itemfm);
        txtTitle_tiga.setTypeface(fontbold);

        TextView txtTitle_tigaa = (TextView) view.findViewById(R.id.item_jenis);
        txtTitle_tigaa.setTypeface(fontbold);

        logout = (ImageView) view.findViewById(R.id.logouts);
        showListAlamat(session.getRoleId(),session.getIdUnit());
        return  view;
    }



    private void showListAlamat(String role,String unit) {

        final String REQUEST_TAG = "get request news";


        //pbar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scListAlamat/"+role+"/"+unit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_alamat");
                            listDataAlamat = new ArrayList<HashMap<String, String>>();
                            dftralamat = new ArrayList<>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id_alamat",object.getString("id_alamat")) ;
                                temp.put("area_id",object.getString("area_id")) ;
                                temp.put("fm_bm",object.getString("fm_bm")) ;
                                temp.put("alamat",object.getString("alamat")) ;
                                temp.put("kota",object.getString("kota")) ;
                                temp.put("nama_cp",object.getString("nama_cp")) ;
                                temp.put("telepon",object.getString("telepon")) ;
                                temp.put("unit_id",object.getString("unit_id")) ;
                                listDataAlamat.add(temp);
                                dftralamat.add(object.getString("fm_bm"));
                            }


                            spinnerDialogs = new SpinnerDialog(getActivity(),dftralamat,"Select item");
                            spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
                                @Override
                                public void onClick(String value, int position) {
                                    //Toast.makeText(getActivity(),""+lantai,Toast.LENGTH_SHORT).show();

                                    String idAlamat = listDataAlamat.get(position).get("id_alamat");
                                    showListStock(idAlamat);
                                    pilihalamat.setText(value);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Periksa koneksi internet anda",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        pbar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }


    private void showListStock(String idAlamat) {

        final String REQUEST_TAG = "get request news";


        pbar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scListStock/"+idAlamat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_stock");
                            listData = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id_stock_sc",object.getString("id_stock_sc")) ;
                                temp.put("qty_stock",object.getString("qty_stock")) ;
                                temp.put("id_perangkat_sc",object.getString("id_perangkat_sc")) ;
                                temp.put("id_alamat",object.getString("id_alamat")) ;
                                temp.put("created_date",object.getString("created_date")) ;
                                temp.put("qty_kurang",object.getString("qty_kurang")) ;
                                temp.put("nama_perangkat_sc",object.getString("nama_perangkat_sc")) ;
                                temp.put("gambar",object.getString("gambar")) ;
                                temp.put("fm_bm",object.getString("fm_bm")) ;
                                listData.add(temp);
                            }

                            adapter = new Adapter_updatestok(getActivity(), listData);
                            listmenu.setAdapter(adapter);

                            listmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent it = new Intent(getContext(), StockUpdateActivity.class);
                                    it.putExtra("data",listData.get(i));
                                    getContext().startActivity(it);
                                }
                            });
                            listmenu.invalidateViews();

                            pbar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pbar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        pbar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(listDataAlamat !=null  && listDataAlamat.size()>0){
            String idAlamat = listDataAlamat.get(selectedPosition).get("id_alamat");
            showListStock(idAlamat);
        }
    }
}


