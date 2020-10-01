package app.com.gitlib.viewmodels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.models.repositories.TrendingRepositories;
import app.com.gitlib.repositories.RepoTrendingRepository;

public class TrendingRepositoriesViewModel extends AndroidViewModel {
    private MutableLiveData<List<TrendingRepositories>> trendingRepos;
    private RepoTrendingRepository trendingRepository;

    public TrendingRepositoriesViewModel(@NonNull Application application) {
        super(application);
    }

    public void getData(Context context, String url){
        trendingRepository = RepoTrendingRepository.getInstance();
        trendingRepos = trendingRepository.getTrendingRepos(context, url);
    }

    public MutableLiveData<List<TrendingRepositories>> getAndroidRepos() {
        return trendingRepos;
    }
}
