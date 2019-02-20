package edu.berkeley.eecs.emission.cordova.unifiedlogger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import org.apache.cordova.CordovaActivity;

import edu.berkeley.eecs.emission.MainActivity;
import edu.berkeley.eecs.emission.R;

public class NotificationHelper {
	private static String TAG = "NotificationHelper";
	public static final String RESOLUTION_PENDING_INTENT_KEY = "rpIntentKey";

	public static void createNotification(Context context, int id, String message) {
		Notification.Builder builder = getNotificationBuilderForApp(context, message);
		/*
		 * This is a bit of magic voodoo. The tutorial on launching the activity actually uses a stackbuilder
		 * to create a fake stack for the new activity. However, it looks like the stackbuilder
		 * is only available in more recent versions of the API. So I use the version for a special activity PendingIntent
		 * (since our app currently has only one activity) which resolves that issue.
		 * This also appears to work, at least in the emulator.
		 * 
		 * TODO: Decide what level API we want to support, and whether we want a more comprehensive activity.
		 */
		Intent activityIntent = new Intent(context, MainActivity.class);
		activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0,
				activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(activityPendingIntent);		
		
		NotificationManager nMgr =
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Log.d(context, TAG, "Generating notify with id " + id + " and message " + message);
		nMgr.notify(id, builder.build());
	}

	/*
	 * Used to show a pending intent - e.g. to turn on location services
	 */
	public static void createNotification(Context context, int id, String message, PendingIntent intent) {
		Notification.Builder builder = getNotificationBuilderForApp(context, message);

		/*
		 * This is a bit of magic voodoo. The tutorial on launching the activity actually uses a stackbuilder
		 * to create a fake stack for the new activity. However, it looks like the stackbuilder
		 * is only available in more recent versions of the API. So I use the version for a special activity PendingIntent
		 * (since our app currently has only one activity) which resolves that issue.
		 * This also appears to work, at least in the emulator.
		 *
		 * TODO: Decide what level API we want to support, and whether we want a more comprehensive activity.
		 */
		Intent activityIntent = new Intent(context, MainActivity.class);
		activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activityIntent.putExtra(NotificationHelper.RESOLUTION_PENDING_INTENT_KEY, intent);

		PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0,
				activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(activityPendingIntent);
		// builder.setAutoCancel(true);

		NotificationManager nMgr =
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		Log.d(context, TAG, "Generating notify with id " + id + ", message " + message
				+ " and pending intent " + intent);
		nMgr.notify(id, builder.build());
	}

	public static void cancelNotification(Context context, int id) {
		NotificationManager nMgr =
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		Log.d(context, TAG, "Cancelling notify with id " + id);
		nMgr.cancel(id);
	}

	public static Notification.Builder getNotificationBuilderForApp(Context context, String message) {
		Notification.Builder builder = new Notification.Builder(context);
		Bitmap appIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
		builder.setLargeIcon(appIcon);
		builder.setSmallIcon(R.drawable.ic_visibility_black);
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setContentText(message);

		return builder;
	}
}
