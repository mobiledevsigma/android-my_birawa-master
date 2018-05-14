package id.co.gsd.mybirawa.controller.sc;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;

public class Detail_update_stokActivity extends AppCompatActivity {

    Typeface font,fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_update_stok);

        font = Typeface.createFromAsset(Detail_update_stokActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(Detail_update_stokActivity.this.getAssets(),"fonts/Nexa Bold.otf");

        TextView txtTitle = (TextView) findViewById(R.id.barang);
        txtTitle.setTypeface(fontbold);

//        TextView txtTitles = (TextView) findViewById(R.id.orderitem);
//        txtTitles.setTypeface(fontbold);

        TextView txtTitless = (TextView) findViewById(R.id.edit);
        txtTitless.setTypeface(fontbold);

        EditText txtTitle_dua = (EditText) findViewById(R.id.stok);
        txtTitle_dua.setTypeface(fontbold);

        TextView txtTitle_tiga = (TextView) findViewById(R.id.nama_barang);
        txtTitle_tiga.setTypeface(font);

        TextView txtTitle1 = (TextView) findViewById(R.id.jml_stok);
        txtTitle1.setTypeface(font);

//        Button goto_menu= (Button) findViewById(R.id.edit);
//        goto_menu.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                Intent i = new Intent(getApplication(), MenuActivity.class);
//                getApplication().startActivity(i);
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
