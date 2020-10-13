package app.com.gitlib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import app.com.gitlib.R;
import app.com.gitlib.models.repositories.Repo;

public class UserRepositoriesAdapter extends RecyclerView.Adapter<UserRepositoriesAdapter.ViewHolder> {
    private List<Repo> allRepositoryList;
    private Context context;

    public UserRepositoriesAdapter(List<Repo> allRepositoryList, Context context) {
        this.allRepositoryList = allRepositoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_layout_user_repositories,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Repo repository = allRepositoryList.get(i);
        viewHolder.name.setText(repository.getName());
        viewHolder.full_name.setText(repository.getFullName());
        viewHolder.description.setText(repository.getDescription());
        viewHolder.language.setText(repository.getLanguage());
        viewHolder.stargazers_count.setText(String.valueOf(repository.getStargazersCount()));
        viewHolder.watchers_count.setText(String.valueOf(repository.getWatchersCount()));
        viewHolder.forks_count.setText(String.valueOf(repository.getForksCount()));
    }

    @Override
    public int getItemCount() {
        return allRepositoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name, full_name ,description ,language, stargazers_count, watchers_count, forks_count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_card_view);
            name = itemView.findViewById(R.id.name);
            full_name = itemView.findViewById(R.id.full_name);
            description = itemView.findViewById(R.id.description);
            language = itemView.findViewById(R.id.language);
            stargazers_count = itemView.findViewById(R.id.stargazers_count);
            watchers_count = itemView.findViewById(R.id.watchers_count);
            forks_count = itemView.findViewById(R.id.forks_count);
        }
    }

}
