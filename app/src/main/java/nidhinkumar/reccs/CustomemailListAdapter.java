package nidhinkumar.reccs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by M.S. Venugopal on 27-05-2016.
 */
public class CustomemailListAdapter extends BaseAdapter{
    Context context;
    ArrayList<EmaillistItems> emailList;
    Useridsession useridsession;
    public CustomemailListAdapter(Context context, ArrayList<EmaillistItems> list) {

        this.context = context;
        emailList = list;
    }



    @Override
    public int getCount() {

        return emailList.size();
    }

    @Override
    public Object getItem(int position) {

        return emailList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        EmaillistItems emaillistItems = emailList.get(position);
        useridsession=new Useridsession(context);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.accountdet, null);

        }
        TextView userid=(TextView) convertView.findViewById(R.id.iduser);
        TextView mailername = (TextView) convertView.findViewById(R.id.tvemail);
        userid.setText(emaillistItems.getUserid());
        mailername.setText(emaillistItems.getMailername());
        useridsession.createLoginSession(userid.getText().toString());
        return convertView;
    }

}
