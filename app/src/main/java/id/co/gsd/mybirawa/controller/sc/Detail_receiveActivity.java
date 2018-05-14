package id.co.gsd.mybirawa.controller.sc;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;


public class Detail_receiveActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_detail_receive);

        font = Typeface.createFromAsset(Detail_receiveActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(Detail_receiveActivity.this.getAssets(),"fonts/Nexa Bold.otf");

        TextView txtTitle_ttiga11 = (TextView) findViewById(R.id.Detail);
        txtTitle_ttiga11.setTypeface(font);

        TextView txtTitle_tiga11 = (TextView) findViewById(R.id.request_item);
        txtTitle_tiga11.setTypeface(font);
        TextView txtTitle_tiga11fs = (TextView) findViewById(R.id.a);
        txtTitle_tiga11fs.setTypeface(fontbold);

        TextView ttxtTitle_iga11 = (TextView) findViewById(R.id.b);
        ttxtTitle_iga11.setTypeface(fontbold);

        TextView ttxtTitle_iga1s1 = (TextView) findViewById(R.id.c);
        ttxtTitle_iga1s1.setTypeface(fontbold);

        TextView txtTitle = (TextView) findViewById(R.id.no_order);
        txtTitle.setTypeface(fontbold);

        final LinearLayout pertama = (LinearLayout) findViewById(R.id.approve);
//        final LinearLayout pertamma = (LinearLayout) findViewById(R.id.reject);
        final LinearLayout pertamaa = (LinearLayout) findViewById(R.id.revisi);

        ImageView gokamera = (ImageView) findViewById(R.id.imageView4);
        gokamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent cameraIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 11);
            }
        });

        Button sendBtn = (Button) findViewById(R.id.approve_);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = pertama.getVisibility();
                if(visibility==View.GONE)
                    // set ListView visible
                    pertama.setVisibility(View.VISIBLE);
                else if (visibility == View.VISIBLE)
                    pertama.setVisibility(View.GONE);


            }
        });
        Button sendBtn1 = (Button) findViewById(R.id.complain);
        sendBtn1.setOnClickListener(new View.OnClickListener() {
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


        listmenu=(ListView) findViewById(R.id.list_reject);

        Adapter_reject adapter=new Adapter_reject(Detail_receiveActivity.this,nama_item);
        listmenu.setAdapter(adapter);

        listmenu2=(ListView) findViewById(R.id.list_detail);

//        Adapter_detail adapter2=new Adapter_detail(Detail_receiveActivity.this,nama_items,no);
//        listmenu2 .setAdapter(adapter2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView a= (TextView) findViewById(R.id.ket);
        a.setTypeface(font);

        EditText b= (EditText) findViewById(R.id.editText2);
        b.setTypeface(font);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
