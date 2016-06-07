package nidhinkumar.reccs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by M.S. Venugopal on 27-05-2016.
 */
public class Options extends AppCompatActivity{
   ListView email;
    Toolbar toolm;
    TextView account;
    LoginSession loginSession;
    String mailerdetails;
    CustomemailListAdapter adapter;
    Switch myswitch;
    LinearLayout currecnys;
    TextView mone;
    Set<Currency> availableCurrenciesSet;
    List<Currency> availableCurrenciesList;
    ArrayAdapter<Currency> adapters;
    Offlinemodesession offlinemodesession;
    CurrenctSession currenctSession;
    String symbol,money;
    AlertDialog alert;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        offlinemodesession=new Offlinemodesession(getApplicationContext());
        loginSession=new LoginSession(getApplicationContext());
        currenctSession=new CurrenctSession(getApplicationContext());
        HashMap<String,String>usermail=loginSession.getUserDetails();
        mailerdetails=usermail.get(LoginSession.KEY_NAME);
        email=(ListView)findViewById(R.id.listaccount);
        account=(TextView)findViewById(R.id.addaccount);
        mone=(TextView)findViewById(R.id.money);
        currecnys=(LinearLayout)findViewById(R.id.currency);
        myswitch=(Switch)findViewById(R.id.switchoffline);
        toolm=(android.support.v7.widget.Toolbar)findViewById(R.id.addtoolbar);
        toolm.setTitle("Options");
        toolm.setNavigationIcon(R.drawable.front);
        setSupportActionBar(toolm);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        EmailDatabaseHandler es=new EmailDatabaseHandler(getApplicationContext());
        ArrayList<EmaillistItems>ls=new ArrayList<EmaillistItems>();
        ls=es.getAllLabels();
        adapter=new CustomemailListAdapter(Options.this,ls);
        email.setAdapter(adapter);
        toolm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Options.this, ListMode.class);
                startActivity(i);
                Options.this.finish();
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(Options.this, LoginPAge.class);
                startActivity(j);
                Options.this.finish();
            }
        });
     if(offlinemodesession.isLoggedIn()){
         myswitch.setChecked(true);
     }else{
         myswitch.setChecked(false);
     }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(myswitch.isChecked()){
                    myswitch.setChecked(true);
                  offlinemodesession.createLoginSession("offline mode enabled");
                }else{
                    myswitch.setChecked(false);
                    offlinemodesession.logoutUser();
                }
            }
        });
        currecnys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currenctSession.isLoggedIn()){
                    HashMap<String, String> cur = currenctSession.getUserDetails();
                    money=cur.get(currenctSession.KEY_CURRENCY);
                }else {
                    cur();
                }
            }
        });
        mone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currenctSession.isLoggedIn()) {
                    HashMap<String, String> cur = currenctSession.getUserDetails();
                    money = cur.get(currenctSession.KEY_CURRENCY);
                }else{
                    cur();
                }

            }
        });
}

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void cur() {
        final LayoutInflater layoutInflater = LayoutInflater.from(Options.this);
        final View promptView = layoutInflater.inflate(R.layout.currency, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Options.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);
        final android.support.v7.widget.Toolbar mtoolbar=(Toolbar)promptView.findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setTitle("Currency");
        final ListView list=(ListView)promptView.findViewById(R.id.listcontent);
        availableCurrenciesSet =
                Currency.getAvailableCurrencies();

        availableCurrenciesList = new ArrayList<Currency>(availableCurrenciesSet);
        adapters = new ArrayAdapter<Currency>(
                this,
                android.R.layout.simple_list_item_single_choice,
                availableCurrenciesList);
        list.setAdapter(adapters);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Currency currency = (Currency) parent.getItemAtPosition(i);
                String currencyCode = currency.getCurrencyCode();
                String displayName = currency.getDisplayName();
                symbol = currency.getSymbol();
                mone.setText(symbol);
                currenctSession.createLoginSession(symbol);
                Toast.makeText(Options.this,
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

}
