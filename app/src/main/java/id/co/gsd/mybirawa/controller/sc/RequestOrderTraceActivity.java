package id.co.gsd.mybirawa.controller.sc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class RequestOrderTraceActivity extends AppCompatActivity {

    ListView list;
    ArrayList<HashMap<String,String>> listData;
    AdapterOrderTrace adapter;
    ProgressBar pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_trace);

        list = findViewById(R.id.list);
        pbar = findViewById(R.id.pb);

        String idOrder = getIntent().getStringExtra("orderId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(idOrder);

        boolean isComplaint = getIntent().getBooleanExtra("is_complaint",false);
        if(isComplaint){
            showComplaintTrace(idOrder);
        }else{
            showOrderTrace(idOrder);
        }

    }

    private void showOrderTrace(String id) {

        final String REQUEST_TAG = "get request news";

        pbar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scOrderTrace/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_order_trace");
                            listData = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id_log",object.getString("id_log")) ;
                                temp.put("order_sc_number",object.getString("order_sc_number")) ;
                                temp.put("updated_by",object.getString("updated_by")) ;
                                temp.put("updated_date",object.getString("updated_date")) ;
                                temp.put("status_order",object.getString("status_order")) ;
                                temp.put("user_name",object.getString("user_name")) ;
                                temp.put("status_name",object.getString("status_name")) ;
                                listData.add(temp);
                            }

                            adapter = new AdapterOrderTrace(RequestOrderTraceActivity.this, listData);
                            list.setAdapter(adapter);

//                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    Intent it = new Intent(RequestOrderTraceActivity.this, StockUpdateActivity.class);
//                                    it.putExtra("data",listData.get(i));
//                                    startActivity(it);
//                                }
//                            });
                            list.invalidateViews();

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
                        Toast.makeText(RequestOrderTraceActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(RequestOrderTraceActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }


    private void showComplaintTrace(String id) {

        final String REQUEST_TAG = "get request news";


        pbar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scComplaintTrace/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_order_trace");
                            listData = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id_log",object.getString("id_log")) ;
                                temp.put("order_sc_number",object.getString("order_sc_number")) ;
                                temp.put("updated_by",object.getString("updated_by")) ;
                                temp.put("updated_date",object.getString("updated_date")) ;
                                temp.put("status_order",object.getString("status_order")) ;
                                temp.put("user_name",object.getString("user_name")) ;
                                temp.put("status_name",object.getString("status_name")) ;
                                listData.add(temp);
                            }

                            adapter = new AdapterOrderTrace(RequestOrderTraceActivity.this, listData);
                            list.setAdapter(adapter);

//                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    Intent it = new Intent(RequestOrderTraceActivity.this, StockUpdateActivity.class);
//                                    it.putExtra("data",listData.get(i));
//                                    startActivity(it);
//                                }
//                            });
                            list.invalidateViews();

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
                        Toast.makeText(RequestOrderTraceActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(RequestOrderTraceActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
