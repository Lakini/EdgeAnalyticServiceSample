package com.example.lakini.edgeanalyticsservice;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;

import java.io.IOException;

/**
 * Created by lakini on 8/19/15.
 */
public class CEP {
    SiddhiManager siddhiManager;
    String callBack;
    String streamName;
    boolean message = false;
    IEdgeAnalyticServiceCallback callb = null;

    public CEP(final Context context, String streamDefinition, String stream, String query, String callbackFunction) {

        final String uQuery=query;
        callBack = callbackFunction;
        streamName = stream;
        siddhiManager = new SiddhiManager();
        //define stream
        siddhiManager.defineStream(streamDefinition);
        //add CEP queries
        siddhiManager.addQuery("from " + stream + "[" + query + "] insert into HighValueQuotes;");
        Log.d("EdgeAnalytics Service", "from " + stream + "[" + query + "] insert into HighValueQuotes;");
        //add Callbacks to see results

        siddhiManager.addCallback("HighValueQuotes", new StreamCallback() {
            public void receive(Event[] events) {
                EventPrinter.print(events);
                try {
                    callb.addcallback("Siddhimanger's current query " + uQuery);
                    message = true;
                    } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CEP(String appName, String type, String streamDefinition, String stream, String query, String callbackFunction) {
        //have to implement
    }

    //Get sound notification
    public void playSound(Context context) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(context, soundUri);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }

    }

    //Analyse the data and this is the method of using in callbacks in siddhi.
    public boolean analyseTheData(int data, IEdgeAnalyticServiceCallback cb) {
        //adding inputs to stream
        InputHandler inputHandler = siddhiManager.getInputHandler(streamName);
        try {
            callb = cb;
            inputHandler.send(new Object[]{data});
           } catch (InterruptedException e) {
            e.printStackTrace();
              }

        return message;
    }


}
