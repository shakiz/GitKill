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

public class MachineLearningRepository {
    private AllApiService apiService;
    private UtilsManager utilsManager;
    //region singleton instance
    private static MachineLearningRepository instance = null;

    public static MachineLearningRepository getInstance(){
        if (instance == null){
            instance = new MachineLearningRepository();
        }
        return instance;
    }
    //endregion

    public MutableLiveData<List<Item>> getMLRepos(Context context, String url, String queryString){
        final MutableLiveData<List<Item>> mlRepos = new MutableLiveData<>();
        utilsManager = new UtilsManager(context);
        apiService = utilsManager.getClient(ALL_TOPICS_BASE_URL).create(AllApiService.class);
        final Call<TopicBase> mlTopicCall=apiService.getAllTopics(url+"repositories",queryString);
        mlTopicCall.enqueue(new Callback<TopicBase>() {
            @Override
            public void onResponse(Call<TopicBase> call, Response<TopicBase> response) {
                if (response.isSuccessful()){
                    if (response.body().getItems() != null){
                        if (response.body().getItems().size() > 0){
                            mlRepos.setValue(response.body().getItems());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TopicBase> call, Throwable t) {
                mlRepos.setValue(null);
            }
        });
        return mlRepos;
    }
}
