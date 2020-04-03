package com.doi.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    boolean bool = false;

    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "DETAILS_.db";

    // Inspection Tables
    private static final String TABLE_DETAILS = "DETAILS";

    private static final String KEY_ID = "Id";
    private static final String Shopname = "Shopname";
    private static final String Ownername = "Ownername";
    private static final String Address="Address";
    private static final String mobile = "mobile";
    private static final String items = "items";
    private static final String District = "District";
    private static final String Tehsil = "Tehsil";
    private static final String Town = "Town";
    private static final String pincode = "pincode";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String address_updated = "address_updated";





    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERDATA_TABLE = "CREATE TABLE " + TABLE_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Shopname + " TEXT,"
                + Ownername + " TEXT,"
                + Address + " TEXT,"
                + mobile + " TEXT,"
                + items + " TEXT,"
                + District + " TEXT,"
                + Tehsil + " TEXT,"
                + Town + " TEXT,"
                + pincode + " TEXT,"
                + latitude + " TEXT,"
                + longitude + " TEXT,"
                + address_updated + " TEXT" + ")";





        db.execSQL(CREATE_USERDATA_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);

        // Create tables again
        onCreate(db);


    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addMouDetails(List<POJO> mou_Details) {
        SQLiteDatabase db = this.getWritableDatabase();


        for(int i=0; i<mou_Details.size(); i++){
            //mou_Details
            ContentValues values = new ContentValues();
            values.put(Shopname, mou_Details.get(i).getShopname());
            values.put(Ownername, mou_Details.get(i).getOwnername());
            values.put(Address, mou_Details.get(i).getAddress());
            values.put(mobile, mou_Details.get(i).getMobile());
            values.put(items, mou_Details.get(i).getItems());
            values.put(District, mou_Details.get(i).getDistrict());

            values.put(Tehsil, mou_Details.get(i).getTehsil());
            values.put(Town, mou_Details.get(i).getTown());
            values.put(pincode, mou_Details.get(i).getPincode());
            values.put(latitude, mou_Details.get(i).getLatitude());
            values.put(longitude, mou_Details.get(i).getLongitude());
            values.put(address_updated,mou_Details.get(i).getAddress_updated());


            db.insert(TABLE_DETAILS, null, values);
            // Log.e("Row Inserted==",Integer.toString(i)+ "\t "+values.toString());


        }


        db.close(); // Closing database connection



    }


    // // Getting the Complete Database in a List Attendance
    public List<POJO> GetAllData() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DETAILS ;  //+ " ORDER BY " + DATE_TIME + " DESC"
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  null);
        List<POJO> mou_details_list_db = new ArrayList<>();
        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            POJO md = new POJO();
            //Log.d(KEY_ID_DB, cursor.getString(0));
            md.setShopname(cursor.getString(1));
            md.setOwnername(cursor.getString(2));
            md.setAddress(cursor.getString(3));
            md.setMobile(cursor.getString(4));
            md.setItems(cursor.getString(5));
            md.setDistrict(cursor.getString(6));
            md.setTehsil(cursor.getString(7));
            md.setTown(cursor.getString(8));
            md.setPincode(cursor.getString(9));
            md.setLatitude(cursor.getString(10));
            md.setLongitude(cursor.getString(11));
            md.setAddress_updated(cursor.getString(12));

            mou_details_list_db.add(md);
        }
        db.close(); // Closing database connection
        return mou_details_list_db;

    }





    public boolean updateData(POJO offline_object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Shopname, offline_object.getShopname());
        values.put(Ownername, offline_object.getOwnername());
        values.put(Address, offline_object.getAddress());
        values.put(mobile, offline_object.getMobile());
        values.put(items, offline_object.getItems());
        values.put(District, offline_object.getDistrict());
        values.put(Tehsil,offline_object.getTehsil());
        values.put(Town,offline_object.getTown());
        values.put(pincode,offline_object.getPincode());
        values.put(latitude,offline_object.getLatitude());
        values.put(longitude,offline_object.getLongitude());
        values.put(address_updated,offline_object.getAddress_updated());
        db.update(TABLE_DETAILS, values, "Shopname = ? ",new String[] { offline_object.getShopname()});
        return true;
    }














    // Getting MOU Count
    public int getNoOfRowsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }





}
