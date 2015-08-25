// IEdgeAnalyticsService.aidl
package com.example.lakini.edgeanalyticsservice;

import com.example.lakini.edgeanalyticsservice.IEdgeAnalyticServiceCallback;

interface IEdgeAnalyticsService {
       void startEdgeAnalyticsService();

          //this method is to register the clients to the services and get the service
          void getService(String appName,String type,String streamDefinition,String stream,String query,String callbackFunction);

          //send data by clients as this is for Type 1 client
          void sendData(int data,IEdgeAnalyticServiceCallback cb);
}
