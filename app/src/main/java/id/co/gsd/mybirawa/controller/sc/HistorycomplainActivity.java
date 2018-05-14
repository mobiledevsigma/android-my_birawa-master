package id.co.gsd.mybirawa.controller.sc;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import id.co.gsd.mybirawa.R;

public class HistorycomplainActivity extends AppCompatActivity {

    String[] no ={
            "No Request : SC001",
            "No Request : SC002",
            "No Request : SC003",
            "No Request : SC004",
            "No Request : SC005",
            "No Request : SC006",

    };

    String[] tanggal ={
            "Tanggal Request : 22/08/2018",
            "Tanggal Request : 21/09/2018",
            "Tanggal Request : 20/04/2018",
            "Tanggal Request : 19/11/2018",
            "Tanggal Request : 21/06/2018",
            "Tanggal Request : 22/07/2018",

    };

    String[] fm={
            "Nama FM : Area 1",
            "Nama FM : Area 2",
            "Nama FM : Area 3",
            "Nama FM : Area 4",
            "Nama FM : Area 5",
            "Nama FM : Area 6",

    };
    String[] item={
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
    };
    String[] jmlitem={
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",

    };
    String[] status={
            "Status : Approved",
            "Status : Approved",
            "Status : Approved",
            "Status : Approved",
            "Status : Approved",
            "Status : Approved",
    };


    //Variable Listview
    ListView listmenu;
    private FloatingActionButton fab;

    //ArrayList<dataModel> listData;
    private static Adapter_historycomplain adapter;
    Typeface font,fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historycomplain);

//        font = Typeface.createFromAsset(HistoryActivity.this.getAssets(),"fonts/Nexa Light.otf");
//        fontbold = Typeface.createFromAsset(HistoryActivity.this.getAssets(),"fonts/Nexa Bold.otf");
//
//        TextView toolbar = (TextView) findViewById(R.id.request);
//        toolbar.setTypeface(fontbold);

        listmenu=(ListView) findViewById(R.id.list_historycomplain);

        Adapter_historycomplain adapter=new Adapter_historycomplain (HistorycomplainActivity.this,no,tanggal,fm,item,jmlitem,status);
        listmenu.setAdapter(adapter);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
