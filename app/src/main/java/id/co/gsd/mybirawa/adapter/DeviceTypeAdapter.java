package id.co.gsd.mybirawa.adapter;

/**
 * Created by MAC on 09/10/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.model.ModelDeviceType;
import id.co.gsd.mybirawa.util.SessionManager;


/**
 * Created by MAC on 29/09/2017.
 */

public class DeviceTypeAdapter extends BaseAdapter {

    private Context mContext;
    private ModelDeviceType model;
    private List<ModelDeviceType> listModel;
    private LinearLayout layout;
    SessionManager session;

    public DeviceTypeAdapter(Context context, List<ModelDeviceType> list) {
        this.mContext = context;
        this.listModel = list;
        session = new SessionManager(context);
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
        viewHolder holder;
        model = listModel.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_expand_group, null);
            holder = new viewHolder(view);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }

        byte[] decodedString = Base64.decode(model.getPj_icon(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.textGroup.setText(model.getPj_name());
        holder.imgPJ.setImageBitmap(decodedByte);
        return view;
    }

    static class viewHolder {
        TextView textGroup;
        ImageView imgPJ;

        viewHolder(View view) {
            imgPJ = view.findViewById(R.id.img_pj);
            textGroup = view.findViewById(R.id.tv_perangkat_jenis);
        }
    }
}