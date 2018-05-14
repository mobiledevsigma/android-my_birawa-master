package id.co.gsd.mybirawa.controller.punchlist;

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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.home.HomeActivity;
import id.co.gsd.mybirawa.util.CameraManager;
import id.co.gsd.mybirawa.util.CustomSessionManager;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * Created by Biting on 1/16/2018.
 */

public class PunchlistDetailActivity extends AppCompatActivity {

    private CustomSessionManager dataSess;
    private Toolbar toolbar;
    private TextView tvNumber, tvOrder, tvGedung, tvLantai, tvPerangkat, tvKeluhan, tvToolbar;
    private Spinner spinner;
    private ImageView img_before, img_after;
    private EditText editText;
    private Button button;
    private String idPunch, foto1, foto2, descript, theStatus;
    private int img_pos;
    private CameraManager camMan;
    private String img_1, img_2;
    private SessionManager session;
    private List<String> listStatus;
    private int status;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punchlist_detail);
        toolbar = findViewById(R.id.toolbar_punch);
        tvToolbar = findViewById(R.id.tv_toolbar_punch);
        tvNumber = findViewById(R.id.tv_det_punch_numb);
        tvOrder = findViewById(R.id.tv_det_punch_order);
        tvGedung = findViewById(R.id.tv_det_punch_gedung);
        tvLantai = findViewById(R.id.tv_det_punch_lantai);
        tvPerangkat = findViewById(R.id.tv_det_punch_perangkat);
        tvKeluhan = findViewById(R.id.tv_det_punch_keluhan);
        img_before = findViewById(R.id.img_cam_before);
        img_after = findViewById(R.id.img_cam_after);
        spinner = findViewById(R.id.spin_punch_status);
        editText = findViewById(R.id.edit_punch_desc);
        button = findViewById(R.id.btn_submit_punch);

        listStatus = new ArrayList<String>();
        listStatus.add("Pilih status");
        listStatus.add("Selesai");
        listStatus.add("Tertunda");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = getIntent();

        SimpleDateFormat defol = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateServer = null;
        try {
            dateServer = defol.parse(intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_ORDER_DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sDate = new SimpleDateFormat("dd MMMM yyyy");
        String date = sDate.format(dateServer);

        theStatus = intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_STATUS);
        idPunch = intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_ID_PUNCHLIST);
        foto1 = intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_FOTO_1);
        foto2 = intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_FOTO_2);
        descript = intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_DESC);
        tvToolbar.setText(intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_NO_PUNCHLIST));
        tvNumber.setText("No. Punchlist : " + intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_NO_PUNCHLIST));
        tvOrder.setText("Tanggal Order : " + date);
        tvGedung.setText("Nama Gedung : " + intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_NAMA_GEDUNG));
        tvLantai.setText("Nama Lantai : " + intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_NAMA_LANTAI));
        tvPerangkat.setText("Nama Perangkat : " + intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_NAMA_PERANGKAT));
        tvKeluhan.setText("Keluhan : " + intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_KELUHAN));

        img_1 = "";
        img_2 = "";

        dataSess = new CustomSessionManager(this, "punchlist" + idPunch);
        session = new SessionManager(this);
        camMan = new CameraManager();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStatus);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        cekData(theStatus, descript);

        openCamera();

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

    private void cekData(String idStatus, String desc) {
        //check data session
        if (idStatus.equals("2")) {
            byte[] decodedString1 = Base64.decode(foto1, Base64.DEFAULT);
            Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
            img_before.setImageBitmap(decodedByte1);
            String attachmentData1 = Base64.encodeToString(decodedString1, Base64.DEFAULT);
            img_1 = attachmentData1;

            byte[] decodedString2 = Base64.decode(foto2, Base64.DEFAULT);
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
            img_after.setImageBitmap(decodedByte2);
            String attachmentData2 = Base64.encodeToString(decodedString2, Base64.DEFAULT);
            img_2 = attachmentData2;

            spinner.setSelection(2);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    status = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            editText.setText(desc);

            button.setText("Update");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!img_1.trim().isEmpty() && !img_2.trim().isEmpty() && status != 0 && !editText.getText().toString().trim().isEmpty()) {
                        submitPunch();
                    } else {
                        Toast.makeText(getBaseContext(), "Harap lengkapi semua isian terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            if (dataSess.getData("kamera1" + idPunch).equals("")) {
                img_before.setImageResource(R.drawable.ic_camera);
            } else {
                byte[] decodedString = Base64.decode(dataSess.getData("kamera1" + idPunch), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img_before.setImageBitmap(decodedByte);
                String attachmentData = Base64.encodeToString(decodedString, Base64.DEFAULT);
                img_1 = attachmentData;
            }
            if (dataSess.getData("kamera2" + idPunch).equals("")) {
                img_after.setImageResource(R.drawable.ic_camera);
            } else {
                byte[] decodedString = Base64.decode(dataSess.getData("kamera2" + idPunch), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img_after.setImageBitmap(decodedByte);
                String attachmentData = Base64.encodeToString(decodedString, Base64.DEFAULT);
                img_2 = attachmentData;
            }

            final String stat = dataSess.getData("status" + idPunch);
            if (stat.equals("")) {
                spinner.setSelection(0);
            } else {
                spinner.setSelection(Integer.parseInt(stat));
                status = Integer.parseInt(stat);
            }

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    status = i;
                    dataSess.setData("status" + idPunch, String.valueOf(status));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            final String ket = dataSess.getData("desc" + idPunch);
            if (ket.equals("")) {
                editText.setText("");
            } else {
                editText.setText(ket);
            }

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    dataSess.setData("desc" + idPunch, charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            button.setText("submit");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!img_1.trim().isEmpty() && !img_2.trim().isEmpty() && status != 0 && !editText.getText().toString().trim().isEmpty()) {
                        submitPunch();
                    } else {
                        Toast.makeText(getBaseContext(), "Harap lengkapi semua isian terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void openCamera() {
        img_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCameraView(1);
            }
        });

        img_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCameraView(2);
            }
        });
    }

    public void setCameraView(int id) {

        img_pos = id;
        System.out.println("masuk kamera 2");
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File f = new File(Environment.getExternalStorageDirectory(), "punchlist.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
            } else {
                ActivityCompat.requestPermissions(PunchlistDetailActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File f = new File(Environment.getExternalStorageDirectory(), "punchlist.jpg");
                //path = f.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //final ImageView uploadArea = (ImageView) attachmentDialog.findViewById(R.id.uploadArea);
        Bitmap bitmap;
        if (resultCode == RESULT_OK) {

            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("punchlist.jpg")) {
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

                byte[] decodedString = Base64.decode(attachmentData, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (img_pos == 1) {
                    img_1 = attachmentData;
                    System.out.println("masuk nih-9 " + img_1);
                    img_before.setImageBitmap(decodedByte);
                    img_before.setMaxHeight(decodedByte.getHeight());
                    img_before.setMaxWidth(decodedByte.getWidth());
                    img_before.getLayoutParams().height = decodedByte.getHeight();
                    img_before.getLayoutParams().width = decodedByte.getWidth();
                    img_before.requestLayout();
                    dataSess.setData("kamera" + img_pos + idPunch, img_1);
                } else {
                    img_2 = attachmentData;
                    img_after.setImageBitmap(decodedByte);
                    img_after.setMaxHeight(decodedByte.getHeight());
                    img_after.setMaxWidth(decodedByte.getWidth());
                    img_after.getLayoutParams().height = decodedByte.getHeight();
                    img_after.getLayoutParams().width = decodedByte.getWidth();
                    img_after.requestLayout();
                    dataSess.setData("kamera" + img_pos + idPunch, img_2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void submitPunch() {
        final ProgressDialog progressDialog = new ProgressDialog(PunchlistDetailActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        try {
            final String REQUEST_TAG = "post request send new task";
            JSONObject jsonTitle = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonData = null;

            jsonData = new JSONObject();
            jsonData.put(ConstantUtils.PUNCHLIST.TAG_FOTO_1, img_1);
            jsonData.put(ConstantUtils.PUNCHLIST.TAG_FOTO_2, img_2);
            jsonData.put(ConstantUtils.USER_DATA.TAG_USER_ID, session.getId());
            jsonData.put(ConstantUtils.PUNCHLIST.TAG_DESC, editText.getText());
            jsonData.put(ConstantUtils.PUNCHLIST.TAG_STATUS, status);
            jsonData.put(ConstantUtils.PUNCHLIST.TAG_ID_PUNCHLIST, idPunch);
            jsonArray.put(jsonData);

            jsonTitle.put(ConstantUtils.PUNCHLIST.TAG_TITLE, jsonArray);
            final String jsonScript = jsonTitle.toString();
            System.out.println("json report " + jsonScript);

            final StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.URL.SUBMIT_PUNCHLIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println(response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("code").equals("T")) {
                                    Toast.makeText(PunchlistDetailActivity.this, "Data Send", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PunchlistDetailActivity.this, HomeActivity.class);
                                    intent.putExtra("fragmentID", 1);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(PunchlistDetailActivity.this, "Data not send", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(PunchlistDetailActivity.this, "Data not send", Toast.LENGTH_LONG).show();
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
            AppSingleton.getInstance(PunchlistDetailActivity.this).addToRequestQueue(request, REQUEST_TAG);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (theStatus.equals("2")) {
            new AlertDialog.Builder(this)
                    .setTitle("Perhatian !!")
                    .setMessage("Perubahan data belum dikirim dan tidak akan tersimpan. \nApakah anda yakin ingin kembali?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(PunchlistDetailActivity.this, HomeActivity.class);
                            intent.putExtra("fragmentID", 1);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        } else {
            Intent intent = new Intent(PunchlistDetailActivity.this, HomeActivity.class);
            intent.putExtra("fragmentID", 1);
            startActivity(intent);
        }

    }
}
