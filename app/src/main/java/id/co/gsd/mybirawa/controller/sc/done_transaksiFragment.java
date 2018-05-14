package id.co.gsd.mybirawa.controller.sc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import id.co.gsd.mybirawa.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class done_transaksiFragment extends Fragment {


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
    String[] status2={
            "Status : Waiting for Approve",
            "Status : Waiting for Approve",
            "Status : Waiting for Approve",
            "Status : Waiting for Approve",
            "Status : Waiting for Approve",
            "Status : Waiting for Approve",
    };

    String[] status={
            "Status : Approved from Area",
            "Status : Approved from Area",
            "Status : Approved from Area",
            "Status : Approved from Pusat",
            "Status : Approved from Pusat",
            "Status : Approved from Pusat",
    };


    //Variable Listview
    ListView listmenu2;
    private static Adapter_history adapter;

    public done_transaksiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done_transaksi, container,false);
        listmenu2=(ListView) view.findViewById(R.id.list_done);

        Adapter_history adapter=new Adapter_history(getActivity(),no,tanggal,fm,item,jmlitem,status);
        listmenu2.setAdapter(adapter);


        return view;
    }

}
