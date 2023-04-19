package com.example.wordpro.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.wordpro.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProgressBarChart {

    Context context;
    BarChart chart;
    LocalDate today = LocalDate.now();

    public ProgressBarChart(Context context, BarChart chart) {

        this.context = context;
        this.chart = chart;

        Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins_light);

        chart.setPinchZoom(true);
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);
        chart.getLegend().setEnabled(false);
        chart.getLegend().setDrawInside(false);
        chart.setDrawValueAboveBar(false);
        chart.setDrawBarShadow(false);
        chart.setGridBackgroundColor(context.getResources().getColor(R.color.transparent));
        chart.animateY(1200);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(typeface);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(6); // only intervals of 1 day
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                LocalDate current = today.plusDays((int) value);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
                return current.format(dateTimeFormatter);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);

        XYMarkerView mv = new XYMarkerView(context);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv);

        chart.setData(barChartSetData());
        chart.invalidate();
    }

    private BarData barChartSetData(){

        ArrayList<BarEntry> values = new ArrayList<>();

        values.add(new BarEntry(0, 2));
        values.add(new BarEntry(1, 10));
        values.add(new BarEntry(2, 15));
        values.add(new BarEntry(3, 5));
        values.add(new BarEntry(4, 23));
        values.add(new BarEntry(5, 11));
        values.add(new BarEntry(6, 8));
        values.add(new BarEntry(7, 16));
        values.add(new BarEntry(8, 31));
        values.add(new BarEntry(9, 27));
        values.add(new BarEntry(10, 8));
        values.add(new BarEntry(11, 9));

        BarDataSet dataSet = new BarDataSet(values, "For Test");
        dataSet.setHighLightColor(context.getResources().getColor(R.color.light_blue));
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        dataSet.setDrawIcons(false);
        dataSet.setColor(context.getResources().getColor(R.color.blue));

        BarData data = new BarData();
        data.addDataSet(dataSet);

        return data;
    }

    @SuppressLint("ViewConstructor")
    public static class XYMarkerView extends MarkerView {

        private final TextView tvContent;

        public XYMarkerView(Context context) {
            super(context, R.layout.custom_marker_view);
            tvContent = findViewById(R.id.customMarkerViewTextView);
        }

        // runs every time the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            tvContent.setText(String.valueOf((int)(e.getY())));
            super.refreshContent(e, highlight);
        }
        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

    public class Formatter implements IAxisValueFormatter {
        private final BarLineChartBase<?> chart;
        public Formatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            LocalDate current = today.plusDays((int) value);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            return current.format(dateTimeFormatter);
        }
    }
}
