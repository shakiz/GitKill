package app.com.gitlib.activities.repositories;

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
import app.com.gitlib.adapters.TrendingRepositoriesAdapter;
import app.com.gitlib.models.repositories.TrendingRepositories;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.TrendingRepositoriesViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import static app.com.gitlib.apiutils.AllUrlClass.BASE_URL;
import static app.com.gitlib.utils.UtilsManager.hasConnection;
import static app.com.gitlib.utils.UtilsManager.internetErrorDialog;

public class TrendingRepositoriesActivity extends AppCompatActivity {
    private Spinner languageSpinner,sinceSpinner;
    private ArrayList<String> languageList, timeList;
    private RecyclerView recyclerViewRepo;
    private TrendingRepositoriesAdapter trendingRepositoriesAdapter;
    private ArrayList<TrendingRepositories> trendingRepoList;
    private String TAG = "TrendingRepositoriesActivity" , languageStr = "" , sinceStr = "";
    private ImageView search;
    private CircleImageView refreshListButton;
    private Dialog itemDialog ;
    private RelativeLayout dialogLayout;
    private UX ux;
    private TextView NoData;
    private ImageView NoDataIV;
    //Dialog components
    private TextView RepoName , RepoLink , UserName , Language , NumberOfStars , NumberOfForks , Description;
    private AdView adView;
    private TrendingRepositoriesViewModel repositoriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_repositories);

        //region init and bin UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI components
    private void init() {
        recyclerViewRepo = findViewById(R.id.mRecyclerView);
        languageSpinner = findViewById(R.id.LanguageSpinner);
        sinceSpinner = findViewById(R.id.SinceSpinner);
        refreshListButton = findViewById(R.id.RefreshList);
        NoData = findViewById(R.id.NoDataMessage);
        NoDataIV = findViewById(R.id.NoDataIV);
        search = findViewById(R.id.Search);
        adView = findViewById(R.id.adView);
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
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
        performServerOperation("");
        //endregion

        loadListView();
        ux.setSpinnerAdapter(languageSpinner,languageList);
        ux.setSpinnerAdapter(sinceSpinner,timeList);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                languageStr = adapterView.getItemAtPosition(position).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                sinceStr = adapterView.getItemAtPosition(position).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUrl = BASE_URL+"repositories?"+"language="+languageStr+"&since="+sinceStr;
                Log.v("SpinnerURL",newUrl);
                performServerOperation(newUrl);
            }
        });

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performServerOperation("");
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
        languageList.add("Java");
        languageList.add("Python");
        languageList.add("C");
        languageList.add("C++");
        languageList.add("C#");
        languageList.add("PHP");
        //Adding data to time list
        timeList.add("Daily");
        timeList.add("Weekly");
        timeList.add("Monthly");
        timeList.add("Yearly");
    }

    private void showDialog(TrendingRepositories trendingRepositories) {
        itemDialog = new Dialog(TrendingRepositoriesActivity.this);
        itemDialog.setContentView(R.layout.popup_trending_repo_items_details);
        customViewInit(itemDialog);
        itemDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Animation a = AnimationUtils.loadAnimation(itemDialog.getContext(), R.anim.push_up_in);
        dialogLayout.startAnimation(a);
        setCustomDialogData(trendingRepositories);
        itemDialog.show();
    }

    private void setCustomDialogData(final TrendingRepositories trendingRepositories) {
        UserName.setText(trendingRepositories.getAuthor());
        RepoName.setText(trendingRepositories.getName());

        if (trendingRepositories.getDescription() == null) Description.setText("No description available for this repository");
        else Description.setText(trendingRepositories.getDescription());

        if (trendingRepositories.getLanguage() == null) Language.setText("No language selected for this repository");
        else Language.setText(trendingRepositories.getLanguage());

        RepoLink.setText(trendingRepositories.getUrl());
        NumberOfStars.setText("Stars : "+trendingRepositories.getStars());
        NumberOfForks.setText("Forks : "+trendingRepositories.getForks());

        RepoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(trendingRepositories.getUrl()));
                startActivity(browserIntent);
            }
        });
    }

    private void customViewInit(Dialog itemDialog) {
        UserName = itemDialog.findViewById(R.id.UserName);
        RepoName = itemDialog.findViewById(R.id.RepoName);
        RepoLink = itemDialog.findViewById(R.id.RepoLink);
        Description = itemDialog.findViewById(R.id.Description);
        Language = itemDialog.findViewById(R.id.Language);
        NumberOfStars = itemDialog.findViewById(R.id.NumberOfStars);
        NumberOfForks = itemDialog.findViewById(R.id.NumberOfForks);
        dialogLayout = itemDialog.findViewById(R.id.dialogLayout);
    }

    //region perform mvvm server fetch
    private void performServerOperation(String url){
        if (hasConnection(TrendingRepositoriesActivity.this)) {
            ux.getLoadingView();
            repositoriesViewModel.getData(this,url);
            repositoriesViewModel.getAndroidRepos().observe(this, new Observer<List<TrendingRepositories>>() {
                @Override
                public void onChanged(List<TrendingRepositories> items) {
                    trendingRepoList = new ArrayList<>(items);
                    if (trendingRepoList.size() <= 0){
                        noDataVisibility(true);
                        Toasty.error(TrendingRepositoriesActivity.this,R.string.no_data_message).show();
                    }
                    loadListView();
                    trendingRepositoriesAdapter.notifyDataSetChanged();
                    ux.removeLoadingView();
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
        trendingRepositoriesAdapter = new TrendingRepositoriesAdapter(trendingRepoList, this, new TrendingRepositoriesAdapter.onItemClickListener() {
            @Override
            public void respond(TrendingRepositories trendingRepositories) {
                showDialog(trendingRepositories);
            }
        });
        recyclerViewRepo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRepo.setAdapter(trendingRepositoriesAdapter);
        trendingRepositoriesAdapter.notifyDataSetChanged();
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