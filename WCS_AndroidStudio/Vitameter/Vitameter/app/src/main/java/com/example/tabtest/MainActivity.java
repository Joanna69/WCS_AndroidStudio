package com.example.tabtest;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //this.textView = (TextView) findViewById(R.id.textView);


        //this.textView.setText(" " + home1BT.getSeekBarValString());


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Home1BT mTab1;
        Happy mTab2;
        Graph3 mTab3;
        Graph2 mTab4;
        Graph4 mTab5;

        public SectionsPagerAdapter(MainActivity mainActivity, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    if (mTab1 == null) mTab1 = new Home1BT();
                    return mTab1;
                case 1:
                    if (mTab2 == null) mTab2 = new Happy();
                    return mTab2;
                case 2:
                    if (mTab3 == null) mTab3 = new Graph3();
                    return mTab3;
                case 3:
                    if (mTab4 == null) mTab4 = new Graph2();
                    return mTab4;
                case 4:
                    if (mTab5 == null) mTab5 = new Graph4();
                    return mTab5;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "Connect";
                case 1:
                    return "Happy";
                case 2:
                    return "Steps";
                case 3:
                    return "Light";
                case 4:
                    return "Air";
            }
            return null;
        }
    }
}

