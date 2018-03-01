package bbt.com.prompter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import bbt.com.prompter.adapters.ContactAdapter;
import bbt.com.prompter.dialogs.AddTemplateMsgDialog;
import bbt.com.prompter.helper.FunctionHelper;
import bbt.com.prompter.model.ContactTable;
import bbt.com.prompter.model.ContactsModel;

public class ContactsListingActivity extends AppCompatActivity {
    private Context context;
    private List<ContactsModel> contacts;
    private android.support.v7.widget.RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private MaterialSearchView materialSearchView;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initViews();

        init();
        initToolbar();
        initListener();
        ContactTable contactTable = new ContactTable();
        Log.e("AllContactData:", new Gson().toJson(contactTable.getAllContacts(context)));
    }


    private void initViews() {
        setContentView(R.layout.activity_contacts_listing);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);
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
            }
        });
    }

    private void initListener() {
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                new BackProcess(query).execute();
                contactAdapter.getFilter().filter(query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
//                new BackProcess(newText).execute();
                contactAdapter.getFilter().filter(query.trim());

                return false;
            }
        });


    }

    private void initAdapter() {
        contactAdapter = new ContactAdapter(context, new ContactAdapter.ContactClickListener() {
            @Override
            public void onItemClicked(String name, String phoneNo, String imgUri) {
                Toast.makeText(context, "Name Clicked: " + name, Toast.LENGTH_SHORT).show();
                new AddTemplateMsgDialog(context, ContactsListingActivity.this, name, phoneNo, imgUri);
            }
        });
        rvContacts.setLayoutManager(new LinearLayoutManager(context));
        rvContacts.setAdapter(contactAdapter);
        new BackProcess().execute();
    }

    private void init() {
        initProgress();
        initAdapter();
    }

    private List<ContactsModel> getAllContacts() {
        progressDialog.show();


        ContentResolver cr = context.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        ArrayList<ContactsModel> allContacts = new ArrayList<ContactsModel>();
        if (cursor.moveToFirst()) {
            try {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
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
        progressDialog.dismiss();
        return allContacts;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                materialSearchView.showSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private List<ContactsModel> getAllContacts(String queryString) {
        contacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        ArrayList<ContactsModel> allContacts = new ArrayList<ContactsModel>();
        if (cursor.moveToFirst()) {
            try {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? and " + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?", new String[]{id, "%" + queryString + "%"}, null);
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

    private void initProgress() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }


    private class BackProcess extends AsyncTask<String, Void, String> {
        private String queryName;
        private boolean isQuery;

        public BackProcess(String queryName) {
            this.queryName = queryName;
            isQuery = true;
        }

        public BackProcess() {
            isQuery = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isQuery) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if (isQuery) {
                List<ContactsModel> filteredContacts = new ArrayList<>();
            } else {
                contacts = getAllContacts();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            contactAdapter.setAndNotifyItems(contacts);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

}
