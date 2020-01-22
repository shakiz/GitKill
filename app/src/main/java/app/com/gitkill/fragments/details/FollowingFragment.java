package app.com.gitkill.fragments.details;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import app.com.gitkill.R;
import app.com.gitkill.adapters.FollowersAndFollowingAdapter;
import app.com.gitkill.apiutils.AllApiService;
import app.com.gitkill.apiutils.AllUrlClass;
import app.com.gitkill.models.details.FollowersAndFollowing;
import app.com.gitkill.utils.UX;
import app.com.gitkill.utils.UtilsManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FollowingFragment extends Fragment {

    private static final FollowersFragment FOLLOWING_FRAGMENT = null;
    private static String UserName = "";
    private ArrayList<FollowersAndFollowing> followingList;
    private RecyclerView followersRecyclerView;
    private Retrofit retrofit;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private OkHttpClient.Builder builder;
    private UX ux;
    private UtilsManager utilsManager;
    private TextView NoData;

    public FollowingFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(String userName) {
        setData(userName);
        if (FOLLOWING_FRAGMENT == null) return new FollowingFragment();
        else return FOLLOWING_FRAGMENT;
    }

    public static void setData(String userName) {
        UserName = userName.split("/")[0];
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        NoData = view.findViewById(R.id.NoDataMessage);
        followersRecyclerView = view.findViewById(R.id.RecyclerFollowingList);
        followingList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        ux = new UX(getContext());
        utilsManager = new UtilsManager(getContext());
    }

    private void bindUIWithComponents() {
        new BackgroundDataLoad(allUrlClass.FOLLOWERS_AND_FOLLOWING).execute();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) bindUIWithComponents();
    }

    private void loadListView(){
        FollowersAndFollowingAdapter followersAdapter = new FollowersAndFollowingAdapter(getContext(), followingList,new FollowersAndFollowingAdapter.onItemClickListener() {
            @Override
            public void respond(FollowersAndFollowing followersAndFollowing) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(followersAndFollowing.getHtmlUrl()));
                startActivity(browserIntent);
            }
        });
        followersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        followersRecyclerView.setAdapter(followersAdapter);
        followersAdapter.notifyDataSetChanged();
    }

    private class BackgroundDataLoad extends AsyncTask<String, Void, String> {

        String url ;

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
                        if (followingList.size()>0)loadListView();
                        else {
                            NoData.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(),R.string.no_following_found,Toast.LENGTH_LONG).show();
                        }
                        ux.removeLoadingView();
                    }
                }, 5000);
            }
        }

    }


    private void loadRecord(String url) {
        Log.v("URL",url);
        followingList.clear();
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
        final Call<ArrayList<FollowersAndFollowing>> followersAndFollowingCall=apiService.getFollowersAndFollowing(url+UserName+"/following");
        //handling user requests and their interactions with the application.
        followersAndFollowingCall.enqueue(new Callback<ArrayList<FollowersAndFollowing>>() {
            @Override
            public void onResponse(Call<ArrayList<FollowersAndFollowing>> call, Response<ArrayList<FollowersAndFollowing>> response) {
                try{
                    for (int start=0;start<response.body().size();start++) {
                        FollowersAndFollowing followers = response.body().get(start);
                        followingList.add(followers);
                    }

                }
                catch (Exception e){
                    Log.v("Error::::",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FollowersAndFollowing>> call, Throwable t) {

            }
        });

    }
}
