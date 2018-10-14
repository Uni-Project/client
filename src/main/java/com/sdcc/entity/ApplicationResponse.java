package com.sdcc.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdcc.enumeration.TaskExecutionMethodEnumeration;


public class ApplicationResponse {

    private String applicationOutput;
    private String applicationError;
    private String SystemError;
    private String downloadKey;
    private TaskExecutionMethodEnumeration executionMethod;
    private int exitCode;


    public String getApplicationOutput() {
        return applicationOutput;
    }

    public String getApplicationError() {
        return applicationError;
    }

    public String getSystemError() {
        return SystemError;
    }

    public String getDownloadKey() {
        return downloadKey;
    }

    public TaskExecutionMethodEnumeration getExecutionMethod() {
        return executionMethod;
    }

    public int getExitCode() {
        return exitCode;
    }

    public ApplicationResponse() {
        this.applicationOutput = "";

        this.applicationError = "";
    }

    public void setApplicationOutput(String applicationOutput) {
        this.applicationOutput = applicationOutput;
    }

    public void setApplicationError(String applicationError) {
        this.applicationError = applicationError;
    }

    public void setSystemError(String systemError) {
        SystemError = systemError;
    }

    public void setDownloadKey(String downloadKey) {
        this.downloadKey = downloadKey;
    }

    public void setExecutionMethod(TaskExecutionMethodEnumeration executionMethod) {
        this.executionMethod = executionMethod;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public void updateError(String s) {
        this.applicationError = this.applicationError + "\n" + s;
    }

    public void updateOutput(String s) {
        this.applicationOutput = this.applicationOutput + "\n" + s;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
