package app.com.gitkill.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import app.com.gitkill.R;

public class FragmentTrendingLanguages extends Fragment {

    private ArrayAdapter<String> arrayAdapter;
    private Spinner timeSpinner;
    private Context context;
    private ArrayList<String>  timeList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public FragmentTrendingLanguages() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_languages, container, false);
        init(view);
        setData();
        setAdapter(timeSpinner,timeList);
        return view;
    }

    private void setData() {
        //Adding data to time list
        timeList.add("Daily");
        timeList.add("Weekly");
        timeList.add("Monthly");
        timeList.add("Yearly");
    }

    private void setAdapter(Spinner spinner, ArrayList<String> languageList) {
        arrayAdapter = new ArrayAdapter<>(context,R.layout.spinner_drop,languageList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void init(View view) {
        timeSpinner = view.findViewById(R.id.TimeSpinner);
        timeList = new ArrayList<>();
    }

}
