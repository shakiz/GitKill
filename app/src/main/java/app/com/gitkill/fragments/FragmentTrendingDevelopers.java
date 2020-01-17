package app.com.gitkill.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import app.com.gitkill.R;
import app.com.gitkill.adapters.TrendingDevelopersAdapter;
import app.com.gitkill.apiutils.AllApiService;
import app.com.gitkill.apiutils.AllUrlClass;
import app.com.gitkill.models.users.TrendingDevelopers;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentTrendingDevelopers extends Fragment {
    private static final FragmentTrendingDevelopers FRAGMENT_TRENDING_DEVELOPERS = null;
    private ArrayAdapter<String> arrayAdapter;
    private Spinner languageSpinner,sinceSpinner;
    private RecyclerView recyclerViewDevelopers;
    private ArrayList<String> languageList, timeList;
    private ArrayList<TrendingDevelopers> trendingDevelopersList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private String TAG = "FragmentTrendingRepositories" , languageStr = "" , sinceStr = "";
    private OkHttpClient.Builder builder;
    private AlertDialog progressDialog;
    private FloatingActionButton search;
    private CircleImageView refreshListButton;

    public static synchronized FragmentTrendingDevelopers getInstance(){
        if (FRAGMENT_TRENDING_DEVELOPERS == null) return new FragmentTrendingDevelopers();
        else return FRAGMENT_TRENDING_DEVELOPERS;
    }

    public FragmentTrendingDevelopers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_developers, container, false);
        init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void bindUIWithComponents(View view) {
        setData();
        new BackgroundDataLoad(view, allUrlClass.TRENDING_DEVS_URL).execute();
        setAdapter(languageSpinner,languageList);
        setAdapter(sinceSpinner,timeList);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                languageStr = adapterView.getItemAtPosition(position).toString().toLowerCase();
                String newUrl = allUrlClass.BASE_URL+"developers?language="+languageStr;
                Log.v("SpinnerURL",newUrl);
                new BackgroundDataLoad(view,newUrl).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                sinceStr = adapterView.getItemAtPosition(position).toString().toLowerCase();
                String newUrl = allUrlClass.BASE_URL+"developers?since="+sinceStr;
                Log.v("SpinnerURL",newUrl);
                new BackgroundDataLoad(view,newUrl).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUrl = allUrlClass.BASE_URL+"developers?"+"language="+languageStr+"&since="+sinceStr;
                Log.v("SpinnerURL",newUrl);
                new BackgroundDataLoad(view,newUrl).execute();
            }
        });

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(view, allUrlClass.TRENDING_DEVS_URL).execute();
            }
        });
    }

    private void setData() {
        //Adding the language list
        languageList.add("Select Language");
        languageList.add("Java");
        languageList.add("Python");
        languageList.add("C");
        languageList.add("C++");
        languageList.add("C#");
        languageList.add("PHP");
        //Adding data to time list
        timeList.add("Select");
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
        TrendingDevelopersAdapter trendingDevelopersAdapter = new TrendingDevelopersAdapter(trendingDevelopersList, getContext(), new TrendingDevelopersAdapter.onItemClickListener() {
            @Override
            public void respond(TrendingDevelopers trendingDevelopersPojo) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(trendingDevelopersPojo.getUrl()));
                startActivity(browserIntent);
            }
        });
        recyclerViewDevelopers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDevelopers.setAdapter(trendingDevelopersAdapter);
        trendingDevelopersAdapter.notifyDataSetChanged();
    }

    private void init(View view) {
        languageSpinner = view.findViewById(R.id.LanguageSpinner);
        sinceSpinner = view.findViewById(R.id.SinceSpinner);
        recyclerViewDevelopers = view.findViewById(R.id.RecyclerUserList);
        refreshListButton = view.findViewById(R.id.RefreshList);
        search = view.findViewById(R.id.Search);
        trendingDevelopersList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
        progressDialog = new SpotsDialog(getContext(),R.style.CustomProgressDialog);
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {

        View view;
        String url;

        public BackgroundDataLoad(View view, String url) {
            this.view = view;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadRecord(url);
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("done")){
                Log.v("result async task :: ",result);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (progressDialog.isShowing()) {
                            if (trendingDevelopersList.size()>0)loadListView();
                            else Toast.makeText(getContext(),R.string.no_data_message,Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                }, 6000);
            }
        }

    }


    private void loadRecord(String url) {
        Log.v("URL",url);
        trendingDevelopersList.clear();
        builder= new OkHttpClient.Builder();
        loggingInterceptorForRetrofit(builder);
        if (retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit=new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        //Creating the instance for api service from AllApiService interface
        apiService=retrofit.create(AllApiService.class);
        final Call<ArrayList<TrendingDevelopers>> userInformationCall=apiService.getTrendingUsers(url);
        //handling user requests and their interactions with the application.
        userInformationCall.enqueue(new Callback<ArrayList<TrendingDevelopers>>() {
            @Override
            public void onResponse(Call<ArrayList<TrendingDevelopers>> call, Response<ArrayList<TrendingDevelopers>> response) {
                try{
                    for(int start=0;start<response.body().size();start++){
                        TrendingDevelopers userPojo=response.body().get(start);
                        trendingDevelopersList.add(new TrendingDevelopers(userPojo.getUsername(),userPojo.getName(),userPojo.getType(),userPojo.getUrl(),userPojo.getAvatar(),userPojo.getRepo()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<TrendingDevelopers>> call, Throwable t) {
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
