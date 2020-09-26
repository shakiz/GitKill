package app.com.gitlib.apiutils;

import java.util.ArrayList;
import app.com.gitlib.models.alltopic.TopicBase;
import app.com.gitlib.models.details.FollowersAndFollowing;
import app.com.gitlib.models.languages.TrendingLanguage;
import app.com.gitlib.models.questionbank.QuestionBank;
import app.com.gitlib.models.repositories.TrendingRepositories;
import app.com.gitlib.models.users.TrendingDevelopers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AllApiService {
    //Call for trending repositories
    @GET
    Call<ArrayList<TrendingRepositories>> getTrendingRepos(@Url String url);

    //Call for trending languages
    @GET
    Call<ArrayList<TrendingLanguage>> getTrendingLanguages(@Url String url);

    //Call for trending developers
    @GET
    Call<ArrayList<TrendingDevelopers>> getTrendingUsers(@Url String url);

    //Call for topics android
    @GET
    Call<TopicBase> getAllTopics(@Url String url, @Query("q") String q);

    //Call for followers and following list
    @GET
    Call<ArrayList<FollowersAndFollowing>> getFollowersAndFollowing(@Url String url);

    //Call for followers and following list
    @GET
    Call<QuestionBank> getAllQuestionAndAnswer(@Url String url);
}
