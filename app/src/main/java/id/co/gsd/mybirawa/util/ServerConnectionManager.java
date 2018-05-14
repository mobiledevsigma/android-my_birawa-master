package id.co.gsd.mybirawa.util;

/**
 * Created by Gilang on 05-Nov-15.
 */



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.gsd.mybirawa.BuildConfig;

public class ServerConnectionManager {

    protected SessionManager session;
    //private String webURL = "http://192.168.100.17:80/peopletracker/services.php";
    //private String webURL = "http://180.250.242.69:8028/";
    private String webURL = "http://180.250.242.69:8031/";
    //private String webURL = "http://180.250.242.69:80/api_global";
    //private String webURL = "http://180.250.242.201:81/api_global";

    //protected DatabaseManager db  = new DatabaseManager(home.getApplicationContext(),null,null,1);

    private JSONObject serverRequestor(List nameValuePair){
        JSONObject json = null;

        /**
         * Menggunakan HttpClient
         */
        /*String responseBody;
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 25 * 60 * 1000);//25 menit
        HttpConnectionParams.setSoTimeout(params, 25 * 60 * 1000);//25 menit
        HttpClient httpclient = new DefaultHttpClient(params);

        HttpPost httppost = new HttpPost(this.webURL);
        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;
        JSONObject json = null;
        try{

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            httpResponse = httpclient.execute(httppost);
            httpEntity = httpResponse.getEntity();
            responseBody = EntityUtils.toString(httpEntity);
            //setRequestProperty("Connection", "Keep-Alive");
            json = new JSONObject(responseBody);

        }catch (Exception e) {
            e.printStackTrace();
        }*/

        /**
         * Menggunakan HttpUrlConnection
         */
        StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(this.webURL+"api_global");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(1 * 15 * 1000);
            urlConnection.setReadTimeout(1 * 15 * 1000);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(nameValuePair));
            writer.flush();
            writer.close();
            os.close();

            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){

                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                    System.out.println("testi"+br.toString());
                    System.out.println("testi2"+sb.toString());
                    json = new JSONObject(sb.toString());
                    br.close();
                }catch(JSONException e){
                    e.printStackTrace();
                }

                System.out.println(""+sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return json;
    }

    private JSONObject serverRequestorModifierPost(List nameValuePair, String webUrl){
        JSONObject json = null;


        /**
         * Menggunakan HttpUrlConnection
         */
        StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(1 * 15 * 1000);
            urlConnection.setReadTimeout(1 * 15 * 1000);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(nameValuePair));
            writer.flush();
            writer.close();
            os.close();

            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){

                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                    System.out.println("testi"+br.toString());
                    System.out.println("testi2"+sb.toString());
                    json = new JSONObject(sb.toString());
                    br.close();
                }catch(JSONException e){
                    e.printStackTrace();
                }

                System.out.println(""+sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return json;
    }


    private JSONObject serverRequestorModifierGet2(String webUrl) {
        HttpClient httpClient;
        HttpGet httpGet = new HttpGet(webUrl);
        InputStream is = null;
        JSONObject result = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
            HttpConnectionParams.setSoTimeout(params, 60 * 1000);
            httpClient = new DefaultHttpClient(params);
            //Log.d(TAG, "executing...");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            StatusLine status = httpResponse.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK && httpResponse != null) {
                /** mengambil response string dari server */
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                is.close();
                result = new JSONObject(stringBuilder.toString());
            }
        } catch (Exception e) {
           System.out.println("diatas"+e);
        }

        return result;

    }
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public String[] serverCheck(){
        String[] resultData = new String[5];

        String versionCode = BuildConfig.VERSION_CODE+"";

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("method","checkServer"));
        nameValuePairList.add(new BasicNameValuePair("versionCode",versionCode));

        try{
            JSONObject json = serverRequestor(nameValuePairList);

            resultData[0] = json.getString("status");
            if(resultData[0].equals("206 UPDATE")){
                resultData[1] = json.getString("version_name");
                resultData[2] = json.getString("version_date");
                resultData[3] = json.getString("version_url");
                resultData[4] = json.getString("version_desc");
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public String userRegister(String email, String name, String phone, String gender, String pass){

        String resultData = "";

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("name",name));
        nameValuePairList.add(new BasicNameValuePair("email",email));
        nameValuePairList.add(new BasicNameValuePair("telp",phone));
        nameValuePairList.add(new BasicNameValuePair("sex",gender));
        nameValuePairList.add(new BasicNameValuePair("password",pass));
        //System.out.println(GCMToken);
        try{
            String url = webURL+"api_akses/register";
            JSONObject json = serverRequestorModifierPost(nameValuePairList,url);
            System.out.println(json.toString());
            if(json.getInt("success")==1){
                resultData = "200 OK";
            }else{
                resultData = "500 NOK";
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return resultData;
    }

    //login, ambil data
    public String[] getUserLogin(String email, String pass, String device_id, String deviceToken){
        String[] resultData = null;
        resultData = new String[20];
        resultData[0] = "";

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("username",email));
        nameValuePairList.add(new BasicNameValuePair("password",pass));
        nameValuePairList.add(new BasicNameValuePair("device_id",device_id));
        //nameValuePairList.add(new BasicNameValuePair("version_apk",version));
        nameValuePairList.add(new BasicNameValuePair("device_token",deviceToken));
        System.out.println(deviceToken);
        try{
            String url = webURL+"api_akses/auth";
            JSONObject json = serverRequestorModifierPost(nameValuePairList,url);
            System.out.println("ini"+json.toString());
            if(json.getInt("success")==1){
                resultData[0] = "200 OK";
                resultData[1] = json.getString("id_user");
                resultData[2] = json.getString("username");
                resultData[3] = json.getString("name");
                resultData[4] = json.getString("telp");
                resultData[5] = json.getString("email");
                resultData[6] = json.getString("id_direktorat");
                resultData[7] = json.getString("nama_direktorat");
                resultData[8] = json.getString("id_service_team");
                resultData[9] = json.getString("nama_service_team");
            }else{
                resultData[0] = "500 NOK";
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public ArrayList<HashMap<String, String>> getEvents(){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/event";
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                    if(json.getJSONArray("event")!=null){
                        //db.deleteOrderTable();
                        JSONArray arr = json.getJSONArray("event");

                        for (int i = 0; i < arr.length(); i++){
                            HashMap<String, String> values = new HashMap<String, String>();
                            values.put("event_id", arr.getJSONObject(i).getString("event_id"));
                            values.put("event_date_time", arr.getJSONObject(i).getString("event_date_time"));
                            values.put("event_image_preview", arr.getJSONObject(i).getString("event_image_preview"));
                            values.put("event_name", arr.getJSONObject(i).getString("event_name"));

                            resultData.add(values);
                        }
                    }else{
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("listType", "0");
                        values.put("name", "no data found");
                        resultData.add(values);
                    }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }

    public ArrayList<HashMap<String, String>> getRundown(String event_id){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/rundownByEvent/"+event_id;
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("rundown")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("rundown");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("rundown_id", arr.getJSONObject(i).getString("rundown_id"));
                        values.put("rundown_name", arr.getJSONObject(i).getString("rundown_name"));
                        values.put("rundown_date", arr.getJSONObject(i).getString("rundown_date"));
                        values.put("rundown_time", arr.getJSONObject(i).getString("rundown_time"));
                        values.put("description", arr.getJSONObject(i).getString("description"));
                        values.put("rundown_image", arr.getJSONObject(i).getString("rundown_image"));
                        values.put("event_date_time", arr.getJSONObject(i).getString("event_date_time"));
                        values.put("event_image_preview", arr.getJSONObject(i).getString("event_image_preview"));
                        values.put("event_name", arr.getJSONObject(i).getString("event_name"));

                        resultData.add(values);
                        System.out.println(values.toString());
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }


    public ArrayList<HashMap<String, String>> getStandCat(){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/standCat";
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("stand_cat")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("stand_cat");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("category_id", arr.getJSONObject(i).getString("category_id"));
                        values.put("category_name", arr.getJSONObject(i).getString("category_name"));
                        values.put("category_image", arr.getJSONObject(i).getString("category_image"));

                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }

    public ArrayList<HashMap<String, String>> getStands(String id, String user_id){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            //String url = webURL+"api_data/standVotedByUser/"+user_id+"/"+id;
            String url = webURL+"api_data/featuresByCat/"+id;
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("features")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("features");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("stand_id", arr.getJSONObject(i).getString("id_features"));
                        values.put("name", arr.getJSONObject(i).getString("name_features"));
                        //values.put("address", arr.getJSONObject(i).getString("address"));
                        values.put("address", "");
                        values.put("category_id", arr.getJSONObject(i).getString("category_id"));
                        //values.put("phone", arr.getJSONObject(i).getString("phone"));
                        values.put("phone", "");
                        values.put("owner", "");
                        values.put("email", "");
                        values.put("since_date", "");
                        values.put("rek_num", "");
                        values.put("rek_owner", "");
                        values.put("rek_bank", "");
                        values.put("description", arr.getJSONObject(i).getString("desc_features"));
                        values.put("stand_image", arr.getJSONObject(i).getString("pic_features"));
                        values.put("link_blanja", "");
                        values.put("category_name", arr.getJSONObject(i).getString("cat_features"));
                        values.put("voted", "");
                        values.put("vote_count", "");
                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }

    public ArrayList<HashMap<String, String>> getNews(String user_id){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/newsByUser/"+user_id;
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("news")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("news");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("news_id", arr.getJSONObject(i).getString("news_id"));
                        values.put("news_content", arr.getJSONObject(i).getString("news_content"));
                        values.put("news_date", arr.getJSONObject(i).getString("news_date"));
                        values.put("news_title", arr.getJSONObject(i).getString("news_title"));
                        values.put("status", arr.getJSONObject(i).getString("status"));
                        values.put("id_user_news", arr.getJSONObject(i).getString("id_user_news"));
                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }

    public ArrayList<HashMap<String, String>> getBuildingImage(String id){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/imageCategory/"+ id;
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("image")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("image");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("image_tlt", arr.getJSONObject(i).getString("image_tlt"));
                        values.put("image_name", arr.getJSONObject(i).getString("image_name"));

                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }

    public ArrayList<HashMap<String, String>> getQuiz(){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/quiz";
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("quiz")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("quiz");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("id", arr.getJSONObject(i).getString("id"));
                        values.put("asker", arr.getJSONObject(i).getString("asker"));
                        values.put("question", arr.getJSONObject(i).getString("question"));
                        values.put("correct_answer", arr.getJSONObject(i).getString("correct_answer"));
                        values.put("enabled", arr.getJSONObject(i).getString("enabled"));
                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }

    public String submitVote(String user_id, String stand_id, String cat_id){

        String resultData = "";

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("id_stand",stand_id));
        nameValuePairList.add(new BasicNameValuePair("user_id",user_id));
        nameValuePairList.add(new BasicNameValuePair("category_id",cat_id));
        //System.out.println(GCMToken);
        try{
            String url = webURL+"api_data/submitVote";
            JSONObject json = serverRequestorModifierPost(nameValuePairList,url);
            System.out.println(json.toString());
            if(json.getInt("success")==1){
                resultData = "200 OK";
            }else if(json.getInt("success")==2){
                resultData = "202 NOK";
            }else{
                resultData = "500 NOK";
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return resultData;
    }


    public String submitQuiz(String user_id, String quiz_id, String answer){

        String resultData = "";

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("user_id",user_id));
        nameValuePairList.add(new BasicNameValuePair("id_quiz",quiz_id));
        nameValuePairList.add(new BasicNameValuePair("answer_value",answer));
        //System.out.println(GCMToken);
        try{
            String url = webURL+"api_data/submitQuiz";
            JSONObject json = serverRequestorModifierPost(nameValuePairList,url);
            System.out.println(json.toString());
            if(json.getInt("success")==1){
                resultData = "200 OK";
            }else if(json.getInt("success")==2){
                resultData = "202 NOK";
            }else{
                resultData = "500 NOK";
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public String submitFeedback(String user_id, String answer){

        String resultData = "";

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("user_id",user_id));
        nameValuePairList.add(new BasicNameValuePair("answer_value",answer));
        //System.out.println(GCMToken);
        try{
            String url = webURL+"api_data/submitFeedback";
            JSONObject json = serverRequestorModifierPost(nameValuePairList,url);
            System.out.println(json.toString());
            if(json.getInt("success")==1){
                resultData = "200 OK";
            }else if(json.getInt("success")==2){
                resultData = "202 NOK";
            }else{
                resultData = "500 NOK";
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return resultData;
    }



    public ArrayList<HashMap<String, String>> getImageLoc(String cat_id){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/imageLocation/" + cat_id;
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("location")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("location");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("id", arr.getJSONObject(i).getString("id"));
                        values.put("name", arr.getJSONObject(i).getString("name"));
                        values.put("desc", arr.getJSONObject(i).getString("desc"));

                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }


    public ArrayList<HashMap<String, String>> getImageList(String loc_id){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/imageList/" + loc_id;
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("image")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("image");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("image_tlt", arr.getJSONObject(i).getString("image_tlt"));
                        values.put("image_name", arr.getJSONObject(i).getString("image_name"));
                        values.put("image_desc", arr.getJSONObject(i).getString("image_desc"));
                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }

    public ArrayList<HashMap<String, String>> getImageList2(String loc_id){
        ArrayList<HashMap<String, String>> resultData = null;

        try{
            String url = webURL+"api_data/imageList2/" + loc_id;
            JSONObject json = serverRequestorModifierGet2(url);
            resultData = new ArrayList<HashMap<String, String>>();
            System.out.println(json);
            if(json!=null){

                if(json.getJSONArray("image")!=null){
                    //db.deleteOrderTable();
                    JSONArray arr = json.getJSONArray("image");

                    for (int i = 0; i < arr.length(); i++){
                        HashMap<String, String> values = new HashMap<String, String>();
                        values.put("image_tlt", arr.getJSONObject(i).getString("image_tlt"));
                        values.put("image_name", arr.getJSONObject(i).getString("image_name"));
                        values.put("image_desc", arr.getJSONObject(i).getString("image_desc"));
                        resultData.add(values);
                    }
                }else{
                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("listType", "0");
                    values.put("name", "no data found");
                    resultData.add(values);
                }

            }

            //System.out.println("log : ");
        }catch (Exception e) {
            System.out.println("di bawah : "+e);
        }

        return resultData;
    }
}
