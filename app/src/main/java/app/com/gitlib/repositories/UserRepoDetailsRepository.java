package app.com.gitlib.repositories;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.models.repositories.Repo;
import app.com.gitlib.utils.UtilsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static app.com.gitlib.apiutils.AllUrlClass.SINGLE_USER_PROFILE_AND_REPOSITORIES_URL;

public class UserRepoDetailsRepository {
    private static UserRepoDetailsRepository instance = null;
    private UtilsManager utilsManager;
    private AllApiService apiService;

    public static UserRepoDetailsRepository getInstance(){
        if (instance == null){
            instance = new UserRepoDetailsRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Repo>> getAllRepositories(Context context, String url){
        final MutableLiveData<List<Repo>> repositories = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(SINGLE_USER_PROFILE_AND_REPOSITORIES_URL).create(AllApiService.class);
        final Call<List<Repo>> trendingDevelopersCall = apiService.getAllRepositories(url);
        trendingDevelopersCall.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        repositories.setValue(response.body());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                repositories.setValue(null);
            }
        });
        return repositories;
    }
}
