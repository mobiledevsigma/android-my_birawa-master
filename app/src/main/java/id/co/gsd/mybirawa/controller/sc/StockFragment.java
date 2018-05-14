package id.co.gsd.mybirawa.controller.sc;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment {


    //Variable Listview
    ListView listmenu;
    private FloatingActionButton fab;

    SessionManager session;

    ArrayList<HashMap<String,String>> listData;
    //ArrayList<dataModel> listData;
    private static Adapter_request adapter;
     Typeface font,fontbold;

    public StockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Bold.otf");

        session = new SessionManager(getActivity());
        TextView toolbar = (TextView) view.findViewById(R.id.complain);
        toolbar.setTypeface(fontbold);

        listmenu = (ListView) view.findViewById(R.id.list_stock);

//        Adapter_complain adapter = new Adapter_complain(getActivity(), listData);
//        listmenu.setAdapter(adapter);

        ImageView goto_menu = (ImageView) view.findViewById(R.id.img_complain);
        goto_menu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(), HistorycomplainActivity.class);
                getActivity().startActivity(i);
            }
        });

        showList();

        return view;
    }

    private void showList() {
        // refreshLayout.setRefreshing(true);
        final String REQUEST_TAG = "get request news";

        String userId = session.getIdUnit();
        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.SERVER+"api_data/scListComplaintArea/"+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list_complaint");
                            listData = new ArrayList<HashMap<String, String>>();
                            System.out.println("liat " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                HashMap<String,String> temp = new HashMap<String, String>();

                                JSONObject object = jsonArray.getJSONObject(i);
                                temp.put("id_order_sc",object.getString("id_order_sc")) ;
                                temp.put("order_sc_number",object.getString("order_sc_number")) ;
                                temp.put("id_user_fm",object.getString("id_user_fm")) ;
                                temp.put("id_user_area",object.getString("id_user_area")) ;
                                temp.put("order_date_area",object.getString("order_date_area")) ;
                                temp.put("id_user_pusat",object.getString("id_user_pusat")) ;
                                temp.put("order_date_pusat",object.getString("order_date_pusat")) ;
                                temp.put("id_user_receive",object.getString("id_user_receive")) ;
                                temp.put("received_date_fm",object.getString("received_date_fm")) ;
                                temp.put("status_order",object.getString("status_order")) ;
                                temp.put("evidence_order",object.getString("evidence_order")) ;
                                temp.put("order_date_fm",object.getString("order_date_fm")) ;
                                temp.put("id_alamat",object.getString("id_alamat")) ;
                                temp.put("id_user_mitra",object.getString("id_user_mitra")) ;
                                temp.put("cp_driver",object.getString("cp_driver")) ;
                                temp.put("delivery_date_pusat",object.getString("delivery_date_pusat")) ;
                                temp.put("delivery_date_mitra",object.getString("delivery_date_mitra")) ;
                                temp.put("approve_date_mitra",object.getString("approve_date_mitra")) ;
                                temp.put("status_complaint",object.getString("status_complaint")) ;
                                temp.put("notes",object.getString("notes")) ;
                                temp.put("id_user_order",object.getString("id_user_order")) ;
                                temp.put("order_date",object.getString("order_date")) ;
                                temp.put("nama",object.getString("nama")) ;
                                temp.put("status_name",object.getString("status_name")) ;
                                listData.add(temp);
                            }

                            adapter = new Adapter_request(getActivity(), listData);
                            listmenu.setAdapter(adapter);

                            listmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent it = new Intent(getContext(), Detail_complain.class);
                                    getContext().startActivity(it);
                                }
                            });
                            listmenu.invalidateViews();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //refreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, REQUEST_TAG);
    }


}
