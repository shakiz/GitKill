package app.com.gitlib.viewmodels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.repositories.WebRepository;

public class WebViewModel extends AndroidViewModel {
    private MutableLiveData<List<Item>> webRepos;
    private WebRepository webRepository;

    public WebViewModel(@NonNull Application application) {
        super(application);
    }

    public void getData(Context context, String url, String queryString){
        webRepository = WebRepository.getInstance();
        webRepos = webRepository.getWebRepos(context, url, queryString);
    }

    public MutableLiveData<List<Item>> getWebRepos() {
        return webRepos;
    }
}
