package com.example.lakini.edgeanalyticsservice;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    Context context;
    EdgeAnalyticsManager edgeAnalyticsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edgeAnalyticsManager = new EdgeAnalyticsManager();
        context = getApplicationContext();

        //creating the connection
        edgeAnalyticsManager.connecttheService(context);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void Register() throws RemoteException {

        edgeAnalyticsManager.name = "wso2.edgeAnalaticsClient";
        edgeAnalyticsManager.type = "type1";
        edgeAnalyticsManager.stream = "define stream StockQuoteStream (value int);";
        edgeAnalyticsManager.streamName = "StockQuoteStream";
        edgeAnalyticsManager.query = "value>30";
        edgeAnalyticsManager.callb = "online";
        edgeAnalyticsManager.getEdgeAnalyticsService();

    }

    public void checkAmount(View view) throws RemoteException, InterruptedException {

        Register();
        edgeAnalyticsManager.checkCount();

    }


}
