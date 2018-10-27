package com.sdcc.util;

import java.io.*;

public class FileOp {

    static int nStatsCount = 0;

    static public String getContents(String sFileName) {

        try {
            BufferedReader oReader = new BufferedReader(new FileReader(sFileName));
            String sLine, sContent = "";
            while ((sLine=oReader.readLine()) != null) {
                sContent += (sContent=="")?sLine: ("\r\n"+sLine);
            }
            oReader.close();
            return sContent;
        }
        catch (IOException oException) {
            throw new IllegalArgumentException("Invalid file path/File cannot be read: \n" + sFileName);
        }
    }
    static public void setContents(String sFileName, String sContent) {
        try {
            File oFile = new  File(sFileName);
            if (!oFile.exists()) {
                oFile.createNewFile();
            }
            if (oFile.canWrite()) {
                BufferedWriter oWriter = new BufferedWriter(new FileWriter(sFileName));
                oWriter.write (sContent);
                oWriter.close();
            }
        }
        catch (IOException oException) {
            throw new IllegalArgumentException("Invalid folder path/File cannot be written: \n" + sFileName);
        }
    }
    public static synchronized void appendContents(String sFileName, String sContent) {
        try {

            File oFile = new File(sFileName);
            if (!oFile.exists()) {
                oFile.createNewFile();
            }
            if (oFile.canWrite()) {
                BufferedWriter oWriter = new BufferedWriter(new FileWriter(sFileName, true));
                oWriter.write (sContent);
                oWriter.close();
            }

        }
        catch (IOException oException) {
            throw new IllegalArgumentException("Error appending/File cannot be written: \n" + sFileName);
        }
    }
}
