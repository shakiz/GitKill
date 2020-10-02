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

public class WebRepository {
    private AllApiService apiService;
    private UtilsManager utilsManager;
    //region singleton instance
    private static WebRepository instance = null;

    public static WebRepository getInstance(){
        if (instance == null){
            instance = new WebRepository();
        }
        return instance;
    }
    //endregion

    public MutableLiveData<List<Item>> getWebRepos(Context context, String url, String queryString){
        final MutableLiveData<List<Item>> webRepos = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(ALL_TOPICS_BASE_URL).create(AllApiService.class);
        final Call<TopicBase> androidTopicCall=apiService.getAllTopics(url+"repositories",queryString);
        androidTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                if (response.isSuccessful()){
                    if (response.body().getItems() != null){
                        if (response.body().getItems().size() > 0){
                            webRepos.setValue(response.body().getItems());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
                webRepos.setValue(null);
            }
        });
        return webRepos;
    }
}
