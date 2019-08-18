package app.com.gitkill.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.gitkill.R;
import app.com.gitkill.pojoclasses.users.TrendingUserPojo;

public class TrendingDevelopersAdapter extends RecyclerView.Adapter<TrendingDevelopersAdapter.VIewHolder>{

    private ArrayList<TrendingUserPojo> trendingUserList;
    private Context context;

    public TrendingDevelopersAdapter(ArrayList<TrendingUserPojo> trendingUserList, Context context) {
        this.trendingUserList = trendingUserList;
        this.context = context;
    }

    @NonNull
    @Override
    public VIewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_layout_trending_developers,viewGroup,false);
        return new VIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VIewHolder vIewHolder, int position) {
        vIewHolder.userName.setText(trendingUserList.get(position).getUsername());
        vIewHolder.name.setText(trendingUserList.get(position).getName());
        vIewHolder.profileLink.setText(trendingUserList.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return trendingUserList.size();
    }

    public class VIewHolder extends RecyclerView.ViewHolder{

        TextView userName,name,profileLink;
        public VIewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.Username);
            name = itemView.findViewById(R.id.Name);
            profileLink = itemView.findViewById(R.id.ProfileLink);
        }
    }
}
