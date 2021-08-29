package app.com.gitlib.activities.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import app.com.gitlib.databinding.ActivityAndroidBinding;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.AndroidRepoViewModel;
import es.dmoral.toasty.Toasty;

import static app.com.gitlib.apiutils.AllUrlClass.ALL_TOPICS_BASE_URL;
import static app.com.gitlib.utils.UtilsManager.hasConnection;

public class AndroidActivity extends AppCompatActivity {
    private ActivityAndroidBinding activityAndroidBinding;
    private static final String TAG = "Shakil::AndroidActivity";
    private UX ux;
    private ArrayList<Item> androidTopicList;
    private String[] androidFilterList = new String[]{"Select Query","Layouts","Drawing",
            "Navigation","Scanning","RecyclerView","ListView","Image Processing","Binding","Debugging"};
    private AdView adView;
    private AndroidRepoViewModel androidRepoViewModel;
    private AllTopicAdapter allTopicAdapter;
    private Spinner FilterSpinner;
    private RecyclerView androidTopicRecyclerView;
    private TextView NoData;
    private ImageView NoDataIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAndroidBinding = DataBindingUtil.setContentView(this, R.layout.activity_android);

        //region init and bin UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI components
    private void init() {
        adView = findViewById(R.id.adView);
        androidTopicList = new ArrayList<>();
        ux = new UX(this);
        FilterSpinner = findViewById(R.id.FilterSpinner);
        androidTopicRecyclerView = findViewById(R.id.mRecyclerView);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        androidRepoViewModel = ViewModelProviders.of(this).get(AndroidRepoViewModel.class);
    }
    //endregion

    //region bind UI components
    private void bindUIWithComponents() {
        //region load first time data
        if (hasConnection(AndroidActivity.this)) {
            noDataVisibility(false);
            performServerOperation("android");
        }
        else{
            noDataVisibility(true);
            Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
        }
        //endregion

        loadListView();

        //region refresh and spinner filter
        activityAndroidBinding.RefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasConnection(AndroidActivity.this)) {
                    noDataVisibility(false);
                    androidTopicRecyclerView.setVisibility(View.GONE);
                    performServerOperation("android");
                }
                else{
                    noDataVisibility(true);
                    Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
                }
            }
        });

        FilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                if (!queryString.equals("Select Query")) {
                    if (hasConnection(AndroidActivity.this)) {
                        noDataVisibility(false);
                        androidTopicRecyclerView.setVisibility(View.GONE);
                        performServerOperation("android"+queryString);
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
        //endregion

        //region toolbar on back click listener
        activityAndroidBinding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AndroidActivity.this,HomeActivity.class));
            }
        });
        //endregion

        ux.setSpinnerAdapter(FilterSpinner,androidFilterList);

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
    //endregion

    //region load list data
    private void loadListView(){
        allTopicAdapter = new AllTopicAdapter(androidTopicList, this, R.layout.adapter_layout_android_topics);
        androidTopicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        androidTopicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        androidTopicRecyclerView.setAdapter(allTopicAdapter);
        allTopicAdapter.notifyDataSetChanged();
        allTopicAdapter.setOnItemClickListener(new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidItem) {
                Intent intent = new Intent(AndroidActivity.this , DetailsActivity.class);
                intent.putExtra("from","android");
                intent.putExtra("item", androidItem);
                startActivity(intent);
            }
        });
    }
    //endregion

    //region perform mvvm server fetch
    private void performServerOperation(String queryString){
        //region start the shimmer layout
        activityAndroidBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        activityAndroidBinding.shimmerFrameLayout.startShimmerAnimation();
        ///endregion
        androidRepoViewModel.getData(this,ALL_TOPICS_BASE_URL , queryString);
        androidRepoViewModel.getAndroidRepos().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                activityAndroidBinding.shimmerFrameLayout.stopShimmerAnimation();
                activityAndroidBinding.shimmerFrameLayout.setVisibility(View.GONE);
                if (items != null) {
                    androidTopicList = new ArrayList<>(items);
                    androidTopicRecyclerView.setVisibility(View.VISIBLE);
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
    //endregion

    //region set no data related components visible
    private void noDataVisibility(boolean shouldVisible){
        if (shouldVisible) {
            androidTopicRecyclerView.setVisibility(View.GONE);
            activityAndroidBinding.shimmerFrameLayout.setVisibility(View.GONE);
            activityAndroidBinding.shimmerFrameLayout.stopShimmerAnimation();
            NoData.setVisibility(View.VISIBLE);
            NoDataIV.setVisibility(View.VISIBLE);
        } else {
            androidTopicRecyclerView.setVisibility(View.VISIBLE);
            NoData.setVisibility(View.GONE);
            NoDataIV.setVisibility(View.GONE);
        }
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AndroidActivity.this, HomeActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //endregion
}