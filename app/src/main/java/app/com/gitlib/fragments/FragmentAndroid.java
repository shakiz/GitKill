package app.com.gitlib.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.activities.s.details.DetailsActivity;
import app.com.gitlib.adapters.AllTopicAdapter;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.alltopic.TopicBase;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.utils.UX;
import app.com.gitlib.utils.UtilsManager;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentAndroid extends Fragment {
    private static final FragmentAndroid FRAGMENT_ANDROID = null;
    private RecyclerView androidTopicRecyclerView;
    private OkHttpClient.Builder builder;
    private UX ux;
    private UtilsManager utilsManager;
    private ArrayList<Item> androiTopicList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private CircleImageView refreshListButton;
    private Spinner androidFilterSpinner;
    private TextView NoData;
    private ImageView NoDataIV;
    private String[] androidFilterList = new String[]{"Select Query","Layouts","Drawing",
            "Navigation","Scanning","RecyclerView","ListView","Image Processing","Binding","Debugging"};

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
        androidTopicRecyclerView = view.findViewById(R.id.mRecyclerView);
        refreshListButton = view.findViewById(R.id.RefreshList);
        androidFilterSpinner = view.findViewById(R.id.AndroidFilterSpinner);
        NoData = view.findViewById(R.id.NoDataMessage);
        NoDataIV = view.findViewById(R.id.NoDataIV);
        androiTopicList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        ux = new UX(getContext());
        utilsManager = new UtilsManager(getContext());
    }

    private void bindUIWithComponents(View view) {

        setAdapter();

        new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL , "android").execute();

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(view , allUrlClass.ALL_TOPICS_BASE_URL , "android").execute();
            }
        });

        androidFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                new BackgroundDataLoad(view, allUrlClass.ALL_TOPICS_BASE_URL, "android"+queryString ).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_drop,androidFilterList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        androidFilterSpinner.setAdapter(arrayAdapter);
    }

    private void loadListView(){
        AllTopicAdapter allTopicAdapter = new AllTopicAdapter(androiTopicList, getContext(), R.layout.adapter_layout_android_topics, new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidTopic) {
                Intent intent = new Intent(getContext() , DetailsActivity.class);
                intent.putExtra("item", androidTopic);
                getContext().startActivity(intent);
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
            ux.getLoadingView();
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
                        if (androiTopicList.size()>0){
                            loadListView();
                            NoData.setVisibility(View.GONE);
                            NoDataIV.setVisibility(View.GONE);
                        }
                        else {
                            NoData.setVisibility(View.VISIBLE);
                            NoDataIV.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(),R.string.no_data_message,Toast.LENGTH_LONG).show();
                        }
                        ux.removeLoadingView();
                    }
                }, 6000);
            }
        }

    }


    private void loadRecord(String url , String queryString) {
        Log.v("URL",url);
        androiTopicList.clear();
        builder= new OkHttpClient.Builder();
        utilsManager.loggingInterceptorForRetrofit(builder);
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
        final Call<TopicBase> androidTopicCall=apiService.getAllTopics(url+"repositories",queryString);
        //handling user requests and their interactions with the application.
        androidTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                try{
                    for (int start=0;start<response.body().getItems().size();start++) {
                        Item item=response.body().getItems().get(start);
                        //License license = item.getLicense();
                        androiTopicList.add(new Item(item.getFullName(),item.getAvatar_url(),item.getHtmlUrl(),item.getLanguage(),item.getStargazersCount(),item.getWatchersCount(),
                                item.getForksCount(),item.getForks(),item.getWatchers()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
                Log.v("Android fragment",""+t.getMessage());
            }
        });

    }

}
