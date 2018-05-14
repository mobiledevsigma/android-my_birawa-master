package id.co.gsd.mybirawa.controller.sc;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import id.co.gsd.mybirawa.R;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class Adapter_isijenis extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] nama_items;
    Typeface font,fontbold;
    //array untuk tampil di opsinya
    ArrayList<String> satuan = new ArrayList<>();
    //nama spinner yang digunakan
    SpinnerDialog spinnerDialogs;
    //inisialisasi id image button nya
    ImageView pilih_satuan;
    //inisialisasi text view nya
    TextView satuannya;


    public Adapter_isijenis(final Activity context, String[] nama_items) {
        super(context, R.layout.item_isijenis, nama_items);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.nama_items=nama_items;



        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");




    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_isijenis, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.nama_jenis);
        txtTitle.setText(nama_items[position]);
        txtTitle.setTypeface(fontbold);



        return rowView;

    };




}
