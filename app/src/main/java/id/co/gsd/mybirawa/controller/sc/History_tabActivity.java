 package id.co.gsd.mybirawa.controller.sc;

 import android.os.Bundle;
 import android.support.design.widget.TabLayout;
 import android.support.v4.app.Fragment;
 import android.support.v4.app.FragmentManager;
 import android.support.v4.app.FragmentPagerAdapter;
 import android.support.v4.view.ViewPager;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.Toolbar;


 import java.util.ArrayList;
 import java.util.List;

 import id.co.gsd.mybirawa.R;
//
// import info.androidhive.materialtabs.R;
// import info.androidhive.materialtabs.fragments.OneFragment;
// import info.androidhive.materialtabs.fragments.ThreeFragment;
// import info.androidhive.materialtabs.fragments.TwoFragment;

 public class History_tabActivity extends AppCompatActivity {

     private Toolbar toolbar;
     private TabLayout tabLayout;
     private ViewPager viewPager;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_history_tab);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         viewPager = (ViewPager) findViewById(R.id.viewpager);
         setupViewPager(viewPager);

         tabLayout = (TabLayout) findViewById(R.id.tabs);
         tabLayout.setupWithViewPager(viewPager);

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     }
     @Override
     public boolean onSupportNavigateUp(){
         finish();
         return true;
     }

     private void setupViewPager(ViewPager viewPager) {
         ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
         adapter.addFragment(new last_transaksiFragment(), "Transaksi Terakhir");
         adapter.addFragment(new done_transaksiFragment(), "Transaksi Selesai");
         viewPager.setAdapter(adapter);
     }

     class ViewPagerAdapter extends FragmentPagerAdapter {
         private final List<Fragment> mFragmentList = new ArrayList<>();
         private final List<String> mFragmentTitleList = new ArrayList<>();

         public ViewPagerAdapter(FragmentManager manager) {
             super(manager);
         }

         @Override
         public Fragment getItem(int position) {
             return mFragmentList.get(position);
         }

         @Override
         public int getCount() {
             return mFragmentList.size();
         }

         public void addFragment(Fragment fragment, String title) {
             mFragmentList.add(fragment);
             mFragmentTitleList.add(title);
         }

         @Override
         public CharSequence getPageTitle(int position) {
             return mFragmentTitleList.get(position);
         }
     }
 }