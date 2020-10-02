package app.com.gitlib.repositories;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.models.users.TrendingDevelopers;
import app.com.gitlib.utils.UtilsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static app.com.gitlib.apiutils.AllUrlClass.TRENDING_DEVS_URL;

public class TrendingDevelopersRepository {
    private static TrendingDevelopersRepository instance = null;
    private UtilsManager utilsManager;
    private AllApiService apiService;

    public static TrendingDevelopersRepository getInstance(){
        if (instance == null){
            instance = new TrendingDevelopersRepository();
        }
        return instance;
    }

    public MutableLiveData<List<TrendingDevelopers>> getTrendingRepos(Context context, String url){
        final MutableLiveData<List<TrendingDevelopers>> trendingDevelopers = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(TRENDING_DEVS_URL).create(AllApiService.class);
        final Call<List<TrendingDevelopers>> trendingDevelopersCall=apiService.getTrendingDevelopers(url);
        trendingDevelopersCall.enqueue(new Callback<List<TrendingDevelopers>>() {
            @Override
            public void onResponse(Call<List<TrendingDevelopers>> call, Response<List<TrendingDevelopers>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().size() > 0){
                            trendingDevelopers.setValue(response.body());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TrendingDevelopers>> call, Throwable t) {
                trendingDevelopers.setValue(null);
            }
        });
        return trendingDevelopers;
    }
}
