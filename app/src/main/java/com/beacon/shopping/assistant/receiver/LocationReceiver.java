

package com.beacon.shopping.assistant.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.util.NotificationBuilder;


public class LocationReceiver extends BroadcastReceiver {

    private static final Criteria criteria = new Criteria();
    private static final long MAX_AGE_TIME = 10000;
    private static final int RETRY_COUNT_MAX = 2;
    private int retryCount = 0;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        retryCount = intent.getIntExtra("RETRY_COUNT", 0);
        Location bestLocation = null;
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationManager.getAllProviders()) {
            if (provider != null) {
                final Location location = locationManager.getLastKnownLocation(provider);
                final long now = System.currentTimeMillis();
                if (location != null
                        && (bestLocation == null || location.getTime() > bestLocation.getTime())
                        && location.getTime() > now - MAX_AGE_TIME) {
                    bestLocation = location;
                }
            }
        }

        if (bestLocation != null) {
            final String position = bestLocation.getLatitude() + "," + bestLocation.getLongitude();
            final Uri uri = Uri.parse("geo:" + position + "?z=16&q=" + position);
            final Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);

            PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationBuilder notificationBuilder = new NotificationBuilder(context);
            notificationBuilder.createNotification(R.mipmap.ic_launcher, context.getString(R.string.action_alarm_text_title), notificationIntent);
            notificationBuilder.setMessage(context.getString(R.string.notification_display_last_position));
            notificationBuilder.show(1);

        } else {
            if (retryCount < RETRY_COUNT_MAX) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                retryCount++;
                intent.putExtra("RETRY_COUNT", retryCount);
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                locationManager.requestSingleUpdate(criteria, pendingIntent);
            }
        }
    }

}
