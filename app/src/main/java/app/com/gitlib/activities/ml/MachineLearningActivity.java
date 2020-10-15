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
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.MachineLearningViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import static app.com.gitlib.apiutils.AllUrlClass.ALL_TOPICS_BASE_URL;
import static app.com.gitlib.utils.UtilsManager.hasConnection;
import static app.com.gitlib.utils.UtilsManager.internetErrorDialog;

public class MachineLearningActivity extends AppCompatActivity {
    private RecyclerView mlRecyclerView;
    private AllTopicAdapter allTopicAdapter;
    private UX ux;
    private ArrayList<Item> mlItemList;
    private Spinner mlFilterSpinner;
    private CircleImageView refreshListButton;
    private TextView NoData;
    private ImageView NoDataIV;
    private String[] mlFilterList = new String[]{"Select Query","Big Data","Data Science",
            "Natural Language Processing","Neural Network","Deep Learning"};
    private AdView adView;
    private MachineLearningViewModel machineLearningViewModel;

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
        mlRecyclerView = findViewById(R.id.mRecyclerView);
        refreshListButton = findViewById(R.id.RefreshList);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        mlFilterSpinner = findViewById(R.id.FilterSpinner);
        adView = findViewById(R.id.adView);
        mlItemList = new ArrayList<>();
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
            performServerOperation("ml");
        }
        else{
            noDataVisibility(true);
            internetErrorDialog(MachineLearningActivity.this);
        }
        //endregion

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasConnection(MachineLearningActivity.this)) {
                    performServerOperation("ml");
                }
                else{
                    noDataVisibility(true);
                    internetErrorDialog(MachineLearningActivity.this);
                }
            }
        });

        mlFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                if (hasConnection(MachineLearningActivity.this)) {
                    performServerOperation(""+queryString);
                }
                else{
                    noDataVisibility(true);
                    internetErrorDialog(MachineLearningActivity.this);
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

    //region load list with recycler and adapter
    private void loadListView(){
        allTopicAdapter = new AllTopicAdapter(mlItemList, this, R.layout.adapter_layout_android_topics);
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
        ux.getLoadingView();
        machineLearningViewModel.getData(this,ALL_TOPICS_BASE_URL , queryString);
        machineLearningViewModel.getMLRepos().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null) {
                    mlItemList = new ArrayList<>(items);
                    if (mlItemList.size() <= 0){
                        noDataVisibility(true);
                        Toast.makeText(MachineLearningActivity.this,R.string.no_data_message,Toast.LENGTH_SHORT).show();
                    }
                    loadListView();
                    allTopicAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MachineLearningActivity.this, "No data found", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(MachineLearningActivity.this, HomeActivity.class));
    }
    //endregion
}