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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import app.com.gitkill.R;
import app.com.gitkill.adapters.TrendingDevelopersAdapter;
import app.com.gitkill.adapters.TrendingRepositoriesAdapter;
import app.com.gitkill.apiutils.AllApiService;
import app.com.gitkill.apiutils.AllUrlClass;
import app.com.gitkill.pojoclasses.repositories.TrendingRepoPojo;
import app.com.gitkill.pojoclasses.users.TrendingUserPojo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TrendingUsers extends Fragment {

    private ArrayAdapter<String> arrayAdapter;
    private Spinner languageSpinner,timeSpinner;
    private RecyclerView recyclerViewUser;
    private Context context;
    private ArrayList<String> languageList, timeList;
    private ArrayList<TrendingUserPojo> trendingUserList;
    private TrendingDevelopersAdapter trendingDevelopersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private String TAG = "TrendingRepositories";
    private OkHttpClient.Builder builder;
    private ShimmerFrameLayout shimmerFrameLayout;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public TrendingUsers() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_users, container, false);
        init(view);
        getUserData();
        setData();
        setAdapter(languageSpinner,languageList);
        setAdapter(timeSpinner,timeList);
        setRecyclerAdapter(recyclerViewUser);

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

    private void init(View view) {
        languageSpinner = view.findViewById(R.id.LanguageSpinner);
        timeSpinner = view.findViewById(R.id.TimeSpinner);
        recyclerViewUser = view.findViewById(R.id.RecyclerUserList);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        trendingUserList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
    }

    private void setRecyclerAdapter(RecyclerView recyclerView){
        layoutManager = new LinearLayoutManager(context);
        trendingDevelopersAdapter = new TrendingDevelopersAdapter(trendingUserList,context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trendingDevelopersAdapter);
        trendingDevelopersAdapter.notifyDataSetChanged();
        shimmerFrameLayout.stopShimmerAnimation();

        if (trendingUserList.size()>0){
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }


    private void getUserData() {
        builder= new OkHttpClient.Builder();
        loggingInterceptorForRetrofit(builder);
        if (retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit=new Retrofit.Builder()
                    .baseUrl(allUrlClass.TRENDING_DEVS_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        //Creating the instance for api service from AllApiService interface
        apiService=retrofit.create(AllApiService.class);
        final Call<ArrayList<TrendingUserPojo>> userInformationCall=apiService.getTrendingUsers(allUrlClass.TRENDING_DEVS_URL);
        //handling user requests and their interactions with the application.
        userInformationCall.enqueue(new Callback<ArrayList<TrendingUserPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<TrendingUserPojo>> call, Response<ArrayList<TrendingUserPojo>> response) {
                try{
                    for(int start=0;start<response.body().size();start++){
                        TrendingUserPojo userPojo=response.body().get(start);
                        Log.v(TAG,userPojo.getUsername());
                        Log.v(TAG,userPojo.getRepo().getName());
                        trendingUserList.add(new TrendingUserPojo(userPojo.getUsername(),userPojo.getName(),userPojo.getType(),userPojo.getUrl(),userPojo.getAvatar(),userPojo.getRepo()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
                setRecyclerAdapter(recyclerViewUser);
            }
            @Override
            public void onFailure(Call<ArrayList<TrendingUserPojo>> call, Throwable t) {
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
