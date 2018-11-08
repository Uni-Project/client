package com.sdcc.util;

import com.google.gson.Gson;
import com.sdcc.entity.ApplicationResponse;
import com.sdcc.entity.Monitor;
import com.sdcc.entity.Node;
import com.sdcc.entity.Application;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GetJsonObject {

    public static JSONObject getObjectFromUrl(String url) throws IOException, JSONException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }

    public static Monitor getMonitorFromUrl(String url) throws IOException, JSONException {
        return new Gson().fromJson(getObjectFromUrl(url).toString(), Monitor.class);
    }

    public static Node getNodeFromUrl(String url) throws IOException, JSONException {
        return new Gson().fromJson(getObjectFromUrl(url).toString(), Node.class);
    }

    public static ApplicationResponse getAppResponseFromUrl(String url) throws IOException, JSONException {
        return new Gson().fromJson(getObjectFromUrl(url).toString(), ApplicationResponse.class);
    }

    public static Application getAppFromUrl(String url) throws IOException, JSONException {
        return new Gson().fromJson(getObjectFromUrl(url).toString(), Application.class);
    }

    public static List<Node> getNodes(String url) throws IOException, JSONException {
        //TODO migliorare...
        JSONArray array = new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        ArrayList<Node> nodes = new ArrayList<Node>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                nodes.add(new Gson().fromJson(array.getJSONObject(i).toString(), Node.class));
            }
        }

        return nodes;
    }

    public static List<Application> getApps(String url) throws IOException, JSONException {
        //TODO migliorare...
        JSONArray array = new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        ArrayList<Application> apps = new ArrayList<Application>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                apps.add(new Gson().fromJson(array.getJSONObject(i).toString(), Application.class));
            }
        }

        return apps;
    }

}
