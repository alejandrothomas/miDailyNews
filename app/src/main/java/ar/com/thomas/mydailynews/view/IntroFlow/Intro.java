package ar.com.thomas.mydailynews.view.IntroFlow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import ar.com.thomas.mydailynews.R;

/**
 * Created by mbp on 7/31/16.
 */
public class Intro extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro,container,false);
        return view;
    }
}
