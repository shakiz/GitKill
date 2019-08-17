package app.com.gitkill.apiutils;

import java.util.ArrayList;

import app.com.gitkill.pojoclasses.languages.TrendingLanguagePojo;
import app.com.gitkill.pojoclasses.repositories.TrendingRepoPojo;
import app.com.gitkill.pojoclasses.users.TrendingUserPojo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface AllApiService {
    //Call for trending repositories
    @GET
    Call<ArrayList<TrendingRepoPojo>> getTrendingRepos(@Url String url);
    //Call for trending languages
    @GET
    Call<ArrayList<TrendingLanguagePojo>> getTrendingLanguages(@Url String url);
    //Call for trending users
    @GET
    Call<ArrayList<TrendingUserPojo>> getTrendingUsers(@Url String url);
}
