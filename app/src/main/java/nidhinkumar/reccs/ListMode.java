package nidhinkumar.reccs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by M.S. Venugopal on 21-05-2016.
 */
public class ListMode extends AppCompatActivity{
    private String  USERID_URL;
    Toolbar toolm;
    ListView listView;
    String seen,offline,imagestr,iduser;
    LoginSession loginSession;
    String empty="List is empty";
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    AlertDialog alert;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODES=2;
    private List<ReceiptMov> movielist = new ArrayList<ReceiptMov>();

    private View view;
  //  ProgressDialog prgDialog; oldone
    LinearLayout refresh;
    byte[] img;
    DatabaseHandler db;
    ProgressBar pg;
    ImageListAdapter imageListAdapter;
    CustomlistJsonadapter customlistJsonadapter;
    TextView tvlastseen,offlinemode;
    Lastupdatesession usession;
    Offlinemodesession offlinemodesession;
    Useridsession useridsession;
    private static final String PREF_FIRSTLAUNCH_HELP = "helpactlist";
    private boolean helpDisplayed = false;
    private static final String TAG = ListMode.class.getSimpleName();
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }


    }
    private boolean checkstoragePermission(){
        int results = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (results == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmain);
        showHelpForFirstLaunch();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        toolm=(Toolbar)findViewById(R.id.addtoolbar);
        view=findViewById(R.id.list_permission);
        toolm.setTitle("Expenses");
        toolm.setNavigationIcon(R.drawable.front);

        setSupportActionBar(toolm);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListMode.this, Options.class);
                startActivity(i);
                ListMode.this.finish();
            }
        });

        offlinemodesession=new Offlinemodesession(getApplicationContext());
        useridsession=new Useridsession(getApplicationContext());
        loginSession=new LoginSession(getApplicationContext());
        usession=new Lastupdatesession(getApplicationContext());
        db=new  DatabaseHandler(getApplicationContext());
        listView=(ListView)findViewById(R.id.listmain);
        offlinemode=(TextView)findViewById(R.id.offlinestatus);
        refresh=(LinearLayout)findViewById(R.id.offlinemode);
        pg=(ProgressBar)findViewById(R.id.progressBar);
        pg.setVisibility(View.INVISIBLE);
        tvlastseen=(TextView)findViewById(R.id.updatestatus);
        HashMap<String, String> users = useridsession.getUserDetails();
        iduser=users.get(Useridsession.KEY_USERID);
        USERID_URL = "http://balajee2777-001-site1.1tempurl.com/backup-07032016/Receiptphp/useridonlinedet.php?userid="+iduser+"";
        if(usession.isLoggedIn()){
            HashMap<String, String> user = usession.getUserDetails();
            seen=user.get(Lastupdatesession.KEY_LASTSEEN);
            tvlastseen.setText(seen);
        }else{
            tvlastseen.setVisibility(View.INVISIBLE);
        }
        if(offlinemodesession.isLoggedIn()){
            HashMap<String, String> user = offlinemodesession.getUserDetails();
            offline=user.get(Offlinemodesession.KEY_LASTSEEN);
            offlinemode.setText(offline);

        }else{
            offlinemode.setVisibility(View.INVISIBLE);
            if(checkPermission()){
                Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                if(checkstoragePermission()) {
                     onlinemode();
                    customlistJsonadapter = new CustomlistJsonadapter(this, movielist);
                    listView.setAdapter(imageListAdapter);
                    if (isInternetPresent) {
                        syncSQLiteMySQLDB();
                    } else {
                        neti();
                    }
                }else{
                    Snackbar.make(view,"Please give access to write on your external storage.",Snackbar.LENGTH_LONG).show();
                    requestStoragePermission();

                }
            }else{
                Snackbar.make(view,"Please give access to read your phone state.",Snackbar.LENGTH_LONG).show();
                requestPermission();
            }
        }
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
     //  List<String> lables = db.getAllLabels();
      ArrayList<ImagelistItems>list=new ArrayList<ImagelistItems>();
         list=db.getAllLabels();
         imageListAdapter = new ImageListAdapter(
               ListMode.this, list);
        if (loginSession.isLoggedIn()) {
            loginSession.checkLogin();
            listView.setAdapter(imageListAdapter);
            listView.setEmptyView(findViewById(R.id.empty));

            Toast.makeText(getApplicationContext(), db.getSyncStatus(), Toast.LENGTH_LONG).show();
        }else{
            Intent i=new Intent(ListMode.this,LoginPAge.class);
            startActivity(i);
            ListMode.this.finish();
        }


       refresh.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(checkPermission()){
                   if(checkstoragePermission()) {
                       Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                       if (isInternetPresent) {
                           syncSQLiteMySQLDB();
                       } else {
                           neti();
                       }
                   }else{
                       Snackbar.make(view,"Please give access to write on your external storage.",Snackbar.LENGTH_LONG).show();
                       requestStoragePermission();

                   }
               }else{
                   Snackbar.make(view,"Please give access to read your phone state.",Snackbar.LENGTH_LONG).show();
                   requestPermission();
               }
           }
       });

    }

    private void showHelpForFirstLaunch() {
        helpDisplayed = getPreferenceValue(PREF_FIRSTLAUNCH_HELP, false);
        if (!helpDisplayed) {
            showHelp();
            savePreference(PREF_FIRSTLAUNCH_HELP, true);
        }else if(helpDisplayed){
            return;
        }
    }
    private void showHelp() {
        final View instructionsContainer = findViewById(R.id.container_help);
        instructionsContainer.setVisibility(View.VISIBLE);
        instructionsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionsContainer.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean getPreferenceValue(String key, boolean defaultValue) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    private void onlinemode() {
        endoutjsonresult();
    }


    private void endoutjsonresult() {
        JsonArrayRequest movieReq = new JsonArrayRequest(USERID_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                imagestr=obj.getString("mimage");
                                img= Base64.decode(imagestr, Base64.DEFAULT);
                                ReceiptMov  movie = new ReceiptMov(img,obj.getString("mname"),obj.getString("mdate"),obj.getString("mamount"),obj.getString("mpaid"),obj.getString("mcate"),obj.getString("mcomment"));
                                //ImagelistItems movie=new ImagelistItems(img,obj.getString("mname"),obj.getString("mdate"),obj.getString("mamount"),obj.getString("mpaid"),obj.getString("mcate"),obj.getString("mcomment"));

                                //movie.setMimage(obj.getString("mimage").getBytes());
                                movie.setMimage(img);
                                movie.setMerchantname(obj.getString("mname"));
                                movie.setMerdate(obj.getString("mdate"));
                                movie.setMeramount(obj.getString("mamount"));
                                movie.setMerpaid(obj.getString("mpaid"));
                                movie.setMercategory(obj.getString("mcate"));
                                movie.setMecomment(obj.getString("mcomment"));
                                // movie.setImage(img);
                                // movie.setMerchantname(obj.getString("mname"));
                                // movie.setPaidon(obj.getString("mdate"));
                                 //movie.setAmount(obj.getString("mamount"));
                                 // movie.setPaymmode(obj.getString("mpaid"));
                                 // movie.setCategory(obj.getString("mcate"));
                                 // movie.setComment(obj.getString("mcomment"));
                                movielist.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        // imageListAdapter.notifyDataSetChanged();
                          //imageListAdapter=new ImageListAdapter(this,imageList);
                          listView.setAdapter(customlistJsonadapter);
                        //listView.setAdapter(customlistJsonadapter);
                       // customlistJsonadapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //hidePDialog();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("userid",iduser);
                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
            SearchManager searchManager = (SearchManager) ListMode.this.getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = null;

            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(ListMode.this.getComponentName()));

            }
            return true;
        }
        if(id==R.id.action_add){

            Intent i=new Intent(ListMode.this,CameraActivity.class);
            startActivity(i);
        }
        if(id==R.id.sync){
            if(checkPermission()){
                Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                if(checkstoragePermission()) {
                    if (isInternetPresent) {
                        syncSQLiteMySQLDB();
                    } else {
                        neti();
                    }
                }else{
                    Snackbar.make(view,"Please give access to write on your external storage.",Snackbar.LENGTH_LONG).show();
                    requestStoragePermission();
                }
            }else{
                Snackbar.make(view,"Please give access to read your phone state.",Snackbar.LENGTH_LONG).show();
                requestPermission();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)){

            Toast.makeText(getApplicationContext(),"Give permission to check whether internet is of or on.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
        }
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            Toast.makeText(getApplicationContext(),"Give permission to write on your external storage.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }
    private void neti() {
        final LayoutInflater layoutInflater = LayoutInflater.from(ListMode.this);
        final View promptView = layoutInflater.inflate(R.layout.lost, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListMode.this);
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

    public void syncSQLiteMySQLDB(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<ImagelistItems>userList =  db.getAllLabels();
        if(userList.size()!=0){
            if(db.dbSyncCount() != 0){
             //   prgDialog.show();
                pg.setVisibility(View.VISIBLE);
                params.put("usersJSON", db.composeJSONfromSQLite());
                client.post("http://balajee2777-001-site1.1tempurl.com/backup-07032016/Receiptphp/insertuser.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        pg.setVisibility(View.INVISIBLE);
                     //   prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                System.out.println(obj.get("time"));
                                db.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                                tvlastseen.setText("Last Updated: "+obj.get("time").toString());
                                db.deleteUploaded(obj.get("status").toString());   //todelte the uploaded one
                            }
                            tvlastseen.setVisibility(View.VISIBLE);
                            usession.createLoginSession(tvlastseen.getText().toString());
                            Toast.makeText(getApplicationContext(), "Upload completed!", Toast.LENGTH_LONG).show();

                            Intent i=getIntent();
                            startActivity(i);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            pg.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                      //  prgDialog.hide();
                        if(statusCode == 404){
                            pg.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                        }else if(statusCode == 500){
                            pg.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                        }else{
                            pg.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }else{
                pg.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Data's are already uploaded!", Toast.LENGTH_LONG).show();

            }
        }else{
            pg.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();

        }
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
            case PERMISSION_REQUEST_CODES:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(view,"Permission Granted, Now you write on External Storage.",Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(view,"Permission Denied, You cannot write on external storage.",Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }
}
