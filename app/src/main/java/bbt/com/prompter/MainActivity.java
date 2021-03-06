package bbt.com.prompter;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bbt.com.prompter.dialogs.SendMessageDialog;
import bbt.com.prompter.fragments.HistoryFragment;
import bbt.com.prompter.fragments.TemplateFragment;
import bbt.com.prompter.helper.AppConstants;
import bbt.com.prompter.helper.DeliverReceiver;
import bbt.com.prompter.helper.FunctionHelper;
import bbt.com.prompter.helper.SentReceiver;
import bbt.com.prompter.helper.UiHelper;

public class MainActivity extends AppCompatActivity {

    private android.widget.Button btnSend;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.TabLayout tabs;
    private android.support.v4.view.ViewPager viewpager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        askPermissions();
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
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.getStringExtra(AppConstants.INTENT_NAME) != null) {
            String name = intent.getStringExtra(AppConstants.INTENT_NAME);
            String no = intent.getStringExtra(AppConstants.INTENT_CONTACT);
            String msg = intent.getStringExtra(AppConstants.INTENT_MSG);
            new SendMessageDialog(context, name, msg, no);
        }
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


                    SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(01);
                    smsManager.sendTextMessage("9974686376", null, "Test Successful", null, null);

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

    private void askPermissions() {
        FunctionHelper.setPermission(context, new String[]{Manifest.permission.SEND_SMS
                , Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS
                , Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS
                , Manifest.permission.RECEIVE_SMS}, new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //do nothing
            }
        });

    }

}
