package id.co.gsd.mybirawa.controller.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.main.LoginActivity;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class ProfileFragment extends Fragment {

    private SessionManager session;
    private TextView tv_name;
    private TextView tv_role;
    private TextView tv_unit;
    private TextView tv_area;
    private LinearLayout lay_faq;
    private LinearLayout lay_policy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_name = v.findViewById(R.id.tv_profile_name);
        tv_role = v.findViewById(R.id.tv_profile_role);
        tv_unit = v.findViewById(R.id.tv_profile_unit);
        tv_area = v.findViewById(R.id.tv_profile_area);
        lay_faq = v.findViewById(R.id.lay_faq);
        lay_policy = v.findViewById(R.id.lay_policy);

        session = new SessionManager(getActivity());

        tv_name.setText(session.getName());
        tv_role.setText(session.getRoleName());
        tv_area.setText("Area : " + session.getAreaName());
        tv_unit.setText("Unit : " + session.getNamaUnit());

        lay_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ConstantUtils.URL.SERVER+"faq_page"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        lay_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ConstantUtils.URL.SERVER+"privacy_page"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return v;
    }
}
