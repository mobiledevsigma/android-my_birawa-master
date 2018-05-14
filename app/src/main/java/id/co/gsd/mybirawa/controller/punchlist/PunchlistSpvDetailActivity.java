package id.co.gsd.mybirawa.controller.punchlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.CameraManager;
import id.co.gsd.mybirawa.util.CustomSessionManager;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * Created by Biting on 1/16/2018.
 */

public class PunchlistSpvDetailActivity extends AppCompatActivity {

    private CustomSessionManager dataSess;
    private Toolbar toolbar;
    private TextView tvNumber, tvOrder, tvGedung, tvLantai, tvPerangkat, tvKeluhan, tvToolbar;
    private Spinner spinner;
    private ImageView img_before, img_after;
    private EditText editText;
    private Button button;
    private String idPunch, idStatus, desc;
    private int img_pos;
    private CameraManager camMan;
    private String img_1, img_2;
    private SessionManager session;
    private List<String> listStatus;

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
        listStatus.add("Belum Dikerjakan");
        listStatus.add("Selesai");
        listStatus.add("Tertunda");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        dataSess = new CustomSessionManager(this, "punchlist" + idPunch);
        session = new SessionManager(this);
        camMan = new CameraManager();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStatus);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Intent intent = getIntent();

        SimpleDateFormat defol = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateServer = null;
        try {
            dateServer = defol.parse(intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_ORDER));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sDate = new SimpleDateFormat("dd MMMM yyyy");
        String date = sDate.format(dateServer);

        idPunch = intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST);
        idStatus = intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_STATUS);
        img_1 = intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_IMAGE_1);
        img_2 = intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_IMAGE_2);
        desc = intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_DESC);
        tvToolbar.setText(intent.getStringExtra(ConstantUtils.PUNCHLIST.TAG_NO_PUNCHLIST));
        tvNumber.setText("No. Punchlist : " + intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_NO_PUNCHLIST));
        tvOrder.setText("Tanggal Order : " + date);
        tvGedung.setText("Nama Gedung : " + intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_GEDUNG));
        tvLantai.setText("Nama Lantai : " + intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_LANTAI));
        tvPerangkat.setText("Nama Perangkat : " + intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_NAMA_PERANGKAT));
        tvKeluhan.setText("Keluhan : " + intent.getStringExtra(ConstantUtils.PUNCH_SPV.TAG_KELUHAN));

        if (!img_1.equals("null")) {
            byte[] decodedString = Base64.decode(img_1, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_before.setImageBitmap(decodedByte);
        } else {
            img_before.setImageResource(R.drawable.ic_camera);
        }

        if (!img_2.equals("null")) {
            byte[] decodedString = Base64.decode(img_2, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_after.setImageBitmap(decodedByte);
        } else {
            img_before.setImageResource(R.drawable.ic_camera);
        }

        spinner.setEnabled(false);
        spinner.setSelection(Integer.parseInt(idStatus));

        editText.setEnabled(false);
        editText.setText(desc);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (idStatus.equals("2")) {
            button.setText("History");
        } else if (idStatus.equals("1")) {
            button.setText("OK");
        } else {
            button.setText("Hapus");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idStatus.equals("2")) {
                    Intent intent1 = new Intent(PunchlistSpvDetailActivity.this, PunchlistHistory.class);
                    intent1.putExtra(ConstantUtils.PUNCH_SPV.TAG_ID_PUNCHLIST, idPunch);
                    startActivity(intent1);
                } else if (idStatus.equals("1")) {
                    onBackPressed();
                } else {
                    new AlertDialog.Builder(PunchlistSpvDetailActivity.this)
                            .setTitle("Peringatan !")
                            .setMessage("Apakah anda yakin akan menghapus data ?")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton("Tidak", null)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
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
}
