package id.co.gsd.mybirawa.controller.sc;

import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;


public class detail_req_bmActivity extends AppCompatActivity {

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

    //ArrayList<dataModel> listData;
    private static Adapter_reject adapter;
    private static Adapter_detail adapter2;
    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_req_bm);


        font = Typeface.createFromAsset(detail_req_bmActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(detail_req_bmActivity.this.getAssets(),"fonts/Nexa Bold.otf");

        TextView txtTitle = (TextView) findViewById(R.id.no_order);
        txtTitle.setTypeface(fontbold);


        EditText ttxtTitle_tiga11 = (EditText) findViewById(R.id.deskripsi);
        ttxtTitle_tiga11.setTypeface(font);

        TextView txtTitle_ttiga11 = (TextView) findViewById(R.id.Detail);
        txtTitle_ttiga11.setTypeface(font);


//        TextView ttxtTitle_iga1s1 = (TextView) findViewById(R.id.c);
//        ttxtTitle_iga1s1.setTypeface(fontbold);

        TextView a= (TextView) findViewById(R.id.ket);
        a.setTypeface(font);

        EditText b= (EditText) findViewById(R.id.editText2);
        b.setTypeface(font);


        final LinearLayout pertamma = (LinearLayout) findViewById(R.id.reject);
        final LinearLayout pertamaa = (LinearLayout) findViewById(R.id.approve);

        Button sendBtn = (Button) findViewById(R.id.button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = pertamaa.getVisibility();
                if(visibility==View.GONE)
                    // set ListView visible
                    pertamaa.setVisibility(View.VISIBLE);
                else if (visibility == View.VISIBLE)
                    pertamaa.setVisibility(View.GONE);


            }
        });
        Button sendBtn2 = (Button) findViewById(R.id.button3);
        sendBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = pertamma.getVisibility();
                if(visibility==View.GONE)
                    // set ListView visible
                    pertamma.setVisibility(View.VISIBLE);
                else if (visibility == View.VISIBLE)
                    pertamma.setVisibility(View.GONE);


            }
        });


        listmenu2=(ListView) findViewById(R.id.list_detail);

//        Adapter_detail adapter2=new Adapter_detail(detail_req_bmActivity.this,nama_items,no);
//        listmenu2 .setAdapter(adapter2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
