package app.com.gitlib.repositories;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.models.repositories.TrendingRepositories;
import app.com.gitlib.utils.UtilsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static app.com.gitlib.apiutils.AllUrlClass.TRENDING_REPOS_URL;

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

    public MutableLiveData<List<TrendingRepositories>> getTrendingRepos(Context context, String url){
        final MutableLiveData<List<TrendingRepositories>> trendingRepos = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(TRENDING_REPOS_URL).create(AllApiService.class);
        final Call<List<TrendingRepositories>> trendingRepositoriesCall=apiService.getTrendingRepos(url);
        trendingRepositoriesCall.enqueue(new Callback<List<TrendingRepositories>>() {
            @Override
            public void onResponse(Call<List<TrendingRepositories>> call, Response<List<TrendingRepositories>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().size() > 0){
                            trendingRepos.setValue(response.body());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TrendingRepositories>> call, Throwable t) {
                trendingRepos.setValue(null);
            }
        });
        return trendingRepos;
    }
}
