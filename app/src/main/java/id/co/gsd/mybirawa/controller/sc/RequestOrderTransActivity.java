package id.co.gsd.mybirawa.controller.sc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class RequestOrderTransActivity extends AppCompatActivity {

    SessionManager session;
    HashMap<String,String> data;
    EditText minStock,qty;
    String orderId;
    ProgressBar pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_order_trans);

        session = new SessionManager(getApplicationContext());
        EditText namaBarang = findViewById(R.id.namaBarang);
        EditText jmlStock = findViewById(R.id.stokNow);
         qty = findViewById(R.id.stokApprove);
        minStock = findViewById(R.id.stokMin);
        Button update = findViewById(R.id.updateButton);

        data = (HashMap<String,String>)getIntent().getSerializableExtra("data");
        orderId = getIntent().getStringExtra("order_id");
        pbar = findViewById(R.id.pb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(data.get("nama_perangkat_sc"));

        namaBarang.setText(data.get("nama_perangkat_sc"));
        namaBarang.setEnabled(false);
        jmlStock.setText(data.get("qty_perangkat_sc"));
        jmlStock.setEnabled(false);
        minStock.setText(data.get("qty_request_sc"));
        minStock.setEnabled(false);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(RequestOrderTransActivity.this)
                        .setMessage("Update Request order ini ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String minstok = qty.getText().toString();
                                if(minstok.equals("") || minstok.equals("0")){
                                    Toast.makeText(RequestOrderTransActivity.this,"Stok pengurangan belum diisi",Toast.LENGTH_SHORT).show();
                                }else{
                                    submitUpdate(minstok);
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

    }

    private void submitUpdate(String appr) {

        final String REQUEST_TAG = "get request news";

        final ProgressDialog pd = new ProgressDialog(RequestOrderTransActivity.this);
        pd.setMessage("Updating data..");
        pd.show();
        String idTrans = data.get("id_transaksi");
        String qtyStok = data.get("qty_request_sc");

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scTransUpdate/"+session.getId()+"/"+idTrans+"/"+orderId+"/"+appr+"/"+qtyStok,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            pd.dismiss();
                            if(status.equals("1") || status.equals("2")){
                                Toast.makeText(RequestOrderTransActivity.this,"Data berhasil di update",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(RequestOrderTransActivity.this,"Data gagal di update",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(RequestOrderTransActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Toast.makeText(RequestOrderTransActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println(request);
        AppSingleton.getInstance(RequestOrderTransActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
