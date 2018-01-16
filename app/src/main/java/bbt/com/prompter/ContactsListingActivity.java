package bbt.com.prompter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import bbt.com.prompter.adapters.ContactAdapter;
import bbt.com.prompter.model.ContactsModel;

public class ContactsListingActivity extends AppCompatActivity {
    private Context context;
    private List<ContactsModel> contacts;
    private android.support.v7.widget.RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private MaterialSearchView materialSearchView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_contacts_listing);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        init();
        initToolbar();
        initListener();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("Contacts");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ContactsListingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("onQueryTextSubmit", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (contacts.size() > 0) {

                }
                return false;
            }
        });
    }

    private void initAdapter() {
        contactAdapter = new ContactAdapter(context);
        rvContacts.setLayoutManager(new LinearLayoutManager(context));
        rvContacts.setAdapter(contactAdapter);

        contacts = getAllContacts();
        contactAdapter.setAndNotifyItems(context, contacts, new ContactAdapter.ContactClickListener() {
            @Override
            public void onItemClicked(String name) {
                Toast.makeText(context, "Name:" + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        initAdapter();
    }

    private List<ContactsModel> getAllContacts() {
        ContentResolver cr = context.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        ArrayList<ContactsModel> allContacts = new ArrayList<ContactsModel>();
        if (cursor.moveToFirst()) {
            try {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? and " + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?", new String[]{id, "%an%"}, null);
                        while (pCur.moveToNext()) {
                            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String imgUri = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                            allContacts.add(new ContactsModel(name, contactNumber, imgUri));
                            break;
                        }
                        pCur.close();
                    }

                } while (cursor.moveToNext());
            } catch (Exception e) {
                Log.e("Errrrrrr", e.getMessage());
            }
        }
        return allContacts;
    }
}
