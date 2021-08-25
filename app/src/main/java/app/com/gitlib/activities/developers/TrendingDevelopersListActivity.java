package app.com.gitlib.activities.developers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import app.com.gitlib.activities.onboard.HomeActivity;
import app.com.gitlib.adapters.TrendingDevelopersAdapter;
import app.com.gitlib.databinding.ActivityTrendingDevelopersBinding;
import app.com.gitlib.models.users.TrendingDevelopers;
import app.com.gitlib.viewmodels.TrendingDevelopersViewModel;
import es.dmoral.toasty.Toasty;

import static app.com.gitlib.utils.UtilsManager.hasConnection;
import static app.com.gitlib.utils.UtilsManager.internetErrorDialog;

public class TrendingDevelopersListActivity extends AppCompatActivity {
    private ActivityTrendingDevelopersBinding activityBinding;
    private RecyclerView recyclerViewDevelopers;
    private ArrayList<TrendingDevelopers> trendingDevelopersList;
    private String TAG = "Shakil::TrendingDevelopersListActivity" , languageStr = "" , sinceStr = "";
    private TextView NoData;
    private ImageView NoDataIV;
    //Dialog components
    private AdView adView;
    private TrendingDevelopersAdapter trendingDevelopersAdapter;
    private TrendingDevelopersViewModel trendingDevelopersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_trending_developers);

        //region init and bind UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI components
    private void init() {
        recyclerViewDevelopers = findViewById(R.id.mRecyclerView);
        adView = findViewById(R.id.adView);
        trendingDevelopersList = new ArrayList<>();
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        trendingDevelopersViewModel = ViewModelProviders.of(this).get(TrendingDevelopersViewModel.class);
    }
    //endregion

    //region bind UI components
    private void bindUIWithComponents() {
        //region toolbar on back click listener
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrendingDevelopersListActivity.this,HomeActivity.class));
            }
        });
        //endregion

        performServerOperation("tom");

        activityBinding.Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(activityBinding.editTextSearch.getText().toString())){
                    recyclerViewDevelopers.setVisibility(View.GONE);
                    String newUrl = activityBinding.editTextSearch.getText().toString();
                    Log.v("newUrl",newUrl);
                    performServerOperation(newUrl);
                }
                else{
                    Toast.makeText(TrendingDevelopersListActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityBinding.RefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewDevelopers.setVisibility(View.GONE);
                activityBinding.editTextSearch.setText("");
                performServerOperation("tom");
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

    //region load list data and set adapter
    private void loadListView(){
        trendingDevelopersAdapter= new TrendingDevelopersAdapter(trendingDevelopersList, this);
        trendingDevelopersAdapter.setOnItemClickListener(new TrendingDevelopersAdapter.onItemClickListener() {
            @Override
            public void respond(TrendingDevelopers trendingDevelopers) {
                startActivity(new Intent(TrendingDevelopersListActivity.this,DevelopersDetailsActivity.class).putExtra("user",trendingDevelopers.getLogin()));
            }
        });
        recyclerViewDevelopers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDevelopers.setAdapter(trendingDevelopersAdapter);
        trendingDevelopersAdapter.notifyDataSetChanged();
    }
    //endregion

    //region perform mvvm server fetch
    private void performServerOperation(String url){
        //region start the shimmer layout
        activityBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        activityBinding.shimmerFrameLayout.startShimmerAnimation();
        ///endregion
        if (hasConnection(TrendingDevelopersListActivity.this)) {
            trendingDevelopersViewModel.getData(this,url);
            trendingDevelopersViewModel.getDevelopersList().observe(this, new Observer<List<TrendingDevelopers>>() {
                @Override
                public void onChanged(List<TrendingDevelopers> items) {
                    activityBinding.shimmerFrameLayout.stopShimmerAnimation();
                    activityBinding.shimmerFrameLayout.setVisibility(View.GONE);
                    if (items != null) {
                        trendingDevelopersList = new ArrayList<>(items);
                        recyclerViewDevelopers.setVisibility(View.VISIBLE);
                        loadListView();
                        trendingDevelopersAdapter.notifyDataSetChanged();
                        noDataVisibility(false);
                    } else {
                        Toasty.error(TrendingDevelopersListActivity.this,R.string.no_data_message).show();
                        noDataVisibility(true);
                    }
                }
            });
        }
        else{
            noDataVisibility(true);
            internetErrorDialog(TrendingDevelopersListActivity.this);
        }
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
        startActivity(new Intent(TrendingDevelopersListActivity.this, HomeActivity.class));
    }
    //endregion
}