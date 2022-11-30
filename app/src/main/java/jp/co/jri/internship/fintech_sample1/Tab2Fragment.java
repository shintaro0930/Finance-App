package jp.co.jri.internship.fintech_sample1;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab2Fragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        // レイアウト（tab2_fragment.xml）を作成する
        View v = inflater.inflate(R.layout.tab2_fragment, container, false);

        return v;
    }
}