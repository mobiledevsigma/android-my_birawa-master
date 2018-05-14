package id.co.gsd.mybirawa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.model.ModelPunchlistSpv;

/**
 * Created by Biting on 1/26/2018.
 */

public class PunchSpvAdapter extends BaseAdapter {

    private ModelPunchlistSpv model;
    private List<ModelPunchlistSpv> listModel;
    private Context mContext;

    public PunchSpvAdapter(Context mContext, List<ModelPunchlistSpv> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int i) {
        return listModel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderSpv holder;
        model = listModel.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_punch_spv, null);
            holder = new ViewHolderSpv(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderSpv) view.getTag();
        }

        if (model.getStatus().equals("0")) {
            holder.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_spv_red));
        } else if (model.getStatus().equals("1")) {
            holder.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_spv_green));
        } else {
            holder.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_spv_yellow));
        }

        holder.textNoPunch.setText("No. Punchlist : " + model.getPunchNo());
        holder.textRole.setText("Role : " + model.getRoleName());
        holder.textGedung.setText("Gedung : " + model.getGedungName());
        holder.textLantai.setText("Lantai : " + model.getLantaiName());
        holder.textPerangkat.setText("Perangkat : " + model.getItemName());
        holder.textKeluhan.setText("Keluhan : " + model.getKeluhan());

        return view;
    }

    static class ViewHolderSpv {
        LinearLayout layout;
        TextView textNoPunch;
        TextView textRole;
        TextView textGedung;
        TextView textLantai;
        TextView textPerangkat;
        TextView textKeluhan;

        ViewHolderSpv(View view) {
            layout = view.findViewById(R.id.submenu);
            textNoPunch = view.findViewById(R.id.tv_punch_spv_code);
            textRole = view.findViewById(R.id.tv_punch_spv_role);
            textGedung = view.findViewById(R.id.tv_punch_spv_gedung);
            textLantai = view.findViewById(R.id.tv_punch_spv_lantai);
            textPerangkat = view.findViewById(R.id.tv_punch_spv_item);
            textKeluhan = view.findViewById(R.id.tv_punch_spv_keluhan);
        }
    }
}
