package com.example.lakini.edgeanalyticsservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EdgeAnalyticsManager {
    Intent intent;
    IEdgeAnalyticsService RemoteService;
    RemoteServiceConnection remoteServiceConnection = new RemoteServiceConnection();
    String name, type, query, callb, stream, streamName;
    int values;

    //the method to bind to the service
    void connecttheService(Context context) {
        intent = new Intent();
        intent.setClassName("com.example.lakini.edgeanalyticsservice", "com.example.lakini.edgeanalyticsservice.EdgeAnalyticsService");
        context.startService(intent);
        context.bindService(intent, remoteServiceConnection, Context.BIND_AUTO_CREATE);
    }

    //service connection class
    class RemoteServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className,
                                       IBinder boundService) {
            RemoteService = IEdgeAnalyticsService.Stub.asInterface((IBinder) boundService);
            Log.d(getClass().getSimpleName(), "onServiceConnected()");
        }

        public void onServiceDisconnected(ComponentName className) {
            RemoteService = null;
            //updateServiceStatus();
            Log.d(getClass().getSimpleName(), "onServiceDisconnected");
        }
    }

    ;

    public void getEdgeAnalyticsService() throws RemoteException {
        RemoteService.getService(name, type, stream, streamName, query, callb);
    }

    public void checkCount() throws RemoteException, InterruptedException {
        Timer time = new Timer(); // Instantiate Timer Object
        ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
        time.schedule(st, 0, 60000); // Create Repetitively task for every 60 secs.here we show it in mili second

        //for demo only.
        for (int i = 0; i <= 5; i++) {

            System.out.println("Execution in Main Thread...." + i);
            Log.d("ClientSecond", "the value is " + String.valueOf(values));
            //sending values to the service
            st.createNumber();
            RemoteService.sendData(values, callback);

            Thread.sleep(2000);
            if (i == 5) {
                System.exit(0);
            }
        }

    }

    ///Implementation for the IserverCallback is from Client Side.
    private IEdgeAnalyticServiceCallback callback = new IEdgeAnalyticServiceCallback.Stub() {
        @Override
        public void addcallback(String text) throws RemoteException {
            Log.d("ClientSecond", "This is the client app and get a callback and the value is >50 " + text);
        }
    };

    //scheduling and send the random numbers periodically
    public class ScheduledTask extends TimerTask {

        void createNumber() {
            run();
        }

        public void run() {

            Random rand = new Random();
            //this will send numbers 0-50 numbers
            values = rand.nextInt(100);
            Log.d("Client Second", String.valueOf(values));

        }
    }

}
