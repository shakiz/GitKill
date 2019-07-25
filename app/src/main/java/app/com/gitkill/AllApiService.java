package app.com.gitkill;

import app.com.gitkill.pojoclasses.languages.TrendingLanguagePojo;
import app.com.gitkill.pojoclasses.repositories.TrendingRepoPojo;
import app.com.gitkill.pojoclasses.users.TrendingUserPojo;
import retrofit2.Call;
import retrofit2.http.Url;

public interface AllApiService {
    //Call for trending repositories
    Call<TrendingRepoPojo> getTrendingRepos(@Url String url);
    //Call for trending languages
    Call<TrendingLanguagePojo> getTrendingLanguages(@Url String url);
    //Call for trending users
    Call<TrendingUserPojo> getTrendingUsers(@Url String url);
}
