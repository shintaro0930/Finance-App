package jp.co.jri.internship.fintech_sample1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //　プログラムの起動時に実行するメソッド
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // レイアウト（activity_main.xml）を表示する
        setContentView(R.layout.activity_main);

        // 「表示」ボタンをタップしたらonClick()を呼び出す
        findViewById(R.id.btSearch).setOnClickListener(this);
        // 「遷移」ボタンをタップしたらonClick()を呼び出す
        findViewById(R.id.btLink).setOnClickListener(this);

        ArrayAdapter<String> adapter_1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_1.add("1月");
        adapter_1.add("2月");
        adapter_1.add("3月");
        adapter_1.add("4月");
        adapter_1.add("5月");
        adapter_1.add("6月");
        adapter_1.add("7月");
        adapter_1.add("8月");
        adapter_1.add("9月");
        adapter_1.add("10月");
        adapter_1.add("11月");
        adapter_1.add("12月");

        Spinner spinner1 = (Spinner) findViewById(R.id.etStart);
        spinner1.setAdapter(adapter_1);

        ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_2.add("1月");
        adapter_2.add("2月");
        adapter_2.add("3月");
        adapter_2.add("4月");
        adapter_2.add("5月");
        adapter_2.add("6月");
        adapter_2.add("7月");
        adapter_2.add("8月");
        adapter_2.add("9月");
        adapter_2.add("10月");
        adapter_2.add("11月");
        adapter_2.add("12月");

        Spinner spinner2 = (Spinner) findViewById(R.id.etEnd);
        spinner2.setAdapter(adapter_2);
    }

    // ボタンをタップした時に実行するメソッド
    @Override
    public void onClick(View view) {
        // クリックされたボタンを識別
        int getButton = view.getId();
        // 「表示」がクリックされたとき、表示処理用のメソッドを呼び出す
        if (getButton == R.id.btSearch) {
            clickBtSearch(view);
        }
        // 「遷移」がクリックされたとき、画面遷移用のメソッドを呼び出す
        if (getButton == R.id.btLink) {
            clickBtnLink(view);
        }
    }

    //「表示」がクリックされたとき
    @SuppressLint("DefaultLocale")
    public void clickBtSearch(View view) {

        // 収支の集計領域を準備する
        int sumIncome = 0;  // 収入の集計領域
        int sumExpense = 0; // 支出の集計領域

        // 最大支出の格納領域を準備する
        int maxAmount = 0;      // 最大支出の金額
        String maxTransDate = null;    // 最大支出の取引日付
        String maxSupplier = null;     // 最大支出の取引先
        String maxContent = null;      // 最大支出の内容
        String maxUse = null;          // 最大支出の用途

        // 対象期間の始まりと終わりを入力する
        Spinner etStart = findViewById(R.id.etStart);     // Viewにプログラムでの変数名を割り当てる
        Spinner etEnd = findViewById(R.id.etEnd);       // Viewにプログラムでの変数名を割り当てる

        String startDate = (String) etStart.getSelectedItem();
        String endDate = (String) etEnd.getSelectedItem();
        Log.d("START_TEST", startDate);
        Log.d("END_TEST", endDate);

        startDate = startDate.substring(0, startDate.length() - 1);
        int startDateInt = Integer.parseInt(startDate); // 7(int)
        startDate = String.format("2022/%02d/01", startDateInt);

        endDate = endDate.substring(0, endDate.length() - 1);
        int endDateInt = Integer.parseInt(endDate);
        endDate = String.format("2022/%02d/31", endDateInt);

        //キーボードの制御
        etStart.setOnFocusChangeListener((v, hasFocus) -> {
            //EditTextのフォーカスが外れた場合
            if (!hasFocus) {
                //ソフトキーボードを非表示にする
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        etStart.clearFocus();
        etEnd.setOnFocusChangeListener((v, hasFocus) -> {
            //EditTextのフォーカスが外れた場合
            if (!hasFocus) {
                //ソフトキーボードを非表示にする
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        etEnd.clearFocus();

        // 表示用のList(fintechDataList)を用意する
        List<FintechData> fintechDataList = new ArrayList<>();

        // LocalFintechDateBase.txt(Androidローカルファイル)が存在しない場合、FintechDataBase.csvからデータを読み込み、
        // LocalFintechDateBase.txtに書き出したのち、List(fintechDataList)を作成する
        // LocalFintechDateBase.txtが存在する場合、ローカルファイルからList(fintechDataList)を作成する
        CsvReader parser = new CsvReader();
        String filename = "LocalFintechDateBase.txt";
        File file = this.getFileStreamPath(filename);
        parser.readerFintechDataBase(getApplicationContext(), file.exists());

        // text.txt(Androidローカルファイル)から1行取り出し、表示用のList(fintechDataList)に入れる
        // 上記をtext.txtが終わるまで繰り返す
        for (FintechData fdata : parser.fintechObjects) {
            // transDateがstartDateから、endDateまでの日付のデータのみListに入れる
            if (fdata.getTransDate().compareTo(startDate) >= 0) {
                if (fdata.getTransDate().compareTo(endDate) <= 0) {
                    fintechDataList.add(fdata);
                    // 収支の合計を集計する
                    if (fdata.getAmount() >= 0) {
                        sumIncome = sumIncome + fdata.getAmount();   // 収入の合計を求める
                    } else {
                        sumExpense = sumExpense + fdata.getAmount(); // 支出の合計を求める
                    }
                    // 最大支出を求める
                    if (maxAmount >= fdata.getAmount()) {
                        maxAmount = fdata.getAmount();
                        maxTransDate = fdata.getTransDate();
                        maxSupplier = fdata.getSupplier();
                        maxContent = fdata.getContent();
                        maxUse = fdata.getUse();
                    }
                }
            }
        }

        // Adapterに表示用のList(fintechDataList)を受け渡す
        List<Map<String, ?>> listData = fintechDataToMapList(fintechDataList);  // Adapterに渡す形式のlist型変数の宣言と初期化
        SimpleAdapter adapter = new SimpleAdapter(                  // ()内で指定した内容のAdapterを生成する
                MainActivity.this,
                listData,                                           // ListView用に自作したレイアウトにFintechDataのどの項目を表示するかを指定する
                R.layout.custom_list_layout,                        // 自作したレイアウト名
                new String[]{"transDate", "supplier", "amount"},     // 表示するFintechDataの項目を指定
                new int[]{R.id.tvList1, R.id.tvList2, R.id.tvList3} // 自作したレイアウトのViewのidを指定
        ){//以下塩田書き換え箇所。getViewメソッドの上書き
            @Override
            public View getView(int isPositive, View convertView, ViewGroup parent) {
                View view = super.getView(isPositive, convertView, parent);
                TextView listText1 = view.findViewById(R.id.tvList3);
                String amount = listText1.getText().toString();
                Boolean isNegative = amount.contains("-");//収入化支出かの判定
                if (isNegative) {
                    listText1.setTextColor(Color.RED);
                }
                else{
                    listText1.setTextColor(Color.BLUE);
                }

                return view;
            }
        };

        // Adapterの内容をlistViewに表示する
        ListView lvHistoricalData = (ListView) findViewById(R.id.lvHistoricalData); //Viewにプログラムでの変数名を割り当てる
        lvHistoricalData.setAdapter(adapter);   //Adapterの内容をlvHistoricalDataに表示する

        // 収入の合計を出力する
        TextView tvIncome = (TextView) findViewById(R.id.tvIncome);
        tvIncome.setText(String.format("%,d", sumIncome));

        // 支出の合計を出力する
        TextView tvExpense = (TextView) findViewById(R.id.tvExpense);
        tvExpense.setText(String.format("%,d", sumExpense));

        //塩田スプリント２加筆箇所。収支差分表示
        TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvTotal.setTypeface(Typeface.DEFAULT_BOLD);
        //tvExpense.setText(String.format("%,d円", sumIncome+sumExpense));
        if(sumIncome+sumExpense> 0){
            tvTotal.setTextColor(Color.BLUE);
            tvTotal.setText(String.format("+%,d円", sumIncome+sumExpense));
        }
        else{
            tvTotal.setTextColor(Color.RED);
            tvTotal.setText(String.format("%,d円", sumIncome+sumExpense));
        }
    }

    //「遷移」がクリックされたとき
    public void clickBtnLink(View view) {
        Intent intent = new Intent(this, Main2Activity.class);  //インテントの作成

        // スプリント2　変更点
        // 追加部分
        Spinner etEnd = findViewById(R.id.etEnd);       // Viewにプログラムでの変数名を割り当てる
        String endDate = (String) etEnd.getSelectedItem();
        endDate = endDate.substring(0, endDate.length() - 1);
        int endDateInt = Integer.parseInt(endDate);
        endDate = String.format("2022/%02d/31", endDateInt);
        String monthString = endDate.substring(5, 7);   // 月に対応する部分文字列を取得
        int month = Integer.parseInt(monthString);

        String yearString = endDate.substring(0, 4);
        int year = Integer.parseInt(yearString);
        // --------

        // 遷移先で表示するため、遷移ボタンのボタン名を取得しintentに格納する
        TextView tv = (TextView) view;
        String buttonName = tv.getText().toString();
        intent.putExtra("ButtonName", buttonName);

        // 追加部分
        // 月を追加
        intent.putExtra("LastMonth", month);

        // スプリント2 変更点
        int[] data = sum(year, month);
        intent.putExtra("Data", data);

        // -----

        startActivity(intent); //画面遷移
    }

    // Adapterに渡す形式のlist型変数の宣言と初期化
    private List<Map<String, ?>> fintechDataToMapList(List<FintechData> fintechDataList) {
        List<Map<String, ?>> data = new ArrayList<>();
        for (FintechData fintechData : fintechDataList) {
            data.add(fintechDataToMap(fintechData));
        }
        return data;
    }

    // 入出金一覧
    // Adapterに渡す形式のlist型変数の宣言と初期化（詳細）
    @SuppressLint("DefaultLocale")
    private Map<String, ?> fintechDataToMap(FintechData fintechData) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", fintechData.getId());
        map.put("transDate", fintechData.getTransDate());
        map.put("transTime", fintechData.getTransTime());
        map.put("service", fintechData.getService());
        map.put("category", fintechData.getCategory());
        map.put("supplier", fintechData.getSupplier());
        map.put("content", fintechData.getContent());
        map.put("use", fintechData.getUse());
        map.put("amount", String.format("%,d円", fintechData.getAmount()));
        map.put("balance", String.format("%,d", fintechData.getBalance()));
        return map;
    }

    // 追加部分
    public int[] sum(int year, int month) {

        int[] sumEx = {0, 0, 0};

        // LocalFintechDateBase.txt(Androidローカルファイル)が存在しない場合、FintechDataBase.csvからデータを読み込み、
        // LocalFintechDateBase.txtに書き出したのち、List(fintechDataList)を作成する
        // LocalFintechDateBase.txtが存在する場合、ローカルファイルからList(fintechDataList)を作成する
        CsvReader parser = new CsvReader();
        String filename = "LocalFintechDateBase.txt";
        File file = this.getFileStreamPath(filename);
        parser.readerFintechDataBase(getApplicationContext(), file.exists());

        for (FintechData fdata : parser.fintechObjects) {
            // transDateがmonth-2月からmonth月の支出を集計する
            // スプリント2　変更点
            int[] range = {(month + 10 - 1) % 12 + 1, (month + 11 - 1) % 12 + 1, month};
            for (int i = 0; i < 3; i++) {
                int myYear = year;
                if (month < range[i]) {
                    myYear = year - 1;
                }
                Log.d("!!!!!!!!!", myYear + "/" + range[i] + ": " + fdata.getAmount());
                if (fdata.getTransDate().startsWith(String.format("%d/%02d/", myYear, range[i]))) {
                    // 収支の合計を集計する
                    if (fdata.getAmount() < 0) {
                        sumEx[i] = sumEx[i] - fdata.getAmount(); // 支出の合計を求める
                    }
                }
            }
        }

        System.out.println(sumEx[0]);

        return sumEx;
    }
}