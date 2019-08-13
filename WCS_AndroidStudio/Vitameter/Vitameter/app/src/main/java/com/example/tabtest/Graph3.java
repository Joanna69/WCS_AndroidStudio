package com.example.tabtest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class Graph3 extends Fragment {


    // Amount of steps
    private TextView textView1;
    // Amount of steps in progress bar
    private TextView textView2;
    ArrayList<Integer> steps = new ArrayList<>();
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph3, container, false);

        try {
            steps = VMUtil.readStepDataFromFile("data.txt", getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stringStep = String.valueOf(steps.get(0));

        textView1 = (TextView) rootView.findViewById(R.id.amountSteps);
        textView2 = (TextView) rootView.findViewById(R.id.number_of_steps);

        progressBar1 = (ProgressBar) rootView.findViewById(R.id.background_progressbar);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.stats_progressbar);
        progressBar1.setMax(10000);
        progressBar2.setProgress(Integer.parseInt(stringStep));

        textView2.setText(stringStep + "/10000");


        textView1.setText("Amount of Steps");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //readAndGraphData();
    }
}