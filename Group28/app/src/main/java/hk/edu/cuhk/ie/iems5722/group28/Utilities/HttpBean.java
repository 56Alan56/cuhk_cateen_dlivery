package hk.edu.cuhk.ie.iems5722.group28.Utilities;


import static hk.edu.cuhk.ie.iems5722.group28.LoginUI.FragmentLogin.UID;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import hk.edu.cuhk.ie.iems5722.group28.ChatUI.Msg;
import hk.edu.cuhk.ie.iems5722.group28.MainUI.delivery.Posts;

public class HttpBean {
    public static final String serverIP = "47.250.46.238";
    //public static final String serverIP = "10.0.2.2:5000";//For Localtest

    public static String httpGet(String httpURL){
        InputStream inputStream = null;
        String results = "";
        int reconn_times = 5;
        try{
            URL url = new URL(httpURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println(httpURL);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            if (response!=HttpURLConnection.HTTP_OK){
                System.out.println("Connection Failed" + response);
                for (int i = 0; i < reconn_times; i++) {
                    conn.connect();
                    System.out.println("Reconnecting: " + i);
                    Thread.sleep(3000);
                }
            }
            else{
                inputStream = conn.getInputStream();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine())!=null){
                    results += line;
                }
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(inputStream!= null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }


    public static String postMsg2Server(Msg msg) throws IOException {

        URL url = new URL("http://"+serverIP+"/send_message/"); //Server
        //SafeIterableMap<Object, Object> para_names;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        Uri.Builder builder = new Uri.Builder();
        // Build the parameters using ArrayList objects para_names and para_values
//        for (int i = 0; i < para_names.size(); i++) {
//            builder.appendQueryParameter(para_names.get(i), para_values.get(i));
//        }
        builder.appendQueryParameter("receiver_id", msg.Receiver);
        builder.appendQueryParameter("sender_id", msg.Sender);
        builder.appendQueryParameter("message", msg.MsgText);

        String query = builder.build().getEncodedQuery();
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Process the response
            System.out.println("Msg sent!");
            System.out.println(query);
        }
        return null;
    }

    public static String postTokentoAPI(String token) throws IOException {
        URL url = new URL("http://" + serverIP +"/submit_push_token/"); //Server

        System.out.println("sending token to server!");

        //SafeIterableMap<Object, Object> para_names;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        Uri.Builder builder = new Uri.Builder();
        // Build the parameters using ArrayList objects para_names and para_values
//        for (int i = 0; i < para_names.size(); i++) {
//            builder.appendQueryParameter(para_names.get(i), para_values.get(i));
//        }
        builder.appendQueryParameter("user_id", UID);
        builder.appendQueryParameter("token", token);

        String query = builder.build().getEncodedQuery();
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Process the response
            System.out.println(builder.toString());
            Log.d("HttpBean","Token Successfully Sent!");

        }
        return null;
    }

    public static String postPosts2Server(String time,String fees,String dest,String food,String canteenName) throws IOException {

        URL url = new URL("http://"+serverIP+"/push_posts/"); //Server
        //SafeIterableMap<Object, Object> para_names;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        Uri.Builder builder = new Uri.Builder();
        // Build the parameters using ArrayList objects para_names and para_values
//        for (int i = 0; i < para_names.size(); i++) {
//            builder.appendQueryParameter(para_names.get(i), para_values.get(i));
//        }


        builder.appendQueryParameter("canteen_name", canteenName);
        builder.appendQueryParameter("time", time);
        builder.appendQueryParameter("dest", dest);

        builder.appendQueryParameter("fees", fees);
        builder.appendQueryParameter("food", food);
        builder.appendQueryParameter("orderer",UID);

        String query = builder.build().getEncodedQuery();
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Process the response
            System.out.println("Msg sent!");
            System.out.println(query);
            return "1";
        }
        return "0";
    }

    public static String postStartStatus2Server(String deliverer_id,String food) throws IOException {

        URL url = new URL("http://"+serverIP+"/start/"); //Server
        //SafeIterableMap<Object, Object> para_names;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        Uri.Builder builder = new Uri.Builder();
        // Build the parameters using ArrayList objects para_names and para_values
//        for (int i = 0; i < para_names.size(); i++) {
//            builder.appendQueryParameter(para_names.get(i), para_values.get(i));
//        }


        builder.appendQueryParameter("deliverer_id", deliverer_id);
        builder.appendQueryParameter("food", food);
        String query = builder.build().getEncodedQuery();
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Process the response
            System.out.println("Msg sent!");
            System.out.println(query);
            return "1";
        }
        return "0";
    }

    public static String postFinishStatus2Server(String params,String food) throws IOException {
        String deliverer = params.split("To")[0];
        String orderer = params.split("To")[1];
        URL url = new URL("http://"+serverIP+"/finish/"); //Server
        //SafeIterableMap<Object, Object> para_names;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        Uri.Builder builder = new Uri.Builder();
        // Build the parameters using ArrayList objects para_names and para_values
//        for (int i = 0; i < para_names.size(); i++) {
//            builder.appendQueryParameter(para_names.get(i), para_values.get(i));
//        }


        builder.appendQueryParameter("food", food);
        builder.appendQueryParameter("deliverer", deliverer);
        builder.appendQueryParameter("orderer", orderer);
        String query = builder.build().getEncodedQuery();
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Process the response
            System.out.println("Msg sent!");
            System.out.println(query);
            return "1";
        }
        return "0";
    }
}

