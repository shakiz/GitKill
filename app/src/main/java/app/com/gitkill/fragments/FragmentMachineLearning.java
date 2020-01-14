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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import app.com.gitkill.R;
import app.com.gitkill.adapters.AllTopicAdapter;
import app.com.gitkill.apiutils.AllApiService;
import app.com.gitkill.apiutils.AllUrlClass;
import app.com.gitkill.models.alltopic.Item;
import app.com.gitkill.models.alltopic.TopicBase;
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

import static android.support.constraint.Constraints.TAG;

public class FragmentMachineLearning extends Fragment {
    private static final FragmentMachineLearning FRAGMENT_MACHINE_LEARNING = null;
    private RecyclerView androidTopicRecyclerView;
    private OkHttpClient.Builder builder;
    private AlertDialog progressDialog;
    private ArrayList<Item> mlItemList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private CircleImageView refreshListButton;

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

    private void init(View view) {
        androidTopicRecyclerView = view.findViewById(R.id.RecyclerMLList);
        refreshListButton = view.findViewById(R.id.RefreshList);
        allUrlClass = new AllUrlClass();
        mlItemList = new ArrayList<>();
        progressDialog = new SpotsDialog(getContext(),R.style.CustomProgressDialog);
    }

    private void bindUIWithComponents(View view) {
        new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL , "ml").execute();

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL , "ml").execute();
            }
        });
    }

    private void loadListView(){
        AllTopicAdapter allTopicAdapter = new AllTopicAdapter(mlItemList, getContext(), R.layout.adapter_layout_trending_ml_repos, new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidTopic) {

            }
        });
        androidTopicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        androidTopicRecyclerView.setAdapter(allTopicAdapter);
        allTopicAdapter.notifyDataSetChanged();
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {

        View view;
        String url , querySting;

        public BackgroundDataLoad(View view, String url, String querySting) {
            this.view = view;
            this.url = url;
            this.querySting = querySting;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadRecord(url , querySting);
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
                            if (mlItemList.size()>0)loadListView();
                            else Toast.makeText(getContext(),R.string.no_data_message,Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                }, 6000);
            }
        }

    }


    private void loadRecord(String url , String queryString) {
        Log.v("URL",url);
        mlItemList.clear();
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
        final Call<TopicBase> androidTopicCall=apiService.getAndroidTopics(url+"repositories",queryString);
        //handling user requests and their interactions with the application.
        androidTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                try{
                    for (int start=0;start<response.body().getItems().size();start++) {
                        Item item=response.body().getItems().get(start);
                        //License license = item.getLicense();
                        mlItemList.add(new Item(item.getFullName(),item.getHtmlUrl(),item.getLanguage(),item.getStargazersCount(),item.getWatchersCount(),
                                item.getForksCount(),item.getForks(),item.getWatchers()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
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