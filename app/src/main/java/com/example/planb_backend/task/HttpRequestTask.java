package com.example.planb_backend.task;

import android.os.AsyncTask;

import com.example.planb_backend.request.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HttpRequestTask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {

        JSONObject msg = new JSONObject();
        try {
            msg.put("username", "test");
            msg.put("password", "123");
            HttpRequest request = new HttpRequest();
            request.post("http://192.168.31.183:3000/send_notification", msg.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
