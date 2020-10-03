package app.com.gitlib.activities.developers;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import app.com.gitlib.adapters.TrendingDevelopersAdapter;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.users.TrendingDevelopers;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.TrendingDevelopersViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import static app.com.gitlib.apiutils.AllUrlClass.BASE_URL;

public class TrendingDevelopersActivity extends AppCompatActivity {
    private Spinner languageSpinner,sinceSpinner;
    private RecyclerView recyclerViewDevelopers;
    private ArrayList<String> languageList, timeList;
    private ArrayList<TrendingDevelopers> trendingDevelopersList;
    private AllUrlClass allUrlClass;
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
        languageSpinner = findViewById(R.id.LanguageSpinner);
        sinceSpinner = findViewById(R.id.SinceSpinner);
        recyclerViewDevelopers = findViewById(R.id.mRecyclerView);
        refreshListButton = findViewById(R.id.RefreshList);
        search = findViewById(R.id.Search);
        adView = findViewById(R.id.adView);
        trendingDevelopersList = new ArrayList<>();
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
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

        setSpinnerData();
        performServerOperation("");
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
                String newUrl = BASE_URL+"developers?"+"language="+languageStr+"&since="+sinceStr;
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

    //region set spinner data
    private void setSpinnerData() {
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
    //endregion

    //region load list data and set adapter
    private void loadListView(){
        trendingDevelopersAdapter= new TrendingDevelopersAdapter(trendingDevelopersList, this);
        trendingDevelopersAdapter.setOnItemClickListener(new TrendingDevelopersAdapter.onItemClickListener() {
            @Override
            public void respond(TrendingDevelopers trendingDevelopers) {
                showDialog(trendingDevelopers);
            }
        });
        recyclerViewDevelopers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDevelopers.setAdapter(trendingDevelopersAdapter);
        trendingDevelopersAdapter.notifyDataSetChanged();
    }
    //endregion

    //region show dialog
    private void showDialog(TrendingDevelopers trendingDevelopers) {
        itemDialog = new Dialog(TrendingDevelopersActivity.this);
        itemDialog.setContentView(R.layout.popup_trending_developers_items_details);
        customViewInit(itemDialog);
        itemDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Animation a = AnimationUtils.loadAnimation(itemDialog.getContext(), R.anim.push_up_in);
        dialogLayout.startAnimation(a);
        setCustomDialogData(trendingDevelopers);
        itemDialog.show();
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

    //region set custom dialog data
    private void setCustomDialogData(final TrendingDevelopers trendingDevelopers) {
        UserName.setText(trendingDevelopers.getUsername());
        ProfileLink.setText(trendingDevelopers.getUrl());

        RepoName.setText(trendingDevelopers.getName());

        if (!TextUtils.isEmpty(trendingDevelopers.getRepo().getDescription())) Description.setText("No description available for this repository");
        else Description.setText(trendingDevelopers.getRepo().getDescription());

        RepoLink.setText(trendingDevelopers.getRepo().getUrl());

        RepoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(trendingDevelopers.getRepo().getUrl()));
                startActivity(browserIntent);
            }
        });

        ProfileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(trendingDevelopers.getUrl()));
                startActivity(browserIntent);
            }
        });
    }
    //endregion

    //region perform mvvm server fetch
    private void performServerOperation(String url){
        ux.getLoadingView();
        trendingDevelopersViewModel.getData(this,url);
        trendingDevelopersViewModel.getDevelopersList().observe(this, new Observer<List<TrendingDevelopers>>() {
            @Override
            public void onChanged(List<TrendingDevelopers> items) {
                trendingDevelopersList = new ArrayList<>(items);
                if (trendingDevelopersList.size() <= 0){
                    NoData.setVisibility(View.VISIBLE);
                    NoDataIV.setVisibility(View.VISIBLE);
                    Toasty.error(TrendingDevelopersActivity.this,R.string.no_data_message).show();
                }
                loadListView();
                trendingDevelopersAdapter.notifyDataSetChanged();
                ux.removeLoadingView();
            }
        });
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TrendingDevelopersActivity.this, HomeActivity.class));
    }
    //endregion
}