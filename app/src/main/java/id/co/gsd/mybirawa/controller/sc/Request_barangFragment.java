package id.co.gsd.mybirawa.controller.sc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class Request_barangFragment extends Fragment {

    String[] nama_item ={
            "Tissu",
            "Air Mineral Galon",
            "Air Mineral Botol",

    };
    String[] nama_jenisitem ={
            "Tissu",
            "Pengharum Ruangan",
            "Air Mineral",

    };
    String[] item_jenis ={
            "Tissu",
            "Air Mineral Galon",
            "Air Mineral Botol",

    };


    //Variable Listview
    ListView listmenu,listmenu1,listmenu2;
    ImageView logout;
    private FloatingActionButton fab;
    ArrayList<String> dftralamat= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
    ImageView almts;
    TextView pilihalamat;

    ArrayList<String> dftralamat2= new ArrayList<>();
    SpinnerDialog spinnerDialogs2;
    ImageView almts2;
    TextView pilihalamat2;

    ArrayList<String> dftralamat3= new ArrayList<>();
    SpinnerDialog spinnerDialogs3;
    ImageView almts3;
    TextView pilihalamat3;



    //ArrayList<dataModel> listData;
    private static Adapter_itembarang adapter;
    private static Adapter_jenisitem adapter1;
    private static Adapter_isijenis adapter2;

    Typeface font,fontbold;


    public Request_barangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_barang, container,false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        TextView toolbar = (TextView) view.findViewById(R.id.itemfm);
        toolbar.setTypeface(fontbold);
        TextView toolbar1 = (TextView) view.findViewById(R.id.item_jenis);
        toolbar1.setTypeface(fontbold);
        TextView toolbar2 = (TextView) view.findViewById(R.id.item_jenis1);
        toolbar2.setTypeface(fontbold);
        TextView toolbar3 = (TextView) view.findViewById(R.id.item_jenis2);
        toolbar3.setTypeface(fontbold);
        TextView toolbarr3 = (TextView) view.findViewById(R.id.item_jenis3);
        toolbarr3.setTypeface(fontbold);


        initItem();
        pilihalamat = (TextView) view.findViewById(R.id.jenisitem2);
        pilihalamat.setTypeface(fontbold);

        spinnerDialogs = new SpinnerDialog(getActivity(),dftralamat,"Select item");
        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int position) {
                Toast.makeText(getActivity(),""+lantai,Toast.LENGTH_SHORT).show();
                pilihalamat.setText(lantai);
            }
        });

        almts = (ImageView) view.findViewById(R.id.pilihanjenis);
        almts.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs.showSpinerDialog();

            }
        });


        initItem2();
        pilihalamat2 = (TextView) view.findViewById(R.id.item_jns);
        pilihalamat2.setTypeface(fontbold);
        spinnerDialogs2 = new SpinnerDialog(getActivity(),dftralamat2,"Select item");
        spinnerDialogs2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int position) {
                Toast.makeText(getActivity(),""+lantai,Toast.LENGTH_SHORT).show();
                pilihalamat2.setText(lantai);
            }
        });

        almts2 = (ImageView) view.findViewById(R.id.pilihitem);
        almts2.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs2.showSpinerDialog();

            }
        });

        initItem3();
        pilihalamat3 = (TextView) view.findViewById(R.id.address);
        pilihalamat3.setTypeface(fontbold);
        spinnerDialogs3 = new SpinnerDialog(getActivity(),dftralamat3,"Select item");
        spinnerDialogs3.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int position) {
                Toast.makeText(getActivity(),""+lantai,Toast.LENGTH_SHORT).show();
                pilihalamat3.setText(lantai);
            }
        });

        almts3 = (ImageView) view.findViewById(R.id.pilihaddress);
        almts3.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                spinnerDialogs3.showSpinerDialog();

            }
        });


//        listmenu=(ListView)view.findViewById(R.id.list_item);
//
//        Adapter_itembarang adapter=new Adapter_itembarang(getActivity(),nama_item);
//        listmenu.setAdapter(adapter);


        Button goto_menu= (Button) view.findViewById(R.id.button4);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(getContext(), RequestConfirmation.class);
                getContext().startActivity(i);
            }
        });

        ImageView goto_menux= (ImageView) view.findViewById(R.id.history);
        goto_menux.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(getContext(), History_tabActivity.class);
                getContext().startActivity(i);
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
                                        SessionManager session = new SessionManager(getActivity());
                                        session.logoutUser();
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

    private void initItem3() {
        dftralamat3.add("Pilih FM");
        dftralamat3.add("FM 1");
        dftralamat3.add("FM 2");
        dftralamat3.add("FM 3");
        dftralamat3.add("FM 4");
        dftralamat3.add("FM 5");
        dftralamat3.add("FM 6");
    }

    private void initItem() {
        dftralamat.add("Jenis Item");
        dftralamat.add("Jenis 1");
        dftralamat.add("Jenis 2");
        dftralamat.add("Jenis 3");
        dftralamat.add("Jenis 4");
        dftralamat.add("Jenis 5");
        dftralamat.add("Jenis 6");
    }

    private void initItem2() {
        dftralamat2.add("Item");
        dftralamat2.add("Item 1");
        dftralamat2.add("Item 2");
        dftralamat2.add("Item 3");
        dftralamat2.add("Item 4");
        dftralamat2.add("Item 5");
        dftralamat2.add("Item 6");
    }

}


