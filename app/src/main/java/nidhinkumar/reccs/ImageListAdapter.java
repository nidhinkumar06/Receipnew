package nidhinkumar.reccs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by M.S. Venugopal on 21-05-2016.
 */
public class ImageListAdapter extends BaseAdapter{
    Context context;
    ArrayList<ImagelistItems> imageList;

    public ImageListAdapter(Context context, ArrayList<ImagelistItems> list) {

        this.context = context;
        imageList = list;
    }



    @Override
    public int getCount() {

        return imageList.size();
    }

    @Override
    public Object getItem(int position) {

        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ImagelistItems imagelistItems = imageList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.imagelist_items, null);

        }
        byte[] outImage=imagelistItems.getImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        final Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        ImageView img=(ImageView) convertView.findViewById(R.id.imgview);
        img.setImageBitmap(theImage);
        final TextView merchname = (TextView) convertView.findViewById(R.id.lsmerchantname);
        merchname.setText(imagelistItems.getMerchantname());
        final TextView paid = (TextView) convertView.findViewById(R.id.lspaiddate);
        paid.setText(imagelistItems.getPaidon());
        TextView status = (TextView) convertView.findViewById(R.id.lsstatus);
        status.setText(imagelistItems.getStatus());
        final TextView amt = (TextView) convertView.findViewById(R.id.lsamount);
        amt.setText(imagelistItems.getAmount());
        ImageButton oofbutton=(ImageButton)convertView.findViewById(R.id.btnofflinebutton);
        oofbutton.setVisibility(View.INVISIBLE);
        final TextView category=(TextView) convertView.findViewById(R.id.lscategory);
        category.setText(imagelistItems.getCategory());
        final TextView paymode=(TextView) convertView.findViewById(R.id.lspaidwith);
        paymode.setText(imagelistItems.getPaymmode());
        final TextView comment=(TextView) convertView.findViewById(R.id.lscomment);
        comment.setText(imagelistItems.getComment());
        final TextView moneydet=(TextView) convertView.findViewById(R.id.moneydetails);
        final String moen;
        CurrenctSession currenctSession=new CurrenctSession(context);
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
                Intent i=new Intent(context,DetailsPage.class);
                i.putExtra("bitmap",theImage);
                i.putExtra("amount",amt.getText().toString());
                i.putExtra("mername",merchname.getText().toString());
                i.putExtra("paidon",paid.getText().toString());
                i.putExtra("catg",category.getText().toString());
                i.putExtra("paym",paymode.getText().toString());
                i.putExtra("comm",comment.getText().toString());
                 context.startActivity(i);
            }
        });
        return convertView;

    }

}
