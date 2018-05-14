package id.co.gsd.mybirawa.controller.sc;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class Adapter_buktiorder extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] nama_item;
    Typeface font,fontbold;


    public Adapter_buktiorder(Activity context, String[] nama_item) {
        super(context, R.layout.item_bukti, nama_item);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.nama_item=nama_item;


        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_bukti, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.nama_item);
        txtTitle.setText(nama_item[position]);
        txtTitle.setTypeface(fontbold);



        return rowView;

    };


}
