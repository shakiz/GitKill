package app.com.gitlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import app.com.gitlib.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UtilsManager {
    private Context context;
    private static Retrofit retrofit = null;

    public UtilsManager(Context context) {
        this.context = context;
    }

    public Retrofit getClient(String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(builder.build())
                .build();
        return retrofit;
    }

    //region check internet connection availability
    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }
    //endregion

    //region get internet connection alert
    public static void internetErrorDialog(Context context){
        SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.showCancelButton(false);
        alert.setTitleText(context.getString(R.string.no_internet_title));
        alert.setContentText(context.getString(R.string.no_internet_message));
        alert.setConfirmText(context.getString(R.string.dialog_ok));
        alert.show();
    }
}
