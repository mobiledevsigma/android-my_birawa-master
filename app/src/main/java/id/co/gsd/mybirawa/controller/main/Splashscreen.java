package id.co.gsd.mybirawa.controller.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.gsd.mybirawa.BuildConfig;
import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.home.HomeActivity;
import id.co.gsd.mybirawa.controller.sc.MenuActivity;
import id.co.gsd.mybirawa.model.ModelVersion;
import id.co.gsd.mybirawa.util.SessionManager;
import id.co.gsd.mybirawa.util.connection.AppSingleton;
import id.co.gsd.mybirawa.util.connection.ConstantUtils;

public class Splashscreen extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1;
    private static final int SPLASH_TIME_OUT = 3000;
    private String TAG = "permission";
    private ModelVersion model;
    private List<ModelVersion> listModel;
    private String versionDevice, versionServer;
    private ProgressDialog progressDialog;
    private String namaFile, namaURL;
    private SessionManager session;
    private ProgressBar progressBar;
    private DownloadFile downloadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        progressDialog = new ProgressDialog(Splashscreen.this);
        progressBar = new ProgressBar(Splashscreen.this);
        session = new SessionManager(Splashscreen.this);
        versionDevice = BuildConfig.VERSION_NAME;
        progressBar.setVisibility(View.GONE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isStoragePermissionEnabled()) {
                    checkVersion();
                } else {
                    requestStoragePermission();
                }
            }
        }, SPLASH_TIME_OUT);


        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadFile.cancel(true);
            }
        });
    }

    //GET DATA
    private void checkVersion() {
        final String REQUEST_TAG = "get request";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantUtils.URL.VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantUtils.VERSION.TAG_TITLE);
                            listModel = new ArrayList<ModelVersion>();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString(ConstantUtils.VERSION.TAG_ID);
                                    String number = object.getString(ConstantUtils.VERSION.TAG_NUMBER);
                                    String url = object.getString(ConstantUtils.VERSION.TAG_URL);
                                    String note = object.getString(ConstantUtils.VERSION.TAG_NOTE);
                                    String dates = object.getString(ConstantUtils.VERSION.TAG_DATE);

                                    model = new ModelVersion(id, number, url, note, dates);
                                    listModel.add(model);

                                    versionServer = number;
                                    namaURL = url;
                                }

                                if (versionServer.equalsIgnoreCase(versionDevice)) {
                                    if (session.isLoggedIn()) {
                                        if(session.getRoleId().equals("12") || session.getRoleId().equals("14") || session.getRoleId().equals("11") || session.getRoleId().equals("98")){
                                            Intent intent = new Intent(Splashscreen.this, MenuActivity.class);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(Splashscreen.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        Intent intent = new Intent(Splashscreen.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    if (session.isLoggedIn()) {
                                        session.logoutUser();
                                    }
                                    new AlertDialog.Builder(Splashscreen.this)
                                            .setMessage("New Release is Available! You must download the latest APK")
                                            .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //new DownloadFile().execute(namaURL, "MyBirawa.apk");
                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                            }
                                        }
                                    }).show();
                                }

                            } else {
                                Toast.makeText(Splashscreen.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                                if (session.isLoggedIn()) {
                                    if(session.getRoleId().equals("12") || session.getRoleId().equals("14") || session.getRoleId().equals("11") || session.getRoleId().equals("98")){
                                        Intent intent = new Intent(Splashscreen.this, MenuActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(Splashscreen.this, HomeActivity.class);
                                        startActivity(intent);
                                    }

                                } else {
                                    finish();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(Splashscreen.this, e.toString(), Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            if (session.isLoggedIn()) {
                                if(session.getRoleId().equals("12") || session.getRoleId().equals("14") || session.getRoleId().equals("11") || session.getRoleId().equals("98")){
                                    Intent intent = new Intent(Splashscreen.this, MenuActivity.class);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(Splashscreen.this, HomeActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                finish();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Splashscreen.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                        if (session.isLoggedIn()) {
                            if(session.getRoleId().equals("12") || session.getRoleId().equals("14") || session.getRoleId().equals("11") || session.getRoleId().equals("98")){
                                Intent intent = new Intent(Splashscreen.this, MenuActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(Splashscreen.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            finish();
                        }
                    }
                });
        System.out.println(request);
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(Splashscreen.this).addToRequestQueue(request, REQUEST_TAG);
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("SETTINGS", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDialogPerm(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    public boolean isStoragePermissionEnabled() {
        return ContextCompat.checkSelfPermission(Splashscreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestStoragePermission() {
//        ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        showDialogPerm("The Permissions are required for this application. Please allow storage permission.",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            ActivityCompat.requestPermissions(Splashscreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
                        }
                    }
                });
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        if (requestCode == REQUEST_PERMISSIONS) {
            Log.d(TAG, "code    ---    " + requestCode);
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "all permission granted");
                    // process the normal flow
                    checkVersion();
                    //else any one or both the permissions are not granted
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                    // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showDialogOK("The Permissions are required for this application",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //checkAndRequestPermissions();
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                        Uri.fromParts("package", getPackageName(), null));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                break;
                                        }
                                    }
                                });
                    } else {
                        checkVersion();
                        Log.d("yes", "masuk");
                    }
                }
            }
        } else {
            Log.d("hasil code", String.valueOf(requestCode));
        }
    }

    class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress_red dialog
            progressDialog = new ProgressDialog(Splashscreen.this);
            // Set your progress_red dialog Title
            progressDialog.setTitle("Updating Application");
            // Set your progress_red dialog Message
            progressDialog.setMessage("Please Wait!");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show progress_red dialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                namaFile = Url[1];
                URLConnection connection = url.openConnection();
                connection.connect();

                // Detect the file lenghth
                int fileLength = connection.getContentLength();

                // Locate storage location
                String filepath = Environment.getExternalStorageDirectory().toString();
                Log.d("isi file path nya", filepath);

                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());

                // Save the downloaded file
                OutputStream output = new FileOutputStream(filepath + "/Download/" + namaFile);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress_red
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
                // Close connection
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                // Error Log
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress_red dialog
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            if (result != null) {
                Toast.makeText(Splashscreen.this, "Download error: " + result, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(Splashscreen.this, "File downloaded", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + "MyBirawa.apk")),
                        "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
