package id.co.gsd.mybirawa.controller.checklist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.ChecklistInputAdapter;
import id.co.gsd.mybirawa.adapter.TimeAdapter;
import id.co.gsd.mybirawa.model.ModelChecklistInput;
import id.co.gsd.mybirawa.model.ModelTime;
import id.co.gsd.mybirawa.util.CameraManager;
import id.co.gsd.mybirawa.util.CustomSessionManager;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;


public class ChecklistSecondTabChildFragment extends Fragment {

    private String idPerangkatTab;
    private String batas_bawah, batas_atas;
    private SessionManager session;
    private ListView listView;
    private LinearLayout lay_no_data;
    private LinearLayout lay_checklist;
    private ProgressBar progressBar;
    private CameraManager camMan;
    private String idForCamera;
    private ModelChecklistInput model;
    private List<ModelChecklistInput> listModel;
    private CustomSessionManager dataSess;
    private String deviceTypeId;
    private String unitID, roleID, idPeriod, selisih, judul;
    private int percent;
    private ChecklistInputAdapter adapter;
    private LinearLayout lay_time;
    private RecyclerView listView_time;
    private ArrayList<String> listTime = new ArrayList<String>();
    private ArrayList<ModelTime> listTime5 = new ArrayList<ModelTime>();
    private TimeAdapter timeAdapter;
    private int itung = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listTime5.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checklist_tab_child_second, container, false);

        String a = getArguments().getString(ConstantUtils.TAB_INTENT.TAG_PAGENAME);
        idPerangkatTab = getArguments().getString(ConstantUtils.TAB_INTENT.TAG_DEVICEID);
        //System.out.println("coba liat " + idPerangkatTab);

        setHasOptionsMenu(true);

        session = new SessionManager(getActivity());
        camMan = new CameraManager();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = getActivity().getIntent();
        deviceTypeId = intent.getStringExtra(ConstantUtils.DEVICE.TAG_PJ_ID);
        idPeriod = intent.getStringExtra(ConstantUtils.PERIOD.TAG_ID);

        unitID = intent.getStringExtra("unit");
        roleID = intent.getStringExtra("role");
        selisih = intent.getStringExtra("selisih");
        percent = intent.getIntExtra("percent", 0);
        judul = intent.getStringExtra("judul");

        dataSess = new CustomSessionManager(getActivity(), "checklistInput" + idPerangkatTab);

        lay_no_data = view.findViewById(R.id.lay_no_data);
        lay_checklist = view.findViewById(R.id.lay_checklist);
        listView = view.findViewById(R.id.list_checklist_input);
        listView.setItemsCanFocus(true);
        progressBar = view.findViewById(R.id.checklistInputProgress);
        progressBar.setVisibility(View.GONE);

        lay_time = view.findViewById(R.id.lay_time);
        listView_time = view.findViewById(R.id.listView_time);

        lay_time.setVisibility(View.GONE);
        if (deviceTypeId.equals("8") || deviceTypeId.equals("9") || deviceTypeId.equals("22") || deviceTypeId.equals("23") || deviceTypeId.equals("24")
                || deviceTypeId.equals("26") || deviceTypeId.equals("27") || deviceTypeId.equals("30") || deviceTypeId.equals("35") || deviceTypeId.equals("36")) {
            lay_time.setVisibility(View.VISIBLE);
            if (itung == 0) {
                listTime.add("08.00");
                listTime.add("10.00");
                listTime.add("12.00");
                listTime.add("14.00");
                listTime.add("16.00");
                listTime.add("17.00");

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH");
                String time = sdf.format(cal.getTime());
                int currentTime = Integer.parseInt(time);

                for (int i = 0; i < listTime.size(); i++) {
                    String times1 = listTime.get(i).substring(0, 2);
                    int timer = Integer.parseInt(times1);
                    if (currentTime >= timer) {
                        if (i < listTime.size()) {
                            //String times2 = listTime.get(i).substring(0, 2);
                            String times2 = listTime.get(i+1).substring(0, 2);
                            int timer2 = Integer.parseInt(times2);
                            if (currentTime < timer2) {
                                batas_bawah = times1;
                                batas_atas = times2;
                                checkHK(batas_atas, batas_bawah);
                            } else {
                                System.out.println("pass2");
                            }
                        } else {
                            if (currentTime < timer + 4) {
                                System.out.println("pass3 " + currentTime);
                            } else {
                                System.out.println("pass4");
                            }
                        }
                    } else {
                        lay_no_data.setVisibility(View.VISIBLE);
                        lay_checklist.setVisibility(View.GONE);
                    }
                }

                listView_time.setHasFixedSize(true);
                LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                timeAdapter = new TimeAdapter(getActivity(), listTime);
                listView_time.setAdapter(timeAdapter);
                listView_time.setLayoutManager(MyLayoutManager);

                itung = itung + 1;
            }
        } else {
            System.out.println("yap 3");
            lay_time.setVisibility(View.GONE);
            getData();
        }

        idForCamera = "";

        Button btn = view.findViewById(R.id.submitButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listModel.size() > 0) {
                    System.out.println("checkID-2 " + idPerangkatTab);
                    int check = 0;
                    int listSize = 0;
                    for (int i = 0; i < listModel.size(); i++) {
                        String checkSwitch = dataSess.getData("adaTidak" + i + idPerangkatTab);
                        String hasilUkur = dataSess.getData("hasilUkur" + i + idPerangkatTab);
                        String isSeparator = listModel.get(i).getIsSeparator();

                        if (checkSwitch.equals("Ada")) {
                            if (!hasilUkur.equals("")) {
                                check++;
                            }

                            if (isSeparator.equals("0")) {
                                listSize++;
                            }
                        }
                    }

                    if (check == listSize) {
                        Toast.makeText(getContext(), "idPerangkat " + idPerangkatTab, Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Perhatian !!")
                                .setMessage("Apakah anda yakin akan mengirim data?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        submitReport();
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .show();
                    } else {
                        Toast.makeText(getActivity(), "Harap lengkapi semua data..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Apa nih", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void reloadData() {
        getData();
    }

    private void reloadHK() {
        checkHK(batas_atas, batas_bawah);
    }

    private void submitReport() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending data...");
        progressDialog.show();

        String userid = session.getId();
        String unitid = session.getIdUnit();

        try {
            final String REQUEST_TAG = "post request send new task";
            JSONObject jsonTitle = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonData = null;

            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.get(i).getIsSeparator().equals("0")) {
                    String kondisi = dataSess.getData("adaTidak" + i + idPerangkatTab);
                    jsonData = new JSONObject();

                    jsonData.put("id_perangkat_checklist", listModel.get(i).getChecklist_id());
                    jsonData.put("user_id", userid);
                    jsonData.put("unit_id", unitid);
                    jsonData.put("id_perangkat", idPerangkatTab);
                    jsonData.put("id_checklist_period", idPeriod);

                    if (kondisi.equals("Ada")) {
                        jsonData.put("hasil", dataSess.getData("hasilUkur" + i + idPerangkatTab));
                        jsonData.put("keterangan", dataSess.getData("hasilKeterangan" + i + idPerangkatTab));
                        jsonData.put("gambar", dataSess.getData("kamera" + i + idPerangkatTab));
                    } else {
                        jsonData.put("hasil", null);
                        jsonData.put("keterangan", "Tidak ada");
                        jsonData.put("gambar", "");
                    }
                    jsonArray.put(jsonData);
                }
            }

            //if ("aaa".)

            jsonTitle.put("input_checklist", jsonArray);
            final String jsonScript = jsonTitle.toString();

            final StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.URL.SUBMIT_CHECKLIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println(response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("code").equals("T")) {
                                    Toast.makeText(getActivity(), "Data Terkirim", Toast.LENGTH_LONG).show();
                                    dataSess.destroySession();
                                    Intent intent = new Intent(getActivity(), ChecklistFirstActivity.class);
                                    intent.putExtra("unit_id", unitID);
                                    intent.putExtra("role_id", roleID);
                                    intent.putExtra(ConstantUtils.PERIOD.TAG_ID, idPeriod);
                                    intent.putExtra("selisih", selisih);
                                    intent.putExtra("percent", percent);
                                    intent.putExtra("judul", judul);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "Data gagal terkirim", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity(), "Data not send", Toast.LENGTH_LONG).show();
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
            AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void checkHK(String atas, String bawah) {

        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.CHECK_INPUT_HK + idPerangkatTab + "/" + idPeriod
                + "/" + atas + "/" + bawah,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reloadHK();
                            }

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            progressBar.setVisibility(View.GONE);

                            if (status.equals("open")) {
                                getData();
                                lay_no_data.setVisibility(View.GONE);
                                lay_checklist.setVisibility(View.VISIBLE);
                            } else {
                                lay_no_data.setVisibility(View.VISIBLE);
                                lay_checklist.setVisibility(View.GONE);
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

    private void getData() {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.CHECKLIST + deviceTypeId + "/" + idPeriod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.substring(0, 9).equals("<!DOCTYPE")) {
                                reloadData();
                            }

                            System.out.println("checklist " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.CHECKLIST.TAG_TITLE);
                            progressBar.setVisibility(View.GONE);
                            listModel = new ArrayList<ModelChecklistInput>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String idDesc = object.getString(ConstantUtils.CHECKLIST.TAG_ID_DESC);
                                    String Desc = object.getString(ConstantUtils.CHECKLIST.TAG_NAME_DESC);
                                    model = new ModelChecklistInput("", "", "", "", idDesc, Desc, "1");
                                    listModel.add(model);

                                    JSONArray jsonArray1 = object.getJSONArray(ConstantUtils.CHECKLIST.TAG_TITLE2);

                                    for (int a = 0; a < jsonArray1.length(); a++) {
                                        JSONObject obj = jsonArray1.getJSONObject(a);
                                        String id = obj.getString(ConstantUtils.CHECKLIST.TAG_ID);
                                        String check = obj.getString(ConstantUtils.CHECKLIST.TAG_CHECKLIST);
                                        String std = obj.getString(ConstantUtils.CHECKLIST.TAG_STD);
                                        String tipe = obj.getString(ConstantUtils.CHECKLIST.TAG_TYPE);

                                        model = new ModelChecklistInput(id, check, std, tipe, "", "", "0");
                                        listModel.add(model);
                                    }
                                }

                                adapter = new ChecklistInputAdapter(getActivity(), listModel, dataSess, idPerangkatTab, ChecklistSecondTabChildFragment.this);
                                listView.setAdapter(adapter);

                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "no data found", Toast.LENGTH_SHORT).show();
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

    public void setCameraView(String id, int fotoId) {
        idForCamera = id;
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File f = new File(Environment.getExternalStorageDirectory(), "checklist.jpg");
                    //path = f.getAbsolutePath();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, fotoId);
                }
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File f = new File(Environment.getExternalStorageDirectory(), "checklist.jpg");
                //path = f.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, fotoId);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;
        if (resultCode == getActivity().RESULT_OK) {

            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("checklist.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                String newSizePath = camMan.compressImage(f.getAbsolutePath());
                bitmap = BitmapFactory.decodeFile(newSizePath,
                        bitmapOptions);

                Matrix matrix = new Matrix();

                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

                byte[] attachmentBytes = byteArrayOutputStream.toByteArray();
                String attachmentData = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

                dataSess.setData("kamera" + idForCamera + idPerangkatTab, attachmentData);

            } catch (Exception e) {
                e.printStackTrace();
            }

            listView.invalidateViews();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.invalidateViews();
    }
}
