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
 *
 * This class extends the arrayadapter. It uses a list of WineObjects as input, and listitem.xml as
 * layout for each item. It fills the needed textviews of each listitem with the title, year and
 * region of the Wine Object.
 */


public class ListadapterBottles extends ArrayAdapter {

    public ListadapterBottles(Context context, List allwines) {
        super(context, 0, allwines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final WineObject wineObject = (WineObject) getItem(position);

        // Use listitem.xml as layout for the items in the lsit
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent,
                    false);
        }

        // Fill the textviews in listitem.xml
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
