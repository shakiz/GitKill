package app.com.gitlib.activities.onboard;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import app.com.gitlib.R;
import app.com.gitlib.activities.android.AndroidActivity;
import app.com.gitlib.activities.developers.TrendingDevelopersActivity;
import app.com.gitlib.activities.ml.MachineLearningActivity;
import app.com.gitlib.activities.repositories.TrendingRepositoriesActivity;
import app.com.gitlib.activities.web.WebActivity;
import app.com.gitlib.adapters.QuestionBankAndResultAdapter;
import app.com.gitlib.adapters.drawerextra.DrawerAdapter;
import app.com.gitlib.adapters.drawerextra.DrawerItem;
import app.com.gitlib.adapters.drawerextra.SimpleItem;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.models.alltopic.TopicBase;
import app.com.gitlib.models.questionbank.QuestionBank;
import app.com.gitlib.models.questionbank.Result;
import app.com.gitlib.utils.UX;
import app.com.gitlib.utils.UtilsManager;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.com.gitlib.apiutils.AllUrlClass.QUESTION_BANK;

public class HomeActivity extends AppCompatActivity {
    private static String TAG = "Shakil::HomeActivity";
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
    private TextView greetingsText, dateTimeText;
    private UX ux;
    private AllApiService apiService;
    private UtilsManager utilsManager;
    private ImageView drawerButton;
    private RecyclerView recyclerView;
    private ArrayList<Result> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);

        //region init and bind UI components
        init(savedInstanceState);
        bindUIWithComponents();
        //endregion
    }

    //region init
    private void init(Bundle savedInstanceState) {
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(null)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.layout_menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        drawerButton = findViewById(R.id.DrawerButton);
        greetingsText = findViewById(R.id.GreetingsText);
        dateTimeText = findViewById(R.id.DateTimeText);
        recyclerView = findViewById(R.id.mRecyclerView);
        resultList = new ArrayList<>();
        ux = new UX(this);
        utilsManager = new UtilsManager(this);
    }
    //endregion

    //region set drawer adapter
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
    //endregion

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withTextTint(getResources().getColor(R.color.md_blue_grey_700))
                .withSelectedTextTint(getResources().getColor(R.color.md_grey_900));
    }

    //region bind UI components
    private void bindUIWithComponents() {
        //region load question bank data on async task
        new BackgroundDataLoad().execute();
        //endregion

        //region set greetings, name, nav drawer texts and dateTime text
        setGreetings();
        dateTimeText.setText(getDateTimeText());
        //endregion

        //region set question bank and result adapter
        setQuestionBankAdapter();
        //endregion

        //region drawer hum-burger icon
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slidingRootNav.isMenuClosed()) {
                    slidingRootNav.openMenu();
                    drawerButton.setImageResource(R.drawable.ic_left_arrow);
                }
                else {
                    slidingRootNav.closeMenu();
                    drawerButton.setImageResource(R.drawable.ic_menu_new);
                }
            }
        });
        //endregion
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
    //endregion

    //region set question bank and result adapter
    private void setQuestionBankAdapter(){
        QuestionBankAndResultAdapter resultAdapter = new QuestionBankAndResultAdapter(resultList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(resultAdapter);
        resultAdapter.notifyDataSetChanged();
    }
    //endregion

    //region set greetings
    private void setGreetings() {
        Calendar calendar = Calendar.getInstance();
        int timeOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfTheDay >= 0 && timeOfTheDay < 12) {
            greetingsText.setText("Good Morning");
        } else if (timeOfTheDay >= 12 && timeOfTheDay < 16) {
            greetingsText.setText("Good Noon");
        } else if (timeOfTheDay >= 16 && timeOfTheDay < 18) {
            greetingsText.setText("Good Afternoon");
        } else if (timeOfTheDay >= 18 && timeOfTheDay < 20) {
            greetingsText.setText("Good Evening");
        } else {
            greetingsText.setText("Good Night");
        }
    }
    //endregion

    //region get dateTime text
    private String getDateTimeText() {
        DateFormat df = new SimpleDateFormat("MMM d, yyyy || EEEE");
        String dateTimeText = df.format(new Date());
        return dateTimeText;
    }
    //endregion

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

    //region asyncTask for calling server data
    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            ux.getLoadingView();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadRecord();
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("done")){
                Log.v("result async task :: ",result);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (resultList.size()>0){
                            setQuestionBankAdapter();
                            //NoData.setVisibility(View.GONE);
                            ///NoDataIV.setVisibility(View.GONE);
                        }
                        else {
                            //NoData.setVisibility(View.VISIBLE);
                            //NoDataIV.setVisibility(View.VISIBLE);
                            Toasty.error(HomeActivity.this,R.string.no_data_message).show();
                        }
                        ux.removeLoadingView();
                    }
                }, 7000);
            }
        }

    }
    //endregion

    //region load data from server
    private void loadRecord() {
        resultList.clear();
        //Creating the instance for api service from AllApiService interface
        apiService=utilsManager.getClient(QUESTION_BANK).create(AllApiService.class);
        final Call<QuestionBank> androidTopicCall=apiService.getAllQuestionAndAnswer(QUESTION_BANK+"api.php?amount=10&type=multiple");
        //handling user requests and their interactions with the application.
        androidTopicCall.enqueue(new Callback<QuestionBank>() {
            @Override
            public void onResponse(Call<QuestionBank> call, Response<QuestionBank> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().getResults() != null){
                            if (response.body().getResults().size() != 0){
                                Log.v(TAG, "onResponse: Response : "+response.isSuccessful());
                                for (int start = 0; start < response.body().getResults().size(); start++) {
                                    Log.v(TAG, "onResponse: Response : Question "+start+" = "+response.body().getResults().get(start).getQuestion());
                                    resultList.add(new Result(response.body().getResults().get(start).getQuestion(),response.body().getResults().get(start).getCorrectAnswer()));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QuestionBank> call, Throwable t) {
                Log.i(TAG, "onFailure: QuestionAnswer load failed");
            }
        });

    }
    //endregion

    //region activity components
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
    //endregion
}
