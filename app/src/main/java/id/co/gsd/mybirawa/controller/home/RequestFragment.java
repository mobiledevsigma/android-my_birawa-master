package id.co.gsd.mybirawa.controller.home;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;

public class RequestFragment extends Fragment {

    private ImageView btn_history;
    private ImageView btn_logout;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
       // font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Light.otf");
        //fontbold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Bold.otf");

        TextView toolbar = (TextView) view.findViewById(R.id.request);
     //   toolbar.setTypeface(fontbold);

     //   listmenu = (ListView) view.findViewById(R.id.list_request);


//        Adapter_request adapter = new Adapter_request(getActivity(), no, tanggal, fm, item, jmlitem, status);
//        listmenu.setAdapter(adapter);

//        ImageView goto_menu = (ImageView) view.findViewById(R.id.imageView7);
//        goto_menu.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                Intent i = new Intent(getActivity(), HistoryActivity.class);
//                getActivity().startActivity(i);
//            }
//        });
//
//        logout = (ImageView) view.findViewById(R.id.logouts);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Creating the instance of PopupMenu
//                PopupMenu popup = new PopupMenu(getActivity(), logout);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater()
//                        .inflate(R.menu.menu2, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        new AlertDialog.Builder(getActivity())
//                                .setMessage("Are you sure you want to logout?")
//                                .setCancelable(false)
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        System.exit(0);
//                                    }
//                                })
//                                .setNegativeButton("No", null)
//                                .show();
//                        return true;
//
//                    }
//                });
//
//                popup.show();
//            }
//        });

        return view;
    }
}
