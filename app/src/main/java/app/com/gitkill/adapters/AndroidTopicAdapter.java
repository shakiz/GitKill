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
import app.com.gitkill.models.androidtopic.AndroidTopic;
import app.com.gitkill.models.androidtopic.Item;

public class AndroidTopicAdapter extends RecyclerView.Adapter<AndroidTopicAdapter.ViewHolder> {
    private onItemClickListener onItemClickListener;
    private ArrayList<AndroidTopic> androidTopicList;
    private Context context;

    public AndroidTopicAdapter(ArrayList<AndroidTopic> androidTopicList, Context context, onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.androidTopicList = androidTopicList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_layout_android_topics,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Item androidItem = androidTopicList.get(i).getItems().get(i);
        viewHolder.FullName.setText(androidItem.getFullName());
        viewHolder.RepoLink.setText(androidItem.getHtmlUrl());
        viewHolder.License.setText(androidItem.getLicense().getName());
        viewHolder.NumberOfStars.setText(""+androidItem.getStargazersCount());
        viewHolder.NumberOfForks.setText(""+androidItem.getForksCount());
        viewHolder.NumberOfWatch.setText(""+androidItem.getWatchersCount());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.respond(androidItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView FullName , RepoLink , License , NumberOfStars , NumberOfWatch , NumberOfForks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_card_view);
            FullName = itemView.findViewById(R.id.FullName);
            RepoLink = itemView.findViewById(R.id.RepoLink);
            License = itemView.findViewById(R.id.License);
            NumberOfStars = itemView.findViewById(R.id.NumberOfStars);
            NumberOfForks = itemView.findViewById(R.id.NumberOfForks);
            NumberOfWatch = itemView.findViewById(R.id.NumberOfWatch);
        }
    }

    public interface onItemClickListener{
        void respond(Item androidItem);
    }
}
