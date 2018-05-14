package id.co.gsd.mybirawa.controller.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import id.co.gsd.mybirawa.adapter.PunchAdapter;
import id.co.gsd.mybirawa.controller.punchlist.PunchlistDetailActivity;
import id.co.gsd.mybirawa.model.ModelPunchlist;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PunchlistFragment extends Fragment {

    private LinearLayout lay_yes;
    private LinearLayout lay_no;
    private ListView listView;
    private ProgressBar progressBar;
    private ModelPunchlist model;
    private List<ModelPunchlist> listModel;
    private SessionManager session;
    private PunchAdapter adapter;
    private String unitID, roleID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_punchlist, container, false);
        lay_yes = view.findViewById(R.id.lay_yes_punch);
        lay_no = view.findViewById(R.id.lay_no_punch);
        listView = view.findViewById(R.id.listView_punch);
        progressBar = view.findViewById(R.id.progressBarPunch);
        progressBar.setVisibility(View.GONE);

        session = new SessionManager(getActivity());
        unitID = session.getIdUnit();
        roleID = session.getRoleId();

        getData(unitID, roleID);
        return view;
    }

    //GET DATA
    private void getData(String unit, String role) {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.PUNCHLIST + unit + "/" + role,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.PUNCHLIST.TAG_TITLE);
                            listModel = new ArrayList<ModelPunchlist>();

                            if (jsonArray.length() > 0) {
                                lay_yes.setVisibility(View.VISIBLE);
                                lay_no.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.PUNCHLIST.TAG_ID_PUNCHLIST);
                                    String number = object.getString(ConstantUtils.PUNCHLIST.TAG_NO_PUNCHLIST);
                                    String keluhan = object.getString(ConstantUtils.PUNCHLIST.TAG_KELUHAN);
                                    String idP = object.getString(ConstantUtils.PUNCHLIST.TAG_ID_PERANGKAT);
                                    String perangkat = object.getString(ConstantUtils.PUNCHLIST.TAG_NAMA_PERANGKAT);
                                    String gedung = object.getString(ConstantUtils.PUNCHLIST.TAG_NAMA_GEDUNG);
                                    String lantai = object.getString(ConstantUtils.PUNCHLIST.TAG_NAMA_LANTAI);
                                    String order = object.getString(ConstantUtils.PUNCHLIST.TAG_ORDER_DATE);
                                    String status = object.getString(ConstantUtils.PUNCHLIST.TAG_STATUS);
                                    String img1 = object.getString(ConstantUtils.PUNCHLIST.TAG_FOTO_1);
                                    String img2 = object.getString(ConstantUtils.PUNCHLIST.TAG_FOTO_2);
                                    String desc = object.getString(ConstantUtils.PUNCHLIST.TAG_DESC);

                                    model = new ModelPunchlist(id, number, keluhan, idP, perangkat, gedung, lantai, order, status, img1, img2, desc);
                                    listModel.add(model);
                                }

                                adapter = new PunchAdapter(getActivity(), listModel);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(getActivity(), PunchlistDetailActivity.class);
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_ID_PUNCHLIST, listModel.get(i).getPunchID());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_NO_PUNCHLIST, listModel.get(i).getPunchNo());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_NAMA_GEDUNG, listModel.get(i).getGedung_name());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_NAMA_LANTAI, listModel.get(i).getLantai_name());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_NAMA_PERANGKAT, listModel.get(i).getPerangkat_name());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_KELUHAN, listModel.get(i).getPunchKeluhan());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_ORDER_DATE, listModel.get(i).getOrder_date());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_STATUS, listModel.get(i).getStatus());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_FOTO_1, listModel.get(i).getImg1());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_FOTO_2, listModel.get(i).getImg2());
                                        intent.putExtra(ConstantUtils.PUNCHLIST.TAG_DESC, listModel.get(i).getDesc());
                                        startActivity(intent);
                                    }
                                });
                                progressBar.setVisibility(View.GONE);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                lay_yes.setVisibility(View.GONE);
                                lay_no.setVisibility(View.VISIBLE);
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
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(unitID, roleID);
    }
}