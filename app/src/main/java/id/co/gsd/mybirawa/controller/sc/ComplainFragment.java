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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComplainFragment extends Fragment {


    //Variable Listview
    ListView listmenu;
    ImageView logout;
    private FloatingActionButton fab;
    SessionManager session;

    ArrayList<HashMap<String,String>> listData;
    private static Adapter_complaint adapter;
    Typeface font,fontbold;
    ProgressBar pbar;
    String statusOr;

    ArrayList<String> dftralamat= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
    ImageView almts;
    TextView pilihalamat;

    public ComplainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complain, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        TextView toolbar = (TextView) view.findViewById(R.id.request);
        toolbar.setTypeface(fontbold);

        session = new SessionManager(getActivity());

        listmenu=(ListView)view.findViewById(R.id.list_request);


        List<String> categories = new ArrayList<String>();

        if(session.getRoleId().equals("14")){
            statusOr = "0";
            categories.add("On Progress");
            dftralamat.add("On Progress");
            categories.add("Reject");
            dftralamat.add("Reject");
            categories.add("Complete");
            dftralamat.add("Complete");

        }else{
            statusOr = "1";
            categories.add("Waiting Approval");
            dftralamat.add("Waiting Approval");
            categories.add("On Progress");
            dftralamat.add("On Progress");
            categories.add("Reject");
            dftralamat.add("Reject");
            categories.add("Complete");
            dftralamat.add("Complete");
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner statusSpin = view.findViewById(R.id.statusSpinner);

        pbar = view.findViewById(R.id.pb);

        pbar.setVisibility(View.GONE);

        statusSpin.setAdapter(dataAdapter);

        statusSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(session.getRoleId().equals("14") ){
                    if(i==0){
                        statusOr = "0";
                        showList("bm","op");
                    }else if(i==1){
                        statusOr = "0";
                        showList("bm","re");
                    }else{
                        statusOr = "0";
                        showList("bm","co");
                    }
                }else if(session.getRoleId().equals("12") ){
                    if(i==0){
                        statusOr = "1";
                        showList("area","wa");
                    }else if(i==1){
                        statusOr = "0";
                        showList("area","op");
                    }else if(i==2){
                        statusOr = "0";
                        showList("area","re");
                    }else{
                        statusOr = "0";
                        showList("area","co");
                    }
                }else if(session.getRoleId().equals("11")){
                    if(i==0){
                        statusOr = "1";
                        showList("fm","wa");
                    }else if(i==1){
                        statusOr = "0";
                        showList("fm","op");
                    }else if(i==2){
                        statusOr = "0";
                        showList("fm","re");
                    }else{
                        statusOr = "0";
                        showList("fm","co");
                    }
                }else{
                    if(i==0){
                        statusOr = "1";
                        showList("pusat","wa");
                    }else if(i==1){
                        statusOr = "0";
                        showList("pusat","op");
                    }else if(i==2){
                        statusOr = "0";
                        showList("pusat","re");
                    }else{
                        statusOr = "0";
                        showList("pusat","co");
                    }
                }
                System.out.println("stat :"+statusOr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        pilihalamat = (TextView) view.findViewById(R.id.jenisitem2);
        pilihalamat.setTypeface(fontbold);

        spinnerDialogs = new SpinnerDialog(getActivity(),dftralamat,"Select item");
        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String value, int i) {
                //Toast.makeText(getActivity(),""+lantai,Toast.LENGTH_SHORT).show();
                pilihalamat.setText(value);
                if(session.getRoleId().equals("14") ){
                    if(i==0){
                        statusOr = "0";
                        showList("bm","op");

                    }else if(i==1){
                        statusOr = "0";
                        showList("bm","re");
                    }else{
                        statusOr = "0";
                        showList("bm","co");
                    }
                }else if(session.getRoleId().equals("12") ){
                    if(i==0){
                        statusOr = "1";
                        showList("area","wa");
                    }else if(i==1){
                        statusOr = "0";
                        showList("area","op");
                    }else if(i==2){
                        statusOr = "0";
                        showList("area","re");
                    }else{
                        statusOr = "0";
                        showList("area","co");
                    }
                }else if(session.getRoleId().equals("11")){
                    if(i==0){
                        statusOr = "1";
                        showList("fm","wa");
                    }else if(i==1){
                        statusOr = "0";
                        showList("fm","op");
                    }else if(i==2){
                        statusOr = "0";
                        showList("fm","re");
                    }else{
                        statusOr = "0";
                        showList("fm","co");
                    }
                }else{
                    if(i==0){
                        statusOr = "1";
                        showList("pusat","wa");
                    }else if(i==1){
                        statusOr = "0";
                        showList("pusat","op");
                    }else if(i==2){
                        statusOr = "0";
                        showList("pusat","re");
                    }else{
                        statusOr = "0";
                        showList("pusat","co");
                    }
                }
                System.out.println("stat :"+statusOr);
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





        if(session.getRoleId().equals("14") ){
            showList("bm","op");
            pilihalamat.setText("On Progress");
        }else if(session.getRoleId().equals("12") ){
            showList("area","wa");
            pilihalamat.setText("Waiting Approval");
        }else if(session.getRoleId().equals("14")){
            showList("fm","wa");
            pilihalamat.setText("Waiting Approval");
        }else{
            showList("pusat","wa");
            pilihalamat.setText("Waiting Approval");
        }

        return view;
    }

    private void showList(String role, final String status) {
        // refreshLayout.setRefreshing(true);
        final String REQUEST_TAG = "get request news";

        String userId = "";
        if(session.getRoleId().equals("14") ){
            userId = session.getId();
        }else{
            userId = session.getIdUnit();
        }
        pbar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scListComplaint/"+role+"/"+status+"/"+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_complaint");
                            listData = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id_order_sc",object.getString("id_order_sc")) ;
                                temp.put("order_sc_number",object.getString("order_sc_number")) ;
                                temp.put("id_user_fm",object.getString("id_user_fm")) ;
                                temp.put("id_user_area",object.getString("id_user_area")) ;
                                temp.put("order_date_area",object.getString("order_date_area")) ;
                                temp.put("id_user_pusat",object.getString("id_user_pusat")) ;
                                temp.put("order_date_pusat",object.getString("order_date_pusat")) ;
                                temp.put("id_user_receive",object.getString("id_user_receive")) ;
                                temp.put("received_date_fm",object.getString("received_date_fm")) ;
                                temp.put("status_name",object.getString("status_name")) ;
                                temp.put("evidence_order",object.getString("evidence_order")) ;
                                temp.put("order_date_fm",object.getString("order_date_fm")) ;
                                temp.put("id_alamat",object.getString("id_alamat")) ;
                                temp.put("id_user_mitra",object.getString("id_user_mitra")) ;
                                temp.put("cp_driver",object.getString("cp_driver")) ;
                                temp.put("delivery_date_pusat",object.getString("delivery_date_pusat")) ;
                                temp.put("delivery_date_mitra",object.getString("delivery_date_mitra")) ;
                                temp.put("approve_date_mitra",object.getString("approve_date_mitra")) ;
                                temp.put("reason_complaint",object.getString("reason_complaint")) ;
                                temp.put("notes",object.getString("notes")) ;
                                temp.put("id_user_order",object.getString("id_user_order")) ;
                                temp.put("order_date",object.getString("order_date")) ;
                                temp.put("nama_user",object.getString("nama_user")) ;
                                temp.put("fm_bm",object.getString("fm_bm")) ;
                                temp.put("alamat",object.getString("alamat")) ;
                                temp.put("kota",object.getString("kota")) ;
                                temp.put("mitra",object.getString("mitra")) ;
                                temp.put("nama_cp",object.getString("nama_cp")) ;
                                temp.put("telepon",object.getString("telepon")) ;
                                listData.add(temp);
                            }

                            adapter = new Adapter_complaint(getActivity(), listData);
                            listmenu.setAdapter(adapter);

                            listmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent it = new Intent(getContext(), ComplaintDetailActivity.class);
                                    it.putExtra("data",listData.get(i));
                                    it.putExtra("on",statusOr);
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


}
