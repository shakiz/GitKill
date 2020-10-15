package app.com.gitlib.repositories;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.models.alltopic.Item;
import app.com.gitlib.models.alltopic.TopicBase;
import app.com.gitlib.utils.UtilsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static app.com.gitlib.apiutils.AllUrlClass.ALL_TOPICS_BASE_URL;

public class RepoTrendingRepository {
    private AllApiService apiService;
    private UtilsManager utilsManager;
    //region singleton instance
    private static RepoTrendingRepository instance = null;

    public static RepoTrendingRepository getInstance(){
        if (instance == null){
            instance = new RepoTrendingRepository();
        }
        return instance;
    }
    //endregion

    public MutableLiveData<List<Item>> getTrendingRepos(Context context, String url, String queryString){
        final MutableLiveData<List<Item>> trendingRepos = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(ALL_TOPICS_BASE_URL).create(AllApiService.class);
        final Call<TopicBase> trendingRepositoriesCall=apiService.getAllTopics(url+"repositories",queryString);
        trendingRepositoriesCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                if (response.isSuccessful()){
                    if (response.body().getItems() != null){
                        if (response.body().getItems().size() > 0){
                            trendingRepos.setValue(response.body().getItems());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
                trendingRepos.setValue(null);
            }
        });
        return trendingRepos;
    }
}
