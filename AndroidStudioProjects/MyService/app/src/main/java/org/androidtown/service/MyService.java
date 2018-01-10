package org.androidtown.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG,"onCreate() 호출됨.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand() 호출됨.");

        if(intent == null){
            return Service.START_STICKY;
        }else {
            processCommand(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void processCommand(Intent intent) {
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

        Log.d(TAG,"전달받은 데이터 : " + command + ", " + name );

        try{
            Thread.sleep(5000);
        }catch(Exception e){}

        Intent showIntent = new Intent(getApplicationContext(),MainActivity.class);
        showIntent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|
                            intent.FLAG_ACTIVITY_CLEAR_TOP|
                            intent.FLAG_ACTIVITY_SINGLE_TOP);
        showIntent.putExtra("command","show");
        showIntent.putExtra("name",name + " from Service.");
        startActivity(showIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"onDestroy() 호출됨.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
