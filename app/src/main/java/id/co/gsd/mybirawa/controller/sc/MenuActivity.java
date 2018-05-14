package id.co.gsd.mybirawa.controller.sc;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.BottomNavigationViewHelper;
import id.co.gsd.mybirawa.util.SessionManager;


public class MenuActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
//        Toolbar tlbr = (Toolbar) findViewById(R.id.tolbar);
//        setSupportActionBar(tlbr);
//        getSupportActionBar().show();
        fragmentManager = getSupportFragmentManager();

        session = new SessionManager(MenuActivity.this);

//        if(session.getRoleId().equals("12")){
//            fragment = new RequestFragment();
//        }else{
//            fragment = new RequestFragment();
//        }
        fragment = new RequestFragment();


        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, fragment).commit();


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        if(session.getRoleId().equals("12") || session.getRoleId().equals("11") || session.getRoleId().equals("14")){
            bottomNavigationView.inflateMenu(R.menu.menu_sc_bm);
        }else if( session.getRoleId().equals("98")){
            bottomNavigationView.inflateMenu(R.menu.menu_sc_area);
        }else{
            bottomNavigationView.inflateMenu(R.menu.menu_sc_area);
        }



        bottomNavigationView.setOnNavigationItemSelectedListener(
                 new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        System.out.println("id :" + item.getItemId());
                        switch (item.getItemId()) {

                            case R.id.area_sc_order:
                                fragment = new RequestFragment();
                                break;
                            case R.id.bm_sc_order:
                                fragment = new RequestFragment();
                                break;
                            case R.id.area_sc_complaint:
                                fragment = new ComplainFragment();
                                break;
                            case R.id.bm_sc_stock:
                                fragment = new UpdateStockFragment();
                                break;
                            case R.id.bm_sc_complaint:
                                fragment = new ComplainFragment();
                                break;
                            case R.id.menu_report:
                                fragment = new ReportFragment();
                                break;
                            case R.id.menu_profile:
                                fragment = new ProfileFragment();
                                break;

                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content, fragment).commit();
                        return true;
                    }
                });

    }

    //logout

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(session.getRoleId().equals("12") || session.getRoleId().equals("11") || session.getRoleId().equals("14")){
            getMenuInflater().inflate(R.menu.menu_sc_bm, menu);
        }else if( session.getRoleId().equals("98")){
            getMenuInflater().inflate(R.menu.menu_sc_area, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_sc_area, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new AlertDialog.Builder(this)
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


}

