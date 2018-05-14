package id.co.gsd.mybirawa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.model.ModelNews;
import id.co.gsd.mybirawa.model.ModelPunchlist;
import id.co.gsd.mybirawa.util.TimeHelper;

/**
 * Created by Biting on 1/16/2018.
 */

public class NewsAdapter extends BaseAdapter {
    private Context mContext;
    private ModelNews model;
    private List<ModelNews> listModel;
    private TextView tvJudul, tvKonten, tvTgl;
    private TimeHelper th;
    private ImageView imageView;

    public NewsAdapter(Context mContext, List<ModelNews> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        th = new TimeHelper();
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_news, null);
        }
        imageView = view.findViewById(R.id.img_news_list);
        tvJudul = view.findViewById(R.id.tv_news_judul);
        tvKonten = view.findViewById(R.id.tv_news_konten);
        tvTgl = view.findViewById(R.id.tv_news_tgl);

        String status = model.getNews_status();

        if (status.equals("1")) {
            imageView.setImageResource(R.drawable.icon_news_red);
        } else if (status.equals("2")) {
            imageView.setImageResource(R.drawable.icon_news_yellow);
        } else {
            imageView.setImageResource(R.drawable.icon_news_blue);
        }
        tvJudul.setText(model.getNews_judul());
        tvKonten.setText(model.getNews_isi());
        tvTgl.setText(th.getElapsedTimeFormatted(model.getNews_tgl()));

        return view;
    }
}
