package yourdev.noisealert.Class;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.content.Intent;
import android.widget.RemoteViews;

import yourdev.noisealert.Activity.ActivityPrincipal;
import yourdev.noisealert.R;


public class MyNotification {

    public NotificationManager simples(Context context, int icon, String titulo,String texto, int id, String mId){

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,mId)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.logo_noise_alert))
                .setColor(Color.parseColor("#fe5196"))
                .setContentTitle(titulo)
                .setContentText(texto);


        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, ActivityPrincipal.class);
        resultIntent.putExtra("notification","1");


        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notification.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.

        if (mNotificationManager != null) {
              mNotificationManager.notify(id, notification.build());
        }

        return mNotificationManager;
    }


    public NotificationManager withLayout(Context context,int icon, int id, String mId){

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews contentView = new RemoteViews(context.getPackageName(),R.layout.layout_notification);
        contentView.setImageViewResource(R.id.notification_phone,R.drawable.ic_activity_principal_phon);
        contentView.setTextViewText(R.id.notification_titulo,"Fone On");
        contentView.setTextViewText(R.id.notification_toque,"Padrão");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,mId)
                .setSmallIcon(icon  )
                .setContent(contentView);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (mNotificationManager != null) {
            mNotificationManager.notify(id, notification);
        }
        /*
        Intent intent = new Intent("clicked");
        intent.putExtra("id",1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,123,intent,0);
        contentView.setOnClickPendingIntent(R);

        */
        return mNotificationManager;
    }


}