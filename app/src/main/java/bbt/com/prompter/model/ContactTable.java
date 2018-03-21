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
    private long contactId;
    private String number;
    public String name;
    private String template;
    private String notifyTime;
    private long createdDateInt;
    private long updatedDateInt;
    private long isDaily;
    private String imgUri;
    //total field 9 (0 to 8)
//    private static boolean isIdManual = false;

    public static final String tableName = ContactTable.class.getSimpleName();

    public static final String createContactTableQuery =
            "CREATE TABLE ContactTable ( contactId INTEGER PRIMARY KEY AUTOINCREMENT, number INTEGER, name TEXT, template TEXT, notifyTime TEXT, createdDateInt INTEGER,updatedDateInt INTEGER,isDaily INTEGER, imgUri TEXT)";

    public ContactTable() {
    }

    public ContactTable(long contactId, String number, String name, String template, String notifyTime, long createdDateInt, long updatedDateInt, long isDaily, String imgUri) {
        this.contactId = contactId;
        this.number = number;
        this.name = name;
        this.template = template;
        this.notifyTime = notifyTime;
        this.createdDateInt = createdDateInt;
        this.updatedDateInt = updatedDateInt;
        this.isDaily = isDaily;
        this.imgUri = imgUri;
//        isIdManual = true;
    }

    //without id
    public ContactTable(String number, String name, String template, String notifyTime, long createdDateInt, long updatedDateInt, long isDaily, String imgUri) {
        this.number = number;
        this.name = name;
        this.template = template;
        this.notifyTime = notifyTime;
        this.createdDateInt = createdDateInt;
        this.updatedDateInt = updatedDateInt;
        this.isDaily = isDaily;
        this.imgUri = imgUri;
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

    public long getIsDaily() {
        return isDaily;
    }

    public void setIsDaily(long isDaily) {
        this.isDaily = isDaily;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
    //---------- Crud methods below------------------------------

    public static void addContact(ContactTable contactTable, Context context) {
        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        /*if (isIdManual) {
            values.put("contactId", contactTable.getContactId());
        }*/
        values.put("number", contactTable.getNumber());
        values.put("name", contactTable.getName());
        values.put("template", contactTable.getTemplate());
        values.put("notifyTime", contactTable.getNotifyTime());
        values.put("createdDateInt", contactTable.getCreatedDateInt());
        values.put("updatedDateInt", contactTable.getUpdatedDateInt());
        values.put("isDaily", contactTable.getIsDaily());
        values.put("imgUri", contactTable.getImgUri());

        // Inserting Row
        db.insert(tableName, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public static void updateContact(ContactTable contactTable, Context context) {
        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number", contactTable.getNumber());
        values.put("name", contactTable.getName());
        values.put("template", contactTable.getTemplate());
        values.put("notifyTime", contactTable.getNotifyTime());
        values.put("createdDateInt", contactTable.getCreatedDateInt());
        values.put("updatedDateInt", contactTable.getUpdatedDateInt());
        values.put("isDaily", contactTable.getIsDaily());
        values.put("imgUri", contactTable.getImgUri());

        // updating row
        db.update(tableName, values, "contactId" + " = ?",
                new String[]{String.valueOf(contactTable.getContactId())});
        db.close(); // Closing database connection
    }

    // Getting single contact
    public static ContactTable getContact(int id, Context context) {
        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from ContactTable where contactId =" + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ContactTable contact = null;
        if (cursor != null && cursor.getCount() != 0) {
            contact = new ContactTable(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getLong(5), cursor.getLong(6), cursor.getLong(7), cursor.getString(8));
            // return contact
        }
        return contact;
    }

    //fetch record of given number
    public static ContactTable getContactWithNumber(String number, Context context) {
        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from ContactTable where number =" + "'" + number + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        ContactTable contact = null;
        if (cursor != null && cursor.getCount() != 0) {
            contact = new ContactTable(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getLong(5), cursor.getLong(6), cursor.getLong(7), cursor.getString(8));
        }
        // return contact
        return contact;
    }

    public static List<ContactTable> getAllContacts(Context context) {
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
                contact.setIsDaily(cursor.getLong(7));
                contact.setImgUri(cursor.getString(8));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Deleting single contact
    public static void deleteContact(long contactTableId, Context context) {
        SQLiteDatabase db = new DataBaseHandler(context).getWritableDatabase();
        db.delete(ContactTable.tableName, "contactId = ?",
                new String[]{String.valueOf(contactTableId)});
        db.close();
    }




}
