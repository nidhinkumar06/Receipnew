package nidhinkumar.reccs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by M.S. Venugopal on 03-06-2016.
 */

public class CustomlistJsonadapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<ReceiptMov> movieItems;


    public CustomlistJsonadapter(Activity activity, List<ReceiptMov> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.onlineimagelist_items, null);

        final TextView merchantname = (TextView) convertView.findViewById(R.id.olsmerchantname);
        final TextView paiddate = (TextView) convertView.findViewById(R.id.olspaiddate);
        final TextView amounts = (TextView) convertView.findViewById(R.id.olsamount);
        final TextView paidwith=(TextView) convertView.findViewById(R.id.olspaidwith);
        final TextView categ=(TextView)convertView.findViewById(R.id.olscategory);
        final TextView comment=(TextView)convertView.findViewById(R.id.olscomment);

        ImageView imgview=(ImageView) convertView.findViewById(R.id.oimgview);
        ReceiptMov m = movieItems.get(position);



       // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        byte[] s =m.getMimage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(s);
        final Bitmap theImage = BitmapFactory.decodeStream(imageStream);
       // Bitmap bm = BitmapFactory.decodeByteArray(s, 0 ,s.length);

        imgview.setImageBitmap(theImage);
        merchantname.setText(m.getMerchantname());
        paiddate.setText("Paid on:" + m.getMerdate());
        amounts.setText(m.getMeramount());
        paidwith.setText(m.getMerpaid());
        categ.setText(m.getMercategory());
        comment.setText(m.getMecomment());
        final TextView moneydet=(TextView) convertView.findViewById(R.id.omoneydetails);
        final String moen;
        CurrenctSession currenctSession=new CurrenctSession(activity);
        if(currenctSession.isLoggedIn()){
            HashMap<String, String> cur = currenctSession.getUserDetails();
            moen=cur.get(currenctSession.KEY_CURRENCY);
            moneydet.setText(moen);
        }else{
            moneydet.setVisibility(View.INVISIBLE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(activity,DetailsPage.class);
                i.putExtra("bitmap",theImage);
                i.putExtra("amount",amounts.getText().toString());
                i.putExtra("mername",merchantname.getText().toString());
                i.putExtra("paidon",paiddate.getText().toString());
                i.putExtra("catg",categ.getText().toString());
                i.putExtra("paym",paidwith.getText().toString());
                i.putExtra("comm",comment.getText().toString());
                activity.startActivity(i);
            }
        });

        return convertView;
    }

}
