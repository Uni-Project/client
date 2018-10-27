package com.sdcc.util;

import com.fasterxml.jackson.databind.util.TypeKey;
import com.sdcc.entity.ApplicationResponse;
import com.sdcc.entity.Node;
import com.sdcc.enumeration.TaskExecutionMethodEnumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class PrettyPrinter {

    public static void printHelp() {

    }

    public static void printResult(ApplicationResponse applicationResponse) {
            System.out.println(
                            "------------------------------------\n" +
                            "Application results:\n" +
                            "Application output: " + applicationResponse.getApplicationOutput() + "\n" +
                            "Application error: " + applicationResponse.getApplicationError() + "\n" +
                            "Application exit code: " + applicationResponse.getExitCode() + "\n" +
                            "System error: " + applicationResponse.getSystemError() + "\n" +
                            "Unique download key: " + applicationResponse.getDownloadKey() + "\n" +
                            "------------------------------------"
            );

    }

    public static void printNodesList(List<Node> nodes) {

    }

    public static String printTestResult(Node node, ApplicationResponse response, long nodeSelection, long execApp) {
        String state = "fail";
        if (response.getSystemError() == null)
            state = "success";

        return  "--------------------------------------\n" +
                "Node: " + node.getIp() + "\n" +
                "Execution: " + state + "\n" +
                "Node selection time: " + nodeSelection + "ms\n" +
                "App execution time: " + execApp + "ms\n";
    }

    public static String[] printResulList(HashMap<String, String> results) {

        if (results.size() == 0) {
            System.out.println("No result key stored!");
            return null;
        }

        int i = 0;
        for (String name: results.keySet()) {
            String key = name.toString();
            String value = results.get(key).toString();
            System.out.println(i + " :  ["+key+" , " + value+"]");
            i++;
        }
        int select = Integer.parseInt(new Scanner(System.in).nextLine());

        if (select > results.size()) {
            System.err.println("Invalid entry!");
            return null;
        }

        String[] res = new String[2];

        res[0] = (String) results.values().toArray()[select];
        res[1] = (String) results.keySet().toArray()[select];

        results.remove(res[1]);

        return res;
    }



}
