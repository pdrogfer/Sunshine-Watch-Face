package com.example.android.sunshine.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class MyDataService extends WearableListenerService {

    private static final String TAG = "MMyDataService";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                // path,
                if (path.equals("/weather")) {
                    int maxTemp = dataMap.getInt("MAX_TEMP");
                    int minTemp = dataMap.getInt("MIN_TEMP");
                    int weatherIconId = dataMap.getInt("WEATHER_ICON");

                    Log.i(TAG, "onDataChanged: DATA RECEIVED!!! Max " + maxTemp +
                            ", Min " + minTemp +
                            ", iconId " + weatherIconId);
                }
            }
        }
    }
}
