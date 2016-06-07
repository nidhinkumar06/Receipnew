package nidhinkumar.reccs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by M.S. Venugopal on 21-05-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "receiptExample";

    // Labels table name
    private static final String TABLE_LABELS = "labels";

    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "merchantname";
    private static final String KEY_DATE = "date";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_PAID = "paid";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_STATUS="status";
    private static final String KEY_UPLOADSTATUS = "uplaodstatus";
    private static final String KEY_USERID="userid";
    SQLiteDatabase db;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE + " BLOB,"
                + KEY_NAME + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_PAID + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_COMMENT + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_UPLOADSTATUS + " TEXT,"
                + KEY_USERID + " TEXT" + ");";

        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new lable into lables table
     * */
    public void insertLabel(byte[] imag,String merchant,String claendardate,String amount,String paymentmode,String expensetype,String coment,String status,String uploadstatus,String userid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE,imag);
        values.put(KEY_NAME, merchant);
        values.put(KEY_DATE, claendardate);
        values.put(KEY_AMOUNT,amount);
        values.put(KEY_PAID,paymentmode);
        values.put(KEY_CATEGORY,expensetype);
        values.put(KEY_COMMENT,coment);
        values.put(KEY_STATUS,status);
        values.put(KEY_UPLOADSTATUS,uploadstatus);
        values.put(KEY_USERID,userid);
        // Inserting Row
        db.insert(TABLE_LABELS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public ArrayList<ImagelistItems> getAllLabels(){
      //  List<String> labels = new ArrayList<String>();
        ArrayList<ImagelistItems>labels=new ArrayList<ImagelistItems>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LABELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ImagelistItems imagelistItems=new ImagelistItems();
                imagelistItems.setImage(cursor.getBlob(1));
                imagelistItems.setMerchantname(cursor.getString(2));
                imagelistItems.setPaidon("Paid on:"+cursor.getString(3));
                imagelistItems.setAmount(cursor.getString(4));
                imagelistItems.setCategory(cursor.getString(6));
                imagelistItems.setPaymmode(cursor.getString(5));
                imagelistItems.setComment(cursor.getString(7));
                imagelistItems.setStatus("Status:"+cursor.getString(9));

               // labels.add(cursor.getString(1));
               // labels.add(cursor.getString(2));
               // labels.add(cursor.getString(3));
              //  labels.add(cursor.getString(4));
              //  labels.add(cursor.getString(5));
              //  labels.add(cursor.getString(6));
                labels.add(imagelistItems);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> label;
        label = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM labels where " + KEY_STATUS + " = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            String encodedImage = Base64.encodeToString(cursor.getBlob(1), Base64.DEFAULT);
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(KEY_ID, cursor.getString(0));
                map.put(KEY_IMAGE, encodedImage);
                map.put(KEY_NAME, cursor.getString(2));
                map.put(KEY_DATE, cursor.getString(3));
                map.put(KEY_AMOUNT, cursor.getString(4));
                map.put(KEY_PAID, cursor.getString(5));
                map.put(KEY_CATEGORY, cursor.getString(6));
                map.put(KEY_COMMENT, cursor.getString(7));
                map.put(KEY_USERID,cursor.getString(10));
                label.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(label);
    }

    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "Wallet is Sync with cloud!";
        }else{
            msg = "Wallet needs to be Sync\n";
        }
        return msg;
    }

    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT  * FROM labels where " +KEY_STATUS+" = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void updateSyncStatus(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update labels set " + KEY_STATUS + " = '"+ status +"'," + KEY_UPLOADSTATUS + " = 'Uploaded' where " + KEY_ID + "="+"'"+ id +"'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    public void deleteUploaded(String status){
        SQLiteDatabase database=this.getWritableDatabase();
        String deleteQuery="Delete From labels where " + KEY_STATUS+ " = 'yes' ";
        Log.d("dquery",deleteQuery);
        database.execSQL(deleteQuery);
        database.close();
    }
}
