package nidhinkumar.reccs;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by M.S. Venugopal on 28-05-2016.
 */
public class ConnectionClass {
    @SuppressLint("NewApi")
    public Connection CONN()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://MYSQL5015.HostBuddy.com/db_9f45d2_penwp", "9f45d2_penwp", "fzQFZ6cuceS7");
        }
        catch (SQLException se)
        {
            Log.e("ERRO", se.getMessage());
            se.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            Log.e("ERRO", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}
