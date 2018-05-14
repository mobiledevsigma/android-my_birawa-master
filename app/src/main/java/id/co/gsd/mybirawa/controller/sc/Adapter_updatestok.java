package id.co.gsd.mybirawa.controller.sc;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.MemoryCache;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class Adapter_updatestok extends BaseAdapter {
    private final Activity context;
    Typeface font,fontbold;
    ArrayList<HashMap<String,String>> data ;

    private MemoryCache mc;
    private View []viewHolder;


    public Adapter_updatestok (Activity context, ArrayList<HashMap<String,String>> d) {
        // TODO Auto-generated constructor stub

        this.data = d;
        this.context=context;

        mc = new MemoryCache();
        viewHolder = new View[d.size()];

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

        HashMap<String,String> dat = data.get(position);

        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_list_stock, null);


            System.out.println("inflate ke "+position);

            TextView txtTitle = (TextView) view.findViewById(R.id.nameText);
            txtTitle.setText(dat.get("nama_perangkat_sc"));
            //txtTitle.setTypeface(font);

            TextView txtTitlse = (TextView) view.findViewById(R.id.isiText);

//            String approve = "";
//            if (dat.get("stok_ditolak").equals("null")) {
//                approve = dat.get("stok_ditolak");
//            } else {
//                approve = dat.get("qty_request_sc");
//            }
//            txtTitlse.setText("Stok Awal : " + dat.get("qty_perangkat_sc") + "\n" +
//                    "Stok Request : " + dat.get("qty_request_sc") + "\n" +
//                    "Stok Approve : " + approve);

            txtTitlse.setText("Stok : " + dat.get("qty_stock") );
            txtTitlse.setTypeface(font);

            TextView txtStatus = (TextView) view.findViewById(R.id.statusText);
//            if (dat.get("status_transaksi").equals("1")) {
//                txtStatus.setText("Ready");
//                txtStatus.setTextColor(Color.GREEN);
//            } else {
//                txtStatus.setText("Not Ready");
//                txtStatus.setTextColor(Color.RED);
//            }

            txtStatus.setTypeface(font);


            ImageView gambar = (ImageView) view.findViewById(R.id.gambarImage);

            if (!dat.get("gambar").equals("") && dat.get("gambar") != null) {
                if (mc.getBitmapFromMemCache(ConstantUtils.URL.SERVER + "image/sc/" + dat.get("gambar")) != null) {
                    //pb.setVisibility(View.GONE);
                    gambar.setImageBitmap(mc.getBitmapFromMemCache(ConstantUtils.URL.SERVER + "image/sc/" + dat.get("gambar")));
                } else {
                    new DownloadImageTask(gambar).execute(ConstantUtils.URL.SERVER + "image/sc/" + dat.get("gambar"));
                }

            } else {
                new DownloadImageTask(gambar).execute(ConstantUtils.URL.SERVER + "image/sc/__a");
            }
            viewHolder[position] = view;
        }else {
            if(viewHolder[position]!=null){
                view = viewHolder[position];
            }

        }

        return view;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String imgUrl;
        //ProgressBar pBar;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            //this.pBar = pb;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            imgUrl = urls[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                mIcon = BitmapFactory.decodeStream(in,null,options);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            if(result!=null){
                // pBar.setVisibility(View.GONE);
                mc.addBitmapToMemoryCache(imgUrl,result);
                bmImage.setImageBitmap(result);
            }else{
                // pBar.setVisibility(View.GONE);
                bmImage.setImageResource(R.drawable.logo_birawa);
            }
        }
    }

}

