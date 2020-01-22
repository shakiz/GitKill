package app.com.gitkill.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class UtilsManager {
    private Context context;

    public UtilsManager(Context context) {
        this.context = context;
    }

    public boolean checkNetworkConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo.isConnected() && networkInfo != null) return true;
        else return false;
    }

    public void loggingInterceptorForRetrofit(OkHttpClient.Builder builder){
        //Creating the logging interceptor
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        //Setting the level
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
    }
}
