package id.co.gsd.mybirawa.controller.sc;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;

import id.co.gsd.mybirawa.R;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RequestConfirmation extends AppCompatActivity {

    String[] nama_item ={
            "Air Mineral",
            "Tissue",
            "Tissu Gulung",
            "Air Galon",
    };

    String[] nama_items ={
            "Air Mineral",
            "Tissue",
            "Tissu Gulung",
            "Air Galon",
    };
    String[] no={
            "2",
            "3",
            "10",
            "5",
    };
    //Variable Listview
    ListView listmenu;
    ListView listmenu2;
    private FloatingActionButton fab;
    ArrayList<String> dftralamat= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
    ImageView almts;
    TextView pilihalamat;


    //ArrayList<dataModel> listData;
    private static Adapter_reject adapter;
    private static Adapter_detail adapter2;
    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_confirmation);


        font = Typeface.createFromAsset(RequestConfirmation.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(RequestConfirmation.this.getAssets(),"fonts/Nexa Bold.otf");

        TextView txtTitle = (TextView) findViewById(R.id.orderitem);
        txtTitle.setTypeface(fontbold);



        TextView txtTitle_tiga11 = (TextView) findViewById(R.id.additem);
        txtTitle_tiga11.setTypeface(fontbold);

//        TextView almt = (TextView) findViewById(R.id.alamat);
//        almt.setTypeface(fontbold);

        TextView txtTitle_tiga11fs = (TextView) findViewById(R.id.a);
        txtTitle_tiga11fs.setTypeface(fontbold);

        TextView ttxtTitle_iga11 = (TextView) findViewById(R.id.b);
        ttxtTitle_iga11.setTypeface(fontbold);

        TextView ttxtTitle_iga1s1 = (TextView) findViewById(R.id.c);
        ttxtTitle_iga1s1.setTypeface(fontbold);

        TextView ttxtTitle_iga1s1x = (TextView) findViewById(R.id.d);
        ttxtTitle_iga1s1x.setTypeface(fontbold);


        LinearLayout goto_menu= (LinearLayout) findViewById(R.id.add);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(RequestConfirmation.this, MenuActivity.class);
                startActivity(i);
            }
        });

        Button goto_menus= (Button) findViewById(R.id.button6);
        goto_menus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(RequestConfirmation.this, ProsesActivity.class);
                startActivity(i);
            }
        });

        listmenu=(ListView) findViewById(R.id.list_pesanan);

        Adapter_list_request adapter=new Adapter_list_request(RequestConfirmation.this,nama_item);
        listmenu.setAdapter(adapter);

//        initItem();
//        pilihalamat = (TextView) findViewById(R.id.address);
//        spinnerDialogs = new SpinnerDialog(RequestConfirmation.this,dftralamat,"Select item");
//        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
//            @Override
//            public void onClick(String lantai, int position) {
//                Toast.makeText(RequestConfirmation.this,""+lantai,Toast.LENGTH_SHORT).show();
//                pilihalamat.setText(lantai);
//            }
//        });
//
//        almts = (ImageView) findViewById(R.id.pilihaddress);
//        almts.setOnClickListener(new View.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v) {
//                spinnerDialogs.showSpinerDialog();
//
//            }
//        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
//    private void initItem() {
//        dftralamat.add("Lantai Eksternal");
//        dftralamat.add("Lantai 1");
//        dftralamat.add("Lantai 2");
//        dftralamat.add("Lantai 3");
//        dftralamat.add("Lantai 4");
//        dftralamat.add("Lantai 5");
//        dftralamat.add("Lantai 6");
//    }
}
