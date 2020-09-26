package app.com.gitlib.activities.ml;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.activities.details.DetailsActivity;
import app.com.gitlib.activities.onboard.HomeActivity;
import app.com.gitlib.adapters.AllTopicAdapter;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.models.alltopic.TopicBase;
import app.com.gitlib.utils.UX;
import app.com.gitlib.utils.UtilsManager;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MachineLearningActivity extends AppCompatActivity {
    private RecyclerView androidTopicRecyclerView;
    private UX ux;
    private UtilsManager utilsManager;
    private ArrayList<Item> mlItemList;
    private Spinner mlFilterSpinner;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private CircleImageView refreshListButton;
    private TextView NoData;
    private ImageView NoDataIV;
    private String[] mlFilterList = new String[]{"Select Query","Big Data","Data Science",
            "Natural Language Processing","Neural Network","Deep Learning"};
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_learning);

        //region init and bin UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    private void init() {
        androidTopicRecyclerView = findViewById(R.id.mRecyclerView);
        refreshListButton = findViewById(R.id.RefreshList);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        mlFilterSpinner = findViewById(R.id.FilterSpinner);
        adView = findViewById(R.id.adView);
        allUrlClass = new AllUrlClass();
        mlItemList = new ArrayList<>();
        ux = new UX(this);
        utilsManager = new UtilsManager(this);
    }

    private void bindUIWithComponents() {
        //region toolbar on back click listener
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MachineLearningActivity.this,HomeActivity.class));
            }
        });
        //endregion

        ux.setSpinnerAdapter(mlFilterSpinner,mlFilterList);

        new BackgroundDataLoad(allUrlClass.ALL_TOPICS_BASE_URL , "ml").execute();

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(allUrlClass.ALL_TOPICS_BASE_URL , "ml").execute();
            }
        });

        mlFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                new BackgroundDataLoad(allUrlClass.ALL_TOPICS_BASE_URL, "" + queryString).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //region adMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.v("onInitComplete","InitializationComplete");
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.v("onAdListener","AdlLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.v("onAdListener","AdFailedToLoad");
                Log.v("onAdListener","AdFailedToLoad Error "+adError.getMessage());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.v("onAdListener","AdOpened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.v("onAdListener","AdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.v("onAdListener","AdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.v("onAdListener","AdClosed");
            }
        });
        //endregion
    }

    private void loadListView(){
        ux.loadListView(mlItemList, androidTopicRecyclerView, R.layout.adapter_layout_trending_ml_repos).setOnItemClickListener(new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidItem) {
                Intent intent = new Intent(MachineLearningActivity.this , DetailsActivity.class);
                intent.putExtra("from", "ml");
                intent.putExtra("item", androidItem);
                startActivity(intent);
            }
        });
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {
        String url , querySting;

        public BackgroundDataLoad(String url, String querySting) {
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
                        if (mlItemList.size()>0){
                            loadListView();
                            NoData.setVisibility(View.GONE);
                            NoDataIV.setVisibility(View.GONE);
                        }
                        else {
                            NoData.setVisibility(View.VISIBLE);
                            NoDataIV.setVisibility(View.VISIBLE);
                            Toasty.error(MachineLearningActivity.this,R.string.no_data_message).show();
                        }
                        ux.removeLoadingView();
                    }
                }, 6000);
            }
        }

    }

    private void loadRecord(String url , String queryString) {
        mlItemList.clear();
        //Creating the instance for api service from AllApiService interface
        apiService=utilsManager.getClient(url).create(AllApiService.class);
        final Call<TopicBase> androidTopicCall=apiService.getAllTopics(url+"repositories",queryString);
        //handling user requests and their interactions with the application.
        androidTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                try{
                    for (int start=0;start<response.body().getItems().size();start++) {
                        Item item=response.body().getItems().get(start);
                        //License license = item.getLicense();
                        mlItemList.add(new Item(item.getFullName(),item.getAvatar_url(),item.getHtmlUrl(),item.getLanguage(),item.getStargazersCount(),item.getWatchersCount(),
                                item.getForksCount(),item.getForks(),item.getWatchers()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
                Log.v("ML fragment",""+t.getMessage());
            }
        });

    }

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MachineLearningActivity.this, HomeActivity.class));
    }
    //endregion
}