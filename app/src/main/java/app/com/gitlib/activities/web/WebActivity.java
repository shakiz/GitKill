package app.com.gitlib.activities.web;

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
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.databinding.ActivityWebBinding;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.WebViewModel;
import es.dmoral.toasty.Toasty;

import static app.com.gitlib.utils.UtilsManager.hasConnection;

public class WebActivity extends AppCompatActivity {
    private ActivityWebBinding activityWebBinding;
    private RecyclerView webTopicRecyclerView;
    private AllTopicAdapter allTopicAdapter;
    private UX ux;
    private ArrayList<Item> webTopicList;
    private AllUrlClass allUrlClass;
    private TextView NoData;
    private ImageView NoDataIV;
    private Spinner webFilterSpinner;
    private String[] webFilterList = new String[]{"Select Query","Javascript","Typescript",
            "Bootstrap","Laravel","Django","Vue Js","Angular"};
    private AdView adView;
    private WebViewModel webViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);

        //region init and bin UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI components
    private void init() {
        webTopicRecyclerView = findViewById(R.id.mRecyclerView);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        adView = findViewById(R.id.adView);
        webFilterSpinner = findViewById(R.id.FilterSpinner);
        webTopicList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        ux = new UX(this);
        webViewModel = ViewModelProviders.of(this).get(WebViewModel.class);
    }
    //endregion

    private void bindUIWithComponents() {
        //region toolbar on back click listener
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WebActivity.this,HomeActivity.class));
            }
        });
        //endregion

        ux.setSpinnerAdapter(webFilterSpinner,webFilterList);

        //region load data and before loading data check internet availability
        if (hasConnection(WebActivity.this)) {
            noDataVisibility(false);
            performServerOperation("web");
        }
        else{
            noDataVisibility(true);
            Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
        }
        //endregion

        activityWebBinding.RefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasConnection(WebActivity.this)) {
                    noDataVisibility(false);
                    webTopicRecyclerView.setVisibility(View.GONE);
                    performServerOperation("web");
                }
                else{
                    noDataVisibility(true);
                    Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
                }
            }
        });

        webFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                if (!queryString.equals("Select Query")) {
                    if (hasConnection(WebActivity.this)) {
                        noDataVisibility(false);
                        webTopicRecyclerView.setVisibility(View.GONE);
                        performServerOperation("web"+queryString);
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

    private void loadListView(){
        allTopicAdapter = new AllTopicAdapter(webTopicList, this, R.layout.adapter_layout_android_topics);
        webTopicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        webTopicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        webTopicRecyclerView.setAdapter(allTopicAdapter);
        allTopicAdapter.notifyDataSetChanged();
        allTopicAdapter.setOnItemClickListener(new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidItem) {
                Intent intent = new Intent(WebActivity.this , DetailsActivity.class);
                intent.putExtra("from", "web");
                intent.putExtra("item", androidItem);
                startActivity(intent);
            }
        });
    }

    //region perform mvvm server fetch
    private void performServerOperation(String queryString){
        //region start the shimmer layout
        activityWebBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        activityWebBinding.shimmerFrameLayout.startShimmerAnimation();
        ///endregion
        webViewModel.getData(this,allUrlClass.ALL_TOPICS_BASE_URL , queryString);
        webViewModel.getWebRepos().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null) {
                    webTopicList = new ArrayList<>(items);
                    webTopicRecyclerView.setVisibility(View.VISIBLE);
                    loadListView();
                    allTopicAdapter.notifyDataSetChanged();
                    noDataVisibility(false);
                }
                else {
                    Toasty.info(getApplicationContext(), getString(R.string.no_data_message), Toasty.LENGTH_LONG).show();
                    noDataVisibility(true);
                }
                ux.removeLoadingView();
            }
        });
    }
    //endregion

    //region set no data related components visible
    private void noDataVisibility(boolean shouldVisible){
        if (shouldVisible) {
            webTopicRecyclerView.setVisibility(View.GONE);
            activityWebBinding.shimmerFrameLayout.setVisibility(View.GONE);
            activityWebBinding.shimmerFrameLayout.stopShimmerAnimation();
            NoData.setVisibility(View.VISIBLE);
            NoDataIV.setVisibility(View.VISIBLE);
        } else {
            webTopicRecyclerView.setVisibility(View.VISIBLE);
            NoData.setVisibility(View.GONE);
            NoDataIV.setVisibility(View.GONE);
        }
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(WebActivity.this, HomeActivity.class));
    }
    //endregion
}