package com.sdcc;

import com.sdcc.entity.ApplicationResponse;
import com.sdcc.entity.Monitor;
import com.sdcc.entity.Node;
import com.sdcc.enumeration.TaskExecutionMethodEnumeration;
import com.sdcc.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

//==================================================
// WARNING!!!!!!
// There is not too many control on the input data!
// The usage is reserved for tests only!!
//==================================================
public class App {

    public static void main(final String[] args) {

        //Automatic tests
        if (args.length > 0) {
            /*
            args[0] = appName
            args[1] = 1) remote: the system select the node with the default policy
                      2) cloud: the computation is forced on the cloud node
                      3) local: the computation is forced on the local machine
            args[2] = RANDOM, GEO_POSITION
            args[3] = number of tests and threads to launch
            args[4] = "-args:param1,param2,param3,...
            */
            int numberOfTests;
            String policy = args[2];
            if (args[1].equals("cloud") || args[1].equals("local")) {
                numberOfTests = Integer.parseInt(args[2]);
                policy = "no policy";
            } else
                numberOfTests = Integer.parseInt(args[3]);
            final String filename = "testxxshxjkxnjkx.txt";
            final TestTimeUtils t = new TestTimeUtils();

            //date time
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            //String to append at the beginning of the file
            String init = "\n\n============TEST MODE===========\n" +
                    "date time: " + dateFormat.format(date) + "\n" +
                    "type of test: " + args[1] + "\n" +
                    "policy: " + policy + "\n" +
                    "number of requests: " + numberOfTests + "\n" +
                    "application name: " + args[0] + "\n\n";

            System.out.println(init);
            FileOp.appendContents(filename, init);

            for (int i = 0; i < numberOfTests; i++) {

                Node selectedNode = null;
                ApplicationResponse response = null;
                Config config = new Config();
                String nodeType = "";
                String execPath = "";

                long requestNode_init = 0;
                long requestNode_end = 0;
                long execApp_init = 0;
                long execApp_end = 0;


                //the requested node type
                if (args[1].equals("remote"))
                    nodeType = "/node/find/" + args[2];
                else if (args[1].equals("cloud"))
                    nodeType = "/node/cloud";
                else {
                    //local computation
                    String[] params = (args[3].split("-args:"));
                    String[] launchParameters = new String[]{"java", "-jar", args[0]};


                    try {
                        execApp_init = System.currentTimeMillis();
                        response = LocalExecution.exec(args[0], params[1]);
                        execApp_end = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FileOp.appendContents(filename, Long.toString(execApp_end - execApp_init) + "\n");
                }

                //Remote application execution
                if (!args[1].equals("local")) {
                    try {
                        requestNode_init = System.currentTimeMillis();
                        selectedNode = GetJsonObject.getNodeFromUrl(config.getProperty("Middleware_node_ip") + ":" +
                                config.getProperty("Middleware_application_port") + nodeType);
                        requestNode_end = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Build the link for the execution
                    execPath = "http://" + selectedNode.getIp() + ":" + config.getProperty("Node_listening_port") +
                            "/run/WAIT_FOR_RESULT/" + args[0];

                    if (args.length > 4) { //there are arguments to pass at the application!
                        String params = args[4].split("-args:")[1];
                        execPath = execPath.concat("/" + params);
                    }

                    try {
                        execApp_init = System.currentTimeMillis();
                        response = GetJsonObject.getAppResponseFromUrl(execPath);
                        execApp_end = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FileOp.appendContents(filename,
                            PrettyPrinter.printTestResult(selectedNode, response,
                                    requestNode_end-requestNode_init, execApp_end-execApp_init));
                    //FileOp.appendContents(filename, Long.toString(requestNode_end - requestNode_init + execApp_end - execApp_init) + "\n");
                    //FileOp.appendContents(filename + "_execapp.txt", Long.toString(execApp_end - execApp_init) + "\n");
                    //FileOp.appendContents(filename + "_selectnode.txt", Long.toString(requestNode_end - requestNode_init) + "\n");
                    //t.addExecTime(execApp_end - execApp_init);
                    //t.addSelectTime(requestNode_end - requestNode_init);
                }
            }
        }

        //single test
        else {
            HashMap<String, String> results = new HashMap<String, String>();
            Config config = new Config();
            Monitor monitor = null;

            try {
                monitor = GetJsonObject.getMonitorFromUrl(config.getProperty("Middleware_node_ip") + ":" + config.getProperty("Middleware_application_port") + config.getProperty("Middleware_test_status_path"));
            } catch (Exception ex) {
                System.err.println("Cannot connect to the middleware server...");
                Shutdown.suhtdown();
            }
            System.out.println("Middleware node up and running!\nMiddleware CPU Load: " + monitor.getTotalCpuUsage());
            System.out.println("Type 'help' for the command list");

            Scanner scanner = new Scanner(System.in);

            String userInput;
            Node selectedNode;
            ApplicationResponse response;

            while (true) {
                long startTime;
                long endTime;
                userInput = scanner.nextLine();
                String[] temp1 = userInput.split("-args:");
                String[] temp2 = temp1[0].split("-name:");
                String command = temp2[0].replaceAll("\\s+", "_");
                String appName = null;
                if (!command.equals("result") && temp2.length > 1)
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
                        startTime = System.currentTimeMillis();
                        response = GetJsonObject.getAppResponseFromUrl("http://" + ip_key[0] + ":8300/result/" + ip_key[1]);
                        endTime = System.currentTimeMillis();
                        PrettyPrinter.printResult(response, endTime-startTime, ip_key[0]);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (command.equals("help") || command.equals("HELP"))
                    PrettyPrinter.printHelp();
                else if (command.equals("nodes")) {
                    try {
                        PrettyPrinter.printNodesList(GetJsonObject.getNodes(config.getProperty("Middleware_node_ip") + ":" + config.getProperty("Middleware_application_port") + "/node"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (command.equals("apps")) {
                    try {
                        PrettyPrinter.printApplicationList(GetJsonObject.getApps(config.getProperty("Middleware_node_ip") + ":" + config.getProperty("Middleware_application_port") + "/application"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (command.equals("exec_local_")) {
                    try {
                        startTime = System.currentTimeMillis();
                        response = LocalExecution.exec(appName, arguments);
                        endTime = System.currentTimeMillis();
                        PrettyPrinter.printResult(response, endTime-startTime, "localhost");
                    } catch (Exception ex) {
                        System.err.println("Unknow command, try 'help' for the command list");
                        System.out.print(ex.getMessage() + "\ncausa\n" + ex.getCause());
                    }
                }
                else if (config.getProperty(command) != null){
                    String[] procedures = urls.split(",");
                    try {
                        startTime = System.currentTimeMillis();
                        selectedNode = GetJsonObject.getNodeFromUrl(config.getProperty("Middleware_node_ip") + ":" + config.getProperty("Middleware_application_port") + procedures[0]);
                        long partialElapsedTime = System.currentTimeMillis() - startTime;

                        String completeUrl = "http://" + selectedNode.getIp() + ":" +
                                config.getProperty("Node_listening_port") + procedures[1] + appName;
                        if (arguments != null)
                            completeUrl = completeUrl.concat("/" + arguments);
                        startTime = System.currentTimeMillis();
                        response = GetJsonObject.getAppResponseFromUrl(completeUrl);
                        endTime = System.currentTimeMillis();
                        if (response.getExecutionMethod() == TaskExecutionMethodEnumeration.POST_RESULT) {
                            results.put(response.getDownloadKey(), selectedNode.getIp());
                        }

                        PrettyPrinter.printResult(response, endTime-startTime+partialElapsedTime, selectedNode.getIp());
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                        System.err.println(ex.getCause());
                    }
                }
                else {
                    System.err.println("Unknow command. Try 'help' for the command list");
                }
            }
        }
    }

}
