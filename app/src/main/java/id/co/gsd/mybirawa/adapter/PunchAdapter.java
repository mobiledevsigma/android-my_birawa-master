package id.co.gsd.mybirawa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.model.ModelPunchlist;

/**
 * Created by Biting on 1/16/2018.
 */

public class PunchAdapter extends BaseAdapter {
    private Context mContext;
    private ModelPunchlist model;
    private List<ModelPunchlist> listModel;
    private TextView tvNumber, tvOrder, tvGedung, tvLantai, tvPerangkat, tvKeluhan;
    private LinearLayout layout;

    public PunchAdapter(Context mContext, List<ModelPunchlist> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
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
            view = inflater.inflate(R.layout.item_list_punchlist, null);
        }
        layout = view.findViewById(R.id.lay_item_punch);
        tvNumber = view.findViewById(R.id.tv_punch_number);
        tvOrder = view.findViewById(R.id.tv_punch_order);
        tvGedung = view.findViewById(R.id.tv_punch_gedung);
        tvLantai = view.findViewById(R.id.tv_punch_lantai);
        tvPerangkat = view.findViewById(R.id.tv_punch_perangkat);
        tvKeluhan = view.findViewById(R.id.tv_punch_keluhan);

        if (model.getStatus().equals("0")) {
            layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_spv_red));
        } else if (model.getStatus().equals("1")) {
            layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_spv_green));
        } else {
            layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_spv_yellow));
        }

        SimpleDateFormat defol = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateServer = null;
        try {
            dateServer = defol.parse(model.getOrder_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sDate = new SimpleDateFormat("dd MMMM yyyy");
        String date = sDate.format(dateServer);

        tvNumber.setText("No. Punchlist : " + model.getPunchNo());
        tvOrder.setText("Tanggal Order : " + date);
        tvGedung.setText("Nama Gedung : " + model.getGedung_name());
        tvLantai.setText("Nama Lantai : " + model.getLantai_name());
        tvPerangkat.setText("Nama Perangkat : " + model.getPerangkat_name());
        tvKeluhan.setText("Keluhan : " + model.getPunchKeluhan());

        return view;
    }
}
