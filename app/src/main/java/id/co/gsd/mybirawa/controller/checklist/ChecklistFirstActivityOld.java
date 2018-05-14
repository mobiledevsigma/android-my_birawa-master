package id.co.gsd.mybirawa.controller.checklist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.model.ModelBuilding;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * Created by Biting on 1/8/2018.
 */

public class ChecklistFirstActivityOld extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textToolbar;
    private ChecklistFirstTabParentFragment fragmentParent;
    private ProgressBar progressBar;
    private ModelBuilding model;
    private List<ModelBuilding> listModel;
    private List<String> listName;
    private String unitID, roleID, periodID, selisih;
    private int percent;
    private ProgressDialog dialog;
    Integer itung = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_first_old);
        toolbar = findViewById(R.id.toolbar);
        textToolbar = findViewById(R.id.tv_toolbar_checklist);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        getIDs();

        listModel = new ArrayList<ModelBuilding>();

        Intent intent = getIntent();
        unitID = intent.getStringExtra(ConstantUtils.USER_DATA.TAG_UNIT_ID);
        roleID = intent.getStringExtra(ConstantUtils.USER_DATA.TAG_ROLE_ID);
        periodID = intent.getStringExtra(ConstantUtils.PERIOD.TAG_ID);
        percent = intent.getIntExtra("percent", 0);
        selisih = intent.getStringExtra("selisih");
        String judul = intent.getStringExtra("judul");

        toolbar.setTitle("");
        textToolbar.setText(judul);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getData(unitID, roleID, periodID, selisih);
//        new getBuilding().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(unitID, roleID, periodID, selisih);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getIDs() {
        fragmentParent = (ChecklistFirstTabParentFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragmentParent);
    }

    //GET DATA
    private void getData(String unit, String role, String period, String hari) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.BUILDING + unit + "/" + role + "/" + period + "/" + hari,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE);
                            listModel = new ArrayList<ModelBuilding>();
                            listName = new ArrayList<String>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String Gid = object.getString(ConstantUtils.BUILDING.TAG_BUILDING_ID);
                                    String nama = object.getString(ConstantUtils.BUILDING.TAG_NAME);
                                    String alamat = object.getString(ConstantUtils.BUILDING.TAG_ADDRESS);
                                    String latit = object.getString(ConstantUtils.BUILDING.TAG_LAT);
                                    String longi = object.getString(ConstantUtils.BUILDING.TAG_LONG);

                                    if (listName.size() <= 10) {
                                        listName.add(nama);
                                    }

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.BUILDING.TAG_TITLE2);
                                    for (int a = 0; a < jsonArray1.length(); a++) {
                                        JSONObject obj = jsonArray1.getJSONObject(a);
                                        String Lid = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_ID);
                                        String Lname = obj.getString(ConstantUtils.BUILDING.TAG_FLOOR_NAME);

                                        model = new ModelBuilding(Gid, nama, alamat, latit, longi, Lid, Lname);
                                        listModel.add(model);
                                    }
                                }

                                if (itung == 0) {
                                    for (int a = 0; a < listName.size(); a++) {
                                        if (!listName.get(a).equals("")) {
                                            fragmentParent.addPage(listName.get(a));
                                            itung = itung + 1;
                                        } else {
                                            Toast.makeText(ChecklistFirstActivityOld.this, "Page name is empty", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ChecklistFirstActivityOld.this, "no data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChecklistFirstActivityOld.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        System.out.println("building " + request);
        AppSingleton.getInstance(ChecklistFirstActivityOld.this).addToRequestQueue(request, REQUEST_TAG);
    }
}
