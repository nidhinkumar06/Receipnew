package nidhinkumar.reccs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by M.S. Venugopal on 01-06-2016.
 */
public class DetailsPage extends AppCompatActivity{
    ImageView Image;
    TextView curry,category,date,paidwith,project;
    EditText amt,merchantname,comment;
    Toolbar mtool;
    String atm,mename,paid,catg,paymod,comme,cur;
    CurrenctSession currenctSession;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);
        mtool=(Toolbar)findViewById(R.id.toolbar);
        currenctSession=new CurrenctSession(getApplicationContext());
        Image=(ImageView)findViewById(R.id.detimgview);
        curry=(TextView)findViewById(R.id.dettvcurrency);
        category=(TextView)findViewById(R.id.dettvcategory);
        date=(TextView)findViewById(R.id.dettvdate);
        paidwith=(TextView)findViewById(R.id.dettvpaidwith);
        project=(TextView)findViewById(R.id.dettvproject);
        amt=(EditText)findViewById(R.id.dettvamt);
        merchantname=(EditText)findViewById(R.id.dettvmerchantname);
        comment=(EditText)findViewById(R.id.dettvcomment);
        mtool.setTitle("Expenses");
        setSupportActionBar(mtool);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtool.setNavigationIcon(R.drawable.front);
        mtool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailsPage.this, ListMode.class);
                startActivity(i);
                DetailsPage.this.finish();

            }
        });
        Intent i=getIntent();
        Bitmap bitmap = (Bitmap) i.getParcelableExtra("bitmap");
        atm=i.getStringExtra("amount");
        mename=i.getStringExtra("mername");
        paid=i.getStringExtra("paidon");
        catg=i.getStringExtra("catg");
        paymod=i.getStringExtra("paym");
        comme=i.getStringExtra("comm");
        paid= paid.replace("Paid on:"," ");
        Image.setImageBitmap(bitmap);
        amt.setText(atm);
        merchantname.setText(mename);
        date.setText(paid);
        paidwith.setText(paymod);
        category.setText(catg);
        comment.setText(comme);
        if(currenctSession.isLoggedIn()){
            HashMap<String, String> curs = currenctSession.getUserDetails();
            cur=curs.get(currenctSession.KEY_CURRENCY);
            curry.setText(cur);
        }else{
            curry.setVisibility(View.INVISIBLE);
        }
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Image.buildDrawingCache();
               // Bitmap bitm = Image.getDrawingCache();
                BitmapDrawable drawable = (BitmapDrawable) Image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                Intent k=new Intent(DetailsPage.this,Imagefull.class);
                k.putExtra("bitss",bitmap);
                startActivity(k);
            }
        });

    }
}
