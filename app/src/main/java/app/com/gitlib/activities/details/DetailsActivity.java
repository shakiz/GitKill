package app.com.gitlib.activities.details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import app.com.gitlib.R;
import app.com.gitlib.activities.android.AndroidActivity;
import app.com.gitlib.activities.ml.MachineLearningActivity;
import app.com.gitlib.activities.repositories.TrendingRepositoriesActivity;
import app.com.gitlib.activities.web.WebActivity;
import app.com.gitlib.adapters.DetailsFragmentPagerAdapter;
import app.com.gitlib.databinding.ActivityDetailsBinding;
import app.com.gitlib.models.alltopic.Item;
import es.dmoral.toasty.Toasty;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding activityDetailsBinding;
    private static Item item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        //region init UI and bind those UI to perform UI interactions
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI
    private void init() {
    }
    //endregion

    //region bind those UI to perform UI interactions
    private void bindUIWithComponents() {
        //region back click listener
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //endregion

        //region get data from intent and perform certain operations
        getData();
        if (item != null){
            setViewPager();
            setData();
            if (activityDetailsBinding.noDataLayout.getVisibility() == View.VISIBLE) {
                activityDetailsBinding.noDataLayout.setVisibility(View.GONE);
            }
        }
        else{
            Toasty.info(DetailsActivity.this,"No Item Found");
            if (activityDetailsBinding.noDataLayout.getVisibility() == View.GONE) {
                activityDetailsBinding.noDataLayout.setVisibility(View.VISIBLE);
            }
        }
        //endregion
    }
    //endregion

    //region get intent parcelable object
    private void getData(){
        if (getIntent().getExtras().getParcelable("item") != null){
            item = (Item) getIntent().getExtras().getParcelable("item");
            Log.v("Shakil : ", "ScholarshipDetailsActivity : getIntentData: "+item.getFull_name());
        }
    }
    //endregion

    //region set data after getting intent data
    private void setData(){
        activityDetailsBinding.UserName.setText(item.getFull_name());
        activityDetailsBinding.RepoLink.setText(item.getHtml_url());

        if (item.getDescription() == null) activityDetailsBinding.Description.setText("No description for this repository.");
        else activityDetailsBinding.Description.setText(item.getDescription());

        activityDetailsBinding.NumberOfForks.setText(""+item.getForks_count());
        activityDetailsBinding.NumberOfStars.setText(""+item.getStargazers_count());
        activityDetailsBinding.NumberOfWatch.setText(""+item.getWatchers_count());

        if (item.getOpen_issues() == null || item.getOpen_issues() == 0) activityDetailsBinding.NumberOfIssues.setText("0");
        else activityDetailsBinding.NumberOfIssues.setText(""+item.getOpen_issues());

        if (item.getLanguage() == null) activityDetailsBinding.Language.setText("No language");
        else activityDetailsBinding.Language.setText(item.getLanguage());

        if (item.getCreated_at() == null) activityDetailsBinding.CreatedAt.setText("No data found");
        else activityDetailsBinding.CreatedAt.setText(item.getCreated_at());

        if (item.getUpdated_at() == null) activityDetailsBinding.UpdatedAt.setText("No data found");
        else activityDetailsBinding.UpdatedAt.setText(item.getUpdated_at());
    }
    //endregion

    //region setup viewpager
    private void setViewPager(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        DetailsFragmentPagerAdapter adapter = new DetailsFragmentPagerAdapter(getSupportFragmentManager(), this, item.getFull_name());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    //endregion

    //region activity components
    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("from").equals("android")){
            startActivity(new Intent(DetailsActivity.this, AndroidActivity.class));
        }
        else if (getIntent().getStringExtra("from").equals("ml")){
            startActivity(new Intent(DetailsActivity.this, MachineLearningActivity.class));
        }
        else if (getIntent().getStringExtra("from").equals("web")){
            startActivity(new Intent(DetailsActivity.this, WebActivity.class));
        }
        else if (getIntent().getStringExtra("from").equals("trendingRepositories")){
            startActivity(new Intent(DetailsActivity.this, TrendingRepositoriesActivity.class));
        }
    }
    //endregion
}
