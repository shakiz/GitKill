package app.com.gitlib.activities.developers;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import app.com.gitlib.models.users.TrendingDevelopersNew;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.TrendingDevelopersViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import static app.com.gitlib.apiutils.AllUrlClass.BASE_URL;
import static app.com.gitlib.utils.UtilsManager.hasConnection;
import static app.com.gitlib.utils.UtilsManager.internetErrorDialog;

public class TrendingDevelopersActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDevelopers;
    private ArrayList<TrendingDevelopersNew> trendingDevelopersList;
    private String TAG = "Shakil::TrendingDevelopersActivity" , languageStr = "" , sinceStr = "";
    private UX ux;
    private ImageView search;
    private CircleImageView refreshListButton;
    private Dialog itemDialog;
    private RelativeLayout dialogLayout;
    private TextView NoData;
    private ImageView NoDataIV;
    //Dialog components
    private TextView  UserName , RepoName , ProfileLink , RepoLink , Description;
    private AdView adView;
    private TrendingDevelopersAdapter trendingDevelopersAdapter;
    private TrendingDevelopersViewModel trendingDevelopersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_developers);

        //region init and bind UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI components
    private void init() {
        recyclerViewDevelopers = findViewById(R.id.mRecyclerView);
        refreshListButton = findViewById(R.id.RefreshList);
        search = findViewById(R.id.Search);
        adView = findViewById(R.id.adView);
        trendingDevelopersList = new ArrayList<>();
        ux = new UX(this);
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
                startActivity(new Intent(TrendingDevelopersActivity.this,HomeActivity.class));
            }
        });
        //endregion

        performServerOperation("tom");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUrl = BASE_URL+"developers?"+"language="+languageStr+"&since="+sinceStr;
                Log.v("newUrl",newUrl);
                performServerOperation(newUrl);
            }
        });

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            public void respond(TrendingDevelopersNew trendingDevelopers) {
            }
        });
        recyclerViewDevelopers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDevelopers.setAdapter(trendingDevelopersAdapter);
        trendingDevelopersAdapter.notifyDataSetChanged();
    }
    //endregion

    //region init dialog components
    private void customViewInit(Dialog itemDialog) {
        UserName = itemDialog.findViewById(R.id.UserName);
        RepoName = itemDialog.findViewById(R.id.RepoName);
        RepoLink = itemDialog.findViewById(R.id.RepoLink);
        ProfileLink = itemDialog.findViewById(R.id.ProfileLink);
        Description = itemDialog.findViewById(R.id.Description);
        dialogLayout = itemDialog.findViewById(R.id.dialogLayout);
    }
    //endregion

    //region perform mvvm server fetch
    private void performServerOperation(String url){
        if (hasConnection(TrendingDevelopersActivity.this)) {
            ux.getLoadingView();
            trendingDevelopersViewModel.getData(this,url);
            trendingDevelopersViewModel.getDevelopersList().observe(this, new Observer<List<TrendingDevelopersNew>>() {
                @Override
                public void onChanged(List<TrendingDevelopersNew> items) {
                    if (items != null) {
                        trendingDevelopersList = new ArrayList<>(items);
                        if (trendingDevelopersList.size() <= 0){
                            noDataVisibility(true);
                            Toasty.error(TrendingDevelopersActivity.this,R.string.no_data_message).show();
                        }
                        loadListView();
                        trendingDevelopersAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TrendingDevelopersActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                    ux.removeLoadingView();
                }
            });
        }
        else{
            noDataVisibility(true);
            internetErrorDialog(TrendingDevelopersActivity.this);
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
        startActivity(new Intent(TrendingDevelopersActivity.this, HomeActivity.class));
    }
    //endregion
}