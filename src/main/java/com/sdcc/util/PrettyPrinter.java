package com.sdcc.util;

import com.sdcc.entity.Application;
import com.sdcc.entity.ApplicationResponse;
import com.sdcc.entity.Node;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class PrettyPrinter {

    public static void printHelp() {
        System.out.println(
                "=============================\n" +
                "        COMMAND LIST         \n"+
                "=============================\n"+
                "SINGLE TEST:\n"+
                "1) exec remote wait random -name:app_name -args:arg1,arg2,arg3\n"+
                "   run the application in a fog node selected with a random policy and wait for result\n"+
                "2) exec remote wait nearest -name:app_name -args:arg1,arg2,arg3\n"+
                "   run the application in the nearest fog node and wait fopr result\n"+
                "3) exec remote post random -name:app_name -args:arg1,arg2,arg3\n"+
                "   run the application in a fog node selected with a random policy and retrieve the result later\n"+
                "4) exec remote post nearest -name:app_name -args:arg1,arg2,arg3\n"+
                "   run the application in the nearest fog node and retrieve the result later\n"+
                "5) exec cloud wait -name:app_name -args:arg1,arg2,arg3\n"+
                "   run the application in the cloud node and wait for result\n"+
                "6) exec cloud post -name:app_name -args:arg1,arg2,arg3\n"+
                "   run the application in the cloud node and retrieve the result later\n"+
                "7) exec local -name:app_name -args:arg1,arg2,arg3\n"+
                "   run the application in the local machine\n"+
                "8) result\n"+
                "   show the result stored in the fog nodes\n"+
                "9) nodes"+
                "   show all the available nodes\n" +
                "10) apps\n" +
                "   show all the available applications\n\n"
        );
    }

    public static void printResult(ApplicationResponse applicationResponse, long elapsedTime, String nodeIp) {
            System.out.println(
                            "----------------------\n" +
                            "APPLICATION RESULT\n" +
                            "Application output: " + applicationResponse.getApplicationOutput() + "\n" +
                            "Application error: " + applicationResponse.getApplicationError() + "\n" +
                            "Application exit code: " + applicationResponse.getExitCode() + "\n" +
                            "System error: " + applicationResponse.getSystemError() + "\n" +
                            "Unique download key: " + applicationResponse.getDownloadKey() + "\n" +
                            "Selected node: " + nodeIp + "\n" +
                            "Elapsed time: " + elapsedTime + "ms\n" +
                            "-----------------------"
            );
    }

    public static void printNodesList(List<Node> nodes) {
        System.out.println("Available nodes: " + nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println(i + ": " + nodes.get(i).getIp());
        }
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
        System.out.print("Insert the result to download -> ");
        int select = Integer.parseInt(new Scanner(System.in).next());

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

    public static void printApplicationList(List<Application> apps) {
        System.out.println("Available apps: " + apps.size());
        for (int i = 0; i < apps.size(); i++) {
            System.out.println(i + ") [" + apps.get(i).getAppName() + "]");
            System.out.println("Status: " + String.valueOf(apps.get(i).getStatus()));
            System.out.println("Description:\n" + apps.get(i).getDescription());
        }
    }



}
