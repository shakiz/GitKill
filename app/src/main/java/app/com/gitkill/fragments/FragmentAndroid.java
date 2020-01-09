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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import app.com.gitkill.R;
import app.com.gitkill.adapters.AndroidTopicAdapter;
import app.com.gitkill.apiutils.AllApiService;
import app.com.gitkill.apiutils.AllUrlClass;
import app.com.gitkill.models.androidtopic.AndroidTopic;
import app.com.gitkill.models.androidtopic.Item;
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

public class FragmentAndroid extends Fragment {
    private static final FragmentAndroid FRAGMENT_ANDROID = null;
    private RecyclerView androidTopicRecyclerView;
    private OkHttpClient.Builder builder;
    private AlertDialog progressDialog;
    private ArrayList<AndroidTopic> androiTopicList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;

    public FragmentAndroid() {
        // Required empty public constructor
    }

    public static FragmentAndroid getInstance() {
        if (FRAGMENT_ANDROID == null) return new FragmentAndroid();
        else return FRAGMENT_ANDROID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_android, container, false);
        init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void init(View view) {
        androidTopicRecyclerView = view.findViewById(R.id.AndroidTopicList);
        androiTopicList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        progressDialog = new SpotsDialog(getContext(),R.style.CustomProgressDialog);
    }

    private void bindUIWithComponents(View view) {
        new BackgroundDataLoad(view , allUrlClass.ANDROID_TOPICS_URL).execute();
    }

    private void loadListView(){
        AndroidTopicAdapter androidTopicAdapter = new AndroidTopicAdapter(androiTopicList, getContext(), new AndroidTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidTopic) {

            }
        });
        androidTopicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        androidTopicRecyclerView.setAdapter(androidTopicAdapter);
        androidTopicAdapter.notifyDataSetChanged();
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
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (progressDialog.isShowing()) {
                        if (androiTopicList.size()>0) {
                            loadListView();
                        }
                        progressDialog.dismiss();
                    }
                }
            }, 4000);
        }

    }


    private void loadRecord(String url) {
        Log.v("URL",url);
        androiTopicList.clear();
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
        final Call<AndroidTopic> userInformationCall=apiService.getAndroidTopics(url+"repositories","android");
        //handling user requests and their interactions with the application.
        userInformationCall.enqueue(new Callback<AndroidTopic>() {
            @Override
            public void onResponse(Call<AndroidTopic> call, Response<AndroidTopic> response) {
                try{
                        AndroidTopic userPojo=response.body();
                        androiTopicList.add(new AndroidTopic(userPojo.getItems()));

                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<AndroidTopic> call, Throwable t) {
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
