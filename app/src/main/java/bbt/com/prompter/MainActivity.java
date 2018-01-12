package bbt.com.prompter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bbt.com.prompter.fragments.HistoryFragment;
import bbt.com.prompter.fragments.TemplateFragment;
import bbt.com.prompter.helper.UiHelper;

public class MainActivity extends AppCompatActivity {

    private android.widget.Button btnSend;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.TabLayout tabs;
    private android.support.v4.view.ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
//        initListeners();
    }

    private void init() {
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        UiHelper.initToolbar(MainActivity.this, toolbar, "Prompter");

        initFragments();
        tabs.setupWithViewPager(viewpager);
    }

    private void initFragments() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TemplateFragment(), "Templates");
        viewPagerAdapter.addFragment(new HistoryFragment(), "History");
        viewpager.setAdapter(viewPagerAdapter);
    }

    private void initListeners() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("8320552183", null, "Alaa vandra", null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent",
                            Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
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
