package nidhinkumar.reccs;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by M.S. Venugopal on 19-05-2016.
 */
public class CameraActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String CROP_IMAGE_FOLDER = "Skholingua/Crop Image";
    public static final String TAG = "CameraActivity";
    private static File file;
    private static final String filepath = Environment
            .getExternalStorageDirectory().getPath();
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final int GALLERY_PIC_REQUEST = 2222;
    public static final int CROP_PIC_REQUEST = 3333;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSIONWRITE_REQUEST_CODE=2;
    private static final int PERMISSIONREAD_REQUEST_CODE=3;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    Uri imageFileUri;
    Bitmap croppedBitmap;
    byte imageInByte[];
    LinearLayout cameraopt;
    ArrayList<String> numNames = new ArrayList<String>();
    ArrayList<String> payments = new ArrayList<String>();
    CustomListAdapter adapters;
    PaidListAdapter adappayments;
    Set<Currency> availableCurrenciesSet;
    List<Currency> availableCurrenciesList;
    ArrayAdapter<Currency> adapter;
    static final int DATE_PICKER_ID = 1111;
    Calendar c = Calendar.getInstance();
    private int year=c.get(Calendar.YEAR);
    private int month=c.get(Calendar.MONTH);
    private int day=c.get(Calendar.DAY_OF_MONTH);
    String symbol,userid,money;
    ImageView mImage;
    Useridsession useridsession;
    CurrenctSession currenctSession;
    TextView curry,category,date,paidwith,project;
    AlertDialog alert;
    EditText amt,merchantname,comment;
    Toolbar mtool;
    private ProgressDialog pDialog;
    private View mLayout;
    public static final int progress_bar_type = 0;
    private boolean checkPermission(){
    int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
    private boolean checkwritePermission(){
        int results = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (results == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
    private boolean checkreadPermission(){
        int resultz = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (resultz == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
    @Override
    public void onBackPressed() {
       Intent j=new Intent(CameraActivity.this,ListMode.class);
        startActivity(j);
        CameraActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameramain);
        mLayout = findViewById(R.id.sample_main_layout);
        mtool=(Toolbar)findViewById(R.id.uploadbar);
        mtool.setTitle("Add expense");
        setSupportActionBar(mtool);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtool.setNavigationIcon(R.drawable.front);
        mtool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CameraActivity.this, ListMode.class);
                startActivity(i);
                CameraActivity.this.finish();

            }
        });
        mImage=(ImageView)findViewById(R.id.imgview);
        cameraopt=(LinearLayout)findViewById(R.id.cameraoption);
        useridsession=new Useridsession(getApplicationContext());
        currenctSession=new CurrenctSession(getApplicationContext());
        HashMap<String, String> user = useridsession.getUserDetails();
        userid=user.get(useridsession.KEY_USERID);
        amt=(EditText)findViewById(R.id.tvamt);
        project=(TextView)findViewById(R.id.tvproject);
        //amt.setRawInputType(Configuration.KEYBOARD_12KEY);
        amt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        curry=(TextView)findViewById(R.id.tvcurrency);
        date=(TextView)findViewById(R.id.tvdate);
        String dates = new SimpleDateFormat("dd-M-yyyy").format(new Date());
        date.setText(dates);
        merchantname=(EditText)findViewById(R.id.tvmerchantname);
        paidwith=(TextView)findViewById(R.id.tvpaidwith);
        category=(TextView)findViewById(R.id.tvcategory);
        comment=(EditText)findViewById(R.id.tvcomment);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID);
            }
        });
        curry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currenctSession.isLoggedIn()){
                    HashMap<String, String> cur = currenctSession.getUserDetails();
                    money=cur.get(currenctSession.KEY_CURRENCY);
                    curry.setText(money);
                }else {
                    cur();
                }
            }
        });
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose();
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catz();
            }
        });
        paidwith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withpaid();
            }
        });
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        CameraActivity.this);
                alertDialogBuilder.setIcon(R.drawable.warning);
                alertDialogBuilder.setTitle("Warning");
                alertDialogBuilder
                        .setMessage("No projects available in project list,please add some projects first!")
                        .setCancelable(false)
                        .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        numNames.add("Air Travel");
        numNames.add("Delivery,Freight and express");
        numNames.add("Fuel costs");
        numNames.add("Gifts");
        numNames.add("Insurance");
        numNames.add("Lodging");
        numNames.add("Maintenance and repairs");
        numNames.add("Meals and entertainment");
        numNames.add("Office equipment and supplies");
        numNames.add("Other");
        numNames.add("Parking");
        numNames.add("Postage");
        numNames.add("Telephone&Internet");
        numNames.add("Taxi");
        adapters = new CustomListAdapter(this, numNames);
        payments.add("Cash");
        payments.add("Credit Card");
        payments.add("Debit Card");
        adappayments = new PaidListAdapter(this, payments);
    }

    private void withpaid() {
        final LayoutInflater layoutInflater = LayoutInflater.from(CameraActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.currency, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CameraActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);
        final android.support.v7.widget.Toolbar mtoolbar=(Toolbar)promptView.findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setTitle("Paid With");
        final ListView list=(ListView)promptView.findViewById(R.id.listcontent);
        list.setAdapter(adappayments);
        list.setDividerHeight(1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String amtpaid= payments.get(position);
                paidwith.setText(amtpaid);
                alert.dismiss();
            }

        });

        alert= alertDialogBuilder.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuupload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_upload){
            uploa();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void catz() {
        final LayoutInflater layoutInflater = LayoutInflater.from(CameraActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.currency, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CameraActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);
        final android.support.v7.widget.Toolbar mtoolbar=(Toolbar)promptView.findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setTitle("Category");
        final ListView list=(ListView)promptView.findViewById(R.id.listcontent);
        list.setAdapter(adapters);
        list.setDividerHeight(1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String catname= numNames.get(position);

               // String passingdata=catname+"_"+"email"+"#"+date.getText().toString();
               // Intent i=new Intent(ListAct.this,Cam.class);
               // Bundle b=new Bundle();
               // b.putString("key",passingdata);
               // i.putExtras(b);
              //  startActivity(i);
                //Intent i=new Intent(ListAct.this,Simpleupload.class);
                // Bundle b=new Bundle();
                // b.putString("key",passingdata);
                // i.putExtras(b);
                // startActivity(i);

               category.setText(catname);
                alert.dismiss();
            }

        });

        alert= alertDialogBuilder.create();
        alert.show();

    }

    public void saveCroppedImage() {
        // TODO Auto-generated method stub
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = getFile();
        imageInByte=bytes.toByteArray();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {

                mImage.setVisibility(View.VISIBLE);
                cropImage();

            } else if (requestCode == GALLERY_PIC_REQUEST && data != null) {

                mImage.setVisibility(View.VISIBLE);
                imageFileUri = data.getData();
               cropImage();
              //  saveCroppedImage();
            } else if (requestCode == CROP_PIC_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                croppedBitmap = (Bitmap) extras.getParcelable("data");
                mImage.setImageBitmap(croppedBitmap);
                cameraopt.setVisibility(View.INVISIBLE);
                mImage.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        saveCroppedImage();
                    }
                });
            }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT).show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    // Crop and resize the image for profile
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageFileUri, IMAGE_UNSPECIFIED);

        // Specify image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        // REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
        // identify the activity in onActivityResult() when it returns
        startActivityForResult(intent, CROP_PIC_REQUEST);
    }
    private void choose() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("Select an option!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {

                                if (checkreadPermission() && checkwritePermission()) {
                                    Snackbar.make(mLayout, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                                    Log.i(TAG,
                                            "CAMERA permission has already been granted. Displaying camera preview.");
                                    ContentValues contentValues = new ContentValues();
                                    contentValues
                                            .put(android.provider.MediaStore.Images.Media.DISPLAY_NAME,
                                                    "Skholingua Cropping");
                                    contentValues
                                            .put(android.provider.MediaStore.Images.Media.DESCRIPTION,
                                                    "Picture taken from " + getPackageName());
                                    imageFileUri = getContentResolver()
                                            .insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                    contentValues);
                                    Intent intent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                            imageFileUri);
                                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                                } else {
                                    Snackbar.make(mLayout, "Please request permission.", Snackbar.LENGTH_LONG).show();
                                    requestwritePermission();

                                }
                            }else{
                                // Camera permissions is already available, show the camera preview.
                                Log.i(TAG,
                                        "CAMERA permission has already been granted. Displaying camera preview.");
                                ContentValues contentValues = new ContentValues();
                                contentValues
                                        .put(android.provider.MediaStore.Images.Media.DISPLAY_NAME,
                                                "Skholingua Cropping");
                                contentValues
                                        .put(android.provider.MediaStore.Images.Media.DESCRIPTION,
                                                "Picture taken from " + getPackageName());
                                imageFileUri = getContentResolver()
                                        .insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                contentValues);
                                Intent intent = new Intent(
                                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                        imageFileUri);
                                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                            }

                        break;

                    case 1:
                        if (checkreadPermission() && checkwritePermission()) {
                            Snackbar.make(mLayout, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                            Intent galleryIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, GALLERY_PIC_REQUEST);
                        }else{
                            Snackbar.make(mLayout, "Please request permission.", Snackbar.LENGTH_LONG).show();
                            requestwritePermission();
                        }

                        break;
                    case 2:
                        dialog.dismiss();
                        break;

                }

            }
        });
        builder.show();
    }

    private void requestwritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

            Toast.makeText(getApplicationContext(),"Storage permission allows you to store in SD Card. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }




    private File getFile() {
        file = new File(filepath, CROP_IMAGE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        return new File(file + File.separator + System.currentTimeMillis()
                + ".jpg");
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month,day);
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setTitle("Uploading");
                pDialog.setMessage("Connecting with cloud...");
                pDialog.setIndeterminate(true);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setProgressNumberFormat(null);
                pDialog.setProgressPercentFormat(null);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            date.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));

        }
    };
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void cur() {
        final LayoutInflater layoutInflater = LayoutInflater.from(CameraActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.currency, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CameraActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);
        final android.support.v7.widget.Toolbar mtoolbar=(Toolbar)promptView.findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setTitle("Currency");
        final ListView list=(ListView)promptView.findViewById(R.id.listcontent);
        availableCurrenciesSet =
                Currency.getAvailableCurrencies();

        availableCurrenciesList = new ArrayList<Currency>(availableCurrenciesSet);
        adapter = new ArrayAdapter<Currency>(
                this,
                android.R.layout.simple_list_item_single_choice,
                availableCurrenciesList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Currency currency = (Currency) parent.getItemAtPosition(i);
                String currencyCode = currency.getCurrencyCode();
                String displayName = currency.getDisplayName();
                symbol = currency.getSymbol();
                curry.setText(symbol);
                currenctSession.createLoginSession(symbol);
                Toast.makeText(CameraActivity.this,
                        displayName + "\n" +
                                currencyCode + "\n" +
                                symbol,
                        Toast.LENGTH_LONG).show();
                alert.dismiss();
            }
        });

        alert= alertDialogBuilder.create();
        alert.show();
    }
    private void uploa() {


        String amount=amt.getText().toString();
        String  claendardate=date.getText().toString();
        String merchant=merchantname.getText().toString();
        String paymentmode=paidwith.getText().toString();
        String expensetype=category.getText().toString();
        String coment=comment.getText().toString();
        String uploadstatus="waiting to upload";
        String status="no";
        if(amount.isEmpty() && claendardate.isEmpty() && merchant.isEmpty() && paymentmode.isEmpty() && expensetype.isEmpty() && coment.isEmpty()){
            Toast.makeText(CameraActivity.this, "Pls Enter all fields", Toast.LENGTH_LONG).show();
        }else if(amount.isEmpty()){
            Toast.makeText(CameraActivity.this, "Enter the amount", Toast.LENGTH_LONG).show();
        }else if(claendardate.isEmpty()){
            Toast.makeText(CameraActivity.this, "Select the date", Toast.LENGTH_LONG).show();
        }else if(merchant.isEmpty()){
            Toast.makeText(CameraActivity.this, "Enter the merchant name", Toast.LENGTH_LONG).show();
        }else if(paymentmode.isEmpty()){
            Toast.makeText(CameraActivity.this, "Select the paymentmode", Toast.LENGTH_LONG).show();
        }else if(expensetype.isEmpty()){
            Toast.makeText(CameraActivity.this, "Select the Expense type", Toast.LENGTH_LONG).show();
        }else {
            DatabaseHandler db = new DatabaseHandler(
                    getApplicationContext());
            db.insertLabel(imageInByte,merchant,claendardate,amount,paymentmode,expensetype,coment,status,uploadstatus,userid);
            Intent i=new Intent(CameraActivity.this,ListMode.class);
            startActivity(i);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONWRITE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(mLayout,"Permission Granted, Now you can store datas.",Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(mLayout,"Permission Denied, You cannot store datas.",Snackbar.LENGTH_LONG).show();

                }
                break;
            case PERMISSIONREAD_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(mLayout,"Permission Granted, Now you can store datas.",Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(mLayout,"Permission Denied, You cannot store datas.",Snackbar.LENGTH_LONG).show();

                }
                break;

        }
    }
}
