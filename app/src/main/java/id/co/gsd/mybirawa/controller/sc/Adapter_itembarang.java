package id.co.gsd.mybirawa.controller.sc;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import id.co.gsd.mybirawa.R;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class Adapter_itembarang extends ArrayAdapter<String> {
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


    public Adapter_itembarang(final Activity context, String[] nama_items) {
        super(context, R.layout.item_order, nama_items);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.nama_items=nama_items;



        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");




    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_order, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.jenis_item);
        txtTitle.setText(nama_items[position]);
        txtTitle.setTypeface(fontbold);

        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.jumlah);
        txtTitle1.setTypeface(font);
        TextView txtTitle13 = (TextView) rowView.findViewById(R.id.stok);
        txtTitle13.setTypeface(font);
        EditText txtTitle11 = (EditText) rowView.findViewById(R.id.editText);
        txtTitle11.setTypeface(fontbold);
        EditText txtTitle12 = (EditText) rowView.findViewById(R.id.editText3);
        txtTitle12.setTypeface(fontbold);

//        satuannya = (TextView) rowView.findViewById(R.id.satuan);
//        initItem();
//        spinnerDialogs = new SpinnerDialog(context,satuan,"Pilih jenis Satuan");
//        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
//            @Override
//            public void onClick(String lantai, int position) {
//                Toast.makeText(context,""+lantai,Toast.LENGTH_SHORT).show();
//                satuannya.setText(lantai);
//            }
//        });

//        pilih_satuan = (ImageView) rowView.findViewById(R.id.pilihsatuan);
//        pilih_satuan.setOnClickListener(new View.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v) {
//                spinnerDialogs.showSpinerDialog();
//
//            }
//        });



//        LinearLayout goto_menu= (LinearLayout) rowView.findViewById(R.id.orders);
//        goto_menu.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                Intent i = new Intent(getContext(), Detail_complain.class);
//                getContext().startActivity(i);
//            }
//        });

        return rowView;

    };

    private void initItem() {
        satuan.add("Dus");
        satuan.add("buah");
        satuan.add("roll");
    }


}
