package com.example.dell.message;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.security.Timestamp;

/**
 * Created by Dell on 21/11/2016.
 */

@IgnoreExtraProperties
public class TextDetail implements Serializable  {
    public String uuid;
    public String Name;
    public String Text;
    public Long timestamp ;

    public TextDetail() {
    }

    public TextDetail(String name, String text) {
        Name = name;
        Text = text;
        this.timestamp = System.currentTimeMillis() / 1000;
    }
     public String getMessageName(){
         return Name;
     }
    public String getMessageText(){
        return Text;
    }
    public Long getTimestamp(){
        return timestamp;
    }
}
