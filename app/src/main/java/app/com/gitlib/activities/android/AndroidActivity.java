package app.com.gitlib.activities.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.utils.UX;
import app.com.gitlib.utils.UtilsManager;
import app.com.gitlib.viewmodels.AndroidRepoViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class AndroidActivity extends AppCompatActivity {
    private static final String TAG = "Shakil::AndroidActivity";
    private RecyclerView androidTopicRecyclerView;
    private UX ux;
    private UtilsManager utilsManager;
    private ArrayList<Item> androidTopicList;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private CircleImageView refreshListButton;
    private Spinner androidFilterSpinner;
    private TextView NoData;
    private ImageView NoDataIV;
    private String[] androidFilterList = new String[]{"Select Query","Layouts","Drawing",
            "Navigation","Scanning","RecyclerView","ListView","Image Processing","Binding","Debugging"};
    private AdView adView;
    private AndroidRepoViewModel androidRepoViewModel;
    private AllTopicAdapter allTopicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);

        //region init and bin UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI components
    private void init() {
        androidTopicRecyclerView = findViewById(R.id.mRecyclerView);
        refreshListButton = findViewById(R.id.RefreshList);
        androidFilterSpinner = findViewById(R.id.FilterSpinner);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        adView = findViewById(R.id.adView);
        androidTopicList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        ux = new UX(this);
        utilsManager = new UtilsManager(this);
        androidRepoViewModel = ViewModelProviders.of(this).get(AndroidRepoViewModel.class);
    }
    //endregion

    //region bind UI components
    private void bindUIWithComponents() {
        //region load first time data
        performServerOperation("android");
        //endregion

        loadListView();

        //region refresh and spinner filter
        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performServerOperation("android");
            }
        });

        androidFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                performServerOperation("android"+queryString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //endregion

        //region toolbar on back click listener
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AndroidActivity.this,HomeActivity.class));
            }
        });
        //endregion

        ux.setSpinnerAdapter(androidFilterSpinner,androidFilterList);

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
        ux.getLoadingView();
        androidRepoViewModel.getData(this,allUrlClass.ALL_TOPICS_BASE_URL , queryString);
        androidRepoViewModel.getAndroidRepos().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                androidTopicList = new ArrayList<>(items);
                loadListView();
                allTopicAdapter.notifyDataSetChanged();
                ux.removeLoadingView();
            }
        });
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AndroidActivity.this, HomeActivity.class));
    }
    //endregion
}