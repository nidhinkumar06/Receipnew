package nidhinkumar.reccs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by M.S. Venugopal on 24-05-2016.
 */
public class LoginPAge extends AppCompatActivity{
    private static final String TAG = LoginPAge.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText email,password;
    Button addaccount,createaccount;
    TextView terms;
    EmailDatabaseHandler es;
    String demail,dpassword,did;
    LoginSession loginSession;
    ResultSet rs;
    String imei;
    Useridsession useridsession;
    ConnectionClass connectionClass;
    private View view;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    AlertDialog alert;
    private static final String LOGINALL_URL = "http://balajee2777-001-site1.1tempurl.com/backup-07032016/Receiptphp/reeclogin.php";

    private static final String LOGIN_URL = "http://balajee2777-001-site1.1tempurl.com/backup-07032016/Receiptphp/receregister.php";
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }


    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        view=findViewById(R.id.login_page);
        email=(EditText)findViewById(R.id.emailaddress);
        password=(EditText)findViewById(R.id.password);
        addaccount=(Button)findViewById(R.id.btnaddaccount);
        createaccount=(Button)findViewById(R.id.btncreateaccount);
        terms=(TextView)findViewById(R.id.tvtermscondition);
        connectionClass = new ConnectionClass();
        useridsession=new Useridsession(getApplicationContext());
        loginSession=new LoginSession(getApplicationContext());
        if(checkPermission()){
            Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
            TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imei=tm.getDeviceId();
        }else{
            Snackbar.make(view,"Please give access to read your phone state.",Snackbar.LENGTH_LONG).show();
            requestPermission();
        }

        addaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    String lemail = email.getText().toString();
                    String lpassword = password.getText().toString();
                    if (!isValidEmail(lemail)) {
                        email.setError("Invalid Email");
                    }
                    if (!isValidPassword(lpassword)) {
                        password.setError("Invalid Password");
                    }
                    if (lemail.isEmpty() && lpassword.isEmpty()) {
                        Snackbar.make(view, "Enter all details", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(LoginPAge.this, "Enter all details", Toast.LENGTH_LONG).show();

                    } else if (lemail.isEmpty()) {
                        Snackbar.make(view, "Enter email", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(LoginPAge.this, "Enter email", Toast.LENGTH_LONG).show();

                    } else if (lpassword.isEmpty()) {
                        Snackbar.make(view, "Enter password", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(LoginPAge.this, "Enter password", Toast.LENGTH_LONG).show();

                    } else {

                        Loginuser(lemail, lpassword);
                    }
                }else{
                    neti();
                }
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    String lemail = email.getText().toString();
                    String lpassword = password.getText().toString();
                    if (!isValidEmail(lemail)) {
                        email.setError("Invalid Email");
                    } else if (!isValidPassword(lpassword)) {
                        password.setError("Invalid Password");
                    } else if (lemail.isEmpty() && lpassword.isEmpty()) {
                        Snackbar.make(view, "Enter all details", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(LoginPAge.this, "Enter all details", Toast.LENGTH_LONG).show();

                    } else if (lemail.isEmpty()) {
                        Snackbar.make(view, "Enter email", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(LoginPAge.this, "Enter email", Toast.LENGTH_LONG).show();

                    } else if (lpassword.isEmpty()) {
                        Snackbar.make(view, "Enter password", Snackbar.LENGTH_LONG).show();
                        //  Toast.makeText(LoginPAge.this, "Enter password", Toast.LENGTH_LONG).show();

                    } else {

                        userLogin(lemail, lpassword, imei);
                    }
                }else{
                    neti();
                }
            }
        });
    }

    private void neti() {
        final LayoutInflater layoutInflater = LayoutInflater.from(LoginPAge.this);
        final View promptView = layoutInflater.inflate(R.layout.lost, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginPAge.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);
        final Button retry=(Button)promptView.findViewById(R.id.btnretry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                finish();
                startActivity(intent);
            }
        });
        alert= alertDialogBuilder.create();
        Window window = alert.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alert.show();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)){

            Toast.makeText(getApplicationContext(),"Give permission to check whether internet is of or on.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }
    private void Loginuser(final String lemail, final String lpassword) {
        class LoginClass extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginPAge.this, "Connecting to Server", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s.equalsIgnoreCase("success")) {
                    if (lemail.equals(email.getText().toString()) && lpassword.equals(password.getText().toString())) {
                        loginSession.createLoginSession(email.getText().toString(), password.getText().toString());
                        Intent intent = new Intent(LoginPAge.this, ListMode.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // intent.putExtra(USER_NAME, username);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(LoginPAge.this, "There is no account for this email address.Please create an account for it.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("lemail",params[0]);
                data.put("lpassword",params[1]);
                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(LOGINALL_URL,data);
                return result;
            }
        }
        LoginClass ulc = new LoginClass();
        ulc.execute(lemail,lpassword);

    }

    private void userLogin(final String lemail, final String lpassword, final String imei) {
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginPAge.this, "Connecting to Server", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                if(s.equalsIgnoreCase("success")) {
                    Connection con = connectionClass.CONN();
                    String querycmd = "SELECT id,resemail,respassword FROM `reclogin` WHERE resemail='"+lemail+"'";
                    try {
                        PreparedStatement statement = con.prepareStatement(querycmd);
                        rs = statement.executeQuery();
                        while (rs.next()) {
                             did= rs.getString("id");
                            demail=rs.getString("resemail");
                            dpassword = rs.getString("respassword");

                        }
                        useridsession.createLoginSession(did);
                    } catch (SQLException e) {

                    }
                    EmailDatabaseHandler  es=new EmailDatabaseHandler(getApplicationContext());
                    es.insertLabel(did,demail,dpassword);

                    loading.dismiss();
                    if (lemail.equals(email.getText().toString()) && lpassword.equals(password.getText().toString())) {
                        loginSession.createLoginSession(email.getText().toString(), password.getText().toString());
                        Intent intent = new Intent(LoginPAge.this, ListMode.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // intent.putExtra(USER_NAME, username);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    loading.dismiss();
                    Toast.makeText(LoginPAge.this, s, Toast.LENGTH_LONG).show();
                    email.setText("");
                    password.setText("");

                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("lemail",params[0]);
                data.put("lpassword",params[1]);
                data.put("imei",params[2]);
                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(LOGIN_URL,data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(lemail,lpassword,imei);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(view,"Permission Granted, Now you can check network status.",Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(view,"Permission Denied, You cannot check networkstatus.",Snackbar.LENGTH_LONG).show();

                }
                break;

        }
    }
}
