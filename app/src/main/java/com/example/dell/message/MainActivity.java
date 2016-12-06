package com.example.dell.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Mainactivity";
    private static final String SYNCHRONIZE_DATA_ARRAY_LOCK = ",hmdtymfh hb h";
    private FirebaseDatabase database;
    private DatabaseReference postsRef;

    private MessageAdapter messageAdapterAdapter;
    private ArrayList<TextDetail> dataArray;
    Intent serviceIntent;

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
    }



    @Override
    public void onBackPressed() {
        stopService(new Intent(this, StartedService.class));
    }

    private ListView mListView;
    private EditText enterText;
    private EditText enterName;

    protected void addNewMessage(final TextDetail model) {
        synchronized (SYNCHRONIZE_DATA_ARRAY_LOCK) {
            this.dataArray.add(model);
        }
        this.messageAdapterAdapter.notifyDataSetChanged();
    }

    protected void removeMessage(final String uuid) {
        synchronized (SYNCHRONIZE_DATA_ARRAY_LOCK) {
            for (TextDetail model : this.dataArray)
                if (model.uuid != null && model.uuid.equalsIgnoreCase(uuid)) {
                    this.dataArray.remove(model);
                    break;
                }
        }
        messageAdapterAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mListView = (ListView) findViewById(R.id.display_message);
        this.enterText = (EditText) findViewById(R.id.enter_message);
        this.enterName = (EditText) findViewById(R.id.enter_name);

        this.database = FirebaseDatabase.getInstance();
        this.postsRef = database.getReference("push_text").child("posts");

        this.dataArray = new ArrayList<>();

        this.messageAdapterAdapter = new MessageAdapter(MainActivity.this, dataArray);
        this.mListView.setAdapter(messageAdapterAdapter);

        Button btnSend = (Button) findViewById(R.id.send_bt);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Form Validation
                String name = enterName.getText().toString().trim();
                String text = enterText.getText().toString().trim();
                if (name.length() <= 0)
                    return;
                if (text.length() <= 0)
                    return;
                enterText.setText("");

                DatabaseReference newPostRef = postsRef.push();

                TextDetail txt = new TextDetail(name, text);
                newPostRef.setValue(txt);
            }
        });

        EventBus.getDefault().register(this);
        serviceIntent = new Intent(this, StartedService.class);
        startService(serviceIntent);
    }

    @Subscribe
    public void onEvent(TextDetail model) {
        addNewMessage(model);
    }

    @Subscribe
    public void onEvent(String uuid) {
        removeMessage(uuid);
    }
}

