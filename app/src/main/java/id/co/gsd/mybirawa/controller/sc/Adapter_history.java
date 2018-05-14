package id.co.gsd.mybirawa.controller.sc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import id.co.gsd.mybirawa.R;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class Adapter_history extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] no;
    private final String[] tanggal;
    private final String[] fm;
    private final String[] item;
    private final String[] jmlitem;
    private final String[] status;
    Typeface font,fontbold;


    public Adapter_history(Activity context, String[] no, String[] tanggal, String[] fm, String[] item, String[] jmlitem, String[] status) {
        super(context, R.layout.item_list_history, no);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.no=no;
        this.tanggal=tanggal;
        this.fm=fm;
        this.item=item;
        this.jmlitem=jmlitem;
        this.status=status;


        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_history2, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.no_order);
        txtTitle.setText(no[position]);
        txtTitle.setTypeface(fontbold);

        TextView txtTitle_dua = (TextView) rowView.findViewById(R.id.tanggal_order);
        txtTitle_dua.setText(tanggal[position]);
        txtTitle_dua.setTypeface(font);

        TextView txtTitle_tiga = (TextView) rowView.findViewById(R.id.nm_fm);
        txtTitle_tiga.setText(fm[position]);
        txtTitle_tiga.setTypeface(font);

        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.nm_item);
        txtTitle1.setText(item[position]);
        txtTitle1.setTypeface(font);

        TextView txtTitle_dua1 = (TextView) rowView.findViewById(R.id.jml_item);
        txtTitle_dua1.setText(jmlitem[position]);
        txtTitle_dua1.setTypeface(font);

        TextView txtTitle_tiga1 = (TextView) rowView.findViewById(R.id.status);
        txtTitle_tiga1.setText(status[position]);
        txtTitle_tiga1.setTypeface(fontbold);

        LinearLayout goto_menu= (LinearLayout) rowView.findViewById(R.id.order);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(getContext(), doneActivity.class);
                getContext().startActivity(i);
            }
        });

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



        return rowView;

    };


}
