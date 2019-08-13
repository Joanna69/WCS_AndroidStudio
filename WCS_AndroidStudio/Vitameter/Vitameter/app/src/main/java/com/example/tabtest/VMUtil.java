package com.example.tabtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class VMUtil {


    private static void scanMedia(String path, Context context) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static ArrayList<Integer> readDataFromFile(String filename, Context context) throws IOException {
        //File file = new File("C:\\Users\\mittnaca\\AndroidStudioProjects\\Vitameter4\\Vitameter2\\Vitameter\\app\\src\\main\\res\\raw");

        if (!isExternalStorageWritable()) {
            throw new IOException();
        }


        //File[] documentsRoot = getActivity().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS);
        //File testFile = new File (documentsRoot[0] + "/vitameter/test.txt");
        File documentsRoot = new File ("/storage/emulated/0/Documents/vitameter");
        File testFile = new File (documentsRoot + "/" + filename);
        scanMedia(testFile.toString(), context);


        //File file = new File("res\\raw\\test.txt");
        BufferedReader br = new BufferedReader(new FileReader(testFile));

        //int[] yAxisData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayList<Integer> yAxisData = new ArrayList<>();


        while (br.ready()) {
            try {
                yAxisData.add(Integer.parseInt(br.readLine()));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return yAxisData;
    }


    public static ArrayList<Integer> readCo2DataFromFile(String filename, Context context) throws IOException {


        if (!isExternalStorageWritable()) {
            throw new IOException();
        }


        File documentsRoot = new File ("/storage/emulated/0/Documents/vitameter");
        File testFile = new File (documentsRoot + "/" + filename);
        scanMedia(testFile.toString(), context);


        BufferedReader br = new BufferedReader(new FileReader(testFile));

        ArrayList<Integer> yAxisData = new ArrayList<>();


        if (br.ready()) {
            try {
                String type = br.readLine();

            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }


        boolean co2 = false;

        while (br.ready()) {
            try {
                String line = br.readLine();
                if (line.equals("CO2")) {
                    co2 = true;
                    line = br.readLine();
                } else if (line.equals("UVI")) {
                    co2 = false;
                }
                if (co2) {
                    yAxisData.add(Integer.parseInt(line));
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return yAxisData;
    }


    public static ArrayList<Integer> readUviDataFromFile(String filename, Context context) throws IOException {


        if (!isExternalStorageWritable()) {
            throw new IOException();
        }


        File documentsRoot = new File ("/storage/emulated/0/Documents/vitameter");
        File testFile = new File (documentsRoot + "/" + filename);
        scanMedia(testFile.toString(), context);


        BufferedReader br = new BufferedReader(new FileReader(testFile));

        ArrayList<Integer> yAxisData = new ArrayList<>();


        if (br.ready()) {
            try {
                String type = br.readLine();

            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }


        boolean uvi = false;

        while (br.ready()) {
            try {
                String line = br.readLine();
                if (line.equals("UVI")) {
                    uvi = true;
                    line = br.readLine();
                } else if (line.equals("Steps")) {
                    uvi = false;
                }
                if (uvi) {
                    yAxisData.add(Integer.parseInt(line));
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return yAxisData;
    }


    public static ArrayList<Integer> readStepDataFromFile(String filename, Context context) throws IOException {


        if (!isExternalStorageWritable()) {
            throw new IOException();
        }


        File documentsRoot = new File ("/storage/emulated/0/Documents/vitameter");
        File testFile = new File (documentsRoot + "/" + filename);
        scanMedia(testFile.toString(), context);


        BufferedReader br = new BufferedReader(new FileReader(testFile));

        ArrayList<Integer> yAxisData = new ArrayList<>();


        if (br.ready()) {
            try {
                String type = br.readLine();

            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }


        boolean steps = false;

        while (br.ready()) {
            try {
                String line = br.readLine();
                if (line.equals("Steps")) {
                    steps = true;
                    line = br.readLine();
                } else if (line.equals("VOC")) {
                    steps = false;
                }
                if (steps) {
                    yAxisData.add(Integer.parseInt(line));
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return yAxisData;
    }
}
