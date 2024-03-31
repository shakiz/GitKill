package app.com.gitlib.activities.developers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
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
import app.com.gitlib.adapters.UserRepositoriesAdapter;
import app.com.gitlib.databinding.ActivityDevelopersDetailsBinding;
import app.com.gitlib.models.repositories.Repo;
import app.com.gitlib.models.users.Developer;
import app.com.gitlib.utils.UX;
import app.com.gitlib.viewmodels.SingleDeveloperViewModel;
import app.com.gitlib.viewmodels.UserRepoDetailsViewModel;

import static app.com.gitlib.utils.UX.launchMarket;
import static app.com.gitlib.utils.UtilsManager.hasConnection;
import static app.com.gitlib.utils.UtilsManager.internetErrorDialog;

public class DevelopersDetailsActivity extends AppCompatActivity {
    private ActivityDevelopersDetailsBinding activityBinding;
    private String userName = "";
    private SingleDeveloperViewModel viewModel;
    private UserRepoDetailsViewModel userRepoDetailsViewModel;
    private UX ux;
    private List<Repo> mRepositoryList;
    private UserRepositoriesAdapter userRepositoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_developers_details);

        //region get intent data
        getIntentData();
        //endregion

        //region init and bind UI components
        init();
        bindUIWithComponents();
        //endregion
    }

    //region get intent data
    private void getIntentData(){
        if (getIntent().getStringExtra("user") != null){
            userName = getIntent().getStringExtra("user");
        }
    }
    //endregion

    //region init UI components
    private void init() {
        viewModel = ViewModelProviders.of(this).get(SingleDeveloperViewModel.class);
        userRepoDetailsViewModel = ViewModelProviders.of(this).get(UserRepoDetailsViewModel.class);
        ux = new UX(this);
    }
    //endregion

    //region bind UI components
    private void bindUIWithComponents() {
        //region toolbar on back click listener and set toolbar title
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DevelopersDetailsActivity.this, TrendingDevelopersListActivity.class));
            }
        });
        //endregion

        //region fetch user basic details
        fetchDataForUserDetails(userName);
        //endregion

        //region rate now button click
        activityBinding.rateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMarket(DevelopersDetailsActivity.this);
            }
        });
        //endregion

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
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.v("onAdListener","AdClosed");
            }
        });
        //endregion
    }
    //endregion

    //region perform mvvm server fetch for user basic details
    private void fetchDataForUserDetails(String url){
        if (hasConnection(DevelopersDetailsActivity.this)) {
            ux.getLoadingView();
            viewModel.getData(this,url);
            viewModel.getDeveloper().observe(this, new Observer<Developer>() {
                @Override
                public void onChanged(Developer developer) {
                    if (developer != null) {
                        setData(developer);
                    } else {
                        Toast.makeText(DevelopersDetailsActivity.this,R.string.no_data_message,Toast.LENGTH_SHORT).show();
                    }
                    ux.removeLoadingView();
                    //region after user basic details loaded , this method will load the repositories
                    fetchDataForUserRepositoryDetails(userName+"/repos");
                    //endregion
                }
            });
        }
        else{
            internetErrorDialog(DevelopersDetailsActivity.this);
        }
    }
    //endregion

    //region set UI data
    private void setData(Developer developer){
        activityBinding.userNameTxt.setText("@"+developer.getLogin());
        activityBinding.name.setText(developer.getName());

        if (developer.getFollowers() != 0) {
            activityBinding.followers.setText(String.valueOf(developer.getFollowers()));
        } else {
            activityBinding.followers.setText("0");
        }

        if (developer.getHtml_url() != null) {
            activityBinding.htmlUrl.setText(developer.getHtml_url());
        } else {
            activityBinding.htmlUrl.setText("No profile url found");
        }

        if (developer.getFollowing() != 0) {
            activityBinding.following.setText(String.valueOf(developer.getFollowing()));
        } else {
            activityBinding.following.setText("0");
        }

        if (developer.getPublicRepos() != 0) {
            activityBinding.publicRepos.setText(String.valueOf(developer.getPublicRepos()));
        } else {
            activityBinding.publicRepos.setText("0");
        }

        if (developer.getPublicGists() != 0) {
            activityBinding.gists.setText(String.valueOf(developer.getPublicGists()));
        } else {
            activityBinding.gists.setText("0");
        }

        if (developer.getBio() != null) {
            activityBinding.bio.setText(developer.getBio().toString());
        } else {
            activityBinding.bio.setText("No bio found");
        }

        if (!TextUtils.isEmpty(developer.getCreatedAt())) {
            activityBinding.createdAt.setText("Joined on "+developer.getCreatedAt().split("T")[0]);
        } else {
            activityBinding.createdAt.setText("No joining date found");
        }

        if (developer.getEmail() != null) {
            activityBinding.email.setText(developer.getEmail().toString());
        } else {
            activityBinding.email.setText("No email found");
        }

        if (!TextUtils.isEmpty(developer.getBlog())) {
            activityBinding.blog.setText(developer.getBlog());
        } else {
            activityBinding.blog.setText("No blog address found");
        }

        if (developer.getCompany() != null) {
            activityBinding.company.setText(developer.getCompany().toString());
        } else {
            activityBinding.company.setText("No company information found");
        }

        if (!TextUtils.isEmpty(developer.getLocation())) {
            activityBinding.location.setText(developer.getLocation());
        } else {
            activityBinding.location .setText("No location information found");
        }

        if (developer.getHireable() != null){
            if (developer.getHireable().toString().equals("true")){
                activityBinding.hireable.setText("Hireable");
            } else{
                activityBinding.hireable.setText("Not Hireable");
            }
        }else {
            activityBinding.hireable.setText("Not Hireable");
        }

        if (developer.getAvatarUrl() != null){
            Glide.with(this).load(developer.getAvatarUrl()).into(activityBinding.userAvatar);
        }
    }
    //endregion

    //region perform mvvm server fetch for user all repositories
    private void fetchDataForUserRepositoryDetails(String url){
        activityBinding.progress.setVisibility(View.VISIBLE);
        if (hasConnection(DevelopersDetailsActivity.this)) {
            userRepoDetailsViewModel.getData(this,url);
            userRepoDetailsViewModel.getRepositories().observe(this, new Observer<List<Repo>>() {
                @Override
                public void onChanged(List<Repo> repos) {
                    mRepositoryList = new ArrayList<>(repos);
                    if (mRepositoryList.size() <= 0){
                        Toast.makeText(DevelopersDetailsActivity.this,R.string.no_data_message,Toast.LENGTH_SHORT).show();
                    }
                    loadListView();
                    userRepositoriesAdapter.notifyDataSetChanged();
                    activityBinding.progress.setVisibility(View.GONE);
                }
            });
        }
        else{
            internetErrorDialog(DevelopersDetailsActivity.this);
        }
    }
    //endregion

    //region load list with recycler and adapter
    private void loadListView(){
        userRepositoriesAdapter = new UserRepositoriesAdapter(mRepositoryList, this);
        activityBinding.allRepositoryRecycler.setLayoutManager(new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false));
        activityBinding.allRepositoryRecycler.setItemAnimator(new DefaultItemAnimator());
        activityBinding.allRepositoryRecycler.setAdapter(userRepositoriesAdapter);
        userRepositoriesAdapter.notifyDataSetChanged();
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DevelopersDetailsActivity.this, TrendingDevelopersListActivity.class));
    }
    //endregion
}