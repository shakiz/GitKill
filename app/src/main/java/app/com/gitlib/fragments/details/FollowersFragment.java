package app.com.gitlib.fragments.details;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.com.gitlib.R;
import app.com.gitlib.adapters.FollowersAndFollowingAdapter;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.apiutils.AllUrlClass;
import app.com.gitlib.models.details.FollowersAndFollowing;
import app.com.gitlib.utils.UtilsManager;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersFragment extends Fragment {
    private static final FollowersFragment FOLLOWERS_FRAGMENT = null;
    private static String UserName = "";
    private ArrayList<FollowersAndFollowing> followersList;
    private RecyclerView followersRecyclerView;
    private AllUrlClass allUrlClass;
    private AllApiService apiService;
    private UtilsManager utilsManager;
    private TextView NoData;
    private ImageView NoDataIV;
    private ProgressBar progressBar;

    public FollowersFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(String userName) {
        setData(userName);
        if (FOLLOWERS_FRAGMENT == null) return new FollowersFragment();
        else return FOLLOWERS_FRAGMENT;
    }

    public static void setData(String userName) {
        UserName = userName.split("/")[0];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followers, container, false);init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void init(View view) {
        progressBar = view.findViewById(R.id.progress);
        NoData = view.findViewById(R.id.NoDataMessage);
        followersRecyclerView = view.findViewById(R.id.mRecyclerView);
        NoDataIV = view.findViewById(R.id.NoDataIV);
        followersList = new ArrayList<>();
        allUrlClass = new AllUrlClass();
        utilsManager = new UtilsManager(getContext());
    }

    private void bindUIWithComponents(View view) {
        new BackgroundDataLoad(allUrlClass.FOLLOWERS_AND_FOLLOWING).execute();
    }

    private void loadListView(){
        FollowersAndFollowingAdapter followersAdapter = new FollowersAndFollowingAdapter(getContext(), followersList,new FollowersAndFollowingAdapter.onItemClickListener() {
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

        public BackgroundDataLoad(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
                        if (followersList.size()>0){
                            loadListView();
                            NoData.setVisibility(View.GONE);
                            NoDataIV.setVisibility(View.GONE);
                        }
                        else {
                            NoData.setVisibility(View.VISIBLE);
                            NoDataIV.setVisibility(View.VISIBLE);
                            Toasty.error(getContext(),R.string.no_followers_found).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, 3000);
            }
        }

    }

    private void loadRecord(String url) {
        followersList.clear();
        //Creating the instance for api service from AllApiService interface
        apiService=utilsManager.getClient(url).create(AllApiService.class);
        final Call<ArrayList<FollowersAndFollowing>> followersAndFollowingCall=apiService.getFollowersAndFollowing(url+UserName+"/followers");
        //handling user requests and their interactions with the application.
        followersAndFollowingCall.enqueue(new Callback<ArrayList<FollowersAndFollowing>>() {
            @Override
            public void onResponse(Call<ArrayList<FollowersAndFollowing>> call, Response<ArrayList<FollowersAndFollowing>> response) {
                try{
                    for (int start=0;start<response.body().size();start++) {
                        FollowersAndFollowing followers = response.body().get(start);
                        followersList.add(followers);
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
