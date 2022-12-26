package com.example.wordpro.tool;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wordpro.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ProgressDialog extends Dialog {

    public String TAG = "ProgressDialog";

    private Context context;
    private RdsConnection rdsConnection;

    private TextView nicknameTextView;
    private LineChart chart;
    private Typeface typeface;

    public ProgressDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_progress);

        this.context = context;
        rdsConnection = new RdsConnection();

        chartInitializer();
    }

    private void chartInitializer(){

        typeface = Typeface.createFromAsset(context.getAssets(), "font/main_font.ttf");

        chart = findViewById(R.id.dialogProgressLineChart);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);

        Legend l = chart.getLegend();
        l.setTypeface(typeface);
        l.setTextSize(15f);
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(typeface);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(typeface);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        int[] a = {10, 20, 30, 30, 40, 40, 40, 50, 50, 50, 40, 30, 30, 30, 20, 20, 20, 10, 10, 10};
        int[] b = {10, 10, 10, 10, 20, 30, 40, 60, 60, 50, 40, 20, 20, 10,  0, 20, 10,  0, 10, 10};

        chart.setData(generateLineData(a, b, Color.rgb(40, 90, 200), Color.rgb(255, 73, 73)));
        chart.invalidate();
    }

    private LineData generateLineData(int[] data1, int[] data2, int color1, int color2) {

        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();

        for (int index = 0; index < 20; index++) {
            entries1.add(new Entry(index + 0.5f, data1[index]));
            entries2.add(new Entry(index + 0.5f, data2[index]));
        }

        LineDataSet set1 = new LineDataSet(entries1, "Recommended Progress");
        set1.setColor(color1);
        set1.setLineWidth(2.5f);
        set1.setFillColor(color1);
        set1.setDrawCircles(false);
        set1.setDrawFilled(true);
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setDrawValues(false);
        set1.setFillAlpha(100);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineDataSet set2 = new LineDataSet(entries2, "Progress");
        set2.setColor(color2);
        set2.setLineWidth(2.5f);
        set2.setFillColor(color2);
        set2.setDrawCircles(false);
        set2.setDrawFilled(true);
        set2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set2.setDrawValues(false);
        set2.setFillAlpha(100);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> result = new ArrayList<>();
        result.add(set1);
        result.add(set2);

        LineData d = new LineData(result);

        return d;
    }

}
