package com.sdcc.entity;


import com.sdcc.enumeration.MessageStatusEnumeration;

public class Monitor {

    private double totalCpuUsage;
    private String errorMessage;
    private MessageStatusEnumeration status;

    public double getTotalCpuUsage() {
        return totalCpuUsage;
    }

    public void setTotalCpuUsage(double totalCpuUsage) {
        this.totalCpuUsage = totalCpuUsage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MessageStatusEnumeration getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnumeration status) {
        this.status = status;
    }
}
