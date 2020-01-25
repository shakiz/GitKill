package app.com.gitlib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import app.com.gitlib.R;
import app.com.gitlib.models.alltopic.Item;

public class AllTopicAdapter extends RecyclerView.Adapter<AllTopicAdapter.ViewHolder> {
    private onItemClickListener onItemClickListener;
    private ArrayList<Item> androidItemList;
    private int layoutRes;
    private Context context;

    public AllTopicAdapter(ArrayList<Item> androidItemList, Context context, int layoutRes, onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.androidItemList = androidItemList;
        this.context = context;
        this.layoutRes = layoutRes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layoutRes,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Item androidItem = androidItemList.get(i);
        viewHolder.FullName.setText(androidItem.getFullName());
        viewHolder.RepoLink.setText(androidItem.getHtmlUrl());
        viewHolder.Language.setText(androidItem.getLanguage());
        //viewHolder.License.setText(androidItem.getLicense().getName());
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
        return androidItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView FullName , RepoLink , Language , NumberOfStars , NumberOfWatch , NumberOfForks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_card_view);
            FullName = itemView.findViewById(R.id.FullName);
            RepoLink = itemView.findViewById(R.id.RepoLink);
            Language = itemView.findViewById(R.id.Language);
            NumberOfStars = itemView.findViewById(R.id.NumberOfStars);
            NumberOfForks = itemView.findViewById(R.id.NumberOfForks);
            NumberOfWatch = itemView.findViewById(R.id.NumberOfWatch);
        }
    }

    public interface onItemClickListener{
        void respond(Item androidItem);
    }
}
