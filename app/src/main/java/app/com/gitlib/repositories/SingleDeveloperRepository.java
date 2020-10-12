package app.com.gitlib.repositories;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import app.com.gitlib.apiutils.AllApiService;
import app.com.gitlib.models.users.Developer;
import app.com.gitlib.utils.UtilsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static app.com.gitlib.apiutils.AllUrlClass.SINGLE_USER_URL;

public class SingleDeveloperRepository {
    private static SingleDeveloperRepository instance = null;
    private UtilsManager utilsManager;
    private AllApiService apiService;

    public static SingleDeveloperRepository getInstance(){
        if (instance == null){
            instance = new SingleDeveloperRepository();
        }
        return instance;
    }

    public MutableLiveData<Developer> getDeveloper(Context context, String url){
        final MutableLiveData<Developer> developer = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(SINGLE_USER_URL).create(AllApiService.class);
        final Call<Developer> trendingDevelopersCall = apiService.getSingleUser(url);
        trendingDevelopersCall.enqueue(new Callback<Developer>() {
            @Override
            public void onResponse(Call<Developer> call, Response<Developer> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        developer.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Developer> call, Throwable t) {
                developer.setValue(null);
            }
        });
        return developer;
    }
}
