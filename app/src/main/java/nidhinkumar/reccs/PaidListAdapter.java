package nidhinkumar.reccs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by M.S. Venugopal on 20-05-2016.
 */
public class PaidListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> listNumNames;


    public PaidListAdapter(Context cont, ArrayList<String> listNumNames)
    {
        super(cont, R.layout.listitem_row, listNumNames);
        this.context = (Activity) cont;
        this.listNumNames = listNumNames;

    }

    static class ViewHolder {
        public TextView txtTitle;

    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        ViewHolder holder = null;

        if (rowView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listitem_row, parent, false);
            holder.txtTitle = (TextView) rowView.findViewById(R.id.num_name);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.txtTitle.setText(listNumNames.get(position));

        return rowView;
    }
}
