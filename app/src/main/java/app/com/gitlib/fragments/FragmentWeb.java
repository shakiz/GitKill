package app.com.gitlib.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.activities.s.details.DetailsActivity;
import app.com.gitlib.adapters.AllTopicAdapter;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.models.alltopic.TopicBase;
import app.com.gitlib.utils.UX;
import app.com.gitlib.utils.UtilsManager;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentWeb extends Fragment {
    private static final FragmentWeb FRAGMENT_WEB = null;
    private RecyclerView webTopicRecyclerView;
    private UX ux;
    private ArrayList<Item> webTopicList;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private TextView NoData;
    private ImageView NoDataIV;
    private CircleImageView refreshListButton;
    private UtilsManager utilsManager;
    private Spinner webFilterSpinner;
    private String[] webFilterList = new String[]{"Select Query","Javascript","Typescript",
            "Bootstrap","Laravel","Django","Vue Js","Angular"};

    public FragmentWeb() {
        // Required empty public constructor
    }

    public static FragmentWeb getInstance() {
        if (FRAGMENT_WEB == null) return new FragmentWeb();
        else return FRAGMENT_WEB;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        init(view);
        bindUIWithComponents();
        return view;
    }

    private void init(View view) {
        webTopicRecyclerView = view.findViewById(R.id.mRecyclerView);
        refreshListButton = view.findViewById(R.id.RefreshList);
        NoData = view.findViewById(R.id.NoDataMessage);
        NoDataIV = view.findViewById(R.id.NoDataIV);
        webFilterSpinner     = view.findViewById(R.id.FilterSpinner);
        webTopicList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        ux = new UX(getContext());
        utilsManager = new UtilsManager(getContext());
    }

    private void bindUIWithComponents() {
        ux.setSpinnerAdapter(webFilterSpinner,webFilterList);

        new BackgroundDataLoad(allUrlClass.ALL_TOPICS_BASE_URL,"web").execute();

        refreshListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundDataLoad(allUrlClass.ALL_TOPICS_BASE_URL,"web").execute();
            }
        });

        webFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String queryString = adapterView.getItemAtPosition(position).toString();
                new BackgroundDataLoad(allUrlClass.ALL_TOPICS_BASE_URL, "web"+queryString ).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadListView(){
        ux.loadListView(webTopicList, webTopicRecyclerView, R.layout.adapter_layout_android_topics).setOnItemClickListener(new AllTopicAdapter.onItemClickListener() {
            @Override
            public void respond(Item androidItem) {
                Intent intent = new Intent(getContext() , DetailsActivity.class);
                intent.putExtra("item", androidItem);
                getContext().startActivity(intent);
            }
        });
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {


        String url , querySting;

        public BackgroundDataLoad(String url, String querySting) {
            this.url = url;
            this.querySting = querySting;
        }

        @Override
        protected void onPreExecute() {
            ux.getLoadingView();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadRecord(url , querySting);
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("done")){
                Log.v("result async task :: ",result);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (webTopicList.size()>0){
                            loadListView();
                            NoData.setVisibility(View.GONE);
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


    private void loadRecord(String url , String queryString) {
        Log.v("URL",url);
        webTopicList.clear();
        //Creating the instance for api service from AllApiService interface
        apiService=utilsManager.getClient(url).create(AllApiService.class);
        final Call<TopicBase> androidTopicCall=apiService.getAllTopics(url+"repositories",queryString);
        //handling user requests and their interactions with the application.
        androidTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                try{
                    for (int start=0;start<response.body().getItems().size();start++) {
                        Item item=response.body().getItems().get(start);
                        //License license = item.getLicense();
                        webTopicList.add(new Item(item.getFullName(),item.getAvatar_url(),item.getHtmlUrl(),item.getLanguage(),item.getStargazersCount(),item.getWatchersCount(),
                                item.getForksCount(),item.getForks(),item.getWatchers()));
                    }
                }
                catch (Exception e){
                    Log.v("EXCEPTION : ",""+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
                Log.v("Web Fragment",""+t.getMessage());
            }
        });

    }

}
