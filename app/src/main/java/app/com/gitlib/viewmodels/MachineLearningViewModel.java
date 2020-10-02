package app.com.gitlib.viewmodels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.repositories.MachineLearningRepository;

public class MachineLearningViewModel extends AndroidRepoViewModel {
    private MutableLiveData<List<Item>> mlRepos;
    private MachineLearningRepository machineLearningRepository;

    public MachineLearningViewModel(@NonNull Application application) {
        super(application);
    }

    public void getData(Context context, String url, String queryString){
        machineLearningRepository = MachineLearningRepository.getInstance();
        mlRepos = machineLearningRepository.getMLRepos(context, url, queryString);
    }

    public MutableLiveData<List<Item>> getMLRepos() {
        return mlRepos;
    }
}
