package id.co.gsd.mybirawa.controller.sc;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;


public class doneActivity extends AppCompatActivity {

    String[] nama_item = {
            "Air Mineral",
            "Tissue",
            "Tissu Gulung",
            "Air Galon",
    };

    String[] nama_items = {
            "Air Mineral",
            "Tissue",
            "Tissu Gulung",
            "Air Galon",
    };
    String[] no = {
            "2",
            "3",
            "10",
            "5",
    };
    //Variable Listview
    ListView listmenu;
    ListView listmenu2;
    private FloatingActionButton fab;

    //ArrayList<dataModel> listData;
    private static Adapter_buktiorder adapter;
    private static Adapter_detail adapter2;
    Typeface font, fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done  );


        font = Typeface.createFromAsset(doneActivity.this.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(doneActivity.this.getAssets(), "fonts/Nexa Bold.otf");

        TextView txtTitle = (TextView) findViewById(R.id.waktu);
        txtTitle.setTypeface(font);
        TextView txtTitlea = (TextView) findViewById(R.id.waktus);
        txtTitlea.setTypeface(fontbold);

        TextView txtTitle_tiga11 = (TextView) findViewById(R.id.tanggal);
        txtTitle_tiga11.setTypeface(font);
        TextView txtTitleb = (TextView) findViewById(R.id.tanggals);
        txtTitleb.setTypeface(fontbold);

        TextView txtTitleaa = (TextView) findViewById(R.id.harga);
        txtTitleaa.setTypeface(fontbold);
        TextView almt = (TextView) findViewById(R.id.no_transaksi);
        almt.setTypeface(fontbold);

        TextView txtTitle_tiga11fs = (TextView) findViewById(R.id.a);
        txtTitle_tiga11fs.setTypeface(fontbold);

        TextView ttxtTitle_iga11 = (TextView) findViewById(R.id.b);
        ttxtTitle_iga11.setTypeface(fontbold);

        TextView ttxtTitle_iga1s1 = (TextView) findViewById(R.id.c);
        ttxtTitle_iga1s1.setTypeface(fontbold);

        TextView tgl_trima = (TextView) findViewById(R.id.tgl_terima);
        tgl_trima.setTypeface(fontbold);

        TextView tgl_trimaa= (TextView) findViewById(R.id.tgl_terimaa);
        tgl_trimaa.setTypeface(fontbold);

        TextView wkttrima = (TextView) findViewById(R.id.wkt_teerima);
        wkttrima.setTypeface(fontbold);

        TextView wkt_trima = (TextView) findViewById(R.id.waktu_terima);
        wkt_trima.setTypeface(fontbold);


        ImageView goto_menu = (ImageView) findViewById(R.id.close);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(doneActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        listmenu = (ListView) findViewById(R.id.list);
        Adapter_buktiorder adapter=new Adapter_buktiorder(doneActivity.this,nama_item);
        listmenu.setAdapter(adapter);
    }
}
