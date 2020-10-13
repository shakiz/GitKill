package app.com.gitlib.viewmodels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.models.repositories.Repo;
import app.com.gitlib.repositories.UserRepoDetailsRepository;

public class UserRepoDetailsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Repo>> repositories;
    private UserRepoDetailsRepository repoDetailsRepository;

    public UserRepoDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getData(Context context, String url){
        repoDetailsRepository = UserRepoDetailsRepository.getInstance();
        repositories = repoDetailsRepository.getAllRepositories(context, url);
    }

    public MutableLiveData<List<Repo>> getRepositories() {
        return repositories;
    }
}
