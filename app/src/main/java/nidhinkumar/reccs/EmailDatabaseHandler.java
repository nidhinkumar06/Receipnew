package nidhinkumar.reccs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by M.S. Venugopal on 27-05-2016.
 */
public class EmailDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "emailRegistration";

    // Labels table name
    private static final String TABLE_LABELS = "sticker";

    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERID="uid";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    SQLiteDatabase db;
    public EmailDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERID + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ");";

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
    public void insertLabel(String uid,String email,String passwaord){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERID,uid);
        values.put(KEY_EMAIL,email);
        values.put(KEY_PASSWORD, passwaord);
        // Inserting Row
        db.insert(TABLE_LABELS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public ArrayList<EmaillistItems> getAllLabels(){
        //  List<String> labels = new ArrayList<String>();
        ArrayList<EmaillistItems>labels=new ArrayList<EmaillistItems>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LABELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                EmaillistItems emaillistItems=new EmaillistItems();
                emaillistItems.setUserid(cursor.getString(1));
                emaillistItems.setMailername(cursor.getString(2));
               // imagelistItems.setStatus(cursor.getString(5));

                labels.add(emaillistItems);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

}
