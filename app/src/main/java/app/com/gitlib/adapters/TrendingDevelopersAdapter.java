package app.com.gitlib.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.models.users.TrendingDevelopers;

public class TrendingDevelopersAdapter extends RecyclerView.Adapter<TrendingDevelopersAdapter.ViewHolder>{

    private ArrayList<TrendingDevelopers> trendingDevelopersList;
    private Context context;
    private onItemClickListener onItemClickListener;

    public TrendingDevelopersAdapter(ArrayList<TrendingDevelopers> trendingDevelopersList, Context context) {
        this.trendingDevelopersList = trendingDevelopersList;
        this.context = context;
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
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
        viewHolder.UserName.setText("@"+trendingDevelopers.getUsername());
        if (!TextUtils.isEmpty(trendingDevelopers.getType())) {
            viewHolder.Type.setText(trendingDevelopers.getType());
        } else {
            viewHolder.Type.setText("No user type found");
        }
        viewHolder.ProfileLink.setText(trendingDevelopers.getUrl());
        if (!TextUtils.isEmpty(trendingDevelopers.getSponsorUrl())) {
            viewHolder.SponsorUrl.setText(trendingDevelopers.getSponsorUrl());
        } else {
            viewHolder.Type.setText("No sponsor url found");
        }
        viewHolder.Name.setText(trendingDevelopers.getName());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.respond(trendingDevelopers);
            }
        });

        if (!TextUtils.isEmpty(trendingDevelopers.getUrl())){
            Glide.with(context).load(trendingDevelopers.getUrl()).placeholder(R.drawable.ic_programmer).dontAnimate().into(viewHolder.userAvatar);
        }
        else{
            viewHolder.userAvatar.setImageResource(R.drawable.ic_programmer);
        }
    }

    @Override
    public int getItemCount() {
        return trendingDevelopersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView UserName, Type, ProfileLink, Name, SponsorUrl;
        CardView cardView;
        ImageView userAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.UserName);
            Type = itemView.findViewById(R.id.Type);
            ProfileLink = itemView.findViewById(R.id.ProfileLink);
            SponsorUrl = itemView.findViewById(R.id.SponsorUrl);
            cardView = itemView.findViewById(R.id.item_card_view);
            Name = itemView.findViewById(R.id.Name);
            userAvatar = itemView.findViewById(R.id.userAvatar);
        }
    }

    public interface onItemClickListener {
        void respond(TrendingDevelopers trendingDevelopers);
    }
}
