package bbt.com.prompter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bbt.com.prompter.DBHelper.DataBaseHandler;

/**
 * Created by anish on 07-02-2018.
 */

public class ContactTable {
    public long contactId;
    public String number;
    public String name;
    public String template;
    public String notifyTime;
    public long createdDateInt;
    public long updatedDateInt;
    //total field 7 (0 to 6)
    private boolean isIdManual = false;

    public static final String tableName = ContactTable.class.getSimpleName();

    public static final String createContactTableQuery =
            "CREATE TABLE ContactTable ( contactId INTEGER PRIMARY KEY AUTOINCREMENT, number INTEGER, name TEXT, template TEXT, notifyTime TEXT, createdDateInt INTEGER,updatedDateInt INTEGER)";

    public ContactTable() {
    }

    public ContactTable(long contactId, String number, String name, String template, String notifyTime, long createdDateInt, long updatedDateInt) {
        this.contactId = contactId;
        this.number = number;
        this.name = name;
        this.template = template;
        this.notifyTime = notifyTime;
        this.createdDateInt = createdDateInt;
        this.updatedDateInt = updatedDateInt;

        isIdManual = true;
    }

    //without id
    public ContactTable(String number, String name, String template, String notifyTime, long createdDateInt, long updatedDateInt) {
        this.number = number;
        this.name = name;
        this.template = template;
        this.notifyTime = notifyTime;
        this.createdDateInt = createdDateInt;
        this.updatedDateInt = updatedDateInt;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public long getCreatedDateInt() {
        return createdDateInt;
    }

    public void setCreatedDateInt(long createdDateInt) {
        this.createdDateInt = createdDateInt;
    }

    public long getUpdatedDateInt() {
        return updatedDateInt;
    }

    public void setUpdatedDateInt(long updatedDateInt) {
        this.updatedDateInt = updatedDateInt;
    }


    //---------- Crud methods below------------------------------

    public void addContact(ContactTable contactTable, Context context) {
        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        if (isIdManual) {
            values.put("contactId", contactTable.getContactId());
        }
        values.put("number", contactTable.getNumber());
        values.put("name", contactTable.getName());
        values.put("template", contactTable.getTemplate());
        values.put("notifyTime", contactTable.getNotifyTime());
        values.put("createdDateInt", contactTable.getCreatedDateInt());
        values.put("updatedDateInt", contactTable.getUpdatedDateInt());

        // Inserting Row
        db.insert(tableName, null, values);
        db.close(); // Closing database connection
    }


    // Getting single contact
    public ContactTable getContact(int id, Context context) {
        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from ContactTable where contactId =" + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        ContactTable contact = new ContactTable(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(4), cursor.getLong(5));
        // return contact
        return contact;
    }


    public List<ContactTable> getAllContacts(Context context) {
        List<ContactTable> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactTable contact = new ContactTable();
                contact.setContactId(Integer.parseInt(cursor.getString(0)));
                contact.setNumber(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setTemplate(cursor.getString(3));
                contact.setNotifyTime(cursor.getString(4));
                contact.setCreatedDateInt(cursor.getLong(5));
                contact.setUpdatedDateInt(cursor.getLong(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

}
