package app.com.gitlib.activities.repositories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

import app.com.gitlib.R;
import app.com.gitlib.activities.details.DetailsActivity;
import app.com.gitlib.activities.onboard.HomeActivity;
import app.com.gitlib.adapters.AllTopicAdapter;
import app.com.gitlib.databinding.ActivityTrendingRepositoriesBinding;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.TrendingRepositoriesViewModel;

import static app.com.gitlib.apiutils.AllUrlClass.ALL_TOPICS_BASE_URL;
import static app.com.gitlib.utils.UtilsManager.hasConnection;
import static app.com.gitlib.utils.UtilsManager.internetErrorDialog;

public class TrendingRepositoriesActivity extends AppCompatActivity {
    private ActivityTrendingRepositoriesBinding activityBinding;
    private Spinner languageSpinner;
    private ArrayList<String> languageList;
    private RecyclerView recyclerViewRepo;
    private AllTopicAdapter allTopicAdapter;
    private ArrayList<Item> trendingRepoList;
    private String TAG = "TrendingRepositoriesActivity";
    private UX ux;
    private TextView NoData;
    private ImageView NoDataIV;
    //Dialog components
    private AdView adView;
    private TrendingRepositoriesViewModel repositoriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_trending_repositories);

        //region init and bin UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI components
    private void init() {
        recyclerViewRepo = findViewById(R.id.mRecyclerView);
        languageSpinner = findViewById(R.id.LanguageSpinner);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        adView = findViewById(R.id.adView);
        languageList = new ArrayList<>();
        ux = new UX(this);
        trendingRepoList = new ArrayList<>();
        repositoriesViewModel = ViewModelProviders.of(this).get(TrendingRepositoriesViewModel.class);
    }
    //endregion

    //region bind UI components
    private void bindUIWithComponents() {
        //region toolbar on back click listener
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrendingRepositoriesActivity.this,HomeActivity.class));
            }
        });
        //endregion

        setData();

        //region load first time data
        if (hasConnection(TrendingRepositoriesActivity.this)) {
            performServerOperation("all");
        }
        else{
            noDataVisibility(true);
            internetErrorDialog(TrendingRepositoriesActivity.this);
        }
        //endregion

        loadListView();
        ux.setSpinnerAdapter(languageSpinner,languageList);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                if (hasConnection(TrendingRepositoriesActivity.this)) {
                    recyclerViewRepo.setVisibility(View.GONE);
                    performServerOperation("all"+queryString);
                }
                else{
                    noDataVisibility(true);
                    internetErrorDialog(TrendingRepositoriesActivity.this);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        activityBinding.RefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasConnection(TrendingRepositoriesActivity.this)) {
                    recyclerViewRepo.setVisibility(View.GONE);
                    performServerOperation("all");
                }
                else{
                    noDataVisibility(true);
                    internetErrorDialog(TrendingRepositoriesActivity.this);
                }
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
    //endregion

    private void setData() {
        //Adding the language list
        languageList.add("Select Language");
        languageList.add("Java");
        languageList.add("Python");
        languageList.add("C");
        languageList.add("C++");
        languageList.add("C#");
        languageList.add("PHP");
    }

    //region perform mvvm server fetch
    private void performServerOperation(String queryString){
        if (hasConnection(TrendingRepositoriesActivity.this)) {
            //region start the shimmer layout
            activityBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
            activityBinding.shimmerFrameLayout.startShimmerAnimation();
            ///endregion
            repositoriesViewModel.getData(this,ALL_TOPICS_BASE_URL , queryString);
            repositoriesViewModel.getAndroidRepos().observe(this, new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {
                    activityBinding.shimmerFrameLayout.stopShimmerAnimation();
                    activityBinding.shimmerFrameLayout.setVisibility(View.GONE);
                    if (items != null) {
                        trendingRepoList = new ArrayList<>(items);
                        recyclerViewRepo.setVisibility(View.VISIBLE);
                        loadListView();
                        allTopicAdapter.notifyDataSetChanged();
                        noDataVisibility(false);
                    }
                    else {
                        Toast.makeText(TrendingRepositoriesActivity.this,R.string.no_data_message,Toast.LENGTH_SHORT).show();
                        noDataVisibility(true);
                    }
                }
            });
        }
        else{
            noDataVisibility(true);
            internetErrorDialog(TrendingRepositoriesActivity.this);
        }
    }
    //endregion

    //region load list data with adapter
    private void loadListView(){
        allTopicAdapter = new AllTopicAdapter(trendingRepoList, this, R.layout.adapter_layout_android_topics);
        recyclerViewRepo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRepo.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRepo.setAdapter(allTopicAdapter);
        allTopicAdapter.notifyDataSetChanged();
        allTopicAdapter.setOnItemClickListener(new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item item) {
                Intent intent = new Intent(TrendingRepositoriesActivity.this , DetailsActivity.class);
                intent.putExtra("from","trendingRepositories");
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });
    }
    //endregion

    //region set no data related components visible
    private void noDataVisibility(boolean shouldVisible){
        if (shouldVisible) {
            NoData.setVisibility(View.VISIBLE);
            NoDataIV.setVisibility(View.VISIBLE);
        } else {
            NoData.setVisibility(View.GONE);
            NoDataIV.setVisibility(View.GONE);
        }
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TrendingRepositoriesActivity.this, HomeActivity.class));
    }
    //endregion
}