package com.example.android.sunshine.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;

public class MyDataService extends WearableListenerService {

    private static final String TAG = "MyDataService";

    GoogleApiClient watchGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        watchGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        watchGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        int maxTemp, minTemp, weatherIconId;

        Log.d(TAG, "onDataChanged: " + dataEvents);
        if (!watchGoogleApiClient.isConnected() || !watchGoogleApiClient.isConnecting()) {
            ConnectionResult connectionResult = watchGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(TAG, "DataLayerListenerService failed to connect to GoogleApiClient, "
                        + "error code: " + connectionResult.getErrorCode());
                return;
            }
        }

        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();

                if (path.equals("/weather")) {
                    maxTemp = dataMap.getInt(Util.MAX_TEMP);
                    minTemp = dataMap.getInt(Util.MIN_TEMP);
                    weatherIconId = dataMap.getInt(Util.ICON_ID);

                    Log.i(TAG, "onDataChanged: DATA RECEIVED!!! Max " + maxTemp +
                            ", Min " + minTemp +
                            ", iconId " + weatherIconId);


                    publishNewWeather(maxTemp, minTemp, weatherIconId);
                }
            }
        }
    }

    private void publishNewWeather(int maxTemp, int minTemp, int weatherIconId) {

        Util.maxTempValue = maxTemp;
        Util.minTempValue = minTemp;
        Util.iconId = weatherIconId;
    }
}
