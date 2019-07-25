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

public class TrendingRepositories extends Fragment {

    private ArrayAdapter<String> arrayAdapter;
    private Spinner languageSpinner;
    private Context context;
    private ArrayList<String> languageList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public TrendingRepositories() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_repositories, container, false);
        init(view);
        setData();
        setAdapter(languageSpinner,languageList);
        return view;
    }

    private void setData() {
        //Adding the language list
        languageList.add("Java");
        languageList.add("Python");
        languageList.add("Javascript");
        languageList.add("PHP");
    }

    private void setAdapter(Spinner spinner, ArrayList<String> languageList) {
        arrayAdapter = new ArrayAdapter<>(context,R.layout.spinner_drop,languageList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void init(View view) {
        languageSpinner = view.findViewById(R.id.LanguageSpinner);
        languageList = new ArrayList<>();
    }

}
