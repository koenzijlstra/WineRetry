package com.example.koen.wineretry.Listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.koen.wineretry.Objects.WineObject;
import com.example.koen.wineretry.R;

import java.util.List;

/**
 * Created by Koen on 17-1-2017.
 */

public class ListadapterBottles extends ArrayAdapter {

    public ListadapterBottles(Context context, List allwines) {
        super(context, 0, allwines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // get wine object at postiion
        final WineObject wineObject = (WineObject) getItem(position);

        // use listitem.xml as layout for each item
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent,
                    false);
        }

        TextView tvtitle = (TextView) convertView.findViewById(R.id.titleitem);
        if (wineObject != null){
            tvtitle.setText(wineObject.getTitle());
        }

        TextView tvyear = (TextView) convertView.findViewById(R.id.tvyear);
        if (wineObject != null){
            tvyear.setText(wineObject.getYear());
        }

        TextView tvregion = (TextView) convertView.findViewById(R.id.tvregion);
        if (wineObject != null){
            tvregion.setText(wineObject.getRegion());
        }
        
        return convertView;
    }


}
