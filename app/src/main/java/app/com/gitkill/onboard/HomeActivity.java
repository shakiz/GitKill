package app.com.gitkill.onboard;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import java.util.Arrays;
import app.com.gitkill.R;
import app.com.gitkill.drawerextra.DrawerAdapter;
import app.com.gitkill.drawerextra.DrawerItem;
import app.com.gitkill.drawerextra.SimpleItem;
import app.com.gitkill.fragments.FragmentAndroid;
import app.com.gitkill.fragments.FragmentTrendingDevelopers;
import app.com.gitkill.fragments.FragmentTrendingRepositories;

public class HomeActivity extends AppCompatActivity {
    private SlidingRootNav slidingRootNav ;
    private DrawerAdapter adapter;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private RecyclerView list;
    private static final int POS_TRENDING_REPO = 0;
    private static final int POS_TRENDING_DEVELOPERS = 1;
    //private static final int POS_TRENDING_LANGUAGES = 2;
    private static final int POS_TRENDING_ON_ANDROID= 2;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                createItemFor(POS_TRENDING_ON_ANDROID)));
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_TRENDING_REPO);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,R.anim.fadeout);
        fragmentTransaction.replace(R.id.container, fragment)
                .commit();
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
                            showFragment(FragmentTrendingRepositories.getInstance());
                            return;
                        }
                        else if (pos == POS_TRENDING_DEVELOPERS){
                            showFragment(FragmentTrendingDevelopers.getInstance());
                            return;
                        }
                        else if (pos == POS_TRENDING_ON_ANDROID){
                            showFragment(FragmentAndroid.getInstance());
                            return;
                        }
                    }
                },5);

                slidingRootNav.closeMenu();

            }
        });
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new FragmentTrendingRepositories());
        fragmentTransaction.commit();
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
        super.onBackPressed();
        exitApp();
    }

}
