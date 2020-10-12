package app.com.gitlib.viewmodels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.models.users.TrendingDevelopers;
import app.com.gitlib.repositories.TrendingDevelopersRepository;

public class TrendingDevelopersViewModel extends AndroidViewModel {
    private MutableLiveData<List<TrendingDevelopers>> developersList;
    private TrendingDevelopersRepository developersRepository;

    public TrendingDevelopersViewModel(@NonNull Application application) {
        super(application);
    }

    public void getData(Context context, String url){
        developersRepository = TrendingDevelopersRepository.getInstance();
        developersList = developersRepository.getTrendingRepos(context, url);
    }

    public MutableLiveData<List<TrendingDevelopers>> getDevelopersList() {
        return developersList;
    }
}
