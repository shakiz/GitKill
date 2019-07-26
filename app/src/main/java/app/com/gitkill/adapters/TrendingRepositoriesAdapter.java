package app.com.gitkill.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.gitkill.R;
import app.com.gitkill.pojoclasses.repositories.TrendingRepoPojo;

public class TrendingRepositoriesAdapter extends RecyclerView.Adapter<TrendingRepositoriesAdapter.ViewHolder>{
    private ArrayList<TrendingRepoPojo> repositoriesArrayList;
    private String TAG = "TrendingRepositoriesAdapter";
    private Context context;

    public TrendingRepositoriesAdapter(ArrayList<TrendingRepoPojo> repositoriesArrayList, Context context) {
        this.repositoriesArrayList = repositoriesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_layout_trending_repositories,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TrendingRepoPojo trendingRepo=repositoriesArrayList.get(position);
        Log.v(TAG,trendingRepo.getAuthor());
        viewHolder.AuthorName.setText("Name : "+trendingRepo.getAuthor());
        viewHolder.RepoName.setText("Repo : "+trendingRepo.getName());
        viewHolder.Language.setText("Language : "+trendingRepo.getLanguage());
        viewHolder.NumberOfStars.setText("Stars : "+trendingRepo.getStars());
        viewHolder.NumberOfForks.setText("Forks : "+trendingRepo.getForks());
        viewHolder.RepoLink.setText("Repo link : "+trendingRepo.getUrl());
    }

    @Override
    public int getItemCount() {
        return repositoriesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView AuthorName,RepoName,Language,NumberOfStars,NumberOfForks,RepoLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AuthorName = itemView.findViewById(R.id.AuthorName);
            RepoName = itemView.findViewById(R.id.RepoName);
            Language = itemView.findViewById(R.id.Language);
            NumberOfStars = itemView.findViewById(R.id.NumberOfStars);
            NumberOfForks = itemView.findViewById(R.id.NumberOfForks);
            RepoLink = itemView.findViewById(R.id.RepoLink);
        }
    }
}
