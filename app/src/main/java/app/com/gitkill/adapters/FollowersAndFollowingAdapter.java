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
import app.com.gitkill.models.details.FollowersAndFollowing;

public class FollowersAndFollowingAdapter extends RecyclerView.Adapter<FollowersAndFollowingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FollowersAndFollowing> followersAndFollowingList;
    private onItemClickListener onItemClickListener;

    public FollowersAndFollowingAdapter(Context context, ArrayList<FollowersAndFollowing> followersAndFollowingList, onItemClickListener onItemClickListener) {
        this.context = context;
        this.followersAndFollowingList = followersAndFollowingList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_layout_followers_and_following,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        final FollowersAndFollowing followersAndFollowing = followersAndFollowingList.get(pos);
        viewHolder.UserName.setText(followersAndFollowing.getLogin());
        viewHolder.ProfileLink.setText(followersAndFollowing.getHtmlUrl());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.respond(followersAndFollowing);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followersAndFollowingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView UserName,ProfileLink;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.UserName);
            ProfileLink = itemView.findViewById(R.id.ProfileLink);
            cardView = itemView.findViewById(R.id.item_card_view);
        }
    }

    public interface onItemClickListener {
        void respond(FollowersAndFollowing followersAndFollowing);
    }
}
