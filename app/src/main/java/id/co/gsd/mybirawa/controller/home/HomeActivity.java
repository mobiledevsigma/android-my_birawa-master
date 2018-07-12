package id.co.gsd.mybirawa.controller.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.util.SessionManager;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private boolean doubleBackToExitPressedOnce = false;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private SessionManager session;
    private String roleID, imeiID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.navigation);

        session = new SessionManager(HomeActivity.this);
        roleID = session.getRoleId();

        final int role = Integer.parseInt(roleID);
        if (role <= 10) {
            //Add MenuItem with icon to Menu
            System.out.println("masuk atas");
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.getMenu().add(Menu.NONE, R.id.navigation_dashboard, Menu.NONE, "Checklist").setIcon(R.drawable.ic_home_black_24dp);
            bottomNavigationView.getMenu().add(Menu.NONE, R.id.navigation_punchlist, Menu.NONE, "Work Order").setIcon(R.drawable.search);
            bottomNavigationView.getMenu().add(Menu.NONE, R.id.navigation_profile, Menu.NONE, "Profile").setIcon(R.drawable.manuser);
        } else {
            //Add MenuItem with icon to Menu
            System.out.println("masuk bawah");
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.getMenu().add(Menu.NONE, R.id.navigation_dashboard, Menu.NONE, "Request").setIcon(R.drawable.ic_home_black_24dp);
            bottomNavigationView.getMenu().add(Menu.NONE, R.id.navigation_second, Menu.NONE, "Complain").setIcon(R.drawable.ic_complaint);
            bottomNavigationView.getMenu().add(Menu.NONE, R.id.navigation_profile, Menu.NONE, "Profile").setIcon(R.drawable.manuser);
        }

        Intent intent = getIntent();
        final int fragmentID = intent.getIntExtra("fragmentID", 0);

        if (fragmentID == 0) {
            fragmentManager = getSupportFragmentManager();
            fragment = new DashboardFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content, fragment).commit();
        } else {
            fragmentManager = getSupportFragmentManager();
            fragment = new PunchlistFragment();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content, fragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.navigation_punchlist);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_dashboard:
                                if (role >= 11) {
                                    fragment = new RequestFragment();
                                }else{
                                    fragment = new DashboardFragment();
                                }

                                break;
                            case R.id.navigation_punchlist:
                                if (roleID.equals("1")) {
                                    fragment = new PunchlistSpvFragment();
                                } else {
                                    fragment = new PunchlistFragment();
                                }
                                break;
                            case R.id.navigation_second:
                                fragment = new ProfileFragment();
                                break;
                            case R.id.navigation_profile:
                                fragment = new ProfileFragment();
                                break;
                        }

                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content, fragment).commit();
                        return true;
                    }
                });
    }

    public void navigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_punchlist);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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
        Toast.makeText(this, "Klik lagi untuk ke menu utama", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
