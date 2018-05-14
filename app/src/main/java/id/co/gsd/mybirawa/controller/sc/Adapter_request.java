package id.co.gsd.mybirawa.controller.sc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import id.co.gsd.mybirawa.R;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class Adapter_request extends BaseAdapter {
    private final Activity context;
    ArrayList<HashMap<String,String>> data ;
    Typeface font,fontbold;


    public Adapter_request(Activity context, ArrayList<HashMap<String,String>> d) {
        //super(context, R.layout.item_list_request2, 0);
        // TODO Auto-generated constructor stub

        this.data = d;
        this.context=context;


        System.out.println("masuk adapter");
        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_history2, null,true);

        HashMap<String,String> dat = data.get(position);

        System.out.println("masuk adapter"+ position);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.no_order);
        txtTitle.setText("No. Order : "+dat.get("order_sc_number"));
        txtTitle.setTypeface(fontbold);

        TextView txtTitle_dua = (TextView) rowView.findViewById(R.id.tanggal_order);
        txtTitle_dua.setText("Tanggal request : "+dat.get("order_date"));
        txtTitle_dua.setTypeface(font);

        TextView txtTitle_tiga = (TextView) rowView.findViewById(R.id.nm_fm);
        txtTitle_tiga.setText("Request by : "+dat.get("nama_user"));
        txtTitle_tiga.setTypeface(font);

        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.nm_item);
        txtTitle1.setText(dat.get("order_sc_number"));
        txtTitle1.setTypeface(font);
        txtTitle1.setVisibility(View.INVISIBLE);

//        TextView txtTitle_dua1 = (TextView) rowView.findViewById(R.id.jml_item);
//        txtTitle_dua1.setText(dat.get("order_sc_number"));
//        txtTitle_dua1.setTypeface(font);
//        txtTitle_dua1.setVisibility(View.INVISIBLE);

        TextView txtTitle_tiga1 = (TextView) rowView.findViewById(R.id.status);
        txtTitle_tiga1.setText("Status :" + dat.get("status_name"));
        txtTitle_tiga1.setTypeface(fontbold);

//        LinearLayout fab = (LinearLayout) view.findViewById(R.id.order);
//
//        fab.setOnClickListener(
//                new View.OnClickListener() {
//
//                    public void onClick(View arg0) {
//                        Intent i = new Intent(getContext(), MainActivity.class);
//
//                        getContext().startActivity(i);
//
//                    }
//                });


//        LinearLayout goto_menu= (LinearLayout) rowView.findViewById(R.id.orders);
//        goto_menu.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                Intent i = new Intent(getContext(), Detail_requestActivity.class);
//                getContext().startActivity(i);
//            }
//        });

        return rowView;

    };


}
