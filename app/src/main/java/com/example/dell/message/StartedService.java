package com.example.dell.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.presence_online;

/**
 * Created by Dell on 24/11/2016.
 */

public class StartedService extends Service {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("push_text");
    DatabaseReference postsRef = ref.child("posts");
    private static final String TAG = "StartedService";
    // start notification manager
    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Create Service from service class");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start Service");

        //create intent for notification
        Intent notiIntent = new Intent();
        PendingIntent pendingNotiIntent = PendingIntent.getActivity(this,0,notiIntent,0);
        //create notification
        Notification messageNoti = new Notification.Builder(this)
                .setTicker("Message Service Started")
                .setContentTitle("MESSAGE SERVICE")
                .setContentText("Message Service Started")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingNotiIntent).getNotification();

        // display notification
        notificationManager.notify(1,messageNoti);


        new Thread(new Runnable() {
            public void run() {
                postsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getData(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                       getData(dataSnapshot);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        EventBus.getDefault().post(dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(1);
        Log.d(TAG, "Stop Service");
    }

public void getData(DataSnapshot dataSnapshot){
    TextDetail value = dataSnapshot.getValue(TextDetail.class);
    if (value == null)
        return;
    if (((System.currentTimeMillis() / 1000) - value.getTimestamp()) > 1200) {
        dataSnapshot.getRef().removeValue();
        return;
    }
    value.uuid = dataSnapshot.getKey();
    EventBus.getDefault().post(value);
    }
}
