package com.sdcc;

import com.sdcc.entity.ApplicationResponse;
import com.sdcc.entity.Monitor;
import com.sdcc.entity.Node;
import com.sdcc.enumeration.TaskExecutionMethodEnumeration;
import com.sdcc.util.*;
import java.util.HashMap;
import java.util.Scanner;


public class App {

    public static void main(String[] args) {

        HashMap<String, String> results = new HashMap<String, String>();
        Config config = new Config();
        Monitor monitor;

        try {
            monitor = GetJsonObject.getMonitorFromUrl(config.getProperty("Middleware_node_ip") + ":" + config.getProperty("Middleware_application_port") + config.getProperty("Middleware_test_status_path"));
            System.out.println(monitor.getTotalCpuUsage());
        } catch (Exception ex) {
            System.err.println("Cannot connect to the middleware server...");
            Shutdown.suhtdown();
        }

        System.out.println("System up and running! Type 'help' for the command list");

        Scanner scanner = new Scanner(System.in);

        String userInput;
        Node selectedNode;
        ApplicationResponse response = new ApplicationResponse();

        while(true) {
            userInput = scanner.nextLine();
            String[] temp1 = userInput.split("-args:");
            String[] temp2 = temp1[0].split("-name:");
            String command = temp2[0].replaceAll("\\s+", "_");
            String appName = null;
            if (!command.equals("result") && temp2.length>1)
                appName = temp2[1].replaceAll("\\s+", "");

            String arguments = null;
            if (temp1.length > 1)
                arguments = temp1[1];

            String urls = null;
            if (!command.equals("exec_local_"))
                urls = config.getProperty(command);


            if (command.isEmpty())
                System.err.println("Unknow command. Try 'help' for the command list");
            else if (command.equals("result")) {
               String[] ip_key = PrettyPrinter.printResulList(results);
               if (ip_key == null) {
                   System.out.println("");
               }
                try {
                    response = GetJsonObject.getAppResponseFromUrl("http://"+ip_key[0]+":8300/result/" + ip_key[1]);
                    PrettyPrinter.printResult(response);
                } catch (Exception ex) { }
            }
            else if (command.equals("help") || command.equals("HELP"))
                PrettyPrinter.printHelp();
            else if (command.equals("exec_local_")) {
                try {
                    response = LocalExecution.exec(appName, arguments);
                    PrettyPrinter.printResult(response);
                } catch (Exception ex) {
                    System.err.println("Unknow command, try 'help' for the command list");
                    System.out.print(ex.getMessage() + "\ncausa\n" + ex.getCause());
                }
            }
            else {
                String[] procedures = urls.split(",");
                try {
                    selectedNode = GetJsonObject.getNodeFromUrl(config.getProperty("Middleware_node_ip") + ":" + config.getProperty("Middleware_application_port") +procedures[0]);

                    String completeUrl = "http://" + selectedNode.getIp() + ":" +
                            config.getProperty("Node_listening_port") + procedures[1] + appName;
                    if (arguments != null)
                        completeUrl = completeUrl.concat("/" + arguments);

                    response = GetJsonObject.getAppResponseFromUrl(completeUrl);
                    if (response.getExecutionMethod() == TaskExecutionMethodEnumeration.POST_RESULT) {
                        results.put(response.getDownloadKey(), selectedNode.getIp());
                    }
                    PrettyPrinter.printResult(response);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.err.println(ex.getCause());
                }
            }
        }
    }

}
