package com.sdcc.util;

import com.sdcc.entity.ApplicationResponse;
import com.sdcc.enumeration.ApplicationStatusEnumeration;

import com.sdcc.entity.Application;
import org.apache.commons.lang.ArrayUtils;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class LocalExecution {

    public static ApplicationResponse exec(String appName, String args) throws IOException, JSONException, InterruptedException {
        ApplicationResponse response;// = new ApplicationResponse();
        Config config = new Config();

        String[] launchParameters = new String[]{"java", "-jar", appName};
        String[] params = null;

        if (args != null)
            params = args.split(",");

        if (params != null && params.length > 0)
            launchParameters = (String[]) ArrayUtils.addAll(launchParameters, params);

        if (!fileCheck(appName)) {
            Application app = GetJsonObject.getAppFromUrl(config.getProperty("Middleware_node_ip") + ":" + config.getProperty("Middleware_application_port") + "/application/find/" + appName);

            if (app.getStatus() == ApplicationStatusEnumeration.INVALID) {
                response = new ApplicationResponse();
                response.setSystemError("Application not found in S3 repository!");
                return response;
            }

            else if (app.getStatus().equals(ApplicationStatusEnumeration.STOPPED)) {
                response = new ApplicationResponse();
                response.setSystemError("Application status -> STOPPED! Not authorized to run app!");
                return response;
            }

            System.out.println("Download application from S3...");
            FileDownloader.downloadFromS3(app.getBucketName(), app.getAppName());

            //Se ci stanno eccezioni quando chiamo il resttemplate allora vuol dire che non esiste e ritorno un messaggio...sempre APplicationResponse
            response = execute(app.getAppName(), launchParameters);

        }
        else {

            response = execute(appName, launchParameters);
        }

        return response;

    }

    private static ApplicationResponse execute(String appName, String[] params) throws IOException, InterruptedException {
        Process ps = Runtime.getRuntime().exec(params);
        ApplicationResponse response = new ApplicationResponse();

        ps.waitFor();

        BufferedReader brout = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        BufferedReader brerr = new BufferedReader(new InputStreamReader(ps.getErrorStream()));

        for (String line = brout.readLine(); line != null; line = brout.readLine()) {
            response.updateOutput(line);
        }

        for (String line = brerr.readLine(); line != null; line = brerr.readLine()) {
            response.updateError(line);
        }

        response.setExitCode(ps.exitValue());

        return response;
    }

    private static boolean fileCheck(String appName) {
        File f = new File(appName);
        if (f.exists() && !f.isDirectory())
            return true;
        return false;
    }
}
