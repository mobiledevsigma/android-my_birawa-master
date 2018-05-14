package id.co.gsd.mybirawa.controller.sc;




import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class    List_request_bmFragment extends Fragment {

    String[] no ={
            "No Request : SC001",
            "No Request : SC002",
            "No Request : SC003",
            "No Request : SC004",
            "No Request : SC005",
            "No Request : SC006",

    };

    String[] tanggal ={
            "Tanggal Request : 22/08/2018",
            "Tanggal Request : 21/09/2018",
            "Tanggal Request : 20/04/2018",
            "Tanggal Request : 19/11/2018",
            "Tanggal Request : 21/06/2018",
            "Tanggal Request : 22/07/2018",

    };

    String[] fm={
            "Nama FM : Area 1",
            "Nama FM : Area 2",
            "Nama FM : Area 3",
            "Nama FM : Area 4",
            "Nama FM : Area 5",
            "Nama FM : Area 6",

    };
    String[] item={
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
            "Item : Air Mineral",
    };
    String[] jmlitem={
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",
            "Jumlah Item : 12",

    };
    String[] status={
            "Status : Waiting for Approved",
            "Status : Waiting for Approved",
            "Status : Waiting for Approved",
            "Status : Waiting for Approved",
            "Status : Waiting for Approved",
            "Status : Waiting for Approved",
    };


    //Variable Listview
    ListView listmenu;
    ImageView logout;
    private FloatingActionButton fab;

    //ArrayList<dataModel> listData;
    private static Adapter_requestbm adapter;
    Typeface font,fontbold;


    public List_request_bmFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_request_bm, container,false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        TextView toolbar = (TextView) view.findViewById(R.id.request);
        toolbar.setTypeface(fontbold);

        listmenu=(ListView)view.findViewById(R.id.list_request);


        Adapter_requestbm adapter=new Adapter_requestbm (getActivity(),no,tanggal,fm,item,jmlitem,status);
        listmenu.setAdapter(adapter);

        ImageView goto_menu= (ImageView) view.findViewById(R.id.imageView7);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(), HistoryActivity.class);
                getActivity().startActivity(i);
            }
        });

        logout = (ImageView) view.findViewById(R.id.logouts);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), logout);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu2 , popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        new AlertDialog.Builder(getActivity())
                                .setMessage("Are you sure you want to logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        System.exit(0);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                        return true;

                    }
                });

                popup.show();
            }
        });

        return view;
    }

}
