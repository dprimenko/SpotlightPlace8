package es.dpinfo.spotlightplace.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.SpotlightApplication;

/**
 * Created by dprimenko on 6/03/17.
 */
public class PlaceDeletedReceiver extends BroadcastReceiver {

    public static final String ACTION = "es.dpinfo.spotlightplace.DELETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        Notification.Builder noti = new Notification.Builder(context);

        noti.setContentTitle(SpotlightApplication.getContext().getString(R.string.app_name));
        noti.setSmallIcon(R.mipmap.ic_launcher_spotlight);
        noti.setContentText(intent.getExtras().getString("place") + " ha sido eliminado");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, noti.build());
    }
}
