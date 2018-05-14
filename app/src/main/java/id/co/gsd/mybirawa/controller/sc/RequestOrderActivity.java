package id.co.gsd.mybirawa.controller.sc;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.co.gsd.mybirawa.R;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RequestOrderActivity extends AppCompatActivity {


    String[] nama_item ={
            "FM JAKARTA PUSAT & MM"

    };
    String[] nama_jenisitem ={
            "Tissu",
            "Pengharum Ruangan",
            "Air Mineral",

    };
    String[] item_jenis ={
            "Tissu",
            "Air Mineral Galon",
            "Air Mineral Botol",

    };

    ArrayList<String> dftralamat= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
    ImageView almts;
    TextView pilihalamat;

    ArrayList<String> dftralamat2= new ArrayList<>();
    SpinnerDialog spinnerDialogs2;
    ImageView almts2;
    TextView pilihalamat2;

    ArrayList<String> dftralamat3= new ArrayList<>();
    SpinnerDialog spinnerDialogs3;
    ImageView almts3;
    TextView pilihalamat3;

    Typeface font,fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Request Order");


        font = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Nexa Bold.otf");




        pilihalamat = (TextView) findViewById(R.id.jenisitem2);
        pilihalamat.setTypeface(fontbold);

        spinnerDialogs = new SpinnerDialog(RequestOrderActivity.this,dftralamat,"Select item");
        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int position) {
               // Toast.makeText(RequestOrderActivity.this,""+lantai,Toast.LENGTH_SHORT).show();
                pilihalamat.setText(lantai);
            }
        });

        almts = (ImageView) findViewById(R.id.pilihanjenis);
        almts.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs.showSpinerDialog();

            }
        });

        initItem();

        initItem2();
        pilihalamat2 = (TextView) findViewById(R.id.item_jns);
        pilihalamat2.setTypeface(fontbold);
        spinnerDialogs2 = new SpinnerDialog(RequestOrderActivity.this,dftralamat2,"Select item");
        spinnerDialogs2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int position) {
                //Toast.makeText(RequestOrderActivity.this,""+lantai,Toast.LENGTH_SHORT).show();
                pilihalamat2.setText(lantai);
            }
        });

        almts2 = (ImageView) findViewById(R.id.pilihitem);
        almts2.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs2.showSpinerDialog();

            }
        });

        initItem3();
        pilihalamat3 = (TextView) findViewById(R.id.address);
        pilihalamat3.setTypeface(fontbold);
        spinnerDialogs3 = new SpinnerDialog(RequestOrderActivity.this,dftralamat3,"Select item");
        spinnerDialogs3.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int position) {
                //Toast.makeText(RequestOrderActivity.this,""+lantai,Toast.LENGTH_SHORT).show();
                pilihalamat3.setText(lantai);
            }
        });

        almts3 = (ImageView) findViewById(R.id.pilihaddress);
        almts3.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs3.showSpinerDialog();

            }
        });

    }

    private void initItem3() {
        dftralamat3.add("FM JAKARTA PUSAT & MM");

    }

    private void initItem() {
        dftralamat2.add("Air Refreshner");
    }

    private void initItem2() {
        dftralamat.add("Tissu");
        dftralamat.add("Air Mineral");
        dftralamat.add("Pengharum Ruangan");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
