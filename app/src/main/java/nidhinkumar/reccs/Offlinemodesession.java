package nidhinkumar.reccs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by M.S. Venugopal on 28-05-2016.
 */
public class Offlinemodesession {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Offlinemode";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsUpdatedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_LASTSEEN = "offline";

    // Email address (make variable public to access from outside)


    // Constructor
    public Offlinemodesession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String lastseen){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_LASTSEEN, lastseen);

        // Storing email in pref


        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
          //  Intent i = new Intent(_context, LoginPAge.class);
            // Closing all the Activities
         //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
        //    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
          //  _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_LASTSEEN, pref.getString(KEY_LASTSEEN, null));

        // user email id


        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
