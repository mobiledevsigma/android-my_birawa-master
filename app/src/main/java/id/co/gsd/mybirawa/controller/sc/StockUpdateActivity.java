package id.co.gsd.mybirawa.controller.sc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class StockUpdateActivity extends AppCompatActivity {


    SessionManager session;
    HashMap<String,String> data;
    EditText minStock;
    ProgressBar pbar;

    NonScrollListView listmenu;

    ArrayList<HashMap<String,String>> listData;
    AdapterStockLog adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_update);

        EditText namaBarang = findViewById(R.id.namaBarang);
        EditText jmlStock = findViewById(R.id.stokNow);
        minStock = findViewById(R.id.stokMin);
        Button update = findViewById(R.id.updateButton);

        data = (HashMap<String,String>)getIntent().getSerializableExtra("data");

        pbar = findViewById(R.id.pb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(data.get("nama_perangkat_sc"));

        listmenu = findViewById(R.id.logList);


        namaBarang.setText(data.get("nama_perangkat_sc"));
        namaBarang.setEnabled(false);
        jmlStock.setText(data.get("qty_stock"));
        jmlStock.setEnabled(false);
        minStock.setText("0");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minstok = minStock.getText().toString();
                if(minstok.equals("") || minstok.equals("0")){
                    Toast.makeText(StockUpdateActivity.this,"Stok pengurangan belum diisi",Toast.LENGTH_SHORT).show();
                }else{
                    if((Integer.parseInt(data.get("qty_stock")) - Integer.parseInt(minstok))<0){
                        Toast.makeText(StockUpdateActivity.this,"Stok tidak dapat dikurangi",Toast.LENGTH_SHORT).show();
                    }else{
                        submitUpdate(minstok);
                    }

                }
            }
        });

        showLogStock(data.get("id_stock_sc"));

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void submitUpdate(String pengurangan) {

        final String REQUEST_TAG = "get request news";

        final ProgressDialog pd = new ProgressDialog(StockUpdateActivity.this);
        pd.setMessage("Updating data..");
        pd.show();
        String idStock = data.get("id_stock_sc");
        String qtyStok = data.get("qty_stock");

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scUpdateStock/"+idStock+"/"+qtyStok+"/"+pengurangan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            pd.dismiss();
                            if(status.equals("1") || status.equals("2")){
                                Toast.makeText(StockUpdateActivity.this,"Data berhasil di update",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(StockUpdateActivity.this,"Data gagal di update",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(StockUpdateActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Toast.makeText(StockUpdateActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(StockUpdateActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }


    private void showLogStock(String idStock) {

        final String REQUEST_TAG = "get request news";


        pbar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scStockLog/"+idStock,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_stock_log");
                            listData = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id_log_stock_sc",object.getString("id_log_stock_sc")) ;
                                temp.put("qty_stock",object.getString("qty_stock")) ;
                                temp.put("updated_date",object.getString("updated_date")) ;
                                temp.put("id_stock_sc",object.getString("id_stock_sc")) ;
                                temp.put("qty_kurang",object.getString("qty_kurang")) ;
                                temp.put("qty_tambah",object.getString("qty_tambah")) ;
                                temp.put("types",object.getString("types")) ;
                                temp.put("nama_perangkat_sc",object.getString("nama_perangkat_sc")) ;
                                listData.add(temp);
                            }

                            adapter = new AdapterStockLog(StockUpdateActivity.this, listData);
                            listmenu.setAdapter(adapter);

                            listmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent it = new Intent(StockUpdateActivity.this, StockUpdateActivity.class);
                                    it.putExtra("data",listData.get(i));
                                    startActivity(it);
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
                        Toast.makeText(StockUpdateActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(StockUpdateActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }
}
