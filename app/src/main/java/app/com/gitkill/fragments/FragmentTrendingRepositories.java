package app.com.gitkill.fragments;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import app.com.gitkill.R;
import app.com.gitkill.adapters.TrendingRepositoriesAdapter;
import app.com.gitkill.apiutils.AllApiService;
import app.com.gitkill.apiutils.AllUrlClass;
import app.com.gitkill.models.repositories.TrendingRepositories;
import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentTrendingRepositories extends Fragment {
    private static final FragmentTrendingRepositories FRAGMENT_TRENDING_REPOSITORIES = null;
    private ArrayAdapter<String> arrayAdapter;
    private Spinner languageSpinner,timeSpinner;
    private ArrayList<String> languageList, timeList;
    private RecyclerView recyclerViewRepo;
    private TrendingRepositoriesAdapter trendingRepositoriesAdapter;
    private ArrayList<TrendingRepositories> trendingRepoList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private String TAG = "FragmentTrendingRepositories";
    private OkHttpClient.Builder builder;
    private AlertDialog progressDialog;

    public static synchronized FragmentTrendingRepositories getInstance(){
        if (FRAGMENT_TRENDING_REPOSITORIES == null) return new FragmentTrendingRepositories();
        else return FRAGMENT_TRENDING_REPOSITORIES;
    }


    public FragmentTrendingRepositories() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_repositories, container, false);
        init(view);
        bindUiWithComponents(view);
        return view;
    }

    private void bindUiWithComponents(View view) {
        setData();
        new BackgroundDataLoad(view).execute();
        setAdapter(languageSpinner,languageList);
        setAdapter(timeSpinner,timeList);
        setAdapter(timeSpinner,timeList);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setData() {
        //Adding the language list
        languageList.add("Java");
        languageList.add("Python");
        languageList.add("Javascript");
        languageList.add("PHP");
        //Adding data to time list
        timeList.add("Daily");
        timeList.add("Weekly");
        timeList.add("Monthly");
        timeList.add("Yearly");
    }

    private void setAdapter(Spinner spinner, ArrayList<String> languageList) {
        arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_drop,languageList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void loadListView(){
        trendingRepositoriesAdapter = new TrendingRepositoriesAdapter(trendingRepoList, getContext(), new TrendingRepositoriesAdapter.onItemClickListener() {
            @Override
            public void respond(TrendingRepositories trendingRepositories) {

            }
        });
        recyclerViewRepo.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRepo.setAdapter(trendingRepositoriesAdapter);
        trendingRepositoriesAdapter.notifyDataSetChanged();
    }

    private void init(View view) {
        recyclerViewRepo = view.findViewById(R.id.RecyclerRepoList);
        languageSpinner = view.findViewById(R.id.LanguageSpinner);
        timeSpinner = view.findViewById(R.id.TimeSpinner);
        allUrlClass = new AllUrlClass();
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
        trendingRepoList = new ArrayList<>();
        progressDialog = new SpotsDialog(getContext(),R.style.CustomProgressDialog);
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {

        View view;

        public BackgroundDataLoad(View view) {
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadRecord();
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (progressDialog.isShowing()) {
                        if (trendingRepoList.size()>0) {
                            loadListView();
                        }
                        progressDialog.dismiss();
                    }
                }
            }, 4000);
        }

    }

    private void loadRecord() {
        builder= new OkHttpClient.Builder();
        loggingInterceptorForRetrofit(builder);
        if (retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit=new Retrofit.Builder()
                    .baseUrl(allUrlClass.TRENDING_REPOS_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        //Creating the instance for api service from AllApiService interface
        apiService=retrofit.create(AllApiService.class);
        final Call<ArrayList<TrendingRepositories>> userInformationCall=apiService.getTrendingRepos(allUrlClass.TRENDING_REPOS_URL);
        //handling user requests and their interactions with the application.
        userInformationCall.enqueue(new Callback<ArrayList<TrendingRepositories>>() {
            @Override
            public void onResponse(Call<ArrayList<TrendingRepositories>> call, Response<ArrayList<TrendingRepositories>> response) {
                try{
                    for(int start=0;start<response.body().size();start++){
                        TrendingRepositories repoPojo=response.body().get(start);
                        trendingRepoList.add(new TrendingRepositories(repoPojo.getAuthor(),repoPojo.getName(),repoPojo.getLanguage(),repoPojo.getStars(),repoPojo.getForks(),repoPojo.getUrl()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<TrendingRepositories>> call, Throwable t) {
                Log.v(TAG,""+t.getMessage());
            }
        });
    }

    public void loggingInterceptorForRetrofit(OkHttpClient.Builder builder){
        //Creating the logging interceptor
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        //Setting the level
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
    }

}
