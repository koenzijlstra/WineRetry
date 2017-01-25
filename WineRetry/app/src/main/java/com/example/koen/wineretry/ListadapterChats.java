package com.example.koen.wineretry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Koen on 25-1-2017.
 */

public class ListadapterChats extends ArrayAdapter {

    public ListadapterChats(Context context, List allchats) {
        super(context, 0, allchats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // get wine object at postiion
        final OtheruserObject otheruserObject = (OtheruserObject) getItem(position);

        // use listitem.xml as layout for each item
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitemchat, parent, false);
        }

        TextView tvname = (TextView) convertView.findViewById(R.id.name);
        if (otheruserObject != null){
            tvname.setText(otheruserObject.getUsernameother());
        }

        return convertView;
    }


}
