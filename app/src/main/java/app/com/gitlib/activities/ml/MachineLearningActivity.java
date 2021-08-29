package app.com.gitlib.activities.ml;

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
import app.com.gitlib.databinding.ActivityMachineLearningBinding;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.MachineLearningViewModel;
import es.dmoral.toasty.Toasty;

import static app.com.gitlib.apiutils.AllUrlClass.ALL_TOPICS_BASE_URL;
import static app.com.gitlib.utils.UtilsManager.hasConnection;

public class MachineLearningActivity extends AppCompatActivity {
    private ActivityMachineLearningBinding activityBinding;
    private RecyclerView mlRecyclerView;
    private AllTopicAdapter allTopicAdapter;
    private UX ux;
    private ArrayList<Item> machineLearningList;
    private Spinner mlFilterSpinner;
    private TextView NoData;
    private ImageView NoDataIV;
    private String[] mlFilterList = new String[]{"Select Query","Big Data","Data Science",
            "Natural Language Processing","Neural Network","Deep Learning"};
    private AdView adView;
    private MachineLearningViewModel machineLearningViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_machine_learning);

        //region init and bin UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    private void init() {
        mlRecyclerView = findViewById(R.id.mRecyclerView);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        mlFilterSpinner = findViewById(R.id.FilterSpinner);
        adView = findViewById(R.id.adView);
        machineLearningList = new ArrayList<>();
        ux = new UX(this);
        machineLearningViewModel = ViewModelProviders.of(this).get(MachineLearningViewModel.class);
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

        //region load first time data
        if (hasConnection(MachineLearningActivity.this)) {
            noDataVisibility(false);
            performServerOperation("ml");
        }
        else{
            noDataVisibility(true);
            Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
        }
        //endregion

        activityBinding.RefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasConnection(MachineLearningActivity.this)) {
                    noDataVisibility(false);
                    mlRecyclerView.setVisibility(View.GONE);
                    performServerOperation("ml");
                }
                else{
                    noDataVisibility(true);
                    Toasty.info(getApplicationContext(), getString(R.string.please_Enable_internet_connection), Toasty.LENGTH_LONG).show();
                }
            }
        });

        mlFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                if (!queryString.equals("Select Query")) {
                    if (hasConnection(MachineLearningActivity.this)) {
                        noDataVisibility(false);
                        mlRecyclerView.setVisibility(View.GONE);
                        performServerOperation(""+queryString);
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

    //region load list with recycler and adapter
    private void loadListView(){
        allTopicAdapter = new AllTopicAdapter(machineLearningList, this, R.layout.adapter_layout_android_topics);
        mlRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mlRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mlRecyclerView.setAdapter(allTopicAdapter);
        allTopicAdapter.notifyDataSetChanged();
        allTopicAdapter.setOnItemClickListener(new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidItem) {
                Intent intent = new Intent(MachineLearningActivity.this , DetailsActivity.class);
                intent.putExtra("from", "ml");
                intent.putExtra("item", androidItem);
                startActivity(intent);
            }
        });
    }
    //endregion

    //region perform mvvm server fetch
    private void performServerOperation(String queryString){
        //region start the shimmer layout
        activityBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        activityBinding.shimmerFrameLayout.startShimmerAnimation();
        ///endregion
        machineLearningViewModel.getData(this,ALL_TOPICS_BASE_URL , queryString);
        machineLearningViewModel.getMLRepos().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                activityBinding.shimmerFrameLayout.stopShimmerAnimation();
                activityBinding.shimmerFrameLayout.setVisibility(View.GONE);
                if (items != null) {
                    machineLearningList = new ArrayList<>(items);
                    mlRecyclerView.setVisibility(View.VISIBLE);
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
            mlRecyclerView.setVisibility(View.GONE);
            activityBinding.shimmerFrameLayout.setVisibility(View.GONE);
            activityBinding.shimmerFrameLayout.stopShimmerAnimation();
            NoData.setVisibility(View.VISIBLE);
            NoDataIV.setVisibility(View.VISIBLE);
        } else {
            mlRecyclerView.setVisibility(View.VISIBLE);
            NoData.setVisibility(View.GONE);
            NoDataIV.setVisibility(View.GONE);
        }
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MachineLearningActivity.this, HomeActivity.class));
    }
    //endregion
}