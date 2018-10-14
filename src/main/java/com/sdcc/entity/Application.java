package com.sdcc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sdcc.enumeration.ApplicationStatusEnumeration;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {

    private Long id;

    private String appName;

    private String appUrl;

    private String description;

    private ApplicationStatusEnumeration status;

    private Date createDate;

    private String bucketName;


    public Application() {}

    public Application(String URL, String appName, String description) {
        this.appUrl = URL;
    //    this.port = port;
        this.appName = appName;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getDescription() {
        return description;
    }

    public ApplicationStatusEnumeration getStatus() {
        return status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getBucketName() {
        return bucketName;
    }
}
