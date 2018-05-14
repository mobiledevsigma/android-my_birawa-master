package id.co.gsd.mybirawa.controller.sc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.NonScrollListView;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class ComplaintDetailActivity extends AppCompatActivity {

//    String[] nama_item ={
//            "Air Mineral",
//            "Tissue",
//            "Tissu Gulung",
//            "Air Galon",
//    };
//
//    String[] nama_items ={
//            "Air Mineral",
//            "Tissue",
//            "Tissu Gulung",
//            "Air Galon",
//    };
//    String[] no={
//            "2",
//            "3",
//            "10",
//            "5",
//    };
    //Variable Listview
    ListView listmenu;
    NonScrollListView listmenu2;
    private FloatingActionButton fab;
    SessionManager session;

    ArrayList<HashMap<String,String>> listData;

    //ArrayList<dataModel> listData;
    private static Adapter_detail adapter;
    private static Adapter_detail adapter2;
    Typeface font,fontbold;

    HashMap<String,String> data;
    String stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_complaint);

        data = (HashMap<String, String>)getIntent().getSerializableExtra("data");
        stat = getIntent().getStringExtra("on");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");

        session = new SessionManager(ComplaintDetailActivity.this);

        font = Typeface.createFromAsset(ComplaintDetailActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(ComplaintDetailActivity.this.getAssets(),"fonts/Nexa Bold.otf");

        TextView txtTitle = (TextView) findViewById(R.id.no_order);
        txtTitle.setText("No. Order : "+data.get("order_sc_number"));
        txtTitle.setTypeface(fontbold);

        Button trace = findViewById(R.id.trace);

        trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), RequestOrderTraceActivity.class);
                                    it.putExtra("orderId",data.get("order_sc_number"));
                                    it.putExtra("is_complaint",true);
                                    getApplicationContext().startActivity(it);
            }
        });

        TextView txtTitle_tiga11 = (TextView) findViewById(R.id.request_item);
        txtTitle_tiga11.setTypeface(font);

        TextView txtTitle_tiga11f = (TextView) findViewById(R.id.ket_revisi);
        txtTitle_tiga11f.setTypeface(font);

        EditText ttxtTitle_tiga11 = (EditText) findViewById(R.id.deskripsi);
        ttxtTitle_tiga11.setTypeface(font);

        TextView txtTitle_ttiga11 = (TextView) findViewById(R.id.Detail);
        txtTitle_ttiga11.setTypeface(font);

        TextView txtTitle_tiga11fs = (TextView) findViewById(R.id.a);
        txtTitle_tiga11fs.setTypeface(fontbold);

        TextView ttxtTitle_iga11 = (TextView) findViewById(R.id.b);
        ttxtTitle_iga11.setTypeface(fontbold);

//        TextView ttxtTitle_iga1s1 = (TextView) findViewById(R.id.c);
//        ttxtTitle_iga1s1.setTypeface(fontbold);

        TextView a= (TextView) findViewById(R.id.ket);
        a.setTypeface(font);

        EditText b= (EditText) findViewById(R.id.editText2);
        b.setTypeface(font);

        TextView ctn = findViewById(R.id.isi);
