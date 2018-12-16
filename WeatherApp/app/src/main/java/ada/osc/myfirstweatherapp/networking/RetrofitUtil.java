package ada.osc.myfirstweatherapp.networking;

import ada.osc.myfirstweatherapp.util.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    public static Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .build();
    }

    private static Interceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideLoggingInterceptor())
                .build();
    }
}
