package app.com.gitlib.repositories;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.models.users.TrendingDevelopersNew;
import app.com.gitlib.models.users.UserBase;
import app.com.gitlib.utils.UtilsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static app.com.gitlib.apiutils.AllUrlClass.ALL_TOPICS_BASE_URL;

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

    public MutableLiveData<List<TrendingDevelopersNew>> getTrendingRepos(Context context, String url){
        final MutableLiveData<List<TrendingDevelopersNew>> trendingDevelopers = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(ALL_TOPICS_BASE_URL).create(AllApiService.class);
        final Call<UserBase> trendingDevelopersCall = apiService.getTrendingDevelopers("users?",url);
        trendingDevelopersCall.enqueue(new Callback<UserBase>() {
            @Override
            public void onResponse(Call<UserBase> call, Response<UserBase> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().getItems() != null){
                            trendingDevelopers.setValue(response.body().getItems());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserBase> call, Throwable t) {
                trendingDevelopers.setValue(null);
            }
        });
        return trendingDevelopers;
    }
}
