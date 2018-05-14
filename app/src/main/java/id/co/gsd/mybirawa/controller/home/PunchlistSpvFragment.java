package id.co.gsd.mybirawa.controller.home;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import id.co.gsd.mybirawa.adapter.PunchSpvAdapter;
import id.co.gsd.mybirawa.controller.punchlist.PunchlistSpvAddActivity;
import id.co.gsd.mybirawa.controller.punchlist.PunchlistSpvDetailActivity;
import id.co.gsd.mybirawa.model.ModelPunchlistSpv;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * Created by Biting on 1/26/2018.
 */

public class PunchlistSpvFragment extends Fragment {

    private ProgressBar progressBar;
    private Spinner spinner;
    private ListView listView;
    private FloatingActionButton fab;
    private ModelPunchlistSpv model;
    private List<ModelPunchlistSpv> listModel;
    private PunchSpvAdapter adapter;
    private SessionManager session;
    private LinearLayout layout;
    private List<String> listStatus;
    private int status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_punchlist_spv, null);
        listView = view.findViewById(R.id.lv_punch_spv);
        spinner = view.findViewById(R.id.spinner_status);
        fab = view.findViewById(R.id.fab_punch_spv);
        layout = view.findViewById(R.id.lay_no_punch);
        progressBar = view.findViewById(R.id.progressBarPunch);
        progressBar.setVisibility(View.GONE);

        session = new SessionManager(getActivity());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PunchlistSpvAddActivity.class);
                startActivity(intent);
            }
        });

        listStatus = new ArrayList<String>();
        listStatus.add("Semua status");
        listStatus.add("Belum dikerjakan");
        listStatus.add("Selesai");
        listStatus.add("Tertunda");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listStatus);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = i - 1;
                if (status == -1) {
                    getData(session.getIdUnit());
                } else {
                    getDataClause(session.getIdUnit(), status);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getData(session.getIdUnit());
        return view;
    }

    //GET DATA
    private void getData(String unit) {
        final String REQUEST_TAG = "get request role";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_PUNCH_SPV + unit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.PUNCH_SPV.TAG_TITLE);
                            listModel = new ArrayList<ModelPunchlistSpv>();

                            if (jsonArray.length() > 0) {
                                listView.setVisibility(View.VISIBLE);
                                layout.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST);
                                    String no = object.getString(ConstantUtils.PUNCH_SPV.TAG_NO_PUNCHLIST);
                                    String role = object.getString(ConstantUtils.PUNCH_SPV.TAG_ROLE);
                                    String gedung = object.getString(ConstantUtils.PUNCH_SPV.TAG_NAMA_GEDUNG);
                                    String lantai = object.getString(ConstantUtils.PUNCH_SPV.TAG_NAMA_LANTAI);
                                    String item = object.getString(ConstantUtils.PUNCH_SPV.TAG_NAMA_PERANGKAT);
                                    String keluhan = object.getString(ConstantUtils.PUNCH_SPV.TAG_KELUHAN);
                                    String status = object.getString(ConstantUtils.PUNCH_SPV.TAG_STATUS);
                                    String img1 = object.getString(ConstantUtils.PUNCH_SPV.TAG_IMAGE_1);
                                    String img2 = object.getString(ConstantUtils.PUNCH_SPV.TAG_IMAGE_2);
                                    String desc = object.getString(ConstantUtils.PUNCH_SPV.TAG_DESC);
                                    String oDate = object.getString(ConstantUtils.PUNCH_SPV.TAG_ORDER);
                                    String sDate = object.getString(ConstantUtils.PUNCH_SPV.TAG_SUBMIT);

                                    model = new ModelPunchlistSpv(id, no, role, gedung, lantai, item, keluhan, status, img1, img2, desc, oDate, sDate);
                                    listModel.add(model);
                                }
                                adapter = new PunchSpvAdapter(getActivity(), listModel);
                                listView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(getActivity(), PunchlistSpvDetailActivity.class);
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST, listModel.get(i).getPunchID());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NO_PUNCHLIST, listModel.get(i).getPunchNo());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_ROLE, listModel.get(i).getRoleName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_GEDUNG, listModel.get(i).getGedungName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_LANTAI, listModel.get(i).getLantaiName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_PERANGKAT, listModel.get(i).getItemName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_KELUHAN, listModel.get(i).getKeluhan());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_STATUS, listModel.get(i).getStatus());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_IMAGE_1, listModel.get(i).getImg1());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_IMAGE_2, listModel.get(i).getImg2());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_DESC, listModel.get(i).getDesc());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_ORDER, listModel.get(i).getOrderDate());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_SUBMIT, listModel.get(i).getSubmitdate());
                                        startActivity(intent);
                                    }
                                });

                            } else {
                                progressBar.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
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

    //GET DATA
    private void getDataClause(String unit, final int status) {
        final String REQUEST_TAG = "get request role";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_PUNCH_SPV_CLAUSE + unit + "/" + status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.PUNCH_SPV.TAG_TITLE);
                            listModel = new ArrayList<ModelPunchlistSpv>();

                            if (jsonArray.length() > 0) {
                                listView.setVisibility(View.VISIBLE);
                                layout.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST);
                                    String no = object.getString(ConstantUtils.PUNCH_SPV.TAG_NO_PUNCHLIST);
                                    String role = object.getString(ConstantUtils.PUNCH_SPV.TAG_ROLE);
                                    String gedung = object.getString(ConstantUtils.PUNCH_SPV.TAG_NAMA_GEDUNG);
                                    String lantai = object.getString(ConstantUtils.PUNCH_SPV.TAG_NAMA_LANTAI);
                                    String item = object.getString(ConstantUtils.PUNCH_SPV.TAG_NAMA_PERANGKAT);
                                    String keluhan = object.getString(ConstantUtils.PUNCH_SPV.TAG_KELUHAN);
                                    String status = object.getString(ConstantUtils.PUNCH_SPV.TAG_STATUS);
                                    String img1 = object.getString(ConstantUtils.PUNCH_SPV.TAG_IMAGE_1);
                                    String img2 = object.getString(ConstantUtils.PUNCH_SPV.TAG_IMAGE_2);
                                    String desc = object.getString(ConstantUtils.PUNCH_SPV.TAG_DESC);
                                    String oDate = object.getString(ConstantUtils.PUNCH_SPV.TAG_ORDER);
                                    String sDate = object.getString(ConstantUtils.PUNCH_SPV.TAG_SUBMIT);

                                    model = new ModelPunchlistSpv(id, no, role, gedung, lantai, item, keluhan, status, img1, img2, desc, oDate, sDate);
                                    listModel.add(model);
                                }
                                adapter = new PunchSpvAdapter(getActivity(), listModel);
                                listView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(getActivity(), PunchlistSpvDetailActivity.class);
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST, listModel.get(i).getPunchID());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NO_PUNCHLIST, listModel.get(i).getPunchNo());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_ROLE, listModel.get(i).getRoleName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_GEDUNG, listModel.get(i).getGedungName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_LANTAI, listModel.get(i).getLantaiName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_PERANGKAT, listModel.get(i).getItemName());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_KELUHAN, listModel.get(i).getKeluhan());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_STATUS, listModel.get(i).getStatus());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_IMAGE_1, listModel.get(i).getImg1());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_IMAGE_2, listModel.get(i).getImg2());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_DESC, listModel.get(i).getDesc());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_ORDER, listModel.get(i).getOrderDate());
                                        intent.putExtra(ConstantUtils.PUNCH_SPV.TAG_SUBMIT, listModel.get(i).getSubmitdate());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
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
        getData(session.getIdUnit());
    }
}