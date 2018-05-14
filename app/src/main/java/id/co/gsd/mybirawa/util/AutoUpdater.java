package id.co.gsd.mybirawa.util;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Aang on 16-Nov-16.
 * Dibuat oleh : Gilang Rahman
 * Kelas ini berisi modul - modul untuk fungsi auto update pada aplikasi.
 */


public class AutoUpdater {

    private Context context;

    public AutoUpdater(Context ctx){
        context = ctx;
    }

    public void apkDownloader(String url, String title){
        final ProgressDialog pd= ProgressDialog.show(context, "Please Wait..", "Downloading data", false, false);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "mforce_tlt.apk";

        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists()){
            file.delete();//file.delete() - test this, I think sometimes it doesnt work
        }

        //get url of app on server
        //String url = url;

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Telkom Landmark Tower");
        request.setTitle(title);

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                pd.dismiss();
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri,"application/vnd.android.package-archive"
                );//manager.getMimeTypeForDownloadedFile(downloadId)
                (context).startActivity(install);

                context.unregisterReceiver(this);
                //(Activity)context.;
            }
        };
        //register receiver for when .apk download is compete
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
/*
    public void apkDownloader(String url, String title){
        final ProgressDialog pd= ProgressDialog.show(context, "Please Wait..", "Downloading data", false, false);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "mforce_tlt.apk";

        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists()){
            file.delete();//file.delete() - test this, I think sometimes it doesnt work
        }

        //get url of app on server
        //String url = url;

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Telkom Landmark Tower");
        request.setTitle(title);

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                pd.dismiss();
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //install.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                //install.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(uri,"application/vnd.android.package-archive"
                        );//manager.getMimeTypeForDownloadedFile(downloadId)
                (context).startActivity(install);

                context.unregisterReceiver(this);
                //(Activity)context.;
            }
        };
        //register receiver for when .apk download is compete
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
*/
    public void cekServer(){
        new ServerCheckTask().execute();
    }


    public class ServerCheckTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;
        String resultData[];
        boolean checkedServer;
        Context ctx;

        ServerCheckTask() {
            resultData = new String[5];
            checkedServer = false;
            ctx = context;
        }

        protected void onPreExecute(){
            //pd = ProgressDialog.show(LoginActivity.this, "", "Checking Server..");
            //pd.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                ServerConnectionManager conn = new ServerConnectionManager();
                resultData = conn.serverCheck();

                if(resultData[0]!=null){
                    checkedServer = true;
                }

            }
            catch (Exception e){
                checkedServer = false;
            }
            return null;
        }

        protected void onPostExecute(Void result){
            //pd.dismiss();
            AlertDialog.Builder alertadd = new AlertDialog.Builder(ctx);

            if (checkedServer){
                if(resultData[0].equals("206 UPDATE")){
                    new AlertDialog.Builder(ctx)

                            .setTitle("Update Available v."+resultData[1])
                            .setMessage(Html.fromHtml(""
                                    +"ChangeLog ("+resultData[2].substring(0,10)
                                    +") : <br><br>" + resultData[4]))
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AutoUpdater update = new AutoUpdater(ctx);
                                    //update.apkDownloader(resultData[3],resultData[1]);
                                    update.apkDownloader(resultData[3],resultData[1]);
                                }

                            })
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(1);
                                }

                            })
                            .show();

                }

            }else{

                Toast toast = Toast.makeText(ctx,"Cannot connect to server", Toast.LENGTH_SHORT);
                toast.setGravity(1, 0, 140);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {

            Toast toast = Toast.makeText(ctx,"Cannot connect to server", Toast.LENGTH_SHORT);
            toast.setGravity(1, 0, 140);
            toast.show();
        }
    }
}
