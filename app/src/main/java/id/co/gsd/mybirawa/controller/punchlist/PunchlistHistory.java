package id.co.gsd.mybirawa.controller.punchlist;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.HistoryAdapter;
import id.co.gsd.mybirawa.model.ModelHistory;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * Created by Biting on 3/5/2018.
 */

public class PunchlistHistory extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView listView;
    private ModelHistory model;
    private List<ModelHistory> listModel;
    private HistoryAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punchlist_history);

        toolbar = findViewById(R.id.toolbar_punch);
        progressBar = findViewById(R.id.progressBarPunch);
        listView = findViewById(R.id.listview_history);

//        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        getData(intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST));

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    //GET DATA
    private void getData(String idPunch) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_HISTORY + idPunch,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.PUNCHLIST.TAG_TITLE);
                            listModel = new ArrayList<ModelHistory>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String user = object.getString(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST);
                                    String tgl = object.getString(ConstantUtils.PUNCH_SPV.TAG_CREATED);
                                    String desc = object.getString(ConstantUtils.PUNCH_SPV.TAG_DESC);

                                    model = new ModelHistory(user, tgl, desc);
                                    listModel.add(model);
                                }

                                adapter = new HistoryAdapter(PunchlistHistory.this, listModel);
                                listView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                progressBar.setVisibility(View.GONE);
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
                        Toast.makeText(PunchlistHistory.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(PunchlistHistory.this).addToRequestQueue(request, REQUEST_TAG);
    }
}
