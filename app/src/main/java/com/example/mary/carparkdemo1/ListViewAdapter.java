package com.example.mary.carparkdemo1;

/**
 * Created by michael.carr on 17/11/15.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<CarPark> {

    private Context context;
    private boolean useList = true;

    public ListViewAdapter(Context context, List items) {
        super(context, android.R.layout.simple_selectable_list_item, items);
        this.context = context;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView carParkName;
        TextView carParkAddress;
        TextView carParkFreeSpaces;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CarPark item = (CarPark)getItem(position);
        View viewToUse = null;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            viewToUse = mInflater.inflate(R.layout.list_entry, null);
            holder = new ViewHolder();
            holder.carParkName = (TextView)viewToUse.findViewById(R.id.carParkName);
            holder.carParkAddress = (TextView)viewToUse.findViewById(R.id.carParkAddress);
            holder.carParkFreeSpaces = (TextView)viewToUse.findViewById(R.id.carParkFreeSpaces);

            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }
        holder.carParkName.setText(item.getName());
        holder.carParkAddress.setText(item.getAddress());
        holder.carParkFreeSpaces.setText(Integer.toString(item.getFreeSpacesNumber()) + " free spaces");
        return viewToUse;
    }
}