//        temp.put("id_order_sc",object.getString("id_order_sc")) ;
//        temp.put("order_sc_number",object.getString("order_sc_number")) ;
//        temp.put("id_user_fm",object.getString("id_user_fm")) ;
//        temp.put("id_user_area",object.getString("id_user_area")) ;
//        temp.put("order_date_area",object.getString("order_date_area")) ;
//        temp.put("id_user_pusat",object.getString("id_user_pusat")) ;
//        temp.put("order_date_pusat",object.getString("order_date_pusat")) ;
//        temp.put("id_user_receive",object.getString("id_user_receive")) ;
//        temp.put("received_date_fm",object.getString("received_date_fm")) ;
//        temp.put("status_order",object.getString("status_order")) ;
//        temp.put("evidence_order",object.getString("evidence_order")) ;
//        temp.put("order_date_fm",object.getString("order_date_fm")) ;
//        temp.put("id_alamat",object.getString("id_alamat")) ;
//        temp.put("id_user_mitra",object.getString("id_user_mitra")) ;
//        temp.put("cp_driver",object.getString("cp_driver")) ;
//        temp.put("delivery_date_pusat",object.getString("delivery_date_pusat")) ;
//        temp.put("delivery_date_mitra",object.getString("delivery_date_mitra")) ;
//        temp.put("approve_date_mitra",object.getString("approve_date_mitra")) ;
//        temp.put("status_complaint",object.getString("status_complaint")) ;
//        temp.put("notes",object.getString("notes")) ;
//        temp.put("id_user_order",object.getString("id_user_order")) ;
//        temp.put("order_date",object.getString("order_date")) ;
//        temp.put("nama",object.getString("nama")) ;
//        temp.put("status_name",object.getString("status_name")) ;

        TextView isi = findViewById(R.id.isi2);
        isi.setText("Waktu Order : "+ data.get("order_date")+"\n\n"+
                "Request Oleh: "+ data.get("nama_user")+"\n"+
                "Lokasi : "+ data.get("fm_bm")+"\n"+
                "Alamat : "+ data.get("alamat")+ " - "+ data.get("kota")+ "\n"+
                "Telepon : "+ data.get("telepon")+"\n\n"+
                "Mitra : "+ data.get("mitra")+"\n"+
                "Waktu Pengiriman : "+ data.get("delivery_date_mitra").replace("null","-")+"\n"+
                "CP Driver : "+ data.get("cp_driver").replace("null","-")+"\n"+
                "Telepon Mitra : "+ data.get("telepon")+"\n\n"+
                "Alasan Complaint : "+ data.get("reason_complaint").replace("null","-")+"\n"+
                "Status : "+ data.get("status_name")+"");


        final LinearLayout pertama = (LinearLayout) findViewById(R.id.revisi);
        final LinearLayout pertamma = (LinearLayout) findViewById(R.id.reject);
        final LinearLayout pertamaa = (LinearLayout) findViewById(R.id.approve);

        Button sendBtn = (Button) findViewById(R.id.button);
        Button sendBtn1 = (Button) findViewById(R.id.button2);
        Button sendBtn2 = (Button) findViewById(R.id.button3);
        if(session.getRoleId().equals("14")){
            pertama.setVisibility(View.GONE);
            pertamaa.setVisibility(View.GONE);
            sendBtn.setVisibility(View.GONE);
           // sendBtn1.setVisibility(View.GONE);
            //sendBtn2.setVisibility(View.GONE);
        }else{
         //   pertama.setVisibility(View.VISIBLE);
          //  pertamaa.setVisibility(View.VISIBLE);
        }

        System.out.println("stat:"+stat);
        if(stat.equals("1")){
            sendBtn.setVisibility(View.VISIBLE);
            sendBtn2.setVisibility(View.VISIBLE);
        }else{
            sendBtn.setVisibility(View.GONE);
            sendBtn2.setVisibility(View.GONE);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = pertamaa.getVisibility();
                //if(visibility==View.GONE)
                    // set ListView visible
//                    pertamaa.setVisibility(View.VISIBLE);
//                    pertama.setVisibility(View.GONE);
//                    pertamma.setVisibility(View.GONE);
               // else if (visibility == View.VISIBLE)
                //    pertamaa.setVisibility(View.GONE);

                new AlertDialog.Builder(ComplaintDetailActivity.this)
                        .setMessage("Approve Request order ini ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                submitUpdate("1","-");
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        sendBtn1.setVisibility(View.INVISIBLE);
        sendBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = pertama.getVisibility();
                //if(visibility==View.GONE)
                    // set ListView visible
//                    pertama.setVisibility(View.VISIBLE);
//                    pertamaa.setVisibility(View.GONE);
//                    pertamma.setVisibility(View.GONE);
                //else if (visibility == View.VISIBLE)
                  //  pertama.setVisibility(View.GONE);



            }
        });

        sendBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = pertamma.getVisibility();
               // if(visibility==View.GONE)
                    // set ListView visible
