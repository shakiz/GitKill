package app.com.gitkill.fragments;

import android.content.Context;
import android.os.Bundle;
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
import app.com.gitkill.pojoclasses.repositories.TrendingRepoPojo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TrendingRepositories extends Fragment {

    private ArrayAdapter<String> arrayAdapter;
    private Spinner languageSpinner,timeSpinner;
    private Context context;
    private ArrayList<String> languageList, timeList;
    private RecyclerView recyclerViewRepo;
    private RecyclerView.LayoutManager layoutManager;
    private TrendingRepositoriesAdapter trendingRepositoriesAdapter;
    private ArrayList<TrendingRepoPojo> trendingRepoList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private String TAG = " TrendingRepositories ";
    private OkHttpClient.Builder builder;

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
        getRepoData();
        setData();
        setAdapter(languageSpinner,languageList);
        setAdapter(timeSpinner,timeList);
        setAdapter(timeSpinner,timeList);
        setRecyclerAdapter(recyclerViewRepo,trendingRepoList);
        return view;
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
        arrayAdapter = new ArrayAdapter<>(context,R.layout.spinner_drop,languageList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void setRecyclerAdapter(RecyclerView recyclerView,ArrayList<TrendingRepoPojo> arrayList){
        layoutManager = new LinearLayoutManager(context);
        trendingRepositoriesAdapter = new TrendingRepositoriesAdapter(trendingRepoList,context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trendingRepositoriesAdapter);
    }

    private void init(View view) {
        recyclerViewRepo = view.findViewById(R.id.RecyclerRepoList);
        languageSpinner = view.findViewById(R.id.LanguageSpinner);
        timeSpinner = view.findViewById(R.id.TimeSpinner);
        allUrlClass = new AllUrlClass();
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
        trendingRepoList = new ArrayList<>();
    }

    private void getRepoData() {
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
        final Call<ArrayList<TrendingRepoPojo>> userInformationCall=apiService.getTrendingRepos(allUrlClass.TRENDING_REPOS_URL);
        //handling user requests and their interactions with the application.
        userInformationCall.enqueue(new Callback<ArrayList<TrendingRepoPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<TrendingRepoPojo>> call, Response<ArrayList<TrendingRepoPojo>> response) {
                try{
                    for(int start=0;start<5;start++){
                        TrendingRepoPojo repoPojo=response.body().get(start);
                        Log.v(TAG,repoPojo.getAuthor());
                        trendingRepoList.add(new TrendingRepoPojo(repoPojo.getAuthor(),repoPojo.getName(),repoPojo.getLanguage(),repoPojo.getStars(),repoPojo.getForks(),repoPojo.getUrl()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
                setRecyclerAdapter(recyclerViewRepo,trendingRepoList);
            }
            @Override
            public void onFailure(Call<ArrayList<TrendingRepoPojo>> call, Throwable t) {
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
