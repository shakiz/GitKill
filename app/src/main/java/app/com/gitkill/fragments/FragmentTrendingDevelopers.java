package app.com.gitkill.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import app.com.gitkill.R;
import app.com.gitkill.adapters.TrendingDevelopersAdapter;
import app.com.gitkill.apiutils.AllApiService;
import app.com.gitkill.apiutils.AllUrlClass;
import app.com.gitkill.models.users.TrendingDevelopers;
import app.com.gitkill.utils.UX;
import app.com.gitkill.utils.UtilsManager;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentTrendingDevelopers extends Fragment {
    private static final FragmentTrendingDevelopers FRAGMENT_TRENDING_DEVELOPERS = null;
    private ArrayAdapter<String> arrayAdapter;
    private Spinner languageSpinner,sinceSpinner;
    private RecyclerView recyclerViewDevelopers;
    private ArrayList<String> languageList, timeList;
    private ArrayList<TrendingDevelopers> trendingDevelopersList;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private String TAG = "FragmentTrendingRepositories" , languageStr = "" , sinceStr = "";
    private OkHttpClient.Builder builder;
    private UX ux;
    private UtilsManager utilsManager;
    private FloatingActionButton search;
    private CircleImageView refreshListButton;
    private Dialog itemDialog;
    private RelativeLayout dialogLayout;
    private TextView NoData;
    private ImageView NoDataIV;
    //Dialog components
    private TextView  UserName , RepoName , ProfileLink , RepoLink , Description;
    private LinearLayout linearLayout;

    public static synchronized FragmentTrendingDevelopers getInstance(){
        if (FRAGMENT_TRENDING_DEVELOPERS == null) return new FragmentTrendingDevelopers();
        else return FRAGMENT_TRENDING_DEVELOPERS;
    }

    public FragmentTrendingDevelopers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_developers, container, false);
        init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void bindUIWithComponents(View view) {
        setData();
        new BackgroundDataLoad(view, allUrlClass.TRENDING_DEVS_URL).execute();
        setAdapter(languageSpinner,languageList);
        setAdapter(sinceSpinner,timeList);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                languageStr = adapterView.getItemAtPosition(position).toString().toLowerCase();
                String newUrl = allUrlClass.BASE_URL+"developers?language="+languageStr;
                Log.v("SpinnerURL",newUrl);
                new BackgroundDataLoad(view,newUrl).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                sinceStr = adapterView.getItemAtPosition(position).toString().toLowerCase();
                String newUrl = allUrlClass.BASE_URL+"developers?since="+sinceStr;
                Log.v("SpinnerURL",newUrl);
                new BackgroundDataLoad(view,newUrl).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUrl = allUrlClass.BASE_URL+"developers?"+"language="+languageStr+"&since="+sinceStr;
                Log.v("SpinnerURL",newUrl);
                new BackgroundDataLoad(view,newUrl).execute();
            }
        });

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(view, allUrlClass.TRENDING_DEVS_URL).execute();
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

    private void setAdapter(Spinner spinner, ArrayList<String> languageList) {
        arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_drop,languageList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void loadListView(){
        TrendingDevelopersAdapter trendingDevelopersAdapter = new TrendingDevelopersAdapter(trendingDevelopersList, getContext(), new TrendingDevelopersAdapter.onItemClickListener() {
            @Override
            public void respond(TrendingDevelopers trendingDevelopersPojo) {
                showDialog(trendingDevelopersPojo);
            }
        });
        recyclerViewDevelopers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDevelopers.setAdapter(trendingDevelopersAdapter);
        trendingDevelopersAdapter.notifyDataSetChanged();
    }

    private void init(View view) {
        languageSpinner = view.findViewById(R.id.LanguageSpinner);
        sinceSpinner = view.findViewById(R.id.SinceSpinner);
        recyclerViewDevelopers = view.findViewById(R.id.mRecyclerView);
        refreshListButton = view.findViewById(R.id.RefreshList);
        search = view.findViewById(R.id.Search);
        trendingDevelopersList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        languageList = new ArrayList<>();
        timeList = new ArrayList<>();
        ux = new UX(getContext());
        NoData = view.findViewById(R.id.NoDataMessage);
        NoDataIV = view.findViewById(R.id.NoDataIV);
        utilsManager = new UtilsManager(getContext());
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {

        View view;
        String url;

        public BackgroundDataLoad(View view, String url) {
            this.view = view;
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
                        if (trendingDevelopersList.size()>0){
                            loadListView();
                            NoData.setVisibility(View.GONE);
                            NoDataIV.setVisibility(View.GONE);
                        }
                        else {
                            NoData.setVisibility(View.VISIBLE);
                            NoDataIV.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(),R.string.no_data_message,Toast.LENGTH_LONG).show();
                        }
                        ux.removeLoadingView();
                    }
                }, 6000);
            }
        }

    }


    private void loadRecord(String url) {
        Log.v("URL",url);
        trendingDevelopersList.clear();
        builder= new OkHttpClient.Builder();
        utilsManager.loggingInterceptorForRetrofit(builder);
        if (retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit=new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        //Creating the instance for api service from AllApiService interface
        apiService=retrofit.create(AllApiService.class);
        final Call<ArrayList<TrendingDevelopers>> userInformationCall=apiService.getTrendingUsers(url);
        //handling user requests and their interactions with the application.
        userInformationCall.enqueue(new Callback<ArrayList<TrendingDevelopers>>() {
            @Override
            public void onResponse(Call<ArrayList<TrendingDevelopers>> call, Response<ArrayList<TrendingDevelopers>> response) {
                try{
                    for(int start=0;start<response.body().size();start++){
                        TrendingDevelopers userPojo=response.body().get(start);
                        trendingDevelopersList.add(new TrendingDevelopers(userPojo.getUsername(),userPojo.getName(),userPojo.getType(),userPojo.getUrl(),userPojo.getAvatar(),userPojo.getRepo()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<TrendingDevelopers>> call, Throwable t) {
                Log.v(TAG,""+t.getMessage());
            }
        });
    }

    private void showDialog(TrendingDevelopers trendingDevelopers) {
        itemDialog = new Dialog(getContext());
        itemDialog.setContentView(R.layout.popup_trending_developers_items_details);
        customViewInit(itemDialog);
        itemDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Animation a = AnimationUtils.loadAnimation(itemDialog.getContext(), R.anim.push_up_in);
        dialogLayout.startAnimation(a);
        setCustomDialogData(trendingDevelopers);
        itemDialog.show();
    }

    private void setCustomDialogData(final TrendingDevelopers trendingDevelopers) {
        UserName.setText(trendingDevelopers.getUsername());
        ProfileLink.setText(trendingDevelopers.getUrl());

        RepoName.setText(trendingDevelopers.getName());

        if (trendingDevelopers.getRepo().getDescription() == null) Description.setText("No description available for this repository");
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

    private void customViewInit(Dialog itemDialog) {
        UserName = itemDialog.findViewById(R.id.UserName);
        RepoName = itemDialog.findViewById(R.id.RepoName);
        RepoLink = itemDialog.findViewById(R.id.RepoLink);
        ProfileLink = itemDialog.findViewById(R.id.ProfileLink);
        Description = itemDialog.findViewById(R.id.Description);
        dialogLayout = itemDialog.findViewById(R.id.dialogLayout);
        linearLayout = itemDialog.findViewById(R.id.Section2);
    }
}
