package id.co.gsd.mybirawa.controller.sc;


import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    RelativeLayout logout;
    TextView faq;
    Typeface font,fontbold;

    SessionManager session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_sc, container, false);

//        faq = (TextView) view.findViewById(R.id.detail);
//        faq.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent harian = new Intent(getActivity() ,detailActivity.class);
//                startActivity(harian);
//            }
//        });

        ImageView img = view.findViewById(R.id.logoutButton);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                session.logoutUser();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        session = new SessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        TextView toolbar = (TextView) view.findViewById(R.id.toolbartext);
        toolbar.setTypeface(font);
        TextView nama = (TextView) view.findViewById(R.id.nama);
        nama.setText(session.getName());
        nama.setTypeface(font);
        TextView posisi = (TextView) view.findViewById(R.id.posisi);
        posisi.setText(session.getJabatan());
        posisi.setTypeface(font);
        TextView info = (TextView) view.findViewById(R.id.info);
        info.setTypeface(fontbold);
        TextView username = (TextView) view.findViewById(R.id.username);
        username.setText("Username : "+session.getUserName());
        username.setTypeface(font);
        TextView unit = (TextView) view.findViewById(R.id.unit);
        unit.setText("Unit : "+session.getNamaUnit());
        unit.setTypeface(font);
        TextView about = (TextView) view.findViewById(R.id.about);
        about.setTypeface(fontbold);
        TextView faq = (TextView) view.findViewById(R.id.faq);
        faq.setTypeface(font);
        TextView privacy = (TextView) view.findViewById(R.id.privacy);
        privacy.setTypeface(font);
        TextView detail = (TextView) view.findViewById(R.id.detail);
        detail.setTypeface(fontbold);





   return view;



    }

}
