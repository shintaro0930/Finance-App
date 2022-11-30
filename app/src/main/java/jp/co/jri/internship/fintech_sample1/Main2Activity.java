package jp.co.jri.internship.fintech_sample1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // レイアウト（activity_main2.xml）を表示する
        setContentView(R.layout.activity_main2);

        // intentに格納されている遷移元のボタン名（buttonName）を受け取る
        Intent intent = getIntent();
        String buttonName = intent.getStringExtra("ButtonName");

        // アダプタ(TapPagerAdapter)を用いてタブ切り替え時のViewPager2の内容表示を制御する.
        // 遷移元のボタン名（buttonName）もアダプタに受け渡す
        ViewPager2 pager = (ViewPager2) findViewById(R.id.pager);
        TapPagerAdapter adapter = new TapPagerAdapter(this, buttonName);

        // 追加部分
        // 仮のデータを生成
        int[] data = intent.getIntArrayExtra("Data");
        ArrayList<Integer> dataList = new ArrayList<>();
        for (int val: data) {
            dataList.add(val);
        }

        int month = intent.getIntExtra("LastMonth", 8);

        // TapPagerAdapterにdataを渡している
        adapter.setData(dataList);
        adapter.setMonth(month);
        // --------

        pager.setAdapter(adapter);

        // TabLayoutとViewPager2を関連付ける（押下されたタブと内容表示を関連付ける）
        TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);
        new TabLayoutMediator(
                tabs,
                pager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("月ごとの支出");
                    } else {
                        tab.setText("TAB" + (position + 1));
                    }
                }
        ).attach();
    }
}


