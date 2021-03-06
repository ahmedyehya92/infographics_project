package com.dev3raby.infographic.Helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev3raby.infographic.Activities.CatgInfographicsActivity;
import com.dev3raby.infographic.Activities.HomeActivity;
import com.dev3raby.infographic.Activities.InfographicActivity;
import com.dev3raby.infographic.Activities.SearchActivity;
import com.dev3raby.infographic.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static android.graphics.Color.rgb;

/**
 * Created by Ahmed Yehya on 04/07/2017.
 */

public class FCMService extends FirebaseMessagingService {

    final String titleKey = "titleKey";
    final String idKey = "idKey";
    Bitmap largeIcon;
    Intent intent;
    int mesgNumber = 0;
    String versionCode;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        versionCode = getVersionInfo();
        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("type"), remoteMessage.getData().get("id"));


    }

    private void sendNotification(String title, String messageBody, String type, String id) {
        if (type.equals("0"))
        {
            intent = new Intent(this, SearchActivity.class);
            intent.putExtra(idKey,id);
        }
        else if (type.equals("1"))
        {
            intent = new Intent(this, InfographicActivity.class);
            intent.putExtra(idKey,id);

        }

        else if (type.equals("2"))
        {


                        intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName()));


        }





   /*     try {
            largeIcon = Glide
                    .with( FCMService.this ) // could be an issue!
                    .load("http://www.dailymedicalinfo.com/wp-content/uploads/2013/11/DailyMedicalinfo_Tea_vs_Coffee1.jpg")
                    .asBitmap().centerCrop()
                    .into(250,250)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        */

        if (type.equals("2") && id.equals(versionCode))
        {

        }

        else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, getID() /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setColor(rgb(39, 117, 244))
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(getID() /* ID of notification */, notificationBuilder.build());
        }
    }


    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }

    private String getVersionInfo() {
        String versionName = "";
        Integer versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode.toString();
    }


}
