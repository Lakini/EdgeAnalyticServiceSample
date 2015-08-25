package com.example.lakini.edgeanalyticsservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class EdgeAnalyticsService extends Service {
    final RemoteCallbackList<IEdgeAnalyticServiceCallback> callbacks = new RemoteCallbackList<IEdgeAnalyticServiceCallback>();

    public EdgeAnalyticsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //Implement the methods in the interface
    private final IEdgeAnalyticsService.Stub mBinder = new IEdgeAnalyticsService.Stub() {
        CEP cep;
        String clientName;
        boolean message;

        @Override
        public void startEdgeAnalyticsService() throws RemoteException {

        }

        @Override
        public void getService(String appName, String type, String streamDefinition, String stream, String query, String callbackFunction) {
            clientName = appName;
            cep = new CEP(getApplicationContext(), streamDefinition, stream, query, callbackFunction);
        }

        @Override
        public void sendData(int data, IEdgeAnalyticServiceCallback cb) {
            callbacks.register(cb);
            message = cep.analyseTheData(data, cb);

        }

    };

    //broadcast method is implemented here
    protected void broadcast(String message) {
        final int N = callbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                callbacks.getBroadcastItem(i).addcallback(message);
            } catch (RemoteException e) {
                // RemoteCallbackList will take care of removing dead objects
            }
        }
        callbacks.finishBroadcast();
    }

}
