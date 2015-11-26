package com.example.mary.carparkdemo1;

/**
 * Created by michael.carr on 17/11/15.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<CarPark> {

    private Context context;
    private ViewHolder holder;

    public ListViewAdapter(Context context, List<CarPark> items) {
        super(context, android.R.layout.simple_selectable_list_item, items);
        this.context = context;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder {
        TextView carParkName;
        TextView carParkFreeSpaces;
        TextView carParkDistance;
        TextView carParkAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarPark carPark = getItem(position);
        View viewToUse;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            viewToUse = mInflater.inflate(R.layout.list_entry, null);
            holder = new ViewHolder();
            holder.carParkName = (TextView) viewToUse.findViewById(R.id.carParkName);
            holder.carParkAddress = (TextView) viewToUse.findViewById(R.id.carParkAddress);
            holder.carParkFreeSpaces = (TextView) viewToUse.findViewById(R.id.carParkFreeSpaces);
            holder.carParkDistance = (TextView) viewToUse.findViewById(R.id.carParkDistance);

            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }
        CardView cardView = (CardView) viewToUse.findViewById(R.id.card_view);
      //  int[] colours = selectColours(carPark);
      //  cardView.setBackgroundColor(Color.rgb(colours[0], colours[1], colours[2]));

        holder.carParkName.setText(carPark.getName());

        float distance = ((MainActivity) context).getDistanceFrom(carPark);
        if (distance != -1) {
            holder.carParkDistance.setText(String.format("%.2f miles from you", convertToMiles(distance)));
        } else {
            holder.carParkDistance.setText("Unknown distance from you");
        }
        holder.carParkAddress.setText(carPark.getAddress());

        if (carPark.isFreeSpacesKnown()) {
            holder.carParkFreeSpaces.setText(String.format("%d/%d empty spaces", carPark.getFreeSpacesNumber(), carPark.getTotalSpaces()));
        } else {
            holder.carParkFreeSpaces.setText("Unknown");
        }
        int [] colours = selectColours(carPark);

        holder.carParkName.setTextColor(Color.rgb(colours[0],colours[1],colours[2]));
        holder.carParkAddress.setTextColor(Color.rgb(colours[0],colours[1],colours[2]));
        holder.carParkFreeSpaces.setTextColor(Color.rgb(colours[0], colours[1], colours[2]));
        holder.carParkDistance.setTextColor(Color.rgb(colours[0], colours[1], colours[2]));

        return viewToUse;
    }

    private float convertToMiles(float distance) {
        distance /= 1000;
        distance *= 0.621371f;
        return distance;
    }

    private int[] selectColours(CarPark carPark) {
        int[] colours = new int[3];
        if (carPark.getFreeSpacesNumber() < 10 && carPark.isFreeSpacesKnown()) {
            colours[0] = 204;
            colours[1] = 0;
            colours[2] = 0;
        }
        if (carPark.getFreeSpacesNumber() >= 10 &&
                carPark.getFreeSpacesNumber() < 30) {
            colours[0] = 255;
            colours[1] = 165;
            colours[2] = 0;
        }
        if (carPark.getFreeSpacesNumber() >= 30) {
            colours[0] = 102;
            colours[1] = 153;
            colours[2] = 0;
        }
        if (!carPark.isFreeSpacesKnown()) {
            colours[0] = 0;
            colours[1] = 153;
            colours[2] = 204;
        }
        return colours;
    }


}