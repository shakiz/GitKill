package app.com.gitlib.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.adapters.TrendingRepositoriesAdapter;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.repositories.TrendingRepositories;
import app.com.gitlib.utils.UX;
import app.com.gitlib.utils.UtilsManager;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTrendingRepositories extends Fragment {
    private static final FragmentTrendingRepositories FRAGMENT_TRENDING_REPOSITORIES = null;
    private Spinner languageSpinner,sinceSpinner;
    private ArrayList<String> languageList, timeList;
    private RecyclerView recyclerViewRepo;
    private TrendingRepositoriesAdapter trendingRepositoriesAdapter;
    private ArrayList<TrendingRepositories> trendingRepoList;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private String TAG = "FragmentTrendingRepositories" , languageStr = "" , sinceStr = "";
    private FloatingActionButton search;
    private CircleImageView refreshListButton;
    private Dialog itemDialog ;
    private RelativeLayout dialogLayout;
    private UX ux;
    private UtilsManager utilsManager;
    private TextView NoData;
    private ImageView NoDataIV;
    //Dialog components
    private TextView RepoName , RepoLink , UserName , Language , NumberOfStars , NumberOfForks , Description;

    public static synchronized FragmentTrendingRepositories getInstance(){
        if (FRAGMENT_TRENDING_REPOSITORIES == null) return new FragmentTrendingRepositories();
        else return FRAGMENT_TRENDING_REPOSITORIES;
    }


    public FragmentTrendingRepositories() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_repositories, container, false);
        init(view);
        bindUiWithComponents();
        return view;
    }

    private void init(View view) {
        recyclerViewRepo = view.findViewById(R.id.mRecyclerView);
        languageSpinner = view.findViewById(R.id.LanguageSpinner);
        sinceSpinner = view.findViewById(R.id.SinceSpinner);
        refreshListButton = view.findViewById(R.id.RefreshList);
        NoData = view.findViewById(R.id.NoDataMessage);
        NoDataIV = view.findViewById(R.id.NoDataIV);
        search = view.findViewById(R.id.Search);
        allUrlClass = new AllUrlClass();
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
        ux = new UX(getContext());
        utilsManager = new UtilsManager(getContext());
        trendingRepoList = new ArrayList<>();
    }

    private void bindUiWithComponents() {
        setData();
        new BackgroundDataLoad(allUrlClass.TRENDING_REPOS_URL).execute();
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
                String newUrl = allUrlClass.BASE_URL+"repositories?"+"language="+languageStr+"&since="+sinceStr;
                Log.v("SpinnerURL",newUrl);
                new BackgroundDataLoad(newUrl).execute();
            }
        });

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(allUrlClass.TRENDING_REPOS_URL).execute();
            }
        });
    }

    private void setData() {
        //Adding the language list
        languageList.add("Select Language");
        languageList.add("Java");
        languageList.add("Python");
        languageList.add("C");
        languageList.add("C++");
        languageList.add("C#");
        languageList.add("PHP");
        //Adding data to time list
        timeList.add("Select");
        timeList.add("Daily");
        timeList.add("Weekly");
        timeList.add("Monthly");
        timeList.add("Yearly");
    }

    private void loadListView(){
        trendingRepositoriesAdapter = new TrendingRepositoriesAdapter(trendingRepoList, getContext(), new TrendingRepositoriesAdapter.onItemClickListener() {
            @Override
            public void respond(TrendingRepositories trendingRepositories) {
                showDialog(trendingRepositories);
            }
        });
        recyclerViewRepo.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRepo.setAdapter(trendingRepositoriesAdapter);
        trendingRepositoriesAdapter.notifyDataSetChanged();
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {

        String url;

        public BackgroundDataLoad( String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            ux.getLoadingView();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadRecord(url);
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("done")){
                Log.v("result async task :: ",result);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (trendingRepoList.size()>0){
                            loadListView();NoData.setVisibility(View.GONE);
                            NoDataIV.setVisibility(View.GONE);
                        }
                        else {
                            NoData.setVisibility(View.VISIBLE);
                            NoDataIV.setVisibility(View.VISIBLE);
                            Toasty.error(getContext(),R.string.no_data_message).show();
                        }
                        ux.removeLoadingView();
                    }
                }, 6000);
            }
        }

    }

    private void loadRecord(String url) {
        trendingRepoList.clear();
        //Creating the instance for api service from AllApiService interface
        apiService=utilsManager.getClient(url).create(AllApiService.class);
        final Call<ArrayList<TrendingRepositories>> userInformationCall=apiService.getTrendingRepos(url);
        //handling user requests and their interactions with the application.
        userInformationCall.enqueue(new Callback<ArrayList<TrendingRepositories>>() {
            @Override
            public void onResponse(Call<ArrayList<TrendingRepositories>> call, Response<ArrayList<TrendingRepositories>> response) {
                try{
                    for(int start=0;start<response.body().size();start++){
                        TrendingRepositories repoPojo=response.body().get(start);
                        trendingRepoList.add(new TrendingRepositories(repoPojo.getAuthor(),repoPojo.getName(),repoPojo.getLanguage(),repoPojo.getStars(),repoPojo.getForks(),repoPojo.getUrl()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<TrendingRepositories>> call, Throwable t) {
                Log.v(TAG,""+t.getMessage());
            }
        });
    }

    private void showDialog(TrendingRepositories trendingRepositories) {
        itemDialog = new Dialog(getContext());
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

}
