package id.co.gsd.mybirawa.controller.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.NewsAdapter;
import id.co.gsd.mybirawa.model.ModelNews;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class PopupActivity extends Activity {

    private ModelNews model;
    private List<ModelNews> listModel;
    private NewsAdapter adapter;
    private SwipeMenuListView listView;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.8));

        session = new SessionManager(PopupActivity.this);
        listView = findViewById(R.id.listView_news);

        getData(session.getId());
    }

    private void getData(String userID) {

        final String REQUEST_TAG = "get request";

        final StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.NEWS + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.NEWS.TAG_TITLE);
                            //progressBar.setVisibility(View.GONE);
                            listModel = new ArrayList<ModelNews>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.NEWS.TAG_ID);
                                    String judul = object.getString(ConstantUtils.NEWS.TAG_JUDUL);
                                    String isi = object.getString(ConstantUtils.NEWS.TAG_ISI);
                                    String tgl = object.getString(ConstantUtils.NEWS.TAG_TGL);
                                    String status = object.getString(ConstantUtils.NEWS.TAG_STATUS);

                                    model = new ModelNews(id, judul, isi, tgl, status);
                                    listModel.add(model);
                                }

                                adapter = new NewsAdapter(PopupActivity.this, listModel);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(PopupActivity.this, NewsDetailActivity.class);
                                        intent.putExtra(ConstantUtils.NEWS.TAG_ID, listModel.get(i).getNews_id());
                                        intent.putExtra(ConstantUtils.NEWS.TAG_JUDUL, listModel.get(i).getNews_judul());
                                        intent.putExtra(ConstantUtils.NEWS.TAG_ISI, listModel.get(i).getNews_isi());
                                        intent.putExtra(ConstantUtils.NEWS.TAG_TGL, listModel.get(i).getNews_tgl());
                                        intent.putExtra(ConstantUtils.NEWS.TAG_STATUS, listModel.get(i).getNews_status());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                Toast.makeText(PopupActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(PopupActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println(request);
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(PopupActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }
}
