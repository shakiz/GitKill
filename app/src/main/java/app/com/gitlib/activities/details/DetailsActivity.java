package app.com.gitlib.activities.details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import app.com.gitlib.R;
import app.com.gitlib.activities.android.AndroidActivity;
import app.com.gitlib.activities.ml.MachineLearningActivity;
import app.com.gitlib.activities.web.WebActivity;
import app.com.gitlib.adapters.DetailsFragmentPagerAdapter;
import app.com.gitlib.models.alltopic.Item;
import es.dmoral.toasty.Toasty;

public class DetailsActivity extends AppCompatActivity {
    private TextView UserName , RepoLink ,Description , NumberOfForks , NumberOfStars , NumberOfWatch , NumberOfIssues , Language , CreatedAt , UpdatedAt;
    private LinearLayout noDataLayout;
    private static Item item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //region init UI and bind those UI to perform UI interactions
        init();
        bindUIWithComponents();
        //endregion
    }

    //region init UI
    private void init() {
        UserName = findViewById(R.id.UserName);
        RepoLink = findViewById(R.id.RepoLink);
        Description = findViewById(R.id.Description);
        NumberOfForks = findViewById(R.id.NumberOfForks);
        NumberOfStars = findViewById(R.id.NumberOfStars);
        NumberOfWatch = findViewById(R.id.NumberOfWatch);
        NumberOfIssues = findViewById(R.id.NumberOfIssues);
        Language = findViewById(R.id.Language);
        CreatedAt = findViewById(R.id.Created_At);
        UpdatedAt = findViewById(R.id.Updated_At);
        noDataLayout = findViewById(R.id.noDataLayout);
    }
    //endregion

    //region bind those UI to perform UI interactions
    private void bindUIWithComponents() {
        //region back click listener
        findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("from").equals("android")){
                    startActivity(new Intent(DetailsActivity.this, AndroidActivity.class));
                }
                else if (getIntent().getStringExtra("from").equals("ml")){
                    startActivity(new Intent(DetailsActivity.this, MachineLearningActivity.class));
                }
                else if (getIntent().getStringExtra("from").equals("web")){
                    startActivity(new Intent(DetailsActivity.this, WebActivity.class));
                }
            }
        });
        //endregion

        //region get data from intent and perform certain operations
        getData();
        if (item != null){
            setViewPager();
            setData();
            if (noDataLayout.getVisibility() == View.VISIBLE) {
                noDataLayout.setVisibility(View.GONE);
            }
        }
        else{
            Toasty.info(DetailsActivity.this,"No Item Found");
            if (noDataLayout.getVisibility() == View.GONE) {
                noDataLayout.setVisibility(View.VISIBLE);
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
        UserName.setText(item.getFull_name());
        RepoLink.setText(item.getHtml_url());

        if (item.getDescription() == null) Description.setText("No description for this repository.");
        else Description.setText(item.getDescription());

        NumberOfForks.setText(""+item.getForks_count());
        NumberOfStars.setText(""+item.getStargazers_count());
        NumberOfWatch.setText(""+item.getWatchers_count());

        if (item.getOpen_issues() == null || item.getOpen_issues() == 0) NumberOfIssues.setText("0");
        else NumberOfIssues.setText(""+item.getOpen_issues());

        if (item.getLanguage() == null) Language.setText("No language");
        else Language.setText(item.getLanguage());

        if (item.getCreated_at() == null) CreatedAt.setText("No data found");
        else CreatedAt.setText(item.getCreated_at());

        if (item.getUpdated_at() == null) UpdatedAt.setText("No data found");
        else UpdatedAt.setText(item.getUpdated_at());


        //Picasso.get().load(item.getAvatar_url()).into(UserIcon);
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
    }
    //endregion
}
