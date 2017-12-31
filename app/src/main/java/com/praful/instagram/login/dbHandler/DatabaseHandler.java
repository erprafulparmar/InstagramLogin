package com.praful.instagram.login.dbHandler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_login";

    private static final String TABLE_LOGIN = "table_login";
    private static final String TABLE_RELATIONSHIP = "table_relationship";

    //TODO TABLE LOGIN KEY...
    public static final String KEY_ID = "l_id";
    public static final String TAG_ID = "id";
    public static final String TAG_PROFILE_PICTURE = "profile_picture";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_FULL_NAME = "full_name";
    public static final String TAG_BIO = "bio";
    public static final String TAG_WEBSITE = "website";
    public static final String TAG_FOLLOWS = "follows";
    public static final String TAG_FOLLOWED_BY = "followed_by";
    public static final String TAG_MEDIA = "media";


    //TODO TABLE RELATIONSHIP KEY...
    public static final String F_KEY_ID = "f_id";
    public static final String F_ID = "id";
    public static final String F_PROFILE_PICTURE = "profile_picture";
    public static final String F_USERNAME = "username";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + TAG_ID + " TEXT,"
                + TAG_PROFILE_PICTURE + " TEXT,"
                + TAG_USERNAME + " TEXT,"
                + TAG_FULL_NAME + " TEXT,"
                + TAG_BIO + " TEXT,"
                + TAG_WEBSITE + " TEXT,"
                + TAG_FOLLOWS + " TEXT,"
                + TAG_FOLLOWED_BY + " TEXT,"
                + TAG_MEDIA + " TEXT" + ")";


        String CREATE_LOGIN_RELATIONSHIP = "CREATE TABLE " + TABLE_RELATIONSHIP + "("
                + F_KEY_ID + " INTEGER PRIMARY KEY,"
                + F_ID + " TEXT,"
                + F_PROFILE_PICTURE + " TEXT,"
                + F_USERNAME + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_LOGIN_RELATIONSHIP);
    }


    //TODO TABLE ADD LOGIN DATA...
    public boolean addLoggedUser(HashMap<String, String> hashMap)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TAG_ID, hashMap.get(TAG_ID));
        values.put(TAG_PROFILE_PICTURE, hashMap.get(TAG_PROFILE_PICTURE));
        values.put(TAG_USERNAME, hashMap.get(TAG_USERNAME));
        values.put(TAG_FULL_NAME, hashMap.get(TAG_FULL_NAME));
        values.put(TAG_BIO, hashMap.get(TAG_BIO));
        values.put(TAG_WEBSITE, hashMap.get(TAG_WEBSITE));
        values.put(TAG_FOLLOWS, hashMap.get(TAG_FOLLOWS));
        values.put(TAG_FOLLOWED_BY, hashMap.get(TAG_FOLLOWED_BY));
        values.put(TAG_MEDIA, hashMap.get(TAG_MEDIA));
        db.insert(TABLE_LOGIN, null, values);
        db.close();
        return true;
    }


    //TODO TABLE ADD RELATIONSHIP DATA...
    public void addRelationShipUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i=0;i<50;i++)
        {
            values.put(F_ID, i);
            values.put(F_PROFILE_PICTURE, "Username");
            values.put(F_USERNAME, "Username");
            db.insert(TABLE_RELATIONSHIP, null, values);
        }
        db.close();
    }




    //TODO TABLE GET LOGIN DATA...
    public HashMap<String, String> getLoggedUser()
    {
        HashMap<String, String> hashMap=new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do {
                hashMap.put(TAG_ID,cursor.getString(1));
                hashMap.put(TAG_PROFILE_PICTURE,cursor.getString(2));
                hashMap.put(TAG_USERNAME,cursor.getString(3));
                hashMap.put(TAG_FULL_NAME,cursor.getString(4));
                hashMap.put(TAG_BIO,cursor.getString(5));
                hashMap.put(TAG_WEBSITE,cursor.getString(6));
                hashMap.put(TAG_FOLLOWS,cursor.getString(7));
                hashMap.put(TAG_FOLLOWED_BY,cursor.getString(8));
                hashMap.put(TAG_MEDIA,cursor.getString(9));
            } while (cursor.moveToNext());
        }
        return hashMap;
    }


    //TODO TABLE GET LOGIN DATA...
    public ArrayList<HashMap<String, String>> getRelationShipUser()
    {
        ArrayList<HashMap<String, String>> arrayList=new ArrayList<>();
        HashMap<String, String> hashMap=new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RELATIONSHIP;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do {
                hashMap.put(F_ID,cursor.getString(1));
                hashMap.put(F_PROFILE_PICTURE,cursor.getString(2));
                hashMap.put(F_USERNAME,cursor.getString(3));
                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }


    //TODO TABLE DELETE LOGIN DATA...
    public void deleteLoggedUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_LOGIN);
        db.close();
    }

    //TODO TABLE DELETE RELATIONSHIP DATA...
    public void deleteRelationshipUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_RELATIONSHIP);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
    }
}
