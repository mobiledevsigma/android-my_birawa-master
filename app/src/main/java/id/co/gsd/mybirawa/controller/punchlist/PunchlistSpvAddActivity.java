package id.co.gsd.mybirawa.controller.punchlist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * Created by Biting on 1/25/2018.
 */

public class PunchlistSpvAddActivity extends AppCompatActivity {

    private SessionManager session;
    private ArrayList<String> listRole = new ArrayList<>();
    private ArrayList<String> listRoleID = new ArrayList<>();
    private ArrayList<String> listGedung = new ArrayList<>();
    private ArrayList<String> listGedungID = new ArrayList<>();
    private ArrayList<String> listLantai = new ArrayList<>();
    private ArrayList<String> listLantaiID = new ArrayList<>();
    private ArrayList<String> listPerangkat = new ArrayList<>();
    private ArrayList<String> listPerangkatID = new ArrayList<>();
    private String lokasiID, roleID, gedungID, lantaiID, perangkatID;
    private String userid, unitid;
    private SpinnerDialog spinRole, spinGedung, spinLantai, spinPerangkat;
    private LinearLayout lay_role, lay_gedung, lay_lantai, lay_perangkat;
    private TextView txt_role, txt_gedung, txt_lantai, txt_perangkat;
    private EditText editText, editNumber;
    private Button button;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punchlist_spv_input);
        lay_role = findViewById(R.id.lay_choose_role);
        lay_gedung = findViewById(R.id.lay_choose_gedung);
        lay_lantai = findViewById(R.id.lay_choose_lantai);
        lay_perangkat = findViewById(R.id.lay_choose_perangkat);
        txt_role = findViewById(R.id.txt_spv_role);
        txt_gedung = findViewById(R.id.txt_spv_gedung);
        txt_lantai = findViewById(R.id.txt_spv_lantai);
        txt_perangkat = findViewById(R.id.txt_spv_perangkat);
        editText = findViewById(R.id.edit_desc_spv);
        editNumber = findViewById(R.id.edit_number_pl);
        button = findViewById(R.id.btn_add_punchlist);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        session = new SessionManager(PunchlistSpvAddActivity.this);
        userid = session.getId();
        unitid = session.getIdUnit();

        getNumber(unitid);
        getRole(unitid);
        getActionSpin();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReport();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    private void getActionSpin() {
        spinRole = new SpinnerDialog(PunchlistSpvAddActivity.this, listRole, "Pilih Role");

        lay_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinRole.showSpinerDialog();
            }
        });
        spinRole.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String role, int i) {
                txt_role.setText(role);
                roleID = listRoleID.get(i);
                getGedung(unitid, roleID);
                txt_gedung.setText("Pilih Gedung");
                txt_lantai.setText("Pilih Lantai");
                txt_perangkat.setText("Pilih Perangkat");
            }
        });

        spinGedung = new SpinnerDialog(PunchlistSpvAddActivity.this, listGedung, "Pilih Gedung");
        lay_gedung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinGedung.showSpinerDialog();
            }
        });
        spinGedung.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String gedung, int i) {
                txt_gedung.setText(gedung);
                gedungID = listGedungID.get(i);
                getLantai(unitid, gedungID, roleID);
            }
        });

        spinLantai = new SpinnerDialog(PunchlistSpvAddActivity.this, listLantai, "Pilih Lantai");
        lay_lantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinLantai.showSpinerDialog();
            }
        });
        spinLantai.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String role, int i) {
                txt_lantai.setText(role);
                lantaiID = listLantaiID.get(i);
                getPerangkat(unitid, gedungID, lantaiID, roleID);
            }
        });

        spinPerangkat = new SpinnerDialog(PunchlistSpvAddActivity.this, listPerangkat, "Pilih Perangkat");
        lay_perangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinPerangkat.showSpinerDialog();
            }
        });
        spinPerangkat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String role, int i) {
                txt_perangkat.setText(role);
                perangkatID = listPerangkatID.get(i);
            }
        });
    }

    private void getNumber(String unit) {
        final String REQUEST_TAG = "get request role";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_NUMBER + unit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String number = jsonObject.getString("number");
                            editNumber.setText(number);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PunchlistSpvAddActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(PunchlistSpvAddActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    //GET DATA
    private void getRole(String unit) {
        final String REQUEST_TAG = "get request role";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_ROLE + unit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.ROLE.TAG_TITLE);
                            listRoleID.clear();
                            listRole.clear();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.ROLE.TAG_ID);
                                    String nama = object.getString(ConstantUtils.ROLE.TAG_NAME);

                                    listRoleID.add(id);
                                    listRole.add(nama);
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                txt_role.setText("Pilih Role");
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
                        Toast.makeText(PunchlistSpvAddActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(PunchlistSpvAddActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    private void getGedung(String unit, String role) {
        final String REQUEST_TAG = "get request role";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_GEDUNG + unit + "/" + role,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.GEDUNG.TAG_TITLE);
                            listGedungID.clear();
                            listGedung.clear();
                            gedungID = "";

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.GEDUNG.TAG_ID);
                                    String nama = object.getString(ConstantUtils.GEDUNG.TAG_NAME);

                                    listGedungID.add(id);
                                    listGedung.add(nama);
                                }
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                listGedungID.clear();
                                listGedung.clear();
                                gedungID = "";
                                txt_gedung.setText("Pilih Gedung");
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
                        Toast.makeText(PunchlistSpvAddActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(PunchlistSpvAddActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    private void getLantai(String unit, String gedung, String role) {
        final String REQUEST_TAG = "get request gedung";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_LANTAI + unit + "/" + gedung + "/" + role,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.LANTAI.TAG_TITLE);
                            listLantaiID.clear();
                            listLantai.clear();
                            lantaiID = "";

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.LANTAI.TAG_ID);
                                    String nama = object.getString(ConstantUtils.LANTAI.TAG_NAME);

                                    listLantaiID.add(id);
                                    listLantai.add(nama);
                                }
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                listLantaiID.clear();
                                listLantai.clear();
                                lantaiID = "";
                                txt_lantai.setText("Pilih Lantai");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PunchlistSpvAddActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(PunchlistSpvAddActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    private void getPerangkat(String unit, String gedung, String lantai, String role) {
        final String REQUEST_TAG = "get request perangkat";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.GET_PERANGKAT + unit + "/" + gedung + "/" + lantai + "/" + role,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.PERANGKAT.TAG_TITLE);
                            listPerangkatID.clear();
                            listPerangkat.clear();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.PERANGKAT.TAG_ID);
                                    String nama = object.getString(ConstantUtils.PERANGKAT.TAG_NAME);
                                    String lokasi = object.getString(ConstantUtils.PERANGKAT.TAG_CODE);

                                    listPerangkatID.add(id);
                                    listPerangkat.add(nama);
                                }
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                listPerangkatID.clear();
                                listPerangkat.clear();
                                txt_perangkat.setText("Pilih Perangkat");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PunchlistSpvAddActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(PunchlistSpvAddActivity.this).addToRequestQueue(request, REQUEST_TAG);
    }

    private void submitReport() {

        final ProgressDialog progressDialog = new ProgressDialog(PunchlistSpvAddActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        try {
            final String REQUEST_TAG = "post request send punchlist";
            JSONObject jsonData = null;

            jsonData = new JSONObject();
            jsonData.put(ConstantUtils.ADD_PUNCH.TAG_NO, editNumber.getText());
            jsonData.put(ConstantUtils.ADD_PUNCH.TAG_ID, perangkatID);
            jsonData.put(ConstantUtils.ADD_PUNCH.TAG_KELUHAN, editText.getText());
            jsonData.put(ConstantUtils.ADD_PUNCH.TAG_UNIT, unitid);
            jsonData.put(ConstantUtils.ADD_PUNCH.TAG_ROLE, roleID);

            final String jsonScript = jsonData.toString();
            System.out.println("json add_punch " + jsonScript);

            final StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.URL.ADD_PUNCHLIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                System.out.println("kirim " + response);
                                if (obj.getString("code").equals("T")) {
                                    Toast.makeText(PunchlistSpvAddActivity.this, "Data Send", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(PunchlistSpvAddActivity.this, "Data not send", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            System.out.println(error);
                            Toast.makeText(PunchlistSpvAddActivity.this, "Data not send", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonScript == null ? null : jsonScript.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonScript, "utf-8");
                        progressDialog.dismiss();
                        return null;
                    }
                }
            };
            // Adding JsonObject request to request queue
            AppSingleton.getInstance(PunchlistSpvAddActivity.this).addToRequestQueue(request, REQUEST_TAG);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }
}
