package jp.co.jri.internship.fintech_sample1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Tab1Fragment extends Fragment {
    protected BarChart chart;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        // レイアウト（tab1_fragment.xml）を作成する
        View v = inflater.inflate(R.layout.tab1_fragment, container, false);

        // スプリント2　以下をコメントアウト
        // TapPagerAdapterから遷移元のボタン名（buttonName）を受け取り、レイアウトに表示する
//        String buttonName = getArguments().getString("ButtonName");
//        TextView textView = v.findViewById(R.id.tapButton);
//        textView.setText(buttonName + "がタップされました。");

        // 3ヶ月分のデータを格納したArrayListをbundleから取得する
        ArrayList<Integer> rawData = getArguments().getIntegerArrayList("Data");

        // 棒グラフ表示領域を取得
        chart = v.findViewById(R.id.barChart);

        // 表のデータを作成
        BarData data = new BarData(getBarData(rawData.get(0), rawData.get(1), rawData.get(2)));
        chart.setData(data);

        // 表示する月をbundleから取得
        int month = getArguments().getInt("LastMonth");
        int axisMax = getMaximum(rawData);

        //Y軸(左)
        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0f);
        left.setAxisMaximum(axisMax);   // 最大値
        left.setDrawTopYLabelEntry(true);

        //Y軸(右)
        YAxis right = chart.getAxisRight();
        right.setDrawLabels(false);
        right.setDrawGridLines(false);
        right.setDrawZeroLine(true);
        right.setDrawTopYLabelEntry(true);

        //X軸
        XAxis xAxis = chart.getXAxis();
        //X軸に表示するLabelのリスト(最初の""は原点の位置)
        final String[] labels = {"", ((month + 10 - 1) % 12 + 1) + "月", ((month + 11 - 1) % 12 + 1) + "月", (month) + "月"};


        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        XAxis bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setTextSize(16);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setDrawAxisLine(true);

        // グラフ上の表示
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setClickable(false);

        // 凡例を非表示
        chart.getLegend().setEnabled(false);

        chart.setScaleEnabled(false);

        return v;
    }

    //棒グラフのデータをセットする関数
    private List<IBarDataSet> getBarData(int sumWin, int sumLoss, int sumDraw) {
        // 表示させるデータをリストに格納
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, sumWin));
        entries.add(new BarEntry(2, sumLoss));
        entries.add(new BarEntry(3, sumDraw));

        // 棒グラフ表示用のデータセットに準備したリストをセット
        BarDataSet dataSet = new BarDataSet(entries, "bar");

        //ハイライトさせない
        dataSet.setHighlightEnabled(false);

        List<IBarDataSet> bars = new ArrayList<>();
        bars.add(dataSet);

        return bars;
    }

    // スプリント2 変更点
    private int getMaximum(ArrayList<Integer> data) {
        int max = Collections.max(data);
        int len = String.valueOf(max).length() - 1;
        int axisMax = (max / (int)Math.pow(10, len) + 1) * (int)Math.pow(10, len);
        return axisMax;
    }
}