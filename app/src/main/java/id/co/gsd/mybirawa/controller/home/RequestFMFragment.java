package id.co.gsd.mybirawa.controller.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.main.LoginActivity;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class RequestFMFragment extends Fragment {

    private Toolbar toolbar;
    private ImageView btn_history;
    private ImageView btn_logout;
    private ListView listView;
    private Button btn_submit;
    private SessionManager session;
    private String imeiID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_fm, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        btn_history = view.findViewById(R.id.btn_history);
        btn_logout = view.findViewById(R.id.btn_logout);
        listView = view.findViewById(R.id.lv_request_item);
        btn_submit = (Button) view.findViewById(R.id.btn_submit_req);

        session = new SessionManager(getActivity());
        imeiID = session.getImei();

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Anda yakin akan logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                session.logoutUser();
//                                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                                startActivity(intent);
                                logout(imeiID);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
//                Intent i = new Intent(getContext(), RequestConfirmation.class);
//                getActivity().startActivity(i);
            }
        });

        return view;
    }

    private void logout(String imei) {
        final String REQUEST_TAG = "get request";

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.LOGOUT + imei,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String logout = jsonObject.getString("logout");

                            if (logout.equals("Success")) {
                                session.logoutUser();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity().getBaseContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity().getBaseContext()).addToRequestQueue(request, REQUEST_TAG);
    }
}
