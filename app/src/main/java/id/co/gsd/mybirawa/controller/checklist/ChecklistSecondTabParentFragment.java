package id.co.gsd.mybirawa.controller.checklist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.adapter.ViewPagerAdapter;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

/**
 * Created by Biting on 1/8/2018.
 */

public class ChecklistSecondTabParentFragment extends Fragment {
    int selectedTabPosition;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist_tab_parent_second, container, false);
        viewPager = view.findViewById(R.id.my_viewpager);
        tabLayout = view.findViewById(R.id.my_tab_layout);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity(), viewPager, tabLayout);
        setEvents();
        //setHasOptionsMenu(true);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(adapter);
        return view;
    }

    private void setEvents() {
        tabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#FFFFFF"));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                viewPager.setCurrentItem(tab.getPosition());
                selectedTabPosition = viewPager.getCurrentItem();

                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
                tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
                tabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#FFFFFF"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }
        });
    }

    public void addPage(String pagename, String idDevice) {
        Bundle bundle = new Bundle();
        //bundle.putString("data", pagename); //ini awalnya
        bundle.putString(ConstantUtils.TAB_INTENT.TAG_PAGENAME, pagename); //ini percobaan
        bundle.putString(ConstantUtils.TAB_INTENT.TAG_DEVICEID, idDevice); //ini percobaan
        ChecklistSecondTabChildFragment fragmentChild = new ChecklistSecondTabChildFragment();
        fragmentChild.setArguments(bundle);
        //fragmentChild.idPerangkatTab = idDevice;
        adapter.addFrag(fragmentChild, pagename);
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > -1) tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(adapter.getCount() - 1);
        setupTabLayout();
    }

    public void setupTabLayout() {
        selectedTabPosition = viewPager.getCurrentItem();
        tabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#FFFFFF"));
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(adapter.getTabView(i));
        }
        tabLayout.getTabAt(0).select();
    }
}