//                    pertamma.setVisibility(View.VISIBLE);
//                    pertama.setVisibility(View.GONE);
//                    pertamaa.setVisibility(View.GONE);
               // else if (visibility == View.VISIBLE)
                 //   pertamma.setVisibility(View.GONE);
                final EditText input = new EditText(ComplaintDetailActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setWidth(300);


                new AlertDialog.Builder(ComplaintDetailActivity.this)
                        .setMessage("Alasan reject ?")
                        .setCancelable(false)
                        .setView(input)
                        .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                submitUpdate("0",input.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });

        Button submit = (Button)findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ComplaintDetailActivity.this,"Data telah disubmit",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        listmenu=(ListView) findViewById(R.id.list_reject);

//        Adapter_reject2 adapter=new Adapter_reject2(Detail_requestActivity.this,nama_item);
//        listmenu.setAdapter(adapter);

        listmenu2=(NonScrollListView) findViewById(R.id.list_detail);

//        Adapter_detail adapter2=new Adapter_detail(Detail_requestActivity.this,nama_items,no);
//        listmenu2 .setAdapter(adapter2);

        showList();

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void showList() {
        // refreshLayout.setRefreshing(true);
        final String REQUEST_TAG = "get request news";

        String userId = session.getIdUnit();
        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scListItemComplaint/"+data.get("order_sc_number"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_item");
                            listData = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("nama_perangkat_sc",object.getString("nama_perangkat_sc")) ;
                                temp.put("qty_request_sc",object.getString("qty_request_sc")) ;
                                temp.put("qty_perangkat_sc",object.getString("qty_perangkat_sc")) ;
                                temp.put("qty_request_sc",object.getString("qty_request_sc")) ;
                                temp.put("status_transaksi",object.getString("status_transaksi")) ;
                                temp.put("gambar",object.getString("gambar")) ;
                                temp.put("stok_ditolak",object.getString("stok_ditolak")) ;
                                temp.put("id_perangkat_sc",object.getString("id_perangkat_sc")) ;
                                temp.put("id_transaksi",object.getString("id_transaksi")) ;

                                listData.add(temp);
                            }

                            adapter = new Adapter_detail(getApplicationContext(), listData);
                            listmenu2.setAdapter(adapter);

                            if(session.getRoleId().equals("12") || session.getRoleId().equals("98")){
                                listmenu2.setSelected(true);
                                listmenu2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        Intent in = new Intent(ComplaintDetailActivity.this,RequestOrderTransActivity.class);
                                        in.putExtra("data",listData.get(i));
                                        in.putExtra("order_id",data.get("id_order_sc"));
                                        startActivity(in);
                                    }
                                });
                            }else{
                                listmenu2.setSelected(false);
                            }


                            listmenu2.invalidateViews();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, REQUEST_TAG);
    }

    private void submitUpdate(String status,String reason) {

        final String REQUEST_TAG = "get request news";

        reason = reason.replace(" ","%20");

        final ProgressDialog pd = new ProgressDialog(ComplaintDetailActivity.this);
        pd.setMessage("Updating data..");
        pd.show();
       // String idUser = session.getId();
        String idOrder = data.get("order_sc_number");
        String role = session.getRoleId();
        String idUser = session.getId();

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scOrderUpdate/"+idUser+"/"+status+"/"+role+"/"+idOrder+"/"+reason,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            pd.dismiss();
                            if(status.equals("1") || status.equals("2")){
                                Toast.makeText(ComplaintDetailActivity.this,"Data berhasil di update",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(ComplaintDetailActivity.this,"Data gagal di update",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(ComplaintDetailActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Toast.makeText(ComplaintDetailActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(ComplaintDetailActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }
}
