package com.example.dell.message;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.message.TextDetail;


import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class MessageAdapter extends ArrayAdapter<TextDetail> {


    public MessageAdapter(Activity context, List<TextDetail> textDetails) {
       super(context,0,textDetails);
    }

    static class ViewHolderItem {
        TextView name;
        TextView text;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        ViewHolderItem viewHolder;
        if (listItemView == null) {
            //inflate layout
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_message_view, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.name = (TextView) listItemView.findViewById(R.id.message_name);
            viewHolder.text = (TextView) listItemView.findViewById(R.id.mesage_text);
            listItemView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) listItemView.getTag();
        }

        //Reset
        viewHolder.name.setText("");
        viewHolder.text.setText("");


        //Data
        TextDetail currentMess = getItem(position);
        if (currentMess == null)
            return listItemView;

        String messName = (currentMess.getMessageName() == null) ? "$$" : "" + currentMess.getMessageName();
        String messText = (currentMess.getMessageText() == null) ? "No Author" : currentMess.getMessageText();


        //UI


        // set name value
        if (currentMess.getMessageName() != null) {
            viewHolder.name.setText(messName);
        }
        // set text value
        if (currentMess.getMessageText() != null) {
            viewHolder.text.setText(messText);
        }


        //abc
        return listItemView;
    }
}
