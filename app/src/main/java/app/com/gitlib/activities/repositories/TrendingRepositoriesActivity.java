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
import es.dmoral.toasty.Toasty;

import static app.com.gitlib.apiutils.AllUrlClass.ALL_TOPICS_BASE_URL;
import static app.com.gitlib.utils.UtilsManager.hasConnection;

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
            noDataVisibility(false);
            performServerOperation("all");
        }
        else{
            noDataVisibility(true);
            Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
        }
        //endregion

        loadListView();
        ux.setSpinnerAdapter(languageSpinner,languageList);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                if (!queryString.equals("Select Language")) {
                    if (hasConnection(TrendingRepositoriesActivity.this)) {
                        noDataVisibility(false);
                        recyclerViewRepo.setVisibility(View.GONE);
                        performServerOperation("all"+queryString);
                    }
                    else{
                        noDataVisibility(true);
                        Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
                    }
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
                    noDataVisibility(false);
                    recyclerViewRepo.setVisibility(View.GONE);
                    performServerOperation("all");
                }
                else{
                    noDataVisibility(true);
                    Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
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
        activityBinding.adView.loadAd(adRequest);
        activityBinding.adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                Log.v("onAdListener","AdlLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.v("onAdListener","AdFailedToLoad Error "+adError.getMessage());
            }

            @Override
            public void onAdOpened() {
                Log.v("onAdListener","AdOpened");
            }

            @Override
            public void onAdClicked() {
                Log.v("onAdListener","AdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                Log.v("onAdListener","AdLeftApplication");
            }

            @Override
            public void onAdClosed() {
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
                        Toasty.info(getApplicationContext(), getString(R.string.no_data_message), Toasty.LENGTH_LONG).show();
                        noDataVisibility(true);
                    }
                }
            });
        }
        else{
            noDataVisibility(true);
            Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
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
            recyclerViewRepo.setVisibility(View.GONE);
            activityBinding.shimmerFrameLayout.setVisibility(View.GONE);
            activityBinding.shimmerFrameLayout.stopShimmerAnimation();
            NoData.setVisibility(View.VISIBLE);
            NoDataIV.setVisibility(View.VISIBLE);
        } else {
            recyclerViewRepo.setVisibility(View.VISIBLE);
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