package com.sdcc.util;

import com.google.gson.Gson;
import com.sdcc.entity.ApplicationResponse;
import com.sdcc.entity.Monitor;
import com.sdcc.entity.Node;
import com.sdcc.entity.Application;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

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

}
