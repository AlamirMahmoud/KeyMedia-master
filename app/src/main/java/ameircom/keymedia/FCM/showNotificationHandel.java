package ameircom.keymedia.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.NotificationTarget;


/**
 * Created by Sotraa on 6/15/2016.
 */
public class showNotificationHandel {
    private NotificationTarget notificationTarget;
    static int ID =0 ;
    Context context ;
    String customer_name ;
//    public showNotificationHandel(Context context ,NewsFeedModel data ) {
//
//        this.context = context ;
//        sendFeedNotification(data);
//
//    }
//
//    public showNotificationHandel(Context context, String customer_name , boolean ed) {
//        this.context = context;
//        this.customer_name = customer_name;
//        sendBilNotfication(ed);
//    }

//    private void sendBilNotfication(boolean ed){
//        Intent intent = new Intent(context, AdminPanel.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("from","notification");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        String title ;
//        if (ed){
//            title = "طلب معدل";
//        }else {
//            title = "طلب جديد";
//        }
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
//                .setContentTitle(title)
//                .setAutoCancel(true)
//                .setContentText(customer_name)
//                .setSmallIcon(R.drawable.notfication)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//                .setPriority( NotificationCompat.PRIORITY_MAX);
//        final Notification notification = notificationBuilder.build();
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(ID++ /* ID of notification */, notification);
//
//    }
//
//    private void sendFeedNotification(final NewsFeedModel data) {
//        Intent intent = new Intent(context, NewsDetailActivity.class);
//        intent.putExtra(NewsDetailActivity.EXTRA_FEED,data);
//        intent.putExtra(NewsDetailActivity.FROM,"notification");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.remoteview_notification);
//        rv.setImageViewResource(R.id.remoteview_notification_icon, R.drawable.notfication);
//        rv.setTextViewText(R.id.remoteview_notification_headline,"منتج جديد");
//        rv.setTextViewText(R.id.remoteview_notification_short_message, data.getProd_title());
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
//                .setContentTitle("منتج جديد")
//              .setContentText(data.getProd_title())
//                .setSmallIcon(R.drawable.notfication)
//                .setSound(defaultSoundUri)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setCustomBigContentView(rv)
//                .setPriority( NotificationCompat.PRIORITY_MAX)
//                .setStyle(new NotificationCompat.BigPictureStyle())
//                ;
//
//
//        final Notification notification = notificationBuilder.build();
//
////        // set big content view for newer androids
////        if (android.os.Build.VERSION.SDK_INT >= 16) {
////            notification.bigContentView = rv;
////        }
////
////        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
////        notificationManager.notify(ID++ /* ID of notification */, notification);
//        notificationTarget = new NotificationTarget(
//                context,
//                rv,
//                R.id.remoteview_notification_icon,
//                notification,
//                0);
//        Handler mainHandler = new Handler(context.getMainLooper());
//
//        Runnable myRunnable = new Runnable() {
//            @Override
//            public void run() { Glide
//                    .with(context) // safer!
//                    .load(EndPoints.img_url+data.getProd_img().get(0))
//                    .asBitmap()
//                    .into(notificationTarget);
//            } // This is your code
//        };
//        Log.e("img",EndPoints.img_url+data.getProd_img().get(0));
//        mainHandler.post(myRunnable);
//    }
}
