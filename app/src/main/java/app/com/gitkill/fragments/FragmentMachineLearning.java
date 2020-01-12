package app.com.gitkill.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.com.gitkill.R;

public class FragmentMachineLearning extends Fragment {
    private static final FragmentMachineLearning FRAGMENT_MACHINE_LEARNING = null;

    public FragmentMachineLearning() {
        // Required empty public constructor
    }

    public static FragmentMachineLearning getInstance() {
        if (FRAGMENT_MACHINE_LEARNING == null) return new FragmentMachineLearning();
        else return FRAGMENT_MACHINE_LEARNING;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_machine_learning, container, false);
        init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void bindUIWithComponents(View view) {

    }

    private void init(View view) {

    }

}
