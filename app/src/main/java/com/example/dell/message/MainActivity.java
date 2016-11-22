package com.example.dell.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("push_text");

    final protected void displayMessageList(final List<TextDetail> textDetails) {
        MessageAdapter messageAdapterAdapter = new MessageAdapter(MainActivity.this, textDetails);
        ListView listView = (ListView) findViewById(R.id.display_message);
        listView.setAdapter(messageAdapterAdapter);


        




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Crash report
//        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        final EditText enterText = (EditText) findViewById(R.id.enter_message);
        final EditText enterName = (EditText) findViewById(R.id.enter_name);
        Button send = (Button) findViewById(R.id.send_bt);
        DatabaseReference postsRef = ref.child("posts");
// push chat's text to server
//        final Map<String, TextDetail> text = new HashMap<String, TextDetail>();
//
//        text.put("push", txt);
        final DatabaseReference newPostRef = postsRef.push();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextDetail txt = new TextDetail(enterName.getText().toString(), enterText.getText().toString());
                newPostRef.setValue(txt);
                enterText.setText("");
            }
        });
//        Map<String, TextDetail> text = new HashMap<>();
        final List<TextDetail> listValue = new ArrayList<TextDetail>(); // Result will be holded Here
        postsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TextDetail value = dataSnapshot.getValue(TextDetail.class);
//                Toast.makeText(MainActivity.this, "current second :" + System.currentTimeMillis() / 1000, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "message second :" + value.timestamp, Toast.LENGTH_SHORT).show();
//
//                 if (((System.currentTimeMillis() / 1000) - value.getTimestamp()) > 600) {
//                    return;
//            }
                // method 1
//                Map<String, TextDetail> td = (HashMap<String,TextDetail>) dataSnapshot.getValue();
//
//                List<TextDetail> listValue =(List<TextDetail>) td.values();


                listValue.add(value); //add result into array list

//                List<TextDetail> listValue = dataSnapshot.getValue(<TextDetail>.class);


                displayMessageList(listValue);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//              TextDetail value = dataSnapshot.getValue(TextDetail.class);
//                displayText.setText(value.Name + "\n" + value.Text);
                TextDetail value = dataSnapshot.getValue(TextDetail.class);
                listValue.add(value); //add result into array list
                displayMessageList(listValue);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("mainactivity", "Failed to read value!", databaseError.toException());
                Toast.makeText(MainActivity.this, "Fail to read value!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
