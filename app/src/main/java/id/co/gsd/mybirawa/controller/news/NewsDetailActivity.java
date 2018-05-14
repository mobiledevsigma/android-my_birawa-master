package id.co.gsd.mybirawa.controller.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * Created by Biting on 1/17/2018.
 */

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView tvJudul, tvTgl, tvKonten, tvToolbar, tvTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar = findViewById(R.id.toolbar_news);
        imageView = findViewById(R.id.img_news_detail);
        tvJudul = findViewById(R.id.tv_detail_judul);
        tvTgl = findViewById(R.id.tv_detail_tgl);
        tvTime = findViewById(R.id.tv_detail_time);
        tvKonten = findViewById(R.id.tv_detail_isi);
        tvToolbar = findViewById(R.id.tv_toolbar_news);

        Intent intent = getIntent();
        String judul = intent.getStringExtra(ConstantUtils.NEWS.TAG_JUDUL);
        String tgl = intent.getStringExtra(ConstantUtils.NEWS.TAG_TGL);
        String konten = intent.getStringExtra(ConstantUtils.NEWS.TAG_ISI);
        String status = intent.getStringExtra(ConstantUtils.NEWS.TAG_STATUS);

        SimpleDateFormat defol = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateServer = null;
        try {
            dateServer = defol.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sDate = new SimpleDateFormat("dd MMMM, yyyy");
        SimpleDateFormat sHour = new SimpleDateFormat("HH:mm");
        String date = sDate.format(dateServer);
        String hour = sHour.format(dateServer);

        if (status.equals("1")) {
            imageView.setImageResource(R.drawable.icon_news_red);
        } else if (status.equals("2")) {
            imageView.setImageResource(R.drawable.icon_news_yellow);
        } else {
            imageView.setImageResource(R.drawable.icon_news_blue);
        }
        tvToolbar.setText(judul);
        tvJudul.setText(judul);
        tvTgl.setText(date);
        tvTime.setText(hour);
        tvKonten.setText(konten);

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
}
