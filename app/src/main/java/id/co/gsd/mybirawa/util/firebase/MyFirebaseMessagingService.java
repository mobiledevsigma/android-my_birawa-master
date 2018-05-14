package id.co.gsd.mybirawa.util.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.news.NewsDetailActivity;
import id.co.gsd.mybirawa.model.ModelNews;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private SessionManager session;
    private List<ModelNews> listModel;
    private ModelNews model;

    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }
    // [END receive_message]

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        String title = "";
        String message = "";
        session = new SessionManager(this);

        if (remoteMessage.getData().size() > 0) {
            if (session.getIdUnit().equals(remoteMessage.getData().get("unit"))) {
                if (remoteMessage.getNotification() != null) {

                    if (remoteMessage.getNotification().getTitle() != null) {
                        title = remoteMessage.getNotification().getTitle();
                    }

                    if (remoteMessage.getNotification().getBody() != null) {
                        message = remoteMessage.getNotification().getBody();
                    }
                } else {
                    if (remoteMessage.getData().get("title") != null) {
                        title = remoteMessage.getData().get("title");
                    }
                    if (remoteMessage.getData().get("message") != null) {
                        message = remoteMessage.getData().get("message");
                    }
                }
            }
        }

        getData(session.getId());
        sendNotification(title, message);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message) {

        //Intent intent = new Intent(this, NewsDetailActivity.class);
        Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantUtils.NEWS.TAG_ID, listModel.get(0).getNews_id());
        intent.putExtra(ConstantUtils.NEWS.TAG_JUDUL, listModel.get(0).getNews_judul());
        intent.putExtra(ConstantUtils.NEWS.TAG_ISI, listModel.get(0).getNews_isi());
        intent.putExtra(ConstantUtils.NEWS.TAG_TGL, listModel.get(0).getNews_tgl());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void getData(String userID) {

        final String REQUEST_TAG = "get request";

        final StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.NEWS + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.NEWS.TAG_TITLE);
                            //progressBar.setVisibility(View.GONE);
                            listModel = new ArrayList<ModelNews>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.NEWS.TAG_ID);
                                    String judul = object.getString(ConstantUtils.NEWS.TAG_JUDUL);
                                    String isi = object.getString(ConstantUtils.NEWS.TAG_ISI);
                                    String tgl = object.getString(ConstantUtils.NEWS.TAG_TGL);
                                    String stat = object.getString(ConstantUtils.NEWS.TAG_STATUS);

                                    model = new ModelNews(id, judul, isi, tgl, stat);
                                    listModel.add(model);
                                }
                            } else {
                                //Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //progressBar.setVisibility(View.GONE);
                        //Toast.makeText(PopupActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println(request);
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(MyFirebaseMessagingService.this).addToRequestQueue(request, REQUEST_TAG);
    }
}