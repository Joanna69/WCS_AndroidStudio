package com.example.tabtest;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class Happy extends Fragment {

    // Parameters for seekbar
    private SeekBar seekBar1;
    private TextView textView1;

    private SeekBar seekBar2;
    private TextView textView2;

    private static int DELTA_VALUE = 5;
    private static final String LOGTAG = "Motion";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.happy, container, false);

        final TextView out = (TextView) rootView.findViewById(R.id.out);


        this.seekBar1 = (SeekBar) rootView.findViewById(R.id.seekBar);
        this.textView1 = (TextView) rootView.findViewById(R.id.textView);

        this.seekBar2 = (SeekBar) rootView.findViewById(R.id.seekBar2);
        this.textView2 = (TextView) rootView.findViewById(R.id.textView2);


        this.textView1.setText("Happiness Score: " + seekBar1.getProgress() + "/" + seekBar1.getMax());
        this.textView2.setText("Health Score: " + seekBar2.getProgress() + "/" + seekBar2.getMax());

        this.seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress;
                textView1.setText("Happiness Score: " + seekBar.getProgress() + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView1.setText("Happiness Score: " + seekBar.getProgress() + "/" + seekBar.getMax());
            }

        });

        this.seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress;
                textView2.setText("Health Score: " + seekBar.getProgress() + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView2.setText("Health Score: " + seekBar.getProgress() + "/" + seekBar.getMax());
            }

        });


        return rootView;
    }



}
