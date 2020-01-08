package app.com.gitkill.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import app.com.gitkill.R;
import app.com.gitkill.models.users.TrendingDevelopers;

public class TrendingDevelopersAdapter extends RecyclerView.Adapter<TrendingDevelopersAdapter.ViewHolder>{

    private ArrayList<TrendingDevelopers> trendingDevelopersList;
    private Context context;
    private onItemClickListener onItemClickListener;

    public TrendingDevelopersAdapter(ArrayList<TrendingDevelopers> trendingDevelopersList, Context context, onItemClickListener onItemClickListener) {
        this.trendingDevelopersList = trendingDevelopersList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_layout_trending_developers,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final TrendingDevelopers trendingDevelopers = trendingDevelopersList.get(position);
        viewHolder.UserName.setText(trendingDevelopers.getUsername());
        viewHolder.Type.setText(trendingDevelopers.getName());
        viewHolder.ProfileLink.setText(trendingDevelopers.getUrl());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.respond(trendingDevelopers);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trendingDevelopersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView UserName, Type,ProfileLink;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.UserName);
            Type = itemView.findViewById(R.id.Type);
            ProfileLink = itemView.findViewById(R.id.ProfileLink);
            cardView = itemView.findViewById(R.id.item_card_view);
        }
    }

    public interface onItemClickListener {
        void respond(TrendingDevelopers trendingDevelopers);
    }
}
