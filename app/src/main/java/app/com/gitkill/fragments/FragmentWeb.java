package app.com.gitkill.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import app.com.gitkill.activities.s.details.DetailsActivity;
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

public class FragmentWeb extends Fragment {


    private static final FragmentWeb FRAGMENT_WEB = null;
    private RecyclerView webTopicRecyclerView;
    private OkHttpClient.Builder builder;
    private AlertDialog progressDialog;
    private ArrayList<Item> webTopicList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private CircleImageView refreshListButton;

    public FragmentWeb() {
        // Required empty public constructor
    }

    public static FragmentWeb getInstance() {
        if (FRAGMENT_WEB == null) return new FragmentWeb();
        else return FRAGMENT_WEB;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void init(View view) {
        webTopicRecyclerView = view.findViewById(R.id.WebTopicList);
        refreshListButton = view.findViewById(R.id.RefreshList);
        webTopicList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        progressDialog = new SpotsDialog(getContext(),R.style.CustomProgressDialog);
    }

    private void bindUIWithComponents(View view) {
        new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL).execute();

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL).execute();
            }
        });
    }

    private void loadListView(){
        AllTopicAdapter allTopicAdapter = new AllTopicAdapter(webTopicList, getContext(),R.layout.adapter_layout_web_topics, new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidTopic) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                browserIntent.setData(Uri.parse(androidTopic.getHtmlUrl()));
//                startActivity(browserIntent);
                Intent intent = new Intent(getContext() , DetailsActivity.class);
                intent.putExtra("item", androidTopic);
                getContext().startActivity(intent);
            }
        });
        webTopicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        webTopicRecyclerView.setAdapter(allTopicAdapter);
        allTopicAdapter.notifyDataSetChanged();
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
                            if (webTopicList.size()>0)loadListView();
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
        webTopicList.clear();
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
        final Call<TopicBase> androidTopicCall=apiService.getAndroidTopics(url+"repositories","web");
        //handling user requests and their interactions with the application.
        androidTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                try{
                    for (int start=0;start<response.body().getItems().size();start++) {
                        Item item=response.body().getItems().get(start);
                        //License license = item.getLicense();
                        webTopicList.add(new Item(item.getFullName(),item.getHtmlUrl(),item.getLanguage(),item.getStargazersCount(),item.getWatchersCount(),
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
