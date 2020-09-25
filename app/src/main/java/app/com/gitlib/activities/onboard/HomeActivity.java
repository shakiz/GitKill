package app.com.gitlib.activities.onboard;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import java.util.Arrays;
import app.com.gitlib.R;
import app.com.gitlib.activities.android.AndroidActivity;
import app.com.gitlib.activities.developers.TrendingDevelopersActivity;
import app.com.gitlib.activities.ml.MachineLearningActivity;
import app.com.gitlib.activities.repositories.TrendingRepositoriesActivity;
import app.com.gitlib.activities.web.WebActivity;
import app.com.gitlib.adapters.drawerextra.DrawerAdapter;
import app.com.gitlib.adapters.drawerextra.DrawerItem;
import app.com.gitlib.adapters.drawerextra.SimpleItem;

public class HomeActivity extends AppCompatActivity {
    private SlidingRootNav slidingRootNav ;
    private DrawerAdapter adapter;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private RecyclerView list;
    private static final int POS_TRENDING_REPO = 0;
    private static final int POS_TRENDING_DEVELOPERS = 1;
    private static final int POS_TRENDING_ON_ANDROID = 2;
    private static final int POS_TRENDING_ON_WEB = 3;
    private static final int POS_TRENDING_ML_LIBRARIES = 4;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        init(savedInstanceState);
        bindUIWithComponents();
    }

    private void init(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.tool_bar);
        slidingRootNav  = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
    }

    private void setAdapter() {
        list = findViewById(R.id.nav_list_item);
        adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_TRENDING_REPO).setChecked(true),
                createItemFor(POS_TRENDING_DEVELOPERS),
                createItemFor(POS_TRENDING_ON_ANDROID),
                createItemFor(POS_TRENDING_ON_WEB),
                createItemFor(POS_TRENDING_ML_LIBRARIES)));
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_TRENDING_REPO);
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withTextTint(getResources().getColor(R.color.md_blue_grey_700))
                .withSelectedTextTint(getResources().getColor(R.color.md_grey_900));
    }

    private void bindUIWithComponents() {
        setAdapter();
        adapter.setListener(new DrawerAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                final int pos = position;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pos == POS_TRENDING_REPO) {
                            startActivity(new Intent(HomeActivity.this, TrendingRepositoriesActivity.class));
                            return;
                        }
                        else if (pos == POS_TRENDING_DEVELOPERS){
                            startActivity(new Intent(HomeActivity.this, TrendingDevelopersActivity.class));
                            return;
                        }
                        else if (pos == POS_TRENDING_ON_ANDROID){
                            startActivity(new Intent(HomeActivity.this, AndroidActivity.class));
                            return;
                        }
                        else if (pos == POS_TRENDING_ON_WEB){
                            startActivity(new Intent(HomeActivity.this, WebActivity.class));
                            return;
                        }
                        else if (pos == POS_TRENDING_ML_LIBRARIES){
                            startActivity(new Intent(HomeActivity.this, MachineLearningActivity.class));
                            return;
                        }
                    }
                },5);

                slidingRootNav.closeMenu();

            }
        });
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    public void exitApp(){
        Intent exitIntent = new Intent(Intent.ACTION_MAIN);
        exitIntent.addCategory(Intent.CATEGORY_HOME);
        exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(exitIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            Log.i("MainActivity", "Popping back stack fragments");
            getSupportFragmentManager().popBackStack();
        }
        else {
            Log.i("MainActivity", "Nothing on back stack, exiting app");
            exitApp();
        }
    }
}